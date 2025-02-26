package com.mtp.scanner.common.localDatabase

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mtp.scanner.common.GET_EVENT_DETAILS

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Perform schema changes here
        database.execSQL("ALTER TABLE $GET_EVENT_DETAILS ADD COLUMN isVirtual BOOLEAN NOT NULL DEFAULT FALSE")
    }
}