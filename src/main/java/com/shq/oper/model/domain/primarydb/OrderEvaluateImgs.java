package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "t_order_evaluate_imgs")
public class OrderEvaluateImgs implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC") 
    private Long id;

    /**
     * 评论Id
     */
    @Column(name = "order_evaluate_id")
    private Long orderEvaluateId;

    /**
     * 图片地址
     */
    @Column(name = "img_url")
    private String imgUrl;

    private static final long serialVersionUID = 1L;
}