package com.pl.myworkoutapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import myworkoutapplication.composeapp.generated.resources.Res
import myworkoutapplication.composeapp.generated.resources.eurostile_bold_iItalic
import myworkoutapplication.composeapp.generated.resources.eurostile_extended_black_italic
import myworkoutapplication.composeapp.generated.resources.orbitron_variable
import myworkoutapplication.composeapp.generated.resources.roboto_italic_variable
import org.jetbrains.compose.resources.Font

//import com.example.resources.R

//// 1. CUSTOM FONT
//val sideLegRaisesFont = FontFamily(
//    //Font(resId = R.font.side_leg_raises_regular, weight = FontWeight.Normal),
//    //Font(resId = R.font.side_leg_raises_bold, weight = FontWeight.Bold)
//    Font(resId = Res.font.eurostile_extended_black_italic),
//)

//val eurostileExtendedBlackItalic = FontFamily(
//    Font(fontResource("fonts/EurostileExtended-BlackItalic.otf"))
//)

val EurostileExt @Composable get() = FontFamily(
    Font(
        resource = Res.font.eurostile_extended_black_italic,
        //weight = FontWeight.Bold
    )
)
val EurostileBoldItalic @Composable get() = FontFamily(
    Font(
        //resource = Res.font.eurostile_bold_italic2,//kiepskie polskie literki
        resource = Res.font.eurostile_bold_iItalic,//kiepskie polskie literki
        //weight = FontWeight.Bold
    )
)


val Orbitron @Composable get() = FontFamily(
    Font(
        //resource = Res.font.eurostile_bold_italic2,//kiepskie polskie literki
        resource = Res.font.orbitron_variable,//kiepskie polskie literki
        //weight = FontWeight.Bold
    )
)

val RobotoItalicVariable @Composable get() = FontFamily(
    Font(
        //resource = Res.font.eurostile_bold_italic2,//kiepskie polskie literki
        resource = Res.font.roboto_italic_variable,//kiepskie polskie literki
        //weight = FontWeight.Bold
    )
)

/*
// 2. TYPOGRAPHY
private val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = sideLegRaisesFont,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = sideLegRaisesFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = sideLegRaisesFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)
*/

//MARKDOWN
//    h1: TextStyle = MaterialTheme.typography.displayLarge,
//    h2: TextStyle = MaterialTheme.typography.displayMedium,
//    h3: TextStyle = MaterialTheme.typography.displaySmall,
//    h4: TextStyle = MaterialTheme.typography.headlineMedium,
//    h5: TextStyle = MaterialTheme.typography.headlineSmall,
//    h6: TextStyle = MaterialTheme.typography.titleLarge,
//    text: TextStyle = MaterialTheme.typography.bodyLarge,

private val defaultTypography = Typography()
val AppTypography: Typography @Composable get() = Typography(
    //bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = EurostileBoldItalic),
    //bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = EurostileExt),
    //bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = Orbitron),
    //bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = RobotoItalicVariable),
    //displayLarge = defaultTypography.displayLarge.copy(fontFamily = RobotoItalicVariable),
    //displaySmall = defaultTypography.displaySmall.copy(fontFamily = RobotoItalicVariable, fontSize = 22.sp),
)

// 3. COLOR SCHEMES
private val LightColors = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    secondary = Color(0xFF03DAC6),
    background = Color(0xFFF2F2F2),
    surface = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    medium = RoundedCornerShape(16.dp)
)

// 4. THEME COMPOSABLE
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = Shapes,
        content = content
    )
}