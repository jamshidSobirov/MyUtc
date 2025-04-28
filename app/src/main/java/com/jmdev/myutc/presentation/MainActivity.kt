package com.jmdev.myutc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jmdev.myutc.data.api.CharacterApi
import com.jmdev.myutc.data.repository.CharacterRepositoryImpl
import com.jmdev.myutc.presentation.character_list.CharacterListScreen
import com.jmdev.myutc.presentation.character_list.CharacterListViewModel
import com.jmdev.myutc.ui.theme.MyUTCTheme
import okhttp3.OkHttpClient
import org.koin.androidx.compose.koinViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: CharacterListViewModel = koinViewModel()
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