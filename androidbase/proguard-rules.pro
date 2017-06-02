-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclassmembers
-dontoptimize
-dontpreverify
-dontshrink
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-ignorewarning

-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class * extends android.view.View{*;}
-keep class * extends android.app.Dialog{*;}

-keep class android.support.** {*;}

-keep public class * {
    public *;
}

-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**
-keep class **.R$* {*;}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-dontwarn org.json.**
-keep class org.json.** { *;}
#httpcore
-dontwarn org.apache.http.**
-keep class org.apache.http.**{ *;}
#httpmine
-dontwarn org.apache.http.entity.mime.**
-keep class org.apache.http.entity.mime.**{ *;}
#simplexml
-dontwarn org.simpleframework.xml.**
-keep class org.simpleframework.xml.**{ *;}
#gson
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable {*;}
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
#volley
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }
#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep class org.greenrobot.eventbus.** { *; }
-keep class de.greenrobot.eventbus.** { *; }
-dontwarn de.greenrobot.eventbus.**
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#ormlite
-dontwarn com.j256.**
-keep class com.j256.** { *;}
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.** { *;}
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.** { *;}
-keepclassmembers interface com.j256.** { *; }
-keep class com.j256.ormlite.android.** { *; }
-keep class com.j256.ormlite.field.** { *; }
-keep class com.j256.ormlite.stmt.** { *; }
-keep class com.j256.ormlite.table.** { *; }

#pinyin4j
-dontwarn net.soureceforge.pinyin4j.**
-dontwarn demo.**
-dontwarn com.hp.hpl.sparta.**
-keep class net.sourceforge.pinyin4j.** { *;}
-keep class demo.** { *;}
-keep class com.hp.hpl.sparta.** { *;}
-dontwarn com.aspire.strangecallssdk.**
-keep class com.aspire.strangecallssdk.** { *;}
-dontwarn cn.dreamtobe.kpswitch.**
-keep class cn.dreamtobe.kpswitch.** { *;}
-dontwarn com.nineoldandroids.*
-keep class com.nineoldandroids.** { *;}
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }
-dontwarn com.support.percent.**
-keep class com.support.percent.** { *; }
-keep class jp.wasabeef.** {*;}
-dontwarn jp.wasabeef.**
-keep class com.github.clans.fab.** {*;}
-dontwarn com.github.clans.fab.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keep class **$Properties
-dontwarn org.greenrobot.greendao.**
-keep class org.greenrobot.greendao.** {*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class com.squareup.picasso.** {*; }
-dontwarn com.squareup.picasso.**
-dontwarn org.apache.log4j.**
-keep class org.apache.log4j.** { *;}
-dontwarn com.jude.swipbackhelper.**
-keep class com.jude.swipbackhelper.** { *;}
-dontwarn cn.cmcc.online.**
-keep class cn.cmcc.online.**{ *;}
-dontwarn com.cmcc.sso.apisdk.**
-keep class com.cmcc.sso.apisdk.**{ *;}
-dontwarn org.aspectj.**
-keep class org.aspectj.**{ *;}
-dontwarn com.coremedia.iso.**
-keep class ccom.coremedia.iso.**{*;}
-dontwarn com.googlecode.mp4parser.**
-keep class com.googlecode.mp4parser.**{*;}
-dontwarn com.mp4parser.**
-keep class com.mp4parser.**{*;}
-dontwarn com.zhuge.analysis.**
-keep class com.zhuge.analysis.**{*;}
-keep class com.google.zxing.** {*;}
-dontwarn com.google.zxing.**
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }
-dontwarn rx.*
-dontwarn sun.misc.**
-dontwarn com.feinno.emojishop.**
-keep class com.feinno.emojishop.**{*;}
-dontwarn com.feinno.eomjishop.data.**
-keep class com.feinno.eomjishop.data.**{*;}

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

-keepclassmembers class * {
    native <methods>;
}
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-keep class com.facebook.imagepipeline.animated.factory.AnimatedFactoryImpl {
    public AnimatedFactoryImpl(com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory,com.facebook.imagepipeline.core.ExecutorSupplier);
}
-dontwarn com.tencent.**
-keep class com.tencent.** {*;}
-dontwarn sun.misc.Unsafe
-dontwarn a_vcard.**
-keep class a_vcard.** {*;}
-dontwarn ezvcard.**
-keep class ezvcard.** {*;}
-dontwarn com.feinno.serialization.protobuf.**
-keep class com.feinno.serialization.protobuf.** {*;}
-keep class com.feinno.util.** {*;}
-keep class com.feinno.androidprotocol.** {*;}
-keep class com.feinno.rongfly.core.service** {*;}
-keep class com.feinno.rongfly.core.modules.login.model.** {*;}
-keep class com.feinno.rongfly.core.service.** {*;}
-dontwarn com.example.proto_test_enhance.proto.**
-keep class com.example.proto_test_enhance.proto.** {*;}
-dontwarn com.android.contacts.**
-keep class com.android.contacts.** {*;}
-keep class com.feinno.v6sdk.** {*;}
-keep class com.ultrapower.**{*;}
-keep class com.feinno.rongfly.common.RFConfig.**{*;}
-keep class IRFServiceFor3th.IRFServiceFor3th.** {*;}
-keep class com.feinno.rongfly.core.service.RFProxy.** {*;}
-keep class com.feinno.circle.**{*;}
-keep class com.feinno.publibrary.**{*;}
-keep class com.feinno.zylibrary.**{*;}
-keep class com.feinno.onlinehall.**{*;}
-keep class com.feinno.redpaper.**{*;}
-keep class com.feinno.ipos2_hblib.**{*;}
-keep class com.hisun.b2c.api.**{*;}
-keep class com.feinno.redpaper.utils.SdkInitManager4$*{
    public <fields>;
    public <methods>;
}
-keep class com.feinno.rongfly.ui.browser.** {*;}
-keep class com.feinno.androidbase.utils.log.**{*;}
-keep class com.feinno.rongfly.ui.littlegroup.**{*;}
-keep class com.feinno.rongfly.core.modules.littlegroup.**{*;}
-keep class com.feinno.rongfly.ui.session.adapter.**{*;}
-keep class com.feinno.rongfly.ui.publicplatform.adapter.**{*;}
-keep class com.feinno.rongfly.core.modules.publicplatform.model.**{*;}
-keep class com.feinno.rongfly.core.modules.setting.model.**{*;}
-keep class com.feinno.rongfly.core.modules.statistics.model.** {*;}
-keep class com.feinno.rongfly.network.** {*;}
-dontwarn net.sqlcipher.**
-keep class net.sqlcipher.** {*;}
-keep class com.feinno.rongfly.plugin.** {*;}
-keep class com.rcspublicaccount.api.** {*;}
-keep class com.feinno.rongfly.core.modules.session.model.** {*;}
-keep class com.feinno.rongfly.core.modules.corporation.model.** {*;}



