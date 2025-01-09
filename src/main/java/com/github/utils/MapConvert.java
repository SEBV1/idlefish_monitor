package com.github.utils;


import com.github.constants.XStateConstants;

import java.util.HashMap;
import java.util.Map;

public class MapConvert {
    public static void m14m(StringBuilder sb, String str, String str2, String str3, String str4) {
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        sb.append(str4);
    }


    public static void m(StringBuilder sb, String str, String str2, String str3) {
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
    }

    public static HashMap convertInnerBaseStrMap(String str, Map map, boolean z) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        String str2 = (String) map.get("utdid");
        String str3 = (String) map.get("uid");
        String str4 = (String) map.get(XStateConstants.KEY_REQBIZ_EXT);
        String str5 = (String) map.get("data");
        String str6 = (String) map.get("t");
        String str7 = (String) map.get("api");
        String str8 = (String) map.get("v");
        String str9 = (String) map.get("sid");
        String str10 = (String) map.get("ttid");
        String str11 = (String) map.get("deviceId");
        String str12 = (String) map.get("lat");
        String str13 = (String) map.get("lng");
        String str14 = (String) map.get("extdata");
        String str15 = (String) map.get("x-features");
        String str16 = (String) map.get(XStateConstants.KEY_ROUTER_ID);
        String str17 = (String) map.get(XStateConstants.KEY_PLACE_ID);
        String str18 = (String) map.get(XStateConstants.KEY_OPEN_BIZ);
        String str19 = (String) map.get(XStateConstants.KEY_MINI_APPKEY);
        String str20 = (String) map.get(XStateConstants.KEY_REQ_APPKEY);
        String str21 = (String) map.get(XStateConstants.KEY_ACCESS_TOKEN);
        String str22 = (String) map.get(XStateConstants.KEY_OPEN_BIZ_DATA);
        StringBuilder sb = new StringBuilder(64);
        if (str2 == null) {
            str2 = "";
        }
        sb.append(str2);
        sb.append("&");
        if (str3 == null) {
            str3 = "";
        }
        sb.append(str3);
        sb.append("&");
        if (str4 == null) {
            str4 = "";
        }
        m14m(sb, str4, "&", str, "&");
        sb.append(SecurityUtils.getMd5(str5));
        sb.append("&");
        sb.append(str6);
        sb.append("&");
        sb.append(str7);
        m(sb, "&", str8, "&");
        if (str9 == null) {
            str9 = "";
        }
        sb.append(str9);
        sb.append("&");
        if (str10 == null) {
            str10 = "";
        }
        sb.append(str10);
        sb.append("&");
        if (str11 == null) {
            str11 = "";
        }
        sb.append(str11);
        sb.append("&");
        if (str12 == null) {
            str12 = "";
        }
        sb.append(str12);
        sb.append("&");
        if (str13 == null) {
            str13 = "";
        }
        sb.append(str13);
        sb.append("&");
        if (z) {
            if (str14 == null) {
                str14 = "";
            }
            sb.append(str14);
            sb.append("&");
        } else if (StringUtils.isNotBlank(str14)) {
            sb.append(str14);
            sb.append("&");
        }
        sb.append(str15);
        sb.append("&");
        sb.append(str16 == null ? "" : str16);
        sb.append("&");
        sb.append(str17 == null ? "" : str17);
        sb.append("&");
        sb.append(str18 == null ? "" : str18);
        sb.append("&");
        sb.append(str19 == null ? "" : str19);
        sb.append("&");
        sb.append(str20 == null ? "" : str20);
        sb.append("&");
        sb.append(str21 == null ? "" : str21);
        sb.append("&");
        sb.append(str22 != null ? str22 : "");
        HashMap hashMap = new HashMap(2);
        hashMap.put("INPUT", sb.toString());
        return hashMap;
    }
}
