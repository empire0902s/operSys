layui.use(['table', 'laytpl', 'form', 'common','element'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, element = layui.element,
	table = layui.table, common = layui.common;
	
	 $("select[url]").loadSelect();
    // 初始化编辑表单
	form.render();
	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl("/mongo/priceReductionGoods/list"),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:80, sort: true},
	      {field:'sortNum', title: '排序', width: 120},
	      {field:'goodsCode', title: '商品code', width: 300},
	      {field:'productName', title: '商品名称', width: 180},
	      {field:'companyName', title: '供销商', width: 150},
	      {field:'financeState', title: '财务审核状态', width: 135,templet:function(val){
	    	  if(val.financeState==0){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#5bc0de '>待审</a>";
	    	  }else if(val.financeState==1){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#5cb85c '>审核通过</a>";
	    	  }else if(val.financeState==2){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#d9534f '>审核不通过</a>";
	    	  }
	      }},
	      {field:'isSale', title: '商品上下架状态', width: 135,templet:function(val){
	    	  if(val.isSale==0){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#5bc0de '>待审</a>";
	    	  }else if(val.isSale==1){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#5cb85c '>上架</a>";
	    	  }else if(val.isSale==2){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#d9534f '>下架</a>";
	    	  }
	      }},
	      {field:'showState', title: '砍价上下架状态', width: 180,templet:'#isShowStateTpl'},
	      {field:'reductionGoodsPrice', title: '砍价价格', width: 100},
	      {field:'reductionNumRule', title: '砍价次数', width: 100},
	      {field:'reductionGoodsNum', title: '砍价商品数量', width: 130},
	      {fixed: 'right',title: '操作',width: 150, align:'center', toolbar:'#editBar'}
	    ]],
    });
    // 添加事件
	$('.btn-add').click(function(){
//		editForm.find('form')[0].reset();
		layer.open({
			title:'砍价商品添加',
			type: 2,
			area: ['1000px','600px'],
			content:   rootPath + '/mongo/priceReductionGoods/bindingToGoods?id='+$("#id").val()+'&operateType=1',
			success: function(){
				editForm.loadData({id:''});	
				form.render();
			}
		});
	});
	
	$('.btn-close').click(function(){
		layer.closeAll();
	});
  	
	var editForm = $('.edit-form'), roleForm = $('.role-form');
    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
		case 'edit':
			layer.open({
				title:'编辑',
				type: 2,
				area: ['1000px','600px'],
				content:   rootPath + '/mongo/priceReductionGoods/bindingToGoods?id='+data.id
									+'&operateType=2&goodsCode='+data.goodsCode,
				success: function(){
					editForm.loadData(data);
					// 加载表单，重新渲染
				
					form.render();
				}
			});
			break;
		case 'look':
			layer.open({
				title:'详细',
				type: 2,
				area: ['65%','45%'],
				shadeClose: true,
				content:  rootPath + '/sys/dict/dictDetails?id='+data.id
			});
			break;
		default:
			break;
		}
   });
    
    form.on('switch(isShowStateFilter)', function(obj){
    	var isDisabledDom=obj.elem;
        var p=isDisabledDom.parentNode.parentNode.parentNode;//获取单选框所在行的的tr节点;
        var tmp= p.childNodes[3];
    	var flag=obj.elem.checked;//flag=true即点击之前是未选中，flag=false即点击之前是已选中
    	if(!flag){
    		layer.confirm("确定是否下架["+tmp.innerText+"]?",  {  
    			  btn: ['确定','取消'] //按钮  
    			  ,cancel: function(index, layero){  
    				  obj.elem.checked=true;
      		    	form.render();
    			  }  
    			}, function(confirmIndex){  
    			//是  		common.wait("提交中...");
        			common.post( rootPath + '/mongo/priceReductionGoods/updateShowState', {id:obj.value,showState:0}, function(msg) {
        				common.hide();
        				if (msg.ok) {
        					// 提交成功刷新数据表格，关闭弹出层
        					layer.alert("操作成功。",function(){
        						layer.closeAll();
        						common.reloadTable(dataTable);
        					});
        				}else{
        					layer.alert(msg.msg);
        				}
        			});
        			layer.close(confirmIndex);
    			}, function(){  //取消
    				 obj.elem.checked=true;
    		    	form.render();
    			});  
    	}else{
    		layer.confirm("确定是否上架["+tmp.innerText+"]?", {  
  			  btn: ['确定','取消'] //按钮  
  			  ,cancel: function(index, layero){  
  			    //取消操作，点击右上角的X  
  				 obj.elem.checked=false;
   		    	form.render();
  			  }  
  			}, function(confirmIndex){  
  			//是  		common.wait("提交中...");
      			common.post( rootPath + '/mongo/priceReductionGoods/updateShowState', {id:obj.value,showState:1}, function(msg) {
      				common.hide();
      				if (msg.ok) {
      					// 提交成功刷新数据表格，关闭弹出层
      					layer.alert("操作成功。",function(){
      						layer.closeAll();
      						common.reloadTable(dataTable);
      					});
      				}else{
      					layer.alert(msg.msg);
      				}
      			});
      			layer.close(confirmIndex);
  			}, function(){  //取消
  				 obj.elem.checked=false;
  		    	form.render();
  			}); 
    	}
      });
    
    
});