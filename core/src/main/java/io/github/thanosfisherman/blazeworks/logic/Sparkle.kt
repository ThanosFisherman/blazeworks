package io.github.thanosfisherman.blazeworks.logic

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.math.Vector2
import io.github.thanosfisherman.blazeworks.utils.Juniper
import ktx.math.vec2

open class Sparkle(protected open val rocketPos: Vector2, fade: Float? = null) {

    protected val fade = fade ?: Juniper.random.nextFloat(0.1961f, 0.29f)
    protected var _brightness = 1f
    val brightness get() = _brightness
    private val hue = Juniper.random.nextInt(20, 65)
    private val sat = Juniper.random.nextFloat(0.25f, 1f)
    private val color = Color().fromHsv(hue.toFloat(), sat, _brightness)

    protected open var burnTime = 2f

    private val sparklePos = vec2()
    private val sparkleSpd = vec2()
    protected open var newXPos: Float = 0f
    protected open var newYPos: Float = 0f

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
        shapeRenderer.circle(newXPos, newYPos, 2f)

        this._brightness -= this.fade;
        this.burnTime++
    }
}