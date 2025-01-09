package com.github.utils;

import okhttp3.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

public class OkHttpUtil {
    private static final OkHttpClient client = new OkHttpClient();

    // POST请求，表单参数
    public static String postForm(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody body = formBuilder.build();

        Request.Builder builder = new Request.Builder()
            .url(url)
            .post(body);

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            assert response.body() != null;
            return response.body().string();
        }
    }

    public static String postForm(String url, Map<String, String> headers, String data) throws IOException {

//        MediaType mediaType = MediaType.parse(" application/x-www-form-urlencoded;charset=UTF-8");
//        RequestBody body = RequestBody.create(mediaType, "data=" + URLEncoder.encode(data, "UTF-8"));
//
//        Request.Builder builder = new Request.Builder()
//                .url(url)
//                .post(body);
//
//        if (headers != null && !headers.isEmpty()) {
//            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                builder.addHeader(entry.getKey(), entry.getValue());
//            }
//        }
//
//
//
//        Request request = builder.build();
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("Unexpected code " + response);
//            }
//            assert response.body() != null;
//            return response.body().string();
//        }


        OkHttpClient client = new OkHttpClient.Builder()
            .hostnameVerifier((hostname, session) -> {
                // 这里简单返回 true 表示接受所有主机名
                // 实际应用中你可以实现更复杂的验证逻辑
                return true;
            })
            .build();

        MediaType mediaType = MediaType.parse(" application/x-www-form-urlencoded;charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, "data=" + URLEncoder.encode(data, "UTF-8"));

        Request.Builder builder = new Request.Builder()
            .url(url)
            .post(body);

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            assert response.body() != null;
            return response.body().string();
        }
    }
}
