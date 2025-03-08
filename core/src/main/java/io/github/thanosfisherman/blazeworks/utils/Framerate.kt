package io.github.thanosfisherman.blazeworks.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import ktx.assets.disposeSafely
import ktx.graphics.use


class FrameRate : Disposable {
    var isRendered = false
    private var sinceChange = 0f
    private var frameRate: Float = 0f
    private val font: BitmapFont = BitmapFont()
    private val spriteBatch = SpriteBatch()

    init {
        font.region.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
    }

    fun update(delta: Float) {
        if (!isRendered) return

        sinceChange += delta
        if (sinceChange >= 1f) {
            sinceChange = 0f
            frameRate = Gdx.graphics.framesPerSecond.toFloat()
        }
    }

    fun render(batch: SpriteBatch? = null, width: Float = 0f, height: Float = 16f) {
        if (!isRendered) return

        if (batch != null) {
            font.draw(batch, frameRate.toInt().toString() + " fps", width, height)
        } else {
            spriteBatch.use {
                font.draw(it, frameRate.toInt().toString() + " fps", width, height)
            }
        }
    }

    override fun dispose() {
        font.disposeSafely()
        spriteBatch.disposeSafely()
    }
}
