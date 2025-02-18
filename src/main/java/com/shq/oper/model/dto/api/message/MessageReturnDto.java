package com.shq.oper.model.dto.api.message;

/**
 * @author: ltg
 * @date: Created in 10:10 2018/7/3
 */
public class MessageReturnDto {
    private static final long serialVersionUID = 1L;

    //消息id
    private int msgId;

    //消息编码
    private String msgCode;

    //标题
    private String title;

    //摘要
    private String summary;

    //内容
    private String content;

    //原始消息
    private String original;

    //发送人
    private String sender;

    //发送人
    private String senderBy;

    //发送账户
    private String senderAccount;

    //接收人
    private String receiver;

    //接收用户
    private String userId;

    //接收通道用户号
    private String aisleAccount;

    //通道名
    private String aisle;

    //渠道名
    private String channel;

    //来源(来源: 消息中心|单点登录|物流系统|餐饮|周边|招聘)
    private String origin;

    //应用系统编号
    private String appId;

    //通知 公告
    private String type;

    //纯文本|文件|超文本
    private String style;

    //1成功 0失败
    private String status;

    //提交
    private String submitTime;

    //发送
    private String sendTime;

    //
    private String takeTime;

    private String readTime;

    //模板编号
    private String template;

    //多个附件逗号隔开
    private String attachment;

    //失败备注
    private String remark;

    //接受失败的用户 多个用户封号分隔
    private String failAccount;

    //重试次数
    private String retryCount;

}
