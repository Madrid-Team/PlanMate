package presentation.feature.tasks

import domain.usecases.DeleteTaskUseCase
import domain.usecases.EditTaskUseCase
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import presentation.components.InputReader
import presentation.components.OutputPrinter

class EditTaskCLITest{

  private lateinit var inputReader: InputReader
  private lateinit var outputPrinter: OutputPrinter
  private lateinit var editTaskUseCase: EditTaskUseCase
  private lateinit var taskView: TaskView
  private lateinit var editTaskCLI: EditTaskCLI

  @BeforeEach
  fun setUp() {
   inputReader = mockk()
   outputPrinter = mockk(relaxed = true)
   editTaskUseCase = mockk()
   editTaskCLI = EditTaskCLI(inputReader, outputPrinter, taskView, editTaskUseCase)
  }
 }