package com.github.taobao.wireless.security.adapter;

import com.github.constants.CommonConstants;
import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Emulator;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.file.FileResult;
import com.github.unidbg.file.IOResolver;
import com.github.unidbg.file.linux.AndroidFileIO;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.api.ApplicationInfo;
import com.github.unidbg.linux.android.dvm.api.ClassLoader;
import com.github.unidbg.linux.android.dvm.array.ArrayObject;
import com.github.unidbg.linux.android.dvm.jni.ProxyDvmObject;
import com.github.unidbg.linux.android.dvm.wrapper.DvmBoolean;
import com.github.unidbg.linux.android.dvm.wrapper.DvmInteger;
import com.github.unidbg.memory.Memory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 闲鱼安卓v7.12.30版本
 */
public class JNICLibraryV7_18_92 extends AbstractJni implements IOResolver<AndroidFileIO> {
    private final AndroidEmulator emulator;
    private final DvmClass JNICLibrary;
    private final VM vm;


    private static JNICLibraryV7_18_92 jnicLibraryV7_18_92 = null;

    public static synchronized JNICLibraryV7_18_92 getInstance() {
        if (jnicLibraryV7_18_92 == null) {
            jnicLibraryV7_18_92 = new JNICLibraryV7_18_92();
            jnicLibraryV7_18_92.init();
        }
        return jnicLibraryV7_18_92;
    }

    public JNICLibraryV7_18_92() {
        emulator = AndroidEmulatorBuilder
                .for64Bit()
                .addBackendFactory(new Unicorn2Factory(true))
                .setRootDir(new File("src/main/resources/IdlefishV7_18_92"))
                .setProcessName("com.taobao.idlefish")
                .build();
        Memory memory = emulator.getMemory();
        memory.setLibraryResolver(new AndroidResolver(23));
        vm = emulator.createDalvikVM(new File("src/main/resources/apk/idlefishv7.18.92.apk"));
        vm.setJni(this);
        vm.setVerbose(true);
        emulator.getSyscallHandler().addIOResolver(this);
        JNICLibrary = vm.resolveClass("com.taobao.wireless.security.adapter.JNICLibrary");
    }


    public void init() {
        doCommandNative_10101();
        doCommandNative_10102();
        // doCommandNative_20102();
    }


    /**
     * WUA
     *
     * @param currentTimeMillis
     * @param appKey
     * @return
     */
    public String doCommandNative_20102(String currentTimeMillis, String appKey) {
        ArrayObject arrayObject = new ArrayObject(
                new StringObject(vm, currentTimeMillis),
                new StringObject(vm, appKey),
                DvmInteger.valueOf(vm, 4),
                new StringObject(vm, ""),
                new StringObject(vm, ""),
                DvmInteger.valueOf(vm, 0));

        String result = JNICLibrary.callStaticJniMethodObject(emulator,
                "doCommandNative(I[Ljava/lang/Object;)Ljava/lang/Object;", 20102, arrayObject).getValue().toString();
        System.out.println("doCommandNative_20102_result:" + result);
        return result;
    }


    public Map<String, String> doCommandNative_70102(String appKey, String input, String api) {
        ArrayObject arrayObject = new ArrayObject(
                new StringObject(vm, appKey),
                new StringObject(vm, input),
                DvmBoolean.valueOf(vm, false),
                DvmInteger.valueOf(vm, 0),
                new StringObject(vm, api),
                new StringObject(vm, "pageId=&pageName="),
                ProxyDvmObject.createObject(vm, null),
                ProxyDvmObject.createObject(vm, null),
                ProxyDvmObject.createObject(vm, null),
                new StringObject(vm, "r_1"),
                DvmInteger.valueOf(vm, 0),
                DvmInteger.valueOf(vm, 0)
        );
        Map result = (Map) JNICLibrary.callStaticJniMethodObject(emulator,
                "doCommandNative(I[Ljava/lang/Object;)Ljava/lang/Object;", 70102, arrayObject).getValue();
        System.out.println("doCommandNative_70102_result:" + result.toString());
        return result;
    }

