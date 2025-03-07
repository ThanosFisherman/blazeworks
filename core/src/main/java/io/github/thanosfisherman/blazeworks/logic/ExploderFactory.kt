package io.github.thanosfisherman.blazeworks.logic

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import io.github.thanosfisherman.blazeworks.utils.Juniper
import ktx.assets.pool
import ktx.collections.gdxArrayOf
import ktx.math.vec2

object ExploderFactory {
    val exploders = gdxArrayOf<Exploder>(false, 2000)
    val exploderPool: Pool<Exploder> = pool(2000) { Exploder() }

    enum class Type {
        WRITER, SIMPLE, SPARKLER, SPLIT, MEGA, PENT, COMET, BURST, DOUBLE, DEFAULT;
    }

    fun createExploder(type: Type, rocketPos: Vector2, rocketHue: Int) {

        when (type) {
            Type.SPLIT -> {
                drawStars(rocketHue, rocketPos, 30, 3f, 8f, 5, 6, Type.WRITER)
                drawStars(rocketHue, rocketPos, 20, 3f, 8f, 3, 5, Type.SPARKLER)
            }

            Type.BURST -> {
                drawStars(rocketHue, rocketPos, 80, 0f, 6f, 7, 8, Type.SPARKLER)
            }

            Type.DOUBLE -> {
                drawStars(rocketHue, rocketPos, 90, 3f, 5f, 3, 4, Type.PENT)
                drawStars(rocketHue, rocketPos, 90, 0.5f, 2f, 5, 6, Type.WRITER)
            }

            Type.MEGA -> {
                drawStars(rocketHue, rocketPos, 600, 0f, 8f, 3, 4, Type.DEFAULT)
            }

            Type.WRITER -> {
                drawStars(rocketHue, rocketPos, 200, 0f, 5f, 1, 3, Type.WRITER)
            }

            Type.SIMPLE -> {
                drawStars(rocketHue, rocketPos, 500, 0f, 5f, 2, 3, Type.DEFAULT)
            }

            Type.PENT -> {
                val baseDir = Juniper.random.nextFloat(0f, PI2)
                drawStars(
                    rocketHue,
                    rocketPos,
                    20,
                    3f,
                    8f,
                    3,
                    8,
                    Type.WRITER,
                    baseDir + (2 / 5) * PI,
                    66f
                )
                drawStars(
                    rocketHue,
                    rocketPos,
                    20,
                    3f,
                    8f,
                    3,
                    6,
                    Type.WRITER,
                    baseDir + (4 / 5) * PI,
                    66f
                )
                drawStars(
                    rocketHue,
                    rocketPos,
                    60,
                    3f,
                    5f,
                    2,
                    8,
                    Type.WRITER,
                    baseDir + (6 / 5) * PI,
                    66f
                )
                drawStars(
                    rocketHue,
                    rocketPos,
                    30,
                    3f,
                    5f,
                    3,
                    4,
                    Type.WRITER,
                    baseDir + (8 / 5) * PI,
                    66f
                )
                drawStars(
                    rocketHue,
                    rocketPos,
                    20,
                    3f,
                    5f,
                    2,
                    3,
                    Type.WRITER,
                    baseDir + (10 / 5) * PI,
                    6f
                )
            }

            Type.COMET -> {
                val baseDir = Juniper.random.nextFloat(0f, PI2)
                drawStars(
                    rocketHue,
                    rocketPos,
                    10,
                    3f,
                    7f,
                    2,
                    8,
                    Type.SPARKLER,
                    baseDir + (2 / 3) * PI,
                    128f
                )
                drawStars(
                    rocketHue,
                    rocketPos,
                    10,
                    3f,
                    7f,
                    1,
                    8,
                    Type.SPARKLER,
                    baseDir + (4 / 3) * PI,
                    128f
                )
                drawStars(
                    rocketHue,
                    rocketPos,
                    10,
                    3f,
                    7f,
                    2,
                    6,
                    Type.SPARKLER,
                    baseDir + (6 / 3) * PI,
                    128f
                )
                drawStars(rocketHue, rocketPos, 200, 0f, 8f, 4, 8, Type.WRITER)
            }

            Type.SPARKLER -> {
                val baseDir = Juniper.random.nextFloat(0f, PI2)
                val dir = Juniper.random.nextFloat(0f, PI2)
                val vel = Juniper.random.nextFloat()
                val starSpd = vec2(vel * cos(dir), vel * sin(dir))
                val newPos = vec2(rocketPos.x + starSpd.x, rocketPos.y + starSpd.y)
                val fade = Juniper.random.nextFloat(0.1961f, 0.2941f)
                val hue = Juniper.random.nextFloat(20f, 40f)
                val sat = Juniper.random.nextFloat(0.1961f, 0.1176f)
                val color = Color().fromHsv(hue, sat, 1f)
                val exploder = exploderPool.obtain().also { it.init(newPos, fade, starSpd, color) }
                exploders.add(exploder)
                drawStars(
                    rocketHue,
                    rocketPos,
                    20,
                    3f,
                    7f,
                    3,
                    5,
                    Type.SPARKLER,
                    baseDir + (6 / 3) * PI,
                    128f
                )
                drawStars(rocketHue, rocketPos, 400, 0f, 8f, 3, 4, Type.WRITER)
            }

            Type.DEFAULT -> {
                drawStars(rocketHue, rocketPos, 400, 2f, 8f, 3, 4, Type.SIMPLE)
            }
        }
    }

    private fun drawStars(
        rocketHue: Int,
        position: Vector2,
        numStars: Int,
        velMin: Float,
        velMax: Float,
        fadeMin: Int,
        fadeMax: Int,
        type: Type,
        baseDir: Float? = null,
        angle: Float = 180f,
    ) {

        for (i in 1..numStars) {
            var dir = Juniper.random.nextFloat(0f, PI2)
            baseDir?.let { dir = it + Juniper.random.nextFloat(0f, PI / angle) }
            val vel = Juniper.random.nextFloat(velMin, velMax)
            val starSpd = vec2(
                Juniper.random.nextFloat() + vel * cos(dir),
                Juniper.random.nextFloat() + vel * sin(dir)
            )
            val hue = rocketHue + Juniper.random.nextInt(-10, 10)
            val sat = Juniper.random.nextInt(98, 100) / 255f
            val fade: Float = Juniper.random.nextInt(fadeMin, fadeMax) / 255f
            val color = Color().fromHsv(hue.toFloat(), sat, 1f)
            val exploder = exploderPool.obtain().also { it.init(position, fade, starSpd, color, type) }
            exploders.add(exploder)
        }
    }
}
