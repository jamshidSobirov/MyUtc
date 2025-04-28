package com.jmdev.myutc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jmdev.myutc.data.remote.CharacterApi
import com.jmdev.myutc.data.remote.CharacterRepository
import com.jmdev.myutc.ui.character_list.CharacterListScreen
import com.jmdev.myutc.ui.character_list.CharacterListViewModel
import com.jmdev.myutc.ui.theme.MyUTCTheme
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CharacterApi::class.java)

        val repository = CharacterRepository(api)
        val viewModel = CharacterListViewModel(repository)

        setContent {
            CharacterListScreen(viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyUTCTheme {

    }
}