package com.ripplecode.reelmind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ripplecode.reelmind.domain.model.Movie
import com.ripplecode.reelmind.presentation.viewmodel.MovieRouletteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieRouletteScreen(
    viewModel: MovieRouletteViewModel = koinViewModel(),
    onMovieSelected: (Movie) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { Int.MAX_VALUE }) // Rolagem infinita
    val scope = rememberCoroutineScope()
    val movies by viewModel.movies.collectAsState()
    val posterSize = 600.dp  // Ajuste no tamanho do pôster

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎲 Gire a Roleta!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.height(350.dp), // Altura ajustada para os pôsteres
            contentPadding = PaddingValues(horizontal = 50.dp),
            pageSpacing = 20.dp
        ) { page ->
            val currentMovies = remember(movies) { movies.ifEmpty { listOf(Movie.empty()) } }
            val movie = currentMovies[page % currentMovies.size]

            Box(
                modifier = Modifier
                    .size(posterSize)
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(12.dp))
                    .clickable {
                        onMovieSelected(movie)
                    }
            ) {
                val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"

                AsyncImage(
                    model = imageUrl,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                spinRoulette(pagerState, movies, scope) { selectedMovie ->
                    onMovieSelected(selectedMovie)
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(0.7f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = "Sortear 🎬", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

private fun spinRoulette(
    pagerState: PagerState,
    movies: List<Movie>,
    scope: CoroutineScope,
    onMovieSelected: (Movie) -> Unit
) {
    if (movies.isEmpty()) return // Evita erro de lista vazia

    val randomIndex = movies.indices.random() // Filme sorteado
    val totalSpins = 7 // Número de voltas completas antes de parar
    val targetPage = pagerState.currentPage + (totalSpins * movies.size) + randomIndex

    var delayTime = 50L // Começa rápido

    scope.launch {
        for (page in pagerState.currentPage..targetPage) {
            pagerState.scrollToPage(page)

            // Aceleração -> Desaceleração: Atraso diminui no começo e aumenta no final
            delay(delayTime)
            delayTime =
                (delayTime * 1.05).coerceAtMost(350.0).toLong() // Aumenta o delay no final para desacelerar
        }

        // **Corrige o problema: Garante que o filme centralizado seja o sorteado**
        val finalIndex = pagerState.currentPage % movies.size

        onMovieSelected(movies[finalIndex])
    }
}