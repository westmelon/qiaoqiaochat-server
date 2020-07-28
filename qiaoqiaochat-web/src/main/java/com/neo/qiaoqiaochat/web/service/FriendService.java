package com.neo.qiaoqiaochat.web.service;

import com.neo.qiaoqiaochat.web.model.dto.AddFriendDTO;
import com.neo.qiaoqiaochat.web.model.dto.ConfirmFriendDTO;
import com.neo.qiaoqiaochat.web.model.dto.SearchUserDTO;
import com.neo.qiaoqiaochat.web.model.vo.ContactVO;
import com.neo.qiaoqiaochat.web.model.vo.SearchUserVO;

/**
 * 好友相关服务层
 *
 * @author linyi
 * @date 2020/7/27 15:26
 */
public interface FriendService {

    /**
     * 搜索用户
     *
     * @return
     * @author linyi
     * @date 2020/7/27 15:26
     * @since 0.0.1
     */
    SearchUserVO searchUserForWeb(SearchUserDTO dto);

    /**
     * 添加好友
     *
     * @param dto the dto
     * @return
     * @author linyi
     * @date 2020/7/27 16:02
     * @since 0.0.1
     */
    void addFriend(AddFriendDTO dto);

    /**
     * 处理好友添加请求
     *
     * @param dto the dto
     * @return
     * @author linyi
     * @date 2020/7/27 16:03
     * @since 0.0.1
     */
    void confirmAddFriend(ConfirmFriendDTO dto);

    /**
     * 获取好友列表
     *
     * @return
     * @author linyi
     * @date 2020/7/27 16:03
     * @since 0.0.1
     */
    ContactVO getFriendList();
}
