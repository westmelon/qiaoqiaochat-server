package com.neo.qiaoqiaochat.web.service.impl;

import com.neo.core.auth.UserUtils;
import com.neo.core.entity.ResultCode;
import com.neo.core.entity.UserParam;
import com.neo.core.exception.BusinessException;
import com.neo.qiaoqiaochat.web.dao.MiFriendModelMapper;
import com.neo.qiaoqiaochat.web.dao.MiGroupModelMapper;
import com.neo.qiaoqiaochat.web.dao.MiUserModelMapper;
import com.neo.qiaoqiaochat.web.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.web.model.bo.AddFriendBO;
import com.neo.qiaoqiaochat.web.model.bo.ConfirmFriendBO;
import com.neo.qiaoqiaochat.web.model.domain.MiFriendModel;
import com.neo.qiaoqiaochat.web.model.domain.MiUserModel;
import com.neo.qiaoqiaochat.web.model.dto.AddFriendDTO;
import com.neo.qiaoqiaochat.web.model.dto.ConfirmFriendDTO;
import com.neo.qiaoqiaochat.web.model.dto.SearchUserDTO;
import com.neo.qiaoqiaochat.web.model.vo.ContactVO;
import com.neo.qiaoqiaochat.web.model.vo.FriendVO;
import com.neo.qiaoqiaochat.web.model.vo.SearchUserVO;
import com.neo.qiaoqiaochat.web.model.vo.UserAccountVO;
import com.neo.qiaoqiaochat.web.service.FriendService;
import com.neo.qiaoqiaochat.web.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 好友相关服务层实现
 *
 * @author linyi
 * @date 2020/7/27 15:29
 */
@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserService userService;
    @Autowired
    private MiGroupModelMapper miGroupModelMapper;

    @Autowired
    private MiFriendModelMapper miFriendModelMapper;

    @Autowired
    private MiUserModelMapper miUserModelMapper;


    @Override
    public SearchUserVO searchUserForWeb(SearchUserDTO dto) {
        //todo 根据账户设置的隐私策略决定是否查询出该账号
        UserAccountVO account = userService.findUserByAccountForApi(dto.getAccount());
        SearchUserVO vo = new SearchUserVO();
        BeanUtils.copyProperties(account, vo);
        return vo;
    }


    @Override
    public void addFriend(AddFriendDTO dto) {
        String account = dto.getAccount();
        MiUserModel miUserModel = userService.getUserByAccount(account);
        if (miUserModel == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }
        //校验好友关系 如果已经添加则返回提示信息 如果已经有添加记录则提示等待
        Long targetUserId = miUserModel.getId();
        UserParam accountVO = UserUtils.getAccount();
        Long userId = accountVO.getId();
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
        bo.setFromAccount(accountVO.getAccount());

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


    @Override
    public void confirmAddFriend(ConfirmFriendDTO dto) {
        String account = dto.getAccount();
        MiUserModel miUserModel = userService.getUserByAccount(account);
        if (miUserModel == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }
        UserParam currentUser = UserUtils.getAccount();
        Long targetUserId = miUserModel.getId();
        Long userId = currentUser.getId();
        MiFriendModel friendRelation = getFriendRelation(targetUserId, userId);
        // 查询对方添加请求是否存在
        if (friendRelation != null
                && QiaoqiaoConst.FriendRelationIndex.WAITING == friendRelation.getRelationIndex()) {

            Integer action = dto.getAction();
            if (QiaoqiaoConst.FriendAction.PASSED_FRIEND_REQUIRE == action) {
                //todo 通过添加好友请求给对方发行一条消息
                ConfirmFriendBO bo = new ConfirmFriendBO();
                BeanUtils.copyProperties(dto, bo);
                bo.setFromAccount(currentUser.getAccount());

                //新增一条好友记录
                insertOrUpdateFriendRelation(userId, targetUserId, QiaoqiaoConst.FriendRelationIndex.GOOD_FRIEND);
                insertOrUpdateFriendRelation(targetUserId, userId, QiaoqiaoConst.FriendRelationIndex.GOOD_FRIEND);
            } else if (QiaoqiaoConst.FriendAction.REFUSE_FRIEND_REQUIRE == action) {
                //拒绝添加好友的操作
                insertOrUpdateFriendRelation(targetUserId, userId, QiaoqiaoConst.FriendRelationIndex.REFUSE);
            } else if (QiaoqiaoConst.FriendAction.BLACK_FRIEND_REQUIRE == action) {
                //拉黑的操作
                insertOrUpdateFriendRelation(userId, targetUserId, QiaoqiaoConst.FriendRelationIndex.BLACK_FRIEND);
                insertOrUpdateFriendRelation(targetUserId, userId, QiaoqiaoConst.FriendRelationIndex.REFUSE);
            }
        }
    }

    @Override
    public ContactVO getFriendList() {
        ContactVO vo = new ContactVO();

        Long userId = UserUtils.getAccount().getId();
        List<FriendVO> friendList = miFriendModelMapper.getFriendList(userId);
        vo.setFriends(friendList);
        return vo;
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
    private void insertOrUpdateFriendRelation(Long userId, Long targetUserId, int index) {
        MiFriendModel friendRelation = getFriendRelation(userId, targetUserId);
        MiFriendModel insertOrUpdate = new MiFriendModel();
        insertOrUpdate.setFid(targetUserId);
        insertOrUpdate.setUid(userId);
        insertOrUpdate.setRelationIndex(index);
        Date now = new Date();
        insertOrUpdate.setUpdateTime(now);
        if (friendRelation == null) {
            insertOrUpdate.setCreateTime(now);
            miFriendModelMapper.insertSelective(insertOrUpdate);
        } else {
            miFriendModelMapper.updateByPrimaryKeySelective(insertOrUpdate);
        }
    }
}