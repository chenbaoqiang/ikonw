package com.feinno.androidbase.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.feinno.androidbase.utils.log.LogFeinno;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 版权所有 新媒传信科技有限公司。保留所有权利。<br>
 * 作者：wangxiaohong on 2016/1/5 11:09
 * 项目名：和飞信 - Android客户端<br>
 * 描述：应用相关的辅助类
 *
 * @version 1.0
 * @since JDK1.7.0_51
 */
public class AppCommonUtils {

    private static final String TAG = "AppCommonUtils";

    private static MediaPlayer avMediaPlayer = null;
    private static Timer timer = null;

    private static AudioManager audioManager = null;

    /**
     * 显示Intent中的所有数据
     *
     * @param intent
     * @return
     */
    public static String toString(Intent intent) {
        if (intent != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("Intent { ");
            String action = intent.getAction();
            if (action != null) {
                sb.append("act = ").append(action).append(",");
            }
            Set<String> categories = intent.getCategories();
            if (categories != null) {
                sb.append(" cat = [");
                Iterator<String> i = categories.iterator();
                boolean didone = false;
                while (i.hasNext()) {
                    if (didone) {
                        sb.append(", ");
                    }
                    didone = true;
                    sb.append(i.next());
                }
                sb.append("]");
            }
            Uri uri = intent.getData();
            if (uri != null) {
                sb.append(" dat = ").append(uri).append(",");
            }
            String type = intent.getType();
            if (type != null) {
                sb.append(" typ = ").append(type).append(",");
            }
            int flags = intent.getFlags();
            if (flags != 0) {
                sb.append(" flg = 0x").append(Integer.toHexString(flags)).append(",");
            }
            String packageStr = intent.getPackage();
            if (packageStr != null) {
                sb.append(" pkg = ").append(packageStr).append(",");
            }
            ComponentName component = intent.getComponent();
            if (component != null) {
                sb.append(" cmp = ").append(component.flattenToShortString()).append(",");
            }
            Rect rect = intent.getSourceBounds();
            if (rect != null) {
                sb.append(" bnds = ").append(rect.toShortString()).append(",");
            }
            Bundle extras = intent.getExtras();
            if (extras != null) {
                sb.append(" extras = [");
                int i = 0;
                for (String key : extras.keySet()) {
                    sb.append(key).append(" = ");
                    Object obj = extras.get(key);
                    if (obj != null && obj instanceof Bundle) {
                        sb.append(" [extras2 = [");
                        int j = 0;
                        Bundle extras2 = (Bundle) obj;
                        for (String key2 : extras2.keySet()) {
                            Object obj2 = extras2.get(key2);
                            sb.append(key2).append(" = ").append(obj2 instanceof byte[] ? new String((byte[]) obj2) : obj2);
                            if (++j <= extras2.size() - 1) {
                                sb.append(", ");
                            }
                        }
                        sb.append("] ]");
                    } else {
                        sb.append(obj instanceof byte[] ? new String((byte[]) obj) : obj);
                    }
                    if (++i <= extras.size() - 1) {
                        sb.append(", ");
                    }
                }
                sb.append("]");
            }
            sb.append(" }");
            return sb.toString();
        }
        return null;
    }

