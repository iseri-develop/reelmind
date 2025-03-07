package com.ripplecode.reelmind.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.ripplecode.reelmind.data.repository.FavoriteMovieRepository
import com.ripplecode.reelmind.domain.model.FavoriteMovie
import com.ripplecode.reelmind.presentation.viewmodel.DetailViewModel
import com.ripplecode.reelmind.presentation.viewmodel.FavoriteMovieViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    movieId: Int,
    onDismiss: () -> Unit,
    favoriteMovieViewModel: FavoriteMovieViewModel = koinViewModel()
) {
    val viewModel: DetailViewModel = koinViewModel(parameters = { parametersOf(movieId) })
    val movieDetail by viewModel.movieDetail.collectAsState()
    val isFavorite by favoriteMovieViewModel.isFavorite(movieId).collectAsState()

    // Verificar se o filme já está favoritado
    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetail(movieId)
    }

    movieDetail?.let { movie ->
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val imageUrl = movie.posterPath.let { "https://image.tmdb.org/t/p/w500$it" }

                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Poster do filme",
                        modifier = Modifier
                            .height(250.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Nota: ${movie.voteAverage}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = movie.overview,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botão de Favoritar
                    IconButton(onClick = {
                        if (isFavorite) {
                            favoriteMovieViewModel.removeFromFavorites(movie.id)
                        } else {
                            favoriteMovieViewModel.addToFavorites(
                                FavoriteMovie(
                                    id = movie.id,
                                    title = movie.title,
                                    posterPath = movie.posterPath,
                                    voteAverage = movie.voteAverage,
                                    overview = movie.overview
                                )
                            )
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botão de Fechar
                    Button(onClick = onDismiss) {
                        Text("Fechar")
                    }
                }
            }
        }
    } ?: run {
        // Exibir um indicador de carregamento enquanto os detalhes do filme são carregados
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}