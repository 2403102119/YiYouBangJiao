package com.tangchaoke.yiyoubangjiao.tuisong;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMMessage.Status;
import com.hyphenate.chat.EMMessage.Type;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.model.EaseNotifier.EaseNotificationInfoProvider;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Chat;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Main;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hx.PreferenceManager;
import com.tangchaoke.yiyoubangjiao.yuyin.CallReceiver;

import java.util.List;
import java.util.UUID;

public class DemoHelper {

    protected static final String TAG = "DemoHelper";

    private EaseUI easeUI;

    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

    private static DemoHelper instance = null;

    public boolean isVoiceCalling;
    public boolean isVideoCalling;

    private Context appContext;

    private CallReceiver callReceiver;

    public synchronized static DemoHelper getInstance() {
        if (instance == null) {
            instance = new DemoHelper();
        }
        return instance;
    }

    /**
     * 帮助程序
     *
     * @param context 应用上下文
     */
    public void init(Context context) {
        EMOptions options = initChatOptions();

        //如果选项为空，则使用默认选项
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;
            //调试模式，如果您想正式发布您的应用程序，最好将其设置为false。
            EMClient.getInstance().setDebugMode(true);
            //得到easeui实例
            easeUI = EaseUI.getInstance();
            //设置用户的个人资料和头像
            setEaseUIProviders();
            //initialize preference manager
            PreferenceManager.init(context);
            setGlobalListeners();
            HeadsetReceiver headsetReceiver = new HeadsetReceiver();
            IntentFilter headsetFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
            appContext.registerReceiver(headsetReceiver, headsetFilter);
        }

    }

    private EMOptions initChatOptions() {
        Log.d(TAG, "init HuanXin Options");
        EMOptions options = new EMOptions();
        /**
         * NOTE:你需要设置自己申请的Sender ID来使用Google推送功能，详见集成文档
         */
        options.setFCMNumber("921300338324");
        //如果您想使用Mi推送通知，您需要申请并设置您自己的ID
        options.setMipushConfig("2882303761517426801", "5381742660801");
        return options;
    }

    protected void setEaseUIProviders() {

//        设置通知选项，如果不设置，将使用默认值
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //你可以在这里更新标题
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //你可以在这里更新图标
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // 在通知栏上使用，根据消息类型不同的文本。
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                return message.getFrom() + ": " + ticker;
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // 在这里你可以自定义文本。
                // 从用户号码+“联系人发送”+ messageNum +“消息给你”返回;
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // 您可以设置用户单击通知时要显示的活动
                Intent intent = new Intent(appContext, Activity_Chat.class);
                // 如果有电话打开呼叫活动
                if (isVideoCalling) {
                    intent = new Intent(appContext, Activity_Main.class);
                } else if (isVoiceCalling) {
                    intent = new Intent(appContext, Activity_Main.class);
                } else {
                    ChatType chatType = message.getChatType();
                    if (chatType == ChatType.Chat) {
                        // single chat message
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                    } else { // group chat message
                        // message.getTo() is the group id
                        intent.putExtra("userId", message.getTo());
                        if (chatType == ChatType.GroupChat) {
                            intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                        } else {
                            intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
                        }

                    }
                }
                intent.putExtra("oid", message.getStringAttribute(EaseConstant.PROBLEM_OID, ""));
                intent.putExtra(EaseConstant.PROBLEM_OID, message.getStringAttribute(EaseConstant.PROBLEM_OID, ""));//问题ID
                intent.putExtra(EaseConstant.ME_ID, BaseApplication.getApplication().getOid());//自己的ID
                intent.putExtra(EaseConstant.ME_HEAD, BaseApplication.getApplication().getHead());//自己的头像
                intent.putExtra(EaseConstant.ME_NAME, BaseApplication.getApplication().getNickName());//自己的名字
                return intent;
            }
        });

    }

    EMConnectionListener connectionListener;

    /**
     * set global listener
     */
    protected void setGlobalListeners() {
        // 创建全局连接监听器
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                EMLog.d("global listener", "onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {
                    onUserException(Constant.ACCOUNT_REMOVED);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    onUserException(Constant.ACCOUNT_CONFLICT);
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
                    onUserException(Constant.ACCOUNT_FORBIDDEN);
                } else if (error == EMError.USER_KICKED_BY_CHANGE_PASSWORD) {
                    onUserException(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD);
                } else if (error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                    onUserException(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE);
                }
            }

            @Override
            public void onConnected() {

            }
        };

        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }
        //注册来电接听者
        appContext.registerReceiver(callReceiver, callFilter);

        //注册连接监听器
        EMClient.getInstance().addConnectionListener(connectionListener);
        //注册消息事件监听器
        registerMessageListener();

    }

    /**
     * 用户遇到了一些异常：冲突，删除或禁止
     */
    protected void onUserException(String exception) {
        EMLog.e(TAG, "onUserException: " + exception);
        Intent intent = new Intent(appContext, Activity_Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.putExtra(exception, true);
        appContext.startActivity(intent);
    }

    /**
     * Global listener
     * If this event already handled by an activity, you don't need handle it again
     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
     */
    protected void registerMessageListener() {

        messageListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    //在后台，不要刷新UI，在通知栏中通知它
                    if (!easeUI.hasForegroundActivies()) {
                        getNotifier().onNewMsg(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "receive command message");
                    //获取邮件正文
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action
                    //获取扩展属性 此处省略
                    //也许你需要扩展你的信息
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action, message.toString()));
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                for (EMMessage msg : messages) {
                    if (msg.getChatType() == ChatType.GroupChat && EaseAtMessageHelper.get().isAtMeMsg(msg)) {
                        EaseAtMessageHelper.get().removeAtMeGroup(msg.getTo());
                    }
                    EMMessage msgNotification = EMMessage.createReceiveMessage(Type.TXT);
                    EMTextMessageBody txtBody = new EMTextMessageBody(String.format(appContext.getString(R.string.msg_recall_by_user), msg.getFrom()));
                    msgNotification.addBody(txtBody);
                    msgNotification.setFrom(msg.getFrom());
                    msgNotification.setTo(msg.getTo());
                    msgNotification.setUnread(false);
                    msgNotification.setMsgTime(msg.getMsgTime());
                    msgNotification.setLocalTime(msg.getMsgTime());
                    msgNotification.setChatType(msg.getChatType());
                    msgNotification.setAttribute(Constant.MESSAGE_TYPE_RECALL, true);
                    msgNotification.setStatus(Status.SUCCESS);
                    EMClient.getInstance().chatManager().saveMessage(msgNotification);
                }
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                EMLog.d(TAG, "change:");
                EMLog.d(TAG, "change:" + change);
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);

    }


    /**
     * 获取EaseNotifier的实例
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

}
