package com.shq.oper.model.domain.mssqlshq;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper=true)
@Table(name = "shopper")
public class Shopper extends com.shq.oper.model.dto.MssqlShqPageDto implements Serializable {
    @Id
    @Column(name = "SEQ")
    private Integer seq;

    private String systemcode;

    private String teamid;

    private String departid;

    private String mobile;

    private String userid;

    private String company;

    private String pwd;

    private String username;

    private Integer logincount;

    private String lastip;

    private Date registtime;

    private Date lastlogintime;

    private String checkemail;

    private String banknumber;

    private String qqnumber;

    @Column(name = "userType")
    private String usertype;

    private String adresscode;

    private String adressdetail;

    private String provice;

    private String city;

    private String area;

    private String street;

    private String community;

    private String contact;

    private String shopname;

    private String serverfanwei;

    private String servertime;

    @Column(name = "zhizhaoNo")
    private String zhizhaono;

    @Column(name = "shenfengNo")
    private String shenfengno;

    @Column(name = "passstateYN")
    private String passstateyn;

    private String telephone;

    private String mark;

    @Column(name = "WeiXinnumber")
    private String weixinnumber;

    @Column(name = "RMmobile")
    private String rmmobile;

    private String keyword;

    @Column(name = "comeFrom")
    private String comefrom;

    @Column(name = "shopType")
    private String shoptype;

    private String experience;

    @Column(name = "agentYN")
    private String agentyn;

    @Column(name = "agentArea")
    private String agentarea;

    private String smallfanwei;

    private String kefu;

    private Double lng;

    private Double lat;

    @Column(name = "DHkefu")
    private String dhkefu;

    @Column(name = "Jpushid")
    private String jpushid;

    private String sex;

    private String agentgrade;

    private String postnumber;

    private String banktype;

    private String stage;

    @Column(name = "URL")
    private String url;

    @Column(name = "corpInfo")
    private String corpinfo;

    private String dbname;

    private String checkuser;

    private Double tlat;

    private Double tlng;

    private Integer age;

    private static final long serialVersionUID = 1L;
}