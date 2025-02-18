/**--------Sql 初始化数据 文件-----V203------------**/
/* 资源菜单数据*/
INSERT INTO `t_resource` VALUES (44, '2018-08-16 10:18:38', 'admin-1', '2018-08-16 10:18:50', 'admin-1', b'0', '0000', '爱之家首页模块管理', '/', 1000, 1, ' ', NULL, NULL);
INSERT INTO `t_resource` VALUES (45, '2018-08-16 10:22:03', 'admin-1', '2018-08-16 10:22:03', 'admin-1', b'0', '0000', '爱之家首页模块', '/mongo/homepagemodule/index', 1000, 1, '1041', NULL, 44);
INSERT INTO `t_resource` VALUES (46, '2018-08-16 11:06:50', 'admin-1', '2018-08-17 15:05:19', 'admin-1', b'0', '0000', '首页设置', '/mongo/homepage/homepagemain', 1000, 1, ' ', NULL, NULL);
INSERT INTO `t_resource` VALUES (47, '2018-08-20 10:52:00', 'admin-1', '2018-08-21 10:15:11', 'admin-1', b'0', '0000', '邀请页设置', '/mongo/homepagemodule/activity/goods/rule/details', 1000, 1, '1047', NULL, 44);
INSERT INTO `t_resource` VALUES (48, '2018-08-23 14:21:20', 'admin-1', '2018-08-23 16:23:56', 'admin-1', b'0', '0000', '砍价商品设置', '/mongo/priceReductionGoods/bargaingoodsSetting', 1000, 1, '10411', NULL, 44);



/** 邀请页栏目初始数据**/
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-08-23 18:00:13', 'admin-1', '2018-08-23 18:00:28', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '天天特价', 0, '天天特价商品');
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-08-23 18:03:08', 'admin-1', '2018-08-23 18:03:12', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '热销商品', 0, '热销商品');
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-08-23 18:04:20', 'admin-1', '2018-08-23 18:04:27', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '每日上新', 0, '每日上新商品');
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-08-23 18:04:35', 'admin-1', '2018-08-23 18:04:39', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '严选精品', 0, '严选精品商品');
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-08-23 18:06:05', 'admin-1', '2018-08-23 18:06:11', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '真实惠', 0, '真实惠商品');
INSERT INTO `t_activity_goods_rule`(`create_time`, `create_admin`, `update_time`, `update_admin`, `is_disabled`, `from_sys`, `from_sys_code`, `name`, `type`, `rule_des`) VALUES ('2018-08-23 18:08:35', 'admin-1', '2018-08-23 18:08:39', 'admin-1', b'0', 'homeOfLove', 'invitationPage_Code', '严选精品惠购多多', 0, '严选精品惠购多多商品');
