# Android Architecture Showcase

[日本語版はこちら](README.ja.md)

A sample application demonstrating Android architecture patterns.
Built with Jetpack Compose, Hilt DI, Navigation (type-safe routes), and a multi-module structure,
showcasing practical design patterns through concrete code.

## Screen Flow

```
Login ─── Success ────────────→ Home ("Welcome, {name}!" Snackbar)
  │                                PreferenceStorage demo
  │
  ├─── Failure → ErrorDialog
  │         ├─ Cancel → dismiss
  │         ├─ Guest Login → Home ("Guest mode" Snackbar)
  │         └─ outside tap → dismiss
  │
  └─── Information → InfoActivity (separate Activity, Effect + ActivityLauncher)
```

## Showcased Patterns

| Screen | Module | Type | Patterns |
|--------|--------|------|----------|
| Login | feature:login | Composable | Dialog (Presenter suspend), IndicatorState, Effect (Activity launch) |
| Home | feature:home | Composable | Snackbar (Presenter direct), PreferenceStorage, Navigation args, BackHandler |
| Info | feature:info | Activity | ActivityLauncher (DI decoupling), separate Activity launch |

### Three UI Feedback Patterns

| Pattern | Use Case | Mechanism |
|---------|----------|-----------|
| Effect | Fire-and-forget (navigation, Activity launch) | `Channel<Effect>` + `receiveAsFlow()` |
| Dialog | Awaiting user response | `DialogPresenter` + `suspendCancellableCoroutine` |
| Snackbar | Immediate display | `SnackbarPresenter.show()` direct call |

### Other Design Patterns

- **mutableStateOf pattern** — `@Stable interface UiState` + `MutableUiState` for Compose snapshot integration
- **Actions class** — Callbacks aggregated into a data class, passed to Screen as a single unit
- **ActivityLauncher** — Interface in `core:foundation`, implementation in `app`. Dependency Inversion between features
- **Convention Plugin** — Shared build configuration via `gradle-conventions/` (composite build, primitive + convention layers). Each module simply declares the plugins it needs
- **PreferenceKey / PreferenceStorage** — Type-safe wrapper for Jetpack DataStore. Key definitions carry type information
- **logOnFailure / onFailureIgnoring** — Result extensions that safely exclude `CancellationException`

## Module Structure

```
gradle-conventions       Convention Plugins (composite build, primitive + convention layers)
app                      App entry point, NavGraph, ActivityLauncher implementation
├── core
│   ├── foundation       Navigation utilities, Result extensions, ActivityLauncher interface, AdaptiveString/Image
│   ├── ui               DialogPresenter, SnackbarPresenter, IndicatorState
│   └── data             AuthRepository, PreferenceStorage (DataStore)
└── feature
    ├── login            Login screen
    ├── home             Home screen
    └── info             Info screen (separate Activity)
```

Dependency direction: `app → feature → core` (unidirectional). Feature modules never depend on each other.
`core:ui` depends on `core:foundation`. `core:foundation` is intentionally Compose-aware (hosts `AdaptiveString`/`AdaptiveImage` with `@Composable val value` accessors).

## Convention Plugins (`gradle-conventions/`)

The build is layered into **primitive** plugins (opt-in capabilities) and **convention** plugins (composite for app/library modules):

| Plugin | Role |
|--------|------|
| convention.app | Application module setup (SDK versions, Application ID, ktlint) |
| convention.module | Library module setup (Android Library, ktlint, kotlinx-coroutines-core) |
| primitive.compose | Jetpack Compose + Material3 |
| primitive.navigation | Navigation Compose + Hilt NavCompose |
| primitive.hilt | Hilt DI + KSP |
| primitive.serialization | kotlinx-serialization-json |
| primitive.datastore | Preferences DataStore |
| primitive.logging | Timber |
| primitive.ktlint | Code formatting via ktlint |
| primitive.unit-test | JUnit + MockK + Truth + Turbine |

Each module's `build.gradle.kts` declares only the plugins it needs. No central god-object plugin.

## Design Highlights

### AdaptiveString / AdaptiveImage

Types that unify resource IDs and string literals (or URLs) into a single abstraction.
ViewModels can provide UI text and images without passing `Context` around —
eliminating the `context.getString()` plumbing common in older approaches.

Lives in `core:foundation/adaptive/` so both `core:ui` Composables and feature state holders can reference it without crossing dependency directions.

### DialogPresenter — suspendCancellableCoroutine

`requestDialogResult()` displays a dialog and suspends until the user responds.
This turns callback-heavy dialog handling into straightforward sequential code.

```kotlin
dialogPresenter.requestDialogResult(uiState) {
    onPositiveButtonClick = { /* handle button tap */ }
}
```

### IndicatorState — Decoupled Loading State

`isLoading` is separated from UiState and managed declaratively via `runWithLoading`.
The `finally` block guarantees reset even on cancellation.

### Result Extension Composition

`runCatchingCancellable`, `logOnFailure`, `runWithLoading`, `onFailureIgnoring` —
combined to declaratively handle error handling, logging, and loading state for async operations.

```kotlin
indicatorState.runWithLoading {
    authRepository.login(email, password)  // runCatchingCancellable + logOnFailure inside
}.onSuccess { result ->
    // success handling
}.onFailureIgnoring { exception ->
    // failure handling (CancellationException excluded)
}
```

### ActivityLauncher — Decoupled Activity Launching

Eliminates direct dependencies between Activities (`Intent(context, OtherActivity::class.java)`).
Interface in `core:foundation`, implementation in `app` — achieving Dependency Inversion.
Feature modules can launch Activities without knowing the concrete class.

### Convention Plugin — Single-Responsibility Build Configuration

Rather than bundling build settings into helper functions, each Plugin encapsulates
a single concern. Modules explicitly declare only the plugins they need in `build.gradle.kts`.

### PreferenceKey — Type-Safe Keys with Namespacing

Typed subclasses of a `sealed class` ensure that each key carries its value type information.
Callers never need to think about the underlying type. Related keys are grouped under `object` namespaces.

```kotlin
sealed class PreferenceKey<T>(val key: String) {
    abstract class StringKey(key: String) : PreferenceKey<String>(key) { ... }
    abstract class BooleanKey(key: String) : PreferenceKey<Boolean>(key) { ... }

    object Login {
        data object SavedEmail : StringKey("saved_email")
        data object SaveEmailEnabled : BooleanKey("save_email_enabled")
    }
}
```

## Tech Stack

| Category | Library |
|----------|---------|
| Language | Kotlin 2.3 |
| UI | Jetpack Compose (Material3) |
| DI | Hilt 2.57 |
| Navigation | Navigation Compose 2.9 (type-safe routes) |
| Async | Kotlin Coroutines + Flow |
| Storage | Preferences DataStore |
| Build | AGP 8.13, KSP, Convention Plugins |
| Code Quality | ktlint |
| Testing | JUnit, MockK, Truth, Turbine |

## Build

```bash
# Debug build
./gradlew assembleDebug

# Unit tests
./gradlew testDebugUnitTest

# Code formatting
./gradlew ktlintFormat

# Format check
./gradlew ktlintCheck
```

### Requirements

- JDK 21
- minSdk 31 (Android 12)

## License

    Copyright 2026 44yarn

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