    /**
     * 设置View级别不启用系统硬件加速（Android2.3以下，使用Java反射调用mView.setLayerType(View.
     * LAYER_TYPE_SOFTWARE, null);）<br>
     * <p/>
     * <pre>
     * 从Android 3.0开始，Android的2D渲染管线可以更好的支持硬件加速。硬件加速使用GPU进行View上的绘制操作。
     * 硬件加速可以在一下四个级别开启或关闭：
     * Application
     * Activity
     * Window
     * View
     *
     * Application级别
     * 往您的应用程序AndroidManifest.xml文件为application标签添加如下的属性即可为整个应用程序开启硬件加速：
     * <application android:hardwareAccelerated="true" ...>
     *
     * Activity级别
     * 您还可以控制每个activity是否开启硬件加速，只需在activity元素中添加android:hardwareAccelerated属性即可办到。比如下面的例子，在application级别开启硬件加速，但在某个activity上关闭硬件加速。
     * <application android:hardwareAccelerated="true">
     * <activity ... />
     * <activity android:hardwareAccelerated="false" />
     * </application>
     *
     * Window级别
     * 如果您需要更小粒度的控制，可以使用如下代码开启某个window的硬件加速：
     * getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
     * 注：目前还不能在window级别关闭硬件加速。
     *
     * View级别
     * 您可以在运行时用以下的代码关闭单个view的硬件加速：
     * mView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
     * 注：您不能在view级别开启硬件加速
     *
     * 为什么需要这么多级别的控制？
     * 很明显，硬件加速能够带来性能提升，android为什么要弄出这么多级别的控制，而不是默认就是全部硬件加速呢？原因是并非所有的2D绘图操作支持硬件加速，如果您的程序中使用了自定义视图或者绘图调用，程序可能会工作不正常。
     * 如果您的程序中只是用了标准的视图和Drawable，放心大胆的开启硬件加速吧！
     * 具体是哪些绘图操作不支持硬件加速呢?以下是已知不支持硬件加速的绘图操作：
     * Canvas
     * clipPath()
     * clipRegion()
     * drawPicture()
     * drawPosText()
     * drawTextOnPath()
     * drawVertices()
     * Paint
     * setLinearText()
     * setMaskFilter()
     * setRasterizer()
     *
     * 另外还有一些绘图操作，开启和不开启硬件加速，效果不一样：
     * Canvas
     * clipRect()： XOR, Difference和ReverseDifference裁剪模式被忽略，3D变换将不会应用在裁剪的矩形上。
     * drawBitmapMesh()：colors数组被忽略
     * drawLines()：反锯齿不支持
     * setDrawFilter()：可以设置，但无效果
     * Paint
     * setDither()： 忽略
     * setFilterBitmap()：过滤永远开启
     * setShadowLayer()：只能用在文本上
     * ComposeShader
     * ComposeShader只能包含不同类型的shader (比如一个BitmapShader和一个LinearGradient，但不能是两个BitmapShader实例)
     * ComposeShader不能包含ComposeShader
     * 如果应用程序受到这些影响，您可以在受影响的部分调用setLayerType(View.LAYER_TYPE_SOFTWARE, null)，这样在其它地方仍然可以享受硬件加速带来的好处
     *
     * Android的绘制模型
     * 开启硬件加速后，Android框架将采用新的绘制模型。基于软件的绘制模型和基于硬件的绘制模型有和不同呢？
     * 基于软件的绘制模型
     * 在软件绘制模型下，视图按照如下两个步骤绘制：
     * 1. Invalidate the hierarchy（注：hierarchy怎么翻译？）
     * 2. Draw the hierarchy
     * 应用程序调用invalidate()更新UI的某一部分，失效(invalidation)消息将会在整个视图层中传递，计算每个需要重绘的区域（即脏区域）。
     * 然后Android系统将会重绘所有和脏区域有交集的view。很明显，这种绘图模式存在缺点：
     * 1. 每个绘制操作中会执行不必要的代码。比如如果应用程序调用invalidate()重绘button，而button又位于另一个view之上，即使该view没有变化，也会进行重绘。
     * 2. 可能会掩盖一些应用程序的bug。因为android系统会重绘与脏区域有交集的view，所以view的内容可能会在没有调用invalidate()的情况下重绘。这可能会导致一个view依赖于其它view的失效才得到正确的行为。
     *
     * 基于硬件的绘制模型
     * Android系统仍然使用invalidate()和draw()来绘制view，但在处理绘制上有所不同。Android系统记录绘制命令到显示列表，而不是立即执行绘制命令。另一个优化就是Android系统只需记录和更新标记为脏（通过invalidate()）的view。新的绘制模型包含三个步骤：
     * 1. Invalidate the hierarchy
     * 2. 记录和更新显示列表
     * 3. 绘制显示列表
     * </pre>
     *
     * @param view
     */
    public static void disableHardwareAccelerated(View view) {
        if (Build.VERSION.SDK_INT >= AndroidVersionCodes.HONEYCOMB && view != null) {
            Method methodSetLayerType = null;
            try {
                Class<?> classView = view.getClass();
                methodSetLayerType = classView.getMethod("setLayerType", new Class[]{int.class, Paint.class});
                if (methodSetLayerType != null) {
                    Integer layerTypeSoftwareInteger = null;
                    layerTypeSoftwareInteger = (Integer) classView.getField("LAYER_TYPE_SOFTWARE").get(null); // 得到静态变量，因为静态成员不需要对象就可以调用，所以在get方法传入null的意思就是取静态变量
                    if (layerTypeSoftwareInteger != null) {
                        int layerTypeSoftware = layerTypeSoftwareInteger;
                        methodSetLayerType.invoke(view, layerTypeSoftware, null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示状态栏通知
     *
     * @param context
     * @param notifyId
     * @param iconResId
     * @param tickerText
     * @param contentTitle
     * @param contentText
     * @param contentIntent
     * @param isScrollingNotify
     */
    public static void showNotification(Context context, int notifyId, int iconResId, String tickerText, String contentTitle, String contentText, Intent contentIntent, boolean isScrollingNotify) {
//        Notification notification; // 当用Notification(icon, tickerText,
//        // when)构造函数时，通知栏会有几秒钟的提示
//        if (isScrollingNotify) {
//            notification = new Notification(iconResId, contentText, System.currentTimeMillis()); // 当用Notification(icon,
//            // tickerText,
//            // when)构造函数时，通知栏会有几秒钟的提示
//        } else {
//            notification = new Notification();
//            notification.icon = iconResId;
//        }
//        notification.flags = Notification.FLAG_ONGOING_EVENT; // FLAG_ONGOING_EVENT：当前正在进行的事件
//        notification.setLatestEventInfo(context, contentTitle, contentText, PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)); // 下拉通知栏，显示状态栏时的信息
//        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(notifyId, notification);
    }

    /**
     * 显示状态栏通知
     *
     * @param context
     * @param notifyId
     * @param iconResId
     * @param tickerText
     * @param contentBitmap
     * @param contentTitle
     * @param contentText
     * @param contentIntent
     * @param isScrollingNotify
     */
    public static void showNotification(Context context, int notifyId, int iconResId, String tickerText, Bitmap contentBitmap, String contentTitle, String contentText, Intent contentIntent,
                                        boolean isScrollingNotify) {
//        Notification notification; // 当用Notification(icon, tickerText,
//        // when)构造函数时，通知栏会有几秒钟的提示
//        if (isScrollingNotify) {
//            notification = new Notification(iconResId, contentText, System.currentTimeMillis()); // 当用Notification(icon,
//            // tickerText,
//            // when)构造函数时，通知栏会有几秒钟的提示
//        } else {
//            notification = new Notification();
//            notification.icon = iconResId;
//        }
//        notification.flags = Notification.FLAG_ONGOING_EVENT; // FLAG_ONGOING_EVENT：当前正在进行的事件
//        notification.setLatestEventInfo(context, contentTitle, contentText, PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)); // 下拉通知栏，显示状态栏时的信息
//
//        // //在通知栏中显示Bitmap类型的图片
//        if (contentBitmap != null) {
//            notification.contentView.setImageViewBitmap(android.R.id.icon, contentBitmap);
//        }
//        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(notifyId, notification);
    }

    /**
     * 取消或者删除状态栏通知
     *
     * @param context
     * @param notifyId
     */
    public static void cancelNotification(Context context, int notifyId) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(notifyId);
    }

    /**
     * 取消或者删除所有状态栏通知
     *
     * @param context
     */
    public static void cancelAllNotification(Context context) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
    }

    /**
     * 跳转系统网络设置界面
     *
     * @param context
     */
    public static boolean startActivitySettingWireless(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < AndroidVersionCodes.ICE_CREAM_SANDWICH) {
            intent.setAction(Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent.setAction(Settings.ACTION_SETTINGS);
        }
        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 拨打电话
     */
    public static final boolean startActivityTel(Context context, String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.equals("0")) {
            return false;
        }

        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
                return true;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 下载
     */
    public static boolean startActivityDownload(Context context, String url) {
        return startActivitySystemBrowser(context, url);
    }

    /**
     * 使用系统浏览器打开url
     *
     * @param context
     * @param url
     */
    public static boolean startActivitySystemBrowser(Context context, String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (!(context instanceof Activity)) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
                return true;
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 跳到系统自带的编辑短信界面
     *
     * @param context
     * @param mobileNumber
     */
    public static boolean startActivitySmsTo(Context context, String mobileNumber) {
        if (mobileNumber != null) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mobileNumber)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
                return true;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 跳到系统自带的编辑email的界面
     *
     * @param context
     * @param mailAddress
     */
    public static boolean startActivityMailTo(Context context, String mailAddress) {
        if (mailAddress != null) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mailAddress));
            try {
                context.startActivity(intent);
                return true;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 应用是否已经安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            // mContext.getPackageInfo(String packageName, int flags)第二个参数flags为0：因为不需要该程序的其他信息，只需返回程序的基本信息。
            return context.getPackageManager().getPackageInfo(packageName, 0) != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 启动应用
     *
     * @param context
     * @param packageName
     * @return 如果packageName对应的应用没有找到，返回false，否则true
     */
    public static boolean startActivityLocalApp(Context context, String packageName, String credential) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (!TextUtils.isEmpty(credential)) {
                intent.putExtra("SSO_CREDENTIAL", credential);
            }
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 复制文本到系统剪切板
     */
    public static void copyToClipboard(Context context, String text) {
        ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setText(text);
    }

    /**
     * 隐藏输入法（根据activity当前焦点所在控件的WindowToken）
     */
    public static void hideSoftInput(Activity activity, View editText) {
        View view;
        if (editText == null) {
            view = activity.getCurrentFocus();
        } else {
            view = editText;
        }

        if (view != null) {
            InputMethodManager inputMethod = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethod.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘（根据焦点所在的控件）
     */
    public static void showSoftInput(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 通过string的字符串名获取R.string中对应属性的值
     *
     * @param name 字符串名
     * @return
     */
    public static int getStringResId(Context context, String name) {
        return context.getResources().getIdentifier(name, android.R.string.class.getSimpleName(), context.getPackageName());
    }

    /**
     * 通过drawable的字符串名获取R.drawable中对应属性的值
     *
     * @param name 图片名
     * @return
     */
    public static int getDrawableResId(Context context, String name) {
        return context.getResources().getIdentifier(name, android.R.drawable.class.getSimpleName(), context.getPackageName());
    }

    /**
     * 在手机主桌面创建应用程序快捷方式（不重复创建），详细内容请看Android系统Launcher或者Launcher2程序源代码
     *
     * @param context Context
     */
    public static void addShortcut(Context context, String name, int iconResId, Class<?> classActivity) {
        Intent shortcutIntent = new Intent(context, classActivity);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER); // Intent（注意是否要写老的Intent），如果不添加：Intent.ACTION_MAIN和Intent.CATEGORY_LAUNCHER，桌面快捷方式无法删除
        // Intent unInstallIntent = new
        // Intent("com.android.launcher.action.UNINSTALL_SHORTCUT"); //
        // 删除快捷方式，当应用程序图标或者包名发生变化时使用
        // unInstallIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title); //
        // 包名（注意是否要写老的包名）
        // unInstallIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
        // shortcutIntent);
        // unInstallIntent.putExtra("duplicate", true); //
        // 删除所有的快捷方式，注意：不是所有的Launcher都支持该属性
        // context.sendBroadcast(unInstallIntent);
        boolean shortcutExists = shortcutExists(context, name, shortcutIntent);
        if (LogFeinno.DEBUG) {
            LogFeinno.d(TAG, "addShortcut.shortcutExists = " + shortcutExists);
        }
        if (!shortcutExists) { // 有的Launcher对“duplicate”不起作用
            Intent installIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT"); // 创建快捷方式
            installIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
            installIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, iconResId));
            installIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            installIntent.putExtra("duplicate", false); // 不允许重复添加，注意：不是所有的Launcher都支持该属性
            context.sendBroadcast(installIntent);
        }
    }

    /**
     * 快捷方式是否已经存在<b><font color="red">（需要适配）</font></b><br />
     * 创建快捷方式时，"duplicate" = false 不起作用时，使用该方法做弥补
     * <uses-permission android:name="com.android.launcher.permission.READ_SETTINGS" />
     *
     * @param context
     * @param title
     * @param intent
     * @return
     */
    protected static boolean shortcutExists(Context context, String title, Intent intent) {
        boolean shortcutExists = false;
        Intent queryIntent = new Intent();
        queryIntent.setAction(Intent.ACTION_MAIN);
        queryIntent.addCategory(Intent.CATEGORY_HOME);
        List<IntentFilter> outFilters = null;
        List<ComponentName> outActivities = null;
        Cursor cursor = null;
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(queryIntent, PackageManager.MATCH_DEFAULT_ONLY);
        try {
            if (resolveInfos != null && resolveInfos.size() > 0) {
                outFilters = new ArrayList<IntentFilter>(); // Intent list cannot be null. so pass empty list
                outActivities = new ArrayList<ComponentName>();
                for (int i = resolveInfos.size() - 1; i >= 0; i--) {
                    String packageName = resolveInfos.get(i).activityInfo.packageName;
                    packageManager.getPreferredActivities(outFilters, outActivities, packageName); // 查询已经设置该应用作为默认打开方式的Activities
                    if (resolveInfos.size() == 1 // 系统只有一个主桌面
                            || isAppProcessesRuning(context, packageName) // 当前正在运行的主桌面
                            || outActivities.size() > 0 // 是系统当前默认的主桌面程序
                            ) {
                        if ("com.sec.android.app.twlauncher".equals(packageName)) {
                            // 三星手机的TouchWiz30Launcher应用程序，包名：com.sec.android.app.twlauncher
                            // TODO 暂时无法解决：在程序创建快捷方式前，手工拖动应用程序图标到桌面创建快捷方式的情况下无法查询
                            shortcutExists = getShortcutCount(context, title, "content://com.sec.android.app.twlauncher.settings/favorites?notify=true", intent.toUri(0)) > 0;
                        } else if ("com.htc.launcher".equals(packageName)) {
                            // HTC手机的Rosie应用程序，包名：com.htc.launcher
                            // 注意：需要在AndroidManifest.xml中添加权限 <uses-permission android:name="com.htc.launcher.permission.READ_SETTINGS" />
                            shortcutExists = getShortcutCount(context, title, "content://com.htc.launcher.settings/favorites?notify=true", intent.toUri(0)) > 0;
                            if (!shortcutExists) { // HTC手机特殊处理，快捷方式图标和应用程序列表图标的Intent是一样的
                                Intent intentMark = new Intent(intent);
                                intentMark.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                shortcutExists = getShortcutCount(context, title, "content://com.htc.launcher.settings/favorites?notify=true", intentMark.toUri(0)) > 1;
                            }
                        } else if ("com.miui.home".equals(packageName)
                                || (("Xiaomi".equalsIgnoreCase(Build.MANUFACTURER) || "MIUI".equalsIgnoreCase(Build.ID)) && "com.android.launcher".equals(packageName))) {
                            // 小米手机的MiuiHome应用程序，包名：com.miui.home
                            // 有的批次手机主界面的包是com.android.launcher而非com.miui.home
                            // 注意：小米手机的Launcher没有分应用程序列表界面和widget界面，只有一个界面
                            shortcutExists = true;
                        } else if ("com.motorola.blur.home".equals(packageName)) {
                            // 摩托罗拉手机的BlurHome应用程序，包名：com.motorola.blur.home
                            shortcutExists = getShortcutCount(context, title, "content://com.android.launcher.settings/favorites?notify=true", intent.toUri(0)) > 0;
                            if (!shortcutExists) { // 摩托罗拉手机特殊处理，在程序创建快捷方式前，手工拖动应用程序图标到桌面创建快捷方式的情况
                                Intent intentMark = new Intent(intent);
                                intentMark.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                shortcutExists = getShortcutCount(context, title, "content://com.android.launcher.settings/favorites?notify=true", intentMark.toUri(0)) > 0;
                            }
                        }
                        outFilters.clear();
                        outActivities.clear();
                        if (shortcutExists) {
                            break;
                        }
                    }
                }
            }
        } finally {
            if (resolveInfos != null) {
                resolveInfos.clear();
                resolveInfos = null;
            }
            if (outFilters != null) {
                outFilters.clear();
                outFilters = null;
            }
            if (outActivities != null) {
                outActivities.clear();
                outActivities = null;
            }
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return shortcutExists;
    }

    /**
     * 查询桌面快捷方式的个数
     *
     * @param context
     * @param selectionTitle
     * @param queryUriString
     * @param selectionIntentUriString
     * @return
     */
    private static int getShortcutCount(Context context, String selectionTitle, String queryUriString, String selectionIntentUriString) {
        int shortcutCount = 0;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Uri.parse(queryUriString), new String[]{"title", "intent"}, "title=? and intent=?",
                    new String[]{selectionTitle, selectionIntentUriString}, null);
            if (null != cursor && 0 < cursor.getCount()) {
                shortcutCount = cursor.getCount();
            }

//			// 测试：查询所有桌面快捷方式
            if (null != cursor) {
                cursor.close();
                cursor = null;
            }
            cursor = context.getContentResolver().query(Uri.parse(queryUriString), null, null, null, null);
            if (null != cursor && cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    if (LogFeinno.DEBUG) {
                        LogFeinno.d(TAG, "getShortcutCount.title = " + cursor.getString(cursor.getColumnIndex("title")) + ", intent = " + cursor.getString(cursor.getColumnIndex("intent")));
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        if (LogFeinno.DEBUG) {
            LogFeinno.d(TAG, "getShortcutCount.shortcutCount = " + shortcutCount + ", title = " + selectionTitle + ", queryUriString = " + queryUriString + ", selectionIntentUriString = "
                    + selectionIntentUriString);
        }
        return shortcutCount;
    }

    /**
     * 判断Service在系统中是否正在运行
     *
     * @param context
     * @param serviceClass
     * @return
     */
    public static boolean isServiceRuning(Context context, Class<?> serviceClass) {
        if (serviceClass != null) {
            return isServiceRuning(context, serviceClass.getClass().getName());
        }
        return false;
    }

    /**
     * 判断Service在系统中是否正在运行
     *
     * @param context
     * @param serviceClassName Service类名称，包括包名前缀，如：cn.com.fetion.android.services.
     *                         FetionSMSService
     * @return
     */
    public static boolean isServiceRuning(Context context, String serviceClassName) {
        if (serviceClassName != null && serviceClassName.length() > 0) {
            ActivityManager ativityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> runningServiceInfos = ativityManager.getRunningServices(-1);
            for (int i = 0; i < runningServiceInfos.size(); i++) {
                if (serviceClassName.equals(runningServiceInfos.get(i).service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断包名对应的应用程序是否正在运行
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppProcessesRuning(Context context, String packageName) {
        if (packageName != null && packageName.length() > 0) {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
            if (runningAppProcessInfo != null) {
                for (ActivityManager.RunningAppProcessInfo appProcInfo : runningAppProcessInfo) {
                    if (appProcInfo != null) {
                        if (packageName.equals(appProcInfo.processName)) {
                            return true;
                        } else if (null != appProcInfo.pkgList) {
                            for (int i = appProcInfo.pkgList.length - 1; i >= 0; i--) {
                                if (packageName.equals(appProcInfo.pkgList[i])) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 应用是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        return isAppOnForeground(context, context.getPackageName());
    }

    /**
     * 根据包名判断应用是否在前台运行
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppOnForeground(Context context, String packageName) {
        if (packageName != null) {
            // Returns a list of application processes that are running on the device
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses != null) {
                for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                    // The name of the process that this object is associated with.
                    if (appProcess.processName.equals(packageName) && ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == appProcess.importance) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取Context所在进程的名称
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 根据包名获取图标
     */
    public static Drawable getAppIcon(Context context, String packageName) {
        PackageManager packManager = context.getPackageManager();
        try {
            return packManager.getApplicationIcon(packManager.getApplicationInfo(packageName, 0));
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 根据包名获取应用名称
     */
    public static CharSequence getAppTitle(Context context, String packageName) {
        PackageManager packManager = context.getPackageManager();
        try {
            return packManager.getApplicationLabel(packManager.getApplicationInfo(packageName, 0));
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 播放提示声音
     */
    public static void playShortAudioFile(Context context, int rid) {
        AssetFileDescriptor file = context.getResources().openRawResourceFd(rid);
        AudioManager audioService = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            // 如果此时系统声音是静音或者振动，则不起作用
            return;
        } else {
            MediaPlayer player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_RING);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.seekTo(0);
                }
            });

            try {
                player.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                player.prepare();
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 循环播放提示声音20秒
     *
     * @param context
     * @param rid
     */
    public static void playAudioFileRepeat(Context context, int rid) {
        try {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    stopAVMedia();
                }
            }, 20 * 1000);

            AssetFileDescriptor file = context.getResources().openRawResourceFd(rid);
            AudioManager audioService = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                // 如果此时系统声音是静音或者振动，则不起作用
                return;
            } else {
                avMediaPlayer = new MediaPlayer();
                avMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                avMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.seekTo(0);
                    }
                });

                try {
                    avMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                    file.close();
                    avMediaPlayer.prepare();
                    avMediaPlayer.setLooping(true);
                    avMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            /**
                             * 音频在初始化过程中，音频被停止，
                             * 而音频在初始化中无法被中止只是cancel了timer,
                             * 所以如果timer已被cancel即停掉音频
                             */
                            try {
                                if (timer != null) {
                                    mp.start();

                                } else {
                                    mp.reset();
                                    mp.release();
                                    mp = null;
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            if (avMediaPlayer != null) {
                avMediaPlayer.reset();
                avMediaPlayer.release();
                avMediaPlayer = null;
            }
        }
    }

    /**
     * 获取播放焦点,使第三方播放器处于暂停状态
     */
    public static void requestAudioFocus(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    /**
     * 释放焦点
     */
    public static void abandonAudioFocus() {
        if (audioManager != null) {
            audioManager.abandonAudioFocus(afChangeListener);
        }
    }

    static AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            }
        }
    };

    /**
     * 终止声音播放
     */
    public static void stopAVMedia() {
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (avMediaPlayer != null) {
                if (avMediaPlayer.isPlaying()) {
                    avMediaPlayer.stop();
                    avMediaPlayer.reset();
                    avMediaPlayer.release();
                    avMediaPlayer = null;
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 设置EditText的光标到最后
     *
     * @param editText
     */
    public static void setSelection(EditText editText) {
        if (editText.getText() != null) {
            Editable ea = editText.getText();
            Selection.setSelection(ea, ea.length());
        }
    }

    public static int parseInt(String str) {
        int num = 0;
        try {
            num = Integer.parseInt(str);
        } catch (Exception e) {
        }
        return num;
    }

    public static long parseLong(String str) {
        long num = 0;
        try {
            num = Long.parseLong(str);
        } catch (Exception e) {
        }
        return num;
    }
}
