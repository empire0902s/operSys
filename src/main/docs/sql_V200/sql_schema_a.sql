


/**--------Sql 修改数据表-----V200------------**/

ALTER TABLE `operatedb`.`t_coupons` 
MODIFY COLUMN `coupon_des` varchar(20000) DEFAULT NULL COMMENT '优惠券说明' AFTER `content_disabled_img`;


DROP TABLE IF EXISTS `t_user_behavior_statistics`;
CREATE TABLE `t_user_behavior_statistics`  (
  `id` bigint(12) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `statistics_time` datetime DEFAULT NULL COMMENT '统计日期',
  `uv` int(12) DEFAULT NULL COMMENT '独立访客量',
  `pv` int(12) DEFAULT NULL COMMENT '页面浏览量',
  `ip` int(12) DEFAULT NULL COMMENT 'ip访问量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT='网站统计基础表';

	

DROP TABLE IF EXISTS `t_banner_auxiliary`;
CREATE TABLE `t_banner_auxiliary`  (
  `id` bigint(12) NOT NULL AUTO_INCREMENT,
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人',
  `logo_path` varchar(250) DEFAULT NULL COMMENT 'logo图片地址',
  `banner_img_path` varchar(250) DEFAULT NULL COMMENT 'banner图片地址',
  `activity_img_path` varchar(250) DEFAULT NULL COMMENT '活动图片地址',
  `parent_id` bigint(12) DEFAULT NULL COMMENT '主表标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Banner附加表';


DROP TABLE IF EXISTS `t_banner_model_goods_detail`;
CREATE TABLE `t_banner_model_goods_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户-角色关系Id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',	
  `banner_id` bigint(20) NOT NULL COMMENT 'BannerId',
  `goods_code` varchar(50) NOT NULL COMMENT '商品Code',
  `first_class_id` varchar(50) DEFAULT NULL COMMENT '1级类目Id',
  `second_class_id` varchar(50) DEFAULT NULL COMMENT '2级类目Id',
  `three_class_id` varchar(50) DEFAULT NULL COMMENT '3级类目Id',
  `four_class_id` varchar(50) DEFAULT NULL COMMENT '4级类目Id',
  `sort_no` smallint(6) DEFAULT 1000 COMMENT '排序',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='栏目模块对应商品详情';





DROP TABLE IF EXISTS `t_msg_send`;
CREATE TABLE `t_msg_send` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',	
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',	
  `msg_desc`  varchar(200) NOT NULL COMMENT '消息描述',	
  `receive_type` smallint(6) NOT NULL DEFAULT '1' COMMENT '接收信息类别：1选择手机号,2选择角色,3选择区域',
  `receive_type_detail` varchar(150) DEFAULT NULL COMMENT '接收信息类别明细',
  `msg_type_json` varchar(200) NOT NULL  COMMENT '消息类型Json{isMsg:ture,isApp:false,isEmail:false}',
  `msg_content` varchar(1500) DEFAULT NULL COMMENT '消息内容',
  `sort_no` smallint(6) DEFAULT 1000 COMMENT '排序',
  `timing_type` smallint(1) DEFAULT '0' COMMENT '消息发送时间类型（0为否立即发送，1为是定时发送,2循环发送）',
  `timing_type_detail` varchar(250) DEFAULT NULL COMMENT '消息发送时间类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息发送设置';

DROP TABLE IF EXISTS `t_msg_send_detail`;
CREATE TABLE `t_msg_send_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',	
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',	
  `user_seq`  varchar(200) NOT NULL COMMENT '用户seq',		
  `user_phone`  varchar(200) NOT NULL COMMENT '用户手机号冗余',		
  `msg_desc`  varchar(200) NOT NULL COMMENT '消息描述冗余',	
  `msg_type` varchar(200) NOT NULL  COMMENT '消息类型isMsg,isApp,isEmail',
  `send_time` datetime DEFAULT NULL COMMENT '消息发送时间',
  `finish_time` datetime DEFAULT NULL COMMENT '发送完成时间(失败或成功才记录)',
  `send_status` smallint(1) DEFAULT '0' COMMENT '消息发送状态（0发送中,1发送成功,2发送失败）',	
  `msg_content` varchar(1500) DEFAULT NULL COMMENT '消息内容',
  `mq_queue`  varchar(200) NOT NULL COMMENT '消息队列名称',	
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息发送详情';

DROP TABLE IF EXISTS `t_msg_excel`;
CREATE TABLE `t_msg_excel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Excel列序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',	
  `excel_name` varchar(32) NOT NULL COMMENT '导入Excel名称.',	
  `batch` varchar(32) NOT NULL COMMENT '导入批次.',
  `phone` varchar(32) NOT NULL COMMENT '手机号',
  `is_msg` int(1) DEFAULT '0' COMMENT '0是否发送短信',
  `is_app` int(1) DEFAULT '0' COMMENT '0否发送推送',
  `is_email` int(1) DEFAULT '0' COMMENT '否发发送Email',
  `msg_content` varchar(32) NOT NULL COMMENT '发送内容',
  `timing_type` smallint(1) DEFAULT '0' COMMENT '消息发送时间类型（0为否立即发送，1为是定时发送,2循环发送）',
  `timing_type_detail` varchar(250) DEFAULT NULL COMMENT '消息发送时间类型',
  `status` int(1) DEFAULT '0' COMMENT '状态,0待发送,1已发送',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Excel消息导入';



