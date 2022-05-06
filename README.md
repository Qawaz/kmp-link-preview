## Lightweight Link Previewing Library for Kotlin Multiplatform

works on (Android,JVM)

It has no dependencies except jsoup which is used for parsing the html provided by the site to extract metadata
information like title , description , favicon and image

## Usage

Complete samples are in desktop and android modules

getLinkPreview is a suspending function you can call from a coroutine and it will return a LinkPreview object containing
the metadata information about the site , Use it however you like

```kotlin
// getLinkPreview throws an exception when encounters an error
val preview = try { getLinkPreview(url) }catch(ex : Exception){ null }
```
