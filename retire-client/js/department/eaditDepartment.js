layui.use(['form','layer','common'],function(){
	var common = layui.common;
	var form = layui.form,
	layer = parent.layer === undefined ? layui.layer : top.layer,
	$ = layui.jquery;
	
	var id = $("#PARENT_ID").val();
	var updaCode = $("#ID").val();
	//监听提交
	form.on('submit(formDemo)', function(data){
		if(updaCode==data.field.ID){
			data.field.updateFlag="1";
		}
		common.ajax(editDepartment, 'post', 'json', data.field, function (res){
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
				layer.msg("网络异常!");
			}
		})
	return false;
	});
	//自定义校验
	form.verify({
		codeLength: function(value, item){ //value：表单的值、item：表单的DOM对象
			if(id==0){
				if(value.length!=5){
					return '不符合编码规则';
				}
			}else{
				if(value.length!=id.length+4){
					return '不符合编码规则';
				}
			}
		},order: [
		/^[+]{0,1}(\d+)$/
		,'请输入正整数'
	  ] 
	});
})