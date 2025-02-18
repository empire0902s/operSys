package com.shq.oper.model.domain.primarydb;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: ltg
 * @date: Created in 16:18 2018/7/4
 */
@Data
@Table(name = "t_msg_send")
public class MessageSend implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 创建人(id-name)
     */
    @Column(name = "create_admin")
    private String createAdmin;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 最近更新人(id-name)
     */
    @Column(name = "update_admin")
    private String updateAdmin;

    @Column(name = "msg_desc")
    private String msgDesc;

    //1、单个手机号，2、用户角色，3、区域用户，4、全平台
    @Column(name = "receive_type")
    private short receiveType;

    @Column(name = "receive_type_detail")
    private String receiveTypeDetail;

    @Column(name = "msg_type_json")
    private String msgTypeJson;

    @Column(name = "msg_content")
    private String msgContent;

    //短信发送才要，短信类型（1：验证码，2：公告，3：营销信息）
    @Column(name = "message_type")
    private Short messageType;

    @Column(name = "timing_type")
    private Short timingType;

    @Column(name = "timing_type_detail")
    private String timingTypeDetail;

    //新增字段，发送平台（1：520爱之家，2：生活圈）
    @Column(name = "sending_platform")
    private Integer sendingPlatform;

    //新增发送用户数量
    @Column(name = "message_num")
    private Integer messageNum;

    //系统来源
    @Column(name = "from_sys")
    private String fromSys;

    //系统来源code
    @Column(name = "from_sys_code")
    private String fromSysCode;

    //跳转链接
    @Column(name = "jump_url")
    private String jumpUrl;

    @Transient
    private String seqs;

    @Transient
    private String message;


    @Transient
    private String push;


    @Transient
    private String mail;

    @Transient
    private Short msgType;

    @Transient
    private Short appType;


    private static final long serialVersionUID = 1L;

}
