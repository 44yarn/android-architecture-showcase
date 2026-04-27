package io.github.yarn44.showcase.core.foundation.adaptive

import androidx.annotation.DrawableRes
import androidx.compose.ui.layout.ContentScale

/**
 * Unifies drawable resource IDs and remote URLs into a single type.
 * This allows ViewModels to provide image sources without platform-specific logic.
 */
class AdaptiveImage private constructor(
    val url: String?,
    @DrawableRes val resId: Int?,
    val type: ImageType,
) {
    constructor(url: String) : this(url = url, resId = null, type = ImageType.Icon)

    constructor(
        @DrawableRes resId: Int,
        type: ImageType = ImageType.Icon,
    ) : this(url = null, resId = resId, type = type)

    /** Describes how the image should be displayed. */
    sealed class ImageType {
        data object Icon : ImageType()

        data class FillMaxWidth(
            val contentScale: ContentScale = ContentScale.Crop,
            val aspectRatio: Float = 16f / 9f,
        ) : ImageType()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdaptiveImage) return false
        return url == other.url &&
            resId == other.resId &&
            type == other.type
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + resId.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}
