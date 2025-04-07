package com.newspeed.newspeed.common.exception.base;


import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;

public class BadRequestException extends CustomException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
