/**
 * Created by qianpeng on 2017/6/15.
 */

// var token =  $.cookie("token")

/***
 * 针对于左侧菜单栏的条选中返回Object
 * 只针对于这个项目
 * 后续需要改变成公用的
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

    var manufacturetype = null;
    if($("#manufacturetype").val() != "") {
        manufacturetype = {
            "": $("#manufacturetype").val()
        }
    }

    var manufacture = null;
    if($("#manufacture").val() != "") {
        manufacture = {
            "": $("#manufacture").val()
        }
    }

    var dosage = null;
    if($("#dosage").val() != "") {
        dosage = {
            "": $("#dosage").val()
        }
    }

    var specification = null
    if($("#specification").val()!= "") {
        specification = {
            "": $("#specification").val()
        }
    }

    var package = null;
    if($("#package").val() != "") {
        package = {
            "": $("#package").val()
        }
    }

    return $.extend(category, edge, oral, product)
}