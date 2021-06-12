package cn.edu.hzau.tmall.dao;

import cn.edu.hzau.tmall.entity.Face;
import org.apache.ibatis.annotations.Param;

public interface FaceMapper {

    Integer insertOne(@Param("face")Face face);

}
