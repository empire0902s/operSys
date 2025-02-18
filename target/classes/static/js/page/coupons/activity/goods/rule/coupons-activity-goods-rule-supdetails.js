layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, laydate = layui.laydate, 
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();
	//截取上个页面传过来的参数
    var Ohref=window.location.href;
    var arrhref=Ohref.split("?ruleId=");
    var aray=arrhref[1].split("&type=");
    //规则id
    var ruleid=aray[0]
    //规则类型
    var rtype=aray[1];

	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl('/coupons/activity/goods/rule/detailsList?ruleid='+ruleid),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:50, sort: true},
	      {field:'refSignValue', title: '规则关联值供应商seq', width: 300},
          {field:'ruleType', title: '规则类型', width: 200,templet:function(val,row){
                  if(val.ruleType===0){
                      return "自由选择商品";
                  }else if (val.ruleType===1){
                      return "一级类目";
                  } else if (val.ruleType===2) {
                      return "二级类目";
                  }else if (val.ruleType===3){
                      return "三级类目";
                  } else if(val.ruleType===4){
                      return "四级类目";
                  }else if (val.ruleType===5){
                      return "供应商";
                  }
              }},
            {field:'refSignName', title: '供应商名称', width: 300},
	      // {field:'updateTime', title: '更新时间', width: 160},
	      // {field:'updateAdmin', title: '更新人', width: 160},
	      {fixed: 'right',title: '操作', width:200, align:'center', toolbar:'#editBar'}
	    ]],
    });

    // 添加事件
	$('.btn-add').click(function(data){
            layer.open({
                title: '添加规则详请',
                type: 2,
                area: ['95%', '95%'],
                shadeClose: true,
                content: rootPath + "/coupons/activity/goods/rule/supplier?ruleId="+ruleid+"&id="+0,
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

    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
            case 'reed':

                layer.open({
                    title: '查看供应商详请',
                    type: 2,
                    area: ['95%', '95%'],
                    shadeClose: true,
                    content: rootPath + "/coupons/activity/goods/rule/reedsupdetails?ruleId="+ruleid+"&id="+data.id,
                    success:function (layero,index) {
                        $(".submit", layero.find("iframe")[0].contentWindow.document).hide();
                        $(".search", layero.find("iframe")[0].contentWindow.document).hide();
                        $("#seq", layero.find("iframe")[0].contentWindow.document).val(data.refSignValue);
                        $("#shopName", layero.find("iframe")[0].contentWindow.document).val(data.refSignName);
                        $("#seq", layero.find("iframe")[0].contentWindow.document).attr("disabled","disabled");
                        $("#shopName", layero.find("iframe")[0].contentWindow.document).attr("disabled","disabled");
                    }
                });
                break;

            case 'delete':
                layer.confirm(" 确定是删除["+data.refSignName+"]"+"规则明细?", function(confirmIndex){
                    common.wait("提交中...");
                    $.ajax({
                        type: 'GET',
                        url:  rootPath + '/coupons/activity/goods/rule/delete?detailId='+data.id,
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
            default:
			break;
		}
   });
});