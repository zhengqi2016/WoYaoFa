package com.woyaofa.bean;

/**
 * Created by LoaR on 15/11/23.
 */
public class AccountBean extends com.lib_common.bean.UserBean {
    private int id;                    //唯一ID
    private String name;                //账号
    private String password;            //密码
    private long lastLoginTime;            //最后一次登录时间
    private long createTime;            //账号创建时间
    private Integer isDeleted;            //0表示未删除，1表示已删除
    private UserBean user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
