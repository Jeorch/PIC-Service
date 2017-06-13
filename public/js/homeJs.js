/**
 * Created by yym on 6/13/17.
 */
var pageTypeIndex = "index1Page";
var userName = $.cookie("user_name")
var list = null;
var temp = false;

$(document).ready(function () {

    $('#timeTab a').click(function (e) {
        e.preventDefault()

        $(this).tab('show')
    });
    $(".userName").text(userName);
    $("#jxname").text(userName.substring(userName.length - 1));

    $("#en").click(function () {
        if (!temp) {
            myAlert();
        }
        temp = true;
    });

    $("#en").click(function () {
        if (!temp) {
            myAlert();
        }
        temp = true;
    });
    $(".mCustomScrollbar").mCustomScrollbar({
        theme: "minimal-dark",
        scrollEasing: "easeOutCirc",
        scrollInertia: 400
    });
    showleft()

    //加载页面直接调用calcPercentage(),之后会撤掉
    calcPercentage();
    calcTrend();
});

$("#userInfo").click(function () {
    var token=$.cookie("token")
    var d=JSON.stringify({
        "token":token
    })
    $.ajax({
        type: "POST",
        url: "/auth/checkAuthToken",
        data: d,
        contentType: "application/json,charset=utf-8",
        error: function(request) {
            window.location="@routes.PagesController.login()"

        },
        success: function(data) {
            if(data.status == "ok") {
                $.cookie("screen_name",data.result.auth.screen_name);
                $.cookie("email",data.result.auth.email);
                $.cookie("phoneNo",data.result.phoneNo);
                $.cookie("screen_photo",data.result.screen_photo);
                window.location="/userInfo"
            }else {
                window.location="@routes.PagesController.login()"

            }
        }
    })
})

function showleft(){
    var d = JSON.stringify({
        "test" : "a"
    })

    $.ajax({
        type: "POST",
        url: "/showConfig",
        data: d,
        contentType: "application/json,charset=utf-8",
        error: function(request) {


        },
        success: function(data) {
            if(data.status == "ok") {

                var pro=data.result.info[0].province
                var lev=data.result.info[0].category
                getProvince("province","区域",pro)
                getLel_one("ATC1","治疗I",lev)
                getLel_two("ATC2","治疗II",lev)
                getLel_thr("ATC3","治疗III",lev)
                //无数据
                showLeftInfo("genericnameinfo","通用名",data)
                showLeftInfo("product","商品名",data)
                showLeftInfo("manufacturetype","生产厂家类型",data)
                showLeftInfo("manufacture","生产厂家",data)
                showLeftInfo("dosage","剂型",data)
                showLeftInfo("specification","规格",data)
                showLeftInfo("package","包装",data)
//                            showLeftInfo("province","区域",pro,getProvince("province",pro))
//                            showLeftInfo("ATC1","治疗I",lev,getDes("ATC1",lev,0))
//                            showLeftInfo("ATC2","治疗I",lev,getDes("ATC2",lev,1))
//                            showLeftInfo("ATC3","治疗I",lev,getDes("ATC3",lev,2))

            }
        }
    })

}
//控制左
function getProvince(btn,info,res) {
    var selectObj = $("#"+btn);
    //设置Select2的处理
    selectObj.select2({
        language : 'zh-CN',
        maximumInputLength:100,//限制最大字符，以防坑货
        placeholder: info,
        allowClear: true,
        escapeMarkup: function (m) { return m; }
    });
    selectObj.empty();//清空下拉框
    selectObj.append("<option value=''>info</option>");
    $.each(res,function (i,item) {
        selectObj.append("<option value="+ item +">" + item + "</option>")
    })
}

function getLel_one(btn,info,res) {
    var selectObj = $("#"+btn);
    //设置Select2的处理
    selectObj.select2({
        language : 'zh-CN',
        maximumInputLength:100,//限制最大字符，以防坑货
        placeholder: info,
        allowClear: true,
        escapeMarkup: function (m) { return m; }
    });
    selectObj.empty();
    selectObj.append("<option value=''>info</option>");
    $.each(res,function(i,item){
        if(item.level==0){
            console.log(item.des)
            selectObj.append("<option value="+ item.des +">" + item.des + "</option>")
        }

    })
}

function getLel_two(btn,info,res) {

    var selectObj = $("#"+btn);
    //设置Select2的处理
    selectObj.select2({
        language : 'zh-CN',
        maximumInputLength:100,//限制最大字符，以防坑货
        placeholder: info,
        allowClear: true,
        escapeMarkup: function (m) { return m; }
    });
    selectObj.empty();//清空下拉框
    selectObj.append("<option value=''>info</option>");
    $.each(res,function(i,item){
        if(item.level==1)
            selectObj.append("<option value="+ item +">" + item.des + "</option>")
    })
}
function getLel_thr(btn,info,res) {

    var selectObj = $("#"+btn);
    //设置Select2的处理
    selectObj.select2({
        language : 'zh-CN',
        maximumInputLength:100,//限制最大字符，以防坑货
        placeholder: info,
        allowClear: true,
        escapeMarkup: function (m) { return m; }
    });
    selectObj.empty();//清空下拉框
    selectObj.append("<option value=''>info</option>");
    $.each(res,function(i,item){
        if(item.level==2)
            selectObj.append("<option value="+ item +">" + item.des + "</option>")
    })
}
function showLeftInfo(btn,info,res) {
    var selectObj = $("#"+btn);
    //设置Select2的处理
    selectObj.select2({
        language : 'zh-CN',
        maximumInputLength:100,//限制最大字符，以防坑货
        placeholder: info,
        allowClear: true,
        escapeMarkup: function (m) { return m; }
    });
    selectObj.empty();//清空下拉框
    selectObj.append("<option value=''>info</option>");

}
// function showLeftInfo(btn,info,res,fun) {
//
//     var selectObj = $("#"+btn);
//     //设置Select2的处理
//     selectObj.select2({
//         language : 'zh-CN',
//         maximumInputLength:100,//限制最大字符，以防坑货
//         placeholder: info,
//         allowClear: true,
//         escapeMarkup: function (m) { return m; }
//     });
//     selectObj.empty();//清空下拉框
//     selectObj.append("<option value=''>info</option>");
//     fun(btn,res)
//
// }
// function getProvince(btn,res){
//     var selectObj = $("#"+btn);
//     $.each(res,function (i,item) {
//         selectObj.append("<option value="+ item +">" + item + "</option>")
//     })
// }
// function getDes(btn,res,f) {
//     var selectObj = $("#"+btn);
//     $.each(res,function(i,item){
//         if(res.level==f)
//             selectObj.append("<option value="+ item +">" + item.des + "</option>")
//     })
// }

//省份
