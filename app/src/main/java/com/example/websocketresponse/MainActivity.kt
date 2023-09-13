package com.example.websocketresponse

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.websocketresponse.ui.theme.WebsocketResponseTheme
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class MainActivity : ComponentActivity() {
    private val wssurl = "ws://192.168.141.222:8765"

    private val wssrequest: Request = Builder().url(wssurl).build()
    private val webSocket = OkHttpClient().newWebSocket(wssrequest, EchoWebSocketListener())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebsocketResponseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent(webSocket = webSocket)
                }
            }
        }
    }
}

private class EchoWebSocketListener : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        output("Receiving : $text")
    }

    // This will be unused in this assignment, but we'll leave it here
    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        output("Receiving bytes : " + bytes.hex())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        output("Closing : $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        output("Error : " + t.message)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }

    private fun output(txt: String) {
        Log.v("WSS", txt)
    }
}

@Composable
fun AppContent(webSocket: WebSocket) {
    var inputText by remember { mutableStateOf("") }
    var sentText by remember { mutableStateOf("") }
    var receivedText by remember { mutableStateOf("") }
    var mutableTxt by remember { mutableStateOf("0") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = inputText,
            onValueChange = { inputText = it },
            singleLine = true,
            textStyle = TextStyle(fontSize = 36.sp,
                color = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .background(Color.DarkGray)
        )
        Box(modifier = Modifier
            .size(340.dp)
            .background(Color.LightGray)
            .padding(vertical = 10.dp),
            contentAlignment = Alignment.TopCenter) {
            Text(color = Color.Red,
                fontSize = 20.sp,
                text = "The webSocket is responding: "
                )
            Text(modifier = Modifier.padding(vertical = 40.dp),
                text = sentText)
            Text(modifier = Modifier.padding(vertical = 80.dp),
                text = receivedText)
        }

        Spacer(modifier = Modifier.height(5.dp))
        Box(modifier = Modifier
            .size(340.dp, 60.dp)
            .background(Color.Black),
            contentAlignment = Alignment.Center) {
            Button(onClick = {
                sentText = "> SENT: {message: $inputText}"
                receivedText = "> RECEIVED: {message: $inputText}"
                webSocket.send(inputText) }) {
                println("DEBUG ${webSocket.request()}")
                Text(text = "Click me!")
            }
        }
        Spacer(modifier = Modifier.height(5.dp))

        Box(modifier = Modifier
            .size(340.dp, 260.dp)
            .background(Color.Black)) {
            Box(modifier = Modifier
                .size(50.dp)
                .background(Color.White)
                .align(Alignment.TopEnd))
            Box(modifier = Modifier
                .size(50.dp)
                .background(Color.Green)
                .align(Alignment.BottomEnd))
            Box(modifier = Modifier
                .size(50.dp)
                .background(Color.Red)
                .align(Alignment.BottomStart))
            Box(modifier = Modifier
                .size(50.dp)
                .background(Color.Blue)
                .align(Alignment.TopStart))
            Box(modifier = Modifier
                .size(50.dp)
                .background(Color.Green)
                .align(Alignment.CenterStart))
            Box(modifier = Modifier
                .size(50.dp)
                .background(Color.Blue)
                .align(Alignment.CenterEnd))
            Box(modifier = Modifier
                .size(115.dp, 50.dp)
                .background(Color.Magenta)
                .align(Alignment.Center),
                contentAlignment = Alignment.Center) {
                Text(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    text = mutableTxt
                )
            }
            Box(modifier = Modifier
                .size(117.dp, 50.dp)
                .background(Color.Yellow)
                .align(Alignment.TopCenter),
                contentAlignment = Alignment.Center){
                Button(onClick = {
                    mutableTxt = (mutableTxt.toInt() + 1).toString()
                }) {
                    Text(text = "Increment")
                }
            }
            Box(modifier = Modifier
                .size(117.dp, 50.dp)
                .background(Color.Cyan)
                .align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center) {
                Button(onClick = {
                    mutableTxt = (mutableTxt.toInt() - 1).toString()
                }) {
                    Text(text = "Decrement")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    WebsocketResponseTheme {

    }
}