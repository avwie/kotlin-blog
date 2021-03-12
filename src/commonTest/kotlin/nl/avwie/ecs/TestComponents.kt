package nl.avwie.ecs

data class Name(val name: String): Component<Name> {
    override val key: ComponentKey<Name> = Key
    object Key : ComponentKey<Name>
}

data class Health(val health: Int): Component<Health> {
    override val key: ComponentKey<Health> = Key
    object Key : ComponentKey<Health>
}