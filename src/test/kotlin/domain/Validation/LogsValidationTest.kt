package domain.Validation

import domain.models.logs.OperationType
import domain.usecases.logs.LogsValidation
import domain.utils.PlanMateExceptions
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LogsValidationTest {

    private lateinit var logsValidation: LogsValidation

    @BeforeEach
    fun setUp() {
        logsValidation = LogsValidation()
    }

    @Test
    fun `validateLogs should throw exception when entity name is blank`() {
        val exception = assertThrows<PlanMateExceptions> {
            logsValidation.validateLogs(
                entityName = "",
                username = "user1",
                operationType = OperationType.CREATE
            )
        }
        assertEquals("Entity name cannot be blank", exception.message)
    }

    @Test
    fun `validateLogs should throw exception when username is blank`() {
        val exception = assertThrows<PlanMateExceptions> {
            logsValidation.validateLogs(
                entityName = "Task",
                username = "",
                operationType = OperationType.CREATE
            )
        }
        assertEquals("Username cannot be blank", exception.message)
    }

    @Test
    fun `validateLogs should not throw exception for valid CREATE operation`() {
        assertDoesNotThrow {
            logsValidation.validateLogs(
                entityName = "Task",
                username = "user1",
                operationType = OperationType.CREATE
            )
        }
    }

    @Test
    fun `validateLogs should not throw exception for valid DELETE operation`() {
        assertDoesNotThrow {
            logsValidation.validateLogs(
                entityName = "Task",
                username = "user1",
                operationType = OperationType.DELETE
            )
        }
    }

    @Test
    fun `validateLogs should throw exception when field name is blank for UPDATE operation`() {
        val exception = assertThrows<PlanMateExceptions> {
            logsValidation.validateLogs(
                entityName = "Task",
                username = "user1",
                fieldName = "",
                oldValue = "old",
                newValue = "new",
                operationType = OperationType.UPDATE
            )
        }
        assertEquals("Field name must be specified for update operation", exception.message)
    }

    @Test
    fun `validateLogs should throw exception when old value is blank for UPDATE operation`() {
        val exception = assertThrows<PlanMateExceptions> {
            logsValidation.validateLogs(
                entityName = "Task",
                username = "user1",
                fieldName = "title",
                oldValue = "",
                newValue = "new",
                operationType = OperationType.UPDATE
            )
        }
        assertEquals("Old value must be specified for update operation", exception.message)
    }

    @Test
    fun `validateLogs should throw exception when new value is blank for UPDATE operation`() {
        val exception = assertThrows<PlanMateExceptions> {
            logsValidation.validateLogs(
                entityName = "Task",
                username = "user1",
                fieldName = "title",
                oldValue = "old",
                newValue = "",
                operationType = OperationType.UPDATE
            )
        }
        assertEquals("New value must be specified for update operation", exception.message)
    }

    @Test
    fun `validateLogs should throw exception when old and new values are the same for UPDATE operation`() {
        val exception = assertThrows<PlanMateExceptions> {
            logsValidation.validateLogs(
                entityName = "Task",
                username = "user1",
                fieldName = "title",
                oldValue = "same",
                newValue = "same",
                operationType = OperationType.UPDATE
            )
        }
        assertEquals("Old and new values must be different for update operation", exception.message)
    }

    @Test
    fun `validateLogs should not throw exception for valid UPDATE operation`() {
        assertDoesNotThrow {
            logsValidation.validateLogs(
                entityName = "Task",
                username = "user1",
                fieldName = "title",
                oldValue = "old",
                newValue = "new",
                operationType = OperationType.UPDATE
            )
        }
    }
}