layui.use(['form','layer','common'],function(){
	var common = layui.common;
	var form = layui.form,
	    layer = parent.layer === undefined ? layui.layer : top.layer,
	    $ = layui.jquery;
	//开关监听事件
	form.on('switch(switchTest)', function(data){
		$(data.elem).attr('type', 'hidden').val(this.checked ? 1 : 0);
	});
	var colsData = parent.getData();
	var codeAddHtml="";
	for(var i=0;i<colsData.length;i++){
		if(i>4&&(i<colsData.length-2)){
			codeAddHtml+="<div class='layui-form-item magt3'>"+
							"<label class='layui-form-label'>"+colsData[i].title+"</label>"+
							"<div class='layui-input-block'>"+
								"<input type='text' class='layui-input' lay-verify='' placeholder='请输入"+colsData[i].title+"' name='"+colsData[i].field+"' id='"+colsData[i].field+"' maxlength='80'>"+
							"</div>"+
						"</div>"
		}
	}
	$("#result").before(codeAddHtml);
	//监听提交
	form.on('submit(formDemo)', function(data){
		console.log(data.field);
		common.ajax(addSysCode, 'post', 'json', data.field, function (res){
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
	//自定义校验
	form.verify({
		checkValue: function(value, item){ //value：表单的值、item：表单的DOM对象
			if(/^[+]{0,1}(\d+)$/.test(value)||value==''){
				
			}else{
				return '请输入正整数';
			}
		},order: [
		/^[+]{0,1}(\d+)$/
		,'请输入正整数',
		]
	});
})