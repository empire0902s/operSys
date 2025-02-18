layui.use(['table', 'laytpl', 'form', 'common','laydate','element'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common,
    element =layui.element;

    form.render();
	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl(rootPath + '/mongo/homepagemodule/listModule?showState=1')
	    ,where:  $(".data-list-form").serializeJSON()
	    ,width:160
	    ,cols: [[
	    	{field:'moduleName', title: '栏目模块名称', event: 'click'}
	    ]]
    });

	//table行监听事件
	 table.on('tool(data-list)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'click'){
	    	//document.getElementById('homePageMainIframe').contentWindow.document 可以获取内嵌iframe的dom对象
			if (data.styleType===11||data.styleType===1||data.styleType===2||data.styleType===3||data.styleType===8){
                document.getElementById('bannerSetting').contentWindow.document.getElementById('moduleId').value=data.id;
                document.getElementById('bannerSetting').contentWindow.document.getElementById('reload').click();
                $('#banner').show();
                $('#banner').click();
                $('#good').hide();
                $('#group').hide();
            }
            if (data.styleType===6||data.styleType===5){
                document.getElementById('bannerSetting').contentWindow.document.getElementById('moduleId').value=data.id;
                document.getElementById('bannerSetting').contentWindow.document.getElementById('reload').click();
                document.getElementById('goodsSetting').contentWindow.document.getElementById('moduleId').value=data.id;
                document.getElementById('goodsSetting').contentWindow.document.getElementById('reload').click();
                $('#banner').show();
                $('#good').show();
                $('#banner').click();
                $('#group').hide();
            }
            if (data.styleType===4||data.styleType===9){
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
            }
            // if (data.styleType===10){
            //     document.getElementById('groupItemSetting').contentWindow.document.getElementById('moduleId').value=data.id;
            //     document.getElementById('groupItemSetting').contentWindow.document.getElementById('reload').click();
            //     $('#banner').show();
            //     $('#good').show();
            //     $('#group').show();
            // }

            // document.getElementById('goodsSetting').hidden;
            // document.getElementById('goodsSetting').contentWindow.document.getElementById('reload').click();

            // document.getElementById('groupItemSetting').hidden;
            // document.getElementById('groupItemSetting').contentWindow.document.getElementById('reload').click();

			// document.getElementById('goodsSetting').contentWindow.document.getElementById('moduleId').value=data.id;
			// document.getElementById('goodsSetting').contentWindow.document.getElementById('reload').click();
            //
			// document.getElementById('groupItemSetting').contentWindow.document.getElementById('moduleId').value=data.id;
			// document.getElementById('groupItemSetting').contentWindow.document.getElementById('reload').click();

		  }
	});

	 
	 var tabHeight = $(document).height() - 123;  
	 $(".layui-tab-content").css("height", tabHeight+"px");
	 $(".iframeCss").css("height", tabHeight+"px");
	 
	 
	 
});