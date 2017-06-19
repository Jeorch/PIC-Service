/**
 * Created by yym on 6/13/17.
 */
var userName = $.cookie("user_name")
var searchCount = 0;
$('.timepk_year').datetimepicker({
    language: 'zh-CN', format: "yyyy", weekStart: 1,
    todayBtn: true, autoclose: true, todayHighlight: 1,
    startView: 4, minView: 4, forceParse: 0
});

$('.timepk_month').datetimepicker({
    language: 'zh-CN', format: "yyyy-mm", weekStart: 1,
    todayBtn: 1, autoclose: 1, todayHighlight: 1,
    startView: 3, minView: 3, forceParse: 0
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


    $(".mCustomScrollbar").mCustomScrollbar({
        theme: "minimal-dark",
        scrollEasing: "easeOutCirc",
        scrollInertia: 400
    });

    showAtcInfo()
    showleft()
});

$("#userInfo").click(function () {
    var token = $.cookie("token")
    var data = JSON.stringify({
        "token": token
    });

    ajaxData("/auth/checkAuthToken", data, "POST", function(){
        if (data.status == "ok") {
            $.cookie("screen_name", data.result.auth.screen_name);
            $.cookie("email", data.result.auth.email);
            $.cookie("phoneNo", data.result.phoneNo);
            $.cookie("screen_photo", data.result.screen_photo);
            window.open("/userInfo")
        } else {
            window.location = "/login"
        }
    }, function(e){console.error(e)})
})

//这个还可以在简化，@杨艳梅 回来你做
function showleft() {
    $.ajax({
        type: "POST",
        url: "/showConfig",
        data: JSON.stringify({}),
        contentType: "application/json,charset=utf-8",
        success: function (data) {
            if (data.status == "ok") {
                showLeftInfo("province", "区域", data.result.info[0].province)
                showLeftInfo("manufacture", "生产厂家", data.result.info[0].manufacture)
                showLeftInfo("manufacturetype", "生产厂商类型",eval(["内资","合资"]))
                showLeftInfo("dosage", "剂型", data.result.info[0].product_type)
                showLeftInfo("specification", "规格", data.result.info[0].specifications)
                showLeftInfo("package", "包装", data.result.info[0].package)
            }
        }
    })
}

var showAtcInfo = function() {
    $.ajax({
        type: "POST",
        url: "/category",
        data: JSON.stringify({}),
        contentType: "application/json,charset=utf-8",
        success: function (data) {
            if (data.status == "ok") {
                showLeftInfo("ATC1", "治疗I", data.result.atc_one)
                showLeftInfo("ATC2", "治疗II", data.result.atc_tow)
                showLeftInfo("ATC3", "治疗III", data.result.atc_three)
                showLeftInfo("genericnameinfo", "通用名", data.result.oral)
                showLeftInfo("product", "商品名", data.result.product)
            }
        }
    })
}

/**
 * 暂时没用到
 * @param data
 * @param flag
 * @returns {Array}
 */
var eachResult = function (data, flag) {
    var array = new Array();
    $.each(data, function(i, v){
        if(flag == "des") {
            array.push(v.des);
        }else if(flag == "def") {
            array.push(v.def);
        }else return array;
    });
    return array;
}

var creatSelect = function(id, info) {
    return $("#" + id).select2({
        language: 'zh-CN',
        maximumInputLength: 100,//限制最大字符，以防坑货
        placeholder: info,
        allowClear: true,
        escapeMarkup: function (m) {
            return m;
        }
    });
}

//控制左
function showLeftInfo(btn, info, res) {
    var selectObj = creatSelect(btn, info);
    selectObj.empty();//清空下拉框
    selectObj.append("<option value=''>info</option>");
    $.each(res, function (i, item) {
        selectObj.append("<option value=" + item + ">" + item + "</option>")
    })
}

//选项框控制
function showDig() {
    $("#xssj").attr({"class":"screen-box","onclick":""})
    $("#guim").text("")
    $('#zengzl').text("");
    $("#fene").text("")
    $("#chanps").text("")
    showDataList()
    showDataGather()
}
