package io.github.thanosfisherman.blazeworks.utils

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.github.tommyettinger.random.ChopRandom
import com.github.tommyettinger.random.EnhancedRandom
import com.github.tommyettinger.random.PouchRandom

object Juniper {

    val random: EnhancedRandom =
        if (Gdx.app.type == Application.ApplicationType.WebGL) {
            ChopRandom()
        } else {
            PouchRandom()
        }
}
