package cn.edu.hzau.tmall.controller;

import cn.edu.hzau.tmall.entity.Face;
import cn.edu.hzau.tmall.entity.User;
import cn.edu.hzau.tmall.service.FaceService;
import cn.edu.hzau.tmall.service.UserService;
import cn.edu.hzau.tmall.util.AipFaceConfig;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

@Controller
public class FaceController extends BaseController {
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "faceService")
    private FaceService faceService;
    //创建与百度云ai交互client
    AipFace client = new AipFace(AipFaceConfig.appId, AipFaceConfig.apiKey, AipFaceConfig.secretKey);
    //参数设置
    HashMap<String, String> options = new HashMap<>();

    //转到前台人脸注册页面
    @RequestMapping(value = "face/register", method = RequestMethod.GET)
    public String goToPage() {
        logger.info("跳转注册人脸页面");
        return "fore/faceRegister";
    }

    //上传人脸图片
    @ResponseBody
    @RequestMapping(value = "face/uploadUserFaceImage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public  String uploadUserHeadImage(@RequestParam MultipartFile file, HttpSession session
    ){
        String originalFileName = file.getOriginalFilename();
        logger.info("获取图片原始文件名：{}", originalFileName);
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + extension;
        String filePath = session.getServletContext().getRealPath("/") + "res/images/item/userFacePicture/" + fileName;
        logger.info("文件上传路径：{}", filePath);
        JSONObject jsonObject = new JSONObject();
        try {
            logger.info("文件上传中...");
            file.transferTo(new File(filePath));
            logger.info("文件上传成功！");
            jsonObject.put("success", true);
            jsonObject.put("fileName", fileName);
        } catch (IOException e) {
            logger.warn("文件上传失败！");
            e.printStackTrace();
            jsonObject.put("success", false);
        }
        return jsonObject.toJSONString();
    }

    //注册人脸
    @ResponseBody
    @RequestMapping(value = "face/doRegister" , method = RequestMethod.POST)
    public String register(HttpSession session,
                           @RequestParam(value = "user_name") String user_name,
                           @RequestParam(value = "user_phone") String user_phone,
                           @RequestParam(value = "user_phone_code") String user_phone_code,
                           @RequestParam(value = "user_face_picture_src") String user_face_picture_src) throws IOException {
        logger.info("验证用户名手机号是否匹配");
        Integer count = userService.checkPhone(user_name, user_phone);
        if (count == 0) {
            logger.info("用户名手机号不匹配，返回错误信息!");
            JSONObject object = new JSONObject();
            object.put("success", false);
            object.put("msg", "用户名或手机号错误，请重新输入！");
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

        logger.info("注册人脸信息");
        Face face = new Face();
        face.setUser_face_picture_src(user_face_picture_src);
        face.setUser_name(user_name);
        faceService.addFace(face);

        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "NONE");

        String filePath = session.getServletContext().getRealPath("/") + "res/images/item/userFacePicture/" + user_face_picture_src;
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        String encode = Base64Util.encode(bytes);

        org.json.JSONObject result = client.addUser(encode, "BASE64", "hzau", user_name, options);
        System.out.println(result.toString());

        JSONObject object = new JSONObject();
        object.put("success", true);

        return object.toString();
    }



    //人脸登录
    @ResponseBody
    @RequestMapping(value = "face/login" , method = RequestMethod.POST)
    public String register(HttpSession session, @RequestParam(value = "face") String face) {

        //System.out.println(face);//data:image/png;base64,...
        face = face.substring(22);
//        System.out.println(face);

        logger.info("人脸搜索");

        String user_name = "";
        org.json.JSONObject res = client.search(face, "BASE64", "hzau", options);
        //error_code
        if (res.has("error_code") && res.getInt("error_code") == 0) {
            //user_list
            org.json.JSONObject result = res.getJSONObject("result");
            JSONArray user_list = result.getJSONArray("user_list");
            if (user_list.length() > 0) {
                org.json.JSONObject user = user_list.getJSONObject(0);
                double score = user.getDouble("score");
                System.out.println(score);
                if (score > 80) {
                    user_name = user.getString("user_id");
                }
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", false);
        if (!user_name.equals("")) {
            User user = userService.faceLogin(user_name);

            if (user == null) {
                logger.info("登录验证失败");
            } else {
                logger.info("登录验证成功,用户ID传入会话");
                session.setAttribute("userId", user.getUser_id());
                jsonObject.put("success", true);
            }
        }

        return jsonObject.toString();
    }

}
