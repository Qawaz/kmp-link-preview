package com.wakaztahir.linkpreview

actual suspend fun getLinkPreview(
    url: String,
    userAgent: String?
): LinkPreview {
    return LinkPreview(
        url = null,
        imageUrl = null,
        title = "",
        description = "",
        siteName = "",
        mediaType = "",
        faviconUrl = "",
        originalUrl = url,
    )
}