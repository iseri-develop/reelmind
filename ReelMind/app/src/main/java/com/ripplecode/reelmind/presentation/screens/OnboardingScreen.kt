package com.ripplecode.reelmind.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ripplecode.reelmind.data.store.UserPreferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.get

@Composable
fun OnboardingScreen(
    navController: NavController,
    userPreferencesDataStore: UserPreferencesDataStore = get(),
    onFinish: () -> Unit = {}
) {
    val selectedGenres = remember { mutableStateListOf<String>() }

    val allGenres = listOf("Ação", "Comédia", "Drama", "Terror", "Ficção Científica")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Escolha seus gêneros favoritos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(allGenres) { genre ->
                GenreItem(
                    genre = genre,
                    isSelected = selectedGenres.contains(genre),
                    onSelect = { selected ->
                        if (selected) selectedGenres.add(genre) else selectedGenres.remove(genre)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (selectedGenres.isNotEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        userPreferencesDataStore.saveFavoriteGenres(selectedGenres.toSet())
                        withContext(Dispatchers.Main) {
                            navController.navigate("home") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    }
                }
            },
            enabled = selectedGenres.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar")
        }
    }
}

@Composable
fun GenreItem(genre: String, isSelected: Boolean, onSelect: (Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(!isSelected) }
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(
                text = genre,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOnboardingScreen() {
    val fakeNavController = rememberNavController()

    OnboardingScreen(navController = fakeNavController)
}

@Preview(showBackground = true)
@Composable
fun PreviewGenreItem() {
    GenreItem(genre = "Ação", isSelected = true, onSelect = {})
}