//加载组件
layui.use(['form', 'layer', 'jquery', 'common'], function() {
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery;
	//登录按钮
	form.on("submit(login)", function(data) {
		$(this).text("登录中...").attr("disabled", "disabled").addClass("layui-disabled");
		var common = layui.common;
		var temp = $(this);
		//加密
		data.field.pwd = doubleEncrypt(data.field.pwd);
		common.ajax(login, 'post', 'json', data.field, function(res) {
			var rdata = res.data;
			var code = res.success;
			var message = res.message;
			if (code == false) {
				temp.text("登录").removeClass("layui-disabled").removeAttr("disabled");
				layer.open({
					type: 1,
					offset: 'atuo',
					id: 'layerDemo',
					content: '<div style="padding: 20px 100px;">' + message + '</div>',
					btn: '关闭',
					btnAlign: 'c',
					shade: 1 //不显示遮罩
						,
					yes: function() {
						layer.closeAll();
					}
				});
			} else {
				if (rdata.timeFlag) {
					sessionStorage.setItem('user', JSON.stringify(rdata));
					sessionStorage.setItem('token', JSON.stringify(rdata.token));
					window.location.href = "/page/userControl/changePwd.html?userId=" + rdata.id;
				} else {
					sessionStorage.setItem('user', JSON.stringify(rdata));
					sessionStorage.setItem('token', JSON.stringify(rdata.token));
					window.location.href = "/index.html";
				}
			}
		});
		return false;
	});
	//单击文本框
	$(".loginBody .input-item").click(function(e) {
		e.stopPropagation();
		$(this).addClass("layui-input-focus").find(".layui-input").focus();
	});
	//获得焦点
	$(".loginBody .layui-form-item .layui-input").focus(function() {
		$(this).parent().addClass("layui-input-focus");
	});
	//失去焦点
	$(".loginBody .layui-form-item .layui-input").blur(function() {
		$(this).parent().removeClass("layui-input-focus");
		if ($(this).val() != '') {
			$(this).parent().addClass("layui-input-active");
		} else {
			$(this).parent().removeClass("layui-input-active");
		}
	});
	//下载浏览器
	$("#down_chrome").click(function(e) {
		window.location.href="/js/chrome75.exe";
		return false;
	});
})
