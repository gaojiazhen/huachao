layui.use(['form', 'layer', 'laydate', 'common', 'formSelects'],function(){
    var common = layui.common,
        form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        laydate = layui.laydate,
        $ = layui.jquery,
        formSelects = layui.formSelects;
    var	user = JSON.parse(sessionStorage.getItem("user"));
    $("#spouse_department_div").hide();

    //1、查人员信息（因为离退休人员登记页会直接跳转到些填写页）
    var user_id = getQueryString("user_id");
    $("#user_id").val(user_id);
    var userParam = {
        "user_id":user_id
    };

    //列表页已经查询过的数据字典
    var parentCodeList = parent.codeList;

    //根据编码获取数据字典名称
    function getCodeValue(code,value){
        var typeCodeList = parentCodeList[code];
        for(var i=0;i<typeCodeList.length;i++){
            if(value == typeCodeList[i].value){
                return typeCodeList[i].name;
            }
        }
    }


    common.ajax(getRetireCommunistByUserid, 'post', 'json', userParam, function (userRes){
        //1、离退休人员信息
        var retireUserDO = userRes.data.retireUserDO;
        $("input[name='modified_user_id']").val(user['id']);
        $("#user_name").html(retireUserDO.user_name);
        //$("#user_id").val(retireUserDO.user_id);

        // //2.1、性别单选项
        //  $("#sex_name").html(getDepartmentHtmlByRadio(parentCodeList[BASE_CODE_SEX],"sex_id","sexRadio"));
        $("#sex_name").html(getCodeValue(BASE_CODE_SEX,retireUserDO.sex_id));
        $("#idcard").html(retireUserDO.idcard);
        //2、中共党员信息
        var retireQuitDO = userRes.data.retireQuitDO;
        if (null!=retireQuitDO && retireQuitDO.id!=null && retireQuitDO.id!="") {
            $("input[name='id']").val(retireQuitDO.id);
            $("#monthly_income").val(retireQuitDO.monthly_income);
            $("#basic_expenses").val(retireQuitDO.basic_expenses);
            $("#province_subsidy").val(retireQuitDO.province_subsidy);
            $("#other_subsidy").val(retireQuitDO.other_subsidy);
            $("#self_employed_fee").val(retireQuitDO.self_employed_fee);
            $("#nursing_fee").val(retireQuitDO.nursing_fee);
            $("#car_fare").val(retireQuitDO.car_fare);
            $("#children_number").val(retireQuitDO.children_number);
            $("#raising_a_child_number").val(retireQuitDO.raising_a_child_number);
            $("input[name='spouse_situation'][value='"+retireQuitDO.spouse_situation+"']").attr("checked","checked");
            $("#spouse_situation").val(retireQuitDO.spouse_situation);
            $("#spouse_name").val(retireQuitDO.spouse_name);
            $("#spouse_birth_date").val(retireQuitDO.spouse_birth_date);
            $("input[name='spouse_is_work'][value='"+retireQuitDO.spouse_is_work+"']").attr("checked","checked");
            $("#spouse_is_work").val(retireQuitDO.spouse_is_work);
            $("#spouse_department_name").val(retireQuitDO.spouse_department_name);
            $("#spouse_settlement").val(retireQuitDO.spouse_settlement);
            $("#regular_subsidy").val(retireQuitDO.regular_subsidy);
            $("#spouse_contact").val(retireQuitDO.spouse_contact);
        }
        shwoSpouseInfo($("#spouse_situation").val());
        shwoSpouseDepartment($("#spouse_is_work").val());
        form.render();
    });

    //1、配偶状况
    form.on('radio(spouse_situation)', function(data){
        shwoSpouseInfo(data.value);
    });
    //2、配偶有无工作情况
    form.on('radio(spouse_is_work)', function(data){
        shwoSpouseDepartment(data.value);
    });
    //3、配偶出生年月
    laydate.render({
        elem: '#spouse_birth_date',
        theme: 'grid',
        trigger: 'click',
        max : 0
    });
    //监听【保存】
    form.on('submit(saveUser)', function(data){
        //实体类传参必须使用application/json;charset=UTF-8 + JSON.stringify(data.field)
        common.ajax(saveRetireQuit, 'post', 'json', JSON.stringify(data.field), function (res){
            if(Number(res.data) > 0){
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭
                layer.close(index);
            }
        },"application/json;charset=UTF-8");
        return false;
    });
    //关闭
    $("#closeWindow").click(function(){
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
        layer.close(index);
    });

    //自定义校验
    form.verify({
        money: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(value) || value==''){

            }else{
                return '金额格式不正确';
            }
        },
        sexRadio: function (value, item) {
            var sex_id = $("input[name='sex_id']:checked").val();
            if (typeof (sex_id) == "undefined") {
                return '性别不能为空';
            }
        },
        integer: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(value!="" && !/^[+]{0,1}(\d+)$/.test(value)){
                return '请输入正整数';
            }else{

            }
        },
        spouseSituationRadio: function (value, item) {
            var sex_id = $("input[name='spouse_situation']:checked").val();
            if (typeof (sex_id) == "undefined") {
                return '配偶状况不能为空';
            }
        }
    });

    //显示配偶信息
    function shwoSpouseInfo(value){
        console.log(value);
        if(value=='1'){
            $(".spouse_info").show();
            $(".spouse_info").find("input").attr("lay-verify","required");
        }else{
            $(".spouse_info").hide();
            $(".spouse_info").find("input").attr("lay-verify","");
        }
        //隐藏域不要加必填验证
        $("#spouse_situation").attr("lay-verify","");
        $("#spouse_is_work").attr("lay-verify","");
    }

    //显示配偶工作单位
    function shwoSpouseDepartment(value){
        if(value=='1'){
            $("#spouse_department_div").show();
            $("#spouse_department_div").find("input").attr("lay-verify","required");
            $("#regular_subsidy").attr("lay-verify","required|money");
        }else{
            $("#spouse_department_div").hide();
            $("#spouse_department_div").find("input").attr("lay-verify","");
            $("#regular_subsidy").attr("lay-verify","money");
        }
    }
})