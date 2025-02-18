
/**--------Sql 初始化数据 文件-----V200------------**/

UPDATE t_resource SET is_disabled = false,url = '/auth/behavior/index' WHERE name='用户行为';




INSERT INTO `t_resource` VALUES (31, '2018-07-04 17:05:58', 'admin-1', '2018-07-06 20:17:51', 'admin-1', b'0', '0000', '分销商品统计管理', '', 5, 1, ' ', NULL, NULL);
INSERT INTO `t_resource` VALUES (32, '2018-07-06 20:17:37', 'admin-1', '2018-07-06 20:17:57', 'admin-1', b'0', '0000', '分销商品统计', '/distribute/statistics/index', 1000, 1, '1002', NULL, 31);
INSERT INTO `t_resource` VALUES (33, '2018-07-06 20:24:06', 'admin-1', '2018-07-06 20:25:56', 'admin-1', b'0', '0000', '分销订单统计', '/distribute/statistics/ordersStatistics', 1000, 1, '1004', NULL, 31);


INSERT INTO t_resource
( id,create_time,create_admin,update_time,update_admin,is_disabled,system_code,	name,			url,					sort,type,	remark,icon,	parent_id )
VALUES
(34,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'信息管理',			null,						320,	1,	null,	null,	null),
(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'Excel发送管理',		'/message/excel/index',		321,	1,	null,	null,	34),
(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'信息发送操作记录',		'/message/setting/index',		322,	1,	null,	null,	34),
(null,	now(),	'admin-1',		now(),		'admin-1',false,		'0000',		'信息发送管理',		'/message/index',			323,	1,	null,	null,	34)
;
