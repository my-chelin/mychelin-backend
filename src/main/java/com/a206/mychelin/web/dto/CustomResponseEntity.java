package com.a206.mychelin.web.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomResponseEntity extends ResponseEntity {

    public CustomResponseEntity(HttpStatus status) {
        super(status);
    }
}
