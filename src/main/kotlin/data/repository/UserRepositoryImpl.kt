package data.repository

import data.mapper.toDomain
import data.mapper.toDto
import data.source.user.ExternalUserDataSource
import data.utils.toUserException
import domain.models.authentication.User
import domain.repository.UserRepository
import domain.utlis.UserExceptions

class UserRepositoryImpl(
    private val externalUserDataSource: ExternalUserDataSource,
) : UserRepository {
    override suspend fun deleteUser(userId: String) = executeUserOperation {
        try {
            externalUserDataSource.deleteUser(userId)
        }catch (e : Exception){
            throw e.toUserException()
        }
    }

    override suspend fun createNewUser(user: User) = executeUserOperation {
        try {
            externalUserDataSource.createNewUser(user.toDto())
        }catch (e : Exception){
            throw e.toUserException()
        }
    }


    override suspend fun getUserById(userId: String): User {
        return try {

            executeUserOperation {
                externalUserDataSource.getUserById(userId)
            }?.toDomain() ?: throw UserExceptions.UserNotFoundException()

        }catch (e:Exception){
            throw e.toUserException()
        }
    }


    override suspend fun getAllUsers(): List<User> {
        return try {
            val users = executeUserOperation {
                externalUserDataSource.getAllUsers()
            }.map { it.toDomain() }
            users.ifEmpty { throw UserExceptions.UserNotFoundException() }
        }catch (e:Exception){
            throw e.toUserException()
        }
    }

    override suspend fun getUserByName(userName: String): User {
       return try {
            executeUserOperation {
                externalUserDataSource.getUserByName(userName)
            }?.toDomain() ?: throw UserExceptions.UserNotFoundException()
        }catch (e:Exception){
            throw e.toUserException()
        }
    }



    private suspend fun <T> executeUserOperation(operation: suspend () -> T): T {
        try {
            return operation()
        } catch (e: Exception) {
            throw e.toUserException()
        }
    }
}

