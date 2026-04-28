package com.intuit.catapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.intuit.catapp.data.Breed
import com.intuit.catapp.ui.theme.CatAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: BreedsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var selectedBreed: Breed? by remember { mutableStateOf(null) }
            var showDetail by remember { mutableStateOf(false) }
            // Set up to observe the ViewModel's LiveData and update the composable state as it's value changes
            var breedsData: List<Breed> by remember { mutableStateOf(emptyList()) }

            viewModel.breedsLiveData.observe(this) {
                breedsData = it.data ?: emptyList()
            }
            // Trigger GET Breeds from the API asynchronously
            LaunchedEffect(Unit) {
                viewModel.getBreeds()
            }

            CatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        if (showDetail && selectedBreed != null) {
                            DetailScreen(
                                breed = selectedBreed!!,
                                onBack = {
                                    showDetail = false
                                    selectedBreed = null
                                }
                            )
                        } else {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally)
                                    .padding(12.dp),
                                text = "Cat Breeds",
                                fontSize = 32.sp,
                                color = MaterialTheme.colors.onBackground,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            BreedsList(
                                data = breedsData,
                                onItemClick = { breed ->
                                    selectedBreed = breed
                                    showDetail = true
                                }
                            )
                        }
                    }
                }

                /**
                 * If you want to use traditional Android View heirarchy components, you can do so
                 * leveraging the interoperability of Compose with the `AndroidView` composable function.
                 *
                 * This allows you a way to define the `factory` param (a lambda that provides context and
                 * returns a View) to integrate Android Views around and inter-mingled with compose content.
                 *
                 * For more information, please check out the Android docs:
                 * https://developer.android.com/jetpack/compose/migrate/interoperability-apis/views-in-compose
                 */
                /*
                 AndroidView(
                    factory = { context ->
                        View(context)
                    },
                    modifier = Modifier,
                    update = { view ->
                        ...
                    }
                )
                 */
            }
        }
    }
}

@Composable
fun BreedsList(data: List<Breed>, onItemClick: (Breed) -> Unit) {
    var searchtext by remember { mutableStateOf("") }

    val filteredData = data.filter {
        it.name.contains(searchtext, ignoreCase = true)
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        label = { Text("Search Breeds") },
        onValueChange = { searchtext = it },
        value = searchtext,
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
    )

    LazyColumn {
        items(filteredData) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onItemClick(it) },
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
                elevation = 4.dp,
                border = BorderStroke(1.dp, MaterialTheme.colors.secondary)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = it.name,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        fontSize = 16.sp
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = it.description,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colors.primary,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
