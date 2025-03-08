package io.github.thanosfisherman.blazeworks

import io.github.thanosfisherman.blazeworks.screen.LoadingScreen
import io.github.thanosfisherman.blazeworks.utils.AssetService
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.logger

private val log = logger<Game>()

class Game : KtxGame<KtxScreen>(clearScreen = false) {

    val assetService: AssetService = AssetService()
    override fun create() {
        addScreen(LoadingScreen(this@Game))
        setScreen<LoadingScreen>()
    }

    companion object {
        fun create() = Game()
    }
}

