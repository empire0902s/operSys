layui.use(['table', 'form', 'element', 'treetable'], function () {
    var $ = layui.jquery;
    var table = layui.table;
    var form = layui.form;
    var element = layui.element;
    var treetable = layui.treetable;

    // 渲染表格
    var renderTable1 = function () {
        layer.load(2);
        $.getJSON('data', function (res) {
            treetable.render({
                treeColIndex: 1,
                treeSpid: -1,
                treeIdName: 'id',
                treePidName: 'pid',
                treeDefaultClose: true,
                treeLinkage: false,
                elem: '#table1',
                data: res.data,
                page: false,
                cols: [[
                    {type: 'numbers'},
                    {field: 'id', title: 'id'},
                    {field: 'name', title: 'name'},
                    {field: 'sex', title: 'sex'},
                    {field: 'pid', title: 'pid'},
                    {templet: '#oper-col', title: 'oper'}
                ]],
                done: function () {
                    layer.closeAll('loading');
                }
            });
        });
    };

    renderTable1();

    $('#btn-expand').click(function () {
        treetable.expandAll('#table1');
    });

    $('#btn-fold').click(function () {
        treetable.foldAll('#table1');
    });

    $('#btn-refresh').click(function () {
        renderTable();
    });
});