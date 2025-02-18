package com.shq.oper.service.impl.primarydb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shq.oper.mapper.primarydb.DictMapper;
import com.shq.oper.model.domain.primarydb.Dict;
import com.shq.oper.service.primarydb.DictService;

@Service("dictService")
public class DictServiceImpl extends BaseServiceImpl<Dict, Long> implements DictService {

	@Autowired
	private DictMapper dictMapper;
	
	@Transactional
	@Override
	public void updateDict(Dict dict) {
		dictMapper.updateDict(dict);
	}

}
