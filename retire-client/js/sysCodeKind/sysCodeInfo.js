var colsData;
layui.use(['form','layer','laydate','table','laytpl','common'],function(){
	var common = layui.common;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;
	var targetId = $("#kdCode").val();
    //初始方法
	common.ajax(codeInfo, 'post', 'json', {'kdCode':targetId}, function (res){
		Action(res);
	})
	function Action(res){
		var col = res.data.cols;
		//页面上写好再将拓展属性put进去
		colsData = [[
				{field: 'KD_CODE', title: '编码', align:"center"},
				{field: 'CODE', title: '编码值', edit: 'text',align:'center'},
				{field: 'CODE_VAL', title: '中文值',  edit: 'text',align:'center'},
				{field: 'CODE_SUPERIOR', title: '父级别编码', edit: 'text', align:'center'},
				{field: 'FL_AVAILABLE', title: '是否启动', templet: '#newsListTpl', align:'center'}
			]]
		for(i in col){
			colsData[0].push(col[i]);
		}
		colsData[0].push({field: 'SEQ_DISPLAY', title: '排序',  edit: 'text',align:'center'});
		colsData[0].push({title: '操作',templet:'#newsListBar',fixed:"right",align:'center'});
		var tableIns = table.render({
			elem: '#newsList',
			data : res.data.data,
			cellMinWidth : 95,
			// page : true,
			height : "full-125",
			limit : res.count,
// 			limits : [10,15,20,25],
			id : "newsListTable",
			cols : colsData
		});
		form.render();
	}
	
	//列表操作
	table.on('tool(newsList)', function(obj){
		var layEvent = obj.event,
			data = obj.data;
		if(layEvent === 'del'){ //删除
			layer.confirm('确定删除此数据？',{icon:3, title:'提示信息'},function(index){
				obj.del();
				layer.msg("记得保存!");
			})
		}
	});
	//开关监听改变table值
	form.on('switch(switchTest)', function(data){
		if($(data.elem).val()==1){
			$(data.elem).val("0");
			table.cache.newsListTable[data.othis.parents('tr').data('index')].FL_AVAILABLE="0";
		}else{
			$(data.elem).val("1");
			table.cache.newsListTable[data.othis.parents('tr').data('index')].FL_AVAILABLE="1";
		}
	});
	//添加编码
	$(".addNews_btn").click(function(){
		addNews();
	})
	//添加编码拓展属性
	$(".codeAddI").click(function(){
		var index = layui.layer.open({
			title : "编码拓展属性",
			type : 2,
			content : "sysCodeAddI.html",
			success : function(layero, index){
				var body = layui.layer.getChildFrame('body', index);
				body.find("#KD_CODE").val(targetId);
				form.render();
			},
			end: function(index, layero){
				common.ajax(codeInfo, 'post', 'json', {'kdCode':targetId}, function (res){
							Action(res);
				})
			}
		})
		//窗口最大化
		layui.layer.full(index);
		//改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
		$(window).on("resize",function(){
			layui.layer.full(index);
		})
   })
	function addNews(){
		var index = layui.layer.open({
			title : "编辑字典",
			type : 2,
			content : "sysCodeAdd.html",
			success : function(layero, index){
				var body = layui.layer.getChildFrame('body', index);
				body.find("#KD_CODE").val(targetId);
				form.render();
	        },
			end: function(index, layero){
				common.ajax(codeInfo, 'post', 'json', {'kdCode':targetId}, function (res){
					Action(res);
				})
			}
		})
		//窗口最大化
		layui.layer.full(index);
		//改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
		$(window).on("resize",function(){
			layui.layer.full(index);
		})
	}
		//保存
		$("#submit").click(function(){
			var eidPara = layui.table.cache.newsListTable;
			if(eidPara.length==0){
				layer.msg("数据不完整!");
				return false;
			}
			var upArr = removeArr(eidPara);
			//校验对应的值
			for(var i=0;i<upArr.length;i++){
				var item=upArr[i];
				if(item.CODE==''||item.CODE_VAL==''){
					layer.msg("数据不完整!");
					return false;
				}
			}
			eidPara= JSON.stringify(upArr);
			var par={'table':eidPara,'kdCode':targetId};
			common.ajax(updateSysCode, 'post', 'json', par, function (res){
				if(res==1){
					layer.msg("保存成功!");
					setTimeout(function(){
					   var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					   parent.layer.close(index); //再执行关闭  
					   layer.close(index);
					},500)
				}else if(res==3){
					layer.msg("数据重复!");
				}else{
					layer.msg("数据重复或数据异常!");
				}
			})
		});
		//数组去空
		function removeArr(list){
			var data = [];
			for(var i=0;i<list.length;i++){
				var item=list[i];
				if(item.KD_CODE){
					data.push(item);
				}else{
				}
			}
			return data;
		}
		
})
//返回colsData
function getData(){
	return colsData[0];
}