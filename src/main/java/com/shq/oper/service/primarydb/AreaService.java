package com.shq.oper.service.primarydb;


import java.util.List;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Area;

public interface AreaService extends BaseService<Area, Long>{
	
	/**
     * 获取子类排序字符
     * @param resources
     * @param id
     * @return
     */
    String getSortStr(Page<Area> areass,String code);

    /**
     * 根据区域级别查询区域
     * @param sLev
     * @return
     */
	List<Area> selectArea(Area areas);
	
}
