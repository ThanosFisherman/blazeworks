package io.github.thanosfisherman.blazeworks.logic

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils.E
import com.badlogic.gdx.math.MathUtils.log
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable

class Exploder : Sparkle(), Poolable {

    private lateinit var color: Color
    private lateinit var speed: Vector2
    private lateinit var type: ExploderFactory.Type

    fun init(
        rocketPos: Vector2,
        fade: Float,
        speed: Vector2,
        color: Color,
        type: ExploderFactory.Type = ExploderFactory.Type.DEFAULT,
    ) {
        super.init(rocketPos, fade)
        this.color = color
        this.speed = speed
        this.type = type
    }

    override fun draw(shapeRenderer: ShapeRenderer) {
        newXPos = rocketPos.x + log(E, this.burnTime) * 8 * this.speed.x
        newYPos = rocketPos.y + log(E, this.burnTime) * 8 * this.speed.y + this.burnTime * 0.2f
        color.a = _brightness
        shapeRenderer.color = color
        shapeRenderer.point(
            newXPos,
            newYPos,
            0f
        )

        if (type == ExploderFactory.Type.WRITER) {
            shapeRenderer.line(
                newXPos, newYPos, rocketPos.x + log(E, burnTime - 2) * 8 *
                        this.speed.x, rocketPos.y + log(E, burnTime - 2) * 8 *
                        this.speed.y + burnTime * GRAVITY
            )
        }

        this._brightness -= this.fade
        this.burnTime++
    }
}
