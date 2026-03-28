package com.soniclab.app.playback

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.LruCache
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicScanner @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    // Cache for performance
    private var cachedTracks: List<Track>? = null
    private val albumArtCache = LruCache<Long, String>(100) // Cache 100 album arts
    
    suspend fun scanMusicFiles(): List<Track> = withContext(Dispatchers.IO) {
        // Return cached if available
        cachedTracks?.let { return@withContext it }
        
        val tracks = mutableListOf<Track>()
        val startTime = System.currentTimeMillis()
        
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )
        
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.DURATION} > 1000"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )
        
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val pathColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val filePath = it.getString(pathColumn) ?: continue
                val title = it.getString(titleColumn) ?: "Unknown Title"
                val artist = it.getString(artistColumn) ?: "Unknown Artist"
                val album = it.getString(albumColumn) ?: "Unknown Album"
                val duration = it.getLong(durationColumn)
                val albumId = it.getLong(albumIdColumn)
                
                if (duration <= 1000) continue
                
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                
                // Try cache first, then extract
                val albumArtUri = albumArtCache.get(albumId) ?: run {
                    val art = extractEmbeddedAlbumArt(filePath) ?: ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"),
                        albumId
                    ).toString()
                    albumArtCache.put(albumId, art)
                    art
                }
                
                tracks.add(
                    Track(
                        id = id,
                        uri = contentUri.toString(),
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration,
                        albumArtUri = albumArtUri
                    )
                )
            }
        }
        
        val elapsed = System.currentTimeMillis() - startTime
        Log.d("MusicScanner", "Scanned ${tracks.size} tracks in ${elapsed}ms")
        
        cachedTracks = tracks
        tracks
    }
    
    private fun extractEmbeddedAlbumArt(filePath: String): String? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(filePath)
            val artBytes = retriever.embeddedPicture
            retriever.release()
            
            if (artBytes != null && artBytes.size < 2_000_000) { // Max 2MB
                val base64 = Base64.encodeToString(artBytes, Base64.NO_WRAP)
                "data:image/jpeg;base64,$base64"
            } else null
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun getTracksByArtist(artist: String) = withContext(Dispatchers.IO) {
        (cachedTracks ?: scanMusicFiles()).filter { it.artist == artist }
    }
    
    suspend fun getTracksByAlbum(album: String) = withContext(Dispatchers.IO) {
        (cachedTracks ?: scanMusicFiles()).filter { it.album == album }
    }
    
    suspend fun searchTracks(query: String) = withContext(Dispatchers.IO) {
        (cachedTracks ?: scanMusicFiles()).filter { 
            it.title.contains(query, true) ||
            it.artist.contains(query, true) ||
            it.album.contains(query, true)
        }
    }
    
    suspend fun getAllArtists() = withContext(Dispatchers.IO) {
        (cachedTracks ?: scanMusicFiles()).map { it.artist }.distinct().sorted()
    }
    
    suspend fun getAllAlbums() = withContext(Dispatchers.IO) {
        (cachedTracks ?: scanMusicFiles()).map { it.album }.distinct().sorted()
    }
    
    fun clearCache() {
        cachedTracks = null
        albumArtCache.evictAll()
    }
}
