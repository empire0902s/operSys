layui.use(['table', 'laytpl', 'form', 'common', 'laydate' ], function() {
	var form = layui.form, laytpl = layui.laytpl, table = layui.table, common = layui.common,laydate = layui.laydate;
    // 初始化编辑表单
	form.render();
	// $("select[url]").loadSelect();
	laydate.render({
		elem : '#sendTimeStart',
		type : 'datetime'
	});
	laydate.render({
		elem : '#sendTimeEnd',
		type : 'datetime'
	});
	
	$('.btn-close').click(function(){
		layer.closeAll();
	});
	form.verify({
		pwd: [/.{6,}/, '请输入6-32位密码']
	});
	
	function reloadPreTable(){
		common.reloadTable(dataTable);
	}
	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl('/coupons/user/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:80, sort: true},
	      {field:'phone', title: '用户手机号', width: 130},
	      {field:'couponsName', title: '优惠券名称', width: 160},
	      {field:'actName', title: '活动名称', width: 160},
	      {field:'createTime', title: '领取时间', width: 180},
	      {field:'', title: '有效期', width: 300,templet:function(val,row){
	    	  return val.valiDayStart+"--"+val.valiDayEnd;
	      }},
	      {field:'couponsTypeDesc', title: '优惠券说明', width: 160},
	      {field:'couponsStatus', title: '状态', width: 100,templet:function(val,row){
	    	  if(val.couponsStatus==1){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#5bc0de '>未使用</a>";
	    	  }else if(val.couponsStatus==2){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#FFB800 '>锁定中</a>";
	    	  }else if(val.couponsStatus==3){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#5cb85c '>已使用</a>";
	    	  }else if(val.couponsStatus==4){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#FF5722 '>已过期</a>";
	    	  }else{
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#FF5722 '>未知状态</a>";
	    	  }
		    } },
		    {field:'', title: '操作', width: 110,align:'center', toolbar:'#editBar'}
	    ]],
    });
    
    
    
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
		case 'details':
			layer.open({
				title:'详细信息',
				type: 2,
				area: ['55%','50%'],
				shadeClose: true,
				content: rootPath + '/coupons/user/couponsUserDetails?id='+data.id
			});
			break;
		default:
			break;
		}
   });
});