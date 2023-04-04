package com.liudonghan.main;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:3/30/23
 */
public class ClassPoolUtils {
    private static volatile ClassPoolUtils instance = null;

    private ClassPoolUtils(){}

    public static ClassPoolUtils getInstance(){
     //single checkout
     if(null == instance){
        synchronized (ClassPoolUtils.class){
            // double checkout
            if(null == instance){
                instance = new ClassPoolUtils();
            }
        }
     }
     return instance;
    }

    public void getClassPool() throws NotFoundException, CannotCompileException, IOException {
        ClassPool.getDefault().insertClassPath("/Users/lawxp/Downloads/jar/com.heytap.msp_3.1.0.jar");
        CtClass c2 = ClassPool.getDefault().getCtClass("com.heytap.mcssdk.manage.NotificatonChannelManager");
        CtMethod aMethod = c2.getDeclaredMethod("createDefaultChannel");
        // 重置方法体
        aMethod.setBody("    {\n" +
                "        if (Build.VERSION.SDK_INT >= 26) {\n" +
                "            Runnable runnable = new Runnable() {\n" +
                "                public void run() {\n" +
                "                    if (!SharedPreferenceManager.getInstance().hasDefaultChannelCreated()) {\n" +
                "                        if (TextUtils.isEmpty(channelName)) {\n" +
                "                            channelName = \"System Default Channel\";\n" +
                "                        }\n" +
                "\n" +
                "                        boolean hasChannelCreated = NotificatonChannelManager.this.createChannel(context, \"Heytap PUSH\", channelName, 3);\n" +
                "                        SharedPreferenceManager.getInstance().setHasDefaultChannelCreated(hasChannelCreated);\n" +
                "                    }\n" +
                "                }\n" +
                "            };\n" +
                "            ThreadUtil.executeOnBackground(runnable);\n" +
                "        }\n" +
                "    }");
        c2.writeFile();

    }}
