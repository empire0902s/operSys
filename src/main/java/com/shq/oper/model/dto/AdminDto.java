package com.shq.oper.model.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import com.shq.oper.model.domain.primarydb.Resource;

@lombok.Data
public class AdminDto implements Serializable {
    private Long id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    private String realName;
    private String sessionId;
    
    @Transient
    private List<Resource> resources;// 拥有的资源
    @Transient
    private List<Resource> allResources;// 所有资源

    private static final long serialVersionUID = 1L;

}