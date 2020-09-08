package com.neo.qiaoqiaochat.web.util;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.neo.qiaoqiaochat.web.config.Audience;
import com.neo.qiaoqiaochat.web.exception.BusinessException;
import com.neo.qiaoqiaochat.web.model.ResultCode;
import com.neo.qiaoqiaochat.web.model.vo.UserAccountVO;

/**
 * @author linyi 2020-09-07
 */
public class UserUtils {

    public static UserAccountVO getAccount() {
        UserAccountVO param = null;
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Audience audience = SpringUtils.getBean(Audience.class);
        String accessToken = request.getHeader("token");

        try {
            param = JwtUtils.parseJWT(accessToken, audience.getBase64Secret());
        } catch (Exception e) {

        }
        if(param == null){
            throw new BusinessException(ResultCode.NO_LOGIN);
        }
        return param;
    }

}
