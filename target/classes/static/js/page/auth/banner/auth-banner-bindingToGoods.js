layui.use(['table', 'laytpl', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
	
	 $("select[url]").loadSelect();
    // 初始化编辑表单
	form.render();
	form.on('submit(submit)', function(data) {
		common.wait("提交中...");
		common.post( rootPath + '/banner/bannerGoodsDetail/save', data.field, function(msg) {
			common.hide();
			if (msg.ok) {
				// 提交成功刷新数据表格，关闭弹出层
				layer.alert("操作成功。",function(){
					parent.location.reload();
					layer.closeAll();
				});
			}else{
				layer.alert(msg.msg);
			}
		});
		return false;
	});

	$('.btn-close').click(function(){
		layer.closeAll();
		//关闭当前内嵌页面窗口
		var index = parent.layer.getFrameIndex(window.name);  
		parent.layer.close(index);
	});
	
	var editForm=$("#editForm");
    $(".search").click(function(){
        var code=$("#code").val();
        if(code==""){
        	return;
        }
        $.ajax({
            type: 'GET',
            url:  rootPath + '/banner/bannerGoodsDetail/findByCode?productCode='+code,
            dataType: "json",
            success: function (msg) {
            	if(!msg.ok){
            		layer.alert(msg.msg);
            	}
            	var data=msg.value;
            	//手动绑定数据到页面
            	$(".product_Code").text(data.productCode);
            	$(".product_Name").text(data.productName);
            	$(".company_name").text(data.companyName);
            	editForm.loadData(data);
            	form.render();
            },
        });
    });
    
    var ID=id;
    var IsDisabled=isDisabled
    var SortNo=sortNo;
    var operateTypeTmp=operateType;
    var productJson=JSON.parse(product);
    var tmp=JSON.stringify(productJson);
	var temp=$.parseJSON(tmp);
    if(operateTypeTmp==2){
    	$(".product_Code").text(temp.productCode);
    	$(".product_Name").text(temp.productName);
    	$(".company_name").text(temp.companyName);
  		temp.sortNo=SortNo;
  		console.info(IsDisabled)
  		if(IsDisabled=="true"){
  			temp.isDisabled=true;
  		}	
  		if(IsDisabled=="false"){
  			temp.isDisabled=false;
  		}
  		$("input[name='productCode']").attr("disabled","disabled");
       	editForm.loadData(temp);
    	form.render();
    }
});