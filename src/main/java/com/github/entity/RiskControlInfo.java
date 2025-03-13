package com.github.entity;

import com.github.constants.CommonConstants;

import java.util.HashMap;
import java.util.Map;

public class RiskControlInfo {
    public String apdId = CommonConstants.appId;
    public String deviceBrand = CommonConstants.deviceBrand;
    public String deviceModel = CommonConstants.deviceModel;
    public String deviceName = CommonConstants.loginInfo_deviceName;
    public Map<String, Object> extRiskData = new HashMap<>();
    public String t;
    public String umidToken = CommonConstants.umidToken;
    public String wua;
}
