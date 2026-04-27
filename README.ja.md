# Android Architecture Showcase

[English](README.md)

Android アーキテクチャパターンを実演するサンプルアプリケーション。
Jetpack Compose、Hilt DI、Navigation（type-safe route）、マルチモジュール構成を軸に、
実務で使える設計パターンを具体的なコードで示します。

## 画面構成

```
Login ─── Login 成功 ──────────→ Home（"Welcome, {name}!" Snackbar）
  │                                PreferenceStorage デモ
  │
  ├─── Login 失敗 → ErrorDialog
  │         ├─ Cancel → 閉じる
  │         ├─ Guest Login → Home（"Guest mode" Snackbar）
  │         └─ 外タップ → 閉じる
  │
  └─── Information → InfoActivity（別 Activity、Effect + ActivityLauncher）
```

## showcase するパターン

| 画面 | モジュール | 種別 | パターン |
|------|-----------|------|----------|
| Login | feature:login | Composable | Dialog (Presenter suspend)、IndicatorState、Effect (Activity 起動) |
| Home | feature:home | Composable | Snackbar (Presenter direct)、PreferenceStorage、Navigation 引数、BackHandler |
| Info | feature:info | Activity | ActivityLauncher (DI 疎結合)、別 Activity 起動 |

### UI フィードバック 3パターン

| パターン | 用途 | 仕組み |
|----------|------|--------|
| Effect | fire-and-forget（画面遷移、Activity 起動） | `Channel<Effect>` + `receiveAsFlow()` |
| Dialog | ユーザー応答を待つ | `DialogPresenter` + `suspendCancellableCoroutine` |
| Snackbar | 即時表示 | `SnackbarPresenter.show()` 直接呼び出し |

### その他の設計パターン

- **mutableStateOf パターン** — `@Stable interface UiState` + `MutableUiState` による Compose snapshot 統合
- **Actions クラス** — コールバックを data class に集約し、Screen に一括で渡す
- **ActivityLauncher** — `core:foundation` に interface、`app` に実装。feature 間の依存逆転（DIP）
- **Convention Plugin** — `gradle-conventions/` でビルド設定を共通化（composite build、primitive + convention の 2 層）。各モジュールはプラグインを宣言するだけ
- **PreferenceKey / PreferenceStorage** — Jetpack DataStore の型安全ラッパー。キー定義が型情報を保持
- **logOnFailure / onFailureIgnoring** — `CancellationException` を安全に除外する Result 拡張

## モジュール構成

```
gradle-conventions       Convention Plugins（composite build、primitive + convention の 2 層）
app                      アプリ本体、NavGraph、ActivityLauncher 実装
├── core
│   ├── foundation       Navigation ユーティリティ、Result 拡張、ActivityLauncher interface、AdaptiveString/Image
│   ├── ui               DialogPresenter、SnackbarPresenter、IndicatorState
│   └── data             AuthRepository、PreferenceStorage（DataStore）
└── feature
    ├── login            ログイン画面
    ├── home             ホーム画面
    └── info             情報画面（別 Activity）
```

依存方向: `app → feature → core`（一方向）。feature 同士は直接依存しない。
`core:ui` は `core:foundation` に依存する。`core:foundation` は意図的に Compose-aware（`@Composable val value` アクセサを持つ `AdaptiveString`/`AdaptiveImage` を抱える）。

## Convention Plugins（`gradle-conventions/`）

ビルドは **primitive**（個別機能）と **convention**（app / library モジュール用の合成）の 2 層に分かれる:

| プラグイン | 役割 |
|-----------|------|
| convention.app | Application モジュール設定（SDK バージョン、Application ID、ktlint） |
| convention.module | Library モジュール設定（Android Library、ktlint、kotlinx-coroutines-core） |
| primitive.compose | Jetpack Compose + Material3 |
| primitive.navigation | Navigation Compose + Hilt NavCompose |
| primitive.hilt | Hilt DI + KSP |
| primitive.serialization | kotlinx-serialization-json |
| primitive.datastore | Preferences DataStore |
| primitive.logging | Timber |
| primitive.ktlint | ktlint によるコードフォーマット |
| primitive.unit-test | JUnit + MockK + Truth + Turbine |

各モジュールの `build.gradle.kts` は必要なプラグインだけを宣言する。中央集権的な god-object プラグインは置かない。

## 設計の工夫ポイント

### AdaptiveString / AdaptiveImage

リソース ID と文字列リテラル（または URL）を統一的に扱う型。
ViewModel が UI テキストや画像を提供する際に `Context` を引き回す必要がなくなる。
旧来のアプローチで使用されていた `context.getString()` の受け渡し問題を解消する。

`core:foundation/adaptive/` に配置。`core:ui` の Composable と feature の state holder の双方が、依存方向を逆転させずに参照できる。

### DialogPresenter — suspendCancellableCoroutine

`requestDialogResult()` はダイアログを表示し、ユーザーがボタンをタップするまで suspend する。
コールバック地獄に陥りがちなダイアログ処理を、直線的なコードで記述できる。

```kotlin
dialogPresenter.requestDialogResult(uiState) {
    onPositiveButtonClick = { /* ボタンタップ後の処理 */ }
}
```

### IndicatorState — ローディング状態の分離

`isLoading` を UiState から分離し、`runWithLoading` で宣言的に管理する。
`finally` ブロックでキャンセル時も確実にリセットされる。

### Result 拡張の組み合わせ

`runCatchingCancellable`, `logOnFailure`, `runWithLoading`, `onFailureIgnoring` を
組み合わせることで、非同期処理のエラーハンドリング・ログ・ローディング管理を宣言的に記述する。

```kotlin
indicatorState.runWithLoading {
    authRepository.login(email, password)  // runCatchingCancellable + logOnFailure 済み
}.onSuccess { result ->
    // 成功処理
}.onFailureIgnoring { exception ->
    // CancellationException を除外した失敗処理
}
```

### ActivityLauncher — 疎結合な Activity 起動

Activity 間の直接依存（`Intent(context, OtherActivity::class.java)`）を排除する。
`core:foundation` に interface、`app` に実装を置くことで依存逆転を実現。
feature モジュールは具体的な Activity クラスを知らずに起動できる。

### Convention Plugin — 単一責務のビルド設定

ヘルパー関数でビルド設定をまとめるのではなく、Plugin として分離することで
1 Plugin = 1 関心事を徹底。
各モジュールの `build.gradle.kts` で必要なプラグインを明示的に宣言する。

### PreferenceKey — 型安全なキーとネームスペース

`sealed class` の型付きサブクラスにより、キー自体が値の型情報を保持する。
呼び出し側が型を意識する必要がない。関連するキーは `object` でグルーピングする。

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

## 技術スタック

| カテゴリ | ライブラリ |
|----------|-----------|
| 言語 | Kotlin 2.3 |
| UI | Jetpack Compose (Material3) |
| DI | Hilt 2.57 |
| Navigation | Navigation Compose 2.9 (type-safe route) |
| 非同期 | Kotlin Coroutines + Flow |
| データ保存 | Preferences DataStore |
| ビルド | AGP 8.13、KSP、Convention Plugins |
| コード品質 | ktlint |
| テスト | JUnit、MockK、Truth、Turbine |

## ビルド

```bash
# デバッグビルド
./gradlew assembleDebug

# テスト
./gradlew testDebugUnitTest

# コードフォーマット
./gradlew ktlintFormat

# フォーマットチェック
./gradlew ktlintCheck
```

### 動作要件

- JDK 21
- minSdk 31（Android 12）

## ライセンス

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
