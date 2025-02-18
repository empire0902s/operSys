layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, laydate = layui.laydate, 
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();
	
	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl('/coupons/activity/goods/rule/activityGoodsRelationList'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:50, sort: true},
	      {field:'activityGoodsRuleId', title: '商品规则Id', width: 180},
	      {field:'goodsProductPid', title: '商品Id', width: 100},
          {field:'ruleType', title: '规则类型', width: 100,templet:function(val,row){
                  if(val.ruleType===0){
                      return "组合商品";
                  }else if (val.ruleType===1){
                      return　"一级类目";
                  } else if (val.ruleType===2) {
                      return　"二级类目";
                  }else if (val.ruleType===3){
                      return　"三级类目";
                  } else if(val.ruleType===4){
                      return　"四级类目";
                  }
              }},
	      {field:'createTime', title: '创建时间', width: 160},
	      {field:'createAdmin', title: '创建人', width: 160},
	      {fixed: 'right',title: '操作', width:250, align:'center', toolbar:'#editBar'}
	    ]],
    });

    // var editForm = $('.edit-form');
    // 添加事件
	$('.btn-add').click(function(data){
        window.location.href= rootPath + "/coupons/activity/goods/rule/proprice?ruleId="+arrhref[1]+"&ruleType="+arrhref[2];
    });


    $('.btn-return').click(function(){
        window.history.go(-1);
    });
	
	$('.btn-close').click(function(){
		layer.closeAll();
	});
    var Ohref=window.location.href;
    var arrhref=Ohref.split("?ruleId=");
    var ruleid=arrhref[1].split("&ruleType=")[0];
    var ruleType=arrhref[1].split("&ruleType=")[1];
  	
	var editForm = $('.edit-form'), roleForm = $('.role-form'),grantForm=$('.grant-form');
    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
		case 'edit':
			layer.open({
				title:'编辑',
				type: 1,
				area: ['400px', '400px'],
				content: editForm,
				success: function(){
					// 加载表单，重新渲染
					editForm.loadData(data);
					form.render();
				}
			});
			break;
		case 'pricesetting':
            window.location.href= rootPath + "/coupons/activity/goods/rule/proprice?ruleId="+arrhref[1]+"&ruleType="+arrhref[2];
			break;
		case 'details':
			window.location.href= rootPath + "/coupons/activity/goods/rule/setting?ruleId="+ruleid+"&ruleYype="+ruleType;
			break;
		case 'setting':
			window.location.href= rootPath + "/coupons/activity/goods/rule/setting?ruleId="+data.id;
			break;
		default:
			break;
		}
   });
});