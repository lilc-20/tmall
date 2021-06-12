package cn.edu.hzau.tmall.controller;

import cn.edu.hzau.tmall.entity.Face;
import cn.edu.hzau.tmall.service.UserService;
import cn.edu.hzau.tmall.util.AipFaceConfig;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

@Controller
public class FaceController extends BaseController {
    @Resource(name = "userService")
    private UserService userService;
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

    //获取二维码
    @ResponseBody
    @RequestMapping(value = "face/getQRcode" , method = RequestMethod.GET)
    public String getQRcode(HttpSession session) throws WriterException, IOException {

        logger.info("生成url地址");
        String content = "www.baidu.com";

        logger.info("生成二维码");
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);//二维码信息、图片类型、宽度、长度
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(image, "png", os);
        String encode = Base64Util.encode(os.toByteArray());
        System.out.println(new String("data:image/png;base64," + encode));

        JSONObject object = new JSONObject();
        object.put("success", true);
        return object.toString();
    }

    //识别人脸
    @ResponseBody
    @RequestMapping(value = "face/login" , method = RequestMethod.POST)
    public String register() {



        return "";
    }
}
