layui.use(['form','layer','laydate','table','laytpl','common'],function(){	
	var common = layui.common;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;
		var para={};
    //初始方法
	common.ajax(codeKindInfo, 'post', 'json', para, function (res){
		Action(res.data);
	})
	
	function Action(data){
		var tableIns = table.render({
			elem: '#newsList',
			data : data,
			cellMinWidth : 95,
			page : true,
			height : "full-125",
			limit : 20,
			limits : [10,15,20,25],
			id : "newsListTable",
			cols : [[
				// {type: "checkbox", fixed:"left", width:50},
				{field: 'KD_CODE', title: '编码',align:"center",sort:true},
				{field: 'NA_CODE_KIND', title: '编码名称', align:'center',sort:true},
				// {field: 'FIELD_SEQ_DISPLAY', title: '排序', width:95, align:'center'},
				{title: '操作', width:240, templet:'#newsListBar',fixed:"right",align:"center"}
			]]
		});
		form.render();
	}
	
	//搜索
	$(".search_btn").on("click",function(){
		var par= getformObj($("#formid").serializeArray());
		common.ajax(codeKindInfo, 'post', 'json', par, function (res){
			Action(res.data);
		})
	});
	
	//添加编码
	function addNews(obj){
	    var index = layui.layer.open({
	        title : "编辑字典",
	        type : 2,
	        content : "sysCodeKindEdit.html",
	        success : function(layero, index){
	            var body = layui.layer.getChildFrame('body', index);
				//有值为编辑否则为新增
				if(obj){
					body.find("#kdCode").val(obj.KD_CODE);
					body.find("#naCodeKind").val(obj.NA_CODE_KIND);
					body.find("#UpdaFlag").val("1");
				}
				form.render();
	            
	        },
			end: function(index, layero){
				common.ajax(codeKindInfo, 'post', 'json', null, function (res){
					Action(res.data);
				})
			}
	    })
	    layui.layer.full(index);
	    //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
	    $(window).on("resize",function(){
	        layui.layer.full(index);
	    })
	}
	$(".addNews_btn").click(function(){
	    addNews();
	})

	//列表操作
	table.on('tool(newsList)', function(obj){
	    var layEvent = obj.event,
	    data = obj.data;
	    if(layEvent === 'look'){ //查看明细
			var index = layui.layer.open({
			title : "数据字典明细",
			type : 2,
			content : "sysCodeinfo.html",
			success : function(layero, index){
				var body = layui.layer.getChildFrame('body', index);
				//有值为编辑否则为新增
				body.find("#kdCode").val(data.KD_CODE);
				form.render();
				setTimeout(function(){
					layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {
						tips: 3
					});
				},500)
			}
		})
		//窗口最大化
		layui.layer.full(index);
		//改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
		$(window).on("resize",function(){
			layui.layer.full(index);
		})
	    } else if(layEvent === 'del'){ //删除
	        layer.confirm('确定删除此数据？',{icon:3, title:'提示信息'},function(index){
				var par={'kdCode':data.KD_CODE};
				common.ajax(deleteCodeKind, 'post', 'json', par, function (res) {
					if(res==1){
						obj.del();
						layer.msg("删除成功!");
					}else{
						layer.msg("网络异常!");
					}
				})
	         })
	    }else if(layEvent === 'update'){
			addNews(data);
		}
	});
})