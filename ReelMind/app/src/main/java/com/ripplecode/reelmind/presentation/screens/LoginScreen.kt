package com.ripplecode.reelmind.presentation.screens

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.ripplecode.reelmind.presentation.viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    authViewModel: AuthViewModel = koinViewModel()
) {
    var loginError by remember { mutableStateOf<String?>(null) }

    // Launcher para iniciar o fluxo de login do Google
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    authViewModel.firebaseAuthWithGoogle(idToken) { success ->
                        if (success) onLoginSuccess() else loginError = "Falha ao autenticar"
                    }
                } else {
                    loginError = "Token inválido"
                }
            } catch (e: ApiException) {
                loginError = "Erro ao fazer login: ${e.message}"
            }
        } else {
            loginError = "Login cancelado"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Faça login para continuar", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.signInWithGoogle { intentSender, error ->
                if (intentSender != null) {
                    launcher.launch(IntentSenderRequest.Builder(intentSender).build())
                } else {
                    loginError = error?.message ?: "Erro desconhecido"
                }
            }
        }) {
            Text("Entrar com Google")
        }

        loginError?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
@Preview
fun LoginPreview() {
    LoginScreen({})
}