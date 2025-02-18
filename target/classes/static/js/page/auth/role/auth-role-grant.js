layui.use(['table', 'laytpl', 'form', 'common','tree', 'layer'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	layer = layui.layer,
	table = layui.table, common = layui.common;
    // 初始化编辑表单
	form.render();
	$(document).on('click','.submitBtn', function(data){
		var formData = $("#menusForm").serializeArray();
		var roleName = $("input[name='roleNames']").val()
		layer.confirm("确定要分配新的菜单资源["+roleName+"]?", function(confirmIndex){
			common.wait("提交中...");
			common.post( rootPath + '/auth/role/grantResource', formData, function(msg) {
				common.hide();
				if (msg.ok) {
					layer.alert("角色["+roleName+"]分配权限成功,下次登录生效",function(index){
						layer.close(index);
						parent.layer.close(parent.layer.getFrameIndex(window.name)); //执行关闭
					});
				}else{
					layer.alert(msg.msg);
				}
				layer.close(confirmIndex);
			});
		});
		return false;
	});
	

	$('.btn-close').click(function(){
		layer.closeAll();
	});
	
	//构建树形菜单(带checkbook的)
	//var treeJson = JSON.parse(roleMenuList);
	var tree = layui.tree({
        elem: '#demoTree', //指定元素，生成的树放到哪个元素上
        spread:true,
        check: 'checkbox', //选择框
        checkboxName: 'resourceIdList',//复选框的name属性值
        data: {//为元素添加额外数据，即在元素上添加data-xxx="yyy"，可选
            hasChild: true
        },
        nodes: roleMenuList
    });
	
	
	//关闭本窗口
	$(document).on('click','#closeBtn', function(){
		parent.layer.close(parent.layer.getFrameIndex(window.name)); //再执行关闭        
		return;
	});
	
  	
});