package com.shq.oper.model.dto.api.req;

import java.io.Serializable;
import java.util.List;


@lombok.Data
public class ReqSendMessageDto implements Serializable{
	private static final long serialVersionUID = 1L;


    /**
     * 来源系统
     */
    private String fromSys;
    /**
     * 来源系统Code
     */
    private String fromSysCode;

    /**
     * 操作人
     */
    private String operateAdmin ;
	//消息描述
    private String msgDesc;

    //消息内容
    private String msgContent;

    private List<String> mobiles;

    //1发,0不发
    private Short isMessage;
    //1发,0不发
    private Short isPush;
    //1发,0不发
    private Short isMail;

    //短信发送才要，短信类型（1：验证码，2：公告，3：营销信息）
    private Short messageType;

    //订单id
    private String orderId;

    private Short orderStatus;

    //1：订单消息类型，2：公告，3：升级信息
    private Short msgType;

    private List<Integer> userSeqs;

    //跳转链接
    private String jumpUrl;

    //物流编号
    private String logisticsNumber;

    //1：买单，2：卖单
    private Short orderType;


}