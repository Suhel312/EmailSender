package com.suhel.emailsendimgapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.floatObjectMapOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suhel.emailsendimgapp.ui.theme.EmailSendimgAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmailSendimgAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                modifier = Modifier.background(Color.White),
                                title = {
                                    Text(
                                        text = "Email Sender",
                                        color = Color.White,
                                        fontSize = 20.sp
                                    )
                                }
                            )
                        }
                    ) { innerPadding ->
                        OpenEmailer(modifier = Modifier.padding(innerPadding))
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenEmailer(modifier: Modifier) {
    var senderEmail by remember {
        mutableStateOf("")
    }
    val emailSubject = remember {
        mutableStateOf("")
    }
    val emailBody = remember {
        mutableStateOf("")
    }
    var isEmailValid by remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.email),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()

            )

            TextField(
                value = senderEmail,
                onValueChange = {
                    senderEmail = it
                    isEmailValid = true
                },
                isError = !isEmailValid,
                placeholder = { Text(text = "Enter sender email address") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = if (isEmailValid) Color.Gray else Color(0xFFFFEBEE), // Light or error background
                    cursorColor = Color.Blue,
                    errorCursorColor = Color.Red,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Red
                )
            )
            if (!isEmailValid) {
                Text(
                    text = "Invalid email address",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 18.dp)
                )
            }
            Spacer(modifier = Modifier.heightIn(10.dp))

            TextField(
                value = emailSubject.value,
                onValueChange = { emailSubject.value = it },
                placeholder = { Text(text = "Enter email subject") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = if (isEmailValid) Color.Gray else Color(0xFFFFEBEE), // Light or error background
                    cursorColor = Color.Blue,
                    errorCursorColor = Color.Red,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Red
                )
            )
            Spacer(modifier = Modifier.heightIn(10.dp))

            TextField(
                value = emailBody.value,
                onValueChange = { emailBody.value = it },
                placeholder = { Text(text = "Enter email body") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                maxLines = 5,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = if (isEmailValid) Color.Gray else Color(0xFFFFEBEE), // Light or error background
                    cursorColor = Color.Blue,
                    errorCursorColor = Color.Red,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Red
                )
            )
            Spacer(modifier = Modifier.heightIn(10.dp))

            Button(onClick = {
                if (isValidEmail(senderEmail)) {
                    val intent = Intent(Intent.ACTION_SEND)

                    val emailAddress = arrayOf(senderEmail)
                    intent.putExtra(Intent.EXTRA_EMAIL, emailAddress)
                    intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject.value)
                    intent.putExtra(Intent.EXTRA_TEXT, emailBody.value)

                    intent.setType("message/rfc822")

                    context.startActivity(Intent.createChooser(intent, "Choose an email client"))
                } else {
                    isEmailValid = false
                }
            }) {
                Text(
                    text = "Send Email",
                    modifier = Modifier
                        .padding(10.dp),
                    color = Color.White,
                    fontSize = 15.sp
                )
            }
        }
    }
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
    return email.matches(emailRegex)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OpenEmailerPreview() {
    OpenEmailer(modifier = Modifier)
}