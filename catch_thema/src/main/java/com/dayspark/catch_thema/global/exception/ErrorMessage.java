package com.dayspark.catch_thema.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    // Common
    SERVER_UNTRACKED_ERROR("미등록 서버 에러입니다. 서버 팀에 연락주세요."),
    OBJECT_NOT_FOUND("조회된 객체가 없습니다."),
    INVALID_PARAMETER("잘못된 파라미터입니다."),
    PARAMETER_VALIDATION_ERROR("파라미터 검증 에러입니다."),
    PARAMETER_GRAMMAR_ERROR("파라미터 문법 에러입니다."),

    // Auth
    UNAUTHORIZED("인증 자격이 없습니다."),
    FORBIDDEN("권한이 없습니다."),
    JWT_ERROR_TOKEN("잘못된 토큰입니다."),
    JWT_EXPIRE_TOKEN("만료된 토큰입니다."),
    AUTHORIZED_ERROR("인증 과정 중 에러가 발생했습니다."),
    JWT_UNMATCHED_CLAIMS("토큰 인증 정보가 일치하지 않습니다"),

    // User
    USER_ALREADY_EXIST("이미 회원가입된 유저입니다."),
    USER_NOT_EXIST("존재하지 않는 유저입니다."),
    USER_WRONG_PASSWORD("비밀번호가 틀렸습니다."),

    ;
    private final String message;
}


