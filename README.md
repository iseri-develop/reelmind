
# 🎬 ReelMind

**ReelMind** é um aplicativo Android desenvolvido em Kotlin que ajuda os usuários a decidirem o que assistir, oferecendo sugestões de filmes e séries por meio de uma roleta interativa. O app consome dados da API do IMDb para exibir títulos, sinopses e imagens atualizadas, promovendo uma experiência dinâmica, intuitiva e divertida.

## 🚀 Funcionalidades

- 🎡 Roleta de recomendações com animação fluida
- 🔍 Exploração de filmes e séries populares
- 📄 Visualização de detalhes como título, sinopse e imagem
- 👤 Tela de perfil com informações básicas do usuário
- 🔄 Navegação fluida com Bottom Navigation

## 🛠️ Tecnologias e Ferramentas

| Categoria         | Tecnologia                          | Motivo da Escolha |
|------------------|--------------------------------------|-------------------|
| Linguagem         | **Kotlin**                          | Concisa, segura e nativa para Android |
| UI                | **Jetpack Compose**                 | UI moderna, declarativa e reativa |
| Arquitetura       | **MVVM (Model-View-ViewModel)**     | Separação clara de responsabilidades |
| Reatividade       | **StateFlow + Coroutines**          | Comunicação eficiente entre ViewModel e UI |
| DI                | **Koin**                            | Injeção de dependência simples e leve |
| Testes            | **JUnit + MockK**                   | Testes unitários confiáveis e legíveis |
| API               | **IMDb API**                        | Fonte confiável de dados de filmes/séries |
| Navegação         | **Navigation Component**            | Controle centralizado e seguro de rotas |

## 📦 Estrutura de Pastas (Simplificada)

```
com.reelmind
│
├── ui               # Telas com Jetpack Compose
├── viewmodel        # ViewModels com StateFlow
├── di               # Módulos Koin
├── data             # Repositórios e acesso à API
├── model            # Modelos de dados
└── utils            # Funções e helpers
```

## ✅ Boas Práticas Aplicadas

- Uso de `sealed classes` para representar estados de UI (Loading, Success, Error)
- Separação entre camadas de dados, domínio e apresentação
- ViewModels desacopladas e testáveis
- Programação assíncrona com coroutines e Dispatchers adequados (`IO`, `Main`)
- Coleta de estados reativos na UI com `collectAsState`

## 📱 Requisitos

- Android Studio Flamingo ou superior
- SDK 33+
- Dispositivo/emulador com Android 8.0 (API 26) ou superior

## 📌 Próximas Melhorias

- Tela de favoritos
- Filtro por gênero
- Armazenamento local com Room
- Dark mode
- Testes unitários foram aplicados na camada de ViewModel com uso de:
- `JUnit` para estrutura de testes
- `MockK` para mock de dependências
- `Turbine` (opcional) para testar fluxos do `StateFlow`

## 👨‍💻 Autor

Desenvolvido por Igor Isaeri — 💬 Focado em desenvolvimento Android com paixão por boas práticas e experiência do usuário.
