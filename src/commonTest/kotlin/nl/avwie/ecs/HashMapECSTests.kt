package nl.avwie.ecs

class HashMapECSTests : ECSTests() {
    override fun createECS(): ECS {
        return HashMapECS()
    }
}