package domain.usecases.logs

//import domain.models.logs.EntityType
//import domain.models.logs.OperationType
//import domain.utils.convertDateToReadableDate
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
//import java.time.LocalDateTime

//class CreateLogUseCaseTest {
//    private val createLogUseCase = CreateLogUseCase()
//
//    @Test
//    fun `Should return correct log message When create task`() {
//        val timestamp =  LocalDateTime.now().convertDateIntoReadableDate()
//        val result = createLogUseCase.invoke(
//            operationType = OperationType.CREATE,
//            entityName = "Task abdo",
//            entityType = EntityType.TASK,
//            username = "admin",
//            timestamp = timestamp,
//        )
//
//        val expected = "User admin create task Task abdo  at $timestamp"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `Should return correct log message When updates project with changes`() {
//        val operationType: OperationType = OperationType.UPDATE
//        val entityName = "Abdo project"
//        val entityType: EntityType = EntityType.PROJECT
//        val username = "editor"
//        val fieldName = "email"
//        val oldValue = "old@mail.com"
//        val newValue = "new@mail.com"
//        val timestamp = LocalDateTime.now().convertDateIntoReadableDate()
//        val result = createLogUseCase.invoke(
//            operationType,
//            entityName,
//            entityType,
//            username,
//            fieldName,
//            oldValue,
//            newValue,
//            timestamp = timestamp
//        )
//
//        val expected = "User $username $operationType $entityType $entityName $fieldName from $oldValue to $newValue at $timestamp"
//
//        assertEquals(expected, result)
//    }
//
//
//
//}