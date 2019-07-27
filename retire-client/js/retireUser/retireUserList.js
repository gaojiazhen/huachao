//与新增、修改页共用的数据字典列表
var codeList;
layui.use(['form','layer','laydate','table','common','laypage'],function(){	
	var common = layui.common,
		form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
		laypage = layui.laypage,
        table = layui.table;
	var	user = JSON.parse(sessionStorage.getItem("user"));
	
	var codeParam = {code:BASE_CODE_SEX + "," + BASE_CODE_MARITAL_STATUS + "," + BASE_CODE_EDUCATION + "," +
			BASE_CODE_POLITICS_STATUS + "," + BASE_CODE_NATURE+","+BASE_DEPARTMENT+","+BASE_CODE_NATION
			+","+ BASE_CODE_TYPE1 +","+ BASE_CODE_SPECIAL_CROWD +","+BASE_CODE_RETIREMENT_RANK +","+
			BASE_CODE_RECEIVE_AREA +","+ BASE_CODE_PLAY_A_ROLE +","+ BASE_CODE_AWARD_LEVEL +","+
            BASE_CODE_NOW_TREATMENT_LEVEL +","+ BASE_CODE_ARCHIVES_AREA +","+ BASE_CODE_HEALTH_STATUS
    };
	common.ajax(getCode, 'post', 'json', codeParam, function (codeRes){
		codeList = codeRes.data;
        Action();
	});

	//监听下拉，为了后续取值
	form.on('select(unit_id)', function(data){
		$("#unit_id").val(data.value);
	});

	//选择离退开始日期
	laydate.render({
	    elem: '#start_retire_time',
		theme: 'grid',
	    trigger: 'click',
	    max : 0,
		done: function(value, date, endDate){
			var startdate = new Date($("#start_retire_time").val().replace(/\-/g,'/'));
			var enddate = new Date($("#end_retire_time").val().replace(/\-/g,'/'));
			if(startdate > enddate){
				$("#start_retire_time").val("");
				layer.msg("开始日期超出了结束日期<br>建议重新选择");
				return;
			}
		}
	});
	//选择离退结束日期
	laydate.render({
	    elem: '#end_retire_time',
		theme: 'grid',
	    trigger: 'click',
	    max : 0,
		done: function(value, date, endDate){
			var startdate = new Date($("#start_retire_time").val().replace(/\-/g,'/'));
			var enddate = new Date($("#end_retire_time").val().replace(/\-/g,'/'));
			if(startdate > enddate){
				$("#end_retire_time").val("");
				layer.msg("开始日期超出了结束日期<br>建议重新选择");
				return;
			}
		}
	});

	var userParam = {};
    //初始方法
	function Action(){

		var departmentParam = {'parent_id':BASE_DEPARTMENT,'department_id':user['unit_id']};
		common.ajax(listBaseDepartment, 'post', 'json', departmentParam, function (dres) {
			//单位名称
			var departmentHtml = getDepartmentHtml(dres.data,$('#unit_id').val());
			//性别
			var sexIdHtml = getCodeHtmlByOption(codeList[BASE_CODE_SEX],$('#sex_id').val());
			//民族
			var nationIdHtml = getCodeHtmlByOption(codeList[BASE_CODE_NATION],$('#nation_id').val());
			//婚姻状况
			var maritalIdHtml = getCodeHtmlByOption(codeList[BASE_CODE_MARITAL_STATUS],$('#marital_status_id').val());
			//最高学历
			var educationIdHtml = getCodeHtmlByOption(codeList[BASE_CODE_EDUCATION],$('#education_id').val());
			//政治面貌
			var politicsIdHtml = getCodeHtmlByOption(codeList[BASE_CODE_POLITICS_STATUS],$('#politics_status_id').val());
			//离退休类型
			var retireTypeIdHtml = getCodeHtmlByOption(codeList[BASE_CODE_TYPE1],$('#retire_type_id').val());
			//退休性质
			var retireNatureIdHtml = getCodeHtmlByOption(codeList[BASE_CODE_NATURE],$('#retire_nature_id').val());
			//离退休职级
			var retirementRankIdHtml = getCodeHtmlByOption(codeList[BASE_CODE_RETIREMENT_RANK],$('#retirement_rank_id').val());
			//现享受待遇级别
			var nowTreatmentLevelIdHtml = getCodeHtmlByOption(codeList[BASE_CODE_NOW_TREATMENT_LEVEL],$('#now_treatment_level_id').val());
            //人事档案存放地
			var archivesAreaIdHtml = getCodeHtmlByOption(codeList[BASE_CODE_ARCHIVES_AREA],$('#archives_area_id').val());
			//接收地
			var receiveAreaIdHtml = getCodeHtmlByOption(codeList[BASE_CODE_RECEIVE_AREA],$('#receive_area_id').val());
			//健康状况
			var healthStatusIdHtml = getCodeHtmlByOption(codeList[BASE_CODE_HEALTH_STATUS],$('#health_status').val());

			//多条数据才加“全部”选项，地市级单位只看自己公司的数据
			if(dres.data.length > 1){
				departmentHtml = "<option value=''>全部</option>" + departmentHtml;
			}
			//性别
			if(codeList[BASE_CODE_SEX].length > 1){
                sexIdHtml = "<option value=''>性别</option>" + sexIdHtml;
            }
            //民族
            if(codeList[BASE_CODE_NATION].length > 1){
                nationIdHtml = "<option value=''>民族</option>" + nationIdHtml;
            }
            //婚姻状况
            if(codeList[BASE_CODE_NATION].length > 1){
                maritalIdHtml = "<option value=''>婚姻状况</option>" + maritalIdHtml;
            }
            //最高学历
            if(codeList[BASE_CODE_NATION].length > 1){
                educationIdHtml = "<option value=''>最高学历</option>" + educationIdHtml;
            }
            //政治面貌
            if(codeList[BASE_CODE_NATION].length > 1){
                politicsIdHtml = "<option value=''>政治面貌</option>" + politicsIdHtml;
            }
            //离退休类型
            if(codeList[BASE_CODE_TYPE1].length > 1){
                retireTypeIdHtml = "<option value=''>离退休类型</option>" + retireTypeIdHtml;
            }
            //退休性质
            if(codeList[BASE_CODE_NATURE].length > 1){
                retireNatureIdHtml = "<option value=''>退休性质</option>" + retireNatureIdHtml;
            }
            //离退休职级
            if(codeList[BASE_CODE_RETIREMENT_RANK].length > 1){
                retirementRankIdHtml = "<option value=''>离退休职级</option>" + retirementRankIdHtml;
            }
            //现享受待遇级别
            if(codeList[BASE_CODE_NOW_TREATMENT_LEVEL].length > 1){
                nowTreatmentLevelIdHtml = "<option value=''>现享受待遇级别</option>" + nowTreatmentLevelIdHtml;
            }
            //人事档案存放地
            if(codeList[BASE_CODE_NOW_TREATMENT_LEVEL].length > 1){
                archivesAreaIdHtml = "<option value=''>人事档案存放地</option>" + archivesAreaIdHtml;
            }
            //接收地
            if(codeList[BASE_CODE_RECEIVE_AREA].length > 1){
                receiveAreaIdHtml = "<option value=''>接收地</option>" + receiveAreaIdHtml;
            }
            //健康状况
            if(codeList[BASE_CODE_HEALTH_STATUS].length > 1){
                healthStatusIdHtml = "<option value=''>健康状况</option>" + healthStatusIdHtml;
            }

			$('#unit_id').html(departmentHtml);
			$('#sex_id').html(sexIdHtml);
			$('#nation_id').html(nationIdHtml);
			$('#marital_status_id').html(maritalIdHtml);
			$('#education_id').html(educationIdHtml);
			$('#politics_status_id').html(politicsIdHtml);
			$('#retire_type_id').html(retireTypeIdHtml);
			$('#retire_nature_id').html(retireNatureIdHtml);
			$('#retirement_rank_id').html(retirementRankIdHtml);
			$('#now_treatment_level_id').html(nowTreatmentLevelIdHtml);
			$('#archives_area_id').html(archivesAreaIdHtml);
			$('#receive_area_id').html(receiveAreaIdHtml);
			$('#health_status').html(healthStatusIdHtml);
			form.render('select');
			//加载列表
			common.ajax(listRetireUserByPaging, 'post', 'json', userParam, function (ures) {
				var tableIns = table.render({
					elem : '#userList',
					data : ures.data,
					cellMinWidth : 60,
					height : "full-160",
					limit : ures.pageSize,
					id : "userListTable",
					method: 'post',
					cols : [[
						{title: '序号', width:60,minWidth:60,align:"center", templet: function(d){
							return d.LAY_TABLE_INDEX + 1 + (ures.pageNum * ures.pageSize) - ures.pageSize;
						}},
						{field: 'user_name', title: '姓名', width:90,minWidth:90,align:"center",sort:true},
						{title: '性别', width:60,minWidth:60, align:"center",templet: function (d) {
								return getCodeValue(BASE_CODE_SEX,d.sex_id);
						}},
						{title: '婚姻状况', width:90,minWidth:90, align:"center",templet: function (d) {
								return getCodeValue(BASE_CODE_MARITAL_STATUS,d.marital_status_id);
						}},
						{field: 'idcard', title: '身份证号码', width:175,minWidth:175, align:"center",sort:true},
						{title: '学历', width:100,minWidth:100, align:"center",templet: function (d) {
								return getCodeValue(BASE_CODE_EDUCATION,d.education_id);
						}},
						{field: 'work_time', title: '参加工作', width:105,minWidth:105, align:"center",sort:true},
						{field: 'retire_time', title: '退休时间', width:105,minWidth:105, align:"center",sort:true},
						{title: '政治面貌', minWidth:90, align:"center",templet: function (d) {
								return getCodeValue(BASE_CODE_POLITICS_STATUS,d.politics_status_id);
						}},
						{field: 'modified_user_name', title: '修改人', width:90,minWidth:90,align:"center"},
						{field: 'gmt_modified', title: '修改时间', width:145,minWidth:145, align:"center",sort:true},
						{title: '操作', width:115,minWidth:115, templet:'#userListBar',fixed:"right",align:"center"}
					]]
				});
				laypage.render({
					elem: 'pagediv',
					count: ures.total, //数据总数，从服务端得到
					limit: ures.pageSize,
					curr: ures.pageNum,
					layout: ['prev', 'page', 'next', 'count', 'limit', 'refresh', 'skip'],
					jump: function(obj, first){
						//首次加载列表不执行
						if(!first){
							userParam['pageNum'] = obj.curr;    //得到当前页，以便向服务端请求对应页的数据。
							userParam['pageSize'] = obj.limit;  //得到每页显示的条数
							Action();
						}
					}
				});
				//监听表格排序
				table.on('sort(userList)', function(obj){ 
					userParam['sortField'] = obj.field; //当前排序的字段名
					userParam['sortType'] = obj.type; 	//当前排序类型：desc（降序）、asc（升序）、null（空对象，默认排序）
					Action();
				}); 
				form.render();
			});
		});
		form.render();
	}
	//根据编码获取数据字典名称
	function getCodeValue(code,value){
		var typeCodeList = codeList[code];
		for(var i=0;i<typeCodeList.length;i++){
			if(value == typeCodeList[i].value){
				return typeCodeList[i].name;
			}
		}	
	}
    //搜索
    $(".search_btn").on("click",function(){
		userParam = getformObj($("#formid").serializeArray());
		Action();
    });
	//添加
	$("#addUser").click(function(){
	    editUser();
	});
	//跳转到导入页
	$("#impExcel").click(function(){
	    var index = layui.layer.open({
	        title : "导入Excel",
			area: ['600px', '400px'],
	        type : 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
	        content : "retireUserExp.html",
	        success : function(layero, index){
	            var body = layui.layer.getChildFrame('body', index);
	    		body.find("#unit_id").val($("#unit_id").val());
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
    //添加、修改的弹窗
    function editUser(obj){
		var open_title = "添加";
		if(obj != null){
			open_title = "修改";
		}
        var index = layui.layer.open({
            title : open_title,
            type : 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
            content : "retireUserEdit.html",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
				//有值为编辑否则为修改
				body.find("input[name='modified_user_id']").val(user['id']);
				if(obj){
					body.find("input[name='id']").val(obj.id);
					body.find("#unit_id").val(obj.unit_id);
					body.find("input[name='user_name']").val(obj.user_name);
					body.find("#sex_id").val(obj.sex_id);
					body.find("#birth_date").val(obj.birth_date);
					body.find("#nation_id").val(obj.nation_id);
					body.find("input[name='native_place']").val(obj.native_place);
					body.find("#marital_status_id").val(obj.marital_status_id);
					body.find("#education_id").val(obj.education_id);
					body.find("#idcard").val(obj.idcard);
					body.find("#registered_permanent_residence").val(obj.registered_permanent_residence);
					body.find("#residence_address").val(obj.residence_address);
					body.find("#work_time").val(obj.work_time);
					body.find("#retire_time").val(obj.retire_time);
					body.find("#politics_status_id").val(obj.politics_status_id);
					body.find("#politics_status_special_mark").val(obj.politics_status_special_mark);
					body.find("#retire_type_id").val(obj.retire_type_id);
					body.find("#retire_type_id_special_mark").val(obj.retire_type_id_special_mark);
					body.find("#retire_nature_id").val(obj.retire_nature_id);

					body.find("input[name='for_retirees']").val(obj.for_retirees);   //离退休岗位及职务
					body.find("#technical_posts").val(obj.technical_posts );			  //专业技术职务

					body.find("#retirement_rank_id").val(obj.retirement_rank_id);
					body.find("#now_treatment_level_id").val(obj.now_treatment_level_id);
					body.find("#treatment_approval_time").val(obj.treatment_approval_time);
					body.find("#treatment_approval_typenumber").val(obj.treatment_approval_typenumber); //待遇批准文号
					body.find("#retirement_settlement").val(obj.retirement_settlement);
					body.find("input[name='is_earthly_place'][value='"+obj.is_earthly_place+"']").attr("checked","checked");
					body.find("input[name='is_beijing'][value='"+obj.is_beijing+"']").attr("checked","checked");
					body.find("input[name='is_socialized_pension'][value='"+obj.is_socialized_pension+"']").attr("checked","checked");
					body.find("input[name='child_working_sys'][value='"+obj.child_working_sys+"']").attr("checked","checked");
					body.find("input[name='is_medical_insurance'][value='"+obj.is_medical_insurance+"']").attr("checked","checked");
					body.find("#bank_card_number").val(obj.bank_card_number);
					body.find("input[name='contact_number']").val(obj.contact_number);
					body.find("input[name='social_security_area']").val(obj.social_security_area);
					body.find("#archives_area_id").val(obj.archives_area_id);
					body.find("#receive_area_id").val(obj.receive_area_id);
					body.find("#receive_area_address").val(obj.receive_area_address);
					body.find("#archives_book_number").val(obj.archives_book_number);
					body.find("#health_status").val(obj.health_status);
					body.find("#health_status_special_mark").val(obj.health_status_special_mark);
					body.find("#health_status_details").val(obj.health_status_details);
					body.find("#deceased_time").val(obj.deceased_time);
					body.find("#play_a_role_ids").val(obj.play_a_role_ids);
					body.find("#play_a_role_specific").val(obj.play_a_role_specific);
					body.find("#play_a_role_ids_special_mark").val(obj.play_a_role_ids_special_mark);
					body.find("input[name='is_system_employees'][value='"+obj.is_system_employees+"']").attr("checked","checked");
					body.find("#is_system_employees").val(obj.is_system_employees);
					body.find("input[name='spouse_name']").val(obj.spouse_name);

					body.find("#spouse_unit").val(obj.spouse_unit);    //配偶退休单位选择

					body.find("input[name='spouse_duty']").val(obj.spouse_duty);
					body.find("input[name='spouse_contact']").val(obj.spouse_contact);
					body.find("input[name='is_model_personnel'][value='"+obj.is_model_personnel+"']").attr("checked","checked");
					body.find("#is_model_personnel").val(obj.is_model_personnel);
					body.find("input[name='honor']").val(obj.honor);
					body.find("input[name='prizes_department']").val(obj.prizes_department);
					body.find("#award_level_id").val(obj.award_level_id);
					body.find("input[name='is_soldier_cadre'][value='"+obj.is_soldier_cadre+"']").attr("checked","checked");
					body.find("#is_soldier_cadre").val(obj.is_soldier_cadre);
					body.find("input[name='military_level']").val(obj.military_level);
					body.find("#special_crowd_ids").val(obj.special_crowd_ids);
					body.find("textarea[name='special_remark']").val(obj.special_remark);
					body.find("#phone").val(obj.phone);
					body.find("#area_code").val(obj.area_code);
					body.find("#landline").val(obj.landline);
				}else{
					body.find("input[name='create_user_id']").val(user['id']);
					body.find("#unit_id").val($("#unit_id").val());
                    body.find("#spouse_unit").val($("#spouse_unit").val());
				}
				form.render();
                setTimeout(function(){
                    layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            },
			end: function(index, layero){
				Action();
			}
        });
        layui.layer.full(index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(index);
        });
    }
    //列表操作
    table.on('tool(userList)', function(obj){
        var layEvent = obj.event,
			data = obj.data;
        if(layEvent === 'del'){ //删除
            layer.confirm("确定删除【" + data.user_name + "】的离退休信息？",{icon:3, title:'提示信息'},function(index){
				var dparam= {'ids':data.id} ;
				common.ajax(removeRetireUser, 'post', 'json', dparam, function (res) {
					if(res.data){
						obj.del();
						layer.msg("删除成功!");
					}else{
						layer.msg("网络异常!");
					}
				})
             })
        }else if(layEvent === 'update'){ //修改
			editUser(data);
		}
    });
});