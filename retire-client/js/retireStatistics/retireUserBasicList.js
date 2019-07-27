layui.use(['form','layer','table','laytpl','common'],function(){	
	var common = layui.common;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;
	var	user = JSON.parse(sessionStorage.getItem("user"));
	Action();
	var userParam = {};
		var parentCodeList = parent.codeList;
	//2、查数据字典列表
	var codeParam = {
		code:BASE_CODE_NATION  + "," + BASE_CODE_EDUCATION + "," + BASE_CODE_RETIREMENT_RANK + "," + 
			BASE_CODE_NOW_TREATMENT_LEVEL + "," + BASE_CODE_ARCHIVES_AREA + "," + BASE_CODE_RECEIVE_AREA + "," + 
			BASE_CODE_HEALTH_STATUS + "," + BASE_CODE_PLAY_A_ROLE + "," + BASE_CODE_AWARD_LEVEL + "," + BASE_CODE_SPECIAL_CROWD + "," +
			BASE_CODE_APPELLATION +","+BASE_DEPARTMENT +","+BASE_CODE_USER_NATURE+","+BASE_CODE_USER_RANK
	};
    //初始方法
	function Action(){
		//1、查单位下拉框数据
		var unitParam = {'parent_id':BASE_DEPARTMENT,'department_id':user['unit_id']};
		common.ajax(listBaseDepartment, 'post', 'json', unitParam, function (dres) {
			var unitHtml = getDepartmentHtml(dres.data,$('#unit_id').val());
			//多条数据才加“全部”选项，地市级单位只看自己公司的数据
			if(dres.data.length > 1){
				unitHtml = "<option value=''>全部</option>" + unitHtml;
			}
			$('#unit_id').html(unitHtml);
			form.render('select');
			//2、职级
//			var codeParam = {"parent_id":BASE_CODE_RETIREMENT_RANK, "is_available":"1"};
			common.ajax(getCode, 'post', 'json', codeParam , function (codeRes) {
				var tableCols = [];
				tableCols[0] = [{field: 'DEPARTMENT_NAME', title: '企业名称', width:160,minWidth:160, align:"center", rowspan:2},
						{field: 'CODE_NAME',title: '退休人<br>员性质', width:80,minWidth:80, align:"center", rowspan:2},
						{title: '年龄结构', align:"center", colspan:11},
						{title: '职级', align:"center", colspan:codeRes.data[BASE_CODE_RETIREMENT_RANK].length}];
				tableCols[1] = [{field: 'TOTAL_NUM', title: '总人数', width:80,minWidth:80, align:"center"},
						{field: 'AGE_AVG', title: '平均<br>年龄', width:70,minWidth:70, align:"center"},
						{field: 'AGE_45', title: '45岁及<br>以下', width:100,minWidth:100, align:"center"},
						{field: 'AGE_46_50', title: '46岁-50岁', width:100,minWidth:100, align:"center"},
						{field: 'AGE_51_55', title: '51岁-55岁', width:100,minWidth:100, align:"center"},
						{field: 'AGE_56_60', title: '56岁-60岁', width:100,minWidth:100, align:"center"},
						{field: 'AGE_61_70', title: '61岁-70岁', width:100,minWidth:100, align:"center"},
						{field: 'AGE_71_80', title: '71岁-80岁', width:100,minWidth:100, align:"center"},
						{field: 'AGE_81_90', title: '81岁-90岁', width:100,minWidth:100, align:"center"},
						{field: 'AGE_91_99', title: '91岁-99岁', width:100,minWidth:100, align:"center"},
						{field: 'AGE_100', title: '100岁<br>及以上', width:80,minWidth:80, align:"center"}];
				for(var i=0;i<codeRes.data[BASE_CODE_RETIREMENT_RANK].length;i++){
					var code_id = codeRes.data[BASE_CODE_RETIREMENT_RANK][i].value;
					var code_name = codeRes.data[BASE_CODE_RETIREMENT_RANK][i].name;
					tableCols[1][11+i] = {field: 'RETIREMENT_RANK_ID_' + code_id.toUpperCase(), title: code_name, width:100,minWidth:100, align:"center"};
				}
				//3、构建动态表头及列表
				userParam['unit_id'] = $("#unit_id").val();
				common.ajax(listRetireUserBasic, 'post', 'json', userParam , function (ures) {
					var tableIns = table.render({
						elem : '#userList',
						data : ures.data,
						height : "full-110",
						id : "userListTable",
						method: 'post',
						page:false,
						limit:ures['data'].length,  //不设置会只显示10条
						cols : tableCols,
						done: function (res, curr, count) {
							//数据渲染完的回调合并表格内容行
							mergeRows(0);
						}
					});
					form.render();
				});
				form.render();
			});
		});
	}
    //搜索
    $(".search_btn").on("click",function(){
		userParam = getformObj($("#formid").serializeArray());
		Action();
    });
	//导出Excel
	$("#excel").click(function(){
	    $("#formid").attr("action",excelRetireUserBasic).trigger("submit");
	});
	/**
     * 合并第几列（相同内容合并）
     * @param index 列的下标,从0开始
     * @author CYK
     */
    function mergeRows(index) {
        // 根据选择器找到表格的tr
        var _tr = $('.layui-table-body table tr');
        var mergeStr = '', oldName, newName, countNum = 0;
        for (var i = 0, trLength = _tr.length; i < trLength; i++) {
            newName = $(_tr[i]).find('div:eq(' + index + ')').text();
			if(newName!="" && newName!=null){
				if (i == 0 || newName == oldName) {
					countNum++;
				} else {
					mergeStr += (countNum + (Array(countNum).join(0)));
					countNum = 1;
				}
				oldName = newName;
			}
        }
        // 解释:从左到右,4表示合并4行,0表示设定display:none
        mergeStr += (countNum + (Array(countNum).join(0)));
        var cssNum = 0;
        // 根据合并参考信息进行行合并
        for (var i = 0, length = mergeStr.length; i < length; i++) {
            var e = $(_tr[i]).find('td:eq(' + index + ')');
            if (mergeStr[i] !== '0') {
                // 合并行
                e.attr('rowspan', mergeStr[i]);
            } else {
                e.css('display', 'none');
            }
        }
    }

})