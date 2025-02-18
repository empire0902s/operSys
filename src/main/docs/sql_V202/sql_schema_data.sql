
/**--------Sql 初始化数据 文件-----V202------------**/
INSERT INTO `t_resource` VALUES (41, '2018-08-03 16:37:17', 'admin-1', '2018-08-03 16:37:17', 'admin-1', b'0', '0000', '广告Banner管理', '/', 1000, 1, NULL, NULL, NULL);
INSERT INTO `t_resource` VALUES (42, '2018-08-03 16:38:46', 'admin-1', '2018-08-06 09:23:14', 'admin-1', b'0', '0000', 'app启动页广告管理', '/auth/banner/app/adv/index', 1000, 1, '1012', NULL, 41);
UPDATE `t_resource` SET `url` = '/mongo/homepagemodule/appAdv' WHERE `id` = 42

