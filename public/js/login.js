/**
 * Created by yym on 6/9/17.
 */

$(function(){
    $("#sub").click(function(){
        if($("#name").val() == "" || $("#password").val() == "") {
            alert("用户名和密码不能为空！")
            return;
        }
        var data = JSON.stringify({
            "user_name" : $("#name").val(),
            "pwd" : $("#password").val()
        })
        ajaxData("/auth/password", data, "POST", function(data){
            if(data.status == "ok") {
                $.cookie("token",data.result.auth_token);
                $.cookie("user_name",data.result.user.user_name)
                if(data.result.user.user_name=="admin"){
                    window.location="/admin"
                }else {
                    window.location = "/data/report"
                }
            }else {
                alert("登录失败，请检查账户与用户名！")
            }
        }, function(e){console.info(e)})
    })
})

function logoutSys() {
    cleanAllCookie();
    location = "/login"
}

var cleanAllCookie = function() {
    var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    if(keys) {
        $.each(keys, function(i, v) {
            $.cookie(v, "", {"path": "/", "expires": -1 });
        })
    }
}