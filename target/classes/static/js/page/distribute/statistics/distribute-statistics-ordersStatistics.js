layui.use(['table', 'laytpl', 'form', 'common','upload','laydate','element'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, upload = layui.upload,
	table = layui.table, common = layui.common,element = layui.element,
	laydate=layui.laydate;
	
	form.render();
	$(".pcite").hover(
		    function(e){
		    	layer.tips('仅包含下单购买次数大于一次的用户数量统计', '.divcite', {
		    		tips: 3
		    		  ,type:4
		    		});
		    },
		    function(e){
		    }
		);
	//获取最近30天日期  
	 var end=getDay(0);//当天日期  
	 var start=getDay(-6);//6天前日期  
	 var time=laydate.render({
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
	    url: common.getUrl('/distribute/statistics/ordersStatisticsList'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	    	{field:'date', title: '日期', width: 150,templet:function(val){
	    		return val.date.split(" ")[0];
	    	}},
	    	{field:'registCounts', title: '爱之家新增注册人数', width: 150},
	    	{field:'totalOrdersCounts', title: '订单总数', width: 150},
	    	{field:'totalOrdersMoney', title: '订单总金额(单位:元,包含运费)', width: 210},
	    	{field:'totalUserSeqCounts', title: '购买用户数', width: 100},
	    	{field:'testOrdersTotalCount', title: '测试订单总数', width: 150,templet:function(val){
		    	  if(val.testOrdersTotalCount==null){
		    		  return 0;
		    	  	}else{
		    	 	  return val.testOrdersTotalCount;
		    	  		}
		      		}},
	    	{field:'testOrderTotalAmmount', title: '测试订单总金额(元)', width: 150,templet:function(val){
		    	  if(val.testOrderTotalAmmount==null){
		    		  return 0;
		    	  	}else{
		    	 	  return val.testOrderTotalAmmount;
		    	  		}
		      		}},
	    	{field:'seqCounts', title: '测试订单购买用户数', width: 100,templet:function(val){
		    	  if(val.seqCounts==null){
		    		  return 0;
		    	  	}else{
		    	 	  return val.seqCounts;
		    	  		}
		      		}},
	    	{field:'hasPaysOrdersGoodsCounts', title: '已售商品数量', width: 150},
	    	{field:'totalNumberOfUnpaidOrder', title: '已下单未付款订单数', width: 150},
	    	{field:'totalAmountOfUnpaidOrder', title: '已经下单未付款订单总金额(元)', width: 150},
	    	{field:'hasPaysTotalOrdersCounts', title: '已下单已付款订单数', width: 150},
	    	{field:'hasPaysOrdersTotalMoney', title: '已下单已付款订单总金额(元)', width: 150},
	    	{field:'totalrefundOrdersCounts', title: '申请退款订单数', width: 150},
	      	{field:'totalRefundAmountMoney', title: '退款总金额(元)', width: 150},
	      	{field:'refundRate', title: '退款率', width: 150,templet:function(val){
	    	  if(val.refundRate==null||val.refundRate==''){
	    		  return 0;
	    	  	}else{
	    	 	  return (val.refundRate*100).toFixed(2)+"%";
	    	  		}
	      		}},
	      	{field:'averageCustomerSpending', title: '客单价(元)', width: 150,templet:function(val){
		    	 	  return val.averageCustomerSpending.toFixed(2);
		      		}},
	      	{fixed: 'right',title: '', width:10, align:'center'}
	    ]],
	    page: {
	    	limit: 10,
	    	limits: [10,15,25,50]
	    }
    });

	
})