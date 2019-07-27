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
			common.ajax(listRetireUserCorrelativeCharges, 'post', 'json', userParam, function (ures) {
				var editable = "background-color:#5FB878";
				var tableIns = table.render({
					elem : '#costList',
					data : ures.data,
					cellMinWidth : 60,
					height : "full-110",
					id : "costListTable",
					method: 'post',
					page:false,
					limit:ures['data'].length,  //不设置会只显示10条
					cols : [[
						{field: 'DEPARTMENT_NAME', title: '企业名称', minWidth:160, align:"center", rowspan:2},
						{field: 'EXPENSE_TOTAL', title: "合计<br>（万元）", minWidth:100, align:"center", rowspan:2},
						{field: 'SUPPLEMENTARY_MEDICAL', title: "在成本中列支的<br>补充医疗保险费<br>（万元）", width:140, minWidth:140, align:"center", 
							style:editable,event:"nine_number", edit:'text', rowspan:2},
						{title: "退休人员统筹外费用（万元）", align:"center", 
							style:editable,event:"nine_number", edit:'text', colspan:7}
					],[
						{field: 'SUBTOTAL', title: "小计", minWidth:100, align:"center"},
						{field: 'MONTHLY_LIVING_ALLOWANCE', title: "按月生活<br>补贴<br>（万元）",  minWidth:100, align:"center", 
							style:editable,event:"nine_number", edit:'text'},
						{field: 'ONE_TIME_LIVING_ALLOWANCE', title: "一次性生活<br>补贴<br>（万元）", minWidth:100, align:"center", 
							style:editable,event:"nine_number", edit:'text'},
						{field: 'MEDICAL_FEE', title: "医疗费<br>（体检费）<br>（万元）",  minWidth:100, align:"center", 
							style:editable,event:"nine_number", edit:'text'},
						{field: 'ACTIVITY_FUNDS', title: "活动经费<br>（万元）", minWidth:100, align:"center", 
							style:editable,event:"nine_number", edit:'text'},
						{field: 'SUBSIDIES_FOR_HEATING', title: "供暖费补贴<br>（万元）", minWidth:100, align:"center", 
							style:editable,event:"nine_number", edit:'text'},
						{field: 'OTHER_EXPENSES', title: "其他费用<br>（万元）", minWidth:100, align:"center", 
							style:editable,event:"nine_number", edit:'text'}
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
	    $("#formid").attr("action",excelRetireUserCorrelativeCharges).trigger("submit");
	});
	
	//修改
	table.on('edit(costList)', function(obj){
		var inputElem = $(this);
		var tdElem = inputElem.closest('td');
		var valueOld = inputElem.prev().text();
		var data = {};
		if(obj.value!="" && !/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(obj.value)){
			$(tdElem).find("input").val(valueOld);
			$(tdElem).find("div").html(valueOld);
			layer.msg("金额格式不正确", {icon: 2});
			return false;
		}else{
			var trElem = inputElem.closest('tr');
			//修改小计、合计值
			var MONTHLY_LIVING_ALLOWANCE = trElem.find("td:eq(4)").text();
			if(obj.field=='MONTHLY_LIVING_ALLOWANCE'){
				MONTHLY_LIVING_ALLOWANCE = obj.value;
			}
			var ONE_TIME_LIVING_ALLOWANCE = trElem.find("td:eq(5)").text();
			if(obj.field=='ONE_TIME_LIVING_ALLOWANCE'){
				ONE_TIME_LIVING_ALLOWANCE = obj.value;
			}
			var MEDICAL_FEE = trElem.find("td:eq(6)").text();
			if(obj.field=='MEDICAL_FEE'){
				MEDICAL_FEE = obj.value;
			}
			var ACTIVITY_FUNDS = trElem.find("td:eq(7)").text();
			if(obj.field=='ACTIVITY_FUNDS'){
				ACTIVITY_FUNDS = obj.value;
			}
			var SUBSIDIES_FOR_HEATING = trElem.find("td:eq(8)").text();
			if(obj.field=='SUBSIDIES_FOR_HEATING'){
				SUBSIDIES_FOR_HEATING = obj.value;
			}
			var OTHER_EXPENSES = trElem.find("td:eq(9)").text();
			if(obj.field=='OTHER_EXPENSES'){
				OTHER_EXPENSES = obj.value;
			}
			var subtotal = Number(MONTHLY_LIVING_ALLOWANCE) + Number(ONE_TIME_LIVING_ALLOWANCE) +
			Number(MEDICAL_FEE) + Number(ACTIVITY_FUNDS) + Number(SUBSIDIES_FOR_HEATING) + Number(OTHER_EXPENSES);
			inputElem.closest('tr').find("td:eq(3)").find("div").html(subtotal.toFixed(2));
			//合计
			var SUPPLEMENTARY_MEDICAL = trElem.find("td:eq(2)").text();
			if(obj.field=='SUPPLEMENTARY_MEDICAL'){
				SUPPLEMENTARY_MEDICAL = obj.value;
			}
			var total = Number(SUPPLEMENTARY_MEDICAL) + Number(subtotal.toFixed(2));
			inputElem.closest('tr').find("td:eq(1)").find("div").html(total.toFixed(2));
		}
		//修改值到数据库
 		var CostParam = {};
		CostParam.id = obj.data.ID;
		CostParam.unit_id = obj.data.UNIT_ID;
		CostParam.year = $("#year").val().replace("年",'');
		CostParam[obj.field.toLowerCase()] = obj.value;
		CostParam.modified_user_id = user['id'];
		common.ajax(saveRetireEmployeeCost, 'post', 'json', JSON.stringify(CostParam), function (res) {
			layer.msg(res.message);
		},"application/json;charset=UTF-8");
	});
	
	//监听单元格事件（修改文本框maxLength）
	table.on('tool(costList)', function(obj){
		var data = obj.data;
		var tr = obj.tr;
		if(obj.event === 'nine_number'){
			$(tr).find("input").attr("maxLength","9");
		}
	});
})