# Fusion SDK for Android

#### Generate Revenue from Every Completed Survey, 24/7

The PureSpectrum Marketplace is the industry's easiest and most intuitive way to integrate your online audience for monetization.
Become a PureSpectrum Partner and engage your audience with online surveys.

This SDK is owned by [PureSpectrum](https://www.purespectrum.com/).

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
    - [Displaying Survey Cards](#displaying-survey-cards)
    - [Customizing Card Appearance](#customizing-card-appearance)
    - [Hiding Survey Cards](#hiding-survey-cards)
    - [SDK Shutdown](#sdk-shutdown)
- [WebView Customization](#webview-customization)
- [Support](#support)

## Prerequisites

- Android SDK 26 (Oreo) or newer
- AndroidX libraries

## Installation

### Step 1: Add the JitPack repository

Add the JitPack repository to your project's root `build.gradle` or `settings.gradle` file:

**For Gradle (Groovy DSL):**
```groovy
// In your root build.gradle file
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**For Gradle (Kotlin DSL):**
```kotlin
// In your settings.gradle.kts file
dependencyResolutionManagement {
    repositories {
        ...
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2: Add the dependency

Add the Fusion SDK dependency to your app module's `build.gradle` or `build.gradle.kts` file:

**For Gradle (Groovy DSL):**
```groovy
dependencies {
    implementation 'com.github.PureSpectrum:fusion-sdk-android:1.0.0'
}
```

**For Gradle (Kotlin DSL):**
```kotlin
dependencies {
    implementation("com.github.PureSpectrum:fusion-sdk-android:1.0.0")
}
```

Replace `1.0.0` with the latest version or commit hash.

### Step 3: Ensure required dependencies

The Fusion SDK has the following dependencies that are automatically included:

- AndroidX Core KTX
- AndroidX AppCompat
- Material Components
- Retrofit (for API calls)
- Kotlin Coroutines

If you need to manually specify these dependencies:

```kotlin
dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
```

## Usage

### Displaying Survey Cards

The Fusion SDK simplifies displaying survey opportunities in your application. It automatically fetches and displays surveys in a **horizontally scrolling list** of cards within a `ViewGroup` you provide.

To display the survey cards, call `FusionSdk.showSurveyCards()` from your Activity or Fragment, typically in a lifecycle method like `onCreate` or `onViewCreated`. The SDK will then handle fetching the data and populating the view.

In your Activity or Fragment:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main) // Ensure this layout contains your cardContainer

    cardContainer = findViewById(R.id.cardContainer)

    // Surveys will load and display automatically here
    loadSurveys()
}

private fun loadSurveys() {
    val customCardConfig = FusionCardConfiguration.Builder()
        .accentColor(Color.parseColor("#00AEEF")) // Sets CPI amount & LOI icon color
        .textColor(Color.BLACK) // Sets LOI text color
        .starColor(Color.parseColor("#FFC107")) // Gold stars
        .inactiveStarColor(Color.parseColor("#E0E0E0")) // Light gray for inactive stars
        .cardBackgroundColor(Color.WHITE)
        .cardCornerRadiusDp(12f)
        .cardElevationDp(4f)
        .cpiAmountFontSizeSp(34f)
        .cpiCurrencyColor(Color.parseColor("#00AEEF")) // For "Coins" text
        .cpiCurrencyFontSizeSp(16f)
        .loiFontSizeSp(18f)
        .showRatingStars(true)
        .loiIconVisibility(true)
        .webViewToolbarColor(Color.parseColor("#007AFF"))
        .webViewToolbarTitleColor(Color.WHITE)
        .webViewToolbarTitle("Complete Survey")
        .build()

    FusionSdk.showSurveyCards(
        context = this,
        targetView = cardContainer,
        config = customCardConfig,
        baseUrl = "http://www.company.com:81/a/b/c.html", // Example url
        accessToken = "d7yXBaH9jVtyxS6iYQd3bVmYGpOvbIkWxuwjMqY2PGBObOVGqbm1GLsqHGuoVBw7", // Example token
        respondentId = "user", // Example id
        locale = "en_US" // Example locale
        // Optional: memberId, hashedId, profileData can also be passed
    )
}

override fun onDestroy() {
    super.onDestroy()
    FusionSdk.shutdown() // Recommended to clear SDK resources
}
```

Make sure your `activity_main.xml` (or relevant layout file) includes a `ViewGroup` (like `LinearLayout` or `FrameLayout`) with the ID `R.id.cardContainer`. This container should be able to accommodate a horizontally scrolling `RecyclerView`.

Example for `cardContainer` in XML:

```xml
<LinearLayout
    android:id="@+id/cardContainer"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical" />
```

### Customizing Card Appearance

The `FusionCardConfiguration.Builder` class provides numerous methods to customize the appearance of the survey cards and the survey WebView toolbar.

Key customizable properties include:

*   **Card Styling**: `cardBackgroundColor`, `cardCornerRadiusDp`, `cardElevationDp`, `cardPaddingHorizontalDp`, `cardPaddingVerticalDp`.
*   **Text Styling**:
    *   CPI Amount (Score): `cpiAmountColor`, `cpiAmountFontSizeSp`.
    *   CPI Currency: `cpiCurrencyColor`, `cpiCurrencyFontSizeSp` (e.g., for "Coins").
    *   LOI Text: `loiTextColor`, `loiFontSizeSp`.
*   **Star Rating**: `starColor`, `inactiveStarColor`, `showRatingStars`.
*   **LOI Icon**: `loiIconVisibility`, `loiIconColor`.
*   **WebView Toolbar**: `webViewToolbarColor`, `webViewToolbarTitleColor`, `webViewToolbarTitle`.

Refer to the `FusionCardConfiguration.Builder` in the example above for usage.

### Hiding Survey Cards

If you need to programmatically remove the survey cards from the view:

```kotlin
FusionSdk.hideSurveyCards(cardContainer) // Pass the same ViewGroup used for showing cards
```

### SDK Shutdown

When the SDK is no longer needed (e.g., in `onDestroy` of your Activity), it's good practice to call `shutdown()` to clear any internal references:

```kotlin
FusionSdk.shutdown()
```

## WebView Customization

The `SurveyWebViewActivity` used to display surveys supports toolbar customization via `FusionCardConfiguration`:
*   `webViewToolbarColor`: Sets the background color of the ActionBar/Toolbar.
*   `webViewToolbarTitleColor`: Sets the title text color.
*   `webViewToolbarTitle`: Sets a custom title for the WebView screen.

These are configured through the `FusionCardConfiguration.Builder()` as shown in the usage example.

## Support

For support, please contact [support@purespectrum.com](mailto:support@purespectrum.com) or visit our [documentation](https://docs.purespectrum.com).
