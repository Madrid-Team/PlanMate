package presentation.feature.tasks

import domain.usecases.CreateTaskUseCase
import domain.usecases.project.CreateProjectUseCase
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter
import presentation.feature.projects.CreateProjectCLI

class CreateTaskCLITest(){
    private val inputReader = mockk<InputReader>()
    private val outputPrinter = mockk<OutputPrinter>(relaxed = true)
    private val useCase = mockk<CreateTaskUseCase>()
    private val taskView = mockk<TaskView>()
    private lateinit var cli : CreateTaskCLI

    @BeforeEach
    fun setUp() {
        cli = CreateTaskCLI(inputReader, outputPrinter,taskView, useCase)
    }
}