package com.newspeed.newspeed.common.exception.base;

import com.newspeed.newspeed.common.exception.code.enums.ErrorCode;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
