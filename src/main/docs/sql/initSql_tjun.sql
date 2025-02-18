
/**--------Sql 文件------------**/


DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  `name` varchar(20) DEFAULT NULL COMMENT '用户名',
  `pwd` varchar(32) DEFAULT NULL COMMENT '密码',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `real_name` varchar(30) DEFAULT NULL COMMENT '姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统管理员表';


DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `code` varchar(8) DEFAULT NULL COMMENT '编码',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `type` varchar(20) DEFAULT NULL COMMENT '分类',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';


DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  `system_code` varchar(4) NOT NULL COMMENT '系统代码',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `url` varchar(50) DEFAULT NULL COMMENT '资源url路径',
  `sort` smallint(6) DEFAULT NULL COMMENT '排序',
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '类型，1=菜单，2=功能',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `icon` varchar(20) DEFAULT NULL,
  `parent_id` bigint(18) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单资源表';


DROP TABLE IF EXISTS `t_admin_role`;
CREATE TABLE `t_admin_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户-角色关系Id',
  `admin_id` bigint(20) NOT NULL COMMENT '用户Id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  PRIMARY KEY (`id`),
  KEY `idx_admin_role` (`admin_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员与角色中间关系表';


DROP TABLE IF EXISTS `t_role_resource`;
CREATE TABLE `t_role_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色-资源权限关系id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `resource_id` bigint(20) NOT NULL COMMENT '资源权限id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  PRIMARY KEY (`id`),
  KEY `idx_role_resource` (`role_id`,`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单资源中间关系表';



DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `code` varchar(30) NOT NULL COMMENT '代码',
  `dict_key` varchar(50) DEFAULT NULL COMMENT 'key',
  `dict_value` varchar(100) NOT NULL COMMENT '值',
  `parent_id` bigint(18) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';


DROP TABLE IF EXISTS `t_area`;
CREATE TABLE `t_area` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL COMMENT '代码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `lev` varchar(1) DEFAULT NULL COMMENT '层级，1=省，2=市，3=区',
  `parent_code` varchar(50) DEFAULT NULL COMMENT '上级代码',
  `is_disabled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `lev` (`lev`,`parent_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='地区信息表';



DROP TABLE IF EXISTS `t_banner`;
CREATE TABLE `t_banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(name-id)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(name-id)',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  `banner_name` varchar(30) DEFAULT NULL COMMENT 'banner名称',
  `img_path` varchar(150) DEFAULT NULL COMMENT '图片路径',
  `url` varchar(150) DEFAULT NULL COMMENT '跳转地址',
  `order_no` int(11) DEFAULT NULL COMMENT '排序',
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '类型(0App引导页,1首页Banner)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='banner表';




DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  `name` varchar(32) NOT NULL COMMENT '规则名称',
  `batch` varchar(32) NOT NULL COMMENT '批次',
  `activity_goods_rule_id` bigint(20) DEFAULT NULL COMMENT '活动商品规则Id',
  `send_time_start` datetime NOT NULL COMMENT '发放开始时间',
  `send_time_end` datetime NOT NULL COMMENT '发放结束时间',
  `user_role_rule` varchar(200) DEFAULT NULL COMMENT '用户角色规则',
  `receive_num_rule` int(32) NOT NULL COMMENT '用户领取次数0不限次数，限X次',
  `activity_desc` varchar(200) NOT NULL COMMENT '活动说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动表';

ALTER TABLE `t_activity` 
MODIFY COLUMN `receive_num_rule` int(32) COMMENT '用户领取次数0不限次数，限X次' AFTER `user_role_rule`,
MODIFY COLUMN `activity_desc` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '活动说明' AFTER `receive_num_rule`;

DROP TABLE IF EXISTS `t_activity_coupons`;
CREATE TABLE `t_activity_coupons` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户-角色关系Id',
  `activity_id` bigint(20) NOT NULL COMMENT '活动Id',
  `coupons_id` bigint(20) NOT NULL COMMENT '优惠券id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  PRIMARY KEY (`id`),
  KEY `idx_activity_coupons` (`activity_id`,`coupons_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动与优惠券中间关系表';




DROP TABLE IF EXISTS `t_activity_goods_rule`;
CREATE TABLE `t_activity_goods_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  `name` varchar(32) NOT NULL COMMENT '规则名称',
  `type` int(11) NOT NULL COMMENT '规则类型｛组合商品,1,2,3,4级类目｝',
  `rule_des` varchar(200) DEFAULT NULL COMMENT '规则说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券商品规则';




DROP TABLE IF EXISTS `t_goods_product`;
CREATE TABLE `t_goods_product` (
  `oper_sys_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `p_id` varchar(32) DEFAULT NULL COMMENT '商品ID',
  `product_code` varchar(100) DEFAULT NULL COMMENT '商品货号Code',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `category_id` varchar(20) DEFAULT NULL COMMENT '一级分类',
  `genre_id` varchar(20) DEFAULT NULL COMMENT '二级分类',
  `three_id` varchar(20) DEFAULT NULL COMMENT '三级分类',
  `four_id` varchar(20) DEFAULT NULL COMMENT '四级分类',
  `sort` varchar(20) DEFAULT NULL COMMENT '排序',
  `financial_audit` varchar(20) DEFAULT NULL COMMENT '财务审核',
  `financial_auditor` varchar(30) DEFAULT NULL COMMENT '财务审核人',
  `financial_auditTime` varchar(50) DEFAULT NULL COMMENT '财务审核时间',
  `financial_audit_remark` varchar(200) DEFAULT NULL COMMENT '财务审核备注',
  `market_audit` varchar(20) DEFAULT NULL COMMENT '市场审核',
  `market_auditor` varchar(30) DEFAULT NULL COMMENT '市场审核人',
  `marketA_audit_time` varchar(50) DEFAULT NULL COMMENT '市场审核时间',
  `market_audit_remark` varchar(200) DEFAULT NULL COMMENT '市场审核备注',
  `activity_state` varchar(11) DEFAULT NULL COMMENT '活动状态',
  `is_sale` varchar(11) DEFAULT NULL COMMENT '上下架状态  0待审核 1上架   2下架	',
  `is_sale_date` varchar(50) DEFAULT NULL COMMENT '上下架时间',
  PRIMARY KEY (`oper_sys_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订货商品信息';



DROP TABLE IF EXISTS `t_activity_goods_rule_goods_product`;
CREATE TABLE `t_activity_goods_rule_goods_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户-角色关系Id',
  `activity_goods_rule_id` bigint(20) NOT NULL COMMENT '商品规则Id',
  `goods_product_pid` varchar(32) NOT NULL COMMENT '商品Id',
  `rule_type` int(1) NOT NULL COMMENT '规则类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  PRIMARY KEY (`id`),
  KEY `idx_admin_role` (`activity_goods_rule_id`,`goods_product_pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品规则对应商品中间关系表';




DROP TABLE IF EXISTS `t_goods_product_price`;
CREATE TABLE `t_goods_product_price` (
  `oper_sys_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `p_id` varchar(32) DEFAULT NULL COMMENT '价格id',
  `product_code` varchar(100) DEFAULT NULL COMMENT '商品货号Code',
  `product_sku` varchar(100) DEFAULT NULL COMMENT 'Sku',
  `platform_price` varchar(100) DEFAULT NULL COMMENT '平台供货价',
  `retail_price` varchar(100) DEFAULT NULL COMMENT '零售价',
  `distribution_price` varchar(100) DEFAULT NULL COMMENT '平台供货单价',
  `price_distribution` varchar(100) DEFAULT NULL COMMENT '分销价',
  `activity_start_time` varchar(100) DEFAULT NULL COMMENT '活动开始时间',
  `activity_finish_time` varchar(100) DEFAULT NULL COMMENT '活动结束时候',
  `seckill_price` varchar(100) DEFAULT NULL COMMENT '秒杀价格',
  `seckill_commission` varchar(100) DEFAULT NULL COMMENT '活动分销商佣金',
  `seckill_dl_mmission` varchar(100) DEFAULT NULL COMMENT '活动代理商佣金',
  `activity_quantity` varchar(11) DEFAULT NULL COMMENT '活动数量',
  `data_sync_state` int(1) DEFAULT '0' COMMENT '活动数据同步状态 0未同步,1 已同步',
  `up_down_state` int(1) DEFAULT '0' COMMENT '上下架状态 0 未设置，1上架，2下架',
  PRIMARY KEY (`oper_sys_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订货商品价格信息';



DROP TABLE IF EXISTS `t_coupons`;
CREATE TABLE `t_coupons` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  `name` varchar(32) NOT NULL COMMENT '优惠券名称',
  `batch` bigint(20) NOT NULL COMMENT '优惠券批次',
  `activity_id` varchar(32) DEFAULT NULL COMMENT '活动Id',
  `category_rule_id` bigint(20) NOT NULL COMMENT '优惠品类规则Id',
  `coupons_rule_id` bigint(20) NOT NULL COMMENT '优惠规则Id',
  `use_platform` varchar(120) DEFAULT NULL COMMENT '可使用平台编码，可多个用,号隔开',
  `send_time_start` datetime NOT NULL COMMENT '发放开始时间',
  `send_time_end` datetime NOT NULL COMMENT '发放结束时间',
  `vali_day_type` int(11) NOT NULL COMMENT '有效期方式,0指定时间,1领取后天数',
  `vali_day_type_detail` varchar(500) DEFAULT NULL COMMENT '有效期明细{vali_day_start,vali_day_end}{vali_day_num}',
  `send_num` int(11) NOT NULL COMMENT '发放数量0为不限量',
  `send_type` int(11) NOT NULL COMMENT '发放方式,0系统自动发放到用户,1,用户领取',
  `receive_num_rule` int(32) NOT NULL COMMENT '用户领取次数0不限次数，限X次',
  `coupon_des` varchar(200) DEFAULT NULL COMMENT '优惠券说明',
  `finan_status` int(32) DEFAULT '0' COMMENT '0：未通过，1：通过 默认值：0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券';

ALTER TABLE `t_coupons` 
ADD COLUMN `finan_auditor` varchar(30) DEFAULT NULL COMMENT '财务审核人',
ADD COLUMN `finan_auditTime` datetime DEFAULT NULL COMMENT '财务审核时间',
ADD COLUMN `finan_audit_remark` varchar(200) DEFAULT NULL COMMENT '财务审核备注';

ALTER TABLE `t_coupons` 
MODIFY COLUMN `batch` varchar(32) NOT NULL COMMENT '优惠券批次' AFTER `name`,
MODIFY COLUMN `category_rule_id` bigint(20) COMMENT '优惠品类规则Id' AFTER `activity_id`,
MODIFY COLUMN `send_type` int(11) NOT NULL DEFAULT 1 COMMENT '发放方式,0系统自动发放到用户,1,用户领取' AFTER `send_num`,
MODIFY COLUMN `coupons_rule_id` bigint(20) COMMENT '优惠规则Id' AFTER `category_rule_id`;


DROP TABLE IF EXISTS `t_coupons_type_rule`;
CREATE TABLE `t_coupons_type_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  `name` varchar(32) NOT NULL COMMENT '品类规则名称',
  `model` int(11) NOT NULL COMMENT '优惠模式{1固定金额,2浮动金额}',
  `type` int(11) NOT NULL COMMENT '优惠类型{满减，立减，折扣}',
  `min_spend_money` decimal(18,2) DEFAULT '0.00' COMMENT '最低消费金额,满足后才可使用',
  `amt_full_over` decimal(18,2) DEFAULT NULL COMMENT '满减_满足金额',
  `amt_full_reduce` decimal(18,2) DEFAULT NULL COMMENT '满减_优惠减少金额',
  `amt_discount` decimal(9,1) DEFAULT NULL COMMENT '折扣',
  `amt_sub` decimal(18,2) DEFAULT NULL COMMENT '立减金额',
  `type_desc` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券金额模式规则';

/**删除 [满减_满足金额]字段**/
ALTER TABLE `t_coupons_type_rule` 
DROP COLUMN `amt_full_over`,
MODIFY COLUMN `min_spend_money` decimal(18, 2) NOT NULL COMMENT '最低消费金额,满足后才可使用' AFTER `type`,
MODIFY COLUMN `model` int(11) DEFAULT 1 COMMENT '优惠模式{1固定金额,2浮动金额}' AFTER `name`;



DROP TABLE IF EXISTS `t_coupons_user`;
CREATE TABLE `t_coupons_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  `user_seq` varchar(32) NOT NULL COMMENT '用户标识',
  `activity_id` varchar(32) DEFAULT NULL COMMENT '活动Id',
  `coupons_id` varchar(32) DEFAULT NULL COMMENT '优惠券关联',
  `receive_src` varchar(32) NOT NULL COMMENT '来源',
  `vali_day_start` datetime NOT NULL COMMENT '有效期开始时间',
  `vali_day_end` datetime NOT NULL COMMENT '有效期结束时间',
  `coupons_type_model` int(11) NOT NULL COMMENT '优惠模式{1固定金额,2浮动金额}',
  `coupons_type` int(11) NOT NULL COMMENT '优惠类型{满减，折扣，立减}',
  `min_spend_money` decimal(18,2) DEFAULT '0.00' COMMENT '最低消费金额,满足后才可使用',
  `amt_full_over` decimal(18,2) DEFAULT NULL COMMENT '满减_满足金额',
  `amt_full_reduce` decimal(18,2) DEFAULT NULL COMMENT '满减_优惠减少金额',
  `amt_discount` int(1) DEFAULT NULL COMMENT '折扣',
  `amt_sub` decimal(18,2) DEFAULT NULL COMMENT '立减金额',
  `coupons_type_desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `coupons_status` int(1) NOT NULL COMMENT '优惠券状态枚举,1未使用,2锁定中,3已使用,4已过期',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `use_order` varchar(32) DEFAULT NULL COMMENT '使用的单号',
  `use_order_money` decimal(18,2) DEFAULT '0.00' COMMENT '订单原金额',
  `use_spend_money` decimal(18,2) DEFAULT '0.00' COMMENT '订单优惠金额',
  `use_desc` varchar(200) DEFAULT NULL COMMENT '使用描述',
  `phone` varchar(32) DEFAULT NULL COMMENT '用户手机号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户优惠券';





DROP TABLE IF EXISTS `t_coupons_area_rule`;
CREATE TABLE `t_coupons_area_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  `name` varchar(32) NOT NULL COMMENT '规则名称',
  `lev` int(11) NOT NULL COMMENT '区级别{1全国,2省级,3市级,4县区级,5街道级}',
  `lev_type` bit(1) NOT NULL COMMENT '区域类型{包含,排除}）',
  `lev_range_detail` varchar(32) DEFAULT NULL COMMENT '品类范围明细，标识字符串',
  `area_desc` varchar(200) NOT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='区域规则';





DROP TABLE IF EXISTS `t_coupons_category_rule`;
CREATE TABLE `t_coupons_category_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_admin` varchar(32) DEFAULT NULL COMMENT '创建人(id-name)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',
  `is_disabled` bit(1) DEFAULT b'0' COMMENT '是否禁用（0为否，1为是）',
  `category_name` varchar(32) NOT NULL COMMENT '品类规则名称',
  `category_range` varchar(32) DEFAULT NULL COMMENT '品类范围{1234级类目,活动类型,组合单品 }',
  `category_range_detail` varchar(32) DEFAULT NULL COMMENT '品类范围明细，标识字符串',
  `category_desc` varchar(200) DEFAULT NULL COMMENT '使用描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券品类规则';

