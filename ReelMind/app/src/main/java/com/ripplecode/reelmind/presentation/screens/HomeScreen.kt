package com.ripplecode.reelmind.presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.ripplecode.reelmind.R
import com.ripplecode.reelmind.domain.model.Movie
import com.ripplecode.reelmind.presentation.viewmodel.DetailViewModel
import com.ripplecode.reelmind.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    genres: Set<String>,
    viewModel: HomeViewModel = koinViewModel(),
    detailViewModel: DetailViewModel = koinViewModel(),
    onMovieClick: (Movie) -> Unit
) {
    val trendingMovies by viewModel.trendingMovies.collectAsState() // 🔥 Em Alta
    val topRatedMovies by viewModel.topRatedMovies.collectAsState() // ⭐ Melhores Avaliados
    val latestMovies by viewModel.latestMovies.collectAsState() // 🎞️ Lançamentos
    val recommendedMovies by viewModel.recommendedMovies.collectAsState() // 🎬 Recomendações

    val searchResults by viewModel.searchResults.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedMovieId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text("ReelMind") })

                // Campo de busca fixo no topo
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.searchMovies(it)
                    },
                    placeholder = { Text("Buscar filmes...") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                                viewModel.searchMovies("")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpar busca"
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ) // Aplica padding corretamente
        ) {
            if (searchQuery.isNotEmpty()) {
                // Exibir os resultados da busca
                items(searchResults) { movie ->
                    MovieItem(movie = movie,
                        onMovieClick = {
                            detailViewModel.clearMovieDetail()
                            selectedMovieId = movie.id
                        })
                }
            } else {
                // Exibir os carrosséis de filmes
                item {
                    MovieBanner(movies = trendingMovies,
                        onMovieClick = {
                            detailViewModel.clearMovieDetail()
                            selectedMovieId = it.id
                        })
                }
                item {
                    MovieCarousel(
                        title = "Recomendados para Você",
                        movies = recommendedMovies,
                        onMovieClick = {
                            detailViewModel.clearMovieDetail()
                            selectedMovieId = it.id
                        }
                    )
                }
                item {
                    MovieCarousel(
                        title = "Melhores Avaliados",
                        movies = topRatedMovies,
                        onMovieClick = {
                            detailViewModel.clearMovieDetail()
                            selectedMovieId = it.id
                        }
                    )
                }
                item {
                    MovieCarousel(title = "Lançamentos",
                        movies = latestMovies,
                        onMovieClick = {
                            detailViewModel.clearMovieDetail()
                            selectedMovieId = it.id
                        })
                }
            }
        }
    }

    selectedMovieId?.let { movieId ->
        DetailScreen(movieId = movieId, onDismiss = { selectedMovieId = null })
    }
}

// 🔥 Destaque maior para "Em Alta"
@Composable
fun MovieBanner(movies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    Text(
        text = "Em Alta",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(8.dp)
    )

    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(movies) { movie ->
            Box(
                modifier = Modifier
                    .width(200.dp) // Defina um tamanho fixo para os banners
                    .height(300.dp) // Defina um tamanho fixo para os banners
//                    .aspectRatio(16f / 9f) // Mantém a proporção correta
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onMovieClick(movie) }
            ) {
                val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                Log.d("MovieImage", "URL da imagem: $imageUrl") // 🔥 Debug da URL

                AsyncImage(
                    model = imageUrl,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop, // Ajusta a imagem sem distorcer
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


// 🎬 Componente para os carrosséis de filmes
@Composable
fun MovieCarousel(title: String, movies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 8.dp)
        )

        LazyRow {
            items(movies) { movie ->
                MovieCard(
                    movie = movie,
                    onMovieClick = onMovieClick
                ) // Usando o MovieCard corrigido
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie, onMovieClick: (Movie) -> Unit) {
    Column(
        modifier = Modifier
            .width(140.dp) // Define um tamanho padrão para os cards
            .height(220.dp) // Altura fixa para evitar inconsistências
            .padding(4.dp)
            .clickable { onMovieClick(movie) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"

        AsyncImage(
            model = imageUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(140.dp)
                .height(180.dp) // Pôster com altura fixa para manter padrão
                .clip(RoundedCornerShape(8.dp))
        )
        Text(
            text = movie.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1, // Evita que títulos longos quebrem o layout
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "Nota: ${movie.voteAverage}",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}


@Composable
fun MovieItem(movie: Movie, onMovieClick: (Movie) -> Unit) {
    val imageUrl = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
        ?: "https://via.placeholder.com/500x750?text=Sem+Imagem"

    Log.d("MovieImage", "URL da imagem: $imageUrl") // 🔥 Debug da URL

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onMovieClick(movie) }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = rememberAsyncImagePainter(
                model = imageUrl,
                error = painterResource(id = R.drawable.ic_no_image)
            )

            Image(
                painter = painter,
                contentDescription = "Poster de ${movie.title}",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Nota: ${movie.voteAverage}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
