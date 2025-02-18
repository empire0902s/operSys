package com.shq.oper.model.dto.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.io.Serializable;


@ApiModel(value="请求发圈数据接口DTO")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.ToString
public class ReqCircleDataDto implements Serializable{
	private static final long serialVersionUID = 1L;


	private Long id;
	/**
	 *分享次数
	 */
	@Column(name = "share_num")
	private int shareNum;

	/**
	 *真实分享次数
	 */
	@Column(name = "real_share")
	private int realShare;

	/**
	 *点击次数
	 */
	@Column(name = "click_num")
	private int clickNum;

	/**
	 *点击人数
	 */
	@Column(name = "click_people_num")
	private int clickPeopleNum;

	/**
	 * 查看次数
	 */
	@Column(name = "reed_num")
	private int reedNum;

	/**
	 * 查看人数
	 */
	@Column(name = "reed_people_num")
	private int reedPeopleNum;

	@ApiModelProperty(hidden=true)
	private String nowTime;	//当前系统时间
	
}