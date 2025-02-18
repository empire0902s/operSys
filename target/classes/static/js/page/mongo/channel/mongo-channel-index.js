layui.use(['table', 'laytpl', 'form', 'common','laydate','upload'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl,  upload = layui.upload,
	table = layui.table, common = layui.common;
	
    // 初始化编辑表单
	form.render();

	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl(rootPath + '/mongo/channel/list'),
	    where:  $(".data-list-form").serializeJSON(),
	    cols: [[
	      {field:'id', title: 'ID', width:150, sort: true},
	      {field:'channelNum', title: '渠道号', width: 150},
	      {field:'name', title: '渠道名', width: 150},
	      {field:'templateType', title: '模板样式', width: 100,templet:function(val) {
	          if (val.templateType===1){
	              return "模板样式1";
              }else if (val.templateType===2) {
                  return "模板样式2";
              }else if (val.templateType===3){
                  return "模板样式3";
              } else if (val.templateType===4){
                  return "模板样式4";
              }else if (val.templateType===5){
                  return "模板样式5";
              }
              }},
            {field: 'brandDescImg', title: '品牌说明图片',  width: 150,templet:function(val) {
                    return "<img style='width: 50px; height: 30px;'" +
                        "src='"+val.brandDescImg+"'>"
                }},
		  // {field:'loveDescImg', title: '爱之家描述图片', width: 150,templet:function(val) {
           //        return "<img style='width: 50px; height: 30px;'" +
           //            "src='"+val.loveDescImg+"'>"
           //    }},
		  {field:'qrCodeUrl', title: '二维码', width: 150,templet:function(val) {
                  return "<img style='width: 50px; height: 30px;'" +
                      "src='"+val.qrCodeUrl+"'>"
              }},
		  {field:'iosLoadUrl', title: 'ios下载地址', width: 180},
		  {field:'androndLoadUrl', title: '安卓下载地址', width: 180},
		  {field:'state', title: '启用状态', width: 100,templet:function(val) {
                if (val.state===1){
                    return "启用";
                } else {
                    return "禁用";
                }
              }},
		  {field:'description', title: '爱之家描述', width: 150},
	      {fixed: 'right',title: '操作', width:300, align:'center', toolbar:'#editBar'}
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
            area:  ['800px', '500px'],
            content: editForm,
            success: function(){
                editForm.loadData({id:''});
            }
        });
    });

    //图片上传
    var uploadInst = upload.render({
        elem: '#logoFile'
        ,url: rootPath+"/auth/bannerAuxiliary/upload"
        ,size:"1024"//限制上传图片大小，单位是kb
        ,before: function(obj){
            //预读本地文件示例，不支持ie8
            obj.preview(function(index, file, result){
                $("#imgDisplay").show();
                $("#imgDisplay").attr('src', result); //图片链接（base64）
            });
            bFlag=1;
        }
        ,done: function(res){
            //上传成功
            if(res.code ==0){
                layer.msg('上传成功');
                var imgPathVal=document.getElementById('logoURL').value;
                document.getElementById('logoURL').value=res.data.src;
                $("#checkBigImg").attr("href",document.getElementById('logoURL').value);
                bFlag=0;
            }
            //如果上传失败
            if(res.code > 0){
                $('#imgDisplay').hide();
                $("#logoURL").val("");
                return layer.msg('上传失败');
            }

        }
    });
    var uploadInst = upload.render({
        elem: '#logoBannerFile'
        ,url: rootPath+"/auth/bannerAuxiliary/upload"
        ,size:"1024"//限制上传图片大小，单位是kb
        ,before: function(obj){
            //预读本地文件示例，不支持ie8
            obj.preview(function(index, file, result){
                $("#logoBannerImgDisplay").show();
                $("#logoBannerImgDisplay").attr('src', result); //图片链接（base64）
            });
            bFlag2=1;
        }
        ,done: function(res){
            //上传成功
            if(res.code ==0){
                layer.msg('上传成功');
                var imgPathVal=document.getElementById('logoBannerURL').value;
                document.getElementById('logoBannerURL').value=res.data.src;
                console.info("imgPathVal:"+document.getElementById('logoBannerURL').value)
                $("#bannerCheckBigImg").attr("href",document.getElementById('logoBannerURL').value);
                bFlag2=0;
            }
            //如果上传失败
            if(res.code > 0){
                $('#logoBannerImgDisplay').hide();
                $("#logoBannerURL").val("");
                return layer.msg('上传失败');
            }
        }
    });
    var uploadInst = upload.render({
        elem: '#activeBannerFile'
        ,url: rootPath+"/auth/bannerAuxiliary/upload"
        ,size:"1024"//限制上传图片大小，单位是kb
        ,before: function(obj){
            //预读本地文件示例，不支持ie8
            obj.preview(function(index, file, result){
                $("#activeBannerImgDisplay").show();
                $("#activeBannerImgDisplay").attr('src', result); //图片链接（base64）
            });
            bFlag3=1;
        }
        ,done: function(res){
            //上传成功
            if(res.code ==0){
                layer.msg('上传成功');
                var imgPathVal=document.getElementById('activeBannerURL').value;
                document.getElementById('activeBannerURL').value=res.data.src;
                console.info("imgPathVal:"+document.getElementById('activeBannerURL').value)
                $("#activityCheckBigImg").attr("href",document.getElementById('activeBannerURL').value);
                bFlag3=0;
            }
            //如果上传失败
            if(res.code > 0){
                $('#activeBannerImgDisplay').hide();
                $("#activeBannerURL").val("");
                return layer.msg('上传失败');
            }
        }
    });

    form.on('submit(submit)', function(data) {
        common.wait("提交中...");
        common.post( rootPath + '/mongo/channel/save', data.field, function(msg) {
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
                    common.post(rootPath + '/mongo/channel/delete', {
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
                $('#imgDisplay').attr('src', data.brandDescImg); //图片链接（base64）
                $('#logoBannerImgDisplay').show();
                $('#logoBannerImgDisplay').attr('src', data.loveDescImg); //图片链接（base64）
                $('#activeBannerImgDisplay').show();
                $('#activeBannerImgDisplay').attr('src', data.qrCodeUrl); //图片链接（base64）

                layer.open({
                    title:'编辑',
                    type: 1,
                    area: ['800px', '500px'],
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
				title:data.name+'的栏目管理',
				type: 2,
				area: ['95%','95%'],
				shadeClose: true,
				content: rootPath + '/mongo/channel/main?id='+data.id,

			});
			break;

            case 'columSetting':
                layer.open({
                    title:data.name+'的栏目管理',
                    type: 2,
                    area: ['95%','95%'],
                    shadeClose: true,
                    content: rootPath + '/mongo/channel/columnsetting?id='+data.id,

                });
                break;
		default:
			break;
		}
   });
});