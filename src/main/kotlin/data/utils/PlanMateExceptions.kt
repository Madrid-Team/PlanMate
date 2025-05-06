package org.madrid.data.utils

open class PlanMateExceptions(message: String) : Throwable(message)

class DatabaseConnectionException(message: String) : PlanMateExceptions(message)
class CollectionAccessException(message: String) : PlanMateExceptions(message)
class DocumentNotFoundException(message: String) : Exception(message)
class OperationFailedException(message: String) : PlanMateExceptions(message)