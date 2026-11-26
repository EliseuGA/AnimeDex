package com.eliseu.pokedex

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.eliseu.pokedex.data.PokemonDetail
import com.eliseu.pokedex.data.PokemonItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeApp(vm: PokeViewModel = viewModel()) {
    val selected = vm.selected

    // Botão "voltar" do celular fecha os detalhes
    BackHandler(enabled = selected != null) { vm.select(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selected?.displayName ?: "PokeDex",
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
                DetailScreen(vm, selected)
            } else {
                ListScreen(vm)
            }
        }
    }
}

@Composable
fun ListScreen(vm: PokeViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = vm.query,
            onValueChange = { vm.onQueryChange(it) },
            label = { Text("Buscar pokémon (nome ou número)") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        when (val s = vm.listState) {
            is ListState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is ListState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(s.message)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { vm.loadList() }) { Text("Tentar de novo") }
                }
            }

            is ListState.Ready -> {
                val itens = vm.filtered()
                if (itens.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nenhum pokémon encontrado.")
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(itens, key = { it.id }) { pokemon ->
                            PokemonCard(pokemon) { vm.select(pokemon) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonCard(pokemon: PokemonItem, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.displayName,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(80.dp)
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = pokemon.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("Nº ${pokemon.id}")
            }
        }
    }
}

@Composable
fun DetailScreen(vm: PokeViewModel, item: PokemonItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.displayName,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        )
        Text(
            text = "${item.displayName}  (Nº ${item.id})",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        when (val s = vm.detailState) {
            is DetailState.Loading -> CircularProgressIndicator()

            is DetailState.Error -> {
                Text(s.message)
                Spacer(Modifier.height(12.dp))
                Button(onClick = { vm.loadDetail(item) }) { Text("Tentar de novo") }
            }

            is DetailState.Success -> DetailInfo(s.pokemon)
        }
    }
}

@Composable
fun DetailInfo(p: PokemonDetail) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Tipos: ${p.types.joinToString(", ") { it.type.name }}")
        Text("Altura: ${p.height / 10.0} m")
        Text("Peso: ${p.weight / 10.0} kg")
        Text("Habilidades: ${p.abilities.joinToString(", ") { it.ability.name }}")
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Atributos base",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        p.stats.forEach { stat ->
            Text("${stat.stat.name}: ${stat.baseStat}")
        }
    }
}
