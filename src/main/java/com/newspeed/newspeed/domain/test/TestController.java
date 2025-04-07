package com.newspeed.newspeed.domain.test;

import com.newspeed.newspeed.common.exception.base.CustomException;
import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;
import com.newspeed.newspeed.common.exception.code.enums.SuccessCode;
import com.newspeed.newspeed.common.response.ApiResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // 테스트 예외 처리
    @GetMapping("/api/test/error")
    public void throwTestError() {
        throw new CustomException(ErrorCode.TEST_ERROR);
    }

    //  테스트 성공 응답
    @GetMapping("/api/test/success")
    public ApiResponseDto<String> testSuccess() {
        String mockData = "성공적으로 동작했습니다!";
        return ApiResponseDto.success(SuccessCode.GENERAL_SUCCESS, mockData, "/api/test/success");
    }
}
