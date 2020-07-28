package com.neo.qiaoqiaochat.websocket.model.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "message_content")
public class MiMessageContentModel implements Serializable {
    /**
     * 消息主键
     */
    @Id
    private Long mid;

    private String code;

    private String content;

    /**
     * 多媒体文件索引
     */
    @Column(name = "stream_index")
    private String streamIndex;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * 获取消息主键
     *
     * @return mid - 消息主键
     */
    public Long getMid() {
        return mid;
    }

    /**
     * 设置消息主键
     *
     * @param mid 消息主键
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }

    /**
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取多媒体文件索引
     *
     * @return stream_index - 多媒体文件索引
     */
    public String getStreamIndex() {
        return streamIndex;
    }

    /**
     * 设置多媒体文件索引
     *
     * @param streamIndex 多媒体文件索引
     */
    public void setStreamIndex(String streamIndex) {
        this.streamIndex = streamIndex;
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
        sb.append(", mid=").append(mid);
        sb.append(", code=").append(code);
        sb.append(", content=").append(content);
        sb.append(", streamIndex=").append(streamIndex);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}