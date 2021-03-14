package nl.avwie.ecs.generators

import nl.avwie.common.Generator

class IntGenerator : Generator<Int> {

    private var last: Int = -1

    override fun generate(): Int = ++last
}