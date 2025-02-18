package com.shq.oper.model.domain.mongo.wholesalebrand;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author: ltg
 * @date: Created in 10:44 2018/11/15
 */
@Data
@Document(collection="t_lesale_industry")
public class WholesaleIndustry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * 行业名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否展示1:展示，2：不展示
     */
    private Integer isShow;

    private String createTime;

    private String createAdmin;


}
