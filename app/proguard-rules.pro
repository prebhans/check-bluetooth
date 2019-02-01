# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/pch/sdk/tools/proguard/proguard-android.txt
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

#-dontwarn java.lang.invoke**
#-dontwarn org.apache.lang.**
#-dontwarn org.apache.commons.**

#-keep class * extends java.util.ListResourceBundle {
#    protected Object[][] getContents();
#}

#-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
#    public static final *** NULL;
#}

#-keepnames @com.google.android.gms.common.annotation.KeepName class *
#-keepclassmembernames class * {
#    @com.google.android.gms.common.annotation.KeepName *;
#}

#-keepnames class * implements android.os.Parcelable {
#    public static final ** CREATOR;
#}

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** d(...);
    public static *** e(...);
}

#-keep public class com.google.android.gms.**
#-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,LineNumberTable, *Annotation*, EnclosingMethod
#-dontwarn com.google.android.gms.**

#-keep class com.google.android.gms.** {*;}
#-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,LineNumberTable, *Annotation*, EnclosingMethod
#-dontwarn android.webkit.JavascriptInterface

#-keep class com.android.support.multidex.** {*;}
#-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,LineNumberTable, *Annotation*, EnclosingMethod

-ignorewarnings

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

-printmapping mapping.txt

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
