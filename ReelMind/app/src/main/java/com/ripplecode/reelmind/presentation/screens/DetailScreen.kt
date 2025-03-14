package com.ripplecode.reelmind.presentation.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.ripplecode.reelmind.data.repository.FavoriteMovieRepository
import com.ripplecode.reelmind.domain.model.FavoriteMovie
import com.ripplecode.reelmind.domain.model.WatchedMovie
import com.ripplecode.reelmind.presentation.viewmodel.DetailViewModel
import com.ripplecode.reelmind.presentation.viewmodel.FavoriteMovieViewModel
import com.ripplecode.reelmind.presentation.viewmodel.WatchedMovieViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    movieId: Int,
    onDismiss: () -> Unit,
    favoriteMovieViewModel: FavoriteMovieViewModel = koinViewModel(),
    watchedMovieViewModel: WatchedMovieViewModel = koinViewModel()
) {
    val viewModel: DetailViewModel = koinViewModel(parameters = { parametersOf(movieId) })
    val movieDetail by viewModel.movieDetail.collectAsState()
    val isFavorite by favoriteMovieViewModel.isFavorite(movieId).collectAsState(initial = false)
    val isWatched by watchedMovieViewModel.isMovieWatched(movieId).collectAsState(initial = false)

    LaunchedEffect(movieId) {
        viewModel.clearMovieDetail() // Evita exibir o filme anterior
        viewModel.fetchMovieDetail(movieId)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            if (movieDetail == null) {
                // Loading
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val movie = movieDetail!!

                Column(modifier = Modifier.padding(16.dp)) {
                    // Linha superior com ícone de Fechar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.weight(1f)) // Empurra o botão para a direita

                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar"
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f) // Ocupa o espaço disponível
                            .verticalScroll(rememberScrollState()) // Habilita scroll
                    ) {
                        val imageUrl = movie.posterPath.let { "https://image.tmdb.org/t/p/w500$it" }

                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = "Poster do filme",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp) // Mantendo proporção correta
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit
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

                        // Plataformas de streaming
                        if (movie.streamingProviders.isNotEmpty()) {
                            Text(
                                text = "Disponível em:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                movie.streamingProviders.forEach { platform ->
                                    AsyncImage(
                                        model = platform.logoUrl,
                                        contentDescription = platform.name,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botões de ação
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val context = LocalContext.current

                            IconButton(onClick = {
                                favoriteMovieViewModel.toggleFavorite(
                                    FavoriteMovie(
                                        id = movie.id,
                                        title = movie.title,
                                        posterPath = movie.posterPath,
                                        voteAverage = movie.voteAverage,
                                        overview = movie.overview
                                    )
                                )

                                Toast.makeText(context, if(isFavorite) "Removido dos favoritos" else "Adicionado aos favoritos", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favoritar",
                                    tint = if (isFavorite) Color.Red else Color.Gray
                                )
                            }

                            // Marcar como assistido
                            IconButton(onClick = {
                                watchedMovieViewModel.toggleWatched(
                                    WatchedMovie(
                                        id = movie.id,
                                        title = movie.title,
                                        posterPath = movie.posterPath,
                                        voteAverage = movie.voteAverage,
                                        overview = movie.overview
                                    )
                                )

                                Toast.makeText(context, if(isWatched) "Removido dos assistidos" else "Adicionado aos assistidos", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Marcar como Assistido",
                                    tint = if (isWatched) Color.Green else Color.Gray
                                )
                            }

                            Log.d("DetailScreen", "Trailer URL: ${movie.trailerUrl}")

                            if (movie.trailerUrl.isNotEmpty()) {
                                Button(
//                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = {
                                        val intent =
                                            Intent(Intent.ACTION_VIEW, Uri.parse(movie.trailerUrl))
                                        context.startActivity(intent)
                                    }) {
                                    Text("Assistir Trailer")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
