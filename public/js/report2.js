/**
 * Created by qianpeng on 2017/6/20.
 */

var printContent = function() {
    window.print();
}


$(function(){
    var data = JSON.stringify({
        "token": $.cookie("token"),
        "reportid": md5report
    })
    ajaxData("/data/query/reportparameter", data, "POST", function(r){
        if(r.status == "ok") {
            reportajax($.extend(r.parameter, {"token": $.cookie("token")}))
        }
    }, function(e){console.error(e)})

    // $("#xiaojieatcname").text("fuck")
});

var reportajax = function(data) {
    ajaxData("/data/calc/report/summary", JSON.stringify(data), "POST", function(r2){
        reportgraphone(r2.reportgraphone)
    }, function(e2){console.error(e2)})
}

var reportgraphone = function(obj) {
    var xAxisData = [], seriesData_sales = [], seriesData_trend = [];
    $.each(obj, function (i, v) {
        xAxisData.push(v.start + "-" + v.end);
        seriesData_sales.push(v.sales);
        seriesData_trend.push(v.trend);
    });
    option = {
        tooltip: {trigger: 'axis'},
        legend: {data:['销售额','增长率']},
        xAxis: [{type: 'category', data: xAxisData}],
        yAxis: [
            {type: 'value', name: '销售额', axisLabel: {formatter: '{value} ￥'}},
            {type: 'value', name: '增长率', axisLabel: {formatter: '{value} %'}}
        ],
        series: [
            {name:'销售额', type:'bar', data:seriesData_sales},
            {name:'增长率', type:'line', yAxisIndex: 1, data:seriesData_trend}
            ]
    };

    var Histogram = Echart("reportgraphone", "vintage")
    Histogram.setOption(option);
}