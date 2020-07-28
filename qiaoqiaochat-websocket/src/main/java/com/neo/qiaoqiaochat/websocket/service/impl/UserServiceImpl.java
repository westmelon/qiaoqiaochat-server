package com.neo.qiaoqiaochat.websocket.service.impl;

import com.neo.qiaoqiaochat.websocket.dao.MiUserModelMapper;
import com.neo.qiaoqiaochat.websocket.model.domain.MiUserModel;
import com.neo.qiaoqiaochat.websocket.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MiUserModelMapper miUserModelMapper;


    @Override
    public Long getUserIdByAccount(String account) {
        if(StringUtils.isBlank(account)){
            return null;
        }
        MiUserModel miUserModel = getUserByAccount(account);
        if(miUserModel != null){
            return miUserModel.getId();
        }
        return null;
    }

    private MiUserModel getUserByAccount(String account) {
        MiUserModel query = new MiUserModel();
        query.setAccount(account);
        return miUserModelMapper.selectOne(query);
    }

}
