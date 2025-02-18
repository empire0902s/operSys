package com.shq.oper.model.domain.primarydb.deductible;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: ltg
 * @date: Created in 11:54 2018/11/17
 */
@Data
@Table(name = "t_deductible_product_rule")
public class DeductibleProductRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @OrderBy(value = "DESC")
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 创建人(id-name)
     */
    @Column(name = "create_admin")
    private String createAdmin;

    /**
     * 商品code
     */
    private String code;

    /**
     * 商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 产品类型:(1.品牌广场商品，2.平台产品)
     */
    private Short type;





}
