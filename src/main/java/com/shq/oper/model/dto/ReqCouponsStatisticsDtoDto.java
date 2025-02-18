package com.shq.oper.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

@lombok.Data
public class ReqCouponsStatisticsDtoDto implements Serializable {
	private Long id;

	private LocalDateTime start;
	private LocalDateTime end;
	private String activityId;
	private String couponsType;

	private static final long serialVersionUID = 1L;

}