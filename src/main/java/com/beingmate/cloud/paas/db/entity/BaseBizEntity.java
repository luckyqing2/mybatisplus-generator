package com.beingmate.cloud.paas.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

public class BaseBizEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -7470678222287614592L;
    @TableField("creatorId")
    private Long creatorId;
    @TableField("createdTime")
    private Date createdTime = new Date();
    @TableField("lastModifierId")
    private Long lastModifierId;
    @TableField("lastModifiedTime")
    private Date lastModifiedTime = new Date();
    @TableField("memo")
    private String memo;

    public BaseBizEntity() {
    }

    public void cleanInit() {
        super.cleanInit();
        this.createdTime = null;
    }

    public Long getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getLastModifierId() {
        return this.lastModifierId;
    }

    public void setLastModifierId(Long lastModifierId) {
        this.lastModifierId = lastModifierId;
    }

    public Date getLastModifiedTime() {
        return this.lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}

