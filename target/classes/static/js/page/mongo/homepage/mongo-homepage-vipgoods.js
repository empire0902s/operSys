layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, laydate = layui.laydate, 
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();
/*	var resultActivityGoodsRuleJSON=JSON.parse(resultActivityGoodsRule);
    //规则id
    var ruleid=resultActivityGoodsRuleJSON.id
    //规则类型
    var rtype=resultActivityGoodsRuleJSON.type;*/

	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl('/mongo/homepage/vipGoodsList'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:80, sort: true},
	      {field:'sortNum', title: '排序', width: 180,templet:function(val){
                  return val.sortNum+"\t\t<a class=\"layui-btn layui-btn-warm layui-btn-xs\" lay-event=\"updateSort\" >修改</a>"
              }},
	      {field:'goodsCode', title: '商品code', width: 300},
	      {field:'productName', title: '商品名称', width: 480},
	      // {field:'isSale', title: '上下架状态', width: 120,templet:function(val){
	    	//   if(val.isSale==0){
	    	// 	  return "<a class='layui-btn layui-btn-xs'style='background-color:#5bc0de '>待审</a>";
	    	//   }else if(val.isSale==1){
	    	// 	  return "<a class='layui-btn layui-btn-xs'style='background-color:#5cb85c '>上架</a>";
	    	//   }else if(val.isSale==2){
	    	// 	  return "<a class='layui-btn layui-btn-xs'style='background-color:#d9534f '>下架</a>";
	    	//   }else{
	    	// 	  return "<a class='layui-btn layui-btn-xs'style='background-color:#d9534f '>未知</a>";
	    	//   }
	      // }},
	      // {field:'fromSysCode', title: '活动栏目', width: 180,templet:function(val){
	    	//   if(val.fromSysCode==="invitationPage_Code"){
	    	// 	  return "邀请页";
	    	//   }else  if(val.fromSysCode==="militaryArea"){
	    	// 	  return "大兵专区";
	    	//   }
	      //
	      // }
	      // },
	      // {field:'goodsRuleName', title: '活动具体栏目', width: 180},
	      {fixed: 'right',title: '操作', width:150, align:'center', toolbar:'#editBar'}
	    ]],
    });

    // 添加事件
	$('.btn-add').click(function(data){
            layer.open({
                title: 'vip商品添加',
                type: 2,
                area: ['70%', '92%'],
                shadeClose: true,
                content: rootPath + "/mongo/homepage/vipandgoods?operType=1",
                success:function (layero,index) {
                }
            });
    });
    $('.btn-return').click(function(){
        window.history.go(-1);
    });
	
	$('.btn-close').click(function(){
		layer.closeAll();
	});

    form.on('submit(submit)', function(data) {
        if(data.field.sortNum.indexOf("+")!=-1){
            layer.alert("排序不能有特殊字符",{icon: 2});
            return false;
        }
        if(data.field.sortNum==0){
            layer.alert("排序不能为0",{icon: 2});
            return false;
        }
        common.wait("提交中...");
        common.post(rootPath + '/mongo/homepage/updateVipSort', data.field, function(msg) {
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

	var editForm= $('.edit-form');

    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
            case 'reed':
                    layer.open({
                        title: '查看规则详请',
                        type: 2,
                        area: ['70%', '92%'],
                        shadeClose: true,
                        content: rootPath + "/mongo/homepage/goodsdetailsSetting?&id="+data.id,
                        success:function (layero,index) {
                            /*   $(".submit", layero.find("iframe")[0].contentWindow.document).hide();
                               $(".search", layero.find("iframe")[0].contentWindow.document).hide();
                               $("#code", layero.find("iframe")[0].contentWindow.document).val(data.refSignValue);
                               $("#code", layero.find("iframe")[0].contentWindow.document).attr("disabled","disabled");*/
                        }
                    });
                    break;
            case 'delete':
                layer.confirm(" 确定是删除["+data.productName+"]"+"商品?", function(confirmIndex){
                    common.wait("提交中...");
                    $.ajax({
                        type: 'GET',
                        url:  rootPath + '/mongo/homepage/deveteVipGoods?id='+data.id,
                        dataType: "json",
                        success: function (msg) {
                            if (msg.ok){
                                common.hide();
                                layer.alert("操作成功。",function(){
                                                layer.closeAll();
                                                common.reloadTable(dataTable);
                                            });
                            }else {
                                common.hide();
                                layer.alert(msg.msg,function(){
                                    layer.closeAll();
                                });
                            }
                        },

                    });

                    layer.close(confirmIndex);
                });
                break;

            case 'updateSort':
                layer.open({
                    title:'编辑',
                    type: 1,
                    area: ['420px','375px'],
                    content: editForm,
                    success: function(){
                        //$("#sortNum").attr("disabled",true);
                        $(".type_t").attr("disabled",true);
                        // 加载表单，重新渲染
                        editForm.loadData(data);
                        form.render();
                    }
                });

            default:
			break;
		}
   });
});