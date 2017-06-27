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

    // $("#xiaojieatcname").text("嘿嘿嘿")
});

var reportajax = function(data) {
    ajaxData("/data/calc/report/summary", JSON.stringify(data), "POST", function(r2){
        reportgraphone(r2.reportgraphone)
        reportgraphfour(r2.reportgraphfour)
        reportgraphfive(r2.reportgraphfive)
        reportgraphseven(r2.reportgraphseven)
        reportgrapheight(r2.reportgrapheight)
        reportgraphsix(r2.reportgraphsix)
        // console.info(r2)
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
        title: {text: "销售额表现", left: 'center', top:'4%'},
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

var reportgrapheight = function (obj) {
    var legend = [];
    var yAxis = [];
    var series = [];
    $.each(obj, function(i, v){
        $.each(v.keyvalue, function(n, k) {
            $.each(k, function(j, l) {
                legend.push(j)
            })
        })
        yAxis.push(v.start + "-" + v.end)
    })
    legend = Array.from(new Set(legend))
    var d = [];
    $.each(legend, function(i, v) {
        d = []
        $.each(yAxis, function(n, k) {
            $.each(obj, function(j, l) {
                if(k == (l.start + "-" + l.end)) {
                    $.each(l.keyvalue, function(o, p) {
                        $.each(p, function(b,m) {
                            if(v == b) {
                                d.push(m)
                            }
                        })
                    })
                }
            })
        })
        series.push({
            name : v,
            type : "bar",
            data : d
        })
    })
    var option = {
        title: {text: "医院市场Top10品牌i销售额占比", left: 'center', top:'4%'},
        tooltip: {trigger: 'axis', axisPointer: {type: 'shadow'}},
        legend: {
            data: legend
        },
        grid: {left: '3%', right: '4%', bottom: '3%', containLabel: true},
        xAxis: {
            type: 'value',
            boundaryGap: [0, 0.01]
        },
        yAxis: {
            type: 'category',
            data: yAxis
        },
        series: series
    };
    var Histogram = Echart("reportgrapheight", "vintage")
    Histogram.setOption(option);
}

var reportgraphseven = function(obj) {
    var xAxisData = [], seriesData_sales = [], seriesData_trend = [];
    $.each(obj, function (i, v) {
        xAxisData.push(v.start + "-" + v.end);
        seriesData_sales.push(v.sales);
        seriesData_trend.push(v.trend);
    });
    option = {
        tooltip: {trigger: 'axis'},
        legend: {data:['销售额占比','占比增长率']},
        xAxis: [{type: 'category', data: xAxisData}],
        yAxis: [
            {type: 'value', name: '销售额占比', axisLabel: {formatter: '{value} %'}},
            {type: 'value', name: '占比增长率', axisLabel: {formatter: '{value} %'}}
        ],
        series: [
            {name:'销售额占比', type:'bar', data:seriesData_sales},
            {name:'占比增长率', type:'line', yAxisIndex: 1, data:seriesData_trend}
        ]
    };

    var Histogram = Echart("reportgraphseven", "vintage")
    Histogram.setOption(option);
}

var reportgraphfive = function(obj) {
    var legend = [];
    var xAxis = [];
    var series = [];

    $.each(obj, function (i, v) {
        $.each(v.sales, function (n, k) {
            legend.push(k.key)
        })
        xAxis.push(v.start + "-" + v.end)
    })
    legend = Array.from(new Set(legend))
    xAxis = xAxis.reverse()

    var bar = [];
    var line = [];
    $.each(legend, function(i, v) {
        bar = []
        line = []
        $.each(xAxis, function(n, k) {
            $.each(obj, function(j, l) {
                if(k == (l.start + "-" + l.end)) {
                    $.each(l.sales, function(o, p) {
                        if(v == p.key) {
                            bar.push(p.value)
                        }
                    })
                    $.each(l.scale, function(b,m) {
                        if(v == m.key) {
                            line.push(m.value)
                        }
                    })
                }
            })
        })
        series.push({
            name: v,
            type: 'bar',
            stack: '总量',
            label: {normal: {show: true, position: 'inside'}},
            data: bar
        },{
            name: v,
            type: 'line',
            yAxisIndex: 1,
            data: line
        })
    })
    var option = {
        tooltip : {trigger: 'axis', axisPointer : {type : 'shadow'}},
        legend: {data: legend},
        grid: {left: '3%', right: '4%', bottom: '3%', containLabel: true},
        xAxis:  {type: 'category', data: xAxis},
        yAxis: [
            {type: 'value', name: '销售额', axisLabel: {formatter: '{value} ￥'}},
            {type: 'value', name: '占有率', axisLabel: {formatter: '{value} %'}}
        ],
        series: series
    };

    var Histogram = Echart("reportgraphfive", "vintage")
    Histogram.setOption(option);
}

var reportgraphfour = function(obj) {
    var xAxis = [], inter=[], outer = [], percent = [];

    $.each(obj, function(i, v){
        xAxis.push(v.start + "-" + v.end)
        inter.push(v.inter)
        outer.push(v.outer)
        percent.push(v.percent)
    });
    xAxis = xAxis.reverse()
    inter = inter.reverse()
    outer = outer.reverse()
    percent = percent.reverse()

    var option = {
        title: {text: "合资与内资企业销售表现", left: 'center', top:'4%'},
        tooltip: {trigger: 'axis', axisPointer: {type: 'cross', crossStyle: {color: '#999'}}},
        legend: {
            data:['合资','内资','占比']
        },
        xAxis: [
            {type: 'category', data: xAxis, axisPointer: {type: 'shadow'}}
        ],
        yAxis: [
            {type: 'value', name: '销售额', axisLabel: {formatter: '{value} ￥'}},
            {type: 'value', name: '占比', axisLabel: {formatter: '{value} %'}}
        ],
        series: [
            {name: '合资', type: 'bar', data: outer},
            {name: '内资', type: 'bar', data: inter},
            {name: '占比', type: 'line', yAxisIndex: 1, data: percent}
        ]
    };
    var Histogram = Echart("reportgraphfour", "vintage")
    Histogram.setOption(option);
}

var reportgraphsix = function(obj) {
    var legend = [];
    var xAxis = [];
    var series = [];
    $.each(obj, function (i, v) {
        $.each(v.keyvalue, function (n, k) {
            $.each(k, function(j, l){
                xAxis.push(j)
            })
        })
        legend.push(v.start + "-" + v.end)
    })
    xAxis = Array.from(new Set(xAxis))

    var d = [];

    $.each(legend, function(i, v) {
        d = []
        $.each(xAxis, function (n, k) {
            $.each(obj, function(j, l) {
                if(v == (l.start + "-" + l.end)){
                    $.each(l.keyvalue, function(o, p){
                        $.each(p, function(b,m) {
                            if(k == b) {
                                d.push(m)
                            }
                        })
                    })
                }
            })
        })
        series.push({
            name : v,
            type : "bar",
            data : d
        })
    })

    var option = {
        tooltip : {
            trigger: 'axis',
            axisPointer: {type: 'shadow', label: {show: true}}
        },
        calculable : true,
        legend: {data: legend, itemGap: 5},
        grid: {top: '12%', left: '1%', right: '10%', containLabel: true},
        xAxis: [{type : 'category', data : xAxis}],
        yAxis: [{type : 'value', name : '销售额￥',
                axisLabel: {
                    formatter: function (a) {
                        a = +a;
                        return isFinite(a)
                            ? echarts.format.addCommas(+a / 1000)
                            : '';
                    }
                }
            }
        ],
        dataZoom: [{show: true, start: 94, end: 100}, {type: 'inside', start: 94, end: 100},
                   {show: true, yAxisIndex: 0, filterMode: 'empty', width: 30, height: '80%', showDataShadow: false, left: '93%'}
        ],
        series : series
    };
    var Histogram = Echart("reportgraphsix", "vintage")
    Histogram.setOption(option);
}