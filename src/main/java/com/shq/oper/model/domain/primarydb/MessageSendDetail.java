package com.shq.oper.model.domain.primarydb;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: ltg
 * @date: Created in 16:27 2018/7/4
 */
@Data
@Table(name = "t_msg_send_detail")
public class MessageSendDetail implements Serializable {
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

    @Column(name = "user_seq")
    private String userSeq;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "msg_desc")
    private String msgDesc;

    @Column(name = "msg_type")
    private String msgType;

    @Column(name = "send_time")
    private LocalDateTime sendTime;

    @Column(name = "finish_time")
    private LocalDateTime finishTime;

    //0发送中,1发送成功,2发送失败,3部分成功,4发送异常
    @Column(name = "send_status")
    private Short sendStatus;

    @Column(name = "msg_content")
    private String msgContent;

    @Column(name = "mq_queue")
    private String mqQueue;

    @Column(name = "msg_id")
    private Long msgId;

    //短信发送才要，短信类型（1：验证码，2：公告，3：营销信息）
    @Column(name = "message_type")
    private Short messageType;

    //失败原因备注
    @Column(name = "remark")
    private String remark;

    @Column(name = "msg_send_id")
    private Long msgSendId;

    //一部返回状态
    @Column(name = "message_return_type")
    private String messageReturnType;

    @Transient
    private LocalDateTime startTime;

    @Transient
    private LocalDateTime endTime;

    @Transient
    private Integer sendingPlatform;

    @Transient
    private Integer offset;

    @Transient
    private String jumpUrl;

    private static final long serialVersionUID = 1L;
}
