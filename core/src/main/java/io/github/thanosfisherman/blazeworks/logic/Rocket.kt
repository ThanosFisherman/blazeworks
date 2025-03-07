package io.github.thanosfisherman.blazeworks.logic

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import io.github.thanosfisherman.blazeworks.utils.Juniper
import ktx.math.vec2


class Rocket(width: Float) {

    private var _isExploded = false
    val isExploded get() = _isExploded
    val type = Juniper.random.nextInt(0, ExploderFactory.Type.entries.count()).let { ExploderFactory.Type.entries[it] }
    val position = vec2(Juniper.random.nextFloat(20f, width), 0f)
    private val speed = vec2(Juniper.random.nextFloat(-2f, 2f), Juniper.random.nextFloat(14f, 18f))
    private val gravity = vec2(0f, -0.2f)

    private val fuse = Juniper.random.nextFloat(-3f, -1f)
    val hue = Juniper.random.nextInt(10, 361)
    private val sat = Juniper.random.nextFloat(0f, 0.12f)
    private val color = Color().fromHsv(hue.toFloat(), sat, 0.45f)
    val isSparkleTrail: Boolean = Juniper.random.nextFloat() < 0.5f
    private var forceExplode = false

    fun update(delta: Float) {

        if (forceExplode) {
            _isExploded = true
            forceExplode = false
            return
        }
        if (this.fuse > this.speed.y) {
            _isExploded = true
            return
        }

        speed.add(gravity)
        position.add(speed)

    }

    fun justExplode(touchPos: Vector3) {
        position.x = touchPos.x
        position.y = touchPos.y
        forceExplode = true
    }

    fun draw(shapeRenderer: ShapeRenderer) {
        color.a = 1f
        shapeRenderer.color = color
        shapeRenderer.circle(position.x, position.y, 2f)
    }
}