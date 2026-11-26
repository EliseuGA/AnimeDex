package com.eliseu.animedex

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.eliseu.animedex.data.Anime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeApp(vm: AnimeViewModel = viewModel()) {
    val selected = vm.selected

    // Botão "voltar" do celular fecha os detalhes
    BackHandler(enabled = selected != null) { vm.select(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selected?.title ?: "AnimeDex",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (selected != null) {
                DetailScreen(selected)
            } else {
                ListScreen(vm)
            }
        }
    }
}

@Composable
fun ListScreen(vm: AnimeViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = vm.query,
            onValueChange = { vm.onQueryChange(it) },
            label = { Text("Buscar anime (Enter para pesquisar)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { vm.load() }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        when (val s = vm.state) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(s.message)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { vm.load() }) { Text("Tentar de novo") }
                }
            }

            is UiState.Success -> {
                if (s.items.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nenhum anime encontrado.")
                    }
                } else {
                    LazyColumn(
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(s.items, key = { it.malId }) { anime ->
                            AnimeCard(anime) { vm.select(anime) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimeCard(anime: Anime, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(10.dp)) {
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(width = 90.dp, height = 130.dp)
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = anime.title ?: "Sem título",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Text("Nota: ${anime.score ?: "-"}")
                Text("Episódios: ${anime.episodes ?: "?"}")
                Text("Ano: ${anime.year ?: "-"}")
            }
        }
    }
}

@Composable
fun DetailScreen(anime: Anime) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AsyncImage(
            model = anime.imageUrl,
            contentDescription = anime.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = anime.title ?: "Sem título",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text("Nota: ${anime.score ?: "-"}  |  Episódios: ${anime.episodes ?: "?"}  |  Ano: ${anime.year ?: "-"}")
        Text("Status: ${anime.status ?: "-"}")
        val generos = anime.genres?.joinToString(", ") { it.name }
        if (!generos.isNullOrBlank()) {
            Text("Gêneros: $generos")
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = anime.synopsis ?: "Sem sinopse disponível.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
