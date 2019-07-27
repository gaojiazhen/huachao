layui.use(['form','layer','common'],function(){
	var common = layui.common;
	var form = layui.form,
	    layer = parent.layer === undefined ? layui.layer : top.layer,
	    $ = layui.jquery;
		//var bb = parent.codeList['MENU_TYPE'];
		//
		var codeList=parent.codeList['MENU_TYPE'];
		//下拉框赋值
		var rdata=codeList;
		var temp =getCodeHtml(rdata);
		$('#MENU_TYPE').html(temp);
		temp =getCodeHtml(rdata[0].children);
		$('#MENU_EVENT_TYPE').html(temp);
		//如果不是顶级菜单不予选择
		if($("#PARENT_ID").val()!='0'||$("#ID").val()){
			console.log($("#MENU_TYPE_FLAG").val());
			console.log('--------');
			for(var i=0;i<rdata.length;i++){
				var temp =getCodeHtml(rdata[i].children);
				console.log(rdata[i].value);
				if(rdata[i].value==$("#MENU_TYPE_FLAG").val()){
					var temp =getCodeHtml(rdata[i]);
					$('#MENU_TYPE').html("<option value='"+rdata[i].value+"'>"+rdata[i].name+"</option>");
					temp =getCodeHtml(rdata[i].children);
					$('#MENU_EVENT_TYPE').html(temp);
					break;
				}
			}
			$("#MENU_TYPE").attr('disabled','false');
		}
		//下拉框选中事件
		form.on('select(filterMenuType)', function(data){
			if(data.value){
				for (var i=0;i<rdata.length;i++) {
					if(rdata[i].value==data.value){
						temp =getCodeHtml(rdata[i].children);
						$('#MENU_EVENT_TYPE').html(temp);
						break;
					}
				}
			}else{
				$('#MENU_EVENT_TYPE').html("<option value=''>请选择</option>");
			}
			form.render('select');
		});
		form.render();
        //监听提交
		form.on('submit(formDemo)', function(data){
			common.ajax(editMenu, 'post', 'json', data.field, function (res){
				if(res==1){
					layer.msg("保存成功!");
					setTimeout(function(){
					   var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					   parent.layer.close(index); //再执行关闭  
					   layer.close(index);
					},500)
				}else{
					layer.msg("网络异常!");
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