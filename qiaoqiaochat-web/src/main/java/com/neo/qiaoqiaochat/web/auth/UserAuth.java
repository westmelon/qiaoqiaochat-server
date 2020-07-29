package com.neo.qiaoqiaochat.web.auth;

import java.lang.annotation.*;

/**
 * 权限验证
 *
 * @author linyi
 * @date 2020/7/29 9:47 下午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserAuth {
}
