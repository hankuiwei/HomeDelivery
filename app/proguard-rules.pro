# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
 -keep public class com.baidu.crabsdk.**
   -keepclassmembers public class com.baidu.crabsdk.*{
        *;
   }
   #Native Crash收集请加入如下配置
   -keepclassmembers public class com.baidu.crabsdk.sender.NativeCrashHandler{
        *;
    }
-keepattributes EnclosingMethod

-keep class com.baidu.**{*;}
-keep class vi.com.gdi.bgl.**{*;}


-dontoptimize
-ignorewarnings
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-dontwarn com.baidu.**


-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod

-keep class com.baidu.navisdk.adapter.base.BaiduNaviSDKProxy { *; }
-keep class com.baidu.navisdk.adapter.BaiduNaviSDKProxy$* { *; }
-keep interface com.baidu.navisdk.adapter.BaiduNaviSDKProxy$* { *; }

-keep class com.baidu.navisdk.adapter.base.BaiduNaviSDKLoader { *; }
-keep class com.baidu.navisdk.adapter.BaiduNaviSDKLoader$* { *; }
-keep interface com.baidu.navisdk.adapter.BaiduNaviSDKLoader$* { *; }

-keep class com.baidu.navisdk.adapter.BaiduNaviCommonModule { *; }
-keep class com.baidu.navisdk.adapter.BaiduNaviCommonModule$* { *; }
-keep interface com.baidu.navisdk.adapter.BaiduNaviCommonModule$* { *; }

-keep class com.baidu.navisdk.adapter.BaiduNaviManager { *; }
-keep class com.baidu.navisdk.adapter.BaiduNaviManager$* { *; }
-keep interface com.baidu.navisdk.adapter.BaiduNaviManager$* { *; }

-keep interface com.baidu.navisdk.adapter.BNaviBaseCallback { *; }

-keep class com.baidu.navisdk.adapter.BNaviBaseCallbackModel { *; }
-keep class com.baidu.navisdk.adapter.BNaviBaseCallbackModel$* { *; }
-keep interface com.baidu.navisdk.adapter.BNaviBaseCallbackModel$* { *; }

-keep interface com.baidu.navisdk.adapter.BNaviCommonModuleCallback { *; }

-keep class com.baidu.navisdk.adapter.BNaviCommonModuleController { *; }
-keep class com.baidu.navisdk.adapter.BNaviCommonModuleController$* { *; }
-keep interface com.baidu.navisdk.adapter.BNaviCommonModuleController$* { *; }

-keep class com.baidu.navisdk.adapter.BNaviSettingManager { *; }
-keep class com.baidu.navisdk.adapter.BNaviSettingManager$* { *; }
-keep interface com.baidu.navisdk.adapter.BNaviSettingManager$* { *; }

-keep class com.baidu.navisdk.adapter.BNOuterLogUtil { *; }
-keep class com.baidu.navisdk.adapter.BNOuterLogUtil$* { *; }
-keep interface com.baidu.navisdk.adapter.BNOuterLogUtil$* { *; }

-keep interface com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback { *; }

-keep class com.baidu.navisdk.adapter.BNRouteGuideManager { *; }
-keep class com.baidu.navisdk.adapter.BNRouteGuideManager$* { *; }
-keep interface com.baidu.navisdk.adapter.BNRouteGuideManager$* { *; }


-keep class com.baidu.navisdk.adapter.BNRoutePlanNode { *; }
-keep class com.baidu.navisdk.adapter.BNRoutePlanNode$* { *; }
-keep interface com.baidu.navisdk.adapter.BNRoutePlanNode$* { *; }

-keep interface com.baidu.navisdk.adapter.CommandDeclare { *; }

-keep class com.baidu.navisdk.adapter.NaviModuleFactory { *; }
-keep class com.baidu.navisdk.adapter.NaviModuleFactory$* { *; }
-keep interface com.baidu.navisdk.adapter.NaviModuleFactory$* { *; }

-keep class com.baidu.navisdk.adapter.NaviModuleImpl { *; }
-keep class com.baidu.navisdk.adapter.NaviModuleImpl$* { *; }
-keep interface com.baidu.navisdk.adapter.NaviModuleImpl$* { *; }

-keep class com.baidu.navisdk.adapter.PackageUtil { *; }
-keep class com.baidu.navisdk.adapter.PackageUtil$* { *; }
-keep interface com.baidu.navisdk.adapter.PackageUtil$* { *; }

-keep class cn.thinkit.libtmfe.test.JNI{
    public protected <fields>;
    public protected <methods>;
}


-keepattributes JavascriptInterface
-keepattributes *Annotation*

-keep class com.sinovoice.hcicloudsdk.**{*;}
-keep interface com.sinovoice.hcicloudsdk.**{*;}
-dontwarn com.sinovoice.hcicloudsdk.**



-keep class * extends android.app.Activity
-keep class * extends android.app.Application

-keep class * extends android.content.ContentProvider
-keep class * extends android.app.backup.BackupAgentHelper
-keep class * extends android.preference.Preference
-keep class * extends android.os.Bundle

-dontwarn com.google.android.support.v4.**
-keep class com.google.android.support.v4.** { *; }
-keep interface com.google.android.support.v4.app.** { *; }
-keep public class * extends com.google.android.support.v4.**
-keep public class * extends com.google.android.support.v4.app.Fragment

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v4.app.Fragment

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}



#wallet
-keep class org.apache.commons.codec.**{*;}
-keep interface org.apache.commons.codec.**{*;}
-dontwarn org.apache.commons.codec.**

-keep class com.dianxinos.tokens.utils.**{*;}
-keep interface com.dianxinos.tokens.utils.**{*;}
-dontwarn com.dianxinos.tokens.utils.**



-keep public class android.net.http.SslError
-dontwarn android.net.http.SslError




-keep class com.dianxinos.tokens.utils.** { *; }




-dontwarn org.apache.commons.codec.**
-keep class org.apache.commons.codec.** { *; }
# baiduwallet.jar end