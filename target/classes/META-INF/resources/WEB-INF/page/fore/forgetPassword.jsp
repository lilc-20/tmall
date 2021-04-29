<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/header.jsp" %>
<head>
    <script src="${pageContext.request.contextPath}/res/js/fore/fore_forgetPassword.js"></script>
    <link href="${pageContext.request.contextPath}/res/css/fore/fore_register.css" rel="stylesheet">
    <title>天猫tmall.com - 忘记密码</title>
    <style rel="stylesheet">
        #nav {
            width: auto;
            height: 32px;
            font-family: "Microsoft YaHei UI", Tahoma, serif;
            font-size: 12px;
            position: relative !important;
            background: #f2f2f2;
            z-index: 999;
            border-bottom: 1px solid #e5e5e5;
        }
    </style>
</head>
<body>
<nav>
    <div class="header">
        <div id="mallLogo">
            <a href="${pageContext.request.contextPath}"><img
                    src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/tmallLogoA.png"><span
                    class="span_tmallRegister">找回密码</span></a>
        </div>
    </div>
</nav>
<div class="content">
    <div class="steps">
        <div class="steps_main">
            <span class="steps_tsl">重置密码</span>
        </div>
    </div>
    <div class="form-list">
        <div class="form-item">
            <label class="form-label tsl">用户名：</label>
            <input name="user_name" id="user_name" class="form-text err-input" placeholder="请输入用户名" maxlength="20">
            <span class="form_span"></span>
        </div>
        <div class="form-item">
            <label class="form-label tsl">手机：</label>
            <input name="user_phone" id="user_phone" class="form-text err-input" placeholder="请输入手机号" maxlength="20">
            <span class="form_span"></span>

            <br/>
            <label class="form-label tsl">验证码：</label>
            <input name="user_phone_code" id="user_phone_code" class="form-text2 err-input" placeholder="请输入验证码" maxlength="20">
            <input type="button" id="get_phone_code" style="height: 32px" value="获取验证码" />
        </div>
        <div class="form-item">
            <label class="form-label form-label-b tsls">设置新密码</label>
        </div>
        <div class="form-item">
            <label class="form-label tsl">登录密码：</label>
            <input name="user_password" type="password" id="user_password" class="form-text err-input"
                   placeholder="请设置登录密码" maxlength="20">
            <span class="form_span"></span>
        </div>
        <div class="form-item">
            <label class="form-label tsl">确认密码：</label>
            <input name="user_password_one" type="password" id="user_password_one" class="form-text err-input"
                   placeholder="请再次输入密码" maxlength="20">
            <span class="form_span"></span>
        </div>

        <div class="form-item">
            <input type="button" id="forget_sub" class="btns btn-large tsl" value="修 改"/>
        </div>
    </div>
</div>
<%@include file="include/footer.jsp" %>
<link href="${pageContext.request.contextPath}/res/css/fore/fore_foot_special.css" rel="stylesheet"/>
<div class="msg">
    <span>修改成功，跳转到登陆页面</span>
</div>
</body>
