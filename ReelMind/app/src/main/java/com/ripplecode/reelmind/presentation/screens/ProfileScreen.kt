import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.ripplecode.reelmind.domain.model.Movie
import com.ripplecode.reelmind.presentation.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel(),
    onMovieClick: (Movie) -> Unit
) {
    val favorites by viewModel.favoriteMovies.collectAsState(initial = emptyList())
    val watcheds by viewModel.watchedMovies.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        Section(title = "Filmes Favoritos", movies = favorites, onMovieClick = onMovieClick)
        Section(title = "Filmes Assistidos", movies = watcheds, onMovieClick = onMovieClick)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("onboarding") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reconfigurar Gêneros")
        }
    }
}

@Composable
fun Section(title: String, movies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (movies.isEmpty()) {
            Text(
                text = "Nenhum filme encontrado",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        } else {
            LazyRow {
                items(movies) { movie ->
                    MovieItem(movie, onMovieClick = onMovieClick)
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onMovieClick: (Movie) -> Unit) {
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
    }
}