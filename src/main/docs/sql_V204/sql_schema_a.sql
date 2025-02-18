


/**--------Sql 修改数据表-----V204------------**/

ALTER TABLE `t_msg_send_detail`
	ADD COLUMN `message_return_type` VARCHAR(30) NULL COMMENT '一部返回状态' AFTER `msg_send_id`;