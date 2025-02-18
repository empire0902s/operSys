package com.shq.oper.model.dto.api.message;

/**
 * @author: ltg
 * @date: Created in 9:49 2018/7/3
 */
@lombok.Data
public class GetMessageListParamDto {
    private static final long serialVersionUID = 1L;

    private int pageNo;

    private int pageSize;

    //通道名(电子邮件|即时通讯|平台推送|手机短信|协议推送)
    private String aisle;

    //1成功 0失败
    private String status;

    //来源
    private String origin;

    //通知|公告|私信|回执
    private String type;

    //纯文本|文件|超文本
    private String style;

    //开始格式：2018-05-04
    private String submitTimeStart;

    private String submitTimeEnd;
}
