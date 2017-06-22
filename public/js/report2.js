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
            var d = $.extend(r.parameter, {"token": $.cookie("token")})
            ajaxData("/data/calc/report/summary", JSON.stringify(d), "POST", function(r2){

            }, function(e2){console.error(e2)})
        }
    }, function(e){console.error(e)})

    // $("#xiaojieatcname").text("fuck")
});