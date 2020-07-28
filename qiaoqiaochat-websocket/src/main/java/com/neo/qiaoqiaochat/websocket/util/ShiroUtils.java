package com.neo.qiaoqiaochat.websocket.util;

import com.google.gson.Gson;
import com.neo.qiaoqiaochat.websocket.exception.BusinessException;
import com.neo.qiaoqiaochat.websocket.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.websocket.model.ResultCode;
import com.neo.qiaoqiaochat.websocket.model.vo.UserAccountVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class ShiroUtils {

    public static UserAccountVO getCurrentUser(){
        Subject subject = SecurityUtils.getSubject();
        String userStr = (String) subject.getSession().getAttribute(QiaoqiaoConst.ShiroConfig.USER_INFO);
        if(StringUtils.isBlank(userStr)){
            throw new BusinessException(ResultCode.NO_LOGIN);
        }
        return new Gson().fromJson(userStr,UserAccountVO.class);

    }
}
