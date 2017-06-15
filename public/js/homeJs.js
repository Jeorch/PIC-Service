/**
 * Created by yym on 6/13/17.
 */
var pageTypeIndex = "homePage";
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
        "test": "no data"
    })

    $.ajax({
        type: "POST",
        url: "/showConfig",
        data: d,
        contentType: "application/json,charset=utf-8",
        success: function (data) {
            if (data.status == "ok") {

                var pro = data.result.info[0].province
                var lev = data.result.info[0].category
                getProvince("province", "区域", pro)
                getLel_one("ATC1", "治疗I", lev)
                getLel_two("ATC2", "治疗II", lev)
                getLel_thr("ATC3", "治疗III", lev)
                //无数据

                showLeftInfo("genericnameinfo", "通用名",data)
                showLeftInfo("product", "商品名",data)


                showLeftInfo("manufacture", "生产厂家", data.result.info[0].manufacture)
                showLeftInfo("manufacturetype", "生产厂商类型",eval(["内资","合资"]))
                showLeftInfo("dosage", "剂型", data.result.info[0].product_type)
                showLeftInfo("specification", "规格", data.result.info[0].specifications)
                showLeftInfo("package", "包装", data.result.info[0].package)

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
    $.each(res, function (i, item) {
        selectObj.append("<option value=" + item + ">" + item+ "</option>")
    })
}
//选项框控制
function showDig() {
    showDataList()
    showDataGather()
}
