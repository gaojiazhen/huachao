layui.use(['form','layer','common'],function(){
	var common = layui.common;
	var form = layui.form,
	    layer = parent.layer === undefined ? layui.layer : top.layer,
	    $ = layui.jquery;
	var codePara = {
		code:"MENU_TYPE"					
	};
	common.ajax(getCategoryCode, 'post', 'json', codePara, function (codeRes){
		//
		codeList=codeRes.data['MENU_TYPE'];
		//下拉框赋值
		var rdata=codeList;
		var temp =getRoleCodeHtml(rdata);
		$('#MENUTYPE').html(temp);
		form.render();
	})
        //监听提交
		form.on('submit(formDemo)', function(data){
			common.ajax(addRole, 'post', 'json', data.field, function (res){
				if(res==1){
					layer.msg("保存成功!");
					setTimeout(function(){
					   var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					   parent.layer.close(index); //再执行关闭  
					   layer.close(index);
					},500)
				}else if(res==2){
					layer.msg("网络异常!");
				}else if(res==3){
					layer.msg("角色名重复!");
				}
			})
		return false;
        });
})
function getRoleCodeHtml(list,value){
	var proHtml='';
	if(!value){
		value=""
	}
	for ( var i=0; i<list.length;i++){
		if(list[i].value!=2 && list[i].value!=3){
			if(value==list[i].value){
				proHtml+='<option value="'+list[i].value+'" selected >'+list[i].name+'</option>';
			}else{
				proHtml+='<option value="'+list[i].value+'" >'+list[i].name+'</option>';
			}
		}
	}
	return proHtml;
}