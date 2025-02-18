layui.use(['table', 'laytpl', 'form', 'common','laydate'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();
	var end=getDay(0);//当天日期  
	var start=getDay(-6);//6天前  
	 laydate.render({
		    elem: '#timeRange'
		    ,range: true
		    ,max:end
		  });
	   $("#timeRange").val(start+" - "+end);
	 function getDay(day){    
	        var today = new Date();    
	        var targetday_milliseconds=today.getTime() + 1000*60*60*24*day;            
	        today.setTime(targetday_milliseconds); //注意，这行是关键代码  
	        var tYear = today.getFullYear();    
	        var tMonth = today.getMonth();    
	        var tDate = today.getDate();    
	        tMonth = doHandleMonth(tMonth + 1);    
	        tDate = doHandleMonth(tDate);    
	        return tYear+"-"+tMonth+"-"+tDate;    
	 }    
	 function doHandleMonth(month){    
	        var m = month;    
	        if(month.toString().length == 1){    
	           m = "0" + month;    
	        }    
	        return m;    
	 }  
	 function diy_time(time1,time2){
		    time1 = Date.parse(new Date(time1));
		    time2 = Date.parse(new Date(time2));
		    return time3 =Math.abs(parseInt((time2 - time1)/1000/3600/24));
		}
	 
	 $("#reload").click(function(){
		var timeArrs= $("#timeRange").val().split(" - ");
		var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
		if(tangeDays>6){
			   layer.alert('查询日期范围最大长度为7天', {icon: 2}); 
			   return false;
		}
	   });
    var actionCode=$(".actionCode").val();
	// 初始化数据表格
    var dataTable = common.renderTable({

	    url: common.getUrl(rootPath + '/mongo/apirequestdatalog/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:150, sort: true},
	      {field:'actionCode', title: '访问方法', width: 180},
//	      {field:'refAction', title: '来源路径', width: 300},
//	      {field:'action', title: '访问路径', width: 300},
	      {field:'refIp', title: '来源IP', width: 150},
	      {field:'createTime', title: '时间', width: 180},
	      {field:'actionDeviceType', title: '设备类型', width: 180},
	      {field:'actionPara', title: '请求参数', width: 300},
	      {field:'returnData', title: '返回数据', width: 300},
	      //{fixed: 'right',title: '操作', width:250, align:'center', toolbar:'#editBar'}
	    ]],
    });

	
	$('.btn-close').click(function(){
		layer.closeAll();
	});
  	
	var editForm = $('.edit-form'), roleForm = $('.role-form'),grantForm=$('.grant-form');
    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
		case 'details':
			layer.open({
				title:'详细信息',
				type: 2,
				area: ['55%','37%'],
				shadeClose: true,
				content: rootPath + '/mongo/apirequestdatalog/detail?id='+data.id
			});
			break;
		default:
			break;
		}
   });
});