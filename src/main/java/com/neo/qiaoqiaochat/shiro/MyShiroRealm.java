package com.neo.qiaoqiaochat.shiro;

import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.vo.UserAccountVO;
import com.neo.qiaoqiaochat.service.impl.UserServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserServiceImpl userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

            return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //验证登录
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        if(!StringUtils.isEmpty(username)) {
            //清除用户信息缓存
            getAuthorizationCache().remove(username);

            //根据用户名查找对应账号信息
            UserAccountVO user = userService.findUserByAccount(username);
            String password = user.getPassword();
            return new SimpleAuthenticationInfo(
                    user.getAccount(),
                    password,
                    ByteSource.Util.bytes(QiaoqiaoConst.ShiroConfig.SALT),
                    getName());
        }
        return null;

    }
}
