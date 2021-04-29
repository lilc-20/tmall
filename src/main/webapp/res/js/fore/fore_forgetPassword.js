$(function () {
    //用户名input获取光标
    $("#user_name").focus(function () {
        $(this).css("border", "1px solid #3879D9")
            .next().text("请输入用户名").css("display", "inline-block").css("color", "#00A0E9");
    });
    //手机input获取光标
    $("#user_phone").focus(function () {
        $("#user_phone").css("border", "1px solid #3879D9")
            .next().text("请输入手机号").css("display", "inline-block").css("color", "#00A0E9");
    });
    //发送验证码按钮触发
    $("#get_phone_code").click(function () {
        var user_phone = $.trim($("input[name=user_phone]").val());
        var reg_tel = new RegExp(/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/);
        if (user_phone == null || user_phone === "") {
            $("#user_phone").css("border", "1px solid red")
                .next().text("请输入手机号").css("display", "inline-block").css("color", "red");
            return false;
        } else if(!reg_tel.test(user_phone)){
            $("#user_phone").css("border", "1px solid red")
                .next().text("请输入正确的手机号").css("display", "inline-block").css("color", "red");
            return false;
        }
        $.ajax({
            type: "POST",
            url: "/tmall/sendSms",
            data: {
                "user_phone": user_phone,
                "templateId": "940980"
            },
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    $("#user_phone").next().text(data.msg).css("display", "inline-block").css("color", "green");
                    $("#get_phone_code").attr({"disabled":"disabled"});
                    var second = 60;
                    var intervalObj = setInterval(function () {
                        $("#get_phone_code").val("重新获取" + "(" + second + ")");
                        if(second == 0){
                            $("#get_phone_code").val("获取验证码");
                            $("#get_phone_code").removeAttr("disabled");
                            clearInterval(intervalObj);
                        }
                        second--;}, 1000 );
                } else {
                    $("#user_phone").css("border", "1px solid red");
                    $("#user_phone").next().text(data.msg).css("display", "inline-block").css("color", "red");
                }
            }
        });
    });
    //验证码input获取光标
    $("#user_phone_code").focus(function () {
        $(this).css("border", "1px solid #3879D9");
        $(this).val("");
        $(this).css("border", "1px solid #3879D9")
            .text("请输入验证码").css("display", "inline-block").css("color", "black");
    });
    //密码input获取光标
    $("#user_password").focus(function () {
        $(this).css("border", "1px solid #3879D9")
            .next().text("请输入密码").css("display", "inline-block").css("color", "#00A0E9");
    });
    //再次输入密码input获取光标
    $("#user_password_one").focus(function () {
        $(this).css("border", "1px solid #3879D9")
            .next().text("请再次输入密码").css("display", "inline-block").css("color", "#00A0E9");
    });

    //input离开光标
    $(".form-text").blur(function () {
        $(this).css("border-color", "#cccccc")
            .next().css("display", "none");
    });

    //非空验证
    $("#forget_sub").click(function () {
        //用户名
        var user_name = $.trim($("input[name=user_name]").val());
        //手机
        var user_phone = $.trim($("input[name=user_phone]").val());
        //验证码
        var user_phone_code = $.trim($("input[name=user_phone_code]").val());
        //密码
        var user_password = $.trim($("input[name=user_password]").val());
        //确认密码
        var user_password_one = $.trim($("input[name=user_password_one]").val());

        //验证密码的格式 包含数字和英文字母
        var reg = new RegExp(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/);
        //验证手机号格式
        var reg_tel = new RegExp(/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/);
        if (user_name == null || user_name === "") {
            $("#user_name").css("border", "1px solid red")
                .next().text("请输入用户名").css("display", "inline-block").css("color", "red");
            return false;
        } else if (user_phone == null || user_phone === "") {
            $("#user_phone").css("border", "1px solid red")
                .next().text("请输入手机号").css("display", "inline-block").css("color", "red");
            return false;
        } else if(!reg_tel.test(user_phone)){
            $("#user_phone").css("border", "1px solid red")
                .next().text("请输入正确的手机号").css("display", "inline-block").css("color", "red");
            return false;
        } else if (user_phone_code == null || user_phone_code === "") {
            $("#user_phone_code").css("border", "1px solid red")
                .text("请输入验证码").css("display", "inline-block").css("color", "red");
            return false;
        } else if (user_password == null || user_password === "") {
            $("#user_password").css("border", "1px solid red")
                .next().text("请输入密码").css("display", "inline-block").css("color", "red");
            return false;
        } else if (user_password_one == null || user_password_one === "") {
            $("#user_password_one").css("border", "1px solid red")
                .next().text("请重复输入密码").css("display", "inline-block").css("color", "red");
            return false;
        } else if(!reg.test(user_password)){
            $("#user_password").css("border", "1px solid red")
                .next().text("密码格式必须包含数字和字母").css("display", "inline-block").css("color", "red");
            return false;
        } else if (user_password !== user_password_one) {
            $("#user_password_one").css("border", "1px solid red")
                .next().text("两次输入密码不相同").css("display", "inline-block").css("color", "red");
            return false;
        }
        $.ajax({
            type: "POST",
            url: "/tmall/resetPassword",
            data: {
                "user_name": user_name,
                "user_phone": user_phone,
                "user_phone_code": user_phone_code,
                "user_password": user_password,
            },
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    $(".msg").stop(true, true).animate({
                        opacity: 1
                    }, 550, function () {
                        $(".msg").animate({
                            opacity: 0
                        }, 1500, function () {
                            location.href = "/tmall/login";
                        });
                    });
                } else {
                    if (data.msg != null) {
                        $("#user_name").css("border", "1px solid red")
                            .next().text(data.msg).css("display", "inline-block").css("color", "red");
                    }
                    if (data.msg_phone != null) {
                        $("#user_phone").css("border", "1px solid red")
                            .next().text(data.msg_phone).css("display", "inline-block").css("color", "red");
                    }
                    if (data.msg_phone_code != null) {
                        $("#user_phone_code").css("border", "1px solid red")
                            .val(data.msg_phone_code).css("display", "inline-block").css("color", "red");
                    }
                    if (data.msg_pwd != null) {
                        $("#user_password").css("border", "1px solid red")
                            .next().text(data.msg_pwd).css("display", "inline-block").css("color", "red");
                    }
                }
            },
            error: function (data) {
                location.reload(true);
            },
            beforeSend: function () {
            }
        });
    });
});
