package sg.com.trekstorageauthentication.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import sg.com.trekstorageauthentication.R

@Composable
fun RegisterScreen(navController: NavHostController) {
    val textValue = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .width(44.dp)
                .height(44.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp)
            )
        }

        Image(
            painter = painterResource(R.drawable.logo_register),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            stringResource(R.string.set_up),
            style = MaterialTheme.typography.h2
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = textValue.value,
                onValueChange = { newText -> textValue.value = newText },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "") },
                label = { Text(stringResource(R.string.password)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) { Text(stringResource(R.string.save)) }
        }
    }
}