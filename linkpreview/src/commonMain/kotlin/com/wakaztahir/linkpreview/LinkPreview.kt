package com.wakaztahir.linkpreview;

data class LinkPreview(
    var url: String?,
    var imageUrl: String?,
    var title: String,
    var description: String,
    var siteName: String?,
    var mediaType: String,
    var faviconUrl: String?,
    var originalUrl: String,
)

expect suspend fun getLinkPreview(url: String,userAgent : String? = null): LinkPreview