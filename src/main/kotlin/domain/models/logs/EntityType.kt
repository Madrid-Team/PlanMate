package domain.models.logs


enum class EntityType {
    PROJECT,
    TASK;

    override fun toString(): String {
        return name.lowercase()
    }
}