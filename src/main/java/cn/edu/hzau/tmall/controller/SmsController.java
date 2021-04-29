package cn.edu.hzau.tmall.controller;

import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.*;
import cn.edu.hzau.tmall.entity.Sms;
import cn.edu.hzau.tmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import java.util.Random;

import static com.squareup.okhttp.internal.Internal.logger;

@Controller
public class SmsController {
    @Resource(name = "userService")
    private UserService userService;

    //发送短信
    @ResponseBody
    @RequestMapping(value = "sendSms", method = RequestMethod.POST)
    public String sendSms(
            HttpSession session,
            @RequestParam(value = "user_phone") String phoneNumber,
            @RequestParam(value = "templateId") String templateId) {

        Sms sms = new Sms();
        sms.setMin(5);
        sms.setTemplateId(templateId);
        sms.setPhoneNumber(phoneNumber);

        logger.info("验证手机号是否存在");
        Integer count_phone = userService.getPhone(phoneNumber);

        logger.info("选择短信功能");
        if (count_phone > 0 && templateId.equals("939288")/*注册*/) {
            logger.info("手机号已存在，返回错误信息");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("msg", "该手机号已注册！");
            return object.toJSONString();
        } else if (count_phone == 0 && templateId.equals("940980")/*重置密码*/) {
            logger.info("手机号不存在，返回错误信息");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("msg", "账号不存在！");
            return object.toJSONString();
        }

        logger.info("生成6位随机验证码");
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            code += random.nextInt(10);
        }
        sms.setCode(code);
        logger.info("将验证码存入session");
        session.setAttribute(phoneNumber, code);

        try{

            logger.info("准备发送短信");
            Credential cred = new Credential("AKIDnLccN1VkhejcRYqiyy1sS7TebDfx9piM", "VoFQEpAGSlJBtFx47wZzzC9DaMusl4AB");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, "", clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {"+86" + sms.getPhoneNumber()};
            req.setPhoneNumberSet(phoneNumberSet1);

            req.setTemplateID(sms.getTemplateId());
            req.setSmsSdkAppid("1400513084");
            req.setSign("望月红叶");

            String[] param = {sms.getCode(), Integer.toString(sms.getMin())};
            req.setTemplateParamSet(param);

            SendSmsResponse resp = client.SendSms(req);

            System.out.println(SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

        JSONObject object = new JSONObject();
        object.put("success", true);
        object.put("msg", "已发送");
        return object.toJSONString();
    }

}
