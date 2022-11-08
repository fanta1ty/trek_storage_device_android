package sg.com.trekstorageauthentication.util

import kotlin.random.Random

object RandomUtil {
    private val numericCharacters = arrayOf('0', '1','2', '3','4', '5','6', '7','8', '9')

    fun randomNumericString(characterCount: Int) : String {
        val builder = StringBuilder()
        for (i in 0 until characterCount) {
            builder.append(numericCharacters[Random.nextInt(numericCharacters.size)])
        }
        return builder.toString()
    }
}