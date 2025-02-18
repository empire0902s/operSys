layui.use(['table', 'laytpl', 'form', 'common','upload','laydate'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, upload = layui.upload,
	table = layui.table, common = layui.common,
	laydate=layui.laydate;
	
	form.render();
	//获取最近30天日期  
	 var end=getDay(0);//当天日期  
	 var start=getDay(-11);//11天前日期  
	 laydate.render({
		    elem: '#statisticsTime'
		    ,range: true
		    ,max:end
		  });
	   $("#statisticsTime").val(start+" - "+end);
	   $("#reload").click(function(){
		   if($("#statisticsTime").val()==""){
			   layer.alert('必须选择统计日期范围', {icon: 2}); 
			   return false;
		   }
			var timeArrs= $("#statisticsTime").val().split(" - ");
			var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
			if(tangeDays>30){
				   layer.alert('查询日期范围最大长度为31天', {icon: 2}); 
				   $("#statisticsTime").val("");
				   return false;
			}
	   });
	   function diy_time(time1,time2){
		    time1 = Date.parse(new Date(time1));
		    time2 = Date.parse(new Date(time2));
		    return time3 =Math.abs(parseInt((time2 - time1)/1000/3600/24));
		}
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
	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl('/auth/behavior/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'statisticsTime', title: '日期', width:'25%', sort: true,templet:function(val,row){
	    	   var tmp=val.statisticsTime.split(' ');
		    	 return  tmp[0];
		  }},
	      {field:'pv', title: 'PV', width: '25%'},
	      {field:'uv', title: 'UV', width: '25%'},
	      {field:'ip', title: 'IP', width: '25%'}
	    ]],
	    page: {
	    	limit: 12,
	    	limits: [12,24,36,50]
	    }
    });

	
})