package com.neo.qiaoqiaochat.web.model.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "message_history")
public class MiMessageHistoryModel implements Serializable {
    @Id
    private Long id;

    /**
     * 消息唯一标识
     */
    private String code;

    /**
     * 消息类型 00添加好友 01通过好友请求 02拒绝好友请求 10 文本消息包含emoji 11语音消息 12图片消息 13视频
     */
    private String type;

    /**
     * 消息发送者
     */
    @Column(name = "sender_id")
    private Long senderId;

    /**
     * 消息接收者
     */
    @Column(name = "receiver_id")
    private Long receiverId;

    /**
     * 消息状态 0未发送 1已发送
     */
    private Integer state;

    /**
     * 消息发送时间
     */
    @Column(name = "send_time")
    private Date sendTime;

    /**
     * 消息送达时间
     */
    @Column(name = "receive_time")
    private Date receiveTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取消息唯一标识
     *
     * @return code - 消息唯一标识
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置消息唯一标识
     *
     * @param code 消息唯一标识
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取消息类型 00添加好友 01通过好友请求 02拒绝好友请求 10 文本消息包含emoji 11语音消息 12图片消息 13视频
     *
     * @return type - 消息类型 00添加好友 01通过好友请求 02拒绝好友请求 10 文本消息包含emoji 11语音消息 12图片消息 13视频
     */
    public String getType() {
        return type;
    }

    /**
     * 设置消息类型 00添加好友 01通过好友请求 02拒绝好友请求 10 文本消息包含emoji 11语音消息 12图片消息 13视频
     *
     * @param type 消息类型 00添加好友 01通过好友请求 02拒绝好友请求 10 文本消息包含emoji 11语音消息 12图片消息 13视频
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取消息发送者
     *
     * @return sender_id - 消息发送者
     */
    public Long getSenderId() {
        return senderId;
    }

    /**
     * 设置消息发送者
     *
     * @param senderId 消息发送者
     */
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    /**
     * 获取消息接收者
     *
     * @return receiver_id - 消息接收者
     */
    public Long getReceiverId() {
        return receiverId;
    }

    /**
     * 设置消息接收者
     *
     * @param receiverId 消息接收者
     */
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * 获取消息状态 0未发送 1已发送
     *
     * @return state - 消息状态 0未发送 1已发送
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置消息状态 0未发送 1已发送
     *
     * @param state 消息状态 0未发送 1已发送
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取消息发送时间
     *
     * @return send_time - 消息发送时间
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * 设置消息发送时间
     *
     * @param sendTime 消息发送时间
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 获取消息送达时间
     *
     * @return receive_time - 消息送达时间
     */
    public Date getReceiveTime() {
        return receiveTime;
    }

    /**
     * 设置消息送达时间
     *
     * @param receiveTime 消息送达时间
     */
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
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
        sb.append(", id=").append(id);
        sb.append(", code=").append(code);
        sb.append(", type=").append(type);
        sb.append(", senderId=").append(senderId);
        sb.append(", receiverId=").append(receiverId);
        sb.append(", state=").append(state);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", receiveTime=").append(receiveTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}