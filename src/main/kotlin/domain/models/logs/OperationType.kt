package domain.models.logs
enum class OperationType {
    CREATE,
    UPDATE,
    DELETE;

    override fun toString(): String {
        return name.lowercase()
    }
}