package cn.edu.hzau.tmall.controller.fore;

import cn.edu.hzau.tmall.controller.BaseController;
import cn.edu.hzau.tmall.entity.Address;
import cn.edu.hzau.tmall.entity.User;
import cn.edu.hzau.tmall.service.AddressService;
import cn.edu.hzau.tmall.service.UserService;
import cn.edu.hzau.tmall.util.MD5Util;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 忘记密码页
 * @author llc
 */
@Controller
public class ForeForgetPasswordController extends BaseController{
    @Resource(name="userService")
    private UserService userService;

    //转到前台天猫-忘记密码页
    @RequestMapping(value = "forgetPassword", method = RequestMethod.GET)
    public String goToPage() {
        return "fore/forgetPassword";
    }

    //重置密码
    @ResponseBody
    @RequestMapping(value = "resetPassword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String resetPwd(
            HttpSession session,
            @RequestParam(value = "user_name") String user_name  /*用户名 */,
            @RequestParam(value = "user_phone") String user_phone  /*手机*/,
            @RequestParam(value = "user_phone_code") String user_phone_code  /*验证码*/,
            @RequestParam(value = "user_password") String user_password  /*用户密码*/
    ) throws ParseException {
        logger.info("验证用户名是否存在");
        Integer count_name = userService.getTotal(new User().setUser_name(user_name));
        if (count_name == 0) {
            logger.info("用户名不存在，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("msg", "用户名不存在，请重新输入！");
            return object.toJSONString();
        }
        logger.info("验证手机号是否存在");
        Integer count_phone = userService.checkPhone(user_name, user_phone);
        if (count_phone == 0) {
            logger.info("手机号不存在，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("msg_phone", "该账号不存在！");
            return object.toJSONString();
        }
        logger.info("验证验证码是否正确");
        String code = (String) session.getAttribute(user_phone);
        if (code == null || !code.equals(user_phone_code)) {
            logger.info("验证码错误,返回错误信息！");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("msg_phone_code", "验证码错误");
            return object.toJSONString();
        }

        logger.info("创建用户对象");
        user_password = MD5Util.getMD5(user_password);

        Integer count_pwd = userService.checkPwd(user_name, user_password);
        if (count_pwd > 0) {
            logger.info("新密码与旧密码相同，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("msg_pwd", "新密码与旧密码相同");
            return object.toJSONString();
        }

        logger.info("修改密码");
        if (userService.updatePwd(user_name, user_password)) {
            logger.info("修改成功");
            JSONObject object = new JSONObject();
            object.put("success", true);
            return object.toJSONString();
        } else {
            logger.info("修改失败");
            JSONObject object = new JSONObject();
            object.put("success", false);
            return object.toJSONString();
        }
    }

}
