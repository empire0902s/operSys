


/**--------Sql 修改数据表-----V207------------**/

alter table t_order_evaluate modify order_no varchar(100) null COMMENT '订单编号';
alter table t_order_evaluate modify user_seq varchar(100) null COMMENT '评价用户seq';

ALTER TABLE `operatedb`.`t_order_evaluate` 
ADD COLUMN `user_icon` varchar(255)  COMMENT '用户头像' AFTER `id`;

ALTER TABLE `operatedb`.`t_order_evaluate` 
ADD COLUMN `nick_name` varchar(100)  COMMENT '昵称'  AFTER `audit_desc`;

ALTER TABLE `operatedb`.`t_order_evaluate` 
ADD COLUMN `evaluatie_type` smallint(6)   DEFAULT 0 COMMENT '评价类型   0：订单评价 1：手动评价' AFTER `audit_desc`;

update  t_order_evaluate set evaluatie_type=0 where evaluatie_type is null 