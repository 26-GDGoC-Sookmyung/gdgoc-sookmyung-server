package com.gdgoc.website.global.security;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller 메서드에서 현재 로그인한 사용자의 정보를 CurrentUser 객체로 주입받는 어노테이션
 * 사용 예시: @AuthUser CurrentUser currentUser
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(hidden = true)
public @interface AuthUser {
}