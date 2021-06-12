package cn.edu.hzau.tmall.entity;

public class Face {
    private String user_face_picture_src; //人脸图片地址
    private String user_name; //用户名

    public Face() {
    }

    public Face(String user_face_picture_src, String user_name) {
        this.user_face_picture_src = user_face_picture_src;
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "Face{" +
                "user_face_picture_src='" + user_face_picture_src + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }

    public String getUser_face_picture_src() {
        return user_face_picture_src;
    }

    public void setUser_face_picture_src(String user_face_picture_src) {
        this.user_face_picture_src = user_face_picture_src;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
