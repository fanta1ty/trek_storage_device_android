package sg.com.trekstorageauthentication.util

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESEncryption {
    private const val secretKey = "awS2Xc4IJEffn69jQjh1Mg=="
    private const val iv = "QBUFPpiZ8GsLzW39i5ZChA=="

    private const val algorithm = "AES"
    private const val feedbackMode = "CBC"
    private const val padding = "PKCS5PADDING"

    fun encrypt(inputString: String): String {
        val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))
        val secretKeySpec = SecretKeySpec(Base64.decode(secretKey, Base64.DEFAULT), algorithm)
        val cipher = Cipher.getInstance("$algorithm/$feedbackMode/$padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val cipherText = cipher.doFinal(inputString.toByteArray())
        return String(Base64.encode(cipherText, Base64.DEFAULT))
    }

    fun decrypt(inputCipher: String): String {
        val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))
        val secretKeySpec = SecretKeySpec(Base64.decode(secretKey, Base64.DEFAULT), algorithm)
        val cipher = Cipher.getInstance("$algorithm/$feedbackMode/$padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        val plainText = cipher.doFinal(Base64.decode(inputCipher, Base64.DEFAULT))
        return String(plainText)
    }
}