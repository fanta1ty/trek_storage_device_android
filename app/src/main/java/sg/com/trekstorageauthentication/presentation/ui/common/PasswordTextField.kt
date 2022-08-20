package sg.com.trekstorageauthentication.presentation.ui.common

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import sg.com.trekstorageauthentication.R

@Composable
fun PasswordTextField(
    password: State<String>,
    onValueChange: (String) -> Unit,
    label: String = "",
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions { }
) {
    var isShowPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password.value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = { isShowPassword = !isShowPassword }) {
                when (isShowPassword) {
                    true -> {
                        Icon(
                            painter = painterResource(R.drawable.ic_password_visible),
                            contentDescription = ""
                        )
                    }

                    false -> {
                        Icon(
                            painter = painterResource(R.drawable.ic_password_not_visible),
                            contentDescription = ""
                        )
                    }
                }
            }
        },
        visualTransformation = when (isShowPassword) {
            true -> VisualTransformation.None
            false -> PasswordVisualTransformation()
        }
    )
}