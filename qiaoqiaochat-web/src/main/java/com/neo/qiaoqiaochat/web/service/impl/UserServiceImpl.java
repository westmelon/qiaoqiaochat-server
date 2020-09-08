package com.neo.qiaoqiaochat.web.service.impl;

import com.neo.core.entity.ResultCode;
import com.neo.core.exception.BusinessException;
import com.neo.core.util.PasswordUtils;
import com.neo.core.util.SnowflakeIdWorker;
import com.neo.qiaoqiaochat.web.dao.MiUserModelMapper;
import com.neo.qiaoqiaochat.web.model.domain.MiUserModel;
import com.neo.qiaoqiaochat.web.model.dto.UserRegisterDTO;
import com.neo.qiaoqiaochat.web.model.vo.UserAccountVO;
import com.neo.qiaoqiaochat.web.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MiUserModelMapper miUserModelMapper;


    @Autowired
    private SnowflakeIdWorker idWorker;


    /**
     * 根据账号查找用户账号
     *
     * @param account 账号名
     * @return UserAccountVO
     */
    @Override
    public UserAccountVO findUserByAccount(String account) {
        MiUserModel miUserModel = getUserByAccount(account);
        if (miUserModel == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }
        UserAccountVO vo = new UserAccountVO();
        vo.setId(miUserModel.getId());
        vo.setAccount(miUserModel.getAccount());
        vo.setNickName(miUserModel.getNickName());
        vo.setHeadImageIndex(miUserModel.getHeadImageIndex());
        vo.setPassword(miUserModel.getPassword());
        return vo;
    }

    /**
     * 根据账号查找用户账号(接口使用)
     *
     * @param account 账号名
     * @return UserAccountVO
     */
    @Override
    public UserAccountVO findUserByAccountForApi(String account) {
        MiUserModel miUserModel = getUserByAccount(account);
        if (miUserModel == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }
        UserAccountVO vo = new UserAccountVO();
        vo.setAccount(miUserModel.getAccount());
        vo.setNickName(miUserModel.getNickName());
        vo.setHeadImageIndex(miUserModel.getHeadImageIndex());
        return vo;
    }

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

    @Override
    public MiUserModel getUserByAccount(String account) {
        MiUserModel query = new MiUserModel();
        query.setAccount(account);
        return miUserModelMapper.selectOne(query);
    }

    private void insertUser(MiUserModel insert) {
        miUserModelMapper.insertSelective(insert);
    }


    /**
     * 用户注册
     *
     * @param dto 注册信息
     */
    @Override
    public void userRegister(UserRegisterDTO dto) {
        String account = dto.getAccount();
        MiUserModel user = getUserByAccount(account);
        //用户名已经存在
        if (user != null) {
            throw new BusinessException(ResultCode.REGIST_ERROR_ACCOUNT_EXISTS);
        }

        MiUserModel insert = new MiUserModel();
        // TODO: 2020/4/5
        insert.setId(idWorker.nextId());
        insert.setOpenid("");
        insert.setAccount(account);
        insert.setNickName(dto.getNickName());
        insert.setIntroduce(dto.getIntroduce());
        //加密密码
        Date now = new Date();
        String secret = PasswordUtils.sin(dto.getPassword());
        insert.setPassword(secret);
        insert.setCreateTime(now);
        insert.setUpdateTime(now);
        insertUser(insert);

    }


    //获取用户token
    public void getUserToken() {

    }

}
