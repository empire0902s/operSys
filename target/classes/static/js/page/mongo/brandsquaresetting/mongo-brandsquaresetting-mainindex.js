layui.use(['table', 'laytpl', 'form', 'common','laydate','element'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common,
    element =layui.element;

    form.render();
	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl(rootPath + '/mongo/brandsquaremodular/groupItemlist')
	    ,where:  $(".data-list-form").serializeJSON()
	    ,width:160
	    ,cols: [[
	    	{field:'modularName', title: '栏目模块名称', event: 'click'}
	    ]]
    });

	//table行监听事件
	 table.on('tool(data-list)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'click'){
            //需要设置商品的模块
			if (data.columnType===2){
                document.getElementById('goodsSetting').contentWindow.document.getElementById('columnId').value=data.id;
                document.getElementById('goodsSetting').contentWindow.document.getElementById('reload').click();
                $('#banner').hide();
                $('#good').click();
                $('#good').show();
            }
            //需要设置banner模块
            if (data.columnType===8){
                document.getElementById('bannerSetting').contentWindow.document.getElementById('columnId').value=data.id;
                document.getElementById('bannerSetting').contentWindow.document.getElementById('reload').click();
                $('#banner').show();
                $('#good').hide();
                $('#banner').click();
            }
            //需要设置banner与商品的模块
            if (data.columnType===7){
                document.getElementById('bannerSetting').contentWindow.document.getElementById('columnId').value=data.id;
                document.getElementById('bannerSetting').contentWindow.document.getElementById('reload').click();
                document.getElementById('goodsSetting').contentWindow.document.getElementById('columnId').value=data.id;
                document.getElementById('goodsSetting').contentWindow.document.getElementById('reload').click();
                $('#banner').show();
                $('#good').show();
                $('#banner').click();
            }
          /*  if (data.styleType===4||data.styleType===9){
                document.getElementById('goodsSetting').contentWindow.document.getElementById('moduleId').value=data.id;
                document.getElementById('goodsSetting').contentWindow.document.getElementById('reload').click();
                $('#banner').hide();
                $('#good').show();
                $('#good').click();
                $('#group').hide();
			}
			if (data.styleType===7||data.styleType===10){
                document.getElementById('groupItemSetting').contentWindow.document.getElementById('moduleId').value=data.id;
                document.getElementById('groupItemSetting').contentWindow.document.getElementById('reload').click();
                $('#banner').hide();
                $('#good').hide();
                $('#group').show();
                $('#group').click();
            }*/
		  }
	});

	 
	 var tabHeight = $(document).height() - 123;  
	 $(".layui-tab-content").css("height", tabHeight+"px");
	 $(".iframeCss").css("height", tabHeight+"px");
	 
	 
	 
});