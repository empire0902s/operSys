package com.shq.oper.model.domain.shq520new;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper=true)
@Table(name = "DistrFirstCategoty")
public class DistrFirstCategoty extends com.shq.oper.model.dto.MssqlShqPageDto implements Serializable {
    @Id
    @Column(name = "Id")
    private Integer id;

    //分类名称
    @Column(name = "Name")
    private String name;

    //审核状态 0待审核（未启用） 1审核完成（启用）
    @Column(name = "Status")
    private Integer status;

    //审核备注
    @Column(name = "CheckMark")
    private String checkmark;

    @Column(name = "Sort")
    private Integer sort;

    @Column(name = "CreateTime")
    private Date createtime;

    @Column(name = "CreateUser")
    private String createuser;

    @Column(name = "EditTime")
    private Date edittime;

    @Column(name = "EditUser")
    private String edituser;

    private static final long serialVersionUID = 1L;
}