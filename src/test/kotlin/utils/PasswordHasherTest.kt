package utils

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class PasswordHasherTest {


    @Test
    fun `hashed password is not equal plain password`(){

        //given
        val plainPassword = "theChance"

        //when
        val hashed = PasswordHasher.hash(plainPassword)

        //then
        assertNotEquals(plainPassword, hashed)


    }

    @Test
    fun `hashed password returns true to the correct password`(){

        //Given
        val password = "theChance"

        //when
        val hashed = PasswordHasher.hash(password)

        //then
        assertTrue(PasswordHasher.verify(password, hashed))


    }



    @Test
    fun `verify should return false for incorrect password`() {

        //Given
        val password = "theChance"

        //when
        val hashed = PasswordHasher.hash(password)

        //then
        assertFalse(PasswordHasher.verify("theChane", hashed))
    }





}