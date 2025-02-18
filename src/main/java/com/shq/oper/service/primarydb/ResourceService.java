package com.shq.oper.service.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Resource;

/**
 *@author ltg
 *@date 2018/4/18 11:41
 */
public interface ResourceService extends BaseService<Resource, Long> {
    /**
     * 获取子类排序字符
     * @param id
     * @return
     */
    String getSortStr(Page<String> remarkList,long id);

}
