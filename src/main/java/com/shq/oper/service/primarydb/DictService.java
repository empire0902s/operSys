package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.Dict;

public interface DictService extends BaseService<Dict, Long> {

	void updateDict(Dict dict);

}
