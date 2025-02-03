package com.example.schedule.exception.custom;

import com.example.schedule.exception.code.CustomErrorCode;

public class AuthorNotFoundException extends CustomException {
    public AuthorNotFoundException(Long authorId) {
        super(CustomErrorCode.AUTHOR_NOT_FOUND, "작성자가 존재하지 않습니다. author_id = " + authorId);
    }
}
