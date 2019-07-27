	layui.use(['authtree', 'form', 'layer','common'], function(){
		 $ = layui.jquery;
		var common = layui.common;
		var authtree = layui.authtree;
		var form = layui.form;
		var layer = layui.layer;
		
		//部门递归
// 		function forDep(arr){
// 			var jsonarray=[];
// 			for(index in arr){
// 				var params=new Object();
// 				params.value=arr[index].value;
// 				params.name=arr[index].name;
// 				params.kd_code=arr[index].kd_code;
// 				params.code_superior=arr[index].code_superior;
// 				jsonarray.push(params);
// 				//有子集递归
// 				if(arr[index].children.length>0){
// 					var z = forDep(arr[index].children)
// 					for(i in z){
// 						jsonarray.push(z[i]);
// 					}
// 				}
// 			}
// 			return jsonarray;
// 		}
		//获取url中的参数
		var USER_ID = getUrlParam('ID');
		//获取url中值的方法
		function getUrlParam (name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if (r!= null) {
				return unescape(r[2]);
			}else{
				return null;
			}
		}
		var codePara = {
			code:"DEPARTMENT"
		};
		common.ajax(getCategoryCode, 'post', 'json', codePara, function (codeRes){
			var depData= codeRes.data;
			// var arr = forDep(depData.DEPARTMENT);
			var para = {"USER_ID":USER_ID};
			common.ajax(selectUserDep, 'post', 'json', para, function (data) {
				var trees = authtree.listConvert(depData.DEPARTMENT, {
				primaryKey: 'value'
				,startPid: '0'
				,parentKey: 'code_superior'
				,nameKey: 'name'
				,valueKey: 'value'
				,checkedKey: data.checkedId
				});
			// 初始化
				// 渲染时传入渲染目标ID，树形结构数据（具体结构看样例，checked表示默认选中），以及input表单的名字
				authtree.render('#LAY-auth-tree-index', trees, {
					inputname: 'ids[]'
					,layfilter: 'lay-check-auth'
					// ,dblshow: true
					// ,dbltimeout: 180
					// ,autoclose: false
					// ,autochecked: false
					// ,openchecked: false
					// ,openall: true
	                // ,hidechoose: true
					// ,checkType: 'radio'
					// ,checkSkin: 'primary'
					,autowidth: true
				});
	
				// PS:使用 form.on() 会引起了事件冒泡延迟的BUG，需要 setTimeout()，并且无法监听全选/全不选
				// PS:如果开启双击展开配置，form.on()会记录两次点击事件，authtree.on()不会
	/*			form.on('checkbox(lay-check-auth)', function(data){
					// 注意这里：需要等待事件冒泡完成，不然获取叶子节点不准确。
					setTimeout(function(){
						console.log('监听 form 触发事件数据', data);
						// 获取选中的叶子节点
						var leaf = authtree.getLeaf('#LAY-auth-tree-index');
						console.log('leaf', leaf);
						// 获取最新选中
						var lastChecked = authtree.getLastChecked('#LAY-auth-tree-index');
						console.log('lastChecked', lastChecked);
						// 获取最新取消
						var lastNotChecked = authtree.getLastNotChecked('#LAY-auth-tree-index');
						console.log('lastNotChecked', lastNotChecked);
					}, 100);
				});
	*/			// 使用 authtree.on() 不会有冒泡延迟
				authtree.on('change(lay-check-auth)', function(data) {
					// console.log('监听 authtree 触发事件数据', data);
					// 获取所有节点
					var all = authtree.getAll('#LAY-auth-tree-index');
					// 获取所有已选中节点
					var checked = authtree.getChecked('#LAY-auth-tree-index');
					// 获取所有未选中节点
					var notchecked = authtree.getNotChecked('#LAY-auth-tree-index');
					// 获取选中的叶子节点
					var leaf = authtree.getLeaf('#LAY-auth-tree-index');
					// 获取最新选中
					var lastChecked = authtree.getLastChecked('#LAY-auth-tree-index');
					// 获取最新取消
					var lastNotChecked = authtree.getLastNotChecked('#LAY-auth-tree-index');
// 					console.log(
// 						'all', all,"\n",
// 						'checked', checked,"\n",
// 						'notchecked', notchecked,"\n",
// 						'leaf', leaf,"\n",
// 						'lastChecked', lastChecked,"\n",
// 						'lastNotChecked', lastNotChecked,"\n"
// 					);
				});
				authtree.on('deptChange(lay-check-auth)', function(data) {
					// console.log('监听到显示层数改变',data);
				});
				authtree.on('dblclick(lay-check-auth)', function(data) {
					// console.log('监听到双击事件',data);
				});
			})
		})
		// 表单提交样例
		form.on('submit(LAY-auth-tree-submit)', function(obj){
			var authids = authtree.getChecked('#LAY-auth-tree-index');
			authids=JSON.stringify(authids);
			var para = {'USER_ID':USER_ID,'DEP_ID':authids};
			// obj.field.authids = authids;
			common.ajax(updateUserDep, 'post', 'json', para, function (res) {
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
	});