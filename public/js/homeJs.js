/**
 * Created by yym on 6/13/17.
 */
var pageTypeIndex = "index1Page";
var userName = $.cookie("user_name")
var list = null;
var temp = false;
$('.timepk_year').datetimepicker({
    language: 'zh-CN',
    format: "yyyy",
    weekStart: 1,
    todayBtn: true,
    autoclose: true,
    todayHighlight: 1,
    startView: 4,
    minView: 4,
    forceParse: 0
});

$('.timepk_month').datetimepicker({
    language: 'zh-CN',
    format: "yyyy-mm",
    weekStart: 1,
    todayBtn: 1,
    autoclose: 1,
    todayHighlight: 1,
    startView: 3,
    minView: 3,
    forceParse: 0
});
$(document).ready(function () {
    $("#tabYear").click(function () {
        $("#timeType").val(1);
        $("#monthInputb").val("");
        $("#quarterInput").val("");
        $("#quarterSelect").prev().children().eq(0).children().eq(0).children().click();
    });

    $("#tabMonth").click(function () {
        $("#timeType").val(2);
        $("#yearInputb").val("");
        $("#quarterInput").val("");
        $("#quarterSelect").prev().children().eq(0).children().eq(0).children().click();
    });

    $("#tabQuarter").click(function () {
        $("#timeType").val(3);
        $("#monthInputb").val("");
        $("#yearInputb").val("");
    });

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


    //加载页面直接调用,之后会撤掉
    var token=$.cookie("token")
    var tempD=JSON.stringify({
        "token":token,
        "condition":{
            "category":["他汀类"],

            "date":{
                "end":"2017-12",
                "start":"2016-01"
            }
        }
    })

    calcMarket(tempD)
    calcPercentage();
    calcTrend();

});

$("#userInfo").click(function () {
    var token = $.cookie("token")
    var d = JSON.stringify({
        "token": token
    })
    $.ajax({
        type: "POST",
        url: "/auth/checkAuthToken",
        data: d,
        contentType: "application/json,charset=utf-8",
        error: function (request) {
            window.location = "@routes.PagesController.login()"

        },
        success: function (data) {
            if (data.status == "ok") {
                $.cookie("screen_name", data.result.auth.screen_name);
                $.cookie("email", data.result.auth.email);
                $.cookie("phoneNo", data.result.phoneNo);
                $.cookie("screen_photo", data.result.screen_photo);
                window.location = "/userInfo"
            } else {
                window.location = "@routes.PagesController.login()"

            }
        }
    })
})

function showleft() {
    var d = JSON.stringify({
        "test": "a"
    })

    $.ajax({
        type: "POST",
        url: "/showConfig",
        data: d,
        contentType: "application/json,charset=utf-8",
        error: function (request) {


        },
        success: function (data) {
            if (data.status == "ok") {

                var pro = data.result.info[0].province
                var lev = data.result.info[0].category
                getProvince("province", "区域", pro)
                getLel_one("ATC1", "治疗I", lev)
                getLel_two("ATC2", "治疗II", lev)
                getLel_thr("ATC3", "治疗III", lev)
                //无数据
                showLeftInfo("genericnameinfo", "通用名", data)
                showLeftInfo("product", "商品名", data)
                showLeftInfo("manufacturetype", "生产厂家类型", data)
                showLeftInfo("manufacture", "生产厂家", data)
                showLeftInfo("dosage", "剂型", data)
                showLeftInfo("specification", "规格", data)
                showLeftInfo("package", "包装", data)
//                            showLeftInfo("province","区域",pro,getProvince("province",pro))
//                            showLeftInfo("ATC1","治疗I",lev,getDes("ATC1",lev,0))
//                            showLeftInfo("ATC2","治疗I",lev,getDes("ATC2",lev,1))
//                            showLeftInfo("ATC3","治疗I",lev,getDes("ATC3",lev,2))

            }
        }
    })

}
//控制左
function getProvince(btn, info, res) {
    var selectObj = $("#" + btn);
    //设置Select2的处理
    selectObj.select2({
        language: 'zh-CN',
        maximumInputLength: 100,//限制最大字符，以防坑货
        placeholder: info,
        allowClear: true,
        escapeMarkup: function (m) {
            return m;
        }
    });
    selectObj.empty();//清空下拉框
    selectObj.append("<option value=''>info</option>");
    $.each(res, function (i, item) {
        selectObj.append("<option value=" + item + ">" + item + "</option>")
    })
}

