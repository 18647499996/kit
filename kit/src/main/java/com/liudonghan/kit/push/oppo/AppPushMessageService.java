package com.liudonghan.kit.push.oppo;

import android.content.Context;
import android.util.Log;

import com.heytap.msp.push.mode.DataMessage;
import com.heytap.msp.push.service.DataMessageCallbackService;

public class AppPushMessageService extends DataMessageCallbackService {

    /**
     * 透传消息处理，应用可以打开页面或者执行命令,如果应用不需要处理透传消息，则不需要重写此方法
     *
     * @param context 上下文
     * @param message 推送消息
     */
    @Override
    public void processMessage(Context context, DataMessage message) {
        super.processMessage(context, message);
        Log.w("Mac_Liu", "OPPO Push DataMessageCallbackService：" + message.toString());
    }

}
