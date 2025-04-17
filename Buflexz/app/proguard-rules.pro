-dontskipnonpubliclibraryclassmembers
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizationpasses 5
-dontusemixedcaseclassnames
-keepattributes Signature,SourceFile,LineNumberTable,*Annotation*,JavascriptInterface,Exceptions
-renamesourcefileattribute SourceFile
-dontpreverify
-verbose
-dontnote retrofit2.Platform,retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn com.igaworks.**,okio.**,retrofit2.Platform$Java8,com.sendbird.android.**,org.apache.**,com.google.android.**,android.support.v4.**,android.webkit.WebView,android.net.http.SslError,android.webkit.WebViewClient,android.util.FloatMath,com.google.gson.**,android.support.v4.**,org.slf4j.**,com.google.android.gms.**,android.support.**,android.support.v7.**,retrofit2.**,com.crashlytics.**
-ignorewarnings


# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\dev\adt-bundle-windows-x86_64-20140702\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
# 
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
# Add any project specific keep options here:
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
# -keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
# }
# #---------------Begin: proguard configuration for Igaworks Common  ----------

-keepclasseswithmembernames class java.lang.Class { *; }

##---------------Begin: proguard configuration for Igaworks Adpopcorn   ----------
-keep class com.igaworks.adpopcorn.** { *; }
-keep class com.igaworks.adpopcorn.R$*
-dontwarn com.igaworks.adpopcorn.**

-keepclassmembers class com.igaworks.adpopcorn.R$* {
    public static <fields>;
}
##---------------End: proguard configuration for Igaworks Adpopcorn   --------—

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.igaworks.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.igaworks.adbrix.model.** { *; }

##---------------End: proguard configuration for Gson  ----------

-keep class com.google.gson.examples.android.model.** {
    <fields>;
    <methods>;
}

# #---------------End: proguard configuration for Gson  ----------
# -dontwarn android.support.v4.**,org.slf4j.**,com.google.android.gms.**
-keep class com.squareup.okhttp.** {
    <fields>;
    <methods>;
}

-keep interface  com.squareup.okhttp.** {
    <fields>;
    <methods>;
}

# 샌드버드
-keep class com.sendbird.android.** {
    <fields>;
    <methods>;
}

-keep interface  com.sendbird.android.** {
    <fields>;
    <methods>;
}

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Fragment

-keep public class * extends androidx.fragment.app.Fragment

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.app.backup.BackupAgentHelper

-keep public class * extends android.preference.Preference

-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

# -keep public class com.google.vending.licensing.ILicensingService
# -keep public class com.android.vending.licensing.ILicensingService
-keep class com.root37.buflexz.BuflexzApplication {
    <fields>;
    <methods>;
}

-keep class io.fabric.sdk.** {
    <fields>;
    <methods>;
}

-keep class android.support.** {
    <fields>;
    <methods>;
}

-keep interface  android.support.** {
    <fields>;
    <methods>;
}

-keep class **.R$* {
    <fields>;
}

-keep class ch.qos.** {
    <fields>;
    <methods>;
}

-keep class org.slf4j.** {
    <fields>;
    <methods>;
}

-keep class org.apache.** {
    <fields>;
    <methods>;
}

-keep interface  ch.qos.** {
    <fields>;
    <methods>;
}

-keep interface  org.slf4j.** {
    <fields>;
    <methods>;
}

-keep interface  org.apache.** {
    <fields>;
    <methods>;
}

-keep class com.google.firebase.** {
    <fields>;
    <methods>;
}

-keep interface  com.google.firebase.** {
    <fields>;
    <methods>;
}

-keep class com.twitter.sdk.** {
    <fields>;
    <methods>;
}

-keep interface  com.twitter.sdk.** {
    <fields>;
    <methods>;
}

-keepclassmembers class * {
    public static <fields>;
    public <fields>;
    public <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keep public class com.android.** {
    <fields>;
    <methods>;
}

-keep public class com.google.** {
    <fields>;
    <methods>;
}

# ## 사용하는 외부 클래스 모두, 내부 클래스 중 필요한 것 예외처리 ################
# 모델들
-keep class com.root37.buflexz.core.network.model.** {
    *;
}

-keep class com.root37.buflexz.core.network.model.dto.** {
    *;
}

-keep class com.root37.buflexz.apps.common.builder.data.** {
    *;
}

-keep class com.root37.buflexz.core.network.model.response.** {
    *;
}

-keep class com.root37.buflexz.apps.common.mgmt.** {
    *;
}


-keepclassmembers class * {
    public static <fields>;
    public <fields>;
    public <methods>;
}

-keep class retrofit.** {
    <fields>;
    <methods>;
}

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keepclassmembers enum  * {
    <fields>;
    <methods>;
}

-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class org.apache.** {
    <fields>;
    <methods>;
}

-keep class android.support.v4.app.** {
    <fields>;
    <methods>;
}

-keep interface  android.support.v4.app.** {
    <fields>;
    <methods>;
}

-keep class android.support.v7.** {
    <fields>;
    <methods>;
}

-keep interface  android.support.v7.** {
    <fields>;
    <methods>;
}

-keep class org.apache.http.** {
    <fields>;
    <methods>;
}

-keep interface  org.apache.http.** {
    <fields>;
    <methods>;
}

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public <fields>;
    public <methods>;
}

