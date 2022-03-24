import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.wakaztahir.linkpreview.LinkPreview
import com.wakaztahir.linkpreview.getLinkPreview
import kotlinx.coroutines.*
import java.io.IOException
import java.net.URL

fun main() = application {
    Window(onCloseRequest = ::exitApplication, state = WindowState(width = 400.dp)) {
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
                        load = { loadImageBitmap(imageUrl) },
                        painterFor = { remember { BitmapPainter(it) } },
                        contentDescription = "Sample",
                        modifier = Modifier.width(200.dp).align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun <T> AsyncImage(
    load: suspend () -> T,
    painterFor: @Composable (T) -> Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val image: T? by produceState<T?>(null) {
        value = withContext(Dispatchers.IO) {
            try {
                load()
            } catch (e: IOException) {
                // instead of printing to console, you can also write this to log,
                // or show some error placeholder
                e.printStackTrace()
                null
            }
        }
    }

    if (image != null) {
        Image(
            painter = painterFor(image!!),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

fun loadImageBitmap(url: String): ImageBitmap =
    URL(url).openStream().buffered().use(::loadImageBitmap)
