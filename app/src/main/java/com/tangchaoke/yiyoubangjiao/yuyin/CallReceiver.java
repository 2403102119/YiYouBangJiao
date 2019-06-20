/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tangchaoke.yiyoubangjiao.yuyin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.model.HeadByOidModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class CallReceiver extends BroadcastReceiver {
    /**
     * 检查是否已经登录过
     *
     * @return
     */
    public boolean isLogined() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isLogined())
            return;
        //拨打方username
        String from = intent.getStringExtra("from");
        /**
         * 传对方的
         */
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(from, EaseCommonUtils.getConversationType(1), true);
        String mToHead = conversation.getLastMessage().getStringAttribute(EaseConstant.TO_HEAD, "");
        String mToName = conversation.getLastMessage().getStringAttribute(EaseConstant.TO_NAME, "");
        String mProblemOid = conversation.getLastMessage().getStringAttribute(EaseConstant.PROBLEM_OID, "");
        Log.e("==视频接收方:::", from + "---from---" + conversation.getLastMessage().getStringAttribute(EaseConstant.TO_HEAD, "") + "---mToHead---" + conversation.getLastMessage().getStringAttribute(EaseConstant.TO_NAME, "") + "---mToName---" + mProblemOid);
        //call type
        String type = intent.getStringExtra("type");
        if ("video".equals(type)) {
            //		    视频通话
            //		    context.startActivity(new Intent(context, VideoCallActivity.class).
            //		    putExtra("username", from).putExtra("isComingCall", true).
            //		    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            //音频通话
            context.startActivity(new Intent(context, VoiceCallActivity.class)
                    .putExtra("username", from)
                    .putExtra("to_id", from.toLowerCase())
                    .putExtra("to_head", mToHead)
                    .putExtra("to_name", mToName)
                    .putExtra("problem_oid", mProblemOid)
                    .putExtra("isComingCall", true).
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        EMLog.d("CallReceiver", "app received a incoming call");
    }

}
