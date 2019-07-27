var codeList;
layui.use(['form','layer','laydate','table','laytpl','common'],function(){
	var common = layui.common;
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : top.layer,
		$ = layui.jquery,
		laydate = layui.laydate,
		laytpl = layui.laytpl,
		table = layui.table;
		var para={'PARENT_ID':'0'};
		//当前菜单的父级
		var parentId;
		//菜单类型flag
		var menuTypeFlag;
		//初始化
		Action(para);
	//初始方法
	function Action(para){
		menuTypeFlag = para.MENU_TYPE_FLAG;
		common.ajax(findMenu, 'post', 'json', para, function (res) {
				var codePara = {
					code:"MENU_TYPE,MENU_EVENT_TYPE"					
				};
				common.ajax(getCategoryCode, 'post', 'json', codePara, function (codeRes){
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
					codeList=codeRes.data;
					//下拉框赋值
					var rdata=codeList['MENU_TYPE'];
					var temp =getCodeHtml(rdata);
					temp="<option value=''>全部</option>"+temp;
					$('#MENU_TYPE').html(temp);
					$('#MENU_EVENT_TYPE').html("<option value=''>请选择</option>");
					$("#TYPE").find("option[value='']").prop("selected",true);
					//下拉框选中事件
					form.on('select(filterMenuType)', function(data){
						if(data.value){
							for (var i=0;i<rdata.length;i++) {
								if(rdata[i].value==data.value){
									temp =getCodeHtml(rdata[i].children);
									temp="<option value=''>全部</option>"+temp
									$('#MENU_EVENT_TYPE').html(temp);
								}
							}
						}else{
							$('#MENU_EVENT_TYPE').html("<option value=''>请选择</option>");
						}
						form.render('select');
					});
					//表单赋值
					console.log(res);
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
							{field: 'ID', title: '菜单项ID', width:95, align:"center",sort:true},
							{field: 'PARENT_ID', title: '上级菜单项ID', align:'center',templet: function (d) {
								if(d.PARENT_ID==0){
									return "顶级菜单";
								}else{
									return d.PARENT_ID;
								}
							}},
							{field: 'MENU_NAME', title: '菜单名称',  align:'center'},
							{field: 'MENU_URL', title: '路径', align:'center'},
							{field: 'MENU_TYPE', title: '菜单类型', align:'center',templet: function (d) {
								return getzy('MENU_TYPE',d.MENU_TYPE);
							}},
							{field: 'MENU_EVENT_TYPE', title: '菜单事件类型', align:'center',templet: function (d) {
								return getzzy('MENU_EVENT_TYPE',d.MENU_TYPE,d.MENU_EVENT_TYPE);
							}},
							{field: 'TYPE', title: '属性', align:'center',templet: function (d) {
								if(d.TYPE==0){
									return "网页";
								}else if(d.TYPE==1){
									return "功能";
								}else{
									return ;
								}
							}},
							{field: 'SORTNUM', title: '序号', align:'center',sort:true},
							{title: '操作', width:240, templet:'#newsListBar',fixed:"right",align:"center"}
						]]
					});
					form.render();
				})
		})
		form.render();
	}
	
	function getzy(code,value){
		var temp=codeList[code];
		for(var i=0;i<temp.length;i++){
			 if(value==temp[i].value){
				 return temp[i].name;
			 }
		}	
	}
	function getzzy(code,fvalue,value){
		var temp=codeList.MENU_TYPE;
		for(var i=0;i<temp.length;i++){
			if(fvalue==temp[i].value){
				for(var j=0;j<temp[i].children.length;j++){
					if(value == temp[i].children[j].value){
						return temp[i].children[j].name;
					}
				}
			}
		}
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
	//添加菜单
	function addNews(obj){
		var index = layui.layer.open({
			title : "编辑菜单",
			type : 2,
			content : "menuAdd.html",
			success : function(layero, index){
				var body = layui.layer.getChildFrame('body', index);
				//有值为编辑否则为新增
				if(obj){
					body.find("#ID").val(obj.ID);
					body.find("#MENU_NAME").val(obj.MENU_NAME);
					body.find("#PARENT_ID").val(obj.PARENT_ID);
					body.find("#MENU_URL").val(obj.MENU_URL);
					body.find("#MENU_TYPE_FLAG").val(obj.MENU_TYPE);
					body.find("#MENU_EVENT_TYPE_FLAG").val(obj.MENU_EVENT_TYPE);
					body.find("#MENU_TYPE_FLAG").val(obj.MENU_TYPE);
					body.find("#SORTNUM").val(obj.SORTNUM);
				}else{
					body.find("#MENU_TYPE_FLAG").val(menuTypeFlag);
					body.find("#PARENT_ID").val(parentId);
					body.find("#PARENT_ID").attr('readonly','true');
				}
				setTimeout(function(){
					layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {
						tips: 3
					});
				},500)
			},
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
		if(layEvent === 'edit'){ //查看子菜单
			var paraZkxj = {'PARENT_ID':data.ID,'MENU_TYPE_FLAG':data.MENU_TYPE}
			Action(paraZkxj);
		} else if(layEvent === 'del'){ //删除
			layer.confirm('确定删除此菜单？',{icon:3, title:'提示信息'},function(index){
				var paraDele = {'ID':data.ID};
				common.ajax(deleteM, 'post', 'json', paraDele, function (res) {
					if(res==1){
						obj.del();
						layer.msg("删除成功!");
					}else if(res==0){
						layer.msg("存在子菜单,无法删除!");
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