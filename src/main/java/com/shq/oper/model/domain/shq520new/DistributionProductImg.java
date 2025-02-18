package com.shq.oper.model.domain.shq520new;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "DistributionProductImg")
public class DistributionProductImg implements Serializable {
    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Code")
    private String code;

    @Column(name = "ImgName")
    private String imgname;

    @Column(name = "ImgUrl")
    private String imgurl;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "UploadUser")
    private String uploaduser;

    @Column(name = "UploadDate")
    private Date uploaddate;

    private Integer isture;

    private static final long serialVersionUID = 1L;
}