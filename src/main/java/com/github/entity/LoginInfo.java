package com.github.entity;

import com.github.constants.CommonConstants;

public class LoginInfo {
    public String appName = CommonConstants.appName;
    public String appVersion = CommonConstants.appVersion;
    public String deviceName = CommonConstants.loginInfo_deviceName;
    public LoginExtInfo ext;
    public String locale = CommonConstants.locale;
    public String loginId;
    public String loginType = CommonConstants.loginType;
    public String password;
    public boolean pwdEncrypted = true;
    public String sdkVersion = CommonConstants.sdkVersion;
    public int site = CommonConstants.site;
    public long t;
    public String ttid = CommonConstants.ttid;
    public boolean useAcitonType = true;
    public boolean useDeviceToken = true;
    public String utdid = CommonConstants.utdid;
}
