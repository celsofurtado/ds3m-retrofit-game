package br.senai.sp.gamesenairetrofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.gamesenairetrofit.api.GameCall
import br.senai.sp.gamesenairetrofit.api.RetrofitApi
import br.senai.sp.gamesenairetrofit.model.Game
import br.senai.sp.gamesenairetrofit.ui.theme.GameSenaiRetrofitTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameSenaiRetrofitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.LightGray
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {

    var titleState by remember {
        mutableStateOf("")
    }
    var studioState by remember {
        mutableStateOf("")
    }
    var descriptionState by remember {
        mutableStateOf("")
    }
    var releaseYearState by remember {
        mutableStateOf("")
    }

    // Load Games
    var games by remember {
        mutableStateOf(listOf<Game>())
    }

    val context = LocalContext.current

    val retrofit = RetrofitApi.getRetrofit()
    val gameCall = retrofit.create(GameCall::class.java)
    val call = gameCall.getGameById(1)
    val calls = gameCall.getGames()

    // List a game by id
    call.enqueue(object : Callback<Game> {
        override fun onResponse(call: Call<Game>, response: Response<Game>) {
            Log.i("ds3m", response.body().toString())
        }

        override fun onFailure(call: Call<Game>, t: Throwable) {
            Log.i("ds3m", t.message.toString())
        }
    })

    // List all games
    calls.enqueue(object : Callback<List<Game>> {
        override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
            games = response.body()!!
            Log.i("ds3m", games.toString())
        }

        override fun onFailure(call: Call<List<Game>>, t: Throwable) {
            Log.i("ds3m", t.message.toString())
        }

    })

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = Color.LightGray
    ) {
        Column() {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                backgroundColor = Color(152, 206, 231, 255)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = titleState,
                        onValueChange = { titleState = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Game tiltle")
                        }
                    )
                    OutlinedTextField(
                        value = studioState,
                        onValueChange = { studioState = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Game studio")
                        }
                    )
                    OutlinedTextField(
                        value = descriptionState,
                        onValueChange = { descriptionState = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Game description")
                        }
                    )
                    OutlinedTextField(
                        value = releaseYearState,
                        onValueChange = { releaseYearState = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Game release year")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Button(
                        onClick = {
                            val game = Game(
                                title = titleState,
                                studio = studioState,
                                releaseYear = releaseYearState.toInt(),
                                description = descriptionState
                            )
                            val gameCallSave = gameCall.saveGame(game)
                            gameCallSave.enqueue(object: Callback<Game>{
                                override fun onResponse(
                                    call: Call<Game>,
                                    response: Response<Game>
                                ) {
                                    val newGame = response.body()!!
                                    Toast.makeText(context, "${newGame.id} - ${newGame.title}", Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(call: Call<Game>, t: Throwable) {
                                    TODO("Not yet implemented")
                                }

                            })
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Save new Game")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(games) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                            .clickable {
                                titleState = it.title
                                studioState = it.studio
                                releaseYearState = it.releaseYear.toString()
                                descriptionState = it.description
                                Toast
                                    .makeText(context, "${it.id}", Toast.LENGTH_SHORT)
                                    .show()
                            },
                        backgroundColor = Color.White
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "${it.id} - ${it.title}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue
                            )
                            Text(text = it.studio)
                            Text(text = it.description)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GameSenaiRetrofitTheme {
        Greeting("Android")
    }
}