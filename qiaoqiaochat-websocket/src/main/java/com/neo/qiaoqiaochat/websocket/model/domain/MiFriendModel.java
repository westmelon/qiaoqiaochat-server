package com.neo.qiaoqiaochat.websocket.model.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "friend")
public class MiFriendModel implements Serializable {
    /**
     * 用户账号主键
     */
    @Id
    private Long uid;

    /**
     * 好友账号主键
     */
    @Id
    private Long fid;

    /**
     * 关系类型
     */
    @Column(name = "relation_index")
    private Integer relationIndex;

    /**
     * 消息接收策略 0接收并提示 1接收但不提示 2拒收
     */
    @Column(name = "msg_receive_setting")
    private Integer msgReceiveSetting;

    /**
     * 成为好友时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * 获取用户账号主键
     *
     * @return uid - 用户账号主键
     */
    public Long getUid() {
        return uid;
    }

    /**
     * 设置用户账号主键
     *
     * @param uid 用户账号主键
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * 获取好友账号主键
     *
     * @return fid - 好友账号主键
     */
    public Long getFid() {
        return fid;
    }

    /**
     * 设置好友账号主键
     *
     * @param fid 好友账号主键
     */
    public void setFid(Long fid) {
        this.fid = fid;
    }

    /**
     * 获取关系类型
     *
     * @return relation_index - 关系类型
     */
    public Integer getRelationIndex() {
        return relationIndex;
    }

    /**
     * 设置关系类型
     *
     * @param relationIndex 关系类型
     */
    public void setRelationIndex(Integer relationIndex) {
        this.relationIndex = relationIndex;
    }

    /**
     * 获取消息接收策略 0接收并提示 1接收但不提示 2拒收
     *
     * @return msg_receive_setting - 消息接收策略 0接收并提示 1接收但不提示 2拒收
     */
    public Integer getMsgReceiveSetting() {
        return msgReceiveSetting;
    }

    /**
     * 设置消息接收策略 0接收并提示 1接收但不提示 2拒收
     *
     * @param msgReceiveSetting 消息接收策略 0接收并提示 1接收但不提示 2拒收
     */
    public void setMsgReceiveSetting(Integer msgReceiveSetting) {
        this.msgReceiveSetting = msgReceiveSetting;
    }

    /**
     * 获取成为好友时间
     *
     * @return create_time - 成为好友时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置成为好友时间
     *
     * @param createTime 成为好友时间
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
        sb.append(", uid=").append(uid);
        sb.append(", fid=").append(fid);
        sb.append(", relationIndex=").append(relationIndex);
        sb.append(", msgReceiveSetting=").append(msgReceiveSetting);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}