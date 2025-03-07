package io.github.thanosfisherman.blazeworks.logic

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils.E
import com.badlogic.gdx.math.MathUtils.log
import com.badlogic.gdx.math.Vector2

class Exploder(
    rocketPos: Vector2,
    fade: Float,
    private val speed: Vector2,
    private val color: Color,
    private val type: ExploderFactory.Type = ExploderFactory.Type.DEFAULT,
) : Sparkle(rocketPos, fade) {

    override fun draw(shapeRenderer: ShapeRenderer) {
        newXPos = rocketPos.x + log(E, this.burnTime) * 8 * this.speed.x
        newYPos = rocketPos.y + log(E, this.burnTime) * 8 * this.speed.y + this.burnTime * 0.2f
        color.a = _brightness
        shapeRenderer.color = color
        shapeRenderer.circle(
            newXPos,
            newYPos,
            2f
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
