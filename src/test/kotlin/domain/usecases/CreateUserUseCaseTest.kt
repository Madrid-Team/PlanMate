package domain.usecases

import com.google.common.truth.Truth.assertThat
import data.dto.authentication.UserDto
import data.dto.authentication.UserRoleDto
import domain.repository.UserRepository
import domain.utlis.UserException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class CreateUserUseCaseTest  {
    private lateinit var userRepository: UserRepository
    private lateinit var createUserUseCase: CreateUserUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        createUserUseCase = CreateUserUseCase(userRepository)
    }


    @Test
    fun ` Should Create user successfully When User dose not exists before `() {
        //Given
         val user3 = UserDto("3", "username3", "passwordhash3"
             , UserRoleDto.ADMIN)

        //When
        every { userRepository.getUserByName("username3") } returns Result.failure(UserException.UserExist())
        every { userRepository.addUser(user3) } returns Result.success(Unit)
        val result =createUserUseCase.createUser(user3)

        //Then
        assertThat(
            result.isSuccess
        ).isTrue()

    }

    @Test
    fun `Should fail to create user when user already exists`() {
        // Given
        val existingUser = UserDto("1", "username1", "passwordhash1", UserRoleDto.ADMIN)

        every { userRepository.getUserByName("username1") } returns Result.success(existingUser)

        // When
        val result = createUserUseCase.createUser(existingUser)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(UserException.UserExist::class.java)
    }
    @Test
    fun `Should not create user When  wrong formed user from repository`() {
        // Given
        val malformedUser = UserDto(id = "", username = "username7", passwordHash = "", role = UserRoleDto.ADMIN)

        every { userRepository.getUserByName("username7") } returns Result.success(malformedUser)

        // When
        val result = createUserUseCase.createUser(malformedUser)

        // Then
        assertThat(result.isFailure).isTrue()

    }
    @Test
    fun `Should call addUser once when user does not exist`() {
        // Given
        val user = UserDto("5", "username5", "passwordhash5", UserRoleDto.MATE)

        every { userRepository.getUserByName("username5") } returns Result.success(null)
        every { userRepository.addUser(user) } returns Result.success(Unit)

        // When
        val result = createUserUseCase.createUser(user)

        // Then
        assertThat(result.isSuccess).isTrue()
        verify(exactly = 1) { userRepository.addUser(user) }
    }
}