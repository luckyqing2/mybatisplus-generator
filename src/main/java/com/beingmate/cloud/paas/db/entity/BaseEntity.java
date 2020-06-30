package com.beingmate.cloud.paas.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;

/**
 * @version 8.0.0
 * @program: mybatisplus-generator
 * @author: "清歌"
 * @create: 2020-05-26 18:07
 * @Copyright topology technology group co.LTD. All rights reserved.
 *         注意：本内容仅限于授权后使用，禁止非授权传阅以及私自用于其他商业目的。
 **/
public class BaseEntity {
    private static final long serialVersionUID = -7470678212287614592L;
    @TableId(
            value = "ID",
            type = IdType.ID_WORKER
    )
    private Long id;
    @TableLogic(
            value = "0",
            delval = "1"
    )
    @TableField("deletedFlag")
    private Byte deletedFlag = 0;
    @Version
    private Long version = 1L;

    public BaseEntity() {
    }

    public void cleanInit() {
        this.deletedFlag = null;
        this.version = null;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getDeletedFlag() {
        return this.deletedFlag;
    }

    public void setDeletedFlag(Byte deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
