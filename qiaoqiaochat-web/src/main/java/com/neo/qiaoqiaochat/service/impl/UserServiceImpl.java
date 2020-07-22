package com.neo.qiaoqiaochat.service.impl;

import com.neo.qiaoqiaochat.dao.MiFriendModelMapper;
import com.neo.qiaoqiaochat.dao.MiUserModelMapper;
import com.neo.qiaoqiaochat.exception.BusinessException;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.ResultCode;
import com.neo.qiaoqiaochat.model.bo.AddFriendBO;
import com.neo.qiaoqiaochat.model.bo.ConfirmFriendBO;
import com.neo.qiaoqiaochat.model.domain.MiFriendModel;
import com.neo.qiaoqiaochat.model.domain.MiUserModel;
import com.neo.qiaoqiaochat.model.dto.AddFriendDTO;
import com.neo.qiaoqiaochat.model.dto.ConfirmFriendDTO;
import com.neo.qiaoqiaochat.model.dto.UserRegisterDTO;
import com.neo.qiaoqiaochat.model.vo.FriendVO;
import com.neo.qiaoqiaochat.model.vo.UserAccountVO;
import com.neo.qiaoqiaochat.service.UserService;
import com.neo.qiaoqiaochat.shiro.PasswordManager;
import com.neo.qiaoqiaochat.util.ShiroUtils;
import com.neo.qiaoqiaochat.util.SnowflakeIdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MiUserModelMapper miUserModelMapper;
    @Autowired
    private MiFriendModelMapper miFriendModelMapper;


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

    private MiUserModel getUserByAccount(String account) {
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
        insert.setSex(dto.getSex());
        insert.setIntroduce(dto.getIntroduce());
        //加密密码
        Date now = new Date();
        String serect = PasswordManager.instance().encryptPassword(dto.getPassword(), QiaoqiaoConst.ShiroConfig.SALT);
        insert.setPassword(serect);
        insert.setCreateTime(now);
        insert.setUpdateTime(now);
        insertUser(insert);

    }


    /**
     * Add 添加好友
     *
     * @param dto the dto
     */
    @Override
    public void addFriend(AddFriendDTO dto) {
        String account = dto.getAccount();
        MiUserModel miUserModel = getUserByAccount(account);
        if (miUserModel == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }
        //校验好友关系 如果已经添加则返回提示信息 如果已经有添加记录则提示等待
        UserAccountVO currentUser = ShiroUtils.getCurrentUser();
        Long targetUserId = miUserModel.getId();
        Long userId = currentUser.getId();
        MiFriendModel friendRelation = getFriendRelation(userId, targetUserId);
        if (friendRelation != null) {
            Integer relationIndex = friendRelation.getRelationIndex();
            if (QiaoqiaoConst.FriendRelationIndex.GOOD_FRIEND == relationIndex) {
                throw new BusinessException(ResultCode.YOU_ARE_ALREADY_FRIEND);
            } else if (QiaoqiaoConst.FriendRelationIndex.BLACK_FRIEND == relationIndex
                    || QiaoqiaoConst.FriendRelationIndex.WAITING == relationIndex) {
                return;
            }
        }
        AddFriendBO bo = new AddFriendBO();
        BeanUtils.copyProperties(dto, bo);
        bo.setFromAccount(currentUser.getAccount());

        //新增添加好友记录
        MiFriendModel insert = new MiFriendModel();
        insert.setUid(userId);
        insert.setFid(targetUserId);
        insert.setRelationIndex(QiaoqiaoConst.FriendRelationIndex.WAITING);
        insert.setMsgReceiveSetting(QiaoqiaoConst.MessageReceiveSetting.RECEIVE_AND_REMAIND);
        insert.setCreateTime(new Date());
        insert.setUpdateTime(new Date());

        miFriendModelMapper.insertSelective(insert);
    }


    /**
     * 确认添加好友
     *
     * @param dto the dto
     */
    @Override
    public void confirmAddFriend(ConfirmFriendDTO dto) {
        String account = dto.getAccount();
        MiUserModel miUserModel = getUserByAccount(account);
        if (miUserModel == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }
        UserAccountVO currentUser = ShiroUtils.getCurrentUser();
        Long targetUserId = miUserModel.getId();
        Long userId = currentUser.getId();
        MiFriendModel friendRelation = getFriendRelation(targetUserId, userId);
        // 查询对方添加请求是否存在
        if (friendRelation != null
                && QiaoqiaoConst.FriendRelationIndex.WAITING == friendRelation.getRelationIndex()) {

            Integer action = dto.getAction();
            if (QiaoqiaoConst.FriendAction.PASSED_FRIEND_REQUIRE == action) {
                //通过添加好友请求给对方发行一条消息
                ConfirmFriendBO bo = new ConfirmFriendBO();
                BeanUtils.copyProperties(dto, bo);
                bo.setFromAccount(ShiroUtils.getCurrentUser().getAccount());

                //新增一条好友记录
                insertOrUpdateFriendRelation(userId, targetUserId, QiaoqiaoConst.FriendRelationIndex.GOOD_FRIEND);
                insertOrUpdateFriendRelation(targetUserId, userId, QiaoqiaoConst.FriendRelationIndex.GOOD_FRIEND);
            }else if(QiaoqiaoConst.FriendAction.REFUSE_FRIEND_REQUIRE == action){
                //拒绝添加好友的操作
                insertOrUpdateFriendRelation(targetUserId, userId, QiaoqiaoConst.FriendRelationIndex.REFUSE);
            }else if(QiaoqiaoConst.FriendAction.BLACK_FRIEND_REQUIRE == action){
                //拉黑的操作
                insertOrUpdateFriendRelation(userId, targetUserId, QiaoqiaoConst.FriendRelationIndex.BLACK_FRIEND);
                insertOrUpdateFriendRelation(targetUserId, userId, QiaoqiaoConst.FriendRelationIndex.REFUSE);
            }
        }
    }

    @Override
    public List<FriendVO> getFriendList() {
        return null;
    }

    /**
     * 获取好友关系
     *
     * @param userId       the user id
     * @param targetUserId the target user id
     * @return the mi friend model
     */
    private MiFriendModel getFriendRelation(Long userId, Long targetUserId) {
        MiFriendModel param = new MiFriendModel();
        param.setUid(userId);
        param.setFid(targetUserId);
        return miFriendModelMapper.selectOne(param);
    }

    /**
     * 添加或者更新好友关系
     *
     * @param userId       自己的用户id
     * @param targetUserId 对方用户id
     * @param index        关系
     */
    private void insertOrUpdateFriendRelation(Long userId, Long targetUserId, int index){
        MiFriendModel friendRelation = getFriendRelation(userId, targetUserId);
        MiFriendModel insertOrUpdate = new MiFriendModel();
        insertOrUpdate.setFid(targetUserId);
        insertOrUpdate.setUid(userId);
        insertOrUpdate.setRelationIndex(index);
        Date now = new Date();
        insertOrUpdate.setUpdateTime(now);
        if(friendRelation == null){
            insertOrUpdate.setCreateTime(now);
            miFriendModelMapper.insertSelective(insertOrUpdate);
        }else{
            miFriendModelMapper.updateByPrimaryKeySelective(insertOrUpdate);
        }
    }

    //获取用户token
    public void getUserToken() {

    }

}
