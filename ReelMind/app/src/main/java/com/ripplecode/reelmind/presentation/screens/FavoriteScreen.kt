package com.ripplecode.reelmind.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ripplecode.reelmind.data.local.FavoriteMovieDao
import com.ripplecode.reelmind.data.repository.FavoriteMovieRepository
import com.ripplecode.reelmind.domain.model.FavoriteMovie
import com.ripplecode.reelmind.presentation.viewmodel.FavoriteMovieViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    viewModel: FavoriteMovieViewModel = koinViewModel()
) {
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Favoritos") })
        }
    ) { padding ->
        if (favoriteMovies.isEmpty()) {
            EmptyFavoritesScreen()
        } else {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoriteMovies) { movie ->
                    FavoriteMovieItem(
                        movie = movie,
                        onRemove = { viewModel.removeFromFavorites(it) })
                }
            }
        }
    }
}

@Composable
fun EmptyFavoritesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Nenhum favorito encontrado!", color = Color.Gray)
    }
}

@Composable
fun FavoriteMovieItem(
    movie: FavoriteMovie,
    onRemove: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = movie.posterPath.let { "https://image.tmdb.org/t/p/w500$it" }
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Movie Poster",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = movie.title, style = MaterialTheme.typography.headlineSmall)
                Text(text = "Nota: ${movie.voteAverage}")
            }
            IconButton(onClick = { onRemove(movie.id) }) {
                Icon(
                    painter = painterResource(id = com.ripplecode.reelmind.R.drawable.ic_favorite),
                    contentDescription = "Remove Favorite",
                    tint = Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteScreenPreview() {
    val dummyMovies = listOf(
        FavoriteMovie(
            id = 1,
            title = "Interstellar",
            posterPath = "",
            voteAverage = 8.6,
            overview = ""
        ),
        FavoriteMovie(
            id = 2,
            title = "Inception",
            posterPath = "",
            voteAverage = 8.8,
            overview = ""
        ),
        FavoriteMovie(
            id = 3,
            title = "The Dark Knight",
            posterPath = "",
            voteAverage = 9.0,
            overview = ""
        )
    )

    val fakeRepository = object : FavoriteMovieRepository(FakeFavoriteMovieDao()) {
        override fun getAllFavorites(): Flow<List<FavoriteMovie>> = flowOf(dummyMovies)
    }

    val viewModel = FavoriteMovieViewModel(fakeRepository)

    FavoriteScreen(viewModel = viewModel)
}

class FakeFavoriteMovieDao : FavoriteMovieDao {
    private val favorites = mutableListOf<FavoriteMovie>()

    override fun getAllFavorites(): Flow<List<FavoriteMovie>> = flowOf(favorites)

    override suspend fun insertFavorite(movie: FavoriteMovie) {
        favorites.add(movie)
    }

    override suspend fun deleteFavorite(movieId: Int) {
        favorites.removeIf { it.id == movieId }
    }

    override suspend fun isFavorite(movieId: Int): Boolean {
        return favorites.any { it.id == movieId }
    }
}