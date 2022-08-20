package sg.com.trekstorageauthentication.presentation.ui.common.textfield

data class PasswordTextFieldState(
    val input: String = "",
    val errorLabel: String = "",
    val isError: Boolean = false
)