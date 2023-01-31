package com.liudonghan.kit;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/31/23
 */
public class AliPayUtils {
    private static volatile AliPayUtils instance = null;

    private AliPayUtils(){}

    public static AliPayUtils getInstance(){
     //single chcekout
     if(null == instance){
        synchronized (AliPayUtils.class){
            // double checkout
            if(null == instance){
                instance = new AliPayUtils();
            }
        }
     }
     return instance;
    }

    public void pay(){

    }
}
