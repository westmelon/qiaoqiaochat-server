package com.neo.qiaoqiaochat.service;
import java.util.Date;

import com.neo.qiaoqiaochat.dao.MiUserModelMapper;
import com.neo.qiaoqiaochat.exception.BusinessException;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.ResultCode;
import com.neo.qiaoqiaochat.model.domain.MiUserModel;
import com.neo.qiaoqiaochat.model.dto.UserRegisterDTO;
import com.neo.qiaoqiaochat.model.vo.UserAccountVO;
import com.neo.qiaoqiaochat.shiro.PasswordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private MiUserModelMapper miUserModelMapper;


    /**
     * 根据账号查找用户账号
     * @param account 账号名
     * @return UserAccountVO
     */
    public UserAccountVO findUserByAccount(String account){
        MiUserModel miUserModel = getUserByAccount(account);
        if(miUserModel == null){
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }
        UserAccountVO vo = new UserAccountVO();
        vo.setAccount(miUserModel.getMiHao());
        vo.setNickName(miUserModel.getNickName());
        vo.setHeadImageIndex(miUserModel.getHeadImageIndex());
        vo.setPassword(miUserModel.getPassword());
        return vo;
    }

    private MiUserModel getUserByAccount(String account){
        MiUserModel query = new MiUserModel();
        query.setMiHao(account);
        return miUserModelMapper.selectOne(query);
    }

    private void insertUser(MiUserModel insert){
        miUserModelMapper.insertSelective(insert);
    }


    /**
     * 用户注册
     * @param dto 注册信息
     */
    public void userRegister(UserRegisterDTO dto){
        String account = dto.getAccount();
        MiUserModel user = getUserByAccount(account);
        //用户名已经存在
        if(user != null){
            throw new BusinessException(ResultCode.REGIST_ERROR_ACCOUNT_EXISTS);
        }

        MiUserModel insert = new MiUserModel();
        // TODO: 2020/4/5
        insert.setOpenid("");
        insert.setMiHao(account);
        insert.setNickName(dto.getNickName());
        insert.setSex(dto.getSex());
        insert.setIntroduce(dto.getIntroduce());
        //加密密码
        String serect = PasswordManager.instance().encryptPassword(dto.getPassword(), QiaoqiaoConst.ShiroConfig.SALT);
        insert.setPassword(serect);
        insert.setCrtime(new Date());
        insertUser(insert);

    }

    //获取用户token
    public void getUserToken(){

    }

}
