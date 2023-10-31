package com.clyl.cloudlaw.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xiugou798
 */
@Data
@AllArgsConstructor
public class RestBean<T> {

    Integer code;

    String message;

    T data;

    public RestBean(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
