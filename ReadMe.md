# 添加功能步骤

#【对数据表结构修 需要记录Sql 语句】
##	1、保存SQL 语句到 src/main/docs/sql_[对应版本号]/sql_schema_a.sql
##	2、对于系统初始化数据(如菜单资源) 需要记录Sql 到对应  src/main/docs/sql_[对应版本号]/sql_schema_data.sql

##	1、添加domain(实体) ，如：com.shq.oper.model.domain.[数据源].Admin.java
	
##	2、添加mapper（Dao），如：com.shq.oper.mapper.[数据源].AdminMapper.java
###		2.1、添加mapper（mapper），如：src/main/resources.mappers.[数据源].AdminMapper.xml
		
##	3、添加service（service），如：com.shq.oper.service.[数据源].AdminService.java
###		3.1、添加serviceImpl（impl）， 如：com.shq.oper.service.impl.[数据源].AdminServiceImpl.java
		
##	4、添加controller（controller），如：com.shq.oper.controller.auth.AdminController.java
		Controller 按模块添加，如
			com.shq.oper.controller.auth.AdminController.java
			com.shq.oper.controller.sys.DictController.java
	
##	5、添加后台页面
			src/main/resources/templates/	auth/admin/auth-admin-index.html
			src/main/resources/templates/	sys/dict/sys-dict-index.html
			添加的层次结构，与Controller包名一一对应
			文件名称，包含所有包名名次
			
##	6、添加后台Js
			src/main/resources/static/js/page/	auth/admin/auth-admin-index.js
			src/main/resources/static/js/page/	sys/dict/sys-dict-index.js
			添加的层次结构，与Controller包名一一对应
			文件名称，包含所有包名名次
			
	

# 注意事项
##	SVN提交代码不提交本地环境的配置文件，不允许选择整个项目提交！！！
##	提交前先同步看看有没有冲突，然后更新代码后，再提交。
##	提交代码要写注释
	
##	domain（实体） 时间类型，统一用LocalDateTime

## 页面表单 参照auth-admin-index.html
	from 表单样式参照
	
	class:
		layui-form-item	可以理解为表单Table的一行。
		layui-inline		在layui-form-item中的列，自适应的分布列。
		
		
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">这里Label标签文字-1:</label>
			<div class="layui-input-inline">
				<input type="text" class="layui-input" name="name" placeholder="这里是Input输入框-1">
			</div>
		</div>	
		<div class="layui-inline">
			<label class="layui-form-label">这里Label标签文字:</label>
			<div class="layui-input-inline">
				<input type="text" class="layui-input" name="name" placeholder="这里是Input输入框">
			</div>
		</div>
	</div>
	
## 页面及JS 参考API
	http://www.layui.com/demo/
	http://www.layui.com/doc/
	
	
		

	
	
	
