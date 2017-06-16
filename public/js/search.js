/**
 * Created by qianpeng on 2017/6/15.
 */

/**
 * 数据列表
 */
var showDataList = function() {

    pageResult(1)
    $("#img").hide();
    $("#grid").show();
}

var pageResult = function(skip) {
    var c = $.extend(getSearchValue(), getTime())
    var data = JSON.stringify({
        "token": $.cookie("token"),
        "condition":{
            "category": c.category,
            "oral_name": c.oral_name,
            "product_name": c.product_name,
            "edge": c.edge,
            "date": c.date,
        },
        "skip": skip
    });
    $.ajax({
        type: "POST",
        url: "/data/search",
        dataType: "json",
        cache: false,
        data: data,
        contentType: "application/json,charset=utf-8",
        success: function (r) {
            if (r.status == "ok") {
                $("#tbody").empty();
                $("#pageview").show()
                $.each(r.search_result, function(i, v){
                    $("#tbody").append(v.html)
                })
                Page(r)
            }else{
                $("#pageview").hide()
                Page(null)
            }
        }
    });
}

var getTime = function() {
    var timeType = $("#timeType").val();
    var start = null;
    var end = null;
    if(timeType == 1 && $("#yearInputb").val() != ""){
        var year = $("#yearInputb").val()
        start = (parseInt(year) - 1) + "01"
        end = year + "01"
    }else if(timeType == 2 && $("#monthInputb").val() != ""){
        var yearMonth = $("#monthInputb").val()
        var year = yearMonth.split("-")[0]
        var month = yearMonth.split("-")[1]
        start = (parseInt(year) - 1) + month
        end = year + month
    }

    var date = null;
    if(start != null) {
        date = {
            "date": {
                "start": start,
                "end": end
            }
        }
    }
    return date;
}

/**
 * 显示主页的四个小汇总
 */
var showDataGather = function() {

    var c = $.extend(getSearchValue(), getTime())

    var data = JSON.stringify({
        "token": $.cookie("token"),
        "condition":{
            "category": c.category,
            "oral_name": c.oral_name,
            "product_name": c.product_name,
            "date": c.date,
            "edge": c.edge
        }
    });

    calcMarket(data)
    calcTrend(data)
    calcPercentage(data)

}

function calcMarket(data) {
    $.ajax({
        type: "POST",
        url: "/data/calc/market",
        dataType: "json",
        cache: false,
        data: data,
        contentType: "application/json,charset=utf-8",
        success: function (r) {
            if (r.status == "ok") {
                var market = r.result.calc.sales;
                $("#guim").text(market)
            }
        }
    });
}

function calcTrend(data) {
    $.ajax({
        type: "POST",
        url: "/data/calc/trend",
        dataType: "json",
        cache: false,
        data: data,
        contentType: "application/json,charset=utf-8",
        success: function (r) {
            if (r.status == "ok") {
                var trend = parseFloat(r.result.trend);
                var treNum=(Math.floor(trend*100)/100)+ "%"
                $('#zengzl').text(treNum);
            }
        }
    });
}

function calcPercentage(data) {
    $.ajax({
        type: "POST",
        url: "/data/calc/percentage",
        dataType: "json",
        cache: false,
        data: data,
        contentType: "application/json,charset=utf-8",
        success: function (r) {
            if (r.status == "ok") {
                var percentage = parseFloat(r.result.percentage);
                var percentNum=(Math.floor(percentage*10000)/100) +"%"
                $('#fene').text(percentNum);
            }
        }
    });
}
