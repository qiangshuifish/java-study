package com.mybatisplus.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author K神带你飞
 * @since 2018-08-15
 */
@TableName("sys_role")
public class Role  extends Model<Role> {

    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String REMARK = "remark";

    public static final String CREATE_TIME = "create_time";

    @Override
    public String toString() {
        return "Role{" +
        ", id=" + id +
        ", name=" + name +
        ", remark=" + remark +
        ", createTime=" + createTime +
        "}";
    }


}
