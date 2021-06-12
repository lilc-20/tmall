package cn.edu.hzau.tmall.entity.vo;

public class FaceLoginResult {

    private String state;//-1:未使用 0:false 1:true
    private String token;
    private String userId;

    public FaceLoginResult() {
    }

    @Override
    public String toString() {
        return "FaceLoginResult{" +
                "state='" + state + '\'' +
                ", token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public FaceLoginResult(String state, String token, String userId) {
        this.state = state;
        this.token = token;
        this.userId = userId;
    }
}
