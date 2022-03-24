package com.wakaztahir.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wakaztahir.linkpreview.LinkPreview
import com.wakaztahir.linkpreview.getLinkPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {

                val scope = rememberCoroutineScope()
                val defaultUrl = "https://www.thecandidadiet.com/recipe/blueberry-panna-cotta/"
                var url by remember { mutableStateOf(defaultUrl) }
                var preview by remember { mutableStateOf<LinkPreview?>(null) }

                suspend fun getPreview(printError: Boolean = true) {
                    preview = kotlin.runCatching { getLinkPreview(url) }.onFailure {
                        if (printError) it.printStackTrace() else println(it.message)
                    }.getOrNull()
                }

                Column(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                ) {

                    LaunchedEffect(null) { getPreview() }

                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = url,
                        onValueChange = {
                            url = it
                            scope.coroutineContext.cancelChildren()
                            scope.launch(Dispatchers.IO) {
                                delay(500)
                                getPreview(false)
                            }
                        },
                        label = {
                            Text(
                                text = "Url"
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Url"
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(textColor = MaterialTheme.colors.onBackground)
                    )

                    Text(
                        text = "Url : ${preview?.url}",
                        color = MaterialTheme.colors.onBackground
                    )
                    Text(
                        text = "Title : ${preview?.title}",
                        color = MaterialTheme.colors.onBackground
                    )
                    Text(
                        text = "Description : ${preview?.description}",
                        color = MaterialTheme.colors.onBackground
                    )
                    Text(
                        text = "Favicon : ${preview?.faviconUrl}",
                        color = MaterialTheme.colors.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val imageUrl = if (preview?.imageUrl != null) preview?.imageUrl else preview?.faviconUrl

                    if (imageUrl != null) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}