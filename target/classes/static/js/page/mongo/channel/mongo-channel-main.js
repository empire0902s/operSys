layui.use(['table', 'laytpl', 'form', 'common','laydate','element'], function() {
	var form = layui.form, laytpl = layui.laytpl, laydate=layui.laydate,
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common,
    element =layui.element;

    form.render();

    var Ohref=window.location.href;
    var arrhref=Ohref.split("?id=");
    var mid=arrhref[1];

	// 初始化数据表格
    var dataTable = common.renderTable({
	    url: common.getUrl(rootPath + '/mongo/channel/listModule?id='+mid)
	    ,where:  $(".data-list-form").serializeJSON()
	    ,width:160
	    ,cols: [[
	    	{field:'modelName', title: '栏目模块名称', event: 'click'}
	    ]]
    });

	//table行监听事件
	 table.on('tool(data-list)', function(obj){
	    var data = obj.data;
	    if(obj.event === 'click'){

            document.getElementById('bannerSetting').contentWindow.document.getElementById('bannerUniqeKey').value=data.bannerUniqeKey;
            document.getElementById('bannerSetting').contentWindow.document.getElementById('reload').click();
            document.getElementById('goodsSetting').contentWindow.document.getElementById('goodUniqeKey').value=data.goodUniqeKey;
            document.getElementById('goodsSetting').contentWindow.document.getElementById('modelName').value=data.modelName;
            document.getElementById('goodsSetting').contentWindow.document.getElementById('modelId').value=data.id;
            document.getElementById('goodsSetting').contentWindow.document.getElementById('reload').click();
            // $('#banner').show();
            // $('#good').show();
            // $('#banner').click();


            //
            // document.getElementById('goodsSetting').hidden;
            // document.getElementById('goodsSetting').contentWindow.document.getElementById('reload').click();
            //
            // document.getElementById('groupItemSetting').hidden;
            // document.getElementById('groupItemSetting').contentWindow.document.getElementById('reload').click();
            //
			// document.getElementById('goodsSetting').contentWindow.document.getElementById('goodUniqeKey').value=data.goodUniqeKey;
			// document.getElementById('goodsSetting').contentWindow.document.getElementById('reload').click();

			// document.getElementById('groupItemSetting').contentWindow.document.getElementById('moduleId').value=data.id;
			// document.getElementById('groupItemSetting').contentWindow.document.getElementById('reload').click();

		  }
	});

	 
	 var tabHeight = $(document).height() - 123;  
	 $(".layui-tab-content").css("height", tabHeight+"px");
	 $(".iframeCss").css("height", tabHeight+"px");
	 
	 
	 
});