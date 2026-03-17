package com.kmpnewsapp.core.utils

import coil3.ImageLoader
import coil3.request.allowHardware

actual fun ImageLoader.Builder.configurePlatform(): ImageLoader.Builder {
    return this.allowHardware(false)
}
