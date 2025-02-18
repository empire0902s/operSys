layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, laydate = layui.laydate, 
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();

	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl('/mongo/homepage/businnessGoodsList'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:80, sort: true},
	      {field:'sortNum', title: '排序', width: 180,templet:function(val){
                  return val.sortNum+"\t\t<a class=\"layui-btn layui-btn-warm layui-btn-xs\" lay-event=\"updateSort\" >修改</a>"
              }},
            {field:'type', title: '商品类型', width: 180,templet:function(val){
                  if (val.type===0){
                      return "平台商品"
                  }
                  if (val.type===1){
                      return "品牌商品";
                  }
                }},
            {field:'isSale', title: '状态', width: 180,templet:function(val){
                    if (val.isSale===1){
                        return "上架"
                    }
                    if (val.isSale===2){
                        return "下架";
                    }
                }},
	      {field:'goodsCode', title: '商品code', width: 300},
	      {field:'productName', title: '商品名称', width: 480},
            {field:'createAdmin', title: '创建人', width: 300},
            {field:'createTime', title: '创建时间', width: 480},
	      {fixed: 'right',title: '操作', width:150, align:'center', toolbar:'#editBar'}
	    ]]
    });

    $('.btn-sync').click(function () {
        common.wait("提交中...");
        common.post(rootPath + '/mongo/homepage/syncBusinnessGoods', {}, function(msg) {
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
    // 添加事件
	$('.btn-add').click(function(data){
            layer.open({
                title: '百业惠盟商品添加',
                type: 2,
                area: ['70%', '92%'],
                shadeClose: true,
                content: rootPath + "/mongo/homepage/businessandgoods?operType=1",
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
        common.post(rootPath + '/mongo/homepage/updateBusinessSort', data.field, function(msg) {
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
                        content: rootPath + "/mongo/homepage/businessandgoods?&code="+data.goodsCode,
                        success:function (layero,index) {
                            $("#sort", layero.find("iframe")[0].contentWindow.document).val(data.sortNum);
                            $("#saveToList", layero.find("iframe")[0].contentWindow.document).attr("disabled","disabled");

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
                        url:  rootPath + '/mongo/homepage/deveteBusinessGoods?id='+data.id+"&code="+data.goodsCode,
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