package domain.usecases.logs

import domain.models.logs.OperationType
import domain.utils.PlanMateExceptions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LogsValidationTest {

    private val logsValidation = LogsValidation()

    @Test
    fun `Should throw When entityName is blank`() {
        val exception = assertThrows(PlanMateExceptions::class.java) {
            logsValidation.validateLogs(
                entityName = "",
                username = "admin",
                operationType = OperationType.CREATE
                )
            }
            assertEquals("Entity name cannot be blank", exception.message)
        }

        @Test
        fun `Should throw When username is blank`() {
            val exception = assertThrows(PlanMateExceptions::class.java) {
                logsValidation.validateLogs(
                    entityName = "Project",
                    username = "",
                    operationType = OperationType.DELETE
                )
            }
            assertEquals("Username cannot be blank", exception.message)
        }

        @Test
        fun `Should throw When update operation is missing fieldName`() {
            val exception = assertThrows(PlanMateExceptions::class.java) {
                logsValidation.validateLogs(
                    entityName = "Task",
                    username = "user1",
                    operationType = OperationType.UPDATE,
                    fieldName = "",
                    oldValue = "old",
                    newValue = "new"
                )
            }
            assertEquals("Field name must be specified for update operation", exception.message)
        }

        @Test
        fun `Should throw When update operation old and new values are the same`() {
            val exception = assertThrows(PlanMateExceptions::class.java) {
                logsValidation.validateLogs(
                    entityName = "Task",
                    username = "user1",
                    operationType = OperationType.UPDATE,
                    fieldName = "title",
                    oldValue = "same",
                    newValue = "same"
                )
            }
            assertEquals("Old and new values must be different for update operation", exception.message)
        }

        @Test
        fun `Should pass When valid update log`() {
            assertDoesNotThrow {
                logsValidation.validateLogs(
                    entityName = "Task",
                    username = "user1",
                    operationType = OperationType.UPDATE,
                    fieldName = "status",
                    oldValue = "Pending",
                    newValue = "Done"
                )
            }
        }

}