    @Override
    public DvmObject<?> callStaticObjectMethod(BaseVM vm, DvmClass dvmClass, String signature, VarArg varArg) {
        if (signature.equals("com/alibaba/wireless/security/mainplugin/SecurityGuardMainPlugin" +
                "->getMainPluginClassLoader()Ljava/lang/ClassLoader;")) {
            return new ClassLoader(vm, signature);
        }
        if (signature.equals("com/alibaba/wireless/security/securitybody/SecurityGuardSecurityBodyPlugin->getPluginClassLoader()Ljava/lang/ClassLoader;")) {
            return new ClassLoader(vm, signature);
        }
        if (signature.equals("com/taobao/dp/util/CallbackHelper->getInstance()Lcom/taobao/dp/util/CallbackHelper;")) {
            return vm.resolveClass(signature);
        }
        if (signature.equals("com/alibaba/wireless/security/middletierplugin/SecurityGuardMiddleTierPlugin->getPluginClassLoader()Ljava/lang/ClassLoader;")) {
            return new ClassLoader(vm, signature);
        }
        if (signature.equals("java/lang/Thread->currentThread()Ljava/lang/Thread;")) {
            return ProxyDvmObject.createObject(vm, Thread.currentThread());
        }
        if (signature.equals("com/alibaba/wireless/security/securitybody/SecurityBodyAdapter->doAdapter(I)Ljava/lang/String;")) {
            return ProxyDvmObject.createObject(vm, "-1");
        }
        if (signature.equals("com/taobao/wireless/security/adapter/datacollection/DeviceInfoCapturer->doCommandForString(I)Ljava/lang/String;")) {
            int intArg = varArg.getIntArg(0);
            if (intArg == 109) {
                return new StringObject(vm, "94:87:e0:33:fb:22");
            } else if (intArg == 126) {
                return new StringObject(vm, "unknown");
            } else if (intArg == 130) {
                return new StringObject(vm, "fb94c88161b58d8b");
            } else if (intArg == 135) {
                return new StringObject(vm, "ZzIHqOzQ5aMDAJJcdcnOfYdB");
            } else if (intArg == 146) {
                return new StringObject(vm, "27479101440");
            } else if (intArg == 104 || intArg == 105) {
                return ProxyDvmObject.createObject(vm, null);
            } else if (intArg == 122) {
                return new StringObject(vm, "com.taobao.idlefish");
            } else if (intArg == 123) {
                return new StringObject(vm, "7.18.92");
            }
        }
        if (signature.equals("com/taobao/wireless/security/adapter/common/SPUtility2->readFromSPUnified(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;")) {
            DvmObject<?> param1 = varArg.getObjectArg(0);
            DvmObject<?> param2 = varArg.getObjectArg(1);
            DvmObject<?> param3 = varArg.getObjectArg(2);

            if (null != param1 && null != param2 && null == param3) {
                if (param1.getValue().toString().equals("LOCAL_DEVICE_INFO") && param2.getValue().toString().equals("982c1b269b8e023e5aede2421cbf9c48")) {
                    return new StringObject(vm, "ZzIHqOzQ5aMDAJJcdcnOfYdB");
                }
            }
        }
        if (signature.equals("com/alibaba/wireless/security/securitybody/InvocationHandlerAdapter->getClassLoader()Ljava/lang/ClassLoader;")) {
            return new ClassLoader(vm, signature);
        }
        return super.callStaticObjectMethod(vm, dvmClass, signature, varArg);
    }

    long slot;

    @Override
    public void setStaticLongField(BaseVM vm, DvmClass dvmClass, String signature, long value) {
        if (signature.equals("com/alibaba/wireless/security/framework/SGPluginExtras->slot:J")) {
            this.slot = value;
            return;
        }
        super.setStaticLongField(vm, dvmClass, signature, value);
    }

    @Override
    public long getStaticLongField(BaseVM vm, DvmClass dvmClass, String signature) {
        if (signature.equals("com/alibaba/wireless/security/framework/SGPluginExtras->slot:J")) {
            return this.slot;
        }
        return super.getStaticLongField(vm, dvmClass, signature);
    }

    @Override
    public DvmObject<?> getObjectField(BaseVM vm, DvmObject<?> dvmObject, String signature) {
        if (signature.equals("android/content/pm/ApplicationInfo->nativeLibraryDir:Ljava/lang/String;")) {
            return new StringObject(vm, "/data/app/com.taobao.idlefish-1/lib/arm64");
        }
        return super.getObjectField(vm, dvmObject, signature);
    }

    Map<Object, Object> map = null;

    @Override
    public DvmObject<?> newObject(BaseVM vm, DvmClass dvmClass, String signature, VarArg varArg) {
        if (signature.equals("java/lang/Integer-><init>(I)V")) {
            return DvmInteger.valueOf(vm, varArg.getIntArg(0));
        }
        if (signature.equals("java/util/HashMap-><init>(I)V")) {
            map = new HashMap();
            return ProxyDvmObject.createObject(vm, map);
        }
        return super.newObject(vm, dvmClass, signature, varArg);
    }

