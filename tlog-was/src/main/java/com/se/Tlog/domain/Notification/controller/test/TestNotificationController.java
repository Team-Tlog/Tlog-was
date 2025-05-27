package com.se.Tlog.domain.Notification.controller.test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.se.Tlog.domain.Notification.domain.MessageGenerator;
import com.se.Tlog.domain.Notification.repository.FcmWrapper;
import com.se.Tlog.domain.Notification.repository.NotificationUtil;
import com.se.Tlog.domain.Notification.repository.dto.FcmMessageDto;
import com.se.Tlog.domain.Notification.repository.dto.FcmRequestDto;
import com.se.Tlog.domain.Notification.repository.jpa.FcmTokenRepository;
import com.se.Tlog.domain.Notification.repository.jpa.entity.UserFcmTokenJpaEntity;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessRes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Controller
@RequiredArgsConstructor
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true")
// @Profile("dev") // 현재 활성화 기준이 Swagger 임을 감안.
@RequestMapping("/api/test/notify")
@Tag(name = "알림")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class TestNotificationController {
	private final NotificationUtil notificationUtil;
	private final FcmWrapper fcmWrapper;
	private final FcmTokenRepository repository;
	private final MessageGenerator messageGenerator;
	
	@PostMapping("/by-user-id")
	@Operation (
			summary = "[개발환경 전용] FCM 테스트 - UserId",
    		description = "[개발환경 전용] FCM 기능을 테스트하는 API입니다."
    				+ "<br/>"
    				+ "<br/> User 및 FcmToken은 DB에 별도 등록 필요."
    				+ "<br/> 요청 처리 결과를 반환합니다. (실패 사유 : 유저 또는 FCM 토큰이 DB에 등록되지 않음)"
    				+ "<br/>"
    				+ "<br/> 다음의 3가지 기능을 제공합니다 : "
    				+ "<br/> - 한 유저에게 단일 메세지 전송 : 1 userId, 1 message일 때"
    				+ "<br/> - 여러 유저에게 단일 메세지 전송 : N userId, 1 message일 때"
    				+ "<br/> - 여러 유저에게 여러 메세지 전송(메세지 수 만큼 전송합니다.) : N userId, M message일 때",
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<String>> testFcmNotificationByUserId(@RequestBody TestFcmMessageByUserIdDto request) {
		if (request.users().size() == 0)
			return ResponseEntity.ok(SuccessRes.from("전송할 유저가 등록되지 않았습니다."));

		// TestDto to FcmDto
		List<FcmRequestDto> messages = new ArrayList<FcmRequestDto>();
		for (int i = 0; i < request.messages().size(); i++) {
			TestFcmMessageDto message = request.messages().get(i);
		    UUID userId = request.users().get(Math.min(i, request.users().size() - 1));
			messages.add(
					new FcmRequestDto(
							userId,
							message.payloads()));
		}
		
		if (request.users().size() == 1 && messages.size() == 1) {
			boolean requested = notificationUtil.sendNotification(messages.get(0));
			UserFcmTokenJpaEntity entity = repository.findByUserId(messages.get(0).getUserId());
			log.info("Send single message : " + (entity != null ? entity.getFcmToken() : "user not found"));
			
			return ResponseEntity.ok(SuccessRes.from("처리 결과 : " + (requested?"1":"0") + "/1"));
		} else if (messages.size() == 1) {
			int requestedCount = notificationUtil.sendNotifications(request.users(), messages.get(0));
			log.info("Send same multiple messages");
			
			return ResponseEntity.ok(SuccessRes.from("처리 결과 : " + requestedCount + "/" + request.users().size()));
		} else {
			int requestedCount = notificationUtil.sendNotifications(messages);
			log.info("Send another multiple messages");
			
			return ResponseEntity.ok(SuccessRes.from("처리 결과 : " + requestedCount + "/" + messages.size()));
		}
	}
	
	@PostMapping("/by-token")
	@Operation (
			summary = "[개발환경 전용] FCM 테스트 - FcmToken",
    		description = "[개발환경 전용] FCM 기능을 테스트하는 API입니다."
    				+ "<br/>"
    				+ "<br/> 요청 처리 결과를 반환합니다. (실패 사유 : 토큰이 유효하지 않음)"
    				+ "<br/>"
    				+ "<br/> 다음의 3가지 기능을 제공합니다 : "
    				+ "<br/> - 한 클라이언트에게 단일 메세지 전송 : 1 token, 1 message일 때"
    				+ "<br/> - 여러 클라이언트에게 단일 메세지 전송 : N token, 1 message일 때"
    				+ "<br/> - 여러 클라이언트에게 여러 메세지 전송(메세지 수 만큼 전송합니다.) : N token, M message일 때",
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<String>> testFcmNotificationByToken(@RequestBody TestFcmMessageByTokenDto request) {
		if (request.tokens().size() == 0)
			return ResponseEntity.ok(SuccessRes.from("전송할 토큰이 등록되지 않았습니다."));
		
		// TestDto to FcmDto
		List<FcmMessageDto> messages = new ArrayList<FcmMessageDto>();
		for (int i = 0; i < request.messages().size(); i++) {
			TestFcmMessageDto message = request.messages().get(i);
			String token = request.tokens().get(Math.min(i, request.tokens().size() - 1));
			messages.add(
					new FcmMessageDto(
							token,
							message.payloads()));
		}
		
		if (request.tokens().size() == 1 && messages.size() == 1) {
			try {
				boolean successed = fcmWrapper.sendFcmMessage(messages.get(0)).get();	
				log.info("Send single message : " + messages.get(0).getFcmToken());
				return ResponseEntity.ok(SuccessRes.from("처리 결과 : " + (successed?1:0) + "/1"));
			} catch (Exception e) {
				log.info("Send single message : fail");
				return ResponseEntity.ok(SuccessRes.from("처리 결과 : 0/1"));
			}
		} else if (messages.size() == 1) {
			try {
				int successedCount = fcmWrapper.sendFcmMessages(request.tokens(), messages.get(0)).get();
				log.info("Send same multiple messages");
				return ResponseEntity.ok(SuccessRes.from("처리 결과 : " + successedCount + "/" + request.tokens().size()));
			} catch (Exception e) {
				log.info("Send same multiple messages : Fail");
				return ResponseEntity.ok(SuccessRes.from("처리 결과 : 0/" + request.tokens().size()));
			}
		} else {
			try {
				int successedCount = fcmWrapper.sendFcmMessages(messages).get();
				log.info("Send another multiple messages");
				return ResponseEntity.ok(SuccessRes.from("처리 결과 : " + successedCount + "/" + messages.size()));
			} catch (Exception e) {
				log.info("Send another multiple messages : Fail");
				return ResponseEntity.ok(SuccessRes.from("처리 결과 : 0/" + messages.size()));
			}
		}
	}
	
    @PostMapping("/send-push-message")
    @Operation (
            summary = "[개발환경 전용] 알림 테스트 (Tlog 메시지 규격에 맞춰 동작)",
            description = "[개발환경 전용] FCM 기능을 테스트하는 API입니다."
                        + "<br> 한 알림을 여러 클라이언트에게 전송합니다."
                        + "<br>"
                        + "<br> - 페이로드는 메시지 규격에 맞아야 합니다."
                        + "<br> - 토큰이 유효해 전송에 성공한 수를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<String>> testDefaultNotification(@RequestBody TestNotificationDto request) {
        if (request.tokens().size() == 0)
            return ResponseEntity.ok(SuccessRes.from("전송할 토큰이 등록되지 않았습니다."));
        
        FcmRequestDto parsedMessage = messageGenerator.getMessage(UUID.randomUUID(), request.data());
        List<FcmMessageDto> validMessages = request.tokens().stream()
                .filter(token -> (null != token))
                .map(token -> new FcmMessageDto(token, parsedMessage.getPayload()))
                .toList();
        
        int successCnt = 0;
        try {
            successCnt = fcmWrapper.sendFcmMessages(validMessages).get();
        } catch (Exception e) {
            
        }
        return ResponseEntity.ok(SuccessRes.from("처리 결과 : " + request.tokens().size() + "/" + successCnt));
    }
}
