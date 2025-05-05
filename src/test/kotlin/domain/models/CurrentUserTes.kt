package domain.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import domain.models.authentication.User
import domain.models.logs.CurrentUser
import java.util.UUID

class CurrentUserTest {

    private val testUser =User(id = UUID.randomUUID(), username = "newUserName",
        passwordHash = "password", role = "MATE")
// adjust constructor

    @BeforeEach
    fun setup() {

        CurrentUser.setCurrentUser(testUser)
    }

    @Test
    fun `setCurrentUser should store non-null user`() {
        val user =  User(
            id = UUID.randomUUID(),
            "abdo155",
            "hash",
            "MATE"
        )
        CurrentUser.setCurrentUser(user)

        val result = CurrentUser.getCurrentUser()
        assertNotNull(result)
        assertEquals("abdo155", result?.username)
    }

    @Test
    fun `setCurrentUser should ignore null user`() {
        // First set a valid user
        CurrentUser.setCurrentUser(testUser)

        // Then set null
        CurrentUser.setCurrentUser(null)

        val result = CurrentUser.getCurrentUser()
        assertEquals("newUserName", result?.username) // original remains
    }

    @Test
    fun `getCurrentUser should return the current user`() {
        CurrentUser.setCurrentUser(testUser)
        val result = CurrentUser.getCurrentUser()

        assertNotNull(result)

    }
}
