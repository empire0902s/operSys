layui.use(['table', 'laytpl', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
	var checkDiv=$("#checkDiv");
	var strHtml=''; 	
	var count=0;
	var roleListJson=JSON.parse(roleList);
	var rolesOfAdminListJson=JSON.parse(rolesOfAdmin);
	for (var i = 0; i < roleListJson.length; i++) {
		strHtml+="<div class='layui-input-inline'><div class='layui-input-inline'><input style='width: 160px;margin-left: 5px; ' type='checkbox' name='roleIds["+i+"]'" +
				" title='"+ roleListJson[i].name+"' value='"+roleListJson[i].id+"' idx='"+i+"'></div></div>";
		count++; 
		if(count%3==0){
			 strHtml+="<p>"
		   }
	}
	checkDiv.html(strHtml);
  var roleCheckbox=$("input[name='roleIds[]']");
  if(rolesOfAdminListJson!=null){
	  for (var i = 0; i < rolesOfAdminListJson.length; i++) {
			for (var j = 0; j < roleListJson.length; j++) {
				if(rolesOfAdminListJson[i].id==roleListJson[j].id){
					$("input[idx="+j+"]").attr("checked",true);
				}
			}
		}
  }
   
    form.render();
	//重新加载form
	form.render();
	var grantForm=$('.grant-form');
	$(document).on('click','.submitBtn', function(data) {
		common.wait("提交中...");
		common.post(rootPath + '/auth/admin/bindRoleToAdmin', $("#bindRoletoAdminForm").serialize(), function(msg) {
			common.hide();
			if (msg.ok) {
				// 提交成功刷新数据表格，关闭弹出层
				layer.alert("操作成功。",function(){
					layer.closeAll();
					//关闭当前内嵌页面窗口
					var index = parent.layer.getFrameIndex(window.name);  
					parent.layer.close(index);
				});
			}else{
				layer.alert(msg.msg);
			}
		});
		return false;
	});
	$('.btn-close').click(function(){
		layer.closeAll();
	});

});