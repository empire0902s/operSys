


/**--------Sql 修改数据表-----V202------------**/

ALTER TABLE `operatedb`.`t_dict` 
ADD COLUMN `sort` smallint(6) AFTER `type`;

ALTER TABLE `operatedb`.`t_coupons_user` 
ADD INDEX `key_couponsUser_couponsStatus`(`coupons_status`) USING BTREE,
ADD INDEX `key_couponsUser_createTime`(`create_time`) USING BTREE;



ALTER TABLE `t_msg_send_detail`
	ADD COLUMN `msg_send_id` BIGINT(20)  NULL COMMENT '关联发送id' AFTER `message_type`;
	

	
	ALTER TABLE `operatedb`.`t_order_evaluate` 
ADD COLUMN `goods_sku` varchar(100) COMMENT '商品sku' AFTER `goods_code`;