    @Override
    public DvmObject<?> callObjectMethod(BaseVM vm, DvmObject<?> dvmObject, String signature, VarArg varArg) {
        if (signature.equals("com/taobao/idlefish/TaoBaoApplication->getPackageCodePath()Ljava/lang/String;")) {
            return new StringObject(vm, "/data/app/com.taobao.idlefish-1/idlefishv7.18.92.apk");
        }
        if (signature.equals("com/taobao/idlefish/TaoBaoApplication->getFilesDir()Ljava/io/File;")) {
            return vm.resolveClass("java/io/File").newObject("/data/user/0/com.taobao.idlefish/files");
        }
        if (signature.equals("java/io/File->getAbsolutePath()Ljava/lang/String;")) {
            return new StringObject(vm, "/data/user/0/com.taobao.idlefish/files");
        }
        if (signature.equals("com/taobao/idlefish/TaoBaoApplication->getApplicationInfo()Landroid/content/pm/ApplicationInfo;")) {
            return new ApplicationInfo(vm);
        }
        if (signature.equals("dalvik/system/PathClassLoader->findClass(Ljava/lang/String;)Ljava/lang/Class;")) {
            return vm.resolveClass(varArg.getObjectArg(0).getValue().toString());
        }
        if (signature.equals("android/content/Context->getFilesDir()Ljava/io/File;")) {
            return vm.resolveClass("java/io/File").newObject("/data/user/0/com.taobao.idlefish/files");
        }
        if (signature.equals("java/lang/Thread->getStackTrace()[Ljava/lang/StackTraceElement;")) {
            Thread thread = (Thread) dvmObject.getValue();
            return ProxyDvmObject.createObject(vm, thread.getStackTrace());
        }
        if (signature.equals("java/lang/StackTraceElement->toString()Ljava/lang/String;")) {
            StackTraceElement stackTraceElement = (StackTraceElement) dvmObject.getValue();
            return new StringObject(vm, stackTraceElement.toString());
        }
        if (signature.equals("android/content/Context->getClassLoader()Ljava/lang/ClassLoader;")) {
            return ProxyDvmObject.createObject(vm, this.getClass().getClassLoader());
        }
        if (signature.equals("dalvik/system/PathClassLoader->loadClass(Ljava/lang/String;)Ljava/lang/Class;")) {
            return vm.resolveClass(varArg.getObjectArg(0).getValue().toString());
        }
        if (signature.equals("java/lang/ClassLoader->loadClass(Ljava/lang/String;)Ljava/lang/Class;")) {
            return vm.resolveClass(varArg.getObjectArg(0).getValue().toString());
        }
        if (signature.equals("java/lang/StackTraceElement->getFileName()Ljava/lang/String;")) {
            StackTraceElement stackTraceElement = (StackTraceElement) dvmObject.getValue();
            return new StringObject(vm, stackTraceElement.getFileName());
        }
        if (signature.equals("java/lang/StackTraceElement->getClassName()Ljava/lang/String;")) {
            StackTraceElement stackTraceElement = (StackTraceElement) dvmObject.getValue();
            return new StringObject(vm, stackTraceElement.getClassName());
        }
        if (signature.equals("java/lang/StackTraceElement->getMethodName()Ljava/lang/String;")) {
            StackTraceElement stackTraceElement = (StackTraceElement) dvmObject.getValue();
            return new StringObject(vm, stackTraceElement.getMethodName());
        }
        if (signature.equals("java/util/HashMap->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")) {
            Object put = map.put(varArg.getObjectArg(0).getValue(), varArg.getObjectArg(1).getValue());
            return ProxyDvmObject.createObject(vm, put);
        }
        return super.callObjectMethod(vm, dvmObject, signature, varArg);
    }

    @Override
    public int callIntMethod(BaseVM vm, DvmObject<?> dvmObject, String signature, VarArg varArg) {
        if (signature.equals("java/lang/StackTraceElement->getLineNumber()I")) {
            StackTraceElement stackTraceElement = (StackTraceElement) dvmObject.getValue();
            return stackTraceElement.getLineNumber();
        }
        return super.callIntMethod(vm, dvmObject, signature, varArg);
    }

