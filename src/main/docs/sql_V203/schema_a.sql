/**--------Sql 修改数据表-----V203------------**/
/** 邀请页功能，规则详细表添加排序字段sort**/
ALTER TABLE `t_activity_goods_rule_details` 
ADD COLUMN `sort` int(11) DEFAULT 1000 COMMENT '排序' AFTER `ref_sign_name`;

UPDATE `operatedb`.`t_resource` SET `url` = '/mongo/homepagemodule/appAdv',`parent_id` = 44 WHERE `id` = 42

/*------------------------------------------*/
/*banner表修改order_no字段名为sort
 *banner表添加type字段
 *字典表添加类型type与排序sort字段 */
/* 执行上述操作的存储过程如下*/
drop procedure if exists schema_change;

delimiter ;;
create procedure schema_change() begin
 if exists (SELECT * FROM  information_schema.COLUMNS WHERE  table_name='t_banner' AND COLUMN_NAME='type') 
 then
  alter table `operatedb`.`t_banner` drop column `type`;
 end if;
 
 if exists (SELECT * FROM  information_schema.COLUMNS WHERE  table_name='t_banner' AND COLUMN_NAME='order_no') 
 then
  alter table `operatedb`.`t_banner` 
	CHANGE COLUMN `order_no` `sort` int(11) DEFAULT NULL COMMENT '排序';
 end if;
 
 if exists (SELECT * FROM  information_schema.COLUMNS WHERE  table_name='t_dict' AND COLUMN_NAME='sort') 
 then
  alter table `operatedb`.`t_dict` drop column `sort`;
 end if;
 
  if exists (SELECT * FROM  information_schema.COLUMNS WHERE  table_name='t_dict' AND COLUMN_NAME='type') 
 then
  alter table `operatedb`.`t_dict` drop column `type`;
 end if;
 
 alter table `operatedb`.`t_banner`  
 add column  `type` varchar(32) NULL COMMENT '类型(0App引导页,1首页Banner)';
 
 alter table  `operatedb`.`t_dict` add column   `sort` int(11) COMMENT '排序' ;
 alter table  `operatedb`.`t_dict` add column   `type` bit(1) DEFAULT b'0' COMMENT '不同的栏目模块是否可以添加相同商品（0为否，1为是）';
end;;
delimiter ;
call schema_change();

drop procedure if exists schema_change;
/*------------------------------------------*/


