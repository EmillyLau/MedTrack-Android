package com.emilly.s35678658.medtrack.presentation.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.emilly.s35678658.medtrack.R
import com.emilly.s35678658.medtrack.presentation.navigation.Routes
import com.emilly.s35678658.medtrack.ui.theme.MedBlue
import com.emilly.s35678658.medtrack.ui.theme.MedLightBlue
import com.emilly.s35678658.medtrack.ui.theme.MedTextSoft

@Composable
fun WelcomeScreen(navController: NavController) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MedLightBlue
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "MedTrack",
                fontSize = 40.sp,
                color = Color.Black,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                // AI acknowledgement: The logo used in this application was generated using ChatGPT for demonstration purposes only.
                painter = painterResource(id = R.drawable.medtrack_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 2.dp
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "MedTrack is designed to assist users in managing daily medication schedules and recording symptoms between appointments. " +
                            "It is not a substitute for professional medical advice, diagnosis, or treatment. " +
                            "Please consult a qualified healthcare professional for any medical concerns.",

                    fontSize = 13.sp,
                    color = MedTextSoft,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            //Hyperlink
            Text(
                text = "Need help? Visit Monash Health Clinic",
                color = MedBlue,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW,
                            "https://www.monash.edu.my/student-life/living-in-bandar-sunway/health-and-safety".toUri())
                        context.startActivity(intent)
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate(Routes.Login)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MedBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate(Routes.ClaimAccount)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MedBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Claim Existing Account")
            }

            Spacer(modifier = Modifier.height(12.dp))

           OutlinedButton(
                onClick = {
                    navController.navigate(Routes.SignUp)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MedBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Sign Up")
            }

            Text(
                modifier = Modifier.padding(12.dp)
                    .clickable {
                        navController.navigate(Routes.SignUp)
                    },
                color = MedBlue,
                text = "Don't have an account? Sign up now",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(35.dp),
                fontSize = 15.sp,
                text = "Designed by Emilly Lau Jie Yee (35678658)"
            )
        }
    }
}
