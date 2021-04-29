package cn.edu.hzau.tmall.entity;

/**
 * 短信实体类
 * @author llc
 */
public class Sms {
    private String phoneNumber;
    private String code;
    private int min;
    private String templateId;

    @Override
    public String toString() {
        return "Sms{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", code='" + code + '\'' +
                ", min=" + min +
                ", templateId='" + templateId + '\'' +
                '}';
    }

    public Sms() {
    }

    public Sms(String phoneNumber, String code, int min, String templateId) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.min = min;
        this.templateId = templateId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
