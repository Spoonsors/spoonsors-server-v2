package com.spoonsors.spoonsorsserver.customException;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001"),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0002"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0003"),
    //사용자 정의
    MANAGER01(HttpStatus.FORBIDDEN, "MANAGER-01","이미 등록되어 있는 식재료입니다."),
    MANAGER02(HttpStatus.FORBIDDEN, "MANAGER-02","후원 등록되어 있는 식재료입니다."),
    MANAGER03(HttpStatus.BAD_REQUEST, "MANAGER-03","검색한 식재료가 존재하지 않습니다."),
    MANAGER04(HttpStatus.BAD_REQUEST, "MANAGER-04","증명서 등록 상태 변경을 실패하였습니다."),

    REVIEW01(HttpStatus.FORBIDDEN, "REVIEW-01","후원 마감되지 않은 글은 리뷰 작성 불가능 합니다."),

    OPENAPI01(HttpStatus.BAD_REQUEST, "OPENAPI-01","입력값을 확인해주세요."),
    OPENAPI02(HttpStatus.FORBIDDEN, "OPENAPI-02","유효 호출건수를 이미 초과하셨습니다."),
    OPENAPI03(HttpStatus.BAD_REQUEST, "OPENAPI-03","해당하는 데이터가 없습니다."),
    OPENAPI04(HttpStatus.BAD_REQUEST, "OPENAPI-04","파싱 불가능한 데이터 입니다."),

    AUTHORIZE01(HttpStatus.BAD_REQUEST, "AUTHORIZE-01","인증번호가 맞지 않습니다."),
    AUTHORIZE02(HttpStatus.BAD_REQUEST, "AUTHORIZE-02","세션이 만료되었습니다."),

    SPON01(HttpStatus.FORBIDDEN, "SPON-01","이미 후원이 완료된 물품입니다."),

    POST01(HttpStatus.FORBIDDEN, "POST-01", "인증이 되지 않은 사용자입니다."),
    POST02(HttpStatus.FORBIDDEN, "POST-02", "후원 등록 가능 상태가 아닙니다."),
    POST03(HttpStatus.FORBIDDEN, "POST-03", "후원 상품이 없는 글은 마감 불가능합니다."),
    POST04(HttpStatus.FORBIDDEN, "POST-04", "후원 완료된 글은 삭제 불가능 합니다."),
    POST05(HttpStatus.FORBIDDEN, "POST-05", "후원된 상품이 있는 글은 삭제 불가능 합니다."),
    POST06(HttpStatus.FORBIDDEN, "POST-06", "리뷰가 있는 글은 상태 변경 불가능 합니다."),

    JOIN01(HttpStatus.BAD_REQUEST, "JOIN-01", "이미 존재하는 아이디입니다."),
    JOIN02(HttpStatus.BAD_REQUEST, "JOIN-02", "이미 존재하는 닉네임입니다."),
    JOIN03(HttpStatus.BAD_REQUEST, "JOIN-03", "비밀번호가 일치하지 않습니다."),

    LOGIN01(HttpStatus.BAD_REQUEST, "LOGIN-01", "이름과 번호가 일치하는 아이디가 없습니다."),
    LOGIN02(HttpStatus.BAD_REQUEST, "LOGIN-02", "일치하지 않는 정보입니다."),
    LOGIN03(HttpStatus.BAD_REQUEST, "LOGIN-03", "등록된 아이디가 없습니다."),
    LOGIN04(HttpStatus.BAD_REQUEST, "LOGIN-04", "비밀번호 변경 실패"),
    LOGIN05(HttpStatus.BAD_REQUEST, "LOGIN-05", "아이디 또는 비밀번호가 일치하지 않습니다."),

    PAY01(HttpStatus.BAD_GATEWAY, "PAY-01", "결제 요청 실패"),
    PAY02(HttpStatus.BAD_GATEWAY, "PAY-02", "결제 실패"),

    PUSH01(HttpStatus.BAD_REQUEST, "PUSH-01", "state가 올바르지 않습니다."),

    MEALPLANNERNOTFOUND(HttpStatus.NOT_FOUND, "MEALPLANNER-01", "식단을 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }


}
