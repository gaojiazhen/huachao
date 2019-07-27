layui.use(['form', 'layer', 'common', 'upload'],function(){
	var common = layui.common,
		form = layui.form,
	    layer = parent.layer === undefined ? layui.layer : top.layer,
	    $ = layui.jquery,
		upload = layui.upload;
	var	user = JSON.parse(sessionStorage.getItem("user"));
	//下载模板
	$("#download_template").click(function(){
	    window.location.href="离休人员登记数据导入模板.xls";
	});
	//关闭
	$("#closeWindow").click(function(){
	    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	    parent.layer.close(index); //再执行关闭  
	    layer.close(index);
	});
	//附件上传
	var headers = new Object();
	var token = sessionStorage.getItem("token");
	if (token) { // 判断是否存在token，如果存在的话，则每个http header都加上token
		headers['token'] = token;
	}
	var timestamp = Date.parse(new Date());
	headers['timestamp'] = timestamp;
	var nonce = Math.round(Math.random() * 1000) + timestamp;
	nonce = doubleEncrypt(nonce);
	headers['nonce'] = nonce;
	var sign = nonce + timestamp;
	sign = doubleEncrypt(sign);
	headers['sign'] = sign;
	upload.render({
		elem: '#upload_excel',
		url: saveRetireQuitByExcel,
		accept: 'file',
		acceptMime: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
		exts: 'xls|xlsx',
		size: 51200,	//最大50M
		auto: false,
		bindAction: "#saveUser",
		headers: headers,
		choose: function(obj){
			$("#upload_excel").attr("lay-verify","");
			//预读本地文件，如果是多文件，则会遍历。(不支持ie8/9)
			obj.preview(function(index, file, result){
				$("input[name='file']").after('<span class="layui-inline layui-upload-choose">'+file.name+'</span>');
			});
		},
		done: function(res) {
			if(null==res.errorMessageHtml || ""==res.errorMessageHtml){
				layer.msg('上传成功');
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index); //再执行关闭  
				layer.close(index);
			}else{
				$(".layui-fluid").html(res.errorMessageHtml).attr("style","color:#FF5722;");
			}
		},
		error: function() {
			return layer.msg('上传失败');
		}
	});
})