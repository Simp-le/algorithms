package com.android.algorithms.utils.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.android.algorithms.R

val nunitoFontFamily = FontFamily(
    Font(R.font.nunito_black, FontWeight.Black),
    Font(R.font.nunito_black_italic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.nunito_extra_bold, FontWeight.ExtraBold),
    Font(R.font.nunito_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.nunito_extra_light, FontWeight.ExtraLight),
    Font(R.font.nunito_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.nunito_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.nunito_light, FontWeight.Light),
    Font(R.font.nunito_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.nunito_medium, FontWeight.Medium),
    Font(R.font.nunito_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.nunito_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.nunito_semi_bold, FontWeight.SemiBold),
    Font(R.font.nunito_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = nunitoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
    ),

    titleLarge = TextStyle(
        fontFamily = nunitoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
)