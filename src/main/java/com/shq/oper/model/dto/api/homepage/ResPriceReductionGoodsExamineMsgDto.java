package com.shq.oper.model.dto.api.homepage;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 
*    
* 项目名称：operSys   
* 类名称：ResPriceReductionGoodsExamineMsgDto   
* 类描述：   
* 创建人：ljz   
* 创建时间：2018年8月27日 上午10:13:52   
* 修改人：ljz   
* 修改时间：2018年8月27日 上午10:13:52   
* 修改备注：   
* @version    
*
 */
@Data
public class ResPriceReductionGoodsExamineMsgDto implements Serializable {

	//返回的提示
    private String msg;

    //财务审核失败商品id集合
    private List<String>failIdList;

    private static final long serialVersionUID = 1L;
}
