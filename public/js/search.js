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
    var tmp = getSearchValue();
    var data = JSON.stringify({
        "token": $.cookie("token"),
        "condition":{
            "category": tmp.category,
            "edge": tmp.edge
        },
        "skip": skip
    });

    $("#tbody").empty();
    $.ajax({
        type: "POST",
        url: "/data/search",
        dataType: "json",
        cache: false,
        data: data,
        contentType: "application/json,charset=utf-8",
        success: function (r) {
            if (r.status == "ok") {
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

/**
 * 显示主页的四个小汇总
 */
var showDataGather = function() {
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
    var c = $.extend(getSearchValue(), date)

    var data = JSON.stringify({
        "token": $.cookie("token"),
        "condition":{
            "category": c.category,
            "date": c.date,
            "edge": c.edge
        }
    });

    calcMarket(data)
    calcTrend(data)
    calcPercentage(data)

}
