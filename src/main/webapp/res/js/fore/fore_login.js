$(function () {
    //二维码动画
    $("#qrcodeA").hover(
        function () {
            $(this).stop().animate({left: "13px"}, 450, function () {
                $("#qrcodeB").stop().animate({opacity: 1}, 300)
            });
        }
        , function () {
            $("#qrcodeB").css("opacity", "0");
            $(this).stop().animate({left: "80px"}, 450);
        });
    //登录方式切换
    $("#loginSwitch").click(function () {
        var messageSpan = $(".loginMessageMain").children("span");
        if ($(".pwdLogin").css("display") === "block") {
            $(".pwdLogin").css("display", "none");
            $(".qrcodeLogin").css("display", "block");
            messageSpan.text("密码登录在这里");
            $(this).removeClass("loginSwitch").addClass("loginSwitch_two");
        } else {
            $(".pwdLogin").css("display", "block");
            $(".qrcodeLogin").css("display", "none");
            messageSpan.text("扫码登录更安全");
            $(this).removeClass("loginSwitch_two").addClass("loginSwitch");
        }

    });
    $("#pwdLogin").click(function () {
        var messageSpan = $(".loginMessageMain").children("span");
        $(".pwdLogin").css("display", "block");
        $(".qrcodeLogin").css("display", "none");
        messageSpan.text("扫码登录更安全");
        $("#loginSwitch").removeClass("loginSwitch_two").addClass("loginSwitch");
    });
    //登录验证
    $(".loginForm").submit(function () {
        var yn = true;
        $(this).find(":text,:password").each(function () {
            if ($.trim($(this).val()) === "") {
                styleUtil.errorShow($("#error_message_p"), "请输入用户名和密码！");
                yn = false;
                return yn;
            }
        });
        if (yn) {
            $.ajax({
                type: "POST",
                url: "/tmall/login/doLogin",
                data: {"username": $.trim($("#name").val()), "password": $.trim($("#password").val())},
                dataType: "json",
                success: function (data) {
                    $(".loginButton").val("登 录");
                    if (data.success) {
                        location.href = "/tmall";
                    } else {
                        styleUtil.errorShow($("#error_message_p"), "用户名和密码错误！");
                    }
                },
                error: function (data) {
                    $(".loginButton").val("登 录");
                    styleUtil.errorShow($("#error_message_p"), "服务器异常，请刷新页面再试！");
                },
                beforeSend: function () {
                    $(".loginButton").val("正在登录...");
                }
            });
        }
        return false;
    });
    $(".loginForm :text,.loginForm :password").focus(function () {
        styleUtil.errorHide($("#error_message_p"));
    });

    $("#openCamera").click(function getMedia(){
        let constraints = {video: true};
        navigator.mediaDevices.getUserMedia(constraints)
            .then(function (stream) {
                // 旧的浏览器可能没有srcObject
                if ("srcObject" in video) {
                    video.srcObject = stream;
                } else {
                    // 防止在新的浏览器里使用它，应为它已经不再支持了
                    video.src = window.URL.createObjectURL(stream);
                }
                video.onloadedmetadata = function (e) {
                    video.play();
                };
            })
            .catch(function (err) {
                console.log(err.name + ": " + err.message);
            });
    })

    $("#closeCamera").click(function stopStreamedVideo() {
        let video = document.getElementById("video");
        const stream = video.srcObject;
        const tracks = stream.getTracks();

        tracks.forEach(function(track) {
            track.stop();
        });

        video.srcObject = null;
    });

    let canvas = document.getElementById("canvas");
    let context = canvas.getContext("2d");
    //注册拍照按钮的单击事件
    $("#faceLogin").click(function () {
        context.drawImage(video, 0, 0, 200, 200);

        $.ajax({
            type: "POST",
            url: "/tmall/face/login",
            data: {"face" : canvas.toDataURL()},
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    location.href = "/tmall";
                } else {
                    styleUtil.errorShow($("#error_message_q"), "识别失败");
                }
            }
        });
    });

});