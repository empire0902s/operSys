package com.shq.oper.model.dto;


import lombok.Data;

/**
 * 图片上传返回数据Bean
 * @author ljz
 * @date 2018年5月18日
 */
@Data
public class ImageData {

	// 图片路径
	private String src;

	// 图片标题
	private String title;
}
