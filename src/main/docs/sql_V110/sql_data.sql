
/**--------Sql 文件-----V110------------**/


INSERT INTO t_resource 
( id,create_time,create_admin,update_time,update_admin,is_disabled,system_code,	name,			url,					sort,type,	remark,icon,	parent_id )
VALUES
(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'网站访问数据',		null,					5,		1,	null,	null,	null)

,(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'活动商品规则',		'/coupons/activity/goods/rule/index',		320,	1,	null,	null,	3)
;

INSERT INTO `t_resource`(`id`, `create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `system_code`, `name`, `url`, `sort`, `type`, `remark`, `icon`, `parent_id`) 
VALUES (null, now(), 'admin-1', now(), 'admin-1', b'0', '0000', '优惠券统计', '/coupons/statistics/index', 380, 1, null, NULL, 3);

 
INSERT INTO `t_role_resource` (`id`,`role_id`,`resource_id`,`create_time`,`create_admin`) 
select null,1,id,now(),'admin-1' from t_resource t
where t.name in ('网站访问数据','活动商品规则','优惠券统计')
;


