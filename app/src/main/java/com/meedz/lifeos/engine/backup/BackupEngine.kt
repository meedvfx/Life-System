package com.meedz.lifeos.engine.backup

import android.content.Context
import android.net.Uri
import com.meedz.lifeos.data.database.AppDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: AppDatabase
) {

    suspend fun exportDataToJson(outputUri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val rootObj = JSONObject()
            // In a real implementation we would fetch from all DAOs
            val tasks = database.taskDao().getAllTasksRaw()
            
            // Convert to JSON Arrays ...
            rootObj.put("exportedAt", System.currentTimeMillis())
            rootObj.put("version", 1)
            
            val outputStream: OutputStream? = context.contentResolver.openOutputStream(outputUri)
            outputStream?.use { stream ->
                stream.write(rootObj.toString(4).toByteArray())
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun importDataFromJson(inputUri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(inputUri)
            val jsonString = inputStream?.bufferedReader().use { it?.readText() }
            
            if (jsonString != null) {
                val rootObj = JSONObject(jsonString)
                val version = rootObj.getInt("version")
                
                // Clear Database, Parse JSON arrays, and Insert into DAOs...
                database.clearAllTables()
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
