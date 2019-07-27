layui.use(['form','layer','laydate','table','common'],function(){	
	var common = layui.common;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table;
	var	user = JSON.parse(sessionStorage.getItem("user"));
	//选择统计年度
	laydate.render({
	    elem: '#year',
		theme: 'grid',
		type: 'year',
	    max : 0,
		value: new Date(),
		format: 'yyyy年',
		// 点击即选中
        ready:function(date){
            $("#layui-laydate1").off('click').on('click','.laydate-year-list li',function(){
                $("#layui-laydate1").remove();
            });
        },
		change: function(value){
			$('#year').val(value);
		}
	});
	Action();
	var userParam = {};
    //初始方法
	function Action(){
		var departmentParam = {'parent_id':BASE_DEPARTMENT,'department_id':user['unit_id']};
		common.ajax(listBaseDepartment, 'post', 'json', departmentParam, function (dres) {
			var departmentHtml = getDepartmentHtml(dres.data,$('#unit_id').val());
			//多条数据才加“全部”选项，地市级单位只看自己公司的数据
			if(dres.data.length > 1){
				departmentHtml = "<option value=''>全部</option>" + departmentHtml;
			}
			$('#unit_id').html(departmentHtml);
			form.render('select');
			//加载列表
			userParam['unit_id'] = $("#unit_id").val();
			userParam['year'] = $('#year').val().replace("年",'');
			common.ajax(listRetireArenaAndUniversity, 'post', 'json', userParam, function (ures) {
				var editable = "background-color:#5FB878";
				var tableIns = table.render({
					elem : '#arenaList',
					data : ures.data,
					cellMinWidth : 60,
					height : "full-110",
					id : "arenaListTable",
					method: 'post',
					page: false,
					limit: ures['data'].length,  //不设置会只显示10条
					cols : [[
						{field: 'DEPARTMENT_NAME', title: '项目', minWidth:160,align:"left", rowspan:2},
						{title: '编号', width:60,minWidth:60, align:"center", rowspan:2, templet: function(d){
							return d.LAY_TABLE_INDEX + 1;
						}},
						{title: '现有活动场所', align:"center", colspan:3},
						{title: '在建活动场所', align:"center", colspan:2},
						{title: '退休人员独立使用 ', align:"center", colspan:4},
						{title: '与在职人员共同使用', align:"center", colspan:4},
						{title: '具备移交条件的活动场所', align:"center", colspan:4},
						{title: '年末固定资产情况（万元）', align:"center", colspan:2},
						{title: '现有老年大学', align:"center", colspan:5},
						{title: '在建老年大学', align:"center", colspan:2},
						{title: '就读社会老年大学', align:"center", colspan:2}
					],[
						{field: 'EXISTING_SUM',title: '总数<br>（个）', width:90,align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'EXISTING_ACREAGE',title: '总建筑面积<br>（平方米）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'EXISTING_EVERYDAY',title: '日均活动<br>人数', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						
						{field: 'ABUILDING_SUM',title: '总数<br>（个）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'ABUILDING_ACREAGE',title: '总建筑面积<br>（平方米）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						
						{field: 'INDEPENDENT_SUM',title: '总数<br>（个）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'INDEPENDENT_ACREAGE',title: '总建筑面积<br>（平方米）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'INDEPENDENT_ORIGINAL_VALUE',title: '年末固定资产<br>原值（万元）', width:120, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'INDEPENDENT_NET_VALUE',title: '年末固定资产<br>净值（万元）', width:120, align:"center", style:editable, event:"nine_number", edit:'text'},
						
						{field: 'COMMON_SUM',title: '总数<br>（个）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'COMMON_ACREAGE',title: '总建筑面积<br>（平方米）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'COMMON_ORIGINAL_VALUE',title: '年末固定资产<br>原值（万元）', width:120, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'COMMON_NET_VALUE',title: '年末固定资产<br>净值（万元）', width:120, align:"center", style:editable, event:"nine_number", edit:'text'},
						
						{field: 'ELIGIBLE_SUM',title: '总数<br>（个）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'ELIGIBLE_ACREAGE',title: '总建筑面积<br>（平方米）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'ELIGIBLE_ORIGINAL_VALUE',title: '年末固定资产<br>原值（万元）', width:120, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'ELIGIBLE_NET_VALUE',title: '年末固定资产<br>净值（万元）', width:120, align:"center", style:editable, event:"nine_number", edit:'text'},
						
						{field: 'FIXED_ASSETS_ORIGINAL_VALUE', title: '原值', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'FIXED_ASSETS_NET_VALUE', title: '净值', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						
						{field: 'UNIVERSITY_SUM',title: '总数<br>（个）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'UNIVERSITY_ACREAGE',title: '总建筑面积<br>（平方米）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'UNIVERSITY_REGULAR_STAFF',title: '正式<br>工作人员', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'UNIVERSITY_STUDENTS',title: '在读学员<br>人数', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'UNIVERSITY_GRADUATE',title: '累计结业<br>人数', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						
						{field: 'ABUILDING_UNIVERSITY_SUM',title: '总数<br>（个）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'ABUILDING_UNIVERSITY_ACREAGE',title: '总建筑面积<br>（平方米）', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						
						{field: 'ATTEND_STUDENTS',title: '在读学员<br>人数', width:100, align:"center", style:editable, event:"nine_number", edit:'text'},
						{field: 'ATTEND_GRADUATE',title: '累计结业<br>人数', width:100, align:"center", style:editable, event:"nine_number", edit:'text'}
					]]
				});
				form.render();
			});
		});
		form.render();
	}
    //搜索
	form.on('submit(search_btn)', function(data){
		userParam = getformObj($("#formid").serializeArray());
		Action();
		return false;
    });
	//导出Excel
	$("#excel").click(function(){
	    $("#formid").attr("action", excelRetireArenaAndUniversity).trigger("submit");
	});
	//跳转到导入Excel附件上传页
	$("#impExcel").click(function(){
	    var index = layui.layer.open({
	        title : "导入Excel",
			area: ['600px', '400px'],
	        type : 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
	        content : "retireArenaExp.html",
	        success : function(layero, index){
	            var body = layui.layer.getChildFrame('body', index);
	    		body.find("#year").val($("#year").val());
	    		form.render();
	            setTimeout(function(){
	                layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {tips: 3});
	            },500)
	        },
	    	end: function(index, layero){
	    		Action();
	    	}
	    });
	});
	//修改
	table.on('edit(arenaList)', function(obj){
		var inputElem = $(this);
		var tdElem = inputElem.closest('td');
		var valueOld = inputElem.prev().text();
		var data = {};
		if(obj.field=='EXISTING_ACREAGE' || obj.field=='ABUILDING_ACREAGE' || obj.field=='INDEPENDENT_ACREAGE' ||
			obj.field=='COMMON_ACREAGE' || obj.field=='ELIGIBLE_ACREAGE' || obj.field=='UNIVERSITY_ACREAGE' ||
			obj.field=='ABUILDING_UNIVERSITY_ACREAGE' || obj.field=='FIXED_ASSETS_ORIGINAL_VALUE' || obj.field=='FIXED_ASSETS_NET_VALUE' ||
			obj.field=='INDEPENDENT_ORIGINAL_VALUE' || obj.field=='INDEPENDENT_NET_VALUE' || obj.field=='COMMON_ORIGINAL_VALUE' ||
			obj.field=='COMMON_NET_VALUE' || obj.field=='ELIGIBLE_ORIGINAL_VALUE' || obj.field=='ELIGIBLE_NET_VALUE'){ 
			if(obj.value!="" && !/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(obj.value)){
				$(tdElem).find("input").val(valueOld);
				$(tdElem).find("div").html(valueOld);
				layer.msg("金额格式不正确", {icon: 2});
				return false;
			}
		}else if(obj.field=='EXISTING_SUM' || obj.field=='EXISTING_EVERYDAY' || obj.field=='ABUILDING_SUM' ||
			obj.field=='INDEPENDENT_SUM' || obj.field=='COMMON_SUM' || obj.field=='ELIGIBLE_SUM' ||
			obj.field=='UNIVERSITY_SUM' || obj.field=='UNIVERSITY_REGULAR_STAFF' || obj.field=='UNIVERSITY_STUDENTS' ||
			obj.field=='UNIVERSITY_GRADUATE' || obj.field=='ABUILDING_UNIVERSITY_SUM' || obj.field=='ATTEND_STUDENTS' ||
			obj.field=='ATTEND_GRADUATE'){
			if(obj.value!="" && !/^[+]{0,1}(\d+)$/.test(obj.value)){
				$(tdElem).find("input").val(valueOld);
				$(tdElem).find("div").html(valueOld);
				layer.msg("请输入正整数", {icon: 2});
				return false;
			}	
		}
		//修改值到数据库
 		var CostParam = {};
		CostParam.unit_id = obj.data.UNIT_ID;
		CostParam.year = $("#year").val().replace("年",'');
		CostParam[obj.field.toLowerCase()] = obj.value;
		CostParam.modified_user_id = user['id'];
		common.ajax(saveRetireArena, 'post', 'json', JSON.stringify(CostParam), function (res) {
			layer.msg(res.message);
		},"application/json;charset=UTF-8");
	});
	
	//监听单元格事件（修改文本框maxLength）
	table.on('tool(arenaList)', function(obj){
		var data = obj.data;
		var tr = obj.tr;
		if(obj.event === 'nine_number'){
			$(tr).find("input").attr("maxLength","9");
		}
	});
})