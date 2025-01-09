package com.github.web;

import com.alibaba.fastjson.JSONObject;
import com.github.service.JNICLibraryV7_18_92ServiceWorker;
import com.github.service.TTEncryptServiceWorker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 控制类
 *
 * @author AnJia
 * @since 2021-07-26 18:31
 */
@Slf4j
@RestController
@RequestMapping(path = "/api/tt-encrypt", produces = MediaType.APPLICATION_JSON_VALUE)
public class TTEncryptController {

    @Resource(name = "ttEncryptWorker")
    private TTEncryptServiceWorker ttEncryptServiceWorker;

    @Autowired
    private JNICLibraryV7_18_92ServiceWorker jnicLibraryV7_18_92ServiceWorker;

    /**
     * 获取ttEncrypt
     * <p>
     * public byte[] ttEncrypt(@RequestParam(required = false) String key1, @RequestBody String body)
     * // 这是接收一个url参数，名为key1,接收一个post或者put请求的body参数
     * key1是选填参数，不写也不报错，值为,body只有在请求方法是POST时才有，GET没有
     *
     * @return 结果
     */
    @SneakyThrows
    @RequestMapping(value = "encrypt", method = {RequestMethod.GET, RequestMethod.POST})
    public byte[] ttEncrypt() {
//        String key1 = "key1";
//        String body = "body";
//        // 演示传参
        byte[] result = ttEncryptServiceWorker.ttEncrypt("", "").get();
//        log.info("入参:key1:{},body:{},result:{}", key1, body, result);
//        return result;
        jnicLibraryV7_18_92ServiceWorker.doCommandNative_70102("21407387",
            "ZzIHqOzQ5aMDAJJcdcnOfYdB&0&&21407387&885de8ae9325cdfae603f96088636f0d&1735205485&com.taobao.mtop.mloginservice.login&1.0&&10003993@fleamarket_android_7.18.92&6jH9GHFl66Sr7aWrJLfyHSnxoyAOd0CtBJOVmws01TDbliKYYMVKLAx1YKbLDX4p&0&0&openappkey=DEFAULT_AUTH&27&&&&&&&", "com.taobao.mtop.mloginservice.login");
        return null;
    }
}
