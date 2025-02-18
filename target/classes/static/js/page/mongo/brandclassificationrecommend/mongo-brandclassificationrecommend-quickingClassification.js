layui.use(['table', 'laytpl', 'form', 'common','upload'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, upload = layui.upload,
	table = layui.table, common = layui.common;
	$("select[url]").loadSelect();
	var imageUrlJson=JSON.parse(imageUrl);
    // 初始化编辑表单
	form.render();
	$("select[url]").loadSelect();
	form.verify({
		pwd: [/.{6,}/, '请输入6-32位密码']
	});
	
	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl('/mongo/brandclassificationrecommend/quickingClassificationList?columnId='+$("#columnId").val()),
	    elem: '#dataList',
	    id: 'dataList',
	    cols: [[
	      {type:'checkbox'},
	      {field:'id', title: 'ID', width:100, sort: true},
	      {field:'brandName', title: '品牌名称', width: 250},
	      {field:'sortNum', title: '排序', width: 120,edit: 'text'},
	      {field:'modularName', title: '分类名称', width: 180},
	      {field:'brandLogoImg', title: '图片', width: 120,templet:function(val){
	    	  return "<img style='width: 50px; height: 30px;'" +
	    	  				"src='"+val.brandLogoImg+"'>";
	      }},
	      {fixed: 'right',title: '操作',width: 220, align:'center', toolbar:'#editBar'}
	    ]],
    });
    
    
    //复选框事件监听
	var recommendArray;
	table.on('checkbox(data-list)', function(obj){
		var checkStatus = table.checkStatus('dataList')
	      ,data = checkStatus.data;
		recommendArray=JSON.stringify(data);//将数据转为json字符串
		//recommendArray=JSON.parse(recommendArray);//返回的是一个json数组
		console.info(recommendArrayTmp)
		});
	
	$('#binding').click(function(){  
		if(recommendArray==null||recommendArray.size==0){
			layer.alert("请选择要绑定的品牌",{icon:2});
			return;
		}
		//重新获取所选中的数据(避免某条数据被修改后，导致当前选中数据与与修改前选中数据不一致)
		var checkStatus = table.checkStatus('dataList')
	      ,data = checkStatus.data;
		recommendArray=JSON.stringify(data);//将数据转为json字符串
		//提交数据
        $.ajax({
            type: 'GET',
            url:  rootPath + '/mongo/brandclassificationrecommend/recommendListBindIngToModular?columnId='+$("#columnId").val(),
            data:{recommendArray:recommendArray},
            dataType: "json",
            success: function (msg) {
            	if(!msg.ok){
            		layer.alert(msg.msg);
            	}
            	var data=msg.value;
            	//手动绑定数据到页面
            	editForm.loadData(data);
            	form.render();
            },
        });
	});
	
	
	//监听单元格编辑
	  table.on('edit(data-list)', function(obj){
	    var value = obj.value //得到修改后的值
	    ,data = obj.data //得到所在行所有键值
	    ,field = obj.field; //得到字段
	    var reg = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/;
	    var flag= reg.test(value)
	    if(!flag){
	    	layer.alert("111111");
	    	return false;
	    }
	  });
	
	
    
    var editForm = $('.edit-form');
	
    
    //-----------------------点击事件-----------------------------//
    
    
    //页面关闭
	$('.btn-close').click(function(){  
		layer.closeAll();
	});
	
	var editForm = $('.edit-form'), roleForm = $('.role-form');
    //监听工具条
    table.on('tool(data-list)', function(obj){
    	var data = obj.data;
    	switch (obj.event) {
		case 'look':
			layer.open({
				title:'详细',
				type: 2,
				area: ['600px','350px'],
				shadeClose: true,
				content:rootPath + '/mongo/brandclassificationrecommend/brandrecommendDetails?id='+data.id
			});
			break;
		case 'jump':
			window.open(data.jumpTarget);
		break;
		default:
			break;
		}
   });
});