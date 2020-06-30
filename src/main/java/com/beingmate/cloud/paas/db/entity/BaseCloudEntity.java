package com.beingmate.cloud.paas.db.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * Description cloud项目基类entity
 * <p>
 *
 * @author wangmeng
 * <p>
 * @date 2019-10-21 14:16
 * <p>
 * Copyright topology technology group co.LTD. All rights reserved.
 * <p>
 * Notice 本内容仅限于授权后使用，禁止非授权传阅以及私自用于其他商业目的。
 */
@Data
public class BaseCloudEntity implements Serializable {

    private static final long serialVersionUID = -6252439376623108884L;
    /**
     * 全局唯一ID
     */
    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 逻辑删除标识(0代表逻辑未删除值 1代表逻辑已删除值)
     * 逻辑删除是为了方便数据恢复和保护数据本身价值等等的一种方案，但实际就是删除。
     * 如果你需要再查出来就不应使用逻辑删除，而是以一个状态去表示。如： 员工离职，账号被锁定等都应该是一个状态字段，此种场景不应使用逻辑删除。
     * 若确需查找删除数据，如老板需要查看历史所有数据的统计汇总信息，请单独手写sql。
     */
    @TableLogic(value = "0",delval = "1")
    @TableField("deleted_flag")
    private Byte deletedFlag = 0;

    /**
     * 乐观锁版本Version
     */
    @Version
    private Long version = 1L;

    public void cleanInit() {
        this.deletedFlag = null;
        this.version = null;
    }
}
