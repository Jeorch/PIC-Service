/**
 * Created by qianpeng on 2017/6/15.
 */

// var token =  $.cookie("token")

/***
 * 针对于左侧菜单栏的条选中Json
 * 目前针对测试通过
 */
var getSearchValue = function () {
    var atc = [
        $("#ATC1"),
        $("#ATC2"),
        $("#ATC3")
    ];

    var category = null;
    $.each(atc, function(i, v) {
        if(v.val() != "") {
            category = {
                "category" : new Array(v.val())
            }
        }
    })

    var oral = null;
    if($("#genericnameinfo").val() != "") {
        oral = {
            "oral_name": $("#genericnameinfo").val()
        }
    }

    var product = null;
    if($("#product").val() != "") {
        product = {
            "product_name": $("#product").val()
        }
    }

    var edge = null;
    if($("#province").val() != "") {
        edge = {
            "edge" : new Array($("#province").val())
        }
    }

    return $.extend(category, edge, oral, product)
}