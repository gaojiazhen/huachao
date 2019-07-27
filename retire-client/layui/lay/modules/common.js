layui.define(['jquery'], function(exports){ 
     var $ = layui.jquery;
    var obj = {
        ax: function(url, data, async, type, dataType, successfn, errorfn) {
        async = (async==null || async=="" || typeof(async)=="undefined")? "true" : async;
        type = (type==null || type=="" || typeof(type)=="undefined")? "post" : type;
        dataType = (dataType==null || dataType=="" || typeof(dataType)=="undefined")? "json" : dataType;
        data = (data==null || data=="" || typeof(data)=="undefined")? {"date": new Date().getTime()} : data;
        $.ajax({
            type: type,
            async: async,
            data: data,
            url: url,
            dataType: dataType,
            success: function(d){
                successfn(d);
            },
            error: function(e){
                errorfn(e);
            }
        });
    },
	axs: function(url, data, successfn) {
        data = (data==null || data=="" || typeof(data)=="undefined")? {"date": new Date().getTime()} : data;
        $.ajax({
            type: "post",
            data: data,
            url: url,
            dataType: "json",
            success: function(d){
                successfn(d);
            }
        });
    },
	
	axse:function(url, data, successfn, errorfn) {
		    data = (data==null || data=="" || typeof(data)=="undefined")? {"date": new Date().getTime()} : data;
		    $.ajax({
		        type: "post",
		        data: data,
		        url: url,
		        dataType: "json",
		        success: function(d){
		            successfn(d);
		        },
		        error: function(e){
		            errorfn(e);
		        }
		    });
		}
	
    };
  exports('common', obj);
})
    //输出接口
  

 