function getLel_one(btn, info, res) {
    var selectObj = $("#" + btn);
    //设置Select2的处理
    selectObj.select2({
        language: 'zh-CN',
        maximumInputLength: 100,//限制最大字符，以防坑货
        placeholder: info,
        allowClear: true,
        escapeMarkup: function (m) {
            return m;
        }
    });
    selectObj.empty();
    selectObj.append("<option value=''>info</option>");
    $.each(res, function (i, item) {
        if (item.level == 0) {
            selectObj.append("<option value=" + item.des + ">" + item.des + "</option>")
        }

    })
}

function getLel_two(btn, info, res) {

    var selectObj = $("#" + btn);
    //设置Select2的处理
    selectObj.select2({
        language: 'zh-CN',
        maximumInputLength: 100,//限制最大字符，以防坑货
        placeholder: info,
        allowClear: true,
        escapeMarkup: function (m) {
            return m;
        }
    });
    selectObj.empty();//清空下拉框
    selectObj.append("<option value=''>info</option>");
    $.each(res, function (i, item) {
        if (item.level == 1)
            selectObj.append("<option value=" + item.des + ">" + item.des + "</option>")
    })
}
function getLel_thr(btn, info, res) {

    var selectObj = $("#" + btn);
    //设置Select2的处理
    selectObj.select2({
        language: 'zh-CN',
        maximumInputLength: 100,//限制最大字符，以防坑货
        placeholder: info,
        allowClear: true,
        escapeMarkup: function (m) {
            return m;
        }
    });
    selectObj.empty();//清空下拉框
    selectObj.append("<option value=''>info</option>");
    $.each(res, function (i, item) {
        if (item.level == 2)
            selectObj.append("<option value=" + item.des + ">" + item.des + "</option>")
    })
}
function showLeftInfo(btn, info, res) {
    var selectObj = $("#" + btn);
    //设置Select2的处理
    selectObj.select2({
        language: 'zh-CN',
        maximumInputLength: 100,//限制最大字符，以防坑货
        placeholder: info,
        allowClear: true,
        escapeMarkup: function (m) {
            return m;
        }
    });
    selectObj.empty();//清空下拉框
    selectObj.append("<option value=''>info</option>");

}
//选项框控制
function showDig() {
    var y = 1;
    var m = 0;
    var endYear
    var endMonth
    var startYear
    var startMonth
    var start= "2016-01"
    var end = "2017-01"
    var flag=1
    if ($("#tabYear").click(function () {
            flag=1
        }))

    if ($("#tabMonth").click(function () {
        flag=2
     }))
    console.log(flag)
    //日期
    var y=$("#yearInputb").val()
    var m=$("#monthInputb").val()
    if(y.length!=0){
        startYear=Number(y)-1
        endYear=y
        start = startYear + "-" + "01"
        end = endYear + "-" + "01"
    }
    if(m.length!=0){
        end=m
        endYear=m.split("-")[0]
        endMonth=m.split("-")[1]
        startYear=Number(endYear)-1
        startMonth=endMonth
        start=startYear+"-"+startMonth
    }
    //省份
    var pro = $("#province").val()
    var lel_one = $("#ATC1").val()
    var lel_two = $("#ATC2").val()
    var lel_thr = $("#ATC3").val()
    var tmp = new Array(lel_one, lel_two, lel_thr)
    var lel = new Array()
    var j = 0

    for (var i = 0; i < tmp.length; i++) {
        if (tmp[i].length != 0) {
            lel[j] = tmp[i]
            j++
        }
    }

    var edge = new Array()
    edge[0] = pro

    var token = $.cookie("token")
    var d = JSON.stringify({
        "token": token,
        "condition": {
            "category": lel,
            "edge": edge,
            "date": {
                "start": start,
                "end": end
            }
        }

    })
    //console.log(d)
    calcMarket(d)
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
