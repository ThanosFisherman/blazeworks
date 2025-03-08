package io.github.thanosfisherman.blazeworks.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import io.github.thanosfisherman.blazeworks.logic.FireworksSystem
import io.github.thanosfisherman.blazeworks.utils.FrameRate
import io.github.thanosfisherman.blazeworks.utils.Screenshot
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.graphics.center
import ktx.graphics.use
import ktx.math.vec3


class FireworksScreen : KtxScreen {

    private val width = Gdx.graphics.width.toFloat()
    private val height = Gdx.graphics.height.toFloat()
    private val screenshot = Screenshot(width.toInt(), height.toInt())
    private val fps = FrameRate()
    private val shapeRenderer = ShapeRenderer()
    private val cam = OrthographicCamera(width, height)
    private val batch = SpriteBatch()
    private lateinit var currentFbo: FrameBuffer
    private lateinit var currentRegion: TextureRegion
    private val fireworksSystem = FireworksSystem(width, height)
    private val touchPos = vec3(0f, 0f, 0f)

    var isRendered = true
    override fun show() {

        Gdx.input.inputProcessor = object : KtxInputAdapter {
            override fun keyUp(keycode: Int): Boolean {

                when (keycode) {
                    Keys.SPACE -> screenshot.takeScreenshot()
                    Keys.ESCAPE -> Gdx.app.exit()
                    Keys.D -> fps.isRendered = !fps.isRendered
                    Keys.S -> isRendered = !isRendered
                }
                return true
            }

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                if (button == Input.Buttons.LEFT) {
                    touchPos.x = screenX.toFloat()
                    touchPos.y = screenY.toFloat()
                    cam.unproject(touchPos)
                    fireworksSystem.justExplode(touchPos)
                }
                return false
            }

            override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                return true
            }

            override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
                return touchDown(screenX, screenY, pointer, Input.Buttons.LEFT)
            }
        }

        currentFbo = FrameBuffer(Pixmap.Format.RGBA8888, width.toInt(), height.toInt(), false)
//        FloatFrameBuffer(
//            width.toInt(),
//            height.toInt(),
//            false
//        )
        //FrameBuffer(Pixmap.Format.RGBA8888,width.toInt(), height.toInt(), false)
        currentRegion = TextureRegion(currentFbo.colorBufferTexture)
        currentRegion.flip(false, true)

        // After creating fboA and fboB:
        currentFbo.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        currentFbo.end();
    }

    override fun render(delta: Float) {
        super.render(delta)
        fps.update(delta)
        cam.update()
        cam.center(width, height)
        if (!isRendered) {
            return
        }

        currentFbo.begin()
        Gdx.gl.glEnable(GL20.GL_BLEND)
        // Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.projectionMatrix = cam.combined
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.setColor(0f, 0f, 0f, 0.089f)
            it.rect(0f, 0f, width, height)
        }

        fireworksSystem.update(delta)

        shapeRenderer.use(ShapeRenderer.ShapeType.Line) { shape ->
            fireworksSystem.draw(shape, delta)
        }

        currentFbo.end()
        clearScreen(0f, 0f, 0f, 1f)
        batch.use { batch ->
            batch.draw(currentRegion, 0f, 0f, width, height)
        }
        fps.render()
    }

    override fun resize(width: Int, height: Int) {
        cam.center(width.toFloat(), height.toFloat())
        cam.update()
    }

    override fun dispose() {
        super.dispose()
        shapeRenderer.dispose()
        currentFbo.dispose()
        fps.dispose()
        fireworksSystem.dispose()
    }
}