	layui.use(['authtree', 'form', 'layer','common'], function(){
		$ = layui.jquery;
		var common = layui.common;
		var authtree = layui.authtree;
		var form = layui.form;
		var layer = layui.layer;
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
		//获取url中的参数
		var USER_ID = getUrlParam('ID');
		var para = {"USER_ID":USER_ID};
		common.ajax(selectUserRole, 'post', 'json', para, function (data) {
			var trees = listConvert(data.data.tree, {
			primaryKey: 'ID'
			,startPid: '0'
			,nameKey: 'ROLE_NAME'
			,valueKey: 'ID'
			,checkedKey: data.data.checkedId
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
		// 表单提交
		form.on('submit(LAY-auth-tree-submit)', function(obj){
			var authids = authtree.getChecked('#LAY-auth-tree-index');
			// console.log('Choosed authids is', authids);
			authids=JSON.stringify(authids);
			var para = {'USER_ID':USER_ID,'ROLE_ID':authids};
			// obj.field.authids = authids;
			common.ajax(updateUserRole, 'post', 'json', para, function (res) {
				if(res==1){
					layer.msg("保存成功!");
					setTimeout(function(){
					   var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					   parent.layer.close(index); //再执行关闭  
					   layer.close(index);
					},500)
				}else if(res==3){
					layer.msg("角色类型需相同!");
				}else{
					layer.msg("数据异常!");
				}
			})
			return false;
		});
		
		
		
		function listConvert(list, opt) {
			opt.primaryKey = opt.primaryKey ? opt.primaryKey : 'id';
			opt.parentKey = opt.parentKey ? opt.parentKey : 'pid';
			opt.startPid = opt.startPid ? opt.startPid : 0;
			opt.currentDept = opt.currentDept ? parseInt(opt.currentDept) : 0;
			opt.maxDept = opt.maxDept ? opt.maxDept : 100;
			opt.childKey = opt.childKey ? opt.childKey : 'list';
			opt.checkedKey = opt.checkedKey ? opt.checkedKey : 'checked';
			opt.disabledKey = opt.disabledKey ? opt.disabledKey : 'disabled';
			opt.nameKey = opt.nameKey ? opt.nameKey : 'name';
			opt.valueKey = opt.valueKey ? opt.valueKey : 'id';
			return _listToTree(list, opt.startPid, opt.currentDept, opt);
		}
		// 实际的递归函数，将会变化的参数抽取出来
		function _listToTree(list, startPid, currentDept, opt) {
			if (opt.maxDept < currentDept) {
				return [];
			}
			var child = [];
			for (index in list) {
				// 筛查符合条件的数据（主键 = startPid）
				var item = list[index];
					// 节点信息保存
					var node = {};
					node['name'] = item[opt.nameKey];
					node['value'] = item[opt.valueKey];
					if ($.inArray(item[opt.valueKey], opt.checkedKey) != -1) {
						node['checked'] = true;
					} else {
						node['checked'] = false;
					}
					// 禁用节点的两种渲染方式
					if ( opt.disabledKey === "string" ||  opt.disabledKey === 'number') {
						node['disabled'] = item[opt.disabledKey];
					} else if( opt.disabledKey === 'object') {
						if ($.inArray(item[opt.valueKey], opt.disabledKey) != -1) {
							node['disabled'] = true;
						} else {
							node['disabled'] = false;
						}
					} else {
						node['disabled'] = false;
					}
					child.push(node);
			}
			return child;
		}
	});