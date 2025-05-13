# PureSpectrum Android SDK

#### Generate Revenue from Every Completed Survey, 24/7

The PureSpectrum Marketplace is the industry’s easiest and most intuitive way to integrate your online audience for monetization.
Become a PureSpectrum Partner and engage your audience with online surveys.

This SDK is owned by [PureSpectrum](https://www.purespectrum.com/).

# Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)

# Prerequisites

- Android SDK 21 (Lollipop) or newer

# Installation

1. Add it in your **root** build.gradle at the end of repositories:

```gradle
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add it in your **module** build.gradle in the dependencies section:

```gradle
dependencies {
	implementation 'com:PureSpectrum1/fusion-sdk-android.git'
}
```

# Usage

To use a recycler view with a default survey card you can get a fully prepared RecyclerView from the SDK. Add this to a parent view on your activity. The recycler view also handles click and update events for you.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val cardContainer = findViewById<LinearLayout>(R.id.cardContainer)

    FusionSdk.showSurveyCards(
        context = this,
        parentLayout = cardContainer,
        config = FusionCardConfiguration.Builder()
            .accentColor(Color.parseColor("#81D4FA"))
            .textColor(Color.BLACK)
            .starColor(Color.parseColor("#FFD700"))
            .inactiveStarColor(Color.LTGRAY)
            .backgroundColor(Color.WHITE)
            .build(),
        baseUrl = "https://url.com/",
        accessToken = "String",
        respondentId = "String",
        locale = "String"
    )
}

```