    private void doCommandNative_10101() {
        vm.loadLibrary("sgmainso-6.6.231201.33656539", true).callJNI_OnLoad(emulator);
        DvmObject<?> context = vm.resolveClass("com/taobao/idlefish/TaoBaoApplication", vm.resolveClass("android/content/Context")).newObject(null);
        ArrayObject arrayObject = new ArrayObject(
                context,
                DvmInteger.valueOf(vm, 3),
                new StringObject(vm, ""),
                new StringObject(vm, "/data/user/0/com.taobao.idlefish/app_SGLib"),
                new StringObject(vm, ""),
                new StringObject(vm, "com.taobao.idlefish"),
                new StringObject(vm, "7.18.92"),
                new StringObject(vm, "9"),
                new StringObject(vm, "com.taobao.idlefish"),
                DvmInteger.valueOf(vm, 0)
        );
        System.out.println("doCommandNative_10101_result:" + JNICLibrary.callStaticJniMethodObject(emulator,
                "doCommandNative(I[Ljava/lang/Object;)Ljava/lang/Object;", 10101, arrayObject).getValue().toString());
    }

    private void doCommandNative_10102() {
        // 初始化10102第一次
        ArrayObject arrayObject = new ArrayObject(
                new StringObject(vm, "main"),
                new StringObject(vm, "6.6.231201.33656539"),
                new StringObject(vm, "/data/app/com.taobao.idlefish-1==/lib/arm64/libsgmainso-6.6.231201.33656539.so")
        );
        System.out.println("doCommandNative_10102_main_result:" + JNICLibrary.callStaticJniMethodObject(emulator,
                "doCommandNative(I[Ljava/lang/Object;)Ljava/lang/Object;", 10102, arrayObject).getValue().toString());

        // 初始化10102第二次
        vm.loadLibrary("sgsecuritybodyso-6.6.231201.33656539", true).callJNI_OnLoad(emulator);
        arrayObject = new ArrayObject(
                new StringObject(vm, "securitybody"),
                new StringObject(vm, "6.6.231201.33656539"),
                new StringObject(vm, "/data/app/com.taobao.idlefish-1/lib/arm64/libsgsecuritybodyso-6.6.231201.33656539.so")
        );
        System.out.println("doCommandNative_10102_securitybody_result:" + JNICLibrary.callStaticJniMethodObject(emulator,
                "doCommandNative(I[Ljava/lang/Object;)Ljava/lang/Object;", 10102, arrayObject).getValue().toString());

        // 初始化10102第三次
        vm.loadLibrary("sgmiddletierso-6.6.231201.33656539", true).callJNI_OnLoad(emulator);
        arrayObject = new ArrayObject(
                new StringObject(vm, "middletier"),
                new StringObject(vm, "6.6.231201.33656539"),
                new StringObject(vm, "/data/app/com.taobao.idlefish-1/lib/arm64/libsgmiddletierso-6.6.231201.33656539.so")
        );
        System.out.println("doCommandNative_10102_middletier_result:" + JNICLibrary.callStaticJniMethodObject(emulator,
                "doCommandNative(I[Ljava/lang/Object;)Ljava/lang/Object;", 10102, arrayObject).getValue().toString());
    }

    @Override
    public int getStaticIntField(BaseVM vm, DvmClass dvmClass, String signature) {
        if (signature.equals("android/os/Build$VERSION->SDK_INT:I")) {
            return 28;
        }
        return super.getStaticIntField(vm, dvmClass, signature);
    }

    @Override
    public int callStaticIntMethod(BaseVM vm, DvmClass dvmClass, String signature, VarArg varArg) {
        if (signature.equals("com/uc2/crashsdk/JNIBridge->registerInfoCallback(Ljava/lang/String;IJI)I")) {
            return 1;
        }
        if (signature.equals("com/alibaba/wireless/security/framework/utils/UserTrackMethodJniBridge->utAvaiable()I")) {
            return 1;
        }
        if (signature.equals("com/taobao/wireless/security/adapter/common/SPUtility2->saveToFileUnifiedForNative" +
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)I")) {
            return 0;
        }
        return super.callStaticIntMethod(vm, dvmClass, signature, varArg);
    }

    @Override
    public boolean callStaticBooleanMethod(BaseVM vm, DvmClass dvmClass, String signature, VarArg varArg) {
        if (signature.equals("com/alibaba/wireless/security/framework/ApmMonitorAdapter->isEnableFullTrackRecord()Z")) {
            return false;
        }
        return super.callStaticBooleanMethod(vm, dvmClass, signature, varArg);
    }

    public static void main(String[] args) {
        JNICLibraryV7_18_92 jnicLibraryV7_18_92 = new JNICLibraryV7_18_92();
        jnicLibraryV7_18_92.init();
        String s = jnicLibraryV7_18_92.doCommandNative_20102(System.currentTimeMillis() + "", CommonConstants.appKey);
        System.out.println(s);

    }

    @Override
    public FileResult<AndroidFileIO> resolve(Emulator emulator, String pathname, int oflags) {
        return null;
    }


}

