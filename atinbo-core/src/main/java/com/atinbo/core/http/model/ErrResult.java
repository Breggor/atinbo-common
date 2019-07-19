package com.atinbo.core.http.model;


import com.atinbo.core.http.status.HttpStatusCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 错误信息返回封装
 *
 * @author breggor
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ErrResult implements Serializable {

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 信息
     */
    private String message;
    /**
     * 错误信息列表
     */
    private List<ErrorInfo> errors;


    public ErrResult(HttpStatusCode error) {
        this.message = error.getMessage();
        this.code = error.getHttpCode();
    }

    public static ErrResult error(Map<String, String> errs) {
        return error(null, errs);
    }

    public static ErrResult error(String message) {
        ErrResult result = new ErrResult();
        result.setMessage(message);
        return result;
    }

    public static ErrResult error(HttpStatusCode statusCode, Map<String, String> errs) {
        ErrResult result = new ErrResult();
        result.setCode(statusCode.getHttpCode());
        result.setMessage(statusCode.getMessage());
        if (null != errs && !errs.isEmpty()) {
            errs.entrySet().stream().forEach(obj -> result.addError(obj.getKey(), obj.getValue()));
        }
        return result;
    }

    public void addError(String message, String reason) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ErrorInfo().setMessage(message).setReason(reason));
    }
}