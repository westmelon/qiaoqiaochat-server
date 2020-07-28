package com.neo.qiaoqiaochat.web.util;

import com.google.gson.Gson;
import com.neo.qiaoqiaochat.web.exception.BusinessException;
import com.neo.qiaoqiaochat.web.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.web.model.ResultCode;
import com.neo.qiaoqiaochat.web.model.vo.UserAccountVO;
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
