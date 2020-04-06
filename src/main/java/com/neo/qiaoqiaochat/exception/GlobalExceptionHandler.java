package com.neo.qiaoqiaochat.exception;

import com.neo.commons.exception.SimpleBusinessException;
import com.neo.qiaoqiaochat.model.ResultCode;
import com.neo.qiaoqiaochat.model.SimpleResult;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;



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

    @ExceptionHandler(SimpleBusinessException.class)
    public
    @ResponseBody
    SimpleResult handleBusinessException(SimpleBusinessException ex){
        logger.info("SimpleBusinessException info Msg",ex);
        SimpleResult rtn = new SimpleResult();
        rtn.setCode(ex.getCode());
        rtn.setMessage(ex.getSubMsg());
        return rtn;
    }
}
