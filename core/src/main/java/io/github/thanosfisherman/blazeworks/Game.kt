package io.github.thanosfisherman.blazeworks

import io.github.thanosfisherman.blazeworks.screen.FireworksScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.logger

private val log = logger<Game>()

class Game : KtxGame<KtxScreen>(clearScreen = false) {

    override fun create() {
        addScreen(FireworksScreen())
        setScreen<FireworksScreen>()
    }

    companion object {
        fun create() = Game()
    }
}

