package io.github.thanosfisherman.blazeworks.screen

import com.badlogic.gdx.assets.AssetManager
import io.github.thanosfisherman.blazeworks.Game
import io.github.thanosfisherman.blazeworks.logic.SoundAsset
import io.github.thanosfisherman.blazeworks.logic.load
import ktx.app.KtxScreen

class LoadingScreen(
    private val game: Game,
    private val assets: AssetManager,
) : KtxScreen {

    override fun show() {
        SoundAsset.entries.forEach { assets.load(it) }
    }

    override fun render(delta: Float) {

        /**
         *  Optimize loadingPermalink
         * In order to perform loading as efficiently/fast as possible while trying to keep a certain FPS, AssetManager.update() should be called with parameters.
         * E.g. AssetManager.update(17) - In this case the AssetManager blocks for at least 17 milliseconds (only less if all assets are loaded) and loads as many assets as possible,
         * before it returns control back to the render method. Blocking for 16 or 17 milliseconds leads to ~60FPS as 1/60*1000 = 16.66667.
         * Note that it might block for longer, depending on the asset that is being loaded so don’t take the desired FPS as guaranteed.
         */
        assets.update(17)
        if (assets.isFinished) {
            game.setScreen<FireworksScreen>()
            game.removeScreen<LoadingScreen>()
            dispose()
        }
    }
}