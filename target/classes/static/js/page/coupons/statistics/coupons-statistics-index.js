layui.use(['table', 'laytpl', 'form', 'common', 'laydate' ], function() {
	var form = layui.form, laytpl = layui.laytpl, table = layui.table, common = layui.common,laydate = layui.laydate;
    // 初始化编辑表单
	form.render();
	 $("select[url]").loadSelect();
	//获取最近30天日期  
	 var end=getDay(0);//当天日期  
	 var start=getDay(-30);//30天前日期  
	 //日期范围
	 laydate.render({
		    elem: '#timeRange'
		    ,range: true
		    ,max:end
		  });
	   $("#timeRange").val(start+" - "+end);
	   $("#reload").click(function(){
		   if($("#timeRange").val()==""){
			   layer.alert('必须选择统计时间范围', {icon: 2}); 
			   return false;
		   }
		   if($("#activity").val()!=""){
			   if($("#timeRange").val()!=$("#timeTemp").val()){
				   layer.alert('查询时间范围已经改变，请重新选择活动', {icon: 2}); 
				   return false;
			   }
		   }
	   });
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
	var docHeight = $(document).height(), limit = 15;
	var tableHeigth = docHeight - $('form').height() - 45 -195;
		
	// 初始化数据表格
    var dataTable = common.renderTable({
    	height: 520,
	    url: common.getUrl('/coupons/statistics/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'statisticsTime', title: '统计时间', width:120, templet:function(val,row){
	    	   var tmp=val.statisticsTime.split(' ');
	    	 return  tmp[0];
	      }},
	      {field:'newAddCount', title: '当日领取并使用优惠券数量', width: 120},
	      {field:'newAddTotalOrderMoney', title: '当日领取并使用优惠券的总订单金额', width: 180},
	      {field:'newAddTotalSpendMoney', title: '当日领取并使用优惠券的总优惠金额', width: 180},
	      {field:'totalSendNums', title: '总发放数量', width: 100},
	      {field:'totalUseCounts', title: '总使用数量', width: 100},
	      {field:'totalOrderMoney', title: '总订单金额', width: 115},
	      {field:'totalSpendMoney', title: '优惠总金额', width: 115},
		  {field:'numberOfUsingcoupons', title: '使用优惠券总人数', width: 150},
		  // {field:'totalAmount', title: '代金券总额', width: 100},
		  // {field:'totalBalance', title: '抵扣券余额', width: 115},
		  // {field:'totalUsedBalance', title: '已抵扣总额', width: 115},
           //  {field:'totalDeductible', title: '总抵扣金额', width: 115},

	    ]],
    });
    
 
   $("#activity").click(function(){
	   var time=$("#timeRange").val();
		layer.open({
			title:'选择活动',
			type: 2,
			area: ['88%','88%'],
			shadeClose: true,
			content:  rootPath + '/coupons/statistics/chooseActivity?timeRange='+time
		});
    });
    $("#cleanActivity").click(function(){
    	$("#activityId").val("");
    	$("#activity").val("");
    })
});
function setValue(textData,value,timeTemp){
	$("#activityId").val(value);
	$("#activity").val(textData);
	$("#timeTemp").val(timeTemp);
}