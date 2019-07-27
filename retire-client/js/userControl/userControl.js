layui.use(['form','layer','laydate','table','laytpl','common','formSelects'],function(){	
	var common = layui.common;
	var formSelects = layui.formSelects;
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
		common.ajax(userControlInfo, 'post', 'json', obj, function (res) {
				var codePara = {
					code:"DEPARTMENT"
				};
				common.ajax(getCategoryCode, 'post', 'json', codePara, function (codeRes){
					codeList=codeRes.data;
					//部门递归
					function forDep(arr){
						var jsonarray=[];
						for(index in arr){
							//只收录顶级节点
							if(arr[index].code_superior==0){
								var params=new Object();
								params.value=arr[index].value;
								params.name=arr[index].name;
								params.children=arr[index].children;
								jsonarray.push(params);
							}
						}
						return jsonarray;
					}
					var selectData = forDep(codeList.DEPARTMENT);
					//多选框框赋值
					layui.formSelects.data('example11_1', 'local', {
						arr: selectData,
						tree: {
							//在点击节点的时候, 如果没有子级数据, 会触发此事件
							nextClick: function(id, item, callback){
								//需要在callback中给定一个数组结构, 用来展示子级数据
// 								callback([
// 									{name: 'test1', value: Math.random()},
// 									{name: 'test2', value: Math.random()}
// 								])
							},
						}
					});
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
							{field: 'ID', title: '用户ID', width:95, align:"center",sort:true},
							{field: 'USER_NAME', title: '用户名称',  align:'center'},
							{field: 'LOGIN_NAME', title: '用户登录', align:'center'},
							{field: 'DEPARTMENT', title: '用户部门', align:'center',templet: function (d) {
								return getzy('DEPARTMENT',d.DEPARTMENT);
							}},
							{field: 'STATE', title: '用户状态', align:'center',templet: function (d) {
								if(d.STATE==0){
									return '启用';
								}else if(d.STATE==1){
									return "禁用";
								}else if(d.STATE==2){
									return '冻结';
								}
							}},
							{title: '操作', templet:'#newsListBar',fixed:"right",align:"center", width: 450}
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
		var temp=codeList.DEPARTMENT;
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
		var para= getformObj($("#formid").serializeArray());
		Action(para);
    });

    //列表操作
    table.on('tool(newsList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        if(layEvent === 'userContro'){
			var index = layui.layer.open({
			    title : "用户操作权限设置",
			    type : 2,
			    content : "userDepControl.html?ID="+data.ID,
			    success : function(layero, index){
					form.render();
			        setTimeout(function(){
			            layui.layer.tips('点击此处返回角色列表', '.layui-layer-setwin .layui-layer-close', {
			                tips: 3
			            });
			        },500)
			    }
			})
			layui.layer.full(index);
			//改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
			$(window).on("resize",function(){
			    layui.layer.full(index);
			})
        } else if(layEvent === 'userRole'){
            var index = layui.layer.open({
                title : "用户角色设置",
                type : 2,
                content : "userRoleControl.html?ID="+data.ID,
                success : function(layero, index){
            		form.render();
                    setTimeout(function(){
                        layui.layer.tips('点击此处返回角色列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    },500)
                }
            })
            layui.layer.full(index);
            //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
            $(window).on("resize",function(){
                layui.layer.full(index);
            })
        }else if(layEvent === 'resetPwd'){//重置密码
			layer.confirm('确定重置密码?',{icon:3, title:'提示信息'},function(index){
				var id={'USER_ID':data.ID};
				common.ajax(resetPwd, 'post', 'json', id, function (res) {
					if(res==1){
						layer.msg("重置成功!");
					}else if(res==3){
						layer.msg("不是管理员,无法重置!");
					}else{
						layer.msg("数据异常!");
					}
				})
			 })
		}else if(layEvent === 'enable'){//启用用户
			layer.confirm('确定启用用户?',{icon:3, title:'提示信息'},function(index){
				var id={'USER_ID':data.ID,'updateState':'0'};
				common.ajax(updateUsetState, 'post', 'json', id, function (res) {
					if(res==1){
						layer.msg("启用成功!");
						Action();
					}else if(res==3){
						layer.msg("不是管理员,无法启用!");
					}else{
						layer.msg("数据异常!");
					}
				})
			})
		}else if(layEvent === 'prohibit'){//禁用用户
			layer.confirm('确定禁用用户?',{icon:3, title:'提示信息'},function(index){
				var id={'USER_ID':data.ID,'updateState':'1'};
				common.ajax(updateUsetState, 'post', 'json', id, function (res) {
					if(res==1){
						layer.msg("禁用成功!");
						Action();
					}else if(res==3){
						layer.msg("不是管理员,无法禁用!");
					}else{
						layer.msg("数据异常!");
					}
				})
			})
		}
    });
})