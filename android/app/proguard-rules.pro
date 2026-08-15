# ProGuard rules for Solo Leveling SSC Prep
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
-keep class com.sololeveling.sscprep.domain.model.** { *; }
