layui.use(['table', 'laytpl', 'form', 'common','upload','element'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, upload = layui.upload,
	table = layui.table,element = layui.element,common = layui.common;

	//$("select[url]").loadSelect();
	 $("#columnId").setRoleList();
   // form.render();
		form.verify({
			Imgrequired: function(value, item){ 
				 if(value==null||value=="")
				 {
					/* layer.alert("请完善当前信息后再执行该操作", {icon: 2});*/
					 return "图片没有上传或者是当前信息仍未完善哦~";
				    }
				  }
		});
    var bFlag=0;
    var fileCount=0;
    var msgDivLength=1;
    
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
    var array=0;
    $('.btn-close').click(function(){
    	layer.closeAll();
		//关闭当前内嵌页面窗口
		var index = parent.layer.getFrameIndex(window.name);  
		parent.layer.close(index);
	});
    var i=0;
    
   $(document).on('click','#deleteBtn1',function(){ $("div#msgDiv1").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn2',function(){ $("div#msgDiv2").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn3',function(){ $("div#msgDiv3").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn4',function(){ $("div#msgDiv4").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn5',function(){ $("div#msgDiv5").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn6',function(){ $("div#msgDiv6").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn7',function(){ $("div#msgDiv7").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn8',function(){ $("div#msgDiv8").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn9',function(){ $("div#msgDiv9").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn10',function(){ $("div#msgDiv10").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn11',function(){ $("div#msgDiv11").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn12',function(){ $("div#msgDiv12").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn13',function(){ $("div#msgDiv13").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn14',function(){ $("div#msgDiv14").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn15',function(){ $("div#msgDiv15").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn16',function(){ $("div#msgDiv16").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn17',function(){ $("div#msgDiv17").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn18',function(){ $("div#msgDiv18").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn19',function(){ $("div#msgDiv19").remove();msgDivLength=msgDivLength-1;});
   $(document).on('click','#deleteBtn20',function(){ $("div#msgDiv20").remove();msgDivLength=msgDivLength-1;});

    $("#addBtn").click(function(){
    	if(fileCount>=20){
    		layer.alert("重复操作信息卡添加删除操作过多！！", {icon: 2});
    		return false;	
    	}
    	if(msgDivLength>=8){
    		layer.alert("一次最多添加8个品牌推荐信息", {icon: 2});
    		return false;
    	}
    	
    	var imgLength=0;
		var arr=new Array();
		for (var i = 0; i <=fileCount; i++) {
			var tmpData=$("input[ name='brandLogoImg["+i+"]'] ").val();
			arr.push(tmpData);
		}
		var nary=arr.sort();
		for(var i=0;i<arr.length;i++){
			if(nary[i]!=null&&nary[i]!=""){
				imgLength=imgLength+1;
			}
		}
		if(imgLength!=msgDivLength){
			layer.alert("请完善当前信息后(图片信息必不可缺)再执行该操作", {icon: 2});
    		return false;
		}
     /* 	document.getElementById("addBtnTmp").click();
    	form.on('submit(addBtnTmp)', function(data){
    		return false; 
    	});*/
 /*   	var tmpData=$("input[ name='brandLogoImg["+fileCount+"]'] ").val();
    	var tmpbrandNameData=$("input[ name='brandName["+fileCount+"]'] ").val();
    	var tmpjumpTargetData=$("input[ name='jumpTarget["+fileCount+"]'] ").val();
    	var tmpsortNumData=$("input[ name='sortNum["+fileCount+"]'] ").val();
    	var tmpcolumnIdData;
    	if(fileCount==0){
    		tmpcolumnIdData=$("#columnId").val();
    	}else{
    		tmpcolumnIdData=$("#columnId"+fileCount).val();
    	}
    	if(tmpData==""||tmpData==null
    		||tmpbrandNameData==""||tmpbrandNameData==null
    		||tmpjumpTargetData==""||tmpjumpTargetData==null
    		||tmpcolumnIdData==""||tmpcolumnIdData==null
    		||tmpsortNumData==""||tmpsortNumData==null){
    		layer.alert("请完善当前信息后再执行该操作", {icon: 2});
    		return false;
    	}*/
    	fileCount=fileCount+1;
    	var file="file"+fileCount;
    	var imgPath="imgPath"+fileCount;
    	var checkBigImg="checkBigImg"+fileCount;
    	var imgDisplay="imgDisplay"+fileCount;
    var formHtmlTmp="<div class='jumbotron' id='msgDiv"+fileCount+"'>" +
			"<div style='margin-left: 550px;'><a href='#' id='deleteBtn"+fileCount+"'><i class='layui-icon' style='color: red;'>&#x1006;</i></a></div>"+
    		"<div style='float:left;margin-left: 30px;margin-top: 15px;'><div class='layui-form-item'>" +
    		"<div class='layui-inline'><div class='layui-input-inline'><a target='_blank' id='checkBigImg' href=''>" +
    		"<img type='' class='layui-upload-img' id='"+imgDisplay+"' style='display: none;width: 210px;height: 120px;margin-top: -10px;'></a>" +
    		"<p id='imageText'></p> " +
    		"<input type='hidden' lay-verify='Imgrequired' style='margin-top: 5px' class='layui-input' name='brandLogoImg["+fileCount+"]' id='"+imgPath+"' readonly='readonly'>" +
    		"<button type='button' class='layui-btn layui-btn-normal' style='margin-top: 5px;'id='"+file+"'>上传图片(宽：120*高：120)</button>" +
    		"</div></div></div></div>" +
    		"<div style='margin-left: 100px;float:left;'>" +
    		"<div class='layui-form-item'>" +
    		"<div class='layui-inline'><div class='layui-input-inline'>" +
    		"<input type='text' name='brandName["+fileCount+"]' maxlength='20'lay-verify='required' placeholder='品牌名称' autocomplete='off' class='layui-input'>" +
    		"</div></div></div>" +
    		"<div class='layui-form-item'>" +
    		"<div class='layui-inline'><div class='layui-input-inline'>" +
    		"<input type='text' name='jumpTarget["+fileCount+"]'  lay-verify='required' placeholder='跳转地址' autocomplete='off' class='layui-input'>" +
    		"</div></div></div>" +
    		"<div class='layui-form-item'>" +
    		"<div class='layui-inline'>" +
    		"<div class='layui-input-inline'>" +
    		"<select lay-verify='required' id=columnId"+fileCount+"  name='columnId["+fileCount+"]'>" +
    		"</select>"+
    		"</div></div></div>"+
    		"<div class='layui-form-item'>" +
    		"<div class='layui-inline'><div class='layui-input-inline'>" +
    		"<input type='text' name='sortNum["+fileCount+"]' lay-verify='required|number|space|positiveInteger|normal'  maxlength='20' autocomplete='off' class='layui-input'placeholder='排序编号'>" +
    		"</div></div></div></div></div>";
    //" url='/operSys/mongo/brandrecommend/selectValue'>" +
    
    
    
    
    $("#addDiv").append(formHtmlTmp);
    msgDivLength=msgDivLength+1;
    var bFlag=0;
	//图片上传
	  var uploadInst = upload.render({
	    elem:'#file'+fileCount
	    ,url: rootPath+'/mongo/brandrecommend/upload/'
	    ,size:"1024"//限制上传图片大小，单位是kb
	    ,before: function(obj){
	      //预读本地文件示例，不支持ie8
	      obj.preview(function(index, file, result){
	    	  $('#imgDisplay'+fileCount).show();
	        $('#imgDisplay'+fileCount).attr('src', result); //图片链接（base64）
	      });
	      
	      bFlag=1;
	    }
	    ,done: function(res){
	    	 //上传成功
	    	if(res.code ==0){
	    		layer.msg('上传成功');
		    	var imgPathVal=document.getElementById('imgPath'+fileCount).value;
		    	document.getElementById('imgPath'+fileCount).value=res.data.src;
		    	$("#checkBigImg"+fileCount).attr("href",document.getElementById('imgPath'+fileCount).value);
		    	bFlag=0;
	    	}
	      //如果上传失败
	      if(res.code > 0){
	    	  $('#imgDisplay'+fileCount).hide();
	    	  $("#imgPath"+fileCount).val("");
	        return layer.msg('上传失败');
	      }
	     
	    }
	  });
	  
	  //form.render();
	  var columnIdTmp="columnId"+fileCount;
	 $("#"+columnIdTmp).setRoleList();
    });
    
    //添加品牌推荐
	form.on('submit(submit)', function(data) {
		if(bFlag==1){
			layer.alert('请等待图片上传完成', {icon: 2}); 
			return false;
		}
		
/*		if(fileCount>0){
			var arr=new Array();
			for (var i = 0; i <=fileCount; i++) {
				arr.push(data.field['sortNum['+i+']']);
			}
		
			var nary=arr.sort();
			for(var i=0;i<arr.length;i++){

			if (nary[i]==nary[i+1]){
				layer.alert("存在相同的排序编号："+nary[i]);
				return false;
			}
			}
		}*/
		
	/*	if((fileCount+1)<4||(fileCount+1)>8){
			layer.alert("至少要上传4个品牌才能保存,最多为8个", {icon: 2});
			return false;
		}*/
/*		console.info(data.field['brandLogoImg[0]']);
		console.info(data.field['sortNum[0]']);*/
		common.wait("提交中...");
		common.post(rootPath + '/mongo/brandrecommend/save', data.field, function(msg) {
			common.hide();
			if (msg.ok) {
				// 提交成功刷新数据表格，关闭弹出层
				layer.alert("操作成功。",function(){
					layer.closeAll();
					//关闭当前内嵌页面窗口
					var index = parent.layer.getFrameIndex(window.name);  
					parent.layer.close(index);
					window.parent.document.getElementById("brandNameTmp").value="";
					 window.parent.document.getElementById("reload").click();
				});
			}else{
				layer.alert(msg.msg);
			}
		});
		return false;
	});
});