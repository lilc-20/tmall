package cn.edu.hzau.tmall.controller.fore;

import com.alibaba.fastjson.JSONObject;
import cn.edu.hzau.tmall.controller.BaseController;
import cn.edu.hzau.tmall.entity.User;
import cn.edu.hzau.tmall.service.UserService;
import cn.edu.hzau.tmall.util.MD5Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 登陆页
 * @author llc
 */
@Controller
public class ForeLoginController extends BaseController {
    @Resource(name = "userService")
    private UserService userService;

    //转到前台天猫-登录页
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String goToPage(HttpSession session, Map<String, Object> map) {
        logger.info("转到前台天猫-登录页");
        return "fore/loginPage";
    }

    //登陆验证-ajax
    @ResponseBody
    @RequestMapping(value = "login/doLogin", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String checkLogin(HttpSession session, @RequestParam String username, @RequestParam String password) {
        logger.info("用户验证登录");
        //密码明文转密文
        password = MD5Util.getMD5(password);
        User user = userService.login(username, password);

        JSONObject jsonObject = new JSONObject();
        if (user == null) {
            logger.info("登录验证失败");
            jsonObject.put("success", false);
        } else {
            logger.info("登录验证成功,用户ID传入会话");
            session.setAttribute("userId", user.getUser_id());
            jsonObject.put("success", true);
        }
        return jsonObject.toJSONString();
    }

    //退出当前账号
    @RequestMapping(value = "login/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        Object o = session.getAttribute("userId");
        if (o != null) {
            session.removeAttribute("userId");
            session.invalidate();
            logger.info("登录信息已清除，返回用户登录页");
        }
        return "redirect:/login";
    }
}