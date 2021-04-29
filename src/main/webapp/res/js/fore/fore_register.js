$(function () {
    //刷新下拉框
    $('#select_user_address_province').selectpicker('refresh');
    $('#select_user_address_city').selectpicker('refresh');
    $('#select_user_address_district').selectpicker('refresh');
    //改变订单信息时
    $('#select_user_address_province').change(function () {
        $.ajax({
            type: "GET",
            url: "/tmall/address/" + $(this).val(),
            data: null,
            dataType: "json",
            success: function (data) {
                $(".loader").hide();
                if (data.success) {
                    $("#select_user_address_city").empty();
                    $("#select_user_address_district").empty();
                    for (var i = 0; i < data.addressList.length; i++) {
                        var address_id = data.addressList[i].address_areaId;
                        var address_name = data.addressList[i].address_name;
                        $("#select_user_address_city").append("<option value='" + address_id + "'>" + address_name + "</option>")
                    }
                    for (var j = 0; j < data.childAddressList.length; j++) {
                        var childAddress_id = data.childAddressList[j].address_areaId;
                        var childAddress_name = data.childAddressList[j].address_name;
                        $("#select_user_address_district").append("<option value='" + childAddress_id + "'>" + childAddress_name + "</option>")
                    }
                    $('#select_user_address_city').selectpicker('refresh');
                    $("#select_user_address_district").selectpicker('refresh');
                    $("span.address-province").text($("#select_user_address_province").find("option:selected").text());
                    $("span.address-city").text($("#select_user_address_city").find("option:selected").text());
                    $("span.address_district").text($("#select_user_address_district").find("option:selected").text());
                } else {
                    alert("加载地区信息失败，请刷新页面再试！")
                }
            },
            beforeSend: function () {
                $(".loader").show();
            },
            error: function () {
                alert("加载地区信息失败，请刷新页面再试！")
            }
        });
    });
    $("#select_user_address_city").change(function () {
        $.ajax({
            type: "GET",
            url: "/tmall/address/" + $(this).val(),
            data: null,
            dataType: "json",
            success: function (data) {
                $(".loader").hide();
                if (data.success) {
                    $("#select_user_address_district").empty();
                    for (var i = 0; i < data.addressList.length; i++) {
                        var address_id = data.addressList[i].address_areaId;
                        var address_name = data.addressList[i].address_name;
                        $("#select_user_address_district").append("<option value='" + address_id + "'>" + address_name + "</option>")
                    }
                    $('#select_user_address_district').selectpicker('refresh');
                    $("span.address-city").text($("#select_user_address_city").find("option:selected").text());
                    $("span.address_district").text($("#select_user_address_district").find("option:selected").text());
                } else {
                    alert("加载地区信息失败，请刷新页面再试！")
                }
            },
            beforeSend: function () {
                $(".loader").show();
            },
            error: function () {
                alert("加载地区信息失败，请刷新页面再试！")
            }
        });
    });
    $("#select_user_address_district").change(function () {
        $("span.address_district").text($(this).find("option:selected").text());
    });
    $("#textarea_details_address").bind('input propertychange', function () {
        $(".address_details").text($(this).val());
    });

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
                "templateId": "939288"
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
    //昵称input获取光标
    $("#user_nickname").focus(function () {
        $(this).css("border", "1px solid #3879D9")
            .next().text("请输入昵称").css("display", "inline-block").css("color", "#00A0E9");
    });
    //出生日期input获取光标
    $("#user_birthday").focus(function () {
        $(this).css("border", "1px solid #3879D9")
            .next().text("请输入出生日期").css("display", "inline-block").css("color", "#00A0E9");
    });

    //input离开光标
    $(".form-text").blur(function () {
        $(this).css("border-color", "#cccccc")
            .next().css("display", "none");
    });

    //非空验证
    $("#register_sub").click(function () {
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
        //昵称
        var user_nickname = $.trim($("input[name=user_nickname]").val());
        //出生日期
        var user_birthday = $.trim($("input[name=user_birthday]").val());

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
        } else if (user_nickname == null || user_nickname === "") {
            $("#user_nickname").css("border", "1px solid red")
                .next().text("请输入昵称").css("display", "inline-block").css("color", "red");
            return false;
        } else if (user_birthday == null || user_birthday === "") {
            $("#user_birthday").css("border", "1px solid red")
                .next().text("请选择出生日期").css("display", "inline-block").css("color", "red");
            return false;
        }
        $.ajax({
            type: "POST",
            url: "/tmall/register/doRegister",
            data: {
                "user_name": user_name,
                "user_phone": user_phone,
                "user_phone_code": user_phone_code,
                "user_password": user_password,
                "user_nickname": user_nickname,
                "user_birthday": user_birthday,
                "user_gender": $("input[name=user_gender]:checked").val(),
                "user_address": $("#select_user_address_district").val()
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

