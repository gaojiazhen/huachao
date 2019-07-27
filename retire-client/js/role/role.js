layui.use(['form','layer','laydate','table','laytpl','common'],function(){
	var common = layui.common;
	var form = layui.form,
	layer = parent.layer === undefined ? layui.layer : top.layer,
	$ = layui.jquery,
	laydate = layui.laydate,
	laytpl = layui.laytpl,
	table = layui.table;
	var codeList;
	Action();
	//初始方法
	function Action(obj){
		common.ajax(roleInfo, 'post', 'json', obj, function (res) {
				var codePara = {code:"MENU_TYPE"};
				common.ajax(getCategoryCode, 'post', 'json', codePara, function (codeRes){
					codeList=codeRes.data;
					//下拉框赋值
					var rdata=codeList['MENU_TYPE'];
					var temp =getCodeHtml(rdata);
					temp="<option value=''>全部</option>"+temp;
					$('#MENU_TYPE').html(temp);
					form.render('select');
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
							{field: 'ID', title: '角色ID', width:95, align:"center",sort:true},
							{field: 'ROLE_NAME', title: '角色名称', align:"center",sort:true},
							{field: 'ROLE_TYPE', title: '角色类型', align:"center",sort:true,templet: function (d) {
								return getzy('MENU_TYPE',d.ROLE_TYPE);
							}},
							/* {title: '操作', width:240,fixed:"right",align:"center", templet:'#newsListBar'}, */
							{title: '操作', width:240,fixed:"right",align:"center", templet: function (d) {
								var html = "<a class='layui-btn layui-btn-xs' lay-event='edit'>配置角色菜单</a>";
								if(d.ROLE_TYPE!=2 && d.ROLE_TYPE!=3){
									html+="<a class='layui-btn layui-btn-xs layui-btn-danger' lay-event='del'>删除</a>"
								}
								return html;
							}},
						]]
					});
				})
		})
		form.render();
	}
	//角色类型
	function getzy(code,value){
		var temp=codeList[code];
		for(var i=0;i<temp.length;i++){
			 if(value==temp[i].value){
				 return temp[i].name;
			 }
		}	
	}
    //搜索
    $(".search_btn").on("click",function(){
		var para= getformObj($("#formid").serializeArray());
		Action(para);
    });
    //添加角色
    function addNews(){
		var index = layui.layer.open({
			title : "添加角色",
			type : 2,
			content : "addRole.html",
			success : function(layero, index){
				var body = layui.layer.getChildFrame('body', index);
				form.render();
				setTimeout(function(){
					layui.layer.tips('点击此处返回角色列表', '.layui-layer-setwin .layui-layer-close', {
						tips: 3
					});
				},500)
			},
			end: function(index, layero){
				Action();
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
		if(layEvent === 'edit'){ //配置角色菜单
			editRole(data);
		} else if(layEvent === 'del'){ //删除
			layer.confirm('确定删除此角色？',{icon:3, title:'提示信息'},function(index){
				var id={'ID':data.ID};
				common.ajax(deleteRole, 'post', 'json', id, function (res) {
					if(res==1){
						obj.del();
						layer.msg("删除成功!");
					}else if(res==0){
						layer.msg("用户具有"+data.ROLE_NAME+"角色，无法删除!");
					}else{
						layer.msg("网络异常!");
					}
				})
			})
		}
	});
	
	//配置角色菜单
	function editRole(data){
		var index = layui.layer.open({
			title : "配置角色菜单",
			type : 2,
			content : "editRole.html?ROLE_ID="+data.ID+"&ROLE_TYPE="+data.ROLE_TYPE,
			success : function(layero, index){
				form.render();
				setTimeout(function(){
					layui.layer.tips('点击此处返回角色列表', '.layui-layer-setwin .layui-layer-close', {
						tips: 3
					});
				},500)
			},
			end: function(index, layero){
				Action();
			}
		})
		layui.layer.full(index);
		//改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
		$(window).on("resize",function(){
			layui.layer.full(index);
		})
	}
})