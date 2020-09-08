package com.neo.qiaoqiaochat.web.config.redis.key;

import com.neo.qiaoqiaochat.web.model.vo.UserAccountVO;
import com.neo.qiaoqiaochat.web.redis.RedisKeyGenerator;

/**
 * @author linyi 2020-09-07
 */
public class UserKey extends RedisKeyGenerator<UserAccountVO> {

    public UserKey(String key) {
        super(key);
    }

    @Override
    public String namespace() {
        return "965";
    }

    @Override
    public String service() {
        return "app";
    }

    @Override
    public String function() {
        return "user";
    }

    @Override
    public Class<UserAccountVO> type() {
        return UserAccountVO.class;
    }

}
