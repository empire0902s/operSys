layui.use(['table', 'laytpl', 'form', 'common','laydate','element','upload'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl, element = layui.element, upload = layui.upload,
	table = layui.table, common = layui.common;
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
		    	var imgPathVal=document.getElementById('imgPath').value;
		    	document.getElementById('imgPath').value=res.data.src;
                $(" input[ name='imageLocation' ] ").val(res.data.src);
		    	console.info("imageLocation:"+document.getElementById('imgPath').value)
		    	$("#checkBigImg").attr("href",document.getElementById('imgPath').value);
		    	bFlag=0;
	    	}
	      //如果上传失败
	      if(res.code > 0){
	    	  $('#imgDisplay').hide();
	    	  $("#imageLocation").val("");
	        return layer.msg('上传失败');
	      }
	     
	    }
	  });
    // 初始化编辑表单
	form.render();
		    var editForm = $('.edit-form');
	    // 添加事件
		$('.btn-add').click(function(){
            var mid= $("#columnId").val();
            if (mid===null||mid===''){
                layer.alert("请选择模板");
                return false;
            }
            $('#imgDisplay').hide();
            $('#imgDisplay').attr('src', ''); //图片链接（base64）
			editForm.find('form')[0].reset();
			layer.open({
				title:'添加',
				type: 1,
				area: ['400px','450px'],
				content: editForm,
				success: function(){
			    	$("#imgPath").val("");
			    	$("#columnIdReal").val($("#columnId").val());
					editForm.loadData({id:''});
                //    $(" input[ name='moduleId' ] ").val($("#moduleId").val());
					// $("#moduleId2").val($("#moduleId").val());
					// $("#moduleId2").text($("#moduleId").val());
					form.render();
				}
			});
		});
		$('.btn-close').click(function(){
			layer.closeAll();
		});
		//保存
		form.on('submit(submit)', function(data) {
			//没有上传图片前
			if(data.field.imageLocation===""){
				layer.alert('请上传图片', {icon: 2}); 
				return false;
			}
			if(bFlag===1){
				layer.alert('请等待图片上传完成', {icon: 2}); 
				return false;
			}
      /*      var num=$(" input[ name='sortNum' ] ").val();
            if(!new RegExp(/^(0|[1-9]\d{0,9})$/).test(num)){
                layer.alert("排序必须是正整数",{icon: 2});
                return false;
            }else if (num==="0") {
                layer.alert("排序必须大于0",{icon: 2});
                return false;
			}*/
			common.wait("提交中...");
			common.post(rootPath + '/mongo/brandsquaresetting/addBrandSquareBanner', data.field, function(msg) {
				common.hide();
				if (msg.ok) {
					// 提交成功刷新数据表格，关闭弹出层
					layer.alert("操作成功。",function(){
						layer.closeAll();
						document.getElementById('reload').click();
					});
				}else{
					layer.alert(msg.msg);
				}
			});
			$("#columnId").val(data.field.columnId);
			$("#columnIdReal").val(data.field.columnId);
			return false;
		});
	// 初始化数据表格
		 var dataTable = common.renderTable({
			 height:500  //容器高度
            , url: common.getUrl('/mongo/brandsquaresetting/bannerList')
            , where: $(".data-list-form").serializeJSON()
            , cols: [[
                {field: 'columnId', title: '模块id', width: 180},
 /*               {field: 'sortNum', title: '排序值', width: 180},*/
                {field: 'title', title: '标题', width: 180},
                {field: 'imageLocation', title: '图片',  width: 180,templet:function(val) {
                        return "<img style='width: 50px; height: 30px;'" +
                            "src='"+val.imageLocation+"'>"
                    }},
                {field: 'jumpTarget', title: '图片跳转连接', width: 180},
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
                common.post(rootPath + '/mongo/brandsquarebanner/delete', {
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
            bFlag=0;
            $('#imgDisplay').show();
            $('#imgDisplay').attr('src', data.imgUrl); //图片链接（base64）
			layer.open({
				title:'编辑',
				type: 1,
				area: ['400px'],
				content: editForm,
				success: function(){
					// 加载表单，重新渲染
					if(data.imageLocation!=null&&data.imageLocation!=""){
						$('#imgDisplay').attr('src', data.imageLocation);
					}else{
						  $('#imgDisplay').hide();
					}
					editForm.loadData(data);
					form.render();
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