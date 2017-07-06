/**
 * Created by yym on 6/9/17.
 */

$(function(){
    $("#sub").click(function(){
        var data = JSON.stringify({
            "user_name" : $("#name").val(),
            "pwd" : $("#password").val()
        })
        ajaxData("/auth/password", data, "POST", function(data){
            if(data.status == "ok") {
                $.cookie("token",data.result.auth_token);
                $.cookie("user_name",data.result.user.user_name);

                var ip = $.cookie("cname")+":"+$.cookie("cip");
                importLoginLog(data.result.user.user_name,ip);

                if(data.result.user.user_name=="admin"){
                    window.location="/admin";
                }else {
                    window.location = "/data/report";
                }
            }
        }, function(e){$("#errText").show();$("#noErr").hide()})
    })
})

function logoutSys() {
    saveLoginLog();
    cleanAllCookie();
    location = "/login";
}

var cleanAllCookie = function() {
    var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    if(keys) {
        $.each(keys, function(i, v) {
            $.cookie(v, "", {"path": "/", "expires": -1 });
        })
    }
}

function importLoginLog(user_name,ip) {
    var data = JSON.stringify({
        "user_name" : user_name,
        "ip" : ip
    })
    ajaxData("/loginLog/import", data, "POST", function(data){
        if(data.status == "ok") {
            $.cookie("log_id",data.result.log_id);
        }
    }, function(e){$("#errText").show();$("#noErr").hide()})
}

function saveLoginLog() {
    var data = JSON.stringify({
        "log_id" : $.cookie("log_id")
    })
    ajaxData("/loginLog/save", data, "POST", function(data){
        if(data.status == "ok") {
           console.log("saveLoginLog succeed");
        }
    }, function(e){$("#errText").show();$("#noErr").hide()})
}
