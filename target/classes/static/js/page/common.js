layui.define(['layer', 'laytpl', 'form'], function(exports) {
	"use strict";
	var layer = layui.layer, laytpl = layui.laytpl, form = layui.form;
	// 添加jquery插件方法
	$.fn.extend({
		loadSelect: function(reload, val, params){
			var urls = [], inputs = [], _doc = $(document);
			// 处理异步重复加载
			$(this).each(function(){
				var _select = $(this), url = _select.attr('url'), depend = _select.attr('depend-lay-filter');
				if(!url){
					return true;
				}
				// 下拉联动效果
				if(!reload && depend){
		            form.on('select('+depend+')', function (data) {
		                $('select[depend-lay-filter="'+depend+'"]').loadSelect(true, null, {depend: data.value});
		            });
					return true;
				}
				var _index = urls.indexOf(url);
				if(_index == -1){
					urls.push(url);
					inputs.push([_select]);
				}else{
					inputs[_index].push(_select);
				}
			});
			$(urls).each(function(i, url){
				var cache = _doc.data(url);
				var loadSelectData = function(data){
					$(inputs[i]).each(function(){
						var _select = $(this), initValue = _select.attr('value');
						laytpl('{{# layui.each(d, function(i,item){ }} <option value="{{item.value}}">{{item.name}}</option> {{# }); }}').render(data, function(html){
							_select.empty().append('<option value="">请选择</option>').append(html);
							if(val){
								_select.val(val);
							}else if(initValue){
								_select.val(initValue);
							}
							// 编辑时联动选中
							var depend = _select.attr('lay-filter');
							if(initValue){
								$('select[depend-lay-filter="'+depend+'"]').loadSelect(true, null, {depend: _select.val()});
							}
							
							layui.form.render('select');
							_doc.data(url, data);
						});
					});
				}
				if(!cache || reload){
					common.get(url, params ? params : {}, loadSelectData);
				}else{
					loadSelectData(cache);
				}
			});
		},
		loadData: function(json,isId){
			var form = $(this);
			$.each(json, function(k, v){
				if(v instanceof Array){
					$(v).each(function(k2, v2){
						$.each(v2, function(k3, v3){
							var ele ;
							if(isId){
								ele = $(':input[id="'+k+'['+k2+'].'+k3+'"]', form);
							}else{
								ele = $(':input[name="'+k+'['+k2+'].'+k3+'"]', form);
							}
							ele.val(v3);
						});
					});
				}else{
					var ele ;
					if(isId){
						ele = $(':input[id="'+k+'"]', form)
					}else{
						ele = $(':input[name="'+k+'"]', form)
					}
					
					if(ele.hasClass("datetimeCss") && v){
						v = v.replace("T"," ");
					}
					if(ele.attr("replaceDateT")){
						v = v.replace("T"," ");
					}
					if(ele.is(':radio')){
						// 单选
						$.grep(ele, function(e){
							return e.value == v;
						})[0].checked = true;
					}else{
						ele.val(v);
					}
				}
			});
		},
	});
	$.fn.extend({
	    //获取roleName并动态生产select下拉选内容
	    setRoleList:function(type){
	        var target = this;
	        $.ajax({
	            type:"GET",
	            url:rootPath+"/mongo/brandrecommend/selectValue",
	            success:function(rtn){//rtn为后端传过来的List集合
	                target.find('option').remove().end();
	                target.append("<option  value=''>请选择品牌模块</option>");  
	                if (rtn.length == 0) {  
	                       target.append($("<option></option>").attr("value", "").text("没有数据"));  
	                }else{
	                       $.each(rtn, function(key, role) {//遍历  
	                       if (type != null) {  
	                           //將value中的属性值赋給option的value和文本內容
	                           target.append($("<option></option>")
	                               .attr("value", role.value).text(role.name));   
	                        } else {  
	                            target.append($("<option ></option>")
	                                .attr("value",role.value).text(role.name));
	                        } 
	                   }); 
	                       form.render();
	                }
	            }

	        });
	    }
	});
	var common = {
		getUrl: function(url){
			if(rootPath && url.startsWith(rootPath)){
				return url;
			}
			return rootPath + url;
		},
		post: function(url, data, callback){
			common.wait('提交中');
			$.ajax({
				url: common.getUrl(url),
				dataType: 'json',
				data: data,
				type: 'post',
				success: function(msg){
					common.hide();
					callback(msg);
				},
				error: function(xhr, textStatus, errorThrown){
					common.hide();
					callback({ok: false, msg: '提交出错：'+textStatus});
				}
			});
		},
		get: function(url, data, callback){
			common.wait('加载中');
			$.ajax({
				url: common.getUrl(url),
				dataType: 'json',
				data: data,
				success: function(msg){
					common.hide();
					callback(msg);
				},
				error: function(xhr, textStatus, errorThrown){
					common.hide();
					callback({ok: false, msg: '加载出错：'+textStatus});
				}
			});
		},
		renderTable: function(options){
			common.wait('初始化表格');
			var docHeight;
			
			var tableHeigth;
			var fromHeight = $('form').height() + 60;
			var limit = 15;
			if( document.readyState == "complete" ){
				docHeight=$(document).height(); 
				console.log(docHeight);
				tableHeigth = docHeight - fromHeight;
			}else{
				if(fromHeight < 0){
					fromHeight = 0;
				}
				tableHeigth = 'full-'+fromHeight;
				console.log("height:250");
			}
			// 当前文档高度小于700时，每页显示10条
			if(tableHeigth < 660 || fromHeight > 100){
				limit = 10;
			}
			options = $.extend({
			    elem: '.data-list',
			    form: '.data-list-form',
			    height: tableHeigth,
			    done: function(res, curr, count){
			    	// 数据加载完成后
			    	common.hide();
			    },
			    page: {
			    	limit: limit,
			    	limits: [10, 15, 30, 100]
			    }
		    }, options);
			var dataTable = layui.table.render(options);
			$(options.form).on('click', 'a[data-type="reload"]', function(){
				common.reloadTable(dataTable, {
					page: {
						curr: 1
					}
				});
			}).on('submit', function(){
				common.reloadTable(dataTable, {
					page: {
						curr: 1
					}
				});
				return false;
			});
		    return dataTable;
		},
		reloadTable: function(dataTable, options){
			common.wait('数据请求中');
			options = $.extend(true, {
				method: 'post',
				done: function(){
					// 数据加载完成后
					common.hide();
				},
				where: $(dataTable.config.form).serializeJSON()
			}, options);
			dataTable.reload(options);
		},
		wait: function(content) {
			var myLayer = window.parent.layer;
			if(!myLayer) {
				myLayer = layer;
			}
			if(!content) {
				content = "请稍后....";
			}
			myLayer.msg(content,{icon: 16,shade:0.5,time: 100000});
		},
		hide: function() {
			var myLayer = window.parent.layer;
			if(!myLayer) {
				myLayer = layer;
			}
			myLayer.close(myLayer.index);
		},
		/**
		 * 抛出一个异常错误信息
		 * @param {String} msg
		 */
		throwError: function(msg) {
			throw new Error(msg);
			return;
		},
		/**
		 * 弹出一个错误提示
		 * @param {String} msg
		 */
		msgError: function(msg) {
			layer.msg(msg, {
				icon: 5
			});
			return;
		}
	};
	
	exports('common', common);
	function IsURL(str_url){
		 var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
		 + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@ 
		  + "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184 
		  + "|" // 允许IP和DOMAIN（域名）
		  + "([0-9a-z_!~*'()-]+\.)*" // 域名- www. 
		  + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名 
		  + "[a-z]{2,6})" // first level domain- .com or .museum 
		  + "(:[0-9]{1,4})?" // 端口- :80 
		  + "((/?)|" // a slash isn't required if there is no file name 
		  + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$"; 
		  var re=new RegExp(strRegex); 
		 //re.test()
		  if (re.test(str_url)){
		  return (true); 
		  }else{ 
		  return (false); 
		  }
		 }
	//自定义验证
	form.verify({
		normal: function(value, item){ //value：表单的值、item：表单的DOM对象
		    if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
		      return '不能有特殊字符';
		    }
		    if(/(^\_)|(\__)|(\_+$)/.test(value)){
		      return '首尾不能出现下划线\'_\'';
		    }
		  },
		pwd: [/.{6,}/, '请输入6-32位密码'],
		space:[/^[^ ]+$/,'不能有空格'],
		PositiveNumber :[/^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, '必须输入正数' ],
		pInteger : [ /^(0|[1-9]\d{0,9})$/, '不能是小数,负数和不能是以0开头的数据(不包括0)' ],
		positiveInteger:[/^\+?[1-9]\d*$/,'必须输入正整数'],
		websiteCheck: function(value, item){ 
			var flag=new RegExp("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]").test(value);
			 if(!IsURL(value))
			 {
				 return '输入有误，该网址不是以http://https://ftp://开头,或者不是网址!' ;
			    }
			 if(!flag)
			 {
			      return '输入有误，该网址不是以http://https://ftp://开头,或者不是网址!' ;
			    }
			  }
	});
});