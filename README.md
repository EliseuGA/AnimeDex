# PokeDex (Kotlin)

App Android feito em Kotlin com Jetpack Compose que consome a API pública [PokéAPI](https://pokeapi.co). Não precisa de chave de API.

## Funcionalidades

- Lista de pokémons com imagem
- Busca por nome ou número enquanto digita
- Tela de detalhes (tipos, altura, peso, habilidades e atributos base)
- Estados de carregamento, erro (com botão "Tentar de novo") e lista vazia

## Tecnologias

- Kotlin
- Jetpack Compose (Material 3)
- Retrofit + Gson (consumo da API)
- Coil (carregamento de imagens)
- ViewModel com estado (MVVM simples)

## Estrutura

- `data/PokeApi.kt`: interface Retrofit com os endpoints `pokemon` e `pokemon/{id}`
- `data/Models.kt`: classes que representam o JSON da API
- `PokeViewModel.kt`: busca os dados e guarda o estado das telas
- `PokeApp.kt`: telas (lista, card e detalhes)
- `MainActivity.kt`: ponto de entrada

## Como rodar

1. Abra a pasta no Android Studio
2. Aguarde o Gradle sincronizar
3. Rode em um emulador ou celular com Android 7.0 (API 24) ou superior

API usada: `https://pokeapi.co/api/v2/`
