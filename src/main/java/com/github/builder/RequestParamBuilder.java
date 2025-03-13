package com.github.builder;

import cn.hutool.core.map.MapUtil;
import com.github.constants.CommonConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestParamBuilder {

    private static final AtomicInteger counter = new AtomicInteger();

    public static String createClientTraceId() {
        StringBuilder sb = new StringBuilder(64);
        sb.append(CommonConstants.utdid);
        sb.append(System.currentTimeMillis());
        int intSeqNo = createIntSeqNo();
        sb.append(new DecimalFormat("0000").format(intSeqNo % 10000));
        sb.append("1");
        sb.append(CommonConstants.pid);
        return sb.toString();
    }

    public static int createIntSeqNo() {
        return counter.incrementAndGet() & Integer.MAX_VALUE;
    }

    public static String generateFalcoId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Map<String, String> builderParam(String t, String dataJson, String api) {
        return MapUtil.builder(new HashMap<String, String>())
                .put("deviceId", CommonConstants.deviceId)
                .put("sid", null)
                .put("uid", CommonConstants.uid)
                .put("x-features", CommonConstants.x_features)
                .put("appKey", CommonConstants.appKey)
                .put("api", api)
                .put("lat", CommonConstants.lat)
                .put("lng", CommonConstants.lng)
                .put("mtopBusiness", "true")
                .put("utdid", CommonConstants.utdid)
                .put("extdata", CommonConstants.extdata)
                .put("ttid", CommonConstants.ttid)
                .put("t", t)
                .put("v", CommonConstants.v)
                .put("data", dataJson).build();
    }


    public static Map<String, String> builderHeaders(String x_sgext, String x_sign, String x_mini_wua, String t,
                                                     String x_umt, String cookie) throws UnsupportedEncodingException {
        Map<String, String> headers = MapUtil.builder(new HashMap<String, String>())
                .put("x-sgext", URLEncoder.encode(x_sgext, "UTF-8"))
                .put("x-sign", URLEncoder.encode(x_sign, "UTF-8"))
                .put("x-uid", URLEncoder.encode(RequestHeader.x_uid, "UTF-8"))
                .put("x-nettype", URLEncoder.encode(RequestHeader.x_nettype, "UTF-8"))
                .put("x-pv", URLEncoder.encode(RequestHeader.x_pv, "UTF-8"))
                .put("x-nq", URLEncoder.encode(RequestHeader.x_nq, "UTF-8"))
                .put("login_sdk_version", URLEncoder.encode(RequestHeader.login_sdk_version, "UTF-8"))
                .put("x-features", URLEncoder.encode(RequestHeader.x_features, "UTF-8"))
                .put("x-app-conf-v", URLEncoder.encode(RequestHeader.x_app_conf_v, "UTF-8"))
                .put("x-mini-wua", URLEncoder.encode(x_mini_wua, "UTF-8"))
                .put("x-t", URLEncoder.encode(t, "UTF-8"))
                .put("x-bx-version", URLEncoder.encode(RequestHeader.x_bx_version, "UTF-8"))
                .put("f-refer", URLEncoder.encode(RequestHeader.f_refer, "UTF-8"))
                .put("x-extdata", URLEncoder.encode(CommonConstants.extdata, "UTF-8"))
                .put("x-ttid", URLEncoder.encode(CommonConstants.ttid, "UTF-8"))
                .put("x-app-ver", URLEncoder.encode(RequestHeader.x_app_ver, "UTF-8"))
                .put("x-c-traceid", URLEncoder.encode(createClientTraceId(), "UTF-8"))
                .put("x-location", URLEncoder.encode(RequestHeader.x_location, "UTF-8"))
                .put("x-umt", URLEncoder.encode(x_umt, "UTF-8"))
                .put("a-orange-q", URLEncoder.encode(RequestHeader.a_orange_q, "UTF-8"))
                .put("x-utdid", URLEncoder.encode(CommonConstants.utdid, "UTF-8"))
                .put("c-launch-info", URLEncoder.encode(RequestHeader.c_launch_info, "UTF-8"))
                .put("x-appkey", URLEncoder.encode(CommonConstants.appKey, "UTF-8"))
                .put("x-falco-id", URLEncoder.encode(generateFalcoId(), "UTF-8"))
                .put("x-devid", URLEncoder.encode(CommonConstants.deviceId, "UTF-8"))
                .put("user-agent", URLEncoder.encode(RequestHeader.user_agent, "UTF-8"))
                .put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8").build();
        if (cookie != null) {
            headers.put("Cookie", cookie);
        }
        return headers;
    }

}
