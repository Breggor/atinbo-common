package com.atinbo.mvc.exception;

import com.atinbo.core.exception.HttpAPIException;
import com.atinbo.core.http.model.ErrResult;
import com.atinbo.core.http.status.ErrorType;
import com.atinbo.core.http.status.HttpStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


/**
 * SpringMVC 统一拦截
 *
 * @author breggor
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptoinHandler {

    /**
     * 请求参数无法验证异常拦截
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrResult bindException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return doErr(HttpStatusCode.ERR_400, e.getBindingResult());
    }


    /**
     * 请求参数无法绑定异常拦截
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrResult handleBindException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return ErrResult.error(HttpStatusCode.ERR_400.getHttpCode(), ErrorType.DATA_INVALID.getLabel());
    }


    /**
     * 自定义HttpAPI异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpAPIException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrResult handleBizRuntimeException(HttpAPIException e) {
        log.error(e.getMessage(), e);
        return new ErrResult(HttpStatusCode.ERR_500);
    }

    /**
     * 系统内部运行期异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrResult handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ErrResult(HttpStatusCode.ERR_500);
    }


    private ErrResult doErr(HttpStatusCode code, BindingResult br) {
        Map<String, String> errs = new HashMap<>();
        for (FieldError fieldError : br.getFieldErrors()) {
            errs.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ErrResult.error(code, errs);
    }
}
