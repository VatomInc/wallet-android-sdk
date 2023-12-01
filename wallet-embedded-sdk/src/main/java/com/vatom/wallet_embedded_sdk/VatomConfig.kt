package com.vatom.wallet_embedded_sdk

import kotlinx.serialization.Serializable

@Serializable
data class ScannerFeatures(
  val enabled: Boolean? = true
)
@Serializable
data class PageThemeHeader(val logo: String)

interface PageThemeIconTitle
interface PageThemeIcon
interface PageThemeMain
interface PageThemeEmptyState

@Serializable
data class PageTheme(
  val header: PageThemeHeader,
  val iconTitle: PageThemeIconTitle,
  val icon: PageThemeIcon,
  val main: PageThemeMain,
  val emptyState: PageThemeEmptyState,
  val mode: String,
  val pageTheme: String
)

@Serializable
data class PageText(val emptyState: String)

// You can define properties in the following interfaces if needed
interface PageFeaturesNotifications
interface PageFeaturesCard

@Serializable
data class PageFeaturesFooterIcon(val id: String, val src: String, val title: String)

@Serializable
data class PageFeaturesFooter(val enabled: Boolean, val icons: List<PageFeaturesFooterIcon>)

// You can define properties in the following interface if needed
interface PageFeaturesVatom

@Serializable
data class PageFeatures(
  val notifications: PageFeaturesNotifications,
  val card: PageFeaturesCard,
  val footer: PageFeaturesFooter,
  val vatom: PageFeaturesVatom
)

@Serializable
data class PageConfig(val theme: PageTheme, val text: PageText, val features: PageFeatures)


@Serializable
data class VatomConfig(
  val hideNavigation: Boolean? = true,
  val scanner: ScannerFeatures?,
  val hideTokenActions: Boolean? = false,
  val disableNewTokenToast: Boolean? = false,
  val ignoreNewTokenToast: List<String>? = null,
  val baseUrl: String? = null,
  val language: String? = null,
  val pageConfig: PageConfig? = null,
){
}



@Serializable
data class UserType(
  val bio: String = "",
  val default_business_id: String = "",
  val default_space_id: String? = null,
  val email: String = "",
  val email_verified: Boolean = false,
  val location: Location = Location(),
  val name: String = "",
  val phone_number: String = "",
  val phone_number_verified: Boolean = false,
  val picture: String = "",
  val sub: String = "",
  val expires_at: Long = 0,
  val updated_at: Long = 0,
  val wallet_address: String = "",
  val website: String = "",
  val guest: Boolean = false,
  val deferred_deeplink: String? = null
)

@Serializable
data class Location(
  val country: String = "",
  val latitude: Double = 0.0,
  val locality: String = "",
  val longitude: Double = 0.0,
  val postal_code: String = "",
  val region: String = ""
)
