package com.example.frontandroid.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.frontandroid.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val interFont = GoogleFont("Inter")

val interFontFamily = FontFamily(
    Font(
        googleFont = interFont,
        fontProvider = provider,
        weight = FontWeight.Normal // Regular weight for default usage
    ),
    Font(
        googleFont = interFont,
        fontProvider = provider,
        weight = FontWeight.SemiBold // SemiBold weight
    ),
    Font(
        googleFont = interFont,
        fontProvider = provider,
        weight = FontWeight.Bold // Bold weight
    )
)
val CustomTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    // Define other text styles with specific font weights as needed
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Define other text styles with specific font weights as needed
)
