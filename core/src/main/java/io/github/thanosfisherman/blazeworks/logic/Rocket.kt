package io.github.thanosfisherman.blazeworks.logic

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool.Poolable
import io.github.thanosfisherman.blazeworks.utils.Juniper
import ktx.math.vec2


class Rocket(private val width: Float) : Poolable {

    private var _isExploded = false
    val isExploded get() = _isExploded
    lateinit var type: ExploderFactory.Type
    lateinit var position: Vector2
    private lateinit var speed: Vector2
    private lateinit var gravity: Vector2

    private var fuse: Float = 0.0f
    var hue: Int = 0
    private var sat: Float = 0.0f
    private lateinit var color: Color
    var isSparkleTrail: Boolean = false
    private var forceExplode = false


    init {
        reset()
    }

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
        shapeRenderer.point(position.x, position.y, 0f)
    }

    override fun reset() {
        _isExploded = false

        type =
            Juniper.random.nextInt(0, ExploderFactory.Type.entries.count()).let { ExploderFactory.Type.entries[it] }
        position = vec2(Juniper.random.nextFloat(20f, width), 0f)
        speed = vec2(Juniper.random.nextFloat(-2f, 2f), Juniper.random.nextFloat(14f, 18f))
        gravity = vec2(0f, -0.2f)

        fuse = Juniper.random.nextFloat(-3f, -1f)
        hue = Juniper.random.nextInt(10, 361)
        sat = Juniper.random.nextFloat(0f, 0.12f)
        color = Color().fromHsv(hue.toFloat(), sat, 0.45f)
        isSparkleTrail = Juniper.random.nextFloat() < 0.5f
        forceExplode = false
    }
}