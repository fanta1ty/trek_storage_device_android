package sg.com.trekstorageauthentication.presentation.login.component

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f

/*
@Composable
fun AutoSizeText(
    text: String,
    maxLines: Int = 1,
    targetTextSizeHeight: TextUnit = 16.sp
) {
    var textSize by remember { mutableStateOf(targetTextSizeHeight) }

    Text(
        text,
        fontSize = textSize,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

            if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                textSize = textSize.times(TEXT_SCALE_REDUCTION_INTERVAL)
            }
        }
    )
}
 */

@Composable
fun AutoSizeText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    targetTextSizeHeight: TextUnit = 16.sp
) {
    var textSize by remember { mutableStateOf(targetTextSizeHeight) }

    Text(
        text,
        modifier = modifier,
        fontSize = textSize,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

            if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                textSize = textSize.times(TEXT_SCALE_REDUCTION_INTERVAL)
            }
        }
    )
}