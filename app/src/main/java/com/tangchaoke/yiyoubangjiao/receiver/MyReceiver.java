package com.tangchaoke.yiyoubangjiao.receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.activity.Activity_AnsweredInfo;
import com.tangchaoke.yiyoubangjiao.activity.Activity_MessageInfo;
import com.tangchaoke.yiyoubangjiao.activity.Activity_OnlineAnswerInfo;
import com.tangchaoke.yiyoubangjiao.activity.Activity_OrderInfo;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "==JIGUANG-Example";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            String json = bundle.get("cn.jpush.android.EXTRA").toString();
            JSONObject jsonObject = new JSONObject(json);
            LocalBroadcastManager localBroadcastManager;
            Intent mIntent;
            SharedPreferences.Editor mEditor = BaseApplication.getApplication().getEditor();
            String mType = jsonObject.getString("type");
            String mStatus = jsonObject.getString("status");
            String mOid = jsonObject.getString("oid");
            String mObject = jsonObject.getString("object");
            Log.e("===json:::", json
                    + "---mType:::" + mType
                    + "---mStatus:::" + mStatus
                    + "---mOid:::" + mOid
                    + "---mObject:::" + mObject);
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            processCustomMessage(context, notifactionId);
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                processCustomMessage(context, notifactionId);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                /**
                 * type = 1 问题   2 认证   3 提现   4 等级   5 系统消息   6 推送消息   7 订单   8 俱乐部
                 * oid = 问题oid / 消息oid
                 * object = 1 成人学历认证   2 在校学生认证（认证）
                 *          1 我自己的   2 别人的（问题）
                 *          1 俱乐部学生   2 俱乐部老师（俱乐部）
                 * status = 1 成功   2 失败（认证）
                 *          1 一星   2 二星   3 三星   4 四星   5 五星（等级）
                 *          1 新发布问题   2 立即抢答   3 结束答题   4 重回大厅   5 已介入   6 已关闭   7 介入处理结果   8 关闭处理结果   9 撤回问题   10自动采纳（问题）
                 *          1 绑定   2 解绑（俱乐部）
                 */
                if (!HGTool.isEmpty(mType)) {
                    if (mType.equals("1")) {
                        if (!HGTool.isEmpty(mStatus)) {
                            if (mStatus.equals("1")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 1 新发布问题
                                 *  oid = 问题oid
                                 */
                                //抢答者如果在在线答题界面，刷新界面
                            } else if (mStatus.equals("2")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 2 立即抢答
                                 *  oid = 问题oid
                                 */
                                //发题者如果在问题详情界面 或 未解答界面，刷新界面
                                if (BaseApplication.getApplication().getActivity().equals("Activity_AnsweredInfo")) {
                                    localBroadcastManager = LocalBroadcastManager.getInstance(context);
                                    mIntent = new Intent("com.tangchaoke.yiyoubangjiao.answered.me");
                                    mIntent.putExtra("tuisong", "answered_me");
                                    mIntent.putExtra("oid", mOid);
                                    mIntent.putExtra("status", mStatus);
                                    localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                                }
                            } else if (mStatus.equals("3")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 3 结束答题
                                 *  oid = 消息oid
                                 */
                                //答题者 如果在聊天界面，需遮挡输入框，无法进行聊天
                                if (BaseApplication.getApplication().getActivity().equals("Activity_Chat")) {
                                    localBroadcastManager = LocalBroadcastManager.getInstance(context);
                                    mIntent = new Intent("com.tangchaoke.yiyoubangjiao.home");
                                    mIntent.putExtra("tuisong", "popups");
                                    mIntent.putExtra("object", mOid);
                                    mIntent.putExtra("status", mStatus);
                                    localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                                }
                            } else if (mStatus.equals("4")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 4 重回大厅
                                 *  oid = 消息oid
                                 */
                                //抢答者 在聊天 或 未解答界面，刷新界面  发题者 在 未解答界面 或 该题详情界面，刷新界面
                                if (BaseApplication.getApplication().getActivity().equals("Activity_Chat")) {
                                    localBroadcastManager = LocalBroadcastManager.getInstance(context);
                                    mIntent = new Intent("com.tangchaoke.yiyoubangjiao.home");
                                    mIntent.putExtra("tuisong", "popups");
                                    mIntent.putExtra("object", mObject);
                                    mIntent.putExtra("status", mStatus);
                                    localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                                } else if (BaseApplication.getApplication().getActivity().equals("Activity_AnsweredInfo")) {
                                    localBroadcastManager = LocalBroadcastManager.getInstance(context);
                                    mIntent = new Intent("com.tangchaoke.yiyoubangjiao.answered.me");
                                    mIntent.putExtra("tuisong", "answered_me");
                                    mIntent.putExtra("oid", mObject);
                                    mIntent.putExtra("status", mStatus);
                                    localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                                }
                            } else if (mStatus.equals("5")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 5 已介入
                                 *  oid = 消息oid
                                 */
                                //抢答者 在聊天界面，刷新界面  发题者 在该题详情界面，刷新界面 /如无人抢答 暂无抢答者
                                if (BaseApplication.getApplication().getActivity().equals("Activity_Chat")) {
                                    localBroadcastManager = LocalBroadcastManager.getInstance(context);
                                    mIntent = new Intent("com.tangchaoke.yiyoubangjiao.home");
                                    mIntent.putExtra("tuisong", "popups");
                                    mIntent.putExtra("object", mObject);
                                    mIntent.putExtra("status", mStatus);
                                    localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                                } else if (BaseApplication.getApplication().getActivity().equals("Activity_MessageSystem")) {
                                    localBroadcastManager = LocalBroadcastManager.getInstance(context);
                                    mIntent = new Intent("com.tangchaoke.yiyoubangjiao.message");
                                    mIntent.putExtra("tuisong", "message");
                                    localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                                }
                            } else if (mStatus.equals("6")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 6 已关闭
                                 *  oid = 消息oid
                                 */
                                //抢答者 在聊天界面，刷新界面  发题者 在该题详情界面，刷新界面 /如无人抢答 暂无抢答者
                                if (BaseApplication.getApplication().getActivity().equals("Activity_Chat")) {
                                    localBroadcastManager = LocalBroadcastManager.getInstance(context);
                                    mIntent = new Intent("com.tangchaoke.yiyoubangjiao.home");
                                    mIntent.putExtra("tuisong", "popups");
                                    mIntent.putExtra("object", mObject);
                                    mIntent.putExtra("status", mStatus);
                                    localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                                } else if (BaseApplication.getApplication().getActivity().equals("Activity_MessageSystem")) {
                                    localBroadcastManager = LocalBroadcastManager.getInstance(context);
                                    mIntent = new Intent("com.tangchaoke.yiyoubangjiao.message");
                                    mIntent.putExtra("tuisong", "message");
                                    localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                                }
                            } else if (mStatus.equals("7")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 7 介入处理结果
                                 *  oid = 消息oid
                                 */
                                //抢答者 或 发题者 都需要跳转到消息详情 /如无人抢答 暂无抢答者
                                if (BaseApplication.getApplication().getActivity().equals("Activity_MessageSystem")) {
                                    localBroadcastManager = LocalBroadcastManager.getInstance(context);
                                    mIntent = new Intent("com.tangchaoke.yiyoubangjiao.message");
                                    mIntent.putExtra("tuisong", "message");
                                    localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                                }
                            } else if (mStatus.equals("8")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 8 关闭处理结果
                                 *  oid = 消息oid
                                 */
                                //抢答者 或 发题者 都需要跳转到消息详情 /如无人抢答 暂无抢答者
                                if (BaseApplication.getApplication().getActivity().equals("Activity_MessageSystem")) {
                                    localBroadcastManager = LocalBroadcastManager.getInstance(context);
                                    mIntent = new Intent("com.tangchaoke.yiyoubangjiao.message");
                                    mIntent.putExtra("tuisong", "message");
                                    localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                                }
                            } else if (mStatus.equals("9")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 9 撤回问题
                                 *  oid = 消息oid
                                 */
                                //发题者需要跳转到消息详情
                                if (BaseApplication.getApplication().getActivity().equals("Activity_MessageSystem")) {
                                    localBroadcastManager = LocalBroadcastManager.getInstance(context);
                                    mIntent = new Intent("com.tangchaoke.yiyoubangjiao.message");
                                    mIntent.putExtra("tuisong", "message");
                                    localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                                }
                            } else if (mStatus.equals("10")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 10 自动采纳
                                 *  oid = 问题oid
                                 */
                                //答题者 或 发题者 在问题详情 刷新界面
                                if (BaseApplication.getApplication().getActivity().equals("Activity_AnsweredInfo")) {
                                    localBroadcastManager = LocalBroadcastManager.getInstance(context);
                                    mIntent = new Intent("com.tangchaoke.yiyoubangjiao.answered.me");
                                    mIntent.putExtra("tuisong", "answered_me");
                                    mIntent.putExtra("oid", mOid);
                                    mIntent.putExtra("status", mStatus);
                                    localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                                }
                            }
                        }
                    } else if (mType.equals("2")) {
                        /**
                         *  type = 2 认证
                         *  oid = 消息oid
                         *  object = 1 / 2
                         *  status = 1 / 2
                         */
                        //根据返回 更新本地用户信息，在认证界面，刷新界面
                        if (BaseApplication.getApplication().getActivity().equals("Activity_Certified")) {
                            /**
                             * 认证消息 如果在认证消息界面  就要通知  该界面数据进行刷新
                             */
                            localBroadcastManager = LocalBroadcastManager.getInstance(context);
                            mIntent = new Intent("com.tangchaoke.yiyoubangjiao.certified");
                            mIntent.putExtra("tuisong", "certified");
                            localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                        }
                        /**
                         * 保存认证状态到本地
                         */
                        if (mStatus.equals("2")) {
                            if (!HGTool.isEmpty(mObject)) {
                                if (mObject.equals("1")) {
                                    /**
                                     * 身份认证
                                     */
                                    mEditor.putString("userRealNameStatus", "2");
                                } else if (mObject.equals("2")) {
                                    /**
                                     * 成人学历认证
                                     */
                                    mEditor.putString("userDiplomaStatus", "2");
                                } else if (mObject.equals("3")) {
                                    /**
                                     * 教师资格证认证
                                     */
                                    mEditor.putString("studentIdCardStatus", "2");
                                } else if (mObject.equals("4")) {
                                    /**
                                     * 在校学生证认证
                                     */
                                    mEditor.putString("userTeacherCertificationStatus", "2");
                                } else if (mObject.equals("5")) {
                                    /**
                                     * 易优教师认证
                                     */
                                    mEditor.putString("teacherInfoStatus", "2");
                                } else if (mObject.equals("6")) {
                                    /**
                                     * 易优教师认证
                                     */
                                    mEditor.putString("tutorInfoStatus", "2");
                                }
                                mEditor.commit();
                            }
                        }
                    } else if (mType.equals("3")) {
                        /**
                         *  type = 3 提现
                         *  oid = 消息oid
                         *  status = 1 / 2
                         */
                        //用户在收支明细界面，刷新界面
                        if (BaseApplication.getApplication().getActivity().equals("Fragment_Balance")) {
                            /**
                             * 提现消息 如果在余额界面  就要通知  该界面数据进行刷新
                             */
                            localBroadcastManager = LocalBroadcastManager.getInstance(context);
                            mIntent = new Intent("com.tangchaoke.yiyoubangjiao.balance");
                            mIntent.putExtra("tuisong", "balance");
                            localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                        }
                    } else if (mType.equals("4")) {
                        /**
                         *  type = 4 等级
                         *  status = 1 / 2 / 3 / 4 / 5
                         */
                        //更新本地用户信息，刷新我的界面星级显示
                        if (!HGTool.isEmpty(mStatus)) {
                            mEditor.putString("grade", mStatus);
                            mEditor.commit();
                            localBroadcastManager = LocalBroadcastManager.getInstance(context);
                            mIntent = new Intent("com.tangchaoke.yiyoubangjiao.home");
                            mIntent.putExtra("tuisong", "mine");
                            localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                        }
                    } else if (mType.equals("5")) {
                        /**
                         *  type = 5 系统消息
                         *  oid = 消息oid
                         */
                        //用户在消息列表界面，属性界面
                        if (BaseApplication.getApplication().getActivity().equals("Activity_MessageSystem")) {
                            localBroadcastManager = LocalBroadcastManager.getInstance(context);
                            mIntent = new Intent("com.tangchaoke.yiyoubangjiao.message");
                            mIntent.putExtra("tuisong", "message");
                            localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                        }
                    } else if (mType.equals("6")) {
                        /**
                         *  type = 6 推送消息
                         *  oid = 消息oid
                         */
                        //用户在消息列表界面，属性界面
                        if (BaseApplication.getApplication().getActivity().equals("Activity_MessageSystem")) {
                            localBroadcastManager = LocalBroadcastManager.getInstance(context);
                            mIntent = new Intent("com.tangchaoke.yiyoubangjiao.message");
                            mIntent.putExtra("tuisong", "message");
                            localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                        }
                    } else if (mType.equals("7")) {
                        /**
                         *  type = 7 订单
                         *  oid = 订单oid
                         */
                        //用户在该订单详情界面，刷新界面
                        if (BaseApplication.getApplication().getActivity().equals("Activity_OrderInfo")) {
                            localBroadcastManager = LocalBroadcastManager.getInstance(context);
                            mIntent = new Intent("com.tangchaoke.yiyoubangjiao.order");
                            mIntent.putExtra("tuisong", "order");
                            mIntent.putExtra("oid", mOid);
                            localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                        }
                    } else if (mType.equals("8")) {
                        /**
                         *  type = 8 俱乐部
                         *  oid = 订单oid
                         *  object = 1 / 2
                         *  status = 1 / 2
                         */
                        //更新用户本地信息，在我的学校界面，进行刷新
                        /**
                         * 保存认证状态到本地
                         */
                        if (mObject.equals("1")) {
                            //俱乐部学生
                            if (mStatus.equals("1")) {
                                //绑定
                                mEditor.putString("isClub", "1");
                            } else if (mStatus.equals("1")) {
                                //解绑
                                mEditor.putString("isClub", "0");
                            }
                        } else if (mObject.equals("2")) {
                            //俱乐部老师
                            if (mStatus.equals("1")) {
                                //绑定
                                mEditor.putString("isClub", "3");
                            } else if (mStatus.equals("1")) {
                                //解绑
                                mEditor.putString("isClub", "0");
                            }
                        }
                        mEditor.commit();
                        if (BaseApplication.getApplication().getActivity().equals("Activity_MyClub")) {
                            localBroadcastManager = LocalBroadcastManager.getInstance(context);
                            mIntent = new Intent("com.tangchaoke.yiyoubangjiao.myclub");
                            mIntent.putExtra("tuisong", "myclub");
                            localBroadcastManager.sendBroadcast(mIntent);//发送本地广播。
                        }
                    }
                }
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                /**
                 * type = 1 问题   2 认证   3 提现   4 等级   5 系统消息   6 推送消息   7 订单   8 俱乐部
                 * oid = 问题oid / 消息oid
                 * object = 1 成人学历认证   2 在校学生认证（认证）
                 *          1 我自己的   2 别人的（问题）
                 *          1 俱乐部学生   2 俱乐部老师（俱乐部）
                 * status = 1 成功   2 失败（认证）
                 *          1 一星   2 二星   3 三星   4 四星   5 五星（等级）
                 *          1 新发布问题   2 立即抢答   3 结束答题   4 重回大厅   5 已介入   6 已关闭   7 介入处理结果   8 关闭处理结果   9 撤回问题   10自动采纳（问题）
                 *          1 绑定   2 解绑（俱乐部）
                 */
                if (!HGTool.isEmpty(mType)) {
                    if (mType.equals("1")) {
                        if (!HGTool.isEmpty(mStatus)) {
                            if (mStatus.equals("1")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 1 新发布问题
                                 *  oid = 问题oid
                                 */
                                //抢答者需要跳转到问题详情 进行抢答
                                if (BaseApplication.getApplication().isClub().equals("0")) {
                                    mIntent = new Intent(context, Activity_OnlineAnswerInfo.class);
                                    mIntent.putExtra("oid", mOid);
                                    mIntent.putExtra("type3", "1");//界面：1首页互动答题 2我的学校互动答题
                                    mIntent.putExtra("type2", "1");//问题类型:1文化课问题 2棋类问题
                                    context.startActivity(mIntent);
                                } else if (BaseApplication.getApplication().isClub().equals("2")) {
                                    mIntent = new Intent(context, Activity_OnlineAnswerInfo.class);
                                    mIntent.putExtra("oid", mOid);
                                    mIntent.putExtra("type3", "2");//界面：1首页互动答题 2我的学校互动答题
                                    mIntent.putExtra("type2", "2");//问题类型:1文化课问题 2棋类问题
                                    context.startActivity(mIntent);
                                } else if (BaseApplication.getApplication().isClub().equals("3")) {
                                    mIntent = new Intent(context, Activity_OnlineAnswerInfo.class);
                                    mIntent.putExtra("oid", mOid);
                                    mIntent.putExtra("type3", "2");//界面：1首页互动答题 2我的学校互动答题
                                    mIntent.putExtra("type2", "1");//问题类型:1文化课问题 2棋类问题
                                    context.startActivity(mIntent);
                                }
                            } else if (mStatus.equals("2")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 2 立即抢答
                                 *  oid = 问题oid
                                 */
                                //发题者需要跳转到问题详情
                                mIntent = new Intent(context, Activity_AnsweredInfo.class);
                                mIntent.putExtra("oid", mOid);
                                mIntent.putExtra("type", "me");
                                context.startActivity(mIntent);
                            } else if (mStatus.equals("3")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 3 结束答题
                                 *  oid = 消息oid
                                 */
                                //答题者 或 发题者 都需要跳转到消息详情
                                mIntent = new Intent(context, Activity_AnsweredInfo.class);
                                mIntent.putExtra("oid", mOid);
                                mIntent.putExtra("type", "help");
                                context.startActivity(mIntent);
                            } else if (mStatus.equals("4")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 4 重回大厅
                                 *  oid = 消息oid
                                 */
                                //抢答者 或 发题者 都需要跳转到消息详情
                                mIntent = new Intent(context, Activity_MessageInfo.class);
                                mIntent.putExtra("oid", mOid);
                                context.startActivity(mIntent);
                            } else if (mStatus.equals("5")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 5 已介入
                                 *  oid = 消息oid
                                 */
                                //抢答者 或 发题者 都需要跳转到消息详情 /如无人抢答 暂无抢答者
                                mIntent = new Intent(context, Activity_MessageInfo.class);
                                mIntent.putExtra("oid", mOid);
                                context.startActivity(mIntent);
                            } else if (mStatus.equals("6")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 6 已关闭
                                 *  oid = 消息oid
                                 */
                                //抢答者 或 发题者 都需要跳转到消息详情 /如无人抢答 暂无抢答者
                                mIntent = new Intent(context, Activity_MessageInfo.class);
                                mIntent.putExtra("oid", mOid);
                                context.startActivity(mIntent);
                            } else if (mStatus.equals("7")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 7 介入处理结果
                                 *  oid = 消息oid
                                 */
                                //抢答者 或 发题者 都需要跳转到消息详情 /如无人抢答 暂无抢答者
                                mIntent = new Intent(context, Activity_MessageInfo.class);
                                mIntent.putExtra("oid", mOid);
                                context.startActivity(mIntent);
                            } else if (mStatus.equals("8")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 8 关闭处理结果
                                 *  oid = 消息oid
                                 */
                                //抢答者 或 发题者 都需要跳转到消息详情 /如无人抢答 暂无抢答者
                                mIntent = new Intent(context, Activity_MessageInfo.class);
                                mIntent.putExtra("oid", mOid);
                                context.startActivity(mIntent);
                            } else if (mStatus.equals("9")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 9 撤回问题
                                 *  oid = 消息oid
                                 */
                                //发题者需要跳转到消息详情
                                mIntent = new Intent(context, Activity_MessageInfo.class);
                                mIntent.putExtra("oid", mOid);
                                context.startActivity(mIntent);
                            } else if (mStatus.equals("10")) {
                                /**
                                 *  type = 1 问题
                                 *  status = 10 自动采纳
                                 *  oid = 问题oid
                                 */
                                //答题者 或 发题者 需要跳转到问题详情
                                mIntent = new Intent(context, Activity_OnlineAnswerInfo.class);
                                mIntent.putExtra("oid", mOid);
//                                mIntent.putExtra("type", mType3);//界面：1 我的 2别人的
                                context.startActivity(mIntent);
                            }
                        }
                    } else if (mType.equals("2")) {
                        /**
                         *  type = 2 认证
                         *  oid = 消息oid
                         *  object = 1 / 2
                         *  status = 1 / 2
                         */
                        //用户需要跳转到消息详情
                        mIntent = new Intent(context, Activity_MessageInfo.class);
                        mIntent.putExtra("oid", mOid);
                        context.startActivity(mIntent);
                    } else if (mType.equals("3")) {
                        /**
                         *  type = 3 提现
                         *  oid = 消息oid
                         *  status = 1 / 2
                         */
                        //用户需要跳转到消息详情
                        mIntent = new Intent(context, Activity_MessageInfo.class);
                        mIntent.putExtra("oid", mOid);
                        context.startActivity(mIntent);
                    } else if (mType.equals("4")) {
                        /**
                         *  type = 4 等级
                         *  status = 1 / 2 / 3 / 4 / 5
                         */
                    } else if (mType.equals("5")) {
                        /**
                         *  type = 5 系统消息
                         *  oid = 消息oid
                         */
                        //用户需要跳转到消息详情
                        mIntent = new Intent(context, Activity_MessageInfo.class);
                        mIntent.putExtra("oid", mOid);
                        context.startActivity(mIntent);
                    } else if (mType.equals("6")) {
                        /**
                         *  type = 6 推送消息
                         *  oid = 消息oid
                         */
                        //用户需要跳转到消息详情
                        mIntent = new Intent(context, Activity_MessageInfo.class);
                        mIntent.putExtra("oid", mOid);
                        context.startActivity(mIntent);
                    } else if (mType.equals("7")) {
                        /**
                         *  type = 7 订单
                         *  oid = 订单oid
                         */
                        //用户需要跳转到订单详情
                        mIntent = new Intent(context, Activity_OrderInfo.class);
                        mIntent.putExtra("oid", mOid);
                        context.startActivity(mIntent);
                    } else if (mType.equals("8")) {
                        /**
                         *  type = 8 俱乐部
                         *  oid = 订单oid
                         *  object = 1 / 2
                         *  status = 1 / 2
                         */
                        //用户需要跳转到消息详情
                        mIntent = new Intent(context, Activity_MessageInfo.class);
                        mIntent.putExtra("oid", mOid);
                        context.startActivity(mIntent);
                    }
                }
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            } else {
                Log.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (HGTool.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.e(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Integer notifactionId) {
        Log.e("==builder_id：：：", BaseApplication.getApplication().isPush() + "---");
        /**
         * BaseApplication.getApplication().isPush()
         * 0 有声音
         * 1 没声音
         */
        if (BaseApplication.getApplication().isPush() == 0) {
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
            builder.statusBarDrawable = R.drawable.ic_launcher;
            builder.notificationFlags = Notification.FLAG_AUTO_CANCEL//
                    | Notification.FLAG_SHOW_LIGHTS; // 设置为自动消失和呼吸灯闪烁
            builder.notificationDefaults =
                    Notification.DEFAULT_SOUND | // 设置为铃声
                            Notification.DEFAULT_VIBRATE | // 设置为、震动
                            Notification.DEFAULT_LIGHTS; // 设置为呼吸灯闪烁
//            JPushInterface.setPushNotificationBuilder(BaseApplication.getApplication().isPush(), builder);
            JPushInterface.setPushNotificationBuilder(notifactionId, builder);
        } else if (BaseApplication.getApplication().isPush() == 1) {
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
            builder.statusBarDrawable = R.drawable.ic_launcher;
            builder.notificationFlags = Notification.FLAG_AUTO_CANCEL//
                    | Notification.FLAG_SHOW_LIGHTS; // 设置为自动消失和呼吸灯闪烁
            builder.notificationDefaults = //
                    Notification.DEFAULT_VIBRATE | // 设置为、震动
                            Notification.DEFAULT_LIGHTS; // 设置为呼吸灯闪烁
//            JPushInterface.setPushNotificationBuilder(BaseApplication.getApplication().isPush(), builder);
            JPushInterface.setPushNotificationBuilder(notifactionId, builder);
        }
    }
}
