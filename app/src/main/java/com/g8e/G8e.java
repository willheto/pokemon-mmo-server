package com.g8e;

import java.io.IOException;
import com.g8e.db.migrations.MigrationRunner;
import com.g8e.gameserver.GameServer;
import com.g8e.loginserver.LoginServer;
import com.g8e.loginserver.util.LoginConstants;
import com.g8e.registerServer.RegisterServer;
import com.g8e.updateserver.UpdateServer;
import com.g8e.util.Logger;

public class G8e {
    public static void main(String[] args) {
        if (args.length != 0) {
            // if arg is "migrate" then run the migration
            if (args[0].equals("migrate")) {
                Logger.printInfo("Running migration");
                MigrationRunner.runMigrations();
                return;
            }
        }

        try {
            final UpdateServer updateServer = new UpdateServer();
            final LoginServer loginServer = new LoginServer(LoginConstants.LOGIN_SERVER_PORT);
            final RegisterServer registerServer = new RegisterServer();
            final GameServer gameServer = new GameServer();
            updateServer.startServer();
            loginServer.startServer();
            registerServer.startServer();
            gameServer.startServer();
        } catch (IOException e) {
            Logger.printError("Failed to start the server" + e.getMessage());
        }
    }

}
