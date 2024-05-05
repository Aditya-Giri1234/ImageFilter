package com.aditya.imagefilter.util

object   Constant {
    // App Level

    const val IMAGE_PATH="Saved Images"
    const val SAVED_IMAGES="Saved Images"

   enum class FragmentNeed{
        RemoveTopMargin,
        AddTopMargin
    }

    // LogLevel
    enum class LogLevel{
        Error ,
        Warning ,
        Debug ,
        Info ,
        Verbose
    }

    enum class LogTag(val icon:String) {
        Error("❌"),
        Warning("⚠️"),
        Debug("🐞"),
        Info("ℹ️"),
        Verbose("🔊") ,
        Other("❓") ,
        ImageProcessing("🖼️")
    }

}