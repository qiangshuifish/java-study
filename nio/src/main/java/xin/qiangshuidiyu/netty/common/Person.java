package xin.qiangshuidiyu.netty.common;

import org.msgpack.annotation.Message;

/**
 * @author wpy
 * @date 2018/6/28 11:06
 */
@Message
public class Person {
    private String name;
    private Integer age;
    private String mobile;
    private String email;

    public Person() {
    }

    public Person(String name, Integer age, String mobile, String email) {
        this.name = name;
        this.age = age;
        this.mobile = mobile;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
