
DELETE FROM `t_admin`;
INSERT INTO `t_admin` (`id`,`create_time`,`create_admin`,`update_time`,`update_admin`,`is_disabled`,`name`,`pwd`,`phone`,`real_name`) 
VALUES (1, now(), 'admin-1', now(), 'admin-1', b'0', 'admin', '42c224b3c8899047460f5a6d1c041411', '18800000000', 'admin');



DELETE FROM `t_role`;
INSERT INTO `t_role` (`id`,`create_time`,`create_admin`,`update_time`,`update_admin`,`is_disabled`,`name`,`code`,`remark`,`type`) 
VALUES 
 (1, now(), 'admin', now(), 'admin-1', b'0', 'admin', '001', '超级管理员角色', '2')
;




DELETE FROM `t_admin_role`;
INSERT INTO `t_admin_role` (`id`,`admin_id`,`role_id`,`create_time`,`create_admin`) 
select null,1,id,now(),'admin-1' from t_role
;



delete from t_resource;
INSERT INTO t_resource 
( id,create_time,create_admin,update_time,update_admin,is_disabled,system_code,	name,			url,					sort,type,	remark,icon,	parent_id )
VALUES
 (1,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'权限管理',		null,					1,		1,	null,	null,	null)
,(2,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'系统管理',		null,					2,		1,	null,	null,	null)
,(3,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'活动优惠',		null,					3,		1,	null,	null,	null)
,(4,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'广告Banner',	null,					4,		1,	null,	null,	null)
,(7,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'用户管理',		'/auth/admin/index',	110,	1,	null,	null,	1)
,(8,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'角色管理',		'/auth/role/index',		120,	1,	null,	null,	1)
,(9,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'资源菜单',		'/auth/resource/index',	130,	1,	null,	null,	1)
,(10,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'系统字典',		'/sys/dict/index',		210,	1,	null,	null,	2)

,(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'管理员操作日志',	'/mongo/adminactionlog/index',	210,	1,	null,	null,	2)
,(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'接口访问日志',	'/mongo/apirequestdatalog/index',	220,	1,	null,	null,	2)
,(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'活动管理',		'/coupons/activity/index',		310,	1,	null,	null,	3)
,(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'优惠券管理',		'/coupons/coupons/index',		330,	1,	null,	null,	3)
,(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'用户优惠券管理',		'/coupons/user/index',		340,	1,	null,	null,	3)
,(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'优惠券财务审核',		'/coupons/examine/index',		350,	1,	null,	null,	3)
,(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'优惠券金额规则',		'/coupons/couponsTypeRule/index',		331,	1,	null,	null,	3)
,(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'Banner管理',	'/auth/banner/index',	410,	1,	null,	null,	4)
;




DELETE FROM `t_role_resource`;
INSERT INTO `t_role_resource` (`id`,`role_id`,`resource_id`,`create_time`,`create_admin`) 
select null,1,id,now(),'admin-1' from t_resource
;


DELETE FROM `t_activity`;
INSERT INTO `t_activity` (`id`,`create_time`,`create_admin`,`update_time`,`update_admin`,`is_disabled`,`name`,`batch`,`activity_goods_rule_id`,`send_time_start`,`send_time_end`,`user_role_rule`,`receive_num_rule`,`activity_desc`)
 VALUES
  (1, now(), 'admin-1', now(), 'admin-1', b'0', '520专场活动礼包', '520GiftPackeg', NULL, '2018-05-01 00:00:00', '2018-07-01 00:00:00', NULL, 1, '520专场活动')
;






