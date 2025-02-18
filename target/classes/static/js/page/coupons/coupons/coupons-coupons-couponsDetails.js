layui
		.use(
				[ 'table', 'laytpl', 'form', 'common' ],
				function() {
					var form = layui.form, laytpl = layui.laytpl, laytpl = layui.laytpl, table = layui.table, common = layui.common;
					form.render();
					var couponsDetailsJson =JSON.parse(couponsDetails);
					if (couponsDetailsJson.contentImg != null) {
						$("#contentImg").show();
						$("#content").show();
						$("#contentImg").attr('src',
								couponsDetailsJson.contentImg);
						$("#content").attr('href',
								couponsDetailsJson.contentImg);
					} else {
						$("#contentImg").hide();
						$("#content").attr("href","javascript:void(0)");
						$("#content").text("无");
					}
					if (couponsDetailsJson.contentDisabledImg != null) {
						$("#contentDisabledImg").attr("src",
								couponsDetailsJson.contentDisabledImg);
						$("#contentDisabled").attr("href",
								couponsDetailsJson.contentDisabledImg);
						$("#contentDisabledImg").show();
						$("#contentDisabled").show();
					} else {
						$("#contentDisabledImg").hide();
						$("#contentDisabled").attr("href","javascript:void(0)");
						$("#contentDisabled").text("无");
					}
				});
