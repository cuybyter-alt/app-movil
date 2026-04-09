package com.canchapp_kotlin.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Configuración de la base de datos local Room
 * - Contenedor central de todas las tablas
 * - Singleton thread-safe para evitar múltiples instancias
 * - Usa fallbackToDestructiveMigration para desarrollo
 */
@Database(
    entities = [CanchaFavorita::class],
    version = 1,
    exportSchema = false
)
abstract class CanchAppDatabase : RoomDatabase() {
    abstract fun canchaFavoritaDao(): CanchaFavoritaDao
    
    companion object {
        // Variable volátil para visibilidad entre threads
        @Volatile
        private var INSTANCE: CanchAppDatabase? = null
        
        /**
         * Obtiene la instancia singleton de la BD
         * Si no existe, la crea de forma thread-safe
         */
        fun getDatabase(context: Context): CanchAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CanchAppDatabase::class.java,
                    "canchapp_database"
                )
                    // Para desarrollo: elimina y recrea la BD si cambia el schema
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
