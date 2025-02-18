	layui.use(['table', 'laytpl', 'form', 'common', 'laydate','element' ], function() {
		var form = layui.form, laytpl = layui.laytpl, table = layui.table,
		common = layui.common,laydate = layui.laydate
		 $ = layui.jquery,element = layui.element,layer=layui.layer;
	    // 初始化编辑表单
		form.render();
		
		$("#totalOrdersCount").hover(
			    function(e){
			    	layer.tips('仅包含已付款与退款订单总数', '#ordersCounts', {
			    		  tips: 2
			    		  ,type:4
			    		});
			    },
			    function(e){
			    }
			);
		$("#totalSalesCount").hover(
			    function(e){
			    	layer.tips('仅包含已付款与退款订单销量总数', '#salesCounts', {
			    		  tips: 2
			    		  ,type:4
			    		});
			    },
			    function(e){
			    }
			);
		$("#totalSalesMoney").hover(
			    function(e){
			    	layer.tips('仅包含已付款与退款订单销售金额总数', '#salesMoney', {
			    		  tips: 2
			    		  ,type:4
			    		});
			    },
			    function(e){
			    }
			);
		$("#testToatalOrdersCount").hover(
			    function(e){
			    	layer.tips('仅包含收货地址以测试开头的测试订单的订单总数', '#testOrdersCount', {
			    		  tips: 2
			    		  ,type:4
			    		});
			    },
			    function(e){
			    }
			);
		$("#testTotalSalesAmount").hover(
			    function(e){
			    	layer.tips('仅包含收货地址以测试开头的测试订单的金额总数', '#testSalesAmount', {
			    		  tips: 2
			    		  ,type:4
			    		});
			    },
			    function(e){
			    }
			);
		$("#homeOfLoveUserBuyCounts").hover(
			    function(e){
			    	layer.tips('只统计下单付款成功的人数', '#homeOfLoveUserBuyCountsText', {
			    		  tips: 2
			    		  ,type:4
			    		});
			    },
			    function(e){
			    }
			);
		$("#homeOfLoveUserDoubleBuyCounts").hover(
			    function(e){
			    	layer.tips('只统计下单付款成功次数大于1的人数', '#homeOfLoveUserDoubleBuyCountsText', {
			    		  tips: 2
			    		  ,type:4
			    		});
			    },
			    function(e){
			    }
			);
		
		
	
		
		 $("select[url]").loadSelect();
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
		//获取最近30天日期  
		 var end=getDay(0);//当天日期  
		 var start=getDay(-6);//6天前日期  
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
		   });
		 for (var i = 2; i <=9; i++) {
			 var tmpElem='timeRange'+i;
			 //日期范围
			 laydate.render({
				    elem: '#'+tmpElem
				    ,range: true
				    ,max:end
				  });
			   $("#"+tmpElem).val(start+" - "+end);
		}
		   function diy_time(time1,time2){
			    time1 = Date.parse(new Date(time1));
			    time2 = Date.parse(new Date(time2));
			    return time3 =Math.abs(parseInt((time2 - time1)/1000/3600/24));
			}
		 function checkTime(elem){
			 	var value=$(elem).val();
				var timeArrs= value.split(" - ");
				var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
				if(tangeDays>30){
					   layer.alert('查询日期范围最大长度为31天', {icon: 2}); 
					   $(elem).val('');
					   return false;
				}
			 
		 } 
		  
		var docHeight = $(document).height(), limit = 15;
		var tableHeigth = docHeight - $('form').height() - 45 -195;
			
		// 初始化数据表格
	    var dataTable = common.renderTable({
	    	height:'full-number' ,
		    url: common.getUrl('/distribute/statistics/list'),
		    where:  $(".data-list-form").serializeJSON(),
		    cols: [[
			      {field:'sort', title: '排名', width: 120},
			      {field:'goodsName', title: '商品名称', width: 180},
			      {field:'goodsCode', title: '商品code', width: 180},
			      {field:'companyName', title: '所属供应商', width: 180},
			      {field:'sellNums', title: '商品销售数量', width: 180},
			      {field:'amt', title: '销售金额(元)', width: 180},
			      {field:'testSellNums', title: '测试订单销售数量', width: 180},
			      {field:'testAmt', title: '测试订单销售金额(元)', width: 180},
			      {field:'notIncludeTestSellNums', title: '除去测试订单销售数量', width: 180},
			      {field:'notIncludeTestAmt', title: '除去测试订单销售金额(元)', width: 180,templet:function(val){
			    	  return val.notIncludeTestAmt.toFixed(2);
			      }},
			    ]],
	    });
		 $("#reload").click(function(){
			 var value=$("#timeRange").val();
			   if(value==""){
				   layer.alert('必须选择统计时间范围', {icon: 2}); 
				   return false;
			   }
				var timeArrs= value.split(" - ");
				var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
				if(tangeDays>30){
					   layer.alert('查询日期范围最大长度为31天', {icon: 2}); 
					   $("#timeRange").val("");
					   return false;
				}
				return ;
		   });
		 var flag2=true;
	    $("#reload2").click(function(){
	    	   if($("#timeRange2").val()==""){
				   layer.alert('必须选择统计时间范围', {icon: 2}); 
				   return false;
			   }
	    	 	var value=$("#timeRange2").val();
				var timeArrs= value.split(" - ");
				var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
				if(tangeDays>30){
					   layer.alert('查询日期范围最大长度为31天', {icon: 2}); 
					   $("#timeRange2").val('');
					   return false;
				}
	    	var opt={url: common.getUrl('/distribute/statistics/commoditySalesDataList?timeRange2='+$("#timeRange2").val())};
	    	common.reloadTable(tab2,opt);
			return false ;
	  });
	    $("#reload3").click(function(){
	    	   if($("#timeRange3").val()==""){
				   layer.alert('必须选择统计时间范围', {icon: 2}); 
				   return false;
			   }
	    	 	var value=$("#timeRange3").val();
				var timeArrs= value.split(" - ");
				var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
				if(tangeDays>30){
					   layer.alert('查询日期范围最大长度为31天', {icon: 2}); 
					   $("#timeRange3").val('');
					   return false;
				}
	    	   var opt={url: common.getUrl('/distribute/statistics/supplierTradingVolumeDataList?timeRange3='+$("#timeRange3").val())};
		    	common.reloadTable(tab3,opt);
	   		return false;
	  });
	    $("#reload4").click(function(){
	    	   if($("#timeRange4").val()==""){
				   layer.alert('必须选择统计时间范围', {icon: 2}); 
				   return false;
			   }
	   	 	var value=$("#timeRange4").val();
			var timeArrs= value.split(" - ");
			var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
			if(tangeDays>30){
				   layer.alert('查询日期范围最大长度为31天', {icon: 2}); 
				   $("#timeRange4").val('');
				   return false;
			}
	    	var opt={url: common.getUrl('/distribute/statistics/supplierSalesDataList?timeRange4='+$("#timeRange4").val())};
		    common.reloadTable(tab4,opt);
	   		return ;
	  });
	    $("#reload5").click(function(){
	    	   if($("#timeRange5").val()==""){
				   layer.alert('必须选择统计时间范围', {icon: 2}); 
				   return false;
			   }
	   	 	var value=$("#timeRange5").val();
			var timeArrs= value.split(" - ");
			var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
			if(tangeDays>30){
				   layer.alert('查询日期范围最大长度为31天', {icon: 2}); 
				   $("#timeRange5").val('');
				   return false;
			}
	    		var opt={url: common.getUrl('/distribute/statistics/categoryIdTradeVolumeDataList?timeRange5='+$("#timeRange5").val())};
			    common.reloadTable(tab5,opt);
	   		return ;
	  });
	    $("#reload6").click(function(){
	    	   if($("#timeRange6").val()==""){
				   layer.alert('必须选择统计时间范围', {icon: 2}); 
				   return false;
			   }
	   	 	var value=$("#timeRange6").val();
			var timeArrs= value.split(" - ");
			var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
			if(tangeDays>30){
				   layer.alert('查询日期范围最大长度为31天', {icon: 2}); 
				   $("#timeRange6").val('');
				   return false;
			}
				var opt={url: common.getUrl('/distribute/statistics/categoryIdSalesDataList?timeRange6='+$("#timeRange6").val())};
			    common.reloadTable(tab6,opt);
	   		return ;
	  });
	    $("#reload7").click(function(){
	    	   if($("#timeRange7").val()==""){
				   layer.alert('必须选择统计时间范围', {icon: 2}); 
				   return false;
			   }
	   	 	var value=$("#timeRange7").val();
			var timeArrs= value.split(" - ");
			var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
			if(tangeDays>30){
				   layer.alert('查询日期范围最大长度为31天', {icon: 2}); 
				   $("#timeRange7").val('');
				   return false;
			}
				var opt={url: common.getUrl('/distribute/statistics/genreIdTradeVolumeDataList?timeRange7='+$("#timeRange7").val())};
			    common.reloadTable(tab7,opt);
	   		return ;
	  });
	    $("#reload8").click(function(){
	    	   if($("#timeRange8").val()==""){
				   layer.alert('必须选择统计时间范围', {icon: 2}); 
				   return false;
			   }
	   	 	var value=$("#timeRange8").val();
			var timeArrs= value.split(" - ");
			var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
			if(tangeDays>30){
				   layer.alert('查询日期范围最大长度为31天', {icon: 2}); 
				   $("#timeRange8").val('');
				   return false;
			}
				var opt={url:common.getUrl('/distribute/statistics/genreIdSalesDataList?timeRange8='+$("#timeRange8").val())};
			    common.reloadTable(tab8,opt);
	   		return ;
	  });
	    $("#reload9").click(function(){
	    	   if($("#timeRange9").val()==""){
				   layer.alert('必须选择统计时间范围', {icon: 2}); 
				   return false;
			   }
	   	 	var value=$("#timeRange9").val();
			var timeArrs= value.split(" - ");
			var tangeDays=diy_time(timeArrs[0],timeArrs[1]);
			if(tangeDays>30){
				   layer.alert('查询日期范围最大长度为31天', {icon: 2}); 
				   $("#timeRange9").val('');
				   return false;
			}
	    		var opt={url: common.getUrl('/distribute/statistics/goodsStatisticsMsgDataList?timeRange9='+$("#timeRange9").val())};
			    common.reloadTable(tab9,opt);
	   		return ;
	  });
		//tab 监听
	    var tab2="";var tab3="";var tab4="";var tab5="";
	    var tab6="";var tab7="";var tab8="";var tab9="";
		element.on('tab(test)', function(data){
			  var data_id= $(this).attr("data-id");
			  initTableData(data_id);
			});
		var initTableData=function(data_id){
			 if(data_id == "commodityTradingVolumeData"){
				  var tmp= $("#commodityTradingVolumeData").parent().find('tbody').length;
				  if(tmp==1){
					  return false;
				  }
					table.render({
						  elem: '#commodityTradingVolumeData' //指定原始表格元素选择器（推荐id选择器）
						  ,height: 'full-number'  //容器高度
						  ,url: common.getUrl('/distribute/statistics/list?timeRange='+$("#timeRange").val())
						  ,where:  $(".data-list-form").serializeJSON()
						  ,cols: [[
						      {field:'sort', title: '排名', width: 120},
						      {field:'goodsName', title: '商品名称', width: 180},
						      {field:'goodsCode', title: '商品code', width: 180},
						      {field:'sellNums', title: '商品销售数量', width: 180},
						      {field:'amt', title: '销售金额(元)', width: 180},
						      {field:'testSellNums', title: '测试订单销售数量', width: 180},
						      {field:'testAmt', title: '测试订单销售金额(元)', width: 180},
						      {field:'notIncludeTestSellNums', title: '除去测试订单销售数量', width: 180},
						      {field:'notIncludeTestAmt', title: '除去测试订单销售金额(元)', width: 180,templet:function(val){
						    	  return val.notIncludeTestAmt.toFixed(2);
						      }},
						    ]], //设置表头
						    page: {
						    	limit: 10,
						    	limits: [10,15,25,50]
						    }
						});
			  }
			 else if(data_id == "commoditySalesData"){
				  var tmp= $("#commoditySalesData").parent().find('tbody').length;
				  if(tmp==1){
					  return false;
				  }
					tab2=table.render({
						  elem: '#commoditySalesData' //指定原始表格元素选择器（推荐id选择器）
						  ,height: 'full-number'  //容器高度
						  ,where:  $(".data-list-form").serializeJSON()
						  ,cols: [[
						      {field:'sort', title: '排名', width: 120},
						      {field:'goodsName', title: '商品名称', width: 180},
						      {field:'goodsCode', title: '商品code', width: 180},
						      {field:'sellNums', title: '商品销售数量', width: 180},
						      {field:'amt', title: '销售金额(元)', width: 180},
						      {field:'testSellNums', title: '测试订单销售数量', width: 180},
						      {field:'testAmt', title: '测试订单销售金额(元)', width: 180},
						      {field:'notIncludeTestSellNums', title: '除去测试订单销售数量', width: 180},
						      {field:'notIncludeTestAmt', title: '除去测试订单销售金额(元)', width: 180,templet:function(val){
						    	  return val.notIncludeTestAmt.toFixed(2);
						      }},
						    ]], //设置表头
						    page: {
						    	limit: 10,
						    	limits: [10,15,25,50]
						    }
						});
					var opt={  url: common.getUrl('/distribute/statistics/commoditySalesDataList?timeRange2='+$("#timeRange2").val())};
					common.reloadTable(tab2,opt);
			  }
			 else  if(data_id == "supplierTradingVolumeData"){
				  var tmp= $("#supplierTradingVolumeData").parent().find('tbody').length;
				  if(tmp==1){
					  return false;
				  }
					tab3=table.render({
						  elem: '#supplierTradingVolumeData' //指定原始表格元素选择器（推荐id选择器）
						  ,height: 'full-number'  //容器高度
						  ,where:  $(".data-list-form").serializeJSON()
						  ,cols: [[
							  {field:'sort', title: '排名', width: 120},
						      {field:'seq', title: '供应商SEQ', width: 180},
						      {field:'companyName', title: '供应商名称', width: 180},
						      {field:'sellNums', title: '商品销售数量', width: 180},
						      {field:'contact', title: '联系电话', width: 180},
						      {field:'amt', title: '销售金额(元)', width: 180},
						      {field:'testSellNums', title: '测试订单销售数量', width: 180},
						      {field:'testAmt', title: '测试订单销售金额(元)', width: 180},
						      {field:'notIncludeTestSellNums', title: '除去测试订单销售数量', width: 180},
						      {field:'notIncludeTestAmt', title: '除去测试订单销售金额(元)', width: 180,templet:function(val){
						    	  return val.notIncludeTestAmt.toFixed(2);
						      }},
						    ]], //设置表头
						    page: {
						    	limit: 10,
						    	limits: [10,15,25,50]
						    }
						});
					  var opt={url: common.getUrl('/distribute/statistics/supplierTradingVolumeDataList?timeRange3='+$("#timeRange3").val())};
						common.reloadTable(tab3,opt);
			  }
			 else  if(data_id == "supplierSalesData"){
				  var tmp= $("#supplierSalesData").parent().find('tbody').length;
				  if(tmp==1){
					  return false;
				  }
				  tab4=table.render({
						  elem: '#supplierSalesData' //指定原始表格元素选择器（推荐id选择器）
						  ,height: 'full-number'  //容器高度
						  ,where:  $(".data-list-form").serializeJSON()
						  ,cols: [[
							  {field:'sort', title: '排名', width: 120},
						      {field:'seq', title: '供应商SEQ', width: 180},
						      {field:'companyName', title: '供应商名称', width: 180},
						      {field:'sellNums', title: '商品销售数量', width: 180},
						      {field:'contact', title: '联系电话', width: 180},
						      {field:'amt', title: '销售金额(元)', width: 180},
						      {field:'testSellNums', title: '测试订单销售数量', width: 180},
						      {field:'testAmt', title: '测试订单销售金额(元)', width: 180},
						      {field:'notIncludeTestSellNums', title: '除去测试订单销售数量', width: 180},
						      {field:'notIncludeTestAmt', title: '除去测试订单销售金额(元)', width: 180,templet:function(val){
						    	  return val.notIncludeTestAmt.toFixed(2);
						      }},
						    ]], //设置表头
						    page: {
						    	limit: 10,
						    	limits: [10,15,25,50]
						    }
						});
				  var opt={url: common.getUrl('/distribute/statistics/supplierSalesDataList?timeRange4='+$("#timeRange4").val())};
					common.reloadTable(tab4,opt);
			  }
			 else  if(data_id == "categoryIdTradeVolumeData"){
				  var tmp= $("#categoryIdTradeVolumeData").parent().find('tbody').length;
				  if(tmp==1){
					  return false;
				  }
					tab5=table.render({
						  elem: '#categoryIdTradeVolumeData' //指定原始表格元素选择器（推荐id选择器）
						  ,height: 'full-number'  //容器高度
						  ,where:  $(".data-list-form").serializeJSON()
						  ,cols: [[
						      {field:'sort', title: '排名', width: 120},
						      {field:'categoryId', title: '一级类目ID', width: 120},
						      {field:'categoryIdName', title: '一级类目', width: 180},
						      {field:'sellNums', title: '商品销售数量', width: 180},
						      {field:'amt', title: '销售金额(元)', width: 180},
						      {field:'testSellNums', title: '测试订单销售数量', width: 180},
						      {field:'testAmt', title: '测试订单销售金额(元)', width: 180},
						      {field:'notIncludeTestSellNums', title: '除去测试订单销售数量', width: 180},
						      {field:'notIncludeTestAmt', title: '除去测试订单销售金额(元)', width: 180,templet:function(val){
						    	  return val.notIncludeTestAmt.toFixed(2);
						      }},
						    ]], //设置表头
						    page: {
						    	limit: 10,
						    	limits: [10,15,25,50]
						    }
						});
					 var opt={url: common.getUrl('/distribute/statistics/categoryIdTradeVolumeDataList?timeRange5='+$("#timeRange5").val())};
						common.reloadTable(tab5,opt);
			  }
			 else   if(data_id == "categoryIdSalesData"){
				  var tmp= $("#categoryIdSalesData").parent().find('tbody').length;
				  if(tmp==1){
					  return false;
				  }
					tab6=table.render({
						  elem: '#categoryIdSalesData' //指定原始表格元素选择器（推荐id选择器）
						  ,height: 'full-number'  //容器高度
						  ,where:  $(".data-list-form").serializeJSON()
						  ,cols: [[
						      {field:'sort', title: '排名', width: 120},
						      {field:'categoryId', title: '一级类目ID', width: 120},
						      {field:'categoryIdName', title: '一级类目', width: 180},
						      {field:'sellNums', title: '商品销售数量', width: 180},
						      {field:'amt', title: '销售金额(元)', width: 180},
						      {field:'testSellNums', title: '测试订单销售数量', width: 180},
						      {field:'testAmt', title: '测试订单销售金额(元)', width: 180},
						      {field:'notIncludeTestSellNums', title: '除去测试订单销售数量', width: 180},
						      {field:'notIncludeTestAmt', title: '除去测试订单销售金额(元)', width: 180,templet:function(val){
						    	  return val.notIncludeTestAmt.toFixed(2);
						      }},
						    ]], //设置表头
						    page: {
						    	limit: 10,
						    	limits: [10,15,25,50]
						    }
						});
					var opt={url: common.getUrl('/distribute/statistics/categoryIdSalesDataList?timeRange6='+$("#timeRange6").val())};
					common.reloadTable(tab6,opt);
			  }
			 else   if(data_id == "genreIdTradeVolumeData"){
				  var tmp= $("#genreIdTradeVolumeData").parent().find('tbody').length;
				  if(tmp==1){
					  return false;
				  }
					tab7=table.render({
						  elem: '#genreIdTradeVolumeData' //指定原始表格元素选择器（推荐id选择器）
						  ,height: 'full-number'  //容器高度
						  ,where:  $(".data-list-form").serializeJSON()
						  ,cols: [[
						      {field:'sort', title: '排名', width: 120},
						      {field:'genreId', title: '二级类目ID', width: 120},
						      {field:'genreIdName', title: '二级类目', width: 180},
						      {field:'sellNums', title: '商品销售数量', width: 180},
						      {field:'amt', title: '销售金额(元)', width: 180},
						      {field:'testSellNums', title: '测试订单销售数量', width: 180},
						      {field:'testAmt', title: '测试订单销售金额(元)', width: 180},
						      {field:'notIncludeTestSellNums', title: '除去测试订单销售数量', width: 180},
						      {field:'notIncludeTestAmt', title: '除去测试订单销售金额(元)', width: 180,templet:function(val){
						    	  return val.notIncludeTestAmt.toFixed(2);
						      }},
						    ]], //设置表头
						    page: {
						    	limit: 10,
						    	limits: [10,15,25,50]
						    }
						});
					var opt={url: common.getUrl('/distribute/statistics/genreIdTradeVolumeDataList?timeRange7='+$("#timeRange7").val())};
					common.reloadTable(tab7,opt);
			  }
			 else   if(data_id == "genreIdSalesData"){
				  var tmp= $("#genreIdSalesData").parent().find('tbody').length;
				  if(tmp==1){
					  return false;
				  }
				  tab8=table.render({
						  elem: '#genreIdSalesData' //指定原始表格元素选择器（推荐id选择器）
						  ,height:'full-number' //容器高度
						  ,where:  $(".data-list-form").serializeJSON()
						  ,cols: [[
						      {field:'sort', title: '排名', width: 120},
						      {field:'genreId', title: '二级类目ID', width: 120},
						      {field:'genreIdName', title: '二级类目', width: 180},
						      {field:'sellNums', title: '商品销售数量', width: 180},
						      {field:'amt', title: '销售金额(元)', width: 180},
						      {field:'testSellNums', title: '测试订单销售数量', width: 180},
						      {field:'testAmt', title: '测试订单销售金额(元)', width: 180},
						      {field:'notIncludeTestSellNums', title: '除去测试订单销售数量', width: 180},
						      {field:'notIncludeTestAmt', title: '除去测试订单销售金额(元)', width: 180,templet:function(val){
						    	  return val.notIncludeTestAmt.toFixed(2);
						      }},
						    ]], //设置表头
						    page: {
						    	limit: 10,
						    	limits: [10,15,25,50]
						    }
						});
					var opt={url: common.getUrl('/distribute/statistics/genreIdSalesDataList?timeRange8='+$("#timeRange8").val())};
					common.reloadTable(tab8,opt);
			  }
			 else  if(data_id == "goodsStatisticsMsgData"){
				  var tmp= $("#goodsStatisticsMsgData").parent().find('tbody').length;
				  if(tmp==1){
					  return false;
				  }
				  tab9=table.render({
						  elem: '#goodsStatisticsMsgData' //指定原始表格元素选择器（推荐id选择器）
						  ,height: 'full-number'  //容器高度
						  ,where:  $(".data-list-form").serializeJSON()
						  ,cols: [[
							      {field:'sort', title: '排名', width: 120},
							      {field:'goodsName', title: '商品名称', width: 180},
							      {field:'goodsCode', title: '商品code', width: 180},
							      {field:'companyName', title: '所属供应商', width: 180},
							      {field:'sellNums', title: '商品销售数量', width: 180},
							      {field:'amt', title: '销售金额(元)', width: 180},
							      {field:'exhibitCount', title: '商品点击次数', width: 180},
							      {field:'purchaseConversionRate', title: '购买转化率', width: 180,templet:function(val){
							    	  if(val.purchaseConversionRate==0){
							    		  return 0;
							    	  }else{
							    		  return (val.purchaseConversionRate*100).toFixed(2)+"%";
							    	  }
							    	  
							      }},
							      {field:'testSellNums', title: '测试订单销售数量', width: 180},
							      {field:'testAmt', title: '测试订单销售金额(元)', width: 180},
							      {field:'notIncludeTestSellNums', title: '除去测试订单销售数量', width: 180},
							      {field:'notIncludeTestAmt', title: '除去测试订单销售金额(元)', width: 180,templet:function(val){
							    	  return val.notIncludeTestAmt.toFixed(2);
							      }},
							      
							    ]],  //设置表头
							    page: {
						    	limit: 10,
						    	limits: [10,15,25,50]
						    }
						});
				  var opt={url: common.getUrl('/distribute/statistics/goodsStatisticsMsgDataList?timeRange9='+$("#timeRange9").val())};
					common.reloadTable(tab9,opt);
			  }
			
		}
	    
	    
	    
	});
