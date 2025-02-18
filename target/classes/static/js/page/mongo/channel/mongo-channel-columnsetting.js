layui.use(['table', 'laytpl', 'form', 'common','laydate','upload'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl,  upload = layui.upload,
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();

    var Ohref=window.location.href;
    var arrhref=Ohref.split("?id=");
    var mid=arrhref[1];
    $("#mid").val(mid);
    $(".mId").val(mid);

	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl(rootPath + '/mongo/channel/moduleList'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:150, sort: true},
	      {field:'modelName', title: '栏目名', width: 200},
	      {field:'goodsType', title: '栏目类型', width: 150, templet:function(val) {
                  if (val.goodsType===1){
                      return "普通商品";
                  }
                  if (val.goodsType===2){
                      return "品牌广场";
                  }
                  if (val.goodsType===3){
                      return "预售商品";
                  }
              }},
            {field:'isShow', title: '展示状态', width: 150, templet:function(val) {
                    if (val.isShow===0){
                        return "隐藏";
                    }
                    if (val.isShow===1){
                        return "展示";
                    }

                }},
	      {field:'bannerNum', title: 'banner数量', width: 180},
	      {field:'sort', title: '排序', width: 300},
	      {fixed: 'right',title: '操作', width:250, align:'center', toolbar:'#editBar'}
	    ]],
    });

	
	$('.btn-close').click(function(){
		layer.closeAll();
	});

    var editForm = $('.edit-form');
    $('.btn-add').click(function(){
        editForm.find('form')[0].reset();
        layer.open({
            title:'添加',
            type: 1,
            area:['30%','80%'],
            content: editForm,
            success: function(){
                editForm.loadData({id:''});
            }
        });
    });

    form.on('submit(submit)', function(data) {

        var num=$("#sortNum").val();
        if(!new RegExp(/^(0|[1-9]\d{0,9})$/).test(num)){
            layer.alert("排序必须是正整数",{icon: 2});
            return false;
        }if (num==="0"){
            layer.alert("排序必须大于0",{icon: 2});
            return false;
        }
        common.wait("提交中...");
        common.post( rootPath + '/mongo/channel/saveModel', data.field, function(msg) {
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
  	

    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
			case 'delete':
                layer.confirm("确认要删除吗，删除后不能恢复", { title: "删除确认" }, function (index) {
                    layer.close(index);
                    common.post(rootPath + '/mongo/channel/deleteModel', {
                        id: data.id,
                        mId:mid
                    }, function (msg) {
                        common.hide();
                        if (msg.ok) {
                            // 提交成功刷新数据表格，关闭弹出层
                            layer.alert("删除成功。", function () {
                                layer.closeAll();
                                common.reloadTable(dataTable);
                            });
                        } else {
                            layer.alert(msg.msg);
                        }
                    });
                });
                break;
			case 'update':
                layer.open({
                    title:'编辑',
                    type: 1,
                    area:  ['30%','80%'],
                    content: editForm,
                    success: function(){
                        // 加载表单，重新渲染
                        editForm.loadData(data);
                        form.render();
                    }
                });
                break;

		default:
			break;
		}
   });
});