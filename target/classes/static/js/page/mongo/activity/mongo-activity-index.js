layui.use(['table', 'laytpl', 'form', 'common','laydate','upload'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl,  upload = layui.upload,
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();

	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl(rootPath + '/mongo/activity/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:150, sort: true},
	      {field:'activityName', title: '活动名称', width: 300},

		  {field:'bannerImg', title: 'banner图片', width: 150,templet:function(val) {
                  return "<img style='width: 50px; height: 30px;'" +
                      "src='"+val.bannerImg+"'>"
              }},
		  {field:'state', title: '启用状态', width: 100,templet:function(val) {
                if (val.state===1){
                    return "启用";
                } else {
                    return "禁用";
                }
              }},
		  {field:'description', title: '描述', width: 200},
	      {fixed: 'right',title: '操作', width:300, align:'center', toolbar:'#editBar'}
	    ]],
    });

	
	$('.btn-close').click(function(){
		layer.closeAll();
	});

    var editForm = $('.edit-form');
    $('.btn-add').click(function(){
        $('#imgDisplay').hide();
        $('#imgDisplay').attr('src', ''); //图片链接（base64）
        editForm.find('form')[0].reset();
        layer.open({
            title:'添加',
            type: 1,
            area:  ['500px'],
            content: editForm,
            success: function(){
                editForm.loadData({id:''});
            }
        });
    });

    //图片上传
    var bFlag=1;
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
    form.on('submit(submit)', function(data) {
        //没有上传图片前
        if(data.field.bannerImg===""){
            layer.alert('请上传图片', {icon: 2});
            return false;
        }
        if(bFlag===1){
            layer.alert('请等待图片上传完成', {icon: 2});
            return false;
        }

        common.wait("提交中...");
        common.post( rootPath + '/mongo/activity/save', data.field, function(msg) {
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
                    common.post(rootPath + '/mongo/activity/delete', {
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
			case 'update':
                $('#imgDisplay').show();
                $('#imgDisplay').attr('src', data.bannerImg); //图片链接（base64）
                bFlag=0;

                layer.open({
                    title:'编辑',
                    type: 1,
                    area:  ['500px'],
                    content: editForm,
                    success: function(){
                        // 加载表单，重新渲染
                        editForm.loadData(data);
                        form.render();
                    }
                });
                break;
		case 'goodssetting':
			layer.open({
				title:data.activityName+'的栏目管理',
				type: 2,
				area: ['95%','95%'],
				shadeClose: true,
				content: rootPath + '/mongo/activity/main?id='+data.id,

			});
			break;

            case 'columSetting':
                layer.open({
                    title:data.activityName+'的栏目管理',
                    type: 2,
                    area: ['95%','95%'],
                    shadeClose: true,
                    content: rootPath + '/mongo/activity/columnsetting?id='+data.id,

                });
                break;
		default:
			break;
		}
   });
});