package com.shq.oper.model.dto;

import java.util.List;


@lombok.Data
public class NavMenuDto {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String title;
	private String icon;
	private Boolean spread;
	private String href;
	private List<NavMenuDto> children;
}
