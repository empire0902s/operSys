package com.shq.oper.model.domain.shq520new;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper=true)
@Table(name = "DistrFourCategoty")
public class DistrFourCategoty extends com.shq.oper.model.dto.MssqlShqPageDto implements Serializable {
    @Id
    @Column(name = "Id")
    private Integer id;

    //一级分类Id
    @Column(name = "CategoryId")
    private Integer categoryId;

    //二级分类Id
    @Column(name = "GenreId")
    private Integer genreId;

    //三级分类Id
    @Column(name = "ClassId")
    private Integer classId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Status")
    private Integer status;

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