package com.wakaztahir.linkpreview

import org.jsoup.Connection
import org.jsoup.Jsoup
import java.net.URI

actual suspend fun getLinkPreview(
    url: String,
    userAgent: String?
): LinkPreview {
    var connection: Connection = Jsoup.connect(url)
        .timeout(30 * 1000)
    if (!userAgent.isNullOrEmpty()) {
        connection = connection.userAgent(userAgent)
    }
    val doc = connection.get()

    val elements: org.jsoup.select.Elements = doc.getElementsByTag("meta")

    var title = doc.select("meta[property=og:title]").attr("content")

    if (title.isNullOrEmpty()) {
        title = doc.title()
    }

    //getDescription
    var description = doc.select("meta[name=description]").attr("content")
    if (description.isNullOrEmpty()) {
        description = doc.select("meta[name=Description]").attr("content")
    }
    if (description.isNullOrEmpty()) {
        description = doc.select("meta[property=og:description]").attr("content")
    }
    if (description.isNullOrEmpty()) {
        description = ""
    }

    // getMediaType
    val mediaTypes: org.jsoup.select.Elements = doc.select("meta[name=medium]")
    val type = if (mediaTypes.size > 0) {
        val media: String = mediaTypes.attr("content")
        if (media == "image") "photo" else media
    } else {
        doc.select("meta[property=og:type]").attr("content")
    }

    //getImages
    var imageUrl: String? = null
    var imageElements: org.jsoup.select.Elements = doc.select("meta[property=og:image]")
    if (imageElements.size > 0) {
        val image = imageElements.attr("content")
        if (!image.isNullOrEmpty()) {
            imageUrl = resolveURL(url, image)
        }
    }

    //get image from meta[name=og:image]
    if (imageUrl.isNullOrEmpty()) {
        imageElements = doc.select("meta[name=og:image]")
        if (imageElements.size > 0) {
            val image = imageElements.attr("content")
            if (!image.isNullOrEmpty()) {
                imageUrl = resolveURL(url, image)
            }
        }
    }

    var faviconUrl: String? = null

    if (imageUrl.isNullOrEmpty()) {
        var src = doc.select("link[rel=image_src]").attr("href")
        if (!src.isNullOrEmpty()) {
            imageUrl = resolveURL(url, src)
        } else {
            src = doc.select("link[rel=apple-touch-icon]").attr("href")
            if (!src.isNullOrEmpty()) {
                imageUrl = resolveURL(url, src)
                faviconUrl = resolveURL(url, src)
            } else {
                src = doc.select("link[rel=icon]").attr("href")
                if (!src.isNullOrEmpty()) {
                    imageUrl = resolveURL(url, src)
                    faviconUrl = resolveURL(url, src)
                }
            }
        }
    }

    //Favicon
    var src = doc.select("link[rel=apple-touch-icon]").attr("href")
    if (!src.isNullOrEmpty()) {
        faviconUrl = resolveURL(url, src)
    } else {
        src = doc.select("link[rel=icon]").attr("href")
        if (!src.isNullOrEmpty()) {
            faviconUrl = resolveURL(url, src)
        }
    }

    var siteUrl: String? = null
    var siteName: String? = null

    for (element in elements) {
        if (element.hasAttr("property")) {
            val strProperty: String = element.attr("property").trim { it <= ' ' }
            if (strProperty == "og:url") {
                siteUrl = element.attr("content")
            }
            if (strProperty == "og:site_name") {
                siteName = element.attr("content")
            }
        }
    }

    if (siteUrl.isNullOrEmpty()) {
        var uri: URI? = null
        try {
            uri = URI(url)
        } catch (e: java.net.URISyntaxException) {
            e.printStackTrace()
        }
        siteUrl = if (siteUrl.isNullOrEmpty()) {
            url
        } else {
            uri?.host
        }
    }

    return LinkPreview(
        url = siteUrl,
        title = title,
        description = description,
        mediaType = type,
        imageUrl = imageUrl,
        siteName = siteName,
        faviconUrl = faviconUrl,
        originalUrl = url
    )
}

private fun resolveURL(url: String, part: String): String? {
    return if (android.webkit.URLUtil.isValidUrl(part)) {
        part
    } else {
        return try {
            URI(url).resolve(part).toString()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }
}