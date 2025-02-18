


/**--------Sql 初始化数据-----V110------------**/

ALTER TABLE `t_activity` 
ADD COLUMN `use_Type` int(8) NOT NULL DEFAULT 1 COMMENT '活动类型(1:优惠券活动,2:商品活动)' AFTER `batch`;

DROP TABLE IF EXISTS `t_activity_goods_rule_details`;
CREATE TABLE `t_activity_goods_rule_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户-角色关系Id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_admin` varchar(32) DEFAULT NULL COMMENT '最近更新人(id-name)',	
  `activity_goods_rule_id` bigint(20) NOT NULL COMMENT '商品规则Id',
  `rule_type` int(1) NOT NULL COMMENT '规则类型',
  `ref_sign_value` varchar(32) NOT NULL COMMENT '规则关联值(如商品则商品Code,如供应商则供应商code)',
  `ref_sign_local_id` varchar(32) DEFAULT NULL COMMENT '规则本地数据Id',
  `ref_sign_name` varchar(32) DEFAULT NULL COMMENT '关联值名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品规则对应商品中间关系表';



ALTER TABLE `t_coupons` 
ADD COLUMN `receive_num` int(0) NOT NULL DEFAULT 0 COMMENT '已经领取数量' AFTER `send_num`;

ALTER TABLE `t_coupons` 
ADD COLUMN `coupons_href_url` varchar(255) COMMENT '优惠券去使用跳转连接。为空默认跳转首页' AFTER `receive_num_rule`,
ADD COLUMN `content_img` varchar(255) COMMENT '优惠券内容图片 为空使用默认图片' AFTER `coupons_href_url`,
ADD COLUMN `content_disabled_img` varchar(255) COMMENT '优惠券内容禁用图片 为空使用默认图片' AFTER `content_img`,
MODIFY COLUMN `coupon_des` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '优惠券说明' AFTER `content_disabled_img`;

ALTER TABLE `t_coupons` 
ADD INDEX `key_coupons_batch`(`batch`);

ALTER TABLE `t_activity` 
ADD UNIQUE INDEX `uq_activity_batch`(`batch`);


ALTER TABLE `t_coupons_user` 
ADD INDEX `key_couponsUser_sqe`(`user_seq`);


ALTER TABLE `t_goods_product` 
ADD COLUMN `company` varchar(50) COMMENT '供货商SEQ' AFTER `four_id`,
ADD COLUMN `company_name` varchar(120) COMMENT '供货商名称' AFTER `company`;



DROP TABLE IF EXISTS `t_user_behavior_statistics`;
CREATE TABLE `t_user_behavior_statistics`  (
  `id` bigint(12) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `statistics_time` datetime(0) DEFAULT NULL COMMENT '统计日期',
  `uv` int(12) DEFAULT NULL COMMENT '独立访客量',
  `pv` int(12) DEFAULT NULL COMMENT '页面浏览量',
  `ip` int(12) DEFAULT NULL COMMENT 'ip访问量',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户网站访问统计';


ALTER TABLE `t_activity_goods_rule_details` 
MODIFY COLUMN `ref_sign_value` varchar(132) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '规则关联值(如商品则商品Code,如供应商则供应商code)' AFTER `rule_type`,
MODIFY COLUMN `ref_sign_local_id` varchar(132) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '规则本地数据Id' AFTER `ref_sign_value`,
MODIFY COLUMN `ref_sign_name` varchar(132) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联值名称' AFTER `ref_sign_local_id`;


