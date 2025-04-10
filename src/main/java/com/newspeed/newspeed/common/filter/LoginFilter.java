package com.newspeed.newspeed.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.common.response.ApiResponseDto;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter("/api/*")  // 모든 API 요청에 대해 필터링
public class LoginFilter implements Filter {

    // 필터 초기화 작업
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // // 회원가입, 로그인은 필터링 제외
        String uri = httpServletRequest.getRequestURI();
        if (uri.contains("/signup") || uri.contains("/login")) {
            chain.doFilter(servletRequest, servletResponse);  // 회원가입 경로는 필터링 하지 않음
            return;
        }

        // 세션에서 사용자 정보 확인
        HttpSession session = httpServletRequest.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            // 인증된 사용자가 있으면 요청을 계속 진행
            chain.doFilter(servletRequest, servletResponse);
        } else {
            // 인증 실패 응답 구성 - HttpServletResponse에 직접 JSON 바디를 작성하는 방법
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.setContentType("application/json;charset=UTF-8");

            ApiResponseDto<?> responseBody = ApiResponseDto.fail(
                    ErrorCode.UNAUTHORIZED,
                    httpServletRequest.getRequestURI()
            );

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(responseBody);

            httpServletResponse.getWriter().write(json);
        }
    }

    @Override
    public void destroy() {}
}
