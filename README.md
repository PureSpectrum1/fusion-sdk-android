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
- [Error Handling](#error-handling)
    - [Error Callback](#error-callback)
    - [Error Types](#error-types-fusionerror)
    - [Empty State Handling](#empty-state-handling)
    - [Logging](#logging)
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
    implementation 'com.github.PureSpectrum1:fusion-sdk-android:v1.0.9'
}
```

**For Gradle (Kotlin DSL):**
```kotlin
dependencies {
    implementation("com.github.PureSpectrum1:fusion-sdk-android:v1.0.9")
}
```

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
    accessToken = "your_token", // Example token
    respondentId = "your_id", // Example id
    locale = "en_US" // Example locale
    // Optional: memberId, hashedId, profileData can also be passed
)
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

## Error Handling

The Fusion SDK provides comprehensive error handling to help you manage various scenarios that may occur during survey loading and display.

### Error Callback

Pass a lambda function to the `onError` parameter when calling `showSurveyCards()`:

```kotlin
FusionSdk.showSurveyCards(
    context = this,
    targetView = cardContainer,
    config = customCardConfig,
    baseUrl = "YOUR_BASE_API_URL",
    accessToken = "YOUR_ACCESS_TOKEN",
    respondentId = "USER_RESPONDENT_ID",
    locale = "en_US",
    onError = { error ->
        // Handle the error based on its type
        when (error) {
            is FusionError.NetworkError -> {
                Log.e("FusionSDK", "Network Error: ${error.message}")
                // Show a network error message to the user
                Toast.makeText(this, "Network error: Check your connection", Toast.LENGTH_SHORT).show()
            }
            is FusionError.AuthenticationError -> {
                Log.e("FusionSDK", "Authentication Error: ${error.message}")
                // Handle authentication failure (e.g., prompt for login)
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
            is FusionError.ServerError -> {
                Log.e("FusionSDK", "Server Error (${error.code}): ${error.message}")
                // Show a generic server error message
                Toast.makeText(this, "Server error, please try again later", Toast.LENGTH_SHORT).show()
            }
            is FusionError.ClientError -> {
                Log.e("FusionSDK", "Client Error (${error.code}): ${error.message}")
                // Handle specific client errors if needed
                Toast.makeText(this, "Request error: ${error.code}", Toast.LENGTH_SHORT).show()
            }
            is FusionError.NoSurveysAvailable -> {
                Log.i("FusionSDK", "No surveys available: ${error.message}")
                // This is handled automatically if showEmptyState is true
                // You can add additional UI feedback here if needed
            }
            is FusionError.UnknownError -> {
                Log.e("FusionSDK", "Unknown Error: ${error.message}")
                // Show a generic error message
                Toast.makeText(this, "An unexpected error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    },
    onSuccess = { surveyCount ->
        // Handle successful loading of surveys
        Log.i("FusionSDK", "Successfully loaded $surveyCount surveys.")
        if (surveyCount > 0) {
            Toast.makeText(this, "Surveys loaded successfully!", Toast.LENGTH_SHORT).show()
        } else {
            // This case might be reached if no surveys are returned
            Log.i("FusionSDK", "No surveys available, but call was successful.")
        }
    }
)
```

### Error Types (`FusionError`)

The `onError` callback receives a `FusionError` object, which is a sealed class representing different error categories:

- `FusionError.NetworkError`: Indicates connectivity issues (e.g., no internet).
- `FusionError.AuthenticationError`: Problems with the access token or authentication (HTTP 401, 403).
- `FusionError.ServerError`: Server-side issues (HTTP 5xx responses).
- `FusionError.ClientError`: Client-side request issues (HTTP 4xx responses, excluding 401/403).
- `FusionError.NoSurveysAvailable`: The API call was successful, but no surveys matched the criteria.
- `FusionError.UnknownError`: Any other unexpected errors, including unhandled HTTP status codes or parsing issues.

### Empty State Handling

The SDK provides built-in support for displaying a customizable message when no surveys are available. This is controlled through the `FusionCardConfiguration`:

```kotlin
FusionCardConfiguration.Builder()
    // ... other configuration ...
    .showEmptyState(true) // Enable empty state display (default is true)
    .emptyStateMessage("No surveys available right now. Check back later!") // Custom message
    .emptyStateMessageColor(Color.GRAY) // Text color
    .emptyStateMessageFontSizeSp(16f) // Text size
    .build()
```

When `showEmptyState` is enabled (which is the default), the SDK will automatically display the empty state message in place of the survey cards when:
- No surveys are returned from the API
- An error occurs during survey fetching

This provides a better user experience than simply showing nothing or requiring manual handling of the empty state.

### Logging

The SDK logs important events and errors using the Android `Log` class with the tag `FusionSDK`. You can filter for this tag in Logcat during development and debugging:

```
adb logcat FusionSDK:D *:S
```

This will show only logs from the Fusion SDK at the Debug level or higher.

## Support

For support, please contact [support@purespectrum.com](mailto:support@purespectrum.com) or visit our [documentation](https://docs.purespectrum.com).
