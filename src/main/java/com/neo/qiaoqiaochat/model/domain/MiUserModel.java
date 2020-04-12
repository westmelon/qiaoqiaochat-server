package com.neo.qiaoqiaochat.model.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "mi_user")
public class MiUserModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String openid;

    /**
     * 密号
     */
    @Column(name = "mi_hao")
    private String miHao;

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
    private Date crtime;

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
     * 获取密号
     *
     * @return mi_hao - 密号
     */
    public String getMiHao() {
        return miHao;
    }

    /**
     * 设置密号
     *
     * @param miHao 密号
     */
    public void setMiHao(String miHao) {
        this.miHao = miHao;
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
     * @return crtime - 创建账户时间
     */
    public Date getCrtime() {
        return crtime;
    }

    /**
     * 设置创建账户时间
     *
     * @param crtime 创建账户时间
     */
    public void setCrtime(Date crtime) {
        this.crtime = crtime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", openid=").append(openid);
        sb.append(", miHao=").append(miHao);
        sb.append(", nickName=").append(nickName);
        sb.append(", sex=").append(sex);
        sb.append(", introduce=").append(introduce);
        sb.append(", headImageIndex=").append(headImageIndex);
        sb.append(", password=").append(password);
        sb.append(", crtime=").append(crtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}