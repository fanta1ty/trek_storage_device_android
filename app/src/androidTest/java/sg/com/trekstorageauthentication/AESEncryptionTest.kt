package sg.com.trekstorageauthentication

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sg.com.trekstorageauthentication.util.AESEncryption

@RunWith(AndroidJUnit4::class)
class AESEncryptionTest {
    private val textToEncrypt = "exampleText"
    private lateinit var encryptedText: String

    @Before
    fun setUp() {
        encryptedText = AESEncryption.encrypt(textToEncrypt)
    }

    @Test
    fun encrypt() {
        val encryptText = AESEncryption.encrypt(textToEncrypt)
        Assert.assertEquals(encryptedText, encryptText)
    }

    @Test
    fun decrypt() {
        val decryptText = AESEncryption.decrypt(encryptedText)
        Assert.assertEquals(textToEncrypt, decryptText)
    }
}