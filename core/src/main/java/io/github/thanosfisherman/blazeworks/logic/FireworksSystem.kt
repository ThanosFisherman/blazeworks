package io.github.thanosfisherman.blazeworks.logic

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import io.github.thanosfisherman.blazeworks.utils.Juniper
import ktx.assets.pool
import ktx.collections.gdxArrayOf

const val GRAVITY = 0.2f

class FireworksSystem(private val width: Float, private val height: Float) {

    private val stars = gdxArrayOf<Star>(false, 200)
    private var rockets = gdxArrayOf<Rocket>(false, 400)
    private var sparkles = gdxArrayOf<Sparkle>(false, 400)
    private var exploders = ExploderFactory.exploders

    private val rocketPool: Pool<Rocket> = pool(400) { Rocket(width) }
    private val sparklePool: Pool<Sparkle> = pool(400) { Sparkle() }
    private val exploderPool: Pool<Exploder> = ExploderFactory.exploderPool

    init {
        (1..200).forEach { _ ->
            stars.add(Star(width, height))
        }
    }

    fun update(delta: Float) {

        if (Juniper.random.nextFloat() < 0.03) {
            val rocket = rocketPool.obtain()
            rockets.add(rocket)
        }

        removeElements()
        rockets.forEach { rocket ->
            rocket.update(delta)

            if (rocket.isSparkleTrail) {
                val sparkle = sparklePool.obtain().also { it.init(rocket.position) }
                sparkles.add(sparkle)
            }

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
        val rocket = rocketPool.obtain()
        rockets.add(rocket)
        rocket.justExplode(touchPos)
    }

    private fun removeElements() {
        for (i in sparkles.size - 1 downTo 0) {
            if (sparkles[i].brightness < 0) {
                val sparkle = sparkles.removeIndex(i)
                sparklePool.free(sparkle)
            }
        }

        for (i in rockets.size - 1 downTo 0) {
            if (rockets[i].isExploded) {
                val rocket = rockets.removeIndex(i)
                rocketPool.free(rocket)
            }
        }

        for (i in exploders.size - 1 downTo 0) {
            if (exploders[i].brightness < 0) {
                val exploder = exploders.removeIndex(i)
                exploderPool.free(exploder)
            }
        }
    }

    fun dispose() {
        exploders.clear()
        sparkles.clear()
        rockets.clear()
        rocketPool.clear()
        sparklePool.clear()
        exploderPool.clear()
    }
}