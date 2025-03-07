package io.github.thanosfisherman.blazeworks.logic

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import io.github.thanosfisherman.blazeworks.utils.Juniper

data class Star(private val width: Float, private val height: Float) {

    private val x = Juniper.random.nextFloat(width)
    private val y = Juniper.random.nextFloat(height)
    private val color = Color.WHITE
    private val colorAlpha = Juniper.random.nextFloat()

    fun draw(shapeRenderer: ShapeRenderer) {
        color.a = colorAlpha
        shapeRenderer.color = color
        shapeRenderer.point(x, y, 0f)
    }
}