package presentation.feature.admin

import presentation.feature.projects.ProjectAuditLogCLI
import presentation.feature.tasks.TaskAuditLogCLI

class AdminCLI(private val projectAuditLogCLI: ProjectAuditLogCLI, private val taskAuditLogCLI: TaskAuditLogCLI) {
    suspend fun showAdminMenu() {
        while (true) {
            println("=== Admin Tools ===")
            println("1. View Project Logs")
            println("2. View Task Logs")
            println("0. Back")
            print("Choose an option: ")

            when (readln()) {
                "1" -> projectAuditLogCLI.show()
                "2" -> taskAuditLogCLI.show()
                "0" -> return
                else -> println("Invalid option!")
            }
        }
    }

}