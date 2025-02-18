package com.shq.oper.model.dto;


import lombok.Data;

/**
 * 图片上传成功后给LayEdit返回如下格式的JSON信息封装.
 * @author ljz
 * @date 2018年5月18日
 */
@Data
public class ImageUploadResultData {

	//结果码,0表示成功,其他失败    
	   private Integer code;    
	 
	   //结果信息,上传失败后返回    
	   private String msg;    
	 
	   //图片数据    
	   private ImageData data;
}
