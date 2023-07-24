# Keep Jetpack Compose classes and methods
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }
-keepclassmembers class kotlin.Metadata { *; }
-keepclassmembers class **$$DefaultImpls { *; }
-keep class * implements androidx.compose.runtime.Composable {
  <methods>;
}
-ignorewarnings