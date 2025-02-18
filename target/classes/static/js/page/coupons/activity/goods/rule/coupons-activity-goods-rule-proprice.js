layui.use(['table', 'laytpl', 'laydate', 'form', 'common'], function() {
	var form = layui.form, laytpl = layui.laytpl, 
	laytpl = layui.laytpl, laydate = layui.laydate, 
	table = layui.table, common = layui.common;
	
	
	$(".disabledTable").find("input,textarea,select").not("").attr("readonly","readonly")


    // 初始化编辑表单
	form.render();
    //
    // // 初始化数据表格
    var options = $.extend({
	    	elem: '.data-list',
		    form: '.data-list-form',
		    height: 200,
		    where:  $(".data-list-form").serializeJSON(),
		    size: 'sm',
		    cols: [[
		      {field:'id', title: 'ID', width:50, sort: true },
		      {field:'product_Sku', title: 'SKU', width: 200 },
		      // {field:'standardName1', title: 'standardName1', width: 100,edit:"text"},
		      // {field:'standardName2', title: 'standardName2', width: 100,edit:"text"},

		      {field:'activityStartTime', title: '活动开始时间', width: 100,edit:"text"},
		      {field:'activityFinishTime', title: '活动结束时间', width: 160,edit:"text"},
		      {field:'seckillPrice', title: '秒杀价格(元)', width: 160,edit:"text"},
		      {field:'platformPrice', title: '平台供货单价(元)', width: 160,edit:"text"},
		      {field:'price_Distribution', title: '分销价(元)', width: 160,edit:"text"},
		      {field:'seckillcommission', title: '活动分销商佣金', width: 160,edit:"text"},
		      {field:'seckillDlmmission', title: '活动代理商佣金', width: 160,edit:"text"},
		      {field:'activityQuantity', title: '活动数量', width: 160,edit:"text"},
		      {field:'dataSyncState', title: '数据同步状态', width: 160,edit:"text"},
		      {field:'upDownState', title: '上下架状态', width: 160,edit:"text"},
		      {fixed: 'right',title: '操作', width:250, align:'center', toolbar:'#editBar'}
		    ]],
		    page: false
    },loadGridDone);

    var ruleid=0;  var ruleType=0;
    var Ohref=window.location.href;
    if (Ohref.indexOf("?ruleId=") !== -1) {
        var　arrhref=Ohref.split("?ruleId=");
        //截取上个页面传来的规则id和规则类型
        ruleid=arrhref[1].split("&ruleType=")[0];
        ruleType=arrhref[1].split("&ruleType=")[1];
	}

    var dataTable = layui.table.render(options);

        $(options.form).on('click', 'a[data-type="btn_return"]', function(){
        window.history.go(-1);
    })
    $(options.form).on('click', 'a[data-type="reload"]', function(){
    	common.wait('加载数据');
		common.reloadTable(dataTable,
                    $.extend(
					{
						url: common.getUrl('/coupons/activity/goods/rule/queryGoods?ruleId='+ruleid+"&ruleType="+ruleType),
					}
					,loadGridDone
				)
		);
	});

	
	var loadGridDone = {
			done: function(res, curr, count){
		        //如果是异步请求数据方式，res即为你接口返回的信息。
		        //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
		        console.log(res);
		       if(res.code == "0"){
		    	   var product = JSON.parse(res.msg);
		           console.log(product);
		           //自动绑定 属性值
		           $.each(product, function(k, v){
                       if(v instanceof Array){
                       }else{
                           if ("."+k===".is_sale") {
                               var is_sale=v==="0"? "待审核":v==="1"?  "上架":v==="2"? "下架":"";
                               var spanDiv = $("."+k);
                               if(spanDiv){
                                   spanDiv.text(is_sale);
                               }
                           }else if ("."+k===".marketAudit") {
                               var marketAudit=v==="0"? "待审核":v==="1"? "审核通过":v==="2"? "不通过":v==="3"? "审核异常":v==="4"? "下架":"";
                               var spanDiv = $("."+k);
                               if(spanDiv){
                                   spanDiv.text(marketAudit);
                               }
                           }else if ("."+k===".activityState") {
                               var activityState=v==="0"?  "未设置":v==="1"? "已设置":v==="2"? "已上架":v==="3"? "已下架":""
                               var spanDiv = $("."+k);
                               if(spanDiv){
                                   spanDiv.text(activityState);
                               }
                           }else {
                               var spanDiv = $("."+k);
                               if(spanDiv){
                                   spanDiv.text(v);
                               }
                           }
                       }
                   });
		           
		           //切换数据列头
		           $("th .laytable-cell-1-standardName1").text("改变列头1");
		           $("th .laytable-cell-1-standardName2").text("改变列头2");
		       }
		        
		    	common.hide();
		      }
			}


			$(".submit").click(function(){
                var postdata=  JSON.stringify(table.cache[1]);
                if (postdata===undefined){
                    postdata="[]";
				}
                common.wait("提交中...");
                $.ajax({
                    type: 'POST',
                    url:  rootPath + '/coupons/activity/goods/rule/saveProductPrice',
                    data: postdata,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (msg) {
						if (msg.ok){
                            common.hide();
                            layer.alert("保存成功。",function(){
                                            layer.closeAll();
                                        });
						}
                    },

                });

                return false;

            });

});