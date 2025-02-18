package com.shq.oper.service.impl.primarydb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.primarydb.DeductibleIntroduceMapper;
import com.shq.oper.model.domain.primarydb.deductible.DeductibleIntroduce;
import com.shq.oper.model.dto.Msg;
import com.shq.oper.service.primarydb.DeductibleIntroduceService;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: ltg
 * @date: Created in 14:28 2018/11/17
 */
@Service("deductibleIntroduceService")
@Slf4j
public class DeductibleIntroduceServiceImpl  extends BaseServiceImpl<DeductibleIntroduce, Long> implements DeductibleIntroduceService {

	@Autowired
	private DeductibleIntroduceMapper deductibleIntroduceMapper;
	
	
	@Transactional
	@Override
	public Msg<String> enableOneAndDisabledOthers(DeductibleIntroduce deductibleIntroduce) {
		try {
			//禁用全部数据
			DeductibleIntroduce tmpDeductibleIntroduce=new DeductibleIntroduce();
			tmpDeductibleIntroduce.setIsDisabled(true);
			deductibleIntroduceMapper.updateAllToDisabled();
			//启用当前数据
			deductibleIntroduceMapper.enableById(deductibleIntroduce);
			return Msg.ok("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return Msg.error("操作失败");
		}
		
	}


	@Override
	public Page<DeductibleIntroduce> queryAllDeductibleIntroduce(DeductibleIntroduce entity) {
		return deductibleIntroduceMapper.queryAllDeductibleIntroduce(entity);
	}
}
