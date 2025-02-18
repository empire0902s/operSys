package com.shq.oper.model.domain.primarydb;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "t_order_evaluate_likes")
public class OrderEvaluateLikes implements Serializable {
    @Id
    @GeneratedValue(generator = "JDBC") 
    private Long id;

    /**
     * 评论Id
     */
    @Column(name = "order_evaluate_id")
    private Long orderEvaluateId;

    /**
     * 点赞时间
     */
    @Column(name = "likes_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime likesTime;

    /**
     * 评价用户seq
     */
    @Column(name = "user_seq")
    private String userSeq;

    private static final long serialVersionUID = 1L;
}