package com.neo.qiaoqiaochat.websocket.exception;

import com.neo.qiaoqiaochat.websocket.model.ResultCode;
import com.neo.qiaoqiaochat.websocket.model.SimpleResult;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


/**
 * controller 增强
 * @author Neo Lin
 * @date  2020/4/5
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(SQLException.class)
    public
    @ResponseBody
    SimpleResult handleSQLException(HttpServletRequest request, Exception ex) {
        logger.error("Sql Error:"+ex.getMessage());
        SimpleResult rtn = new SimpleResult();
        rtn.setResultCode(ResultCode.REQ_DATA_ERROR);
        return rtn;
    }

    @ExceptionHandler(IOException.class)
    public
    @ResponseBody
    SimpleResult handleIOException( Exception ex){
        logger.error("Io Error:"+ex.getMessage());
        SimpleResult rtn = new SimpleResult();
        rtn.setResultCode(ResultCode.REQ_FORMAT_ERROR);
        return rtn;
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public SimpleResult processUnauthorizedException( Exception ex) {
        logger.error("UnauthorizedException Error",ex);
        SimpleResult rtn = new SimpleResult();
        rtn.setResultCode(ResultCode.PERMISSION_DENIED);
        return rtn;
    }


    @ExceptionHandler(Exception.class)
    public
    @ResponseBody
    SimpleResult handleException( Exception ex){
        logger.error("Sys Exception Msg:"+ex.getMessage(),ex);
        SimpleResult rtn = new SimpleResult();
        rtn.setResultCode(ResultCode.SERVICE_ERROR);
        return rtn;
    }

    //Add your exception handler
    @ExceptionHandler(BusinessException.class)
    public
    @ResponseBody
    SimpleResult handleBusinessException(BusinessException ex){
        logger.info("Biz info Msg",ex);
        SimpleResult rtn = new SimpleResult();
        rtn.setCode(ex.getCode());
        rtn.setMessage(ex.getMsg());
        return rtn;
    }


    /**
     * 校验错误拦截处理
     * 处理{@link Valid} & {@link NotNull}
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public SimpleResult validationBodyException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        SimpleResult rtn = new SimpleResult();

        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                logger.warn("Data check failure : object={}, field={}, errorMessage={}", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
            });
            rtn.setResultCode(ResultCode.REQ_DATA_ERROR);
            String errorMsg = result.getFieldError() == null ? "请求参数有误" : result.getFieldError().getDefaultMessage();
            rtn.setMessage(errorMsg);

        }else {
            //其他错误
            rtn.setResultCode(ResultCode.REQ_DATA_ERROR);
        }

        return rtn;
    }


    /**
     * 参数类型转换错误 {@link HttpMessageConversionException}
     *
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseBody
    public SimpleResult parameterTypeException(HttpMessageConversionException exception) {
        logger.warn("parameterTypeException {}", exception.getCause().getLocalizedMessage());
        SimpleResult rtn = new SimpleResult();
        rtn.setResultCode(ResultCode.REQ_FORMAT_ERROR);
        return rtn;
    }
}
