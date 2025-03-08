package io.github.thanosfisherman.blazeworks.utils

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.Disposable
import io.github.thanosfisherman.blazeworks.logic.SoundAsset
import ktx.assets.getAsset
import ktx.assets.load
import ktx.log.logger

class AssetService(fileHandleResolver: FileHandleResolver = InternalFileHandleResolver()) : Disposable {

    private val soundCache = mutableMapOf<SoundAsset, Sound>()
    private val manager = AssetManager(fileHandleResolver)
    var soundVolume: Float = 0.8f
        set(value) {
            field = value.coerceIn(0f, 1f)
        }

    /**
     * Queues a [SoundAsset] for loading.
     */
    fun load(asset: SoundAsset) {
        manager.load<Sound>(asset.path)
    }

    /**
     * Queues a [SoundAsset] for unloading.
     */
    fun unload(asset: SoundAsset) {
        manager.unload(asset.path)
    }

    /**
     * Gets a loaded [SoundAsset] as [Sound].
     */
    operator fun get(asset: SoundAsset): Sound = manager.getAsset<Sound>(asset.path)

    /**
     * Updates the loading process of any queued asset.
     */
    fun update(): Boolean = manager.update(1 / 60 * 1000)

    /**
     * Returns the loading progress in percent of completion.
     */
    fun progress(): Float = manager.progress

    /**
     * Immediately finishes loading of any queued asset.
     */
    fun finishLoading() = manager.finishLoading()

    /**
     * Disposes all assets and stop all loading processes.
     */
    override fun dispose() {
        log.debug { "Disposing AssetService:\n${manager.diagnostics}" }
        manager.dispose()
    }

    companion object {
        private val log = logger<AssetService>()
    }

    fun play(soundAsset: SoundAsset, pitch: Float = 1f) {
        if (soundCache.size > 100) {
            // cache is to big -> unload current sound instances
            log.info { "Sound cache exceeds size of 100 -> clearing it" }
            soundCache.keys.forEach { unload(it) }
            finishLoading()
            soundCache.clear()
        }

        if (soundAsset !in soundCache) {
            // load sound instance into sound cache
            log.debug { "Adding $soundAsset to sound cache" }
            load(soundAsset)
            finishLoading()
            soundCache[soundAsset] = get(soundAsset)
            log.debug { "${soundCache.size} sound entries in sound cache" }
        }

        soundCache[soundAsset]?.play(soundVolume, pitch.coerceIn(0.5f, 2f), 0f)
    }

    fun pause() {
        soundCache.values.forEach { it.pause() }
    }

    fun resume() {
        soundCache.values.forEach { it.resume() }
    }
}
