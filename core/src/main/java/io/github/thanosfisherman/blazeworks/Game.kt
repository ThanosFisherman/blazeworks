package io.github.thanosfisherman.blazeworks

import io.github.thanosfisherman.blazeworks.screen.FirstScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.logger

private val log = logger<Game>()

class Game : KtxGame<KtxScreen>() {

    override fun create() {
        addScreen(FirstScreen())
        setScreen<FirstScreen>()
    }

    companion object {
        fun create() = Game()
    }
}

