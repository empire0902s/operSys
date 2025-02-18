layui.use(['table', 'laytpl', 'form', 'common','upload'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, upload = layui.upload,
	table = layui.table, common = layui.common;
    form.render();
    
    
    $('.btn-close').click(function(){
    	layer.closeAll();
		//关闭当前内嵌页面窗口
		var index = parent.layer.getFrameIndex(window.name);  
		parent.layer.close(index);
	});

	var arrayJson=JSON.parse(array);
	var sortNumJson=JSON.parse(sortNum);
	var bFlag=0;
	//图片上传
	  var uploadInst = upload.render({
	    elem: '#file'
	    ,url: rootPath+'/mongo/brandrecommend/upload/'
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
	    	if(res.code ==0){
	    		layer.msg('上传成功');
		    	var imgPathVal=document.getElementById('imgPath').value;
		    	document.getElementById('imgPath').value=res.data.src;
		    	console.info("imgPathVal:"+document.getElementById('imgPath').value)
		    	$("#checkBigImg").attr("href",document.getElementById('imgPath').value);
		    	bFlag=0;
	    	}
	      //如果上传失败
	      if(res.code > 0){
	    	  $('#imgDisplay').hide();
	    	  $("#imgPath").val("");
	        return layer.msg('上传失败');
	      }
	     
	    }
	  });
    //保存品牌推荐
		form.on('submit(submit)', function(data) {
			//没有上传图片前
			if(data.field.brandLogoImg==""){
				layer.alert('请上传图片', {icon: 2}); 
				return false;
			}
			if(bFlag==1){
				layer.alert('请等待图片上传完成', {icon: 2}); 
				return false;
			}
			
		//将添加的品牌图片显示出来
		var liHtmlTmp="<li style='list-style-type: none;'><img alt='' style='width: 152px;'" +
					"src='"+data.field.brandLogoImg+"'></li>"
		window.parent.document.getElementById("showUl").innerHTML+=liHtmlTmp;
		//在表单中添加一个包含当前品牌数据的div
		if(array==0){
			var formHtmlTmp="<input type='hidden' name='recommend[0].brandName' value='"+data.field.brandName+"' readonly='readonly'>" +
			"<input type='hidden'  name='recommend[0].brandLogoImg' value='"+data.field.brandLogoImg+"' readonly='readonly'>" +
			"<input type='hidden' name='recommend[0].jumpTarget' value='"+data.field.jumpTarget+"' readonly='readonly'>" +
			"<input type='hidden' name='recommend[0].sortNum' value='"+data.field.sortNum+"'readonly='readonly'>";
			window.parent.document.getElementById("recommendListForm").innerHTML+=formHtmlTmp;
		}else{
			var formHtmlTmp="<p style='display: none'>------------------------------</p>"+
			"<input type='hidden' name='recommend["+arrayJson+"].brandName' value='"+data.field.brandName+"' readonly='readonly'>" +
			"<input type='hidden'  name='recommend["+arrayJson+"].brandLogoImg' value='"+data.field.brandLogoImg+"' readonly='readonly'>" +
			"<input type='hidden' name='recommend["+arrayJson+"].jumpTarget' value='"+data.field.jumpTarget+"' readonly='readonly'>" +
			"<input type='hidden' name='recommend["+arrayJson+"].sortNum' value='"+data.field.sortNum+"'readonly='readonly'>";			window.parent.document.getElementById("recommendListForm").innerHTML+=formHtmlTmp;
		}
			alert("保存成功");
			var index = parent.layer.getFrameIndex(window.name);  
			parent.layer.close(index);
			return false;
		});
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
    
    
});