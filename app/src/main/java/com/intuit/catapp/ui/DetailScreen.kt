package com.intuit.catapp.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.intuit.catapp.data.Breed

@Composable
fun DetailScreen(
    breed: Breed,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = breed.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {

            AsyncImage(
                model = breed.getImageUrl(),
                contentDescription = breed.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = breed.name,
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = breed.description,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.body1
            )
        }
    }
}