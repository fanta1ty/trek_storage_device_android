package sg.com.trekstorageauthentication

import org.junit.Test

import org.junit.Assert.*
import sg.com.trekstorageauthentication.util.RandomUtil
import java.util.*

class RandomUtilUnitTest {

    @Test
    fun randomUUID() {
        val additionalCharacterCount = 8
        RandomUtil.randomNumericString(additionalCharacterCount)
    }
}