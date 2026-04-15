# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Development Commands

```bash
./gradlew assembleDebug              # Debug build
./gradlew testDebugUnitTest          # Unit tests
./gradlew ktlintFormat               # Auto-format (run before commit)
./gradlew ktlintCheck                # Format check (CI)
./gradlew :feature:login:assembleDebug  # Single module build
./gradlew clean assembleDebug        # Clean build (needed after Hilt DI changes)
```

Requires **JDK 21**.

## Architecture

### Module Structure

```
app                  Application module, NavGraph, ActivityLauncherImpl
core/foundation      Navigation helpers, Result extensions, ActivityLauncher interface
core/ui-kit          UI components: DialogPresenter, SnackbarPresenter, IndicatorState, AdaptiveString/Image
core/data            AuthRepository, PreferenceStorage (DataStore)
feature/login        Login screen
feature/home         Home screen
feature/info         Info screen (separate Activity)
build-logic          Convention Plugins
```

Dependencies flow: `app → feature/* → core/*`. Features depend on `core:foundation` + `core:ui-kit`, and optionally `core:data`.

### Key Patterns

- **UiState**: `@Stable interface` (read-only) + `MutableXxxUiState` (mutableStateOf) with derived properties.
- **Effect**: `Channel<XxxEffect>` + `receiveAsFlow()` for one-shot events.
- **Actions**: Data class holding lambda references, passed to Composable.
- **DialogPresenter**: `requestDialogResult()` suspends until the user responds.
- **AdaptiveString/Image**: Unify resource IDs and literals so ViewModels stay Context-free.
- **Result**: `runCatchingCancellable {}` rethrows `CancellationException`; `.logOnFailure()` logs via Timber.
- **Navigation**: Type-safe `@Serializable` routes with custom `screen<T>()` DSL (slide animations).
- **DI**: Hilt with `@HiltViewModel`. Run `./gradlew clean` after removing `@Module` or `@Binds`.

## Code Style

Configured via `.editorconfig`: max line length 130. ktlint enforced on all modules.
