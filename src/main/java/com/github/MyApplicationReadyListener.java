package com.github;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.builder.RequestParamBuilder;
import com.github.constants.CommonConstants;
import com.github.entity.ExtInfo;
import com.github.entity.LoginExtInfo;
import com.github.entity.LoginInfo;
import com.github.entity.RiskControlInfo;
import com.github.taobao.wireless.security.adapter.JNICLibraryV7_18_92;
import com.github.utils.MapConvert;
import com.github.utils.OkHttpUtil;
import com.github.utils.Rsa;
import com.github.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MyApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {


    @Value("${user.account}")
    private String account;
    @Value("${user.password}")
    private String password;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final String SUCCESS_CODE = "3000";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!StringUtils.isEmpty(account) && !StringUtils.isEmpty(password)) {
            password = Rsa.encrypt(password);

            long currentTimeSeconds = System.currentTimeMillis() / 1000;
            long currentTimeMillis = System.currentTimeMillis();
            String currentTimeStr = String.valueOf(currentTimeMillis);

            ExtInfo extInfo = new ExtInfo();
            extInfo.sdkTraceId = String.format(extInfo.sdkTraceId, currentTimeSeconds);

            LoginExtInfo loginExtInfo = new LoginExtInfo();
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.ext = loginExtInfo;
            loginInfo.loginId = account;
            loginInfo.t = currentTimeMillis;
            try {
                loginInfo.password = password;
            } catch (Exception e) {
                log.error("加密密码时出错: {}", e.getMessage());
                return;
            }

            RiskControlInfo riskControlInfo = new RiskControlInfo();
            riskControlInfo.t = currentTimeStr;

            JNICLibraryV7_18_92 jnicLibraryV7_18_92 = new JNICLibraryV7_18_92();
            jnicLibraryV7_18_92.init();
            riskControlInfo.wua = jnicLibraryV7_18_92.doCommandNative_20102(currentTimeStr, CommonConstants.appKey);

            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("ext", JSONObject.toJSONString(extInfo));
            jsonMap.put("loginInfo", JSONObject.toJSONString(loginInfo));
            jsonMap.put("riskControlInfo", JSONObject.toJSONString(riskControlInfo));
            String data = JSONObject.toJSONString(jsonMap);
            System.out.println(data);

            String t3 = String.valueOf(currentTimeSeconds);
            Map<String, String> param = RequestParamBuilder.builderParam(t3, data, CommonConstants.loginApi);
            String input = MapConvert.convertInnerBaseStrMap(CommonConstants.appKey, param, true).get("INPUT").toString();
            Map<String, String> signResult = jnicLibraryV7_18_92.doCommandNative_70102(CommonConstants.appKey, input, CommonConstants.searchApi);
            Map<String, String> headers = null;
            try {
                headers = RequestParamBuilder.builderHeaders(signResult.get("x-sgext"), signResult.get("x-sign"), signResult.get("x-mini-wua"), t3, signResult.get("x-umt"), null);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            param.clear();
            try {
                param.put("data", URLEncoder.encode(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("URL编码数据时出错: {}", e.getMessage());
                return;
            }

            String url = "https://acs.m.goofish.com/gw/com.taobao.mtop.mloginservice.login/1.0/";
            String result;
            try {
                result = OkHttpUtil.postForm(url, headers, data);
                JSONObject jsonObject = JSONObject.parseObject(result);
                JSONObject dataJson = jsonObject.getJSONObject("data");
                String code = dataJson.getString("code");
                String message = dataJson.getString("message");
                log.info("账号登录结果: code={}, message={}", code, message);
                if (SUCCESS_CODE.equals(code)) {
                    String cookie = extractCookie(result);
                    redisTemplate.opsForValue().set("cookie", cookie);
                }

            } catch (IOException e) {
                log.error("发送POST请求时出错: {}", e.getMessage());
                return;
            }
        }
    }


    public static String extractCookie(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String data = jsonObject.getJSONObject("data").getJSONObject("returnValue").getString("data");
        JSONObject jsonObjectData = JSONObject.parseObject(data);

        String unbValue = extractUnbValue(jsonObjectData);
        StringBuilder buffer = new StringBuilder();
        if (unbValue != null) {
            buffer.append(unbValue).append("; ");
        }

        appendCookies(buffer, jsonObjectData);

        if (buffer.length() > 0) {
            return buffer.substring(0, buffer.length() - 2);
        }
        return "";
    }


    private static String extractUnbValue(JSONObject jsonObjectData) {
        String externalCookies = jsonObjectData.getString("externalCookies");
        if (externalCookies != null && externalCookies.startsWith("[\"") && externalCookies.endsWith("\"]")) {
            String trimmedInput = externalCookies.substring(2, externalCookies.length() - 2);
            String[] parts = trimmedInput.split("; ");
            for (String part : parts) {
                if (part.startsWith("unb=")) {
                    return part;
                }
            }
        }
        return null;
    }


    private static void appendCookies(StringBuilder buffer, JSONObject jsonObjectData) {
        JSONArray cookies = jsonObjectData.getJSONArray("cookies");
        if (cookies != null) {
            for (Object cookie : cookies) {
                String part = cookie.toString().split(";")[0];
                buffer.append(part).append("; ");
            }
        }
    }
}