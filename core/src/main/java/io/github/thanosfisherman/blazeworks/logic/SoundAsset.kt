package io.github.thanosfisherman.blazeworks.logic

import java.io.File


enum class SoundAsset(val path: String) {
    EXPLOSION0("sounds" + File.separator + "sounds_explosion0.mp3"),
    EXPLOSION1("sounds" + File.separator + "sounds_explosion1.mp3"),
    EXPLOSION2("sounds" + File.separator + "sounds_explosion2.mp3");
}

//fun AssetManager.load(asset: SoundAsset) = load<Sound>(asset.path)
//operator fun AssetManager.get(asset: SoundAsset) = getAsset<Sound>(asset.path)
