layui.use(['form','layer','laydate','table','laytpl','common'],function(){
	var common = layui.common;
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery,
		laydate = layui.laydate,
		laytpl = layui.laytpl,
		table = layui.table;
		var para = {'PARENT_ID':'0'};
		var parentId;
		Action(para);
	//初始方法
	function Action(para){
		common.ajax(departmentInfo, 'post', 'json', para, function (res) {
			//存父级id
			if(para.PARENT_ID){
				parentId=para.PARENT_ID;
			}else if(res.data.length>0){
				parentId=res.data[0].PARENT_ID;
			}
			//顶级节点不显示返回按钮
			if(parentId==0){
				$("#fh").hide();
			}else{
				$("#fh").show();
			}
			//表单赋值
			var tableIns = table.render({
				elem: '#newsList',
				data : res.data,
				cellMinWidth : 95,
				page : true,
				height : "full-125",
				limit : 20,
				limits : [10,15,20,25],
				id : "newsListTable",
				cols : [[
					// {type: "checkbox", fixed:"left", width:50},
					{field: 'ID', title: '组织编码', align:'center'},
					{field: 'PARENT_ID', title: '父级组织名称', align:'center',templet: function (d) {
						if(d.PARENT_ID==0){
							return "顶级部门";
						}else{
							return d.DEPNAME;
						}
					}},
					{field: 'DEPARTMENT_NAME', title: '组织名称',  align:'center'},
					{title: '操作', width:240, templet:'#newsListBar',fixed:"right",align:"center"}
				]]
			});
			form.render();
		})
	}
	//搜索
	$(".search_btn").on("click",function(){
		var paraSs= getformObj($("#formid").serializeArray());
		paraSs.PARENT_ID = parentId;
		Action(paraSs);
	});
	//返回
	$("#fh").on("click",function(){
		var paraFh = {'returnId':parentId};
		Action(paraFh);
	});
	//添加组织机构
	function addNews(obj){
		var index = layui.layer.open({
			title : "组织机构编辑",
			type : 2,
			content : "eaditDepartment.html",
			success : function(layero, index){
				var body = layui.layer.getChildFrame('body', index);
				//有值为编辑否则为新增
				if(obj){
					body.find("#DEPARTMENT_NAME").val(obj.DEPARTMENT_NAME);
					body.find("#ID").val(obj.ID);
					body.find("#ID").attr('readonly','true');
				}
				body.find("#PARENT_ID").val(parentId);
				body.find("#PARENT_ID").attr('readonly','true');
				form.render();
				setTimeout(function(){
					layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {
						tips: 3
					});
				},500)
			}
			,
			end: function(index, layero){
				var paraHd={'PARENT_ID':parentId};
				Action(paraHd);
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
		if(layEvent === 'edit'){ //展开下级
			var paraZkxj = {'PARENT_ID':data.ID}
			Action(paraZkxj);
		} else if(layEvent === 'del'){ //删除
			layer.confirm('确定删除此菜单？',{icon:3, title:'提示信息'},function(index){
				var paraDele = {'ID':data.ID};
				common.ajax(deleteDepartment, 'post', 'json', paraDele, function (res) {
					if(res==1){
						obj.del();
						layer.msg("删除成功!");
					}else if(res==0){
						layer.msg("存在子组织机构，无法删除!");
					}else{
						layer.msg("数据异常!");
					}
				})
			})
		}else if(layEvent === 'update'){
			addNews(data);
		}
	});
})