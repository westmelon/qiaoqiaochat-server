package com.neo.qiaoqiaochat.model.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "group_user_relation")
public class MiGroupUserRelationModel implements Serializable {
    @Id
    private Long gid;

    @Id
    private Long uid;

    /**
     * 群昵称
     */
    @Column(name = "group_nick_name")
    private String groupNickName;

    /**
     * 消息接受策略 0接受消息 1接受消息但不提示 2拒收消息
     */
    @Column(name = "msg_receive_setting")
    private Integer msgReceiveSetting;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * @return gid
     */
    public Long getGid() {
        return gid;
    }

    /**
     * @param gid
     */
    public void setGid(Long gid) {
        this.gid = gid;
    }

    /**
     * @return uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * @param uid
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * 获取群昵称
     *
     * @return group_nick_name - 群昵称
     */
    public String getGroupNickName() {
        return groupNickName;
    }

    /**
     * 设置群昵称
     *
     * @param groupNickName 群昵称
     */
    public void setGroupNickName(String groupNickName) {
        this.groupNickName = groupNickName;
    }

    /**
     * 获取消息接受策略 0接受消息 1接受消息但不提示 2拒收消息
     *
     * @return msg_receive_setting - 消息接受策略 0接受消息 1接受消息但不提示 2拒收消息
     */
    public Integer getMsgReceiveSetting() {
        return msgReceiveSetting;
    }

    /**
     * 设置消息接受策略 0接受消息 1接受消息但不提示 2拒收消息
     *
     * @param msgReceiveSetting 消息接受策略 0接受消息 1接受消息但不提示 2拒收消息
     */
    public void setMsgReceiveSetting(Integer msgReceiveSetting) {
        this.msgReceiveSetting = msgReceiveSetting;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", gid=").append(gid);
        sb.append(", uid=").append(uid);
        sb.append(", groupNickName=").append(groupNickName);
        sb.append(", msgReceiveSetting=").append(msgReceiveSetting);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}