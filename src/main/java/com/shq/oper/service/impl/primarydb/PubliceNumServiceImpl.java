package com.shq.oper.service.impl.primarydb;

import com.shq.oper.model.domain.primarydb.deductible.DeductibleIntroduce;
import com.shq.oper.model.dto.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.primarydb.PubliceNumMapper;
import com.shq.oper.model.domain.primarydb.PubliceNum;
import com.shq.oper.service.primarydb.PubliceNumService;

@Service("publiceNumService")
public class PubliceNumServiceImpl extends BaseServiceImpl<PubliceNum, Long> implements PubliceNumService {
	
	@Autowired
	private PubliceNumMapper publiceNumMapper;

	@Override
	public Page<PubliceNum> queryAllPubliceNum(PubliceNum entity) {
		return publiceNumMapper.queryAllPubliceNum(entity);
	}

	@Override
	public Msg<String> enableAndDisbled(PubliceNum entity) {

		//禁用全部数据
//		DeductibleIntroduce tmpDeductibleIntroduce=new DeductibleIntroduce();
		entity.setIsDisabled(true);
		publiceNumMapper.updateAllToDisabled();
		//启用当前数据
		publiceNumMapper.enableById(entity);
		return Msg.ok("操作成功");
	}
}