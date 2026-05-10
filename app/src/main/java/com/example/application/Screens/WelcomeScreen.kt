package com.example.application.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
/*import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
 */
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.application.R
import com.example.application.ui.theme.Background
import com.example.application.ui.theme.Mostaza
import com.example.application.ui.theme.NegroBase
import com.example.application.ui.theme.NegroSuave
import com.example.application.ui.theme.Terracota
import com.example.application.ui.theme.TextSec

val degradado = verticalGradient(
    colors = listOf(
        NegroSuave, NegroBase
    )
)
val modBackground = Modifier
    .fillMaxSize()
    .background(brush = degradado)

@Preview(showBackground = true)
@Composable
fun ScreenB() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box(
            modifier = modBackground.padding(padding)
        ) {
            Bienvenida()
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun Bienvenida(

) {

    Box(
        /*modifier = Modifier
            .fillMaxSize()
            .background(brush = degradado)
            .padding(10.dp)

         */
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.05f))
            Image(
                painter = painterResource(id = R.drawable.taza_cafe),
                contentDescription = "null", modifier = Modifier
                    .fillMaxWidth(.8f)
                    .aspectRatio(1f)
                    .align(alignment = Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.weight(.01f))
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = Mostaza)
                    )
                    {
                        append("Conoce")
                    }
                    append(", ")
                    withStyle(
                        style = SpanStyle(color = Terracota)
                    )
                    {
                        append("comparte \n")
                    }
                    append("y demuestra tus\n habilidades")
                }, fontSize = 35.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 45.sp,
                color = Background,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(.01f))
            Text(
                "Una comunidad hecha para  \n compartir lo que más nos apasiona \n  hacer",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 25.sp,
                color = TextSec,
                modifier = Modifier.fillMaxWidth()

            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                modifier = Modifier
                    .padding(15.dp, 10.dp)
                    .fillMaxWidth()
                    .height(60.dp),


                onClick = {},
                enabled = true,
                shape = RoundedCornerShape(35),
                colors = ButtonDefaults.buttonColors(Terracota)


            ) {
                Text(
                    "Continuar",
                    fontWeight = FontWeight.Medium,
                    fontSize = 25.sp,
                    color = Background
                )

            }

        }
    }
}
