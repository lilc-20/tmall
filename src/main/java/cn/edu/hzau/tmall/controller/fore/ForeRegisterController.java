package cn.edu.hzau.tmall.controller.fore;

import com.alibaba.fastjson.JSONObject;
import cn.edu.hzau.tmall.controller.BaseController;
import cn.edu.hzau.tmall.entity.Address;
import cn.edu.hzau.tmall.entity.User;
import cn.edu.hzau.tmall.service.AddressService;
import cn.edu.hzau.tmall.service.UserService;
import cn.edu.hzau.tmall.util.MD5Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 注册页
 * @author llc
 */
@Controller
public class ForeRegisterController extends BaseController{
    @Resource(name = "addressService")
    private AddressService addressService;
    @Resource(name="userService")
    private UserService userService;

    //转到前台-用户注册页
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String goToPage(Map<String,Object> map) {
        String addressId = "110000";
        String cityAddressId = "110100";//北京北京市
        logger.info("获取省份信息");
        List<Address> addressList = addressService.getRoot();
        logger.info("获取addressId为{}的市级地址信息", addressId);
        List<Address> cityAddress = addressService.getList(null, addressId);
        logger.info("获取cityAddressId为{}的区级地址信息", cityAddressId);
        List<Address> districtAddress = addressService.getList(null, cityAddressId);
        map.put("addressList", addressList);
        map.put("cityList", cityAddress);
        map.put("districtList", districtAddress);
        map.put("addressId", addressId);
        map.put("cityAddressId", cityAddressId);
        logger.info("转到前台-用户注册页");
        return "fore/register";
    }

    //前台-用户注册-ajax
    @ResponseBody
    @RequestMapping(value = "register/doRegister", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String register(
            HttpSession session,
            @RequestParam(value = "user_name") String user_name  /*用户名 */,
            @RequestParam(value = "user_phone") String user_phone  /*手机*/,
            @RequestParam(value = "user_phone_code") String user_phone_code  /*验证码*/,
            @RequestParam(value = "user_nickname") String user_nickname  /*用户昵称 */,
            @RequestParam(value = "user_password") String user_password  /*用户密码*/,
            @RequestParam(value = "user_gender") String user_gender  /*用户性别*/,
            @RequestParam(value = "user_birthday") String user_birthday /*用户生日*/,
            @RequestParam(value = "user_address") String user_address  /*用户所在地 */
    ) throws ParseException {
        logger.info("验证用户名是否存在");
        Integer count_name = userService.getTotal(new User().setUser_name(user_name));
        if (count_name > 0) {
            logger.info("用户名已存在，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("msg", "用户名已存在，请重新输入！");
            return object.toJSONString();
        }
        logger.info("验证手机号是否存在");
        Integer count_phone = userService.getPhone(user_phone);
        if (count_phone > 0) {
            logger.info("手机号已存在，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("msg_phone", "该手机号已注册！");
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
        User user = new User()
                .setUser_name(user_name)
                .setUser_phone(user_phone)
                .setUser_nickname(user_nickname)
                .setUser_password(MD5Util.getMD5(user_password))//密码明文转密文
                .setUser_gender(Byte.valueOf(user_gender))
                .setUser_birthday(new SimpleDateFormat("yyyy-MM-dd").parse(user_birthday))
                .setUser_address(new Address().setAddress_areaId(user_address))
                .setUser_homeplace(new Address().setAddress_areaId("130000"));
        logger.info("用户注册");
        if (userService.add(user)) {
            logger.info("注册成功");
            JSONObject object = new JSONObject();
            object.put("success", true);
            return object.toJSONString();
        } else {
            throw new RuntimeException();
        }
    }
}
