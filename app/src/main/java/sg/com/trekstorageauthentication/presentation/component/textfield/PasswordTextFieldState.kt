package sg.com.trekstorageauthentication.presentation.component.textfield

data class PasswordTextFieldState(
    val input: String = "",
    val errorLabel: String = "",
    val isError: Boolean = false
)