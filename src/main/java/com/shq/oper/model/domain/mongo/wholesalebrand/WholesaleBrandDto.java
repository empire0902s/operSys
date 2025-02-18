package com.shq.oper.model.domain.mongo.wholesalebrand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: ltg
 * @date: Created in 14:26 2018/10/7
 */
@lombok.Data
@lombok.NoArgsConstructor
public class WholesaleBrandDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<String> brandLogoImg=new ArrayList<>();
	private List<String> brandName=new ArrayList<>();
	private List<String> jumpTarget=new ArrayList<>();
	private List<Integer> sortNum=new ArrayList<>();
	private List<String> columnId=new ArrayList<>();
	
}
