package com.shq.oper.model.dto.api.res;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "1007返回活动列表接口DTO", description = "返回活动列表接口DTO")
@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ResActivityListDataDto implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String fromSys;	//来源系统
	
	private String fromSysCode;	//来源系统Code

	
    private String id;

    /**是否启用**/
    private String isDisabled;
    
    /**
     * 规则名称
     */
    private String name;

    /**
     * 批次
     */
    private String batch;

    
    /**
     * 活动开始时间
     */
    private String sendTimeStartStr;

    /**
     * 活动结束时间
     */
    private String sendTimeEndStr;


    /**
     * 活动说明
     */
    private String activityDesc;
    
    /**
     * 用户角色范围  0:所有人  1: 新人
     */
    private String userRoleRule;

	
	
}