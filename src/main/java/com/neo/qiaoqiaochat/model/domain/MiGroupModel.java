package com.neo.qiaoqiaochat.model.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "group")
public class MiGroupModel implements Serializable {
    @Id
    private Long id;

    /**
     * 群名
     */
    private String name;

    /**
     * 群简介
     */
    private String description;

    /**
     * 群主id
     */
    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "uptime_time")
    private Date uptimeTime;

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
     * 获取群名
     *
     * @return name - 群名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置群名
     *
     * @param name 群名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取群简介
     *
     * @return description - 群简介
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置群简介
     *
     * @param description 群简介
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取群主id
     *
     * @return owner_id - 群主id
     */
    public Long getOwnerId() {
        return ownerId;
    }

    /**
     * 设置群主id
     *
     * @param ownerId 群主id
     */
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
     * @return uptime_time
     */
    public Date getUptimeTime() {
        return uptimeTime;
    }

    /**
     * @param uptimeTime
     */
    public void setUptimeTime(Date uptimeTime) {
        this.uptimeTime = uptimeTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", ownerId=").append(ownerId);
        sb.append(", createTime=").append(createTime);
        sb.append(", uptimeTime=").append(uptimeTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}