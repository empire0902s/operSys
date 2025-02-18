
/**--------Sql 初始化数据 文件-----V201------------**/

INSERT INTO `t_resource` VALUES (38, '2018-07-16 14:28:29', 'admin-1', '2018-07-16 14:30:37', 'admin-1', b'0', '0000', '订单管理', '/', 1000, 1, ' ', NULL, NULL);
INSERT INTO `t_resource` VALUES (39, '2018-07-16 14:30:25', 'admin-1', '2018-07-16 14:30:47', 'admin-1', b'0', '0000', '订单评价管理', '/order/evaluate/index', 1000, 1, '1071', NULL, 38);
INSERT INTO `t_resource` VALUES (40, '2018-07-23 10:42:46', 'admin-1', '2018-07-23 10:42:46', 'admin-1', b'0', '0000', '分销活动订单统计', '/distribute/statistics/activityOrdersStatistics', 1000, 1, '1005', NULL, 31);




DELETE FROM `t_role_resource`;
INSERT INTO `t_role_resource` (`id`,`role_id`,`resource_id`,`create_time`,`create_admin`) 
select null,1,id,now(),'admin-1' from t_resource
;