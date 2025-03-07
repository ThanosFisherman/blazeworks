package io.github.thanosfisherman.blazeworks.logic

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import io.github.thanosfisherman.blazeworks.utils.Juniper

const val GRAVITY = 0.2f

class FireworksSystem(private val width: Float, private val height: Float) {

    private val stars = mutableListOf<Star>()
    private var rockets = mutableListOf<Rocket>()
    private var sparkles = mutableListOf<Sparkle>()
    private var exploders = ExploderFactory.exploders

    init {
        (1..200).forEach { _ ->
            stars.add(Star(width, height))
        }
    }

    fun update(delta: Float) {

        if (Juniper.random.nextFloat() < 0.03) {
            val rocket = Rocket(width)
            rockets.add(rocket)
        }

        removeElements()
        rockets.forEach { rocket ->
            rocket.update(delta)

            if (rocket.isSparkleTrail)
                sparkles.add(Sparkle(rocket.position))

            if (rocket.isExploded) {
                ExploderFactory.createExploder(rocket.type, rocket.position, rocket.hue)
            }
        }
    }

    fun draw(shapeRenderer: ShapeRenderer, delta: Float) {
        stars.forEach { star -> star.draw(shapeRenderer) }
        rockets.forEach { rocket -> rocket.draw(shapeRenderer) }
        sparkles.forEach {
            it.update(delta)
            it.draw(shapeRenderer)
        }
        exploders.forEach { exploder ->
            exploder.draw(shapeRenderer)
        }
    }

    fun justExplode(touchPos: Vector3) {
        val rocket = Rocket(width)
        rockets.add(rocket)
        rocket.justExplode(touchPos)
    }

    private fun removeElements() {
        for (i in sparkles.size - 1 downTo 0) {
            if (sparkles[i].brightness < 0) {
                sparkles.removeAt(i)
            }
        }

        for (i in rockets.size - 1 downTo 0) {
            if (rockets[i].isExploded) {
                rockets.removeAt(i)
            }
        }

        for (i in exploders.size - 1 downTo 0) {
            if (exploders[i].brightness < 0) {
                exploders.removeAt(i)
            }
        }
    }
}