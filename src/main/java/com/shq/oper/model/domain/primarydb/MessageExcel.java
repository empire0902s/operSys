package com.shq.oper.model.domain.primarydb;

import com.shq.oper.model.dto.api.message.IsNeeded;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: ltg
 * @date: Created in 9:46 2018/7/7
 */
@Data
@Table(name = "t_msg_excel")
public class MessageExcel implements Serializable {

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

    // '导入Excel名称.',
    @Column(name = "excel_name")
    private String excelName;

    // '导入批次.',
    @IsNeeded
    @Column(name = "batch")
    private String batch;

    @IsNeeded
    @Column(name = "phone")
    private String phone;

    @IsNeeded
    @Column(name = "is_msg")
    private Integer isMsg;

    @IsNeeded
    @Column(name = "is_app")
    private Integer isApp;

    @IsNeeded
    @Column(name = "is_email")
    private Integer isEmail;

    // '发送内容',
    @IsNeeded
    @Column(name = "msg_content")
    private String msgContent;

//    @Column(name = "sending_platform")
//    private Short sendingPlatform;

    //'消息发送时间类型（0为否立即发送，1为是定时发送,2循环发送）',
    @Column(name = "timing_type")
    private Short timingType;

    //'消息发送时间类型',
    @Column(name = "timing_type_detail")
    private String timingTypeDetail;

    //'状态,0待发送,1已发送',
    @Column(name = "status")
    private Integer status;

    // '完成时间',
    @Column(name = "finish_time")
    private LocalDateTime finishTime;


    private static final long serialVersionUID = 1L;

}
