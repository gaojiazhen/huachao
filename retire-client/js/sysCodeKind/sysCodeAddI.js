layui.use(['form','layer','common'],function(){
	var common = layui.common;
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery;
	//监听提交
	form.on('submit(formDemo)', function(data){
		common.ajax(addSysCodeAddI, 'post', 'json', data.field, function (res){
			if(res==1){
				layer.msg("保存成功!");
				setTimeout(function(){
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭  
					layer.close(index);
				},500)
			}else if(res==3){
				layer.msg("编码重复!");
			}else{
				layer.msg("数据异常!");
			}
		})
		return false;
	});
})