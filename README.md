# AnimeDex (Kotlin)

App Android feito em Kotlin com Jetpack Compose que consome a API pública [Jikan](https://docs.api.jikan.moe) (dados do MyAnimeList). Não precisa de chave de API.

## Funcionalidades

- Lista dos animes mais bem avaliados
- Busca por nome
- Tela de detalhes (nota, episódios, ano, status, gêneros e sinopse)
- Estados de carregamento, erro (com botão "Tentar de novo") e lista vazia

## Tecnologias

- Kotlin
- Jetpack Compose (Material 3)
- Retrofit + Gson (consumo da API)
- Coil (carregamento de imagens)
- ViewModel com estado (MVVM simples)

## Estrutura

- `data/JikanApi.kt`: interface Retrofit com os endpoints `top/anime` e `anime?q=`
- `data/Models.kt`: classes que representam o JSON da API
- `AnimeViewModel.kt`: busca os dados e guarda o estado da tela
- `AnimeApp.kt`: telas (lista, card e detalhes)
- `MainActivity.kt`: ponto de entrada

## Como rodar

1. Abra a pasta no Android Studio
2. Aguarde o Gradle sincronizar
3. Rode em um emulador ou celular com Android 7.0 (API 24) ou superior

API usada: `https://api.jikan.moe/v4/`
