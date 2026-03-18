package com.soundstage.app.data

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import android.net.Uri

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val uri: Uri,
    val duration: Long
)

class MusicRepository(private val contentResolver: ContentResolver) {

    fun fetchSongs(): List<Song> {
        val songs = mutableListOf<Song>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )

        val cursor = contentResolver.query(
            uri, projection, "${MediaStore.Audio.Media.IS_MUSIC} != 0", null, null
        )

        cursor?.use {
            val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val id = it.getLong(idCol)
                songs.add(Song(
                    id = id,
                    title = it.getString(titleCol),
                    artist = it.getString(artistCol),
                    duration = it.getLong(durCol),
                    uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                ))
            }
        }
        return songs
    }
}
