package com.neo.qiaoqiaochat.model.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "user")
public class MiUserModel implements Serializable {
    @Id
    private Long id;

    private String openid;

    /**
     * 账号
     */
    private String account;

    @Column(name = "nick_name")
    private String nickName;

    private String sex;

    /**
     * 简介
     */
    private String introduce;

    @Column(name = "head_image_index")
    private String headImageIndex;

    private String password;

    /**
     * 创建账户时间
     */
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
     * @return openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * 获取账号
     *
     * @return account - 账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置账号
     *
     * @param account 账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return nick_name
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * @param nickName
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * @return sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取简介
     *
     * @return introduce - 简介
     */
    public String getIntroduce() {
        return introduce;
    }

    /**
     * 设置简介
     *
     * @param introduce 简介
     */
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    /**
     * @return head_image_index
     */
    public String getHeadImageIndex() {
        return headImageIndex;
    }

    /**
     * @param headImageIndex
     */
    public void setHeadImageIndex(String headImageIndex) {
        this.headImageIndex = headImageIndex;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取创建账户时间
     *
     * @return create_time - 创建账户时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建账户时间
     *
     * @param createTime 创建账户时间
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
        sb.append(", openid=").append(openid);
        sb.append(", account=").append(account);
        sb.append(", nickName=").append(nickName);
        sb.append(", sex=").append(sex);
        sb.append(", introduce=").append(introduce);
        sb.append(", headImageIndex=").append(headImageIndex);
        sb.append(", password=").append(password);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}