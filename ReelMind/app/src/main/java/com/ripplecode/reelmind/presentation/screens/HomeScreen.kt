package com.ripplecode.reelmind.presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
    val movies by viewModel.movies.collectAsState() // Lista de filmes populares
    val searchResults by viewModel.searchResults.collectAsState() // Lista de filmes buscados
    var searchQuery by remember { mutableStateOf("") }
    var selectedMovieId by remember { mutableStateOf<Int?>(null) }

    // Mapeia os nomes dos gêneros para IDs (precisamos ter essa correspondência)
    val genreIdMap = mapOf(
        "Ação" to 28,
        "Comédia" to 35,
        "Drama" to 18,
        "Terror" to 27,
        "Ficção Científica" to 878
    )

    val selectedGenreIds = genres.mapNotNull { genreIdMap[it] }.toSet()

    val filteredMovies = movies.filter { movie ->
        movie.genreIds.any { it in selectedGenreIds }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text("Recomendações") })

                // Campo de busca
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.searchMovies(it) // Faz a busca automaticamente
                    },
                    placeholder = { Text("Buscar filmes...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""  // Limpa o campo de busca
                                viewModel.searchMovies("") // Reseta os resultados da busca
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
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            val listToShow = if (searchQuery.isEmpty()) filteredMovies else searchResults

            items(listToShow) { movie ->
                MovieItem(movie = movie, onMovieClick = {
                    detailViewModel.clearMovieDetail()
                    selectedMovieId = movie.id
                })
            }
        }
    }

    // Exibir o diálogo de detalhes quando um filme for selecionado
    selectedMovieId?.let { movieId ->
        DetailScreen(
            movieId = movieId,
            onDismiss = { selectedMovieId = null }
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
