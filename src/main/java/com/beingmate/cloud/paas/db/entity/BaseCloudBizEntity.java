package com.beingmate.cloud.paas.db.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * Description 基类逻辑entity,后续cloud项目表字段均改为下划线格式
 * <p>
 *
 * <p>
 * @date 2020-4-07 11:09
 * <p>
 */
@Data
public class BaseCloudBizEntity extends BaseCloudEntity implements Serializable {
    private static final long serialVersionUID = -330828541312506532L;
    /**
     * 创建人编号。取值为创建人的全局唯一主键标识符。
     */
    @TableField("creator_id")
    private Long creatorId;

    /**
     * 创建时间。取值为系统的当前时间。
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 最后修改人编号。取值为最后修改人的全局唯一主键标识符。
     */
    @TableField("last_modifier_id")
    private Long lastModifierId;

    /**
     * 最后修改时间。取值为系统的当前时间。
     */
    @TableField("last_modified_time")
    private Date lastModifiedTime=new Date();;

    /**
     * 备注。用于备注其他信息。
     */
    @TableField("memo")
    private String memo;

    public BaseCloudBizEntity() {
    }

    @Override
    public void cleanInit() {
        super.cleanInit();
        this.createdTime = null;
    }
}
