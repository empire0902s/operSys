package com.shq.oper.model.dto.api.message;

/**
 * @author: ltg
 * @date: Created in 16:10 2018/6/30
 */
@lombok.Data
public class SendMessageParamDto {
    private static final long serialVersionUID = 1L;

    //消息标题
    private String title;

    /**
     * 自定义消息直接写内容。如果使用有参数的模板写参数，多个参数值以英文逗号 ，
     * 间隔。如果是无参数模板写模板编号
     */
    private String content;

    /**
     * 接收消息的用户，多接收用户使用英文分号 ; 间隔
     */
    private String aisleAccount;

    /**
     * 目前可用通道有：手机短信（sms）,推送(sdk),邮件(email)
     */
    private String aisle;

    /**
     * 消息发送的渠道(云之讯 天下通 163 sina qq 极光推送)
     */
    private String channel;

    /**
     * 消息的来源（需要在消息后台添加）(来源: 消息中心 单点登录 物流系统 餐饮 周边 招聘)
     */
    private String origin;

    /**
     * 应用系统编号
     */
    private Integer appId;



    //通知 公告
    private String type;

    //纯文本 文件 超文本
    private String style;

    //模板编号
    private Long template;

    //附件，多个附件分号隔开
    private String attachment;

    //摘要
    private String summary;

    //接收用户
    private String userId;

    //发送账户
    private String senderAccount;

    //接收人
    private String receiver;

    //发送人
    private String sender;

    private Long msgId;

    private String extras;


}