# for google play service library
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Glide 이미지 로더
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

-keep class retrofit2.** {
    <fields>;
    <methods>;
}

-keep class org.apache.commons.logging.**

# -keep class com.google.android.gms.** { *; }
-keep class com.crashlytics.** {
    <fields>;
    <methods>;
}

# GreenDao rules
# Source: http://greendao-orm.com/documentation/technical-faq
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}

-keep class **$Properties

# Application classes that will be serialized/deserialized over Gson
# -keep class com.google.gson.examples.android.model.** { *; }
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapterFactory

-keep class * extends com.google.gson.JsonSerializer

-keep class * extends com.google.gson.JsonDeserializer

-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter


-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

# # 구글 proguard 적용
-keepclasseswithmembers,allowshrinking class com.google.android.gms.ads.identifier.AdvertisingIdClient {
    <fields>;
    <methods>;
}

-keepclasseswithmembers,allowshrinking class com.google.android.gms.common.GooglePlayServicesUtil {
    <fields>;
    <methods>;
}

-keep,allowshrinking class * extends android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep,allowshrinking class * extends java.io.Serializable

# -keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
#    public static final *** NULL;
# }
-keep,allowshrinking @com.google.android.gms.common.annotation.KeepName class *

-keepclassmembers,allowshrinking class * {
    @com.google.android.gms.common.annotation.KeepName <fields>;
    @com.google.android.gms.common.annotation.KeepName <methods>;
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

-keepclassmembers class com.root37.buflexz.apps.common.ui.SearchAddressActivity$AndroidBridge {
    public *;
}


-keep class com.github.mikephil.charting.** { *; }
-dontwarn io.realm.**

# 빌드 SDK 버전이 킷캣(Android 19) 이하 일 경우 추가해주세요.
-dontwarn android.os.Build

# 유니티를 이용한 경우 classes.jar 경로를 확인하고 넣어주시기 바랍니다.
# Mac OS 경로 예시 : /Applications/Unity/Unity.app/Contents/PlaybackEngines/AndroidPlayer/development/bin/classes.jar
# Windows 경로 예시 : 'C:\Program Files (x86)\Unity\Editor\Data\PlaybackEngines\androidplayer\bin\classes.jar'
#-libraryjars /Applications/Unity/Unity.app/Contents/PlaybackEngines/AndroidPlayer/development/bin/classes.jar

# Inmobi 사용을 위한 설정입니다.
-keep class com.inmobi.** { *; }
-dontwarn com.inmobi.**
-keep public class com.google.android.gms.**
-dontwarn com.google.android.gms.**
-dontwarn com.squareup.picasso.**
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{
     public *;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
     public *;
}

## skip the Picasso library classes
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**

## skip Moat classes
-keep class com.moat.** {*;}
-dontwarn com.moat.**

## skip AVID classes
-keep class com.integralads.avid.library.* {*;}

# Inmobi 사용을 위한 설정입니다. -- END

# AdMob 사용을 위한 설정입니다.
#-keep class test.adlib.project.ads.SubAdlibAdViewAdmob { *; }
-keep public class com.google.android.gms.ads.**{
    public *;
}
-keep public class com.google.ads.**{
    public *;
}

-dontwarn com.httpmodule.**
-dontwarn com.imgmodule.**
-keep class com.httpmodule.** { *; }
-keep class com.imgmodule.** { *; }
-keep public class com.mobon.**{
 public *;
}


-dontwarn retrofit2.**
-dontwarn org.codehaus.mojo.**
-keep class retrofit2.** { *; }
#-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

-keepattributes EnclosingMethod

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keepclasseswithmembers interface * {
    @retrofit2.* <methods>;
}

-keep class a.a.a.a.a.** { *; }
-dontwarn a.a.a.a.a.**


-dontwarn okio.**
-dontwarn com.squareup.picasso.**
-keepattributes *Annotation*
-keepattributes Signature

-keep class com.bytedance.sdk.** { *; }

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# With R8 full mode generic signatures are stripped for classes that are not kept.
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken