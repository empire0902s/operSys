/** 邀请页栏目初始数据**/
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-09-06 14:03:08', 'admin-1', '2018-09-06 14:03:12', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '大兵-笑工优选', 0, '大兵-笑工优选商品');
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-09-06 14:03:09', 'admin-1', '2018-09-06 14:03:09', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '大兵-天天特价', 0, '大兵-天天特价商品');
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-09-06 14:03:10', 'admin-1', '2018-09-06 14:03:10', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '大兵-每日上新', 0, '大兵-每日上新商品');
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-09-06 14:03:11', 'admin-1', '2018-09-06 14:03:11', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '大兵-严选精品', 0, '大兵-严选精品商品');
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-09-06 14:03:13', 'admin-1', '2018-09-06 14:03:13', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '大兵-热销榜', 0, '大兵-热销榜商品');
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-09-06 14:03:14', 'admin-1', '2018-09-06 14:03:14', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '大兵-真实惠', 0, '大兵-真实惠商品');
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-09-06 14:03:15', 'admin-1', '2018-09-06 14:03:15', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '大兵-限量购', 0, '大兵-限量购商品');








ALTER TABLE `t_circle`
	ADD COLUMN `goods_type` SMALLINT(6) NULL COMMENT '商品类型（1，普通商品，2.秒杀）' AFTER `update_admin`,
	DROP COLUMN `goods_type`;



CREATE TABLE `t_deductible` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
	`create_admin` VARCHAR(32) NULL DEFAULT NULL COMMENT '创建人',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
	`update_admin` VARCHAR(32) NULL DEFAULT NULL COMMENT '更新人',
	`user_seq` VARCHAR(32) NOT NULL COMMENT '用户seq',
	`validay_start` DATETIME NOT NULL COMMENT '有效期开始时间',
	`validay_end` DATETIME NOT NULL COMMENT '有效结束时间',
	`status` SMALLINT(6) NOT NULL COMMENT '状态（1.未使用，2.使用中，3.已用完，4.已过期，5.升级清空）',
	`is_locking` SMALLINT(6) NULL DEFAULT '1' COMMENT '是否锁定（1解锁，2锁定）',
	`balance` DECIMAL(18,2) NOT NULL DEFAULT '0.00' COMMENT '抵扣券余额',
	`used_balance` DECIMAL(18,2) NOT NULL DEFAULT '0.00' COMMENT '已抵扣金额',
	`amount` DECIMAL(18,2) NOT NULL DEFAULT '0.00' COMMENT '抵扣券总额',
	`type` SMALLINT(6) NOT NULL COMMENT '券类型（1.代金券，2.抵扣券）',
	`examine_status` SMALLINT(6) NOT NULL DEFAULT '0' COMMENT '审核状态 0 未审核 1 审核通过 2 审核不通过',
	PRIMARY KEY (`id`)
)
COMMENT='抵扣券'
COLLATE='utf8_general_ci'
ENGINE=MyISAM
AUTO_INCREMENT=342
;


CREATE TABLE `t_deductible_introduce` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`title` VARCHAR(100) NULL DEFAULT NULL COMMENT '标题',
	`content` VARCHAR(1000) NULL DEFAULT NULL COMMENT '内容',
	`discount` FLOAT NOT NULL COMMENT '抵扣百分比',
	`is_disabled` BIT(1) NOT NULL COMMENT '是否禁用（0为否，1为是）',
	`create_time` DATETIME NULL DEFAULT NULL COMMENT '创建时间',
	`create_admin` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
	`update_admin` VARCHAR(50) NULL DEFAULT NULL COMMENT '更新人',
	PRIMARY KEY (`id`)
)
COMMENT='抵扣券使用介绍'
COLLATE='utf8_general_ci'
ENGINE=MyISAM
AUTO_INCREMENT=7
;



CREATE TABLE `t_deductible_introduce_detail` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`introduce_id` BIGINT(20) NOT NULL COMMENT '介绍id',
	`create_time` DATETIME NOT NULL,
	`create_admin` VARCHAR(50) NOT NULL,
	`update_time` DATETIME NOT NULL,
	`update_admin` VARCHAR(50) NOT NULL,
	`title` VARCHAR(50) NULL DEFAULT NULL COMMENT '标题',
	`content` VARCHAR(200) NULL DEFAULT NULL COMMENT '内容',
	`sort` SMALLINT(6) NULL DEFAULT NULL COMMENT '排序',
	PRIMARY KEY (`id`)
)
COMMENT='抵扣券介绍明细'
COLLATE='utf8_general_ci'
ENGINE=MyISAM
AUTO_INCREMENT=26
;







CREATE TABLE `t_deductible_use_rule` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`create_time` DATETIME NOT NULL,
	`create_admin` VARCHAR(30) NOT NULL,
	`update_time` DATETIME NOT NULL,
	`update_admin` VARCHAR(30) NOT NULL,
	`discount` SMALLINT(10) NOT NULL COMMENT '折扣',
	`is_disabled` BIT(1) NOT NULL COMMENT '是否禁用（0为否，1为是）',
	PRIMARY KEY (`id`)
)
COMMENT='抵扣券抵扣规则'
COLLATE='utf8_general_ci'
ENGINE=MyISAM
;


CREATE TABLE `t_deductible_product_rule` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(30) NOT NULL COMMENT '商品code',
	`product_name` VARCHAR(30) NOT NULL COMMENT '商品名称',
	`type` SMALLINT(6) NOT NULL COMMENT '产品类型（1.产品广场商品，2.平台商品）',
	`create_time` DATETIME NOT NULL,
	`create_admin` VARCHAR(30) NOT NULL,
	PRIMARY KEY (`id`)
)
COMMENT='抵扣券商品规则'
COLLATE='utf8_general_ci'
ENGINE=MyISAM
;


CREATE TABLE `t_publice_num` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(30) NOT NULL COMMENT '公众号名称',
	`connection_adress` VARCHAR(150) NOT NULL COMMENT '连接地址',
	`floating_content` VARCHAR(150) NOT NULL COMMENT '浮窗文案',
	`code_img` VARCHAR(150) NOT NULL COMMENT '二维码图',
	`is_disabled` BIT(1) NOT NULL COMMENT '是否禁用（0为否，1为是）',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`create_admin` VARCHAR(30) NOT NULL COMMENT '创建人',
	`update_time` DATETIME NOT NULL COMMENT '更新时间',
	`update_admin` VARCHAR(30) NOT NULL COMMENT '更新人',
	PRIMARY KEY (`id`)
)
COMMENT='公众号'
COLLATE='utf8_general_ci'
ENGINE=MyISAM
AUTO_INCREMENT=8
;




