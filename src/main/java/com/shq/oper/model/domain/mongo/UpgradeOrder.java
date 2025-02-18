package com.shq.oper.model.domain.mongo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: ltg
 * @date: Created in 19:00 2018/12/5
 */
@Data
public class UpgradeOrder  implements Serializable {
    private static final long serialVersionUID = 1;

    private Long userSeq;
}
