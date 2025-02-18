layui.use(['table', 'laytpl', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, 
	table = layui.table, common = layui.common;
	
    // form.render();

    // 品类明细表格
    var categorydataTable = common.renderTable({
        url: common.getUrl ('/coupons/category/Details'),
        height: 400,
        where:  $("#childrenform").serializeJSON(),
        cols: [[
            {field:'id', title: 'ID', width:50, sort: true},
            {field:'name', title: '名称', width: 120},
            {field:'parentName', title: '上级菜单',width:120},
            {fixed: 'right',title: '操作', width:100, align:'center', toolbar:'#editBar'}

        ]],
    });

    $('#query').click(function () {
        categorydataTable = common.renderTable({
            url: common.getUrl ('/coupons/category/Details'),
            height: 400,
            where:  $("#childrenform").serializeJSON(),
            cols: [[
                {field:'id', title: 'ID', width:50, sort: true},
                {field:'name', title: '名称', width: 120},
                {field:'parentName', title: '上级菜单',width:120},
                {fixed: 'right',title: '操作', width:100, align:'center', toolbar:'#editBar'}

            ]]
        });

    });

    //监听工具条

    table.on('tool(data-list)', function(obj){

        var data = obj.data;
        switch (obj.event) {
            case 'sure':
                var index = parent.layer.getFrameIndex(window.name);
                var content = $("#_operSys_coupons_category_index",top.document)[0].contentWindow.document;
                var $table = $("#if1",content).find("#added");
                var inputcategoryRange= $("#if1",content).find("#categoryRange");
                var inputcategoryRangeDetail= $("#if1",content).find("#categoryRangeDetail");
                var table=$table[0];
                var $select_id= $("#if1",content).find("#select_id option:selected");

                var add=true;
                if (inputcategoryRangeDetail!==null &&inputcategoryRangeDetail!=="") {
                    var strs=inputcategoryRangeDetail.val().split(",");
                    if (strs.length>0){
                        for (var i=0;i<strs.length ;i++ )
                        {
                          if (data.id+""===strs[i]){
                              layer.alert("不能重复选");
                              add=false;
                              break;
                          }
                        }
                    }
                }
                //已选就不加行了
                if (add===false){
                    break;
                }

                var strhtml="";
                strhtml +="<tr>"+
                    "<td ><span style='display:none' class='idCss'>" +$select_id.val()+"</span>"+ $select_id.text() + "</td>" +
                    "<td ><span style='display:none' class='idCss'>" +data.id+"</span>"+ data.name + "</td>"  +
                    "<td><input type='button' value='删除' id='deleteCategory' /></td>"+
                "</tr>";
                $table.append(strhtml);

                inputcategoryRange.val($select_id.val());
                var detail="";
                var index=0;
                $("#if1",content).find("#added").find("tr").each(function(){
                    if (index !==0) {
                        var tdArr = $(this).children();
                        var detailspan = tdArr.eq(1).find('.idCss').text();
                        detail += detailspan + ",";
                    }
                    index+=1;
                });
                inputcategoryRangeDetail.val(detail);

                break;
            case 'disabled':
                layer.confirm("确定是否禁用["+data.name+"]?", function(confirmIndex){
                    common.wait("提交中...");
                    common.post( rootPath + '/coupons/category/disabled', {id:data.id}, function(msg) {
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
                    layer.close(confirmIndex);
                });
                break;

            default:
                break;
        }
    });

});