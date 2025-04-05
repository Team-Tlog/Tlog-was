package com.se.Tlog.domain.Notification.repository;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.se.Tlog.domain.Notification.repository.dto.FcmKeyValuePairDto;
import com.se.Tlog.domain.Notification.repository.dto.FcmMessageDto;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@Component
public class FcmWrapper implements InitializingBean {
	@Autowired
	private InvalidTokenHandler invalidTokenHandler;
	
	@Value("${SERVICE_ACCOUNT_KEY_PATH}")
	private String SERVICE_ACCOUNT_KEY_PATH;

	@Override
	public void afterPropertiesSet() throws Exception {
		initializeFirebaseApp();
		log.info("FCM Initialize Done");
	}

	private static final int MAX_MESSAGE_COUNT = 500;
	
	private void initializeFirebaseApp() {
		try {
			FileInputStream serviceAccount = new FileInputStream(SERVICE_ACCOUNT_KEY_PATH);
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();

			FirebaseApp.initializeApp(options);
		} catch (Exception e) {
			throw new CustomException(ErrorType.FIREBASE_INITIALIZE_FAIL);
		}		
	}
	
	public boolean isValidToken(String firebaseToken) {
		Message message = Message.builder()
			    .putData("dummyData", "dummy")
			    .setToken(firebaseToken)
			    .build();

		try {
			FirebaseMessaging.getInstance().send(message, true);
			return true;
		} catch (FirebaseMessagingException e) {
			return false;
		} catch (Exception e) {
			throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR);
		}
	}
	
	private void handleExternalException(Exception e) {
		log.error("fail to send FCM message, without firebase messaging system.", e);
	}
	
	private void handleFcmException(FirebaseMessagingException e) {
		if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED
				|| e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
			log.error("fail to send FCM message, invalid token found, Error code : " + e.getMessagingErrorCode().toString());
			// 유효하지 않은 토큰 삭제 처리
			//invalidTokenHandler.handle();
		}
		else
			log.error("fail to send FCM message, Error code : " + e.getMessagingErrorCode().toString(), e);
	}

	private Message toMessage(FcmMessageDto dto) {
		Message.Builder messageBuilder = Message.builder();
		messageBuilder.setToken(dto.fcmToken());
		for (FcmKeyValuePairDto payload : dto.payload())
			messageBuilder.putData(payload.key(), payload.value());
		
		return messageBuilder.build();
	}
	
	/**
	 * Firebase에서 허용하는 단일 최대 전송 수량에 맞추어 복수의 메세지를 분할합니다.
	 * @return
	 */
	private List<List<Message>> separatedMessages(List<FcmMessageDto> messagesDto) {
		List<List<Message>> separatedMessages = new ArrayList<List<Message>>();
		
		int messageCount = 0;
		for (int partition = 0; partition <= (messagesDto.size()-1)/MAX_MESSAGE_COUNT; partition++) {
			List<Message> messages = new ArrayList<Message>();
			while (messageCount < messagesDto.size() && messages.size() < MAX_MESSAGE_COUNT)
				messages.add(toMessage(messagesDto.get(messageCount++)));
			
			separatedMessages.add(messages);
		}
		
		return separatedMessages;
	}
	
	/**
	 * Firebase에서 허용하는 단일 최대 전송 수량에 맞추어 복수의 메세지를 분할합니다.
	 * @return
	 */
	private List<MulticastMessage> separatedMessages(List<String> tokens, FcmMessageDto messageDto) {
		List<MulticastMessage> separatedMessages = new ArrayList<MulticastMessage>();
		
		for (int partition = 0; partition <= (tokens.size()-1)/MAX_MESSAGE_COUNT; partition++) {
			MulticastMessage.Builder messageBuilder = MulticastMessage.builder();
			messageBuilder.addAllTokens(
					tokens.subList(
							partition * MAX_MESSAGE_COUNT, 
							Math.min((partition + 1) * MAX_MESSAGE_COUNT, tokens.size())));
			for (FcmKeyValuePairDto payload : messageDto.payload())
				messageBuilder.putData(payload.key(), payload.value());
			
			separatedMessages.add(messageBuilder.build());
		}
		
		return separatedMessages;
	}
	
	/**
	 * 단일 푸시 메세지를 전송할 때 사용합니다.
	 * <br/> 메세지 전송 성공 여부를 반환합니다.
	 * <br/> 여러 메세지를 일괄 전송할 경우, <code>sendFcmMessages()</code>를 사용하는 것이 권장됩니다.
	 * @param messageDto
	 */
	@Async
	public CompletableFuture<Boolean> sendFcmMessage(FcmMessageDto messageDto) {
		try {
			FirebaseMessaging.getInstance().send(toMessage(messageDto));
			return CompletableFuture.completedFuture(true);
		} catch (FirebaseMessagingException e) {
			handleFcmException(e);
		} catch (Exception e) {
			handleExternalException(e);
		}
		return CompletableFuture.completedFuture(false);
	}
	
	/**
	 * 동일 내용의 푸시 메세지를 여러 클라이언트에 전송할 때 사용합니다.
	 * <br/> 메세지 전송 성공 수를 반환합니다.
	 * <br/><code>messageDto.fcmToken()</code>는 사용되지 않습니다.
	 */
	@Async
	public CompletableFuture<Integer> sendFcmMessages(List<String> tokens, FcmMessageDto messageDto) {
		int successCount = 0;
		for (MulticastMessage messages : separatedMessages(tokens, messageDto)) {
			try {
				BatchResponse result = FirebaseMessaging.getInstance().sendEachForMulticast(messages);
				if (result.getFailureCount() > 0) {
					result.getResponses().forEach((response) -> {
						if (!response.isSuccessful()) 
							handleFcmException(response.getException());
					});
				}
				successCount += result.getSuccessCount();
			} catch (FirebaseMessagingException e) {
				handleFcmException(e);
			} catch (Exception e) {
				handleExternalException(e);
			}
		}
		return CompletableFuture.completedFuture(successCount);
	}
	
	/**
	 * 서로 다른 내용의 여러 메세지를 전송할 때 사용합니다.
	 * <br/> 메세지 전송 성공 수를 반환합니다.
	 * @param messagesDto
	 */
	@Async
	public CompletableFuture<Integer> sendFcmMessages(List<FcmMessageDto> messagesDto) {
		int successCount = 0;
		for (List<Message> messages : separatedMessages(messagesDto)) {
			try {
				BatchResponse result = FirebaseMessaging.getInstance().sendEach(messages);
				if (result.getFailureCount() > 0) {
					result.getResponses().forEach((response) -> {
						if (!response.isSuccessful())
							handleFcmException(response.getException());
					});
				}
				successCount += result.getSuccessCount();
			} catch (FirebaseMessagingException e) {
				handleFcmException(e);
			} catch (Exception e) {
				handleExternalException(e);
			}
		}
		return CompletableFuture.completedFuture(successCount);
	}
}
