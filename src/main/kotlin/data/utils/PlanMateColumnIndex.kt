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


    object AuditLogColumnsIndex {
        const val AUDIT_LOG_ID = 0
        const val AUDIT_LOG_ENTITY_ID = 1
        const val AUDIT_LOG_ENTITY_NAME = 2
        const val AUDIT_LOG_ENTITY_TYPE = 3
        const val AUDIT_LOG_USER_ID = 4
        const val AUDIT_LOG_USER_NAME = 5
        const val AUDIT_LOG_CHANGE_TYPE = 6
        const val AUDIT_LOG_FIELD_NAME = 7
        const val AUDIT_LOG_OLD_VALUE = 8
        const val AUDIT_LOG_NEW_VALUE = 9
        const val AUDIT_LOG_TIME_STAMP = 10
    }
}

object UserColumnsIndex {
    const val USER_ID = 0
    const val USER_NAME = 1
    const val USER_PASSWORD_HASH = 2
    const val USER_ROLE = 3
}

