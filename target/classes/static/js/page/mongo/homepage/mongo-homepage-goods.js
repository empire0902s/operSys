layui.use(['table', 'laytpl', 'form', 'common','laydate','element','upload'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl, element = layui.element, upload = layui.upload,
	table = layui.table, common = layui.common;
	//图片上传
	  var uploadInst = upload.render({
	    elem: '#file'
	    ,url: rootPath+'/auth/banner/upload/'
	    ,size:"1024"//限制上传图片大小，单位是kb
	    ,before: function(obj){
	      //预读本地文件示例，不支持ie8
	      obj.preview(function(index, file, result){
	    	  $('#imgDisplay').show();
	        $('#imgDisplay').attr('src', result); //图片链接（base64）
	      });
	      bFlag=1;
	    }
	    ,done: function(res){
	    	 //上传成功
	    	if(res.code ===0){
	    		layer.msg('上传成功');
		    	var imgPathVal=document.getElementById('imgUrl').value;
		    	document.getElementById('imgUrl').value=res.data.src;
                $(" input[ name='imgUrl' ] ").val(res.data.src);
		    	console.info("imgUrlVal:"+document.getElementById('imgUrl').value)
		    	$("#checkBigImg").attr("href",document.getElementById('imgUrl').value);
		    	bFlag=0;
	    	}
	      //如果上传失败
	      if(res.code > 0){
	    	  $('#imgDisplay').hide();
	    	  $("#imgUrl").val("");
	        return layer.msg('上传失败');
	      }
	     
	    }
	  });
    // 初始化编辑表单
	form.render();
		    var editForm = $('.edit-form');
	    // 添加事件
		$('.btn-add').click(function(){
            var mid= $("#moduleId").val();
            if (mid===null||mid===''){
				layer.alert("请选择模板");
				return false;
			}
            layer.open({
                title: '添加商品',
                type: 2,
                area: ['95%', '95%'],
                shadeClose: true,
                content: rootPath + "/mongo/homepage/addgood",
                success:function (layero,index) {
                    $("#moduleId", layero.find("iframe")[0].contentWindow.document).val(mid);
                }
            });
		});
		$('.btn-close').click(function(){
			layer.closeAll();
		});
		//保存
		form.on('submit(submit)', function(data) {
            var num=$(" input[ name='sortNum' ] ").val();
            if(!new RegExp(/^(0|[1-9]\d{0,9})$/).test(num)){
                layer.alert("排序必须是正整数",{icon: 2});
                return false;
            }else if (num==="0") {
                layer.alert("排序必须大于0",{icon: 2});
                return false;
            }

			common.wait("提交中...");
			common.post(rootPath + '/mongo/homepage/addgoods', data.field, function(msg) {
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
	// 初始化数据表格
		 var dataTable = common.renderTable({
			 height:500  //容器高度
            , url: common.getUrl('/mongo/homepage/goodsList')
            , where: $(".data-list-form").serializeJSON()
            , cols: [[
                {field: 'moduleId', title: '模块id', width: 180},
                {field: 'sortNum', title: '排序值', width: 100},
                {field: 'goodsCode', title: '商品code', width: 180},
                 {field: 'companyName', title: '供应商', width: 180},
                 {field: 'productName', title: '产品名称', width: 180},
                 {field: 'isSale', title: '上下架状态', width: 100 , templet:function(val) {
                 	if (val.isSale===0){
                 		return "待审核";
					}
					if (val.isSale===1){
                 		return "上架";
					}
					if (val.isSale===2){
                 		return "下架";
					}
                 }},
                {field: 'createAdmin', title: '创建人', width: 180},
                {field: 'createTime', title: '创建时间', width: 180},
                {fixed: 'right',title: '操作',align:'center', toolbar:'#editBar',width:150}
            ]], //设置表头
            page: {
                limit: 10,
                limits: [10, 15, 25, 50]
            }
        });
	
	$('.btn-close').click(function(){
		layer.closeAll();
	});
  	
	var editForm = $('.edit-form'), roleForm = $('.role-form'),grantForm=$('.grant-form');
    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
		case 'delete':
            layer.confirm("确认要删除吗，删除后不能恢复", { title: "删除确认" }, function (index) {
                layer.close(index);
                common.post(rootPath + '/mongo/homepage/deleteGoods', {
                    moduleId: data.moduleId,
                    id: data.id
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
		case 'edit':

            var mid= $("#moduleId").val();
            // var groupItemId=
            // $('.btn-add').click(function(data){
            layer.open({
                title: '添加商品',
                type: 2,
                area: ['95%', '95%'],
                shadeClose: true,
                content: rootPath + "/mongo/homepage/addgood",
                success:function (layero,index) {
                    $("#code", layero.find("iframe")[0].contentWindow.document).val(data.goodsCode);
                    $("#id", layero.find("iframe")[0].contentWindow.document).val(data.id);
                    $("#groupItemId", layero.find("iframe")[0].contentWindow.document).val(data.groupItemId);
                    $("#goodsItemId", layero.find("iframe")[0].contentWindow.document).val(data.goodsItemId);
                    $("#moduleId", layero.find("iframe")[0].contentWindow.document).val(mid);
                    $("#sortNum", layero.find("iframe")[0].contentWindow.document).val(data.sortNum);

                }
            });

            break;
		case 'jump':
			window.open(data.loadMoreTarget);
			break;
		default:
			break;
		}
   });
});