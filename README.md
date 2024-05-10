---
category: "wallet"
platform: "wallet-sdk"
group: "android-wallet-sdk"
path: "/android/about"
title: 'About Android Wallet SDK'
order: 30100
date: "2023-11-06"
version: 2
tags: ["vAtom", "publisher"]
apipath: ""
apiurl: ""
apiverb: ""
apiauthtype: ""
user_required: false
---

The Kotlin version of Wallet SDK allows you to embed a Vatom Wallet within your own based app.

This SDK allows you to easily and efficiently incorporate digital asset management into your application, providing your users with a comprehensive experience.

https://jitpack.io/#VatomInc/kotlin-wallet-sdk

## Installation

Add it in your root build.gradle at the end of repositories:

```bash
  dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```

Add the dependency

```bash
	dependencies {
	        implementation 'com.github.VatomInc:kotlin-wallet-sdk:Tag'
	}
```

<strong>Android Permissions Configuration</strong>

Make sure you have Kotlin version greater than or equal to `1.9.10` installed.

AndroidManifest.xml Configuration </br>
Ensure the following permissions are added to your `android/app/src/main/AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.VIDEO_CAPTURE" />
<uses-permission android:name="android.permission.AUDIO_CAPTURE" />
<uses-feature android:name="android.hardware.camera" />
```

These permissions are necessary for the proper functioning of your app on Android devices.

Make sure to update the permissions as needed based on your app's requirements.

## Usage/Examples

Integrating our SDK for embedding the Vatom™ Wallet in projects is both simple and powerful. Initialize the wallet with just a few steps and empower users to effortlessly conduct transactions.

Explore code examples and guides in our documentation to unlock the full potential capabilities. Elevate your application to new heights with intuitive and secure wallet features.

```Kotlin
package com.example.myapplication

import VatomWebWalletView
import android.app.ActionBar
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.vatom.wallet_embedded_sdk.ScannerFeatures
import com.vatom.wallet_embedded_sdk.VatomConfig

class MainActivity : AppCompatActivity() {
    var vatomWebWallet: VatomWebWalletView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val vatomConig = VatomConfig(
            scanner= ScannerFeatures(
                enabled = false
            ),
            hideTokenActions  = true,
            disableNewTokenToast = false,
            language = "en",
            hideNavigation = false
        )

        var AT = ""

        vatomWebWallet = VatomWebWalletView(this, this@MainActivity, AT, vatomConig)

        val webView = findViewById<WebView>(R.id.vatomWebView)

        webView.addView(vatomWebWallet!!.webView)

    }

}
```

```XML
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:visibility="visible"
    tools:context=".MainActivity">

    <WebView
        android:id="@+id/vatomWebView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />


</androidx.constraintlayout.widget.ConstraintLayout>
```

## VatomConfig Properties

- **language**: The "language" field is used to specify the language setting for an application. The default language is English ("en"). This field allows users to customize the language in which the app's content is displayed, providing a localized and personalized experience. (e.g., "es" for Spanish)

- **hideNavigation**: Set this property to `false` if you want to show the wallet navigation. The default value is `true`.

- **hideDrawer**: Set to `false` by default, deactivates the drawer, rendering it inaccessible. Developers can customize this option to control the initial availability of the drawer, ensuring its activation or deactivation based on specific app requirements.

- **scanner**: Configure the scanner feature with the following options:

  - **enabled**: Set to `false` to hide the scanner icon on the home page; The default value is `true`.

- **disableNewTokenToast**: Disables the toast notification for new tokens. Default is false.

- **hideTokenActions**: Hides token actions UI. Default is `false`.

- **pageConfig**: Configuration for the page layout, including features like icons and footer.

  - **features**: Specifies the features for the page.
    - **icon**: Configuration for icons on the page.
      - **badges**: Enables or disables badges. Default is `true`.
      - **editions**: Enables or disables editions. Default is `true`.
      - **titles**: Enables or disables titles. Default is `true`.
    - **footer**: Configuration for the footer on the page.
      - **enabled**: Enables or disables the footer. Default is `true`.
      - **icons**: List of footer icons, each having an ID, source, and title.
    - **maxDistanceForPickup**: Max distance between the user and the object allowed so it can be picked up (in meters).

- **emptyStateImage**: Customize the image on the empty view.
- **emptyStateTitle**: Customize the title for the empty state.
- **emptyStateMessage**: Customize the message for the empty state.

These properties allow you to customize the behavior of the wallet according to your preferences.

## navigateToTab()

The `navigateToTab` function allows navigation to a specific tab in the application, providing a tab route and optional parameters.

```Kotlin
  vatomWebWallet?.navigateToTab("Connect");
```

### Parameters

- tabRoute (String): The route of the tab to navigate to.

### Example

```Kotlin
  vatomWebWallet?.navigateToTab("Home")
```

## openCommunity()

The `openCommunity` function facilitates the opening of a community within the wallet SDK. It sends a message to the wallet SDK to navigate to a specific community, and optionally to a specific room within that community.

### Parameters:

- **`communityId` (String)**: The unique identifier of the community to be opened.
- **`roomId` (String, optional)**: The unique identifier of the room within the community to navigate to.

### Usage:

```Kotlin
// Example: Open a community without specifying a room.
vatomWebWallet?.openCommunity("communityId");

// Example: Open a specific room within a community.
vatomWebWallet?.openCommunity("communityId", roomId: "roomId");
```

## listTokens()

The `listTokens` function is intended to be called by the host application to retrieve a list of tokens owned by the user within the wallet SDK.

### Usage:

```Kotlin
  vatomWebWallet?.listTokens();
```

## getToken()

The `getToken` function is intended to be called by the host application to retrieve information about a specific token in the user's wallet inventory.

### Parameters:

- **`tokenId`** (`String`): The unique identifier of the token for which information is requested.

### Returns:

```Kotlin
{
  "id":"320ca...",
  "type":"vatom",
  "parentId":".",
  "owner":"b02...",
  "author":"739f...",
  "lastOwner":"739f..."
  "modified":1697142415000,
  "shouldShowNotification":true,
  "created":1695758987000,
  ...
}
```

### Usage:

```Kotlin
  vatomWebWallet.getToken('tokenId123');
```

## getPublicToken()

The `getPublicToken` function is designed to be called by the host application to retrieve information about a public token that is not neccessarily in the user's wallet inventory (e.g. dropped on the map)

### Parameters:

- **`tokenId`** (`String`): The unique identifier of the token for which public information is requested.

### Returns:

```Kotlin
{
  "id":"320ca...",
  "type":"vatom",
  "parentId":".",
  "owner":"b02...",
  "author":"739f...",
  "lastOwner":"739f..."
  "modified":1697142415000,
  "shouldShowNotification":true,
  "created":1695758987000,
  ...
}
```

### Usage:

```Kotlin
  vatomWebWallet.getPublicToken('tokenId123');
```

## openToken()

The `openToken` function facilitates the navigation to the NFT detail screen within the wallet SDK.

### Parameters:

- **`tokenId` (String)**: The unique identifier of the NFT for which the detail screen should be opened.

### Usage:

```Kotlin
// Example: Open the NFT detail screen for a specific token.
vatomWebWallet.openToken("abc123");
```

## trashToken()

The `trashToken` function is designed to be called by the host application to initiate the removal or deletion of a specific token within the wallet SDK.

### Parameters:

- **`tokenId`** (`String`): The unique identifier of the token to be trashed or deleted.

### Usage:

```Kotlin
vatomWebWallet.trashToken('tokenId123');
```

## performAction()

The `performAction` function is intended to be called by the host application to initiate a specific action on a token within the wallet SDK.

### Parameters:

- **`tokenId`** (`String`): The unique identifier of the token on which the action will be performed.
- **`actionName`** (`String`): The name of the action to be executed on the token.
- **`payload`** (`Object?`): An optional payload containing additional data required for the specified action. It can be `null` if no additional data is needed.

### Usage:

```Kotlin
vatomWebWallet.performAction('tokenId123', 'activate', {'param': 'value'});
```

## isLoggedIn()

The `isLoggedIn` function allows the host application to check whether the user is currently logged in to the wallet SDK.

### Usage:

```Kotlin
val userLoggedIn: Boolean = vatomWebWallet.isLoggedIn()

if (userLoggedIn) {
    // The user is logged in, perform the corresponding actions.
} else {
    // The user is not logged in, handle the scenario appropriately.
}

```

## getCurrentUser()

The `getCurrentUser` function is used to retrieve the current user's data from the wallet SDK. It sends a message to the wallet SDK to fetch the user data and returns a `UserData` object.

### Returns:

```Kotlin
{
  "default_business_id": "3D...",
  "default_space_id": null,
  "email": "some@email.com",
  "email_verified": true,
  "location": {
    "country": "USA",
    "latitude": 0.0000000,
    "locality": "Sample City",
    "longitude": 0.0000000,
    "postal_code": "12345",
    "region": "Sample Region"
  },
  "name": "John Doe",
  "phone_number": "+123456789",
  "phone_number_verified": false,
  "picture": "https://example.com/profile.jpg",
  "sub": "sample-sub-id",
  "updated_at": 9876543210,
  "wallet_address": "sample-wallet-address",
  "website": "https://example.com",
  "guest": false,
  "deferred_deeplink": null
}

```

### Usage:

```Kotlin
val currentUser: User? = vatomWebWallet.getCurrentUser()

if (currentUser != null) {
    println("User Name: ${currentUser.name}")
    println("User Email: ${currentUser.email}")
} else {
    println("Error fetching user data.")
}

```

## logOut()

The `logOut` function initiates the log-out process in the wallet SDK by sending a message to trigger the log-out action.

### Usage:

```Kotlin
// Example: Initiate the log-out process.
await vatomWebWallet.logOut();
```

## linkTo()

The `linkTo` function allows navigation to a specific tab in the application, providing a url.

### Usage:

```Kotlin
 vatomWebWallet.linkTo("/map")
```