package data.utils


object PlanMateColumnIndex {

    object ProjectColumnsIndex {
        const val PROJECT_ID = 0
        const val PROJECT_NAME = 1
        const val PROJECT_DESCRIPTION = 2
        const val PROJECT_CREATED_BY = 3
        const val PROJECT_LOGS = 4
        const val PROJECT_STATE = 5
        const val PROJECT_TASKS_STATES = 6
        const val PROJECT_STATES = 7
    }

    object TaskColumnsIndex {
        const val TASK_ID = 0
        const val PROJECT_ID = 1
        const val TASK_TITLE = 2
        const val TASK_DESCRIPTION = 3
        const val TASK_STATES = 4
        const val TASK_CREATED_BY = 5
        const val TASK_LOGS = 6
    }

}
