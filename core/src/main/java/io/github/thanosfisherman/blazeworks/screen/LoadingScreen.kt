package io.github.thanosfisherman.blazeworks.screen

import io.github.thanosfisherman.blazeworks.Game
import io.github.thanosfisherman.blazeworks.logic.SoundAsset
import io.github.thanosfisherman.blazeworks.utils.AssetService
import ktx.app.KtxScreen

class LoadingScreen(private val game: Game, private val assetService: AssetService = game.assetService) : KtxScreen {
    private var done = false
    override fun show() {
        SoundAsset.entries.forEach { assetService.load(it) }
    }

    override fun render(delta: Float) {

        if (!done && assetService.update()) {
            done = true
        } else if (done) {
            game.addScreen(FireworksScreen(game))

            game.removeScreen<LoadingScreen>()
            dispose()
            game.setScreen<FireworksScreen>()
        }
    }
}