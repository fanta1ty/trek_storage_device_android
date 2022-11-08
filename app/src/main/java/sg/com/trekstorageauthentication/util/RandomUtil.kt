package sg.com.trekstorageauthentication.util

import java.util.Calendar
import kotlin.random.Random

object RandomUtil {
    private val numericCharacters = arrayOf('0', '1','2', '3','4', '5','6', '7','8', '9')

    fun randomNumericString(additionalCharacterCount: Int) : String {
        val builder = StringBuilder()
        builder.append(Calendar.getInstance().timeInMillis)
        for (i in 0 until additionalCharacterCount) {
            builder.append(numericCharacters[Random.nextInt(numericCharacters.size)])
        }
        return builder.toString()
    }
}