package com.bezkoder.springjwt.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseWithMessage<T> {
    private T item;
    private String message;

    public ResponseWithMessage(T item, String message) {
        this.item = item;
        this.message = message;
    }
}
