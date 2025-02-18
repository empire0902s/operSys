layui.use(['table', 'laytpl', 'form', 'common', 'laydate' ], function() {
	var form = layui.form, laytpl = layui.laytpl, table = layui.table, common = layui.common,laydate = layui.laydate;
    // 初始化编辑表单
	form.render();
	 $("select[url]").loadSelect();
	laydate.render({
		elem : '#sendTimeStart',
		type : 'datetime'
	});
	var et=laydate.render({
		elem : '#sendTimeEnd',
		type : 'datetime',
        done:function(value,date){
            var strTime=value.substr(11,8);
            if ("00:00:00"===strTime){
                et.config.dateTime={
                    year:date.year,
                    month:date.month-1,
                    date: date.date,
                    hours: 23,
                    minutes: 59,
                    seconds : 59
                };
            }
        }
	});
	laydate.render({
		elem : '#finanAuditTime',
		type : 'datetime'
	});
	var couponsIdsArray;
	table.on('checkbox(data-list)', function(obj){
		var checkStatus = table.checkStatus('couponsList')
	      ,data = checkStatus.data;
		couponsIdsArray=JSON.stringify(data);//将数据转为json字符串
		couponsIdsArray=JSON.parse(couponsIdsArray);//返回的是一个json数组
		});
	var couponsIds="";
	//财务审核事件
	$('.btn-check').click(function(){
		if(couponsIdsArray==null||couponsIdsArray.length==0){
			layer.alert("请选择要通过审核的优惠券");
			return;
		}
		couponsIds="";
		if(couponsIdsArray.length>0){
			for(var i=0;i<couponsIdsArray.length;i++){
				couponsIds+=couponsIdsArray[i].id+",";
			}
		}
		//拼接字符串，以逗号隔开
		couponsIds=couponsIds.substring(0,couponsIds.length-1);
		var editForm = $('.edit-form');
		editForm.find('form')[0].reset();
		//弹出审核框
		form.render();
		layer.open({
			title:'审核',
			type: 1,
			area: ['400px','300px'],
			content: editForm,
			success: function(){
				editForm.loadData({id:''});
			}
		});
	form.on('submit(submit)', function(data) {
		layer.confirm("是否确定审核通过这"+(couponsIdsArray.length)+"条优惠券数据?", function(confirmIndex){
			common.post(rootPath + '/coupons/examine/changeFinanStatus', 
			{'finanAuditRemark':data.field.finanAuditRemark,couponsIds:couponsIds},
			 function(msg) {
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
		});
		return false;
		});
	});
	$('.btn-close').click(function(){
		layer.closeAll();
	});
	form.verify({
		pwd: [/.{6,}/, '请输入6-32位密码']
	});
	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl('/coupons/examine/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    id:'couponsList',
	    cols: [[
	      {type:'checkbox'},
	      {field:'id', title: 'ID', width:100, sort: true},
	      {field:'name', title: '优惠券名称', width: 150},
	      {field:'batch', title: '优惠券批次', width: 150},
	      {field:'couponsTypeRuleName', title: '金额规则', width: 220},
	      {field:'sendTimeStart', title: '发放开始时间', width: 230},
	      {field:'sendTimeEnd', title: '发放结束时间', width: 230},
	      {field:'finanStatus', title: '审核状态', width: 150,templet:function(val,row){
	    	  if(val.finanStatus== "0"){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#5bc0de '>待审核</a>";
	    	  }  else  if(val.finanStatus== "1"){
	    		  return "<a class='layui-btn layui-btn-xs'style='background-color:#5cb85c '>审核通过</a>";
	    	  }  else{
	    		  return "---";
	    	  }
	      } },
		    {field:'sellNum', title: '已发放数量', width: 130,templet:function(val,row){
		    		if(val.sellNum==null){
		    			return 0;
		    		}  else{
		    			return val.sellNum;
		    		}
		    } },
		    {fixed: 'right', title: '操作', width: 170,align:'center', toolbar:'#editBar'}
	    ]],
    });
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
		case 'details':
			layer.open({
				title:'详情',
				type: 2,
				area: ['68%','83%'],
				shadeClose: true,
				content: rootPath + '/coupons/coupons/couponsDetails?id='+data.id
			});
			break;
		default:
			break;
		}
   });
});