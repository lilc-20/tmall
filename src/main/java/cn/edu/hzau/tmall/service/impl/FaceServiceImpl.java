package cn.edu.hzau.tmall.service.impl;

import cn.edu.hzau.tmall.dao.AdminMapper;
import cn.edu.hzau.tmall.dao.FaceMapper;
import cn.edu.hzau.tmall.entity.Face;
import cn.edu.hzau.tmall.service.FaceService;

import javax.annotation.Resource;

public class FaceServiceImpl implements FaceService {

    private FaceMapper faceMapper;
    @Resource(name = "faceMapper")
    public void setFaceMapper(FaceMapper faceMapper) { this.faceMapper = faceMapper; }

    @Override
    public int addFace(Face face) {
        return faceMapper.insertOne(face);
    }
}
