package nl.avwie.kotlin.composition.delegates.extensions

import nl.avwie.kotlin.composition.delegates.Dangerous
import nl.avwie.kotlin.composition.delegates.Health

val <T> T.isBerserk : Boolean where T : Health, T : Dangerous get() {
    return this.health <= 10 && !this.isDead
}

fun <T> T.berserkAttack(enemy: Health) where T : Health, T : Dangerous {
    if (this.isBerserk) {
        enemy.damage(this.damage * 2)
    } else {
        enemy.damage(this.damage)
    }
}