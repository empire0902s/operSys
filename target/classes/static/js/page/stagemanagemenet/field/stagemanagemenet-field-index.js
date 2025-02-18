layui.use(['table', 'laytpl', 'laydate', 'form', 'common','layer'], function() {
	var form = layui.form, 
	table = layui.table, 
	laytpl = layui.laytpl, 
	laydate = layui.laydate, 
	layer = layui.layer,
	common = layui.common;

    $("select[url]").loadSelect();
    
    var ue_Add = null;
 // 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl( rootPath + '/stagemanagemenet/field/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:50, sort: true},
	      {field:'name', title: '场次名称', width: 170},
	      {field:'addressName', title: '场地名称', width: 170},
	      {field:'status', title: '场次状态', width: 150,templet:function(val,row){
	    	  if(val.status== "0"){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#5bc0de '>未开始</a>";
	    	  }  else  if(val.status== "1"){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#5FB878 '>进行中</a>";
	    	  }   else  if(val.status== "2"){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#FF5722 '>已结束</a>";
	    	  }  
	      } },
	      {field:'beginTime', title: '开始时间', width: 160},
	      {field:'endTime', title: '结束时间', width: 160},
	      {field:'isDisabled', title: '是否禁用', templet:'#isDisabledTpl',width: 130},
	      {fixed: 'right',title: '操作', width:188, align:'center', toolbar:'#editBar'}
	    ]],
    });
    form.on('switch(isDisabledFilter)', function(obj){
    	var isDisabledDom=obj.elem;
        var p=isDisabledDom.parentNode.parentNode.parentNode;//获取单选框所在行的的tr节点;
        var tmp= p.childNodes[1];
    	var flag=obj.elem.checked;//flag=true即点击之前是未选中，flag=false即点击之前是已选中
    	if(flag){
    		layer.confirm("确定是否禁用["+tmp.innerText+"]?",  {  
    			  btn: ['确定','取消'] //按钮  
    			  ,cancel: function(index, layero){  
    				  obj.elem.checked=false;
      		    	form.render();
    			  }  
    			}, function(confirmIndex){  
    			//是  		common.wait("提交中...");
        			common.post( rootPath + '/stagemanagemenet/field/disabled', {id:obj.value}, function(msg) {
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
    	}else{
    		layer.confirm("确定是否启用["+tmp.innerText+"]?", {  
  			  btn: ['确定','取消'] //按钮  
  			  ,cancel: function(index, layero){  
  			    //取消操作，点击右上角的X  
  				 obj.elem.checked=true;
   		    	form.render();
  			  }  
  			}, function(confirmIndex){  
      			common.post( rootPath + '/stagemanagemenet/field/enableBy', {id:obj.value}, function(msg) {
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
    	}
      });
	laydate.render({
		elem : "input[name='beginTime']"
		,type: 'datetime'
	});
    laydate.render({
        elem : "input[name='endTime']"
        ,type: 'datetime'
    });

	form.verify({
		pwd: [/.{6,}/, '请输入6-32位密码']
	});
	var index;
	 // 初始化编辑表单
	form.render();

    form.verify({
        sendTimeEnd:function (value,item) {
            var $startTime=$(" input[ name='beginTime' ] ").val();
			if (value<=$startTime){
                return '演出结束时间不能小于演出开始时间';
			}
        }
    });


    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "H+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };



    form.on('submit(submit)', function(data) {

		common.wait("提交中...");
		common.post( rootPath + '/stagemanagemenet/field/save', data.field, function(msg) {
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
		return false;
	});


    var editForm = $('.edit-form');
    // 添加事件
	$('.btn-add').click(function(){
		editForm.find('form')[0].reset();
        var time = new Date().Format("yyyyMMddHHmmssS");
        $(" input[ name='batch' ] ").val(time);
		layer.open({
			title:'添加',
			type: 1,
			area:  ['550px', '600px'],
			content: editForm,
			success: function(){
                $("select[url]").loadSelect();
                editForm.loadData({id:''});
                editForm.find("input,select").not("input[name='name'],input[name='isDisabled']").removeAttr("disabled");
                $("#actid").hide();
			}
		});
	});

	$('.btn-close').click(function(){
		layer.closeAll();
	});


    form.on('select(type)', function(data){
    	if (data.value==="1"){
			$("#activityGoodsRuleId").val("");
            $("#actid").hide();
		}else {

            $("#actid").show();
		}
    });



    var editForm = $('.edit-form'), roleForm = $('.role-form'),grantForm=$('.grant-form');
    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
		case 'edit':
			layer.open({
				title:'编辑',
				type: 1,
				area:  ['550px', '600px'],
				content: editForm,
				success: function(){
                    editForm.loadData(data);
                    //移除多余选项
      				$("select option[selected='false']").remove();
      				editForm.find("input,select").not("input[name='name'],select[name='status'],input[name='addressName'],input[name='isDisabled']").attr("disabled","disabled");

      				$("#actid").hide();
                    form.render();
                    //
                    // ue_Add = ueditor.getEditor('activityDesc',{
                    //     toolbars:[[  'undo', 'redo', '|',
                    //         'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript','|', 'cleardoc', '|', 'simpleupload']],
                		// autoClearinitialContent:false,
                    //     wordCount:false,
                    //     elementPathEnabled:false,
                    //     initialFrameHeight:230,
                    //     initialFrameWidth:500
                    // });

				},
				end: function(){
					if(ue_Add){
	            		ue_Add.destroy();
	            	}
	            }
			});
			break;
		case 'disabled':
			layer.confirm("确定是否禁用["+data.name+"]?", function(confirmIndex){
				common.wait("提交中...");
				common.post( rootPath + '/stagemanagemenet/field/disabled', {id:data.id}, function(msg) {
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
			});
			break;
		case 'enableBy':
			layer.confirm("确定是否启用["+data.name+"]?", function(confirmIndex){
				common.wait("提交中...");
				common.post(rootPath + '/stagemanagemenet/field/enableBy', {id:data.id}, function(msg) {
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
			});
			break;
		case 'details':
			layer.open({
				title:'详细信息',
				type: 2,
				area: ['55%','37%'],
				shadeClose: true,
				content:  rootPath + '/coupons/activity/couponsActivityDetails?id='+data.id
			});
			break;

            case 'bingcoupons':
                layer.open({
                    title: data.name+'绑定的优惠券列表',
                    type: 2,
                    area: ['90%', '90%'],
                    shadeClose: true,
                    content: rootPath + "/coupons/activity/coupons?activityId="+data.id,
                    success:function (layero,index) {

                    }
                });
                break;
		default:
			break;
		}
   });
});