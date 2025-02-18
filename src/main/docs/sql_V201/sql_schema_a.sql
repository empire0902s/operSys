


/**--------Sql 修改数据表-----V201------------**/

ALTER TABLE `t_activity` 
ADD COLUMN `title` varchar(100) COMMENT '活动宣传标题' AFTER `batch`,
ADD COLUMN `from_sys` varchar(32) DEFAULT 'operSys' COMMENT '活动来源系统' AFTER `title`,
ADD COLUMN `from_sys_code` varchar(50) DEFAULT 'operSys_Code' COMMENT '活动来源系统Code' AFTER `from_sys`;


ALTER TABLE `t_activity_goods_rule` 
ADD COLUMN `from_sys` varchar(32) DEFAULT 'operSys' COMMENT '活动来源系统' AFTER `is_disabled`,
ADD COLUMN `from_sys_code` varchar(50) DEFAULT 'operSys_Code' COMMENT '活动来源系统Code' AFTER `from_sys`;

ALTER TABLE `t_coupons` 
MODIFY COLUMN `coupon_des` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '优惠券说明' AFTER `content_disabled_img`,
ADD COLUMN `from_sys` varchar(32) DEFAULT 'operSys' COMMENT '活动来源系统' AFTER `use_platform`,
ADD COLUMN `from_sys_code` varchar(50) DEFAULT 'operSys_Code' COMMENT '活动来源系统Code' AFTER `from_sys`;


ALTER TABLE `t_coupons_type_rule` 
ADD COLUMN `from_sys` varchar(32) DEFAULT 'operSys' COMMENT '活动来源系统' AFTER `is_disabled`,
ADD COLUMN `from_sys_code` varchar(50) DEFAULT 'operSys_Code' COMMENT '活动来源系统Code' AFTER `from_sys`;


ALTER TABLE `t_msg_send` 
ADD COLUMN `from_sys` varchar(32) DEFAULT 'operSys' COMMENT '活动来源系统' AFTER `update_admin`,
ADD COLUMN `from_sys_code` varchar(50) DEFAULT 'operSys_Code' COMMENT '活动来源系统Code' AFTER `from_sys`;




DROP TABLE IF EXISTS `t_order_evaluate`;
CREATE TABLE `t_order_evaluate`  (
  `id` bigint(12) NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人',
	`order_no` varchar(100) NOT NULL COMMENT '订单编号',
	`goods_code` varchar(100) NOT NULL COMMENT '商品货号Code',
  `evaluate_time` datetime DEFAULT NULL COMMENT '评价时间',	
	`user_seq` varchar(100) NOT NULL COMMENT '评价用户seq',	
	`evaluate_content` text NOT NULL COMMENT '评价内容',		
  `evaluate_lev` smallint(6) DEFAULT 5 COMMENT '评价等级1-5',
  `is_anonymous` smallint(6) DEFAULT 0 COMMENT '是否匿名0:实名,1匿名',
  `is_top` smallint(6) DEFAULT 0 COMMENT '是否置顶0不置顶,1置顶',
  `append_evaluate_id` bigint(18) DEFAULT NULL COMMENT '追加评论Id(末尾为空)',
  `audit_stats` smallint(6) DEFAULT 0 COMMENT '审核状态)待审核,1审核通过',
  `audit_admin` varchar(30) DEFAULT NULL COMMENT '审核人',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_reply` varchar(500) DEFAULT NULL COMMENT '后台人员回复',
  `audit_desc` varchar(200) DEFAULT NULL COMMENT '审核备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单评价';


DROP TABLE IF EXISTS `t_order_evaluate_likes`;
CREATE TABLE `t_order_evaluate_likes`  (
  `id` bigint(12) NOT NULL AUTO_INCREMENT,
  `order_evaluate_id` bigint(18) NOT NULL COMMENT '评论Id',
  `likes_time` datetime DEFAULT NULL COMMENT '点赞时间',	
	`user_seq` varchar(100) NOT NULL COMMENT '评价用户seq',	
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单评价点赞记录表';

DROP TABLE IF EXISTS `t_order_evaluate_imgs`;
CREATE TABLE `t_order_evaluate_imgs`  (
  `id` bigint(12) NOT NULL AUTO_INCREMENT,
  `order_evaluate_id` bigint(18) NOT NULL COMMENT '评论Id',
	`img_url` varchar(500) NOT NULL COMMENT '图片地址',	
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单评价图片表';




ALTER TABLE `t_msg_send_detail`
	ADD COLUMN `msg_id` BIGINT(20) NOT NULL COMMENT '关联消息id' AFTER `send_status`;

ALTER TABLE `t_msg_send`
	ADD COLUMN `sending_platform` SMALLINT(1) NOT NULL COMMENT '发送平台（1为爱之家，2为生活圈）' AFTER `timing_type_detail`;

ALTER TABLE `t_msg_send_detail`
	ADD COLUMN `remark` VARCHAR(800) NULL COMMENT '发送失败原因' AFTER `mq_queue`;

ALTER TABLE `t_msg_send`
	ADD COLUMN `message_num` INT(6) NULL COMMENT '发送用户数量' AFTER `sending_platform`;


ALTER TABLE `t_msg_send`
	ALTER `sort_no` DROP DEFAULT;
ALTER TABLE `t_msg_send`
	CHANGE COLUMN `sort_no` `message_type` SMALLINT(1) NULL COMMENT '短信发送类型（1为验证码，2为公告，3为营销信息）' AFTER `msg_content`;


ALTER TABLE `t_msg_send_detail`
	ADD COLUMN `message_type` SMALLINT(1) NULL DEFAULT NULL COMMENT '短信类型（1：验证码，2：公告，3：营销信息）' AFTER `remark`;


