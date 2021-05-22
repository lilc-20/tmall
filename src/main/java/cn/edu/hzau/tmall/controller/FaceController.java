package cn.edu.hzau.tmall.controller;

import cn.edu.hzau.tmall.util.AipFaceConfig;
import com.baidu.aip.face.AipFace;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FaceController extends BaseController {
    //创建与百度云ai交互client
    AipFace client = new AipFace(AipFaceConfig.appId, AipFaceConfig.apiKey, AipFaceConfig.secretKey);

    //register
    @RequestMapping(value = "face/register", method = RequestMethod.POST)
    public String faceRegister() {
        logger.info("注册人脸");

        return "";
    }
}
