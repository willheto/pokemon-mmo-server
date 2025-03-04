package com.g8e.db.migrations;

public class MigrationRunner {
    public static void runMigrations() {
        V1_init migration = new V1_init();

        migration.down();
        migration.up();

    }
}
