package com.req;

public class MerchantUserQueryReq extends PageReq {
    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String toString() {
        return "MerchantUserQueryReq{" +
                "loginName='" + loginName + '\'' +
                '}';
    }
}
