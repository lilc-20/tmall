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
                "templateId": "975518"
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
        //图片地址
        var user_face_picture_src = $.trim($("input[name=user_face_picture_src]").val());

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
        } else if (user_face_picture_src == null || user_face_picture_src === "") {
            alert("请上传图片")
            return false;
        }



        $.ajax({
            type: "POST",
            url: "/tmall/face/doRegister",
            data: {
                "user_name": user_name,
                "user_phone": user_phone,
                "user_phone_code": user_phone_code,
                "user_face_picture_src": user_face_picture_src
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

//图片上传
function uploadImage(fileDom) {
    //获取文件
    var file = fileDom.files[0];
    //判断类型
    var imageType = /^image\//;
    if (file === undefined || !imageType.test(file.type)) {
        alert("请选择图片！");
        return;
    }
    //判断大小
    if (file.size > 512000) {
        alert("图片大小不能超过500K！");
        return;
    }
    //清空值
    $(fileDom).val('');
    var formData = new FormData();
    formData.append("file", file);
    //上传图片
    $.ajax({
        url: "/tmall/face/uploadUserFaceImage",
        type: "post",
        data: formData,
        contentType: false,
        processData: false,
        dataType: "json",
        mimeType: "multipart/form-data",
        success: function (data) {
            if (data.success) {
                $(fileDom).prev("img").attr("src","/tmall/res/images/item/userFacePicture/"+data.fileName);
                $("#user_face_picture_src_value").val(data.fileName);
            } else {
                alert("图片上传异常！");
            }
        },
        beforeSend: function () {
        },
        error: function () {

        }
    });
}

