package io.github.thanosfisherman.blazeworks

import com.badlogic.gdx.assets.AssetManager
import io.github.thanosfisherman.blazeworks.screen.FireworksScreen
import io.github.thanosfisherman.blazeworks.screen.LoadingScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.logger

private val log = logger<Game>()

class Game : KtxGame<KtxScreen>(clearScreen = false) {
    private val assetManager = AssetManager()
    override fun create() {
        addScreen(LoadingScreen(this@Game, assetManager))
        addScreen(FireworksScreen(assetManager))
        setScreen<LoadingScreen>()
    }

    companion object {
        fun create() = Game()
    }
}

