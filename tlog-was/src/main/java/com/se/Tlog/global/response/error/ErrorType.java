package com.se.Tlog.global.response.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    // 400 잘못된 요청
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "null값 또는 잘못된 값이 입력되었습니다."),
    CONTENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "Content 내용이 비어있습니다."),
    TEAM_NAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "팀 이름이 비어있습니다."),
    TEAM_CANNOT_BE_ORPHAN(HttpStatus.BAD_REQUEST, "팀에 최소 1명 이상의 팀원이 필요합니다."),
    CANNOT_REMOVE_TEAM_LEADER(HttpStatus.BAD_REQUEST, "팀장을 퇴출시킬 수 없습니다."),
    ROLE_MISMATCH(HttpStatus.BAD_REQUEST,"Role 값을 잘못 입력하였습니다."),
    INVALID_FIREBASE_TOKEN(HttpStatus.BAD_REQUEST,"클라이언트 FCM Token이 유효하지 않습니다!"),
    SCRAP_UNSUPPORTED_TO_TEAM(HttpStatus.BAD_REQUEST, "팀에는 스크랩 기능을 지원하지 않습니다!"),
    INVITE_USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "초대하려는 유저 중 존재하지 않는 유저가 있습니다."),
    LOGOUT_FAILED(HttpStatus.BAD_REQUEST,"로그아웃에 실패하였습니다." ),
    INVALID_REVIEW_RATING(HttpStatus.BAD_REQUEST, "리뷰 평점은 1점 이상 5점 이하여야 합니다."),
    INVALID_IMAGE_URL(HttpStatus.BAD_REQUEST, "잘못된 이미지 URL 입니다." ),
    // 사용자로부터 소셜 로그인 인증 실패
    SSO_LOGIN_FAIL(HttpStatus.BAD_REQUEST,"외부 소셜 로그인이 취소되거나 실패했습니다."),
    
    // 보상 조건
    INVALID_REWARD_CRITERIA(HttpStatus.BAD_REQUEST,"제시된 보상 조건이 올바르지 않은 형식입니다."),
    NOT_FIT_ON_CRITERIA(HttpStatus.BAD_REQUEST, "사용자가 해당 보상 조건을 충족하지 않았습니다."),
    
    // 코스 관련 검증
    INVALID_COURSE_OWNER(HttpStatus.BAD_REQUEST,"여행 코스 소유자가 잘못 기입되었습니다."),
    INVALID_COURSE_DURATION(HttpStatus.BAD_REQUEST,"여행 코스의 기간이 잘못 기입되었습니다."),
    INVALID_COURSE_DATE(HttpStatus.BAD_REQUEST,"여행지를 등록하려는 날짜가 여행 코스의 기간을 벗어납니다."),
    
    // 어드민 관련 검증
    ALREADY_APPROVED_DESTINATION(HttpStatus.BAD_REQUEST, "해당 여행지는 이미 관리자의 검수가 완료되었습니다."),

    // FCM 메시지 형식 오류
    NULL_IN_FCM_MESSAGE(HttpStatus.BAD_REQUEST, "FCM 수신자 또는 데이터에 null은 허용되지 않습니다."),
    INVALID_TLOG_MESSAGE(HttpStatus.BAD_REQUEST, "Tlog 메시지 규격에 맞지 않습니다."),
    INVALID_TLOG_MESSAGE_TYPE(HttpStatus.BAD_REQUEST, "Tlog 알림 타입이 없거나 잘못된 값입니다."),
    INVALID_LINK_MESSAGE_TYPE(HttpStatus.BAD_REQUEST, "링크 알림 타입이 없거나 잘못된 값입니다."),

    // 인증
    // 401
    UN_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "인증이 실패되었습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인 실패입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다." ),
    KAKAO_AUTH_FAIL(HttpStatus.UNAUTHORIZED, "카카오 인증이 실패되었습니다."),
    GOOGLE_AUTH_FAIL(HttpStatus.UNAUTHORIZED, "구글 인증이 실패되었습니다."),
    // 인가
    // 403
    UN_AUTHORIZATION(HttpStatus.FORBIDDEN, "허용되지 않은 접근입니다."),

    //데이터
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 데이터 입니다."),
    NOT_FOUND_TAG(HttpStatus.NOT_FOUND, "존재하지 않는 태그 입니다."),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "역할이 존재하지 않습니다."),
    INVALID_TBTI_CATEGORY(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 사용자 입니다."),
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 관리자 입니다."),
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 채팅방 입니다."),
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않은 메시지 입니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 팀입니다."),
    TEAM_USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 팀원입니다."),
    INVALID_DESTINATION(HttpStatus.NOT_FOUND, "존재하지 않는 구독경로입니다."),
    DESTINATION_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 여행지입니다."),
    SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 여행지가 스크랩 목록에 없습니다."),
    SHOPCART_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 여행지가 장바구니에 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 리뷰를 찾을 수 없습니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 여행 코스입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 코스 리뷰(게시글)입니다."),
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    //데이터 충돌
    ALREADY_EXISTS_SNSId(HttpStatus.CONFLICT, "이미 존재하는 Id 입니다."),
    ALREADY_EXISTS_TAG(HttpStatus.CONFLICT, "이미 존재하는 태그입니다."),
    ALREADY_EXISTS_DESTINATION(HttpStatus.CONFLICT, "이미 존재하는 여행지입니다."),
    ALREADY_OWN_REWARD(HttpStatus.CONFLICT, "이미 보유하고 있는 보상입니다."),
    ALREADY_EXIST_IN_TEAM(HttpStatus.CONFLICT, "이미 팀에 속해있는 유저입니다."),
    // 서버 에러
    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러. 서버 팀으로 연락주시기 바랍니다."),
    NO_MORE_SPACE_FOR_INVITE_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러. 팀 초대 코드를 더 이상 생성할 수 없습니다."),
    INTERNAL_ERROR_BY_INVITE_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러. 팀 초대 코드 에러."),
    FIREBASE_INITIALIZE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러. Firebase 라이브러리 초기화 에러."),
    FIREBASE_INITIALIZE_FAIL_KEY_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러. Firebase Key 파일을 찾지 못했습니다."),
    BLACKLIST_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"블랙리스트 등록에 실패했습니다."),
    FIREBASE_CUSTOM_TOKEN_ISSUE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Firebase 커스텀 토큰 발급 실패"),
    // 외부 소셜 로그인 처리 중 에러
    SSO_ACCESSTOKEN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "외부 인증 서버로부터 인증을 받는데 실패했습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    // 501 구현되지 않은 기능
    UNSUPPORTED_SSO_LOGIN(HttpStatus.NOT_IMPLEMENTED, "현재 해당 소셜 로그인 방식은 아직 지원되지 않습니다."),
  	UNSUPPORTED_REWARD_CRITERIA(HttpStatus.NOT_IMPLEMENTED, "현재 해당 보상 기준은 아직 지원되지 않습니다.");


    private final HttpStatus status;
    private final String message;

    public int getStatusCode (){ return status.value();}

}
