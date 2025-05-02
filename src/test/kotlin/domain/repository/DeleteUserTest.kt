package domain.repository

import com.google.common.truth.Truth.assertThat
import data.dto.authentication.UserDto
import data.dto.authentication.UserRoleDto
import domain.usecases.DeleteUser
import domain.utlis.UserException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest

class DeleteUserTest {

    private lateinit var deleteUser: DeleteUser
    private lateinit var userRepository: UserRepository

    @BeforeTest
    fun setUp() {
        userRepository = mockk()
        deleteUser = DeleteUser(userRepository)
    }

    @Test
    fun `Should delete user success When requested is admin and user is exist`() {
        // Given
        val admin = UserDto("1", "ADMIN_1", "PASSWORD_HASH_1", UserRoleDto.ADMIN)
        val mate = UserDto("2", "MATE_1", "PASSWORD_HASH_2", UserRoleDto.MATE)

        every { userRepository.getUser("1") } returns Result.success(admin)
        every { userRepository.getUser("2") } returns Result.success(mate)
        every { userRepository.deleteUser("2") } returns true

        // When
        val result = deleteUser.invoke(admin.id, mate.id)

        // Then
        assertThat(result).isTrue()
        verify { userRepository.deleteUser("2") }
    }

    @Test
    fun `should throw PermissionDenied exception when requested is not admin`() {
        // Given
        val mate1 = UserDto("1", "MATE_1", "PASSWORD_HASH_1", UserRoleDto.MATE)
        val mate2 = UserDto("2", "MATE_2", "PASSWORD_HASH_2", UserRoleDto.MATE)
        every { userRepository.getUser("1") } returns Result.success(mate1)
        every { userRepository.getUser("2") } returns Result.success(mate2)


        // When && Then
        assertThrows<UserException.PermissionDenied> { deleteUser.invoke(mate1.id, mate2.id) }
    }

    @Test
    fun `should throw PermissionDenied exception when admin want to delete another admin`() {
        // Given
        val admin1 = UserDto("1", "ADMIN_1", "PASSWORD_HASH_1", UserRoleDto.ADMIN)
        val admin2 = UserDto("2", "ADMIN_2", "PASSWORD_HASH_2", UserRoleDto.ADMIN)

        every { userRepository.getUser("1") } returns Result.success(admin1)
        every { userRepository.getUser("2") } returns Result.success(admin2)

        // When && Then
        assertThrows<UserException.PermissionDenied> { deleteUser.invoke(admin1.id, admin2.id) }
    }

    @Test
    fun `should throw NotFoundUser exception when user does not exist`() {
        // Given
        val admin = UserDto(id = "1", "ADMIN", "PASSWORD_HASH_1", UserRoleDto.ADMIN)

        every { userRepository.getUser("1") } returns Result.success(admin)
        every { userRepository.getUser("2") } returns Result.success(null)

        // When && Then
        assertThrows<UserException.NotFoundUser> { deleteUser.invoke(admin.id, "2") }
        verify(exactly = 0) { userRepository.deleteUser(any()) }
    }

    @Test
    fun `should throw NotFoundUser exception when admin does not exist`() {
        // Given
        val mate = UserDto(id = "1", "MATE_1", "PASSWORD_HASH_1", UserRoleDto.ADMIN)

        every { userRepository.getUser("1") } returns Result.success(mate)
        every { userRepository.getUser("2") } returns Result.success(null)

        // When && Then
        assertThrows<UserException.NotFoundUser> { deleteUser.invoke("2", mate.id) }
    }

    @Test
    fun `should throw InvalidInput exception when user is blank`() {
        // Given
        val admin = UserDto(id = "1", "ADMIN", "PASSWORD_HASH_1", UserRoleDto.ADMIN)

        every { userRepository.getUser("1") } returns Result.success(admin)
        // When && Then
        assertThrows<UserException.InvalidInput> { deleteUser.invoke(admin.id, " ") }
    }

    @Test
    fun `should throw InvalidInput exception when admin is blank`() {
        // Given
        val mate = UserDto(id = "1", "MATE_1", "PASSWORD_HASH_1", UserRoleDto.ADMIN)

        every { userRepository.getUser("1") } returns Result.success(mate)
        // When && Then
        assertThrows<UserException.InvalidInput> { deleteUser.invoke(" ", mate.id) }
    }
}