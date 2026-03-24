# Android Showcase

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
- **Convention Plugin** — Shared build configuration via build-logic. Each module simply declares the plugins it needs
- **PreferenceKey / PreferenceStorage** — Type-safe wrapper for Jetpack DataStore. Key definitions carry type information
- **logOnFailure / onFailureIgnoring** — Result extensions that safely exclude `CancellationException`

## Module Structure

```
build-logic              Convention Plugins (shared build configuration)
app                      App entry point, NavGraph, ActivityLauncher implementation
├── core
│   ├── foundation       Navigation utilities, Result extensions, ActivityLauncher interface
│   ├── ui-kit           DialogPresenter, SnackbarPresenter, IndicatorState, AdaptiveString
│   └── data             AuthRepository, PreferenceStorage (DataStore)
└── feature
    ├── login            Login screen
    ├── home             Home screen
    └── info             Info screen (separate Activity)
```

Dependency direction: `app → feature → core` (unidirectional). Feature modules never depend on each other.

## Convention Plugins (build-logic)

| Plugin | Role |
|--------|------|
| AppPlugin | Application module setup (SDK versions, Application ID) |
| ModulePlugin | Common library module configuration |
| ComposePlugin | Jetpack Compose + Material3 |
| NavigationPlugin | Navigation + kotlinx-serialization + Hilt NavCompose |
| HiltPlugin | Hilt DI + KSP |
| DataStorePlugin | Preferences DataStore |
| LoggingPlugin | Timber |
| KtlintPlugin | Code formatting via ktlint |
| UnitTestPlugin | JUnit + MockK + Truth + Turbine |

## Design Highlights

### AdaptiveString / AdaptiveImage

Types that unify resource IDs and string literals (or URLs) into a single abstraction.
ViewModels can provide UI text and images without passing `Context` around —
eliminating the `context.getString()` plumbing common in older approaches.

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

TBD
