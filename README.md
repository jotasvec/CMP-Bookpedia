# Bookpedia 📚

A Kotlin Multiplatform book discovery app built with Compose Multiplatform.

## Features

- Search books via Open Library
- View detailed book information
- Save favorites locally
- Cross-platform: Android, iOS, Desktop

## Tech Stack

- **UI**: Jetpack Compose Material3
- **Architecture**: Clean Architecture + MVVM
- **Networking**: Ktor Client + Kotlinx Serialization
- **DI**: Koin
- **Database**: Room
- **Image Loading**: Coil3
- **Async**: Kotlinx Coroutines

## Structure

```
composeApp/src/
├── commonMain/kotlin/
│   ├── book/
│   │   ├── domain/       # Entities, Repository interfaces
│   │   ├── data/         # DTOs, Data sources, Repository impl
│   │   └── presentation/ # ViewModels, UI screens
│   ├── core/             # Error handling, HTTP client
│   └── dl/               # Koin DI modules
├── androidMain/
├── desktopMain/
└── iosMain/
```

## API

- [Open Library](https://openlibrary.org) for book search and details