package io.github.thanosfisherman.blazeworks.logic

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable
import io.github.thanosfisherman.blazeworks.utils.Juniper
import ktx.math.vec2

open class Sparkle : Poolable {

    protected var fade: Float = 0f
    protected var _brightness: Float = 1f
    val brightness get() = _brightness
    private var hue: Int = 0
    private var sat: Float = 0f
    private lateinit var color: Color

    protected open var burnTime: Float = 2f

    private lateinit var sparklePos: Vector2
    private lateinit var sparkleSpd: Vector2
    protected open var newXPos: Float = 0f
    protected open var newYPos: Float = 0f

    protected open lateinit var rocketPos: Vector2
    private var fadeRate: Float? = null

    init {
        reset()
    }

    fun init(rocketPos: Vector2, fadeRate: Float? = null) {
        this.rocketPos = rocketPos
        this.fadeRate = fadeRate
    }

    open fun update(delta: Float) {

        val sparkleDir = Juniper.random.nextFloat(0f, PI2)
        val sparkleVel = Juniper.random.nextFloat(1f)

        with(sparkleSpd) {
            x = sparkleVel * cos(sparkleDir)
            y = sparkleVel * sin(sparkleDir)
        }
        with(sparklePos) {
            x = rocketPos.x + sparkleSpd.x
            y = rocketPos.y + sparkleSpd.y
        }
        newXPos = sparklePos.x + log(E, this.burnTime) * 8 * this.sparkleSpd.x
        newYPos = sparklePos.y + log(E, this.burnTime) * 8 * this.sparkleSpd.y + this.burnTime * GRAVITY
    }

    open fun draw(shapeRenderer: ShapeRenderer) {

        shapeRenderer.color = color
        shapeRenderer.color.a = _brightness
        shapeRenderer.point(newXPos, newYPos, 0f)

        this._brightness -= this.fade;
        this.burnTime++
    }

     final override fun reset() {
        fade = fadeRate ?: Juniper.random.nextFloat(0.1961f, 0.29f)
        _brightness = 1f

        hue = Juniper.random.nextInt(20, 65)
        sat = Juniper.random.nextFloat(0.25f, 1f)
        color = Color().fromHsv(hue.toFloat(), sat, _brightness)

        burnTime = 2f

        sparklePos = vec2()
        sparkleSpd = vec2()
        newXPos = 0f
        newYPos = 0f
    }
}