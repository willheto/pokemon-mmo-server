package com.g8e.gameserver.models.pokemon;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.g8e.db.DatabaseConnection;
import com.g8e.db.models.DBPokemon;
import com.g8e.gameserver.managers.PokemonMovesManager;
import com.g8e.gameserver.managers.PokemonsManager;
import com.g8e.gameserver.models.objects.Item;
import com.g8e.util.Logger;
import com.google.gson.Gson;

public class Pokemon {

    private transient final Gson gson = new Gson();

    private Integer pokemonID; // db id
    private int id;
    private int xp;
    private int hp;
    private int maxHp;
    private int level;
    private int[] moves = new int[4];

    private int hpIv = 0;
    private int atkIv = 0;
    private int defIv = 0;
    private int spAtkIv = 0;
    private int spDefIv = 0;
    private int spdIv = 0;

    private int hpEv = 0;
    private int atkEv = 0;
    private int defEv = 0;
    private int spAtkEv = 0;
    private int spDefEv = 0;
    private int spdEv = 0;

    private transient Item heldItem = null; // Item held by the Pokémon
    private boolean isOutsider = false; // Flag to indicate if the Pokémon is an outsider

    // Add evasion modifier variable
    private int evasionModifier = 0; // This will be affected by status changes like Minimize

    private transient PokemonMovesManager pokemonMovesManager = new PokemonMovesManager();

    public Pokemon(DBPokemon dbPokemon) {
        this.pokemonID = dbPokemon.getPokemonID();
        this.id = dbPokemon.getID();
        this.xp = dbPokemon.getXP();
        this.hp = dbPokemon.getHP();
        this.maxHp = dbPokemon.getHP();
        this.level = dbPokemon.getLevel();
        this.moves = gson.fromJson(dbPokemon.getMoves(), int[].class);
        this.hpIv = dbPokemon.getHPIV();
        this.atkIv = dbPokemon.getAtkIV();
        this.defIv = dbPokemon.getDefIV();
        this.spAtkIv = dbPokemon.getSpAtkIV();
        this.spDefIv = dbPokemon.getSpDefIV();
        this.spdIv = dbPokemon.getSpdIV();
        this.hpEv = dbPokemon.getHPEV();
        this.atkEv = dbPokemon.getAtkEV();
        this.defEv = dbPokemon.getDefEV();
        this.spAtkEv = dbPokemon.getSpAtkEV();
        this.spDefEv = dbPokemon.getSpDefEV();
        this.spdEv = dbPokemon.getSpdEV();
        this.isOutsider = dbPokemon.getIsOutsider();
        this.evasionModifier = dbPokemon.getEvasionModifier();
        PokemonsManager pokemonsManager = new PokemonsManager();

        PokemonData data = pokemonsManager.getPokemonDataByIndex(id);
        int baseHp = data.getHp();
        this.maxHp = calculateHP(baseHp, this.hpIv, this.hpEv);

    }

    public Pokemon(int id, int level) {
        PokemonsManager pokemonsManager = new PokemonsManager();
        PokemonData data = pokemonsManager.getPokemonDataByIndex(id);

        this.id = id;
        this.level = level;

        int baseHp = data.getHp();

        // random ivs from 1 to 15
        this.hpIv = (int) (Math.random() * 15) + 1;
        this.atkIv = (int) (Math.random() * 15) + 1;
        this.defIv = (int) (Math.random() * 15) + 1;
        this.spAtkIv = (int) (Math.random() * 15) + 1;
        this.spDefIv = (int) (Math.random() * 15) + 1;
        this.spdIv = (int) (Math.random() * 15) + 1;

        // Initialize EVs (assuming they are 0 at the start, or set them accordingly)
        this.hpEv = 0; // EVs are typically set to 0 by default
        this.atkEv = 0;
        this.defEv = 0;
        this.spAtkEv = 0;
        this.spDefEv = 0;
        this.spdEv = 0;

        // Calculate HP at level 1 based on IVs, EVs, and Base HP
        this.hp = calculateHP(baseHp, this.hpIv, this.hpEv);
        this.maxHp = this.hp;

        List<PokemonMoveLearnCurve> movesLearned = data.getMovesLearned();
        // loops through the list reverse, and set up to 4 moves pokemon is able to
        // learn on its level
        for (int i = 0; i < movesLearned.size(); i++) {
            PokemonMoveLearnCurve moveLearned = movesLearned.get(movesLearned.size() - 1 - i);
            int moveID = pokemonMovesManager.getMoveIdByName(moveLearned.getName());
            if (moveLearned.getLevel() <= level) {
                // find first empty slot
                for (int j = 0; j < 4; j++) {
                    if (moves[j] == 0) {
                        moves[j] = moveID;
                        break;
                    }
                }

                // if no free slots, start replacing moves. first replce first move, then
                // second...
                if (moves[3] != 0) {
                    moves[0] = moves[1];
                    moves[1] = moves[2];
                    moves[2] = moves[3];
                    moves[3] = moveID;
                }

            }
        }

    }

    public void saveUsersNewPokemon(int playerID) {
        String SQL_INSERT_POKEMON = "INSERT INTO pokemons "
                + "(player_id, id, xp, hp, level, moves, hpIv, atkIv, defIv, spAtkIv, spDefIv, spdIv, hpEv, atkEv, defEv, spAtkEv, spDefEv, spdEv, heldItem, isOutsider, evasionModifier) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.createDatabaseConnection();
                var statement = connection.prepareStatement(SQL_INSERT_POKEMON)) {
            statement.setInt(1, playerID);
            statement.setInt(2, id);
            statement.setInt(3, xp);
            statement.setInt(4, hp);
            statement.setInt(5, level);
            statement.setString(6, gson.toJson(moves));
            statement.setInt(7, hpIv);
            statement.setInt(8, atkIv);
            statement.setInt(9, defIv);
            statement.setInt(10, spAtkIv);
            statement.setInt(11, spDefIv);
            statement.setInt(12, spdIv);
            statement.setInt(13, hpEv);
            statement.setInt(14, atkEv);
            statement.setInt(15, defEv);
            statement.setInt(16, spAtkEv);
            statement.setInt(17, spDefEv);
            statement.setInt(18, spdEv);
            statement.setInt(19, heldItem != null ? heldItem.getItemID() : 0);
            statement.setBoolean(20, isOutsider);
            statement.setInt(21, evasionModifier);

            statement.executeUpdate();

            // fetch the pokemon id
            String SQL_FETCH_POKEMON_ID = "SELECT pokemon_id FROM pokemons WHERE player_id = ? AND id = ? AND level = ?";
            var fetchStatement = connection.prepareStatement(SQL_FETCH_POKEMON_ID);
            fetchStatement.setInt(1, playerID);
            fetchStatement.setInt(2, id);
            fetchStatement.setInt(3, level);

            var result = fetchStatement.executeQuery();

            if (result.next()) {
                pokemonID = result.getInt("pokemon_id");
            }

        } catch (SQLException e) {
            Logger.printError("Error saving new Pokemon: " + e.getMessage());
        }

    }

    public void savePokemonHp() {
        String SQL_UPDATE_POKEMON_HP = "UPDATE pokemons SET hp = ? WHERE pokemon_id = ?";

        try (Connection connection = DatabaseConnection.createDatabaseConnection();
                var statement = connection.prepareStatement(SQL_UPDATE_POKEMON_HP)) {
            statement.setInt(1, hp);
            statement.setInt(2, pokemonID);

            statement.executeUpdate();

        } catch (SQLException e) {
            Logger.printError("Error saving Pokemon HP: " + e.getMessage());
        }
    }

    public void savePokemonXp() {
        String SQL_UPDATE_POKEMON_XP = "UPDATE pokemons SET xp = ? WHERE pokemon_id = ?";

        try (Connection connection = DatabaseConnection.createDatabaseConnection();
                var statement = connection.prepareStatement(SQL_UPDATE_POKEMON_XP)) {
            statement.setInt(1, xp);
            statement.setInt(2, pokemonID);

            statement.executeUpdate();

        } catch (SQLException e) {
            Logger.printError("Error saving Pokemon XP: " + e.getMessage());
        }
    }

    public void savePokemonLevel() {
        String SQL_UPDATE_POKEMON_LEVEL = "UPDATE pokemons SET level = ? WHERE pokemon_id = ?";

        try (Connection connection = DatabaseConnection.createDatabaseConnection();
                var statement = connection.prepareStatement(SQL_UPDATE_POKEMON_LEVEL)) {
            statement.setInt(1, level);
            statement.setInt(2, pokemonID);

            statement.executeUpdate();

        } catch (SQLException e) {
            Logger.printError("Error saving Pokemon Level: " + e.getMessage());
        }
    }

    public void savePokemonMoves() {
        String SQL_UPDATE_POKEMON_MOVES = "UPDATE pokemons SET moves = ? WHERE pokemon_id = ?";

        try (Connection connection = DatabaseConnection.createDatabaseConnection();
                var statement = connection.prepareStatement(SQL_UPDATE_POKEMON_MOVES)) {
            statement.setString(1, gson.toJson(moves));
            statement.setInt(2, pokemonID);

            statement.executeUpdate();

        } catch (SQLException e) {
            Logger.printError("Error saving Pokemon Moves: " + e.getMessage());
        }
    }

    // Method to calculate HP, considering level, IV, and EV
    private int calculateHP(int baseHp, int hpIv, int hpEv) {
        // HP formula considering level
        return (int) (((2 * baseHp + hpIv + (hpEv / 4)) * level) / 100) + level + 10;
    }

    // Set the level of the Pokémon
    public void setLevel(int level) {
        this.level = level;

        // Recalculate HP based on new level
        int baseHp = new PokemonsManager().getPokemonDataByIndex(id).getHp();
        this.hp = calculateHP(baseHp, hpIv, hpEv);
        this.maxHp = this.hp;

        Logger.printDebug("Pokemon leveled up to " + level + "!");
        Logger.printDebug("HP: " + hp + "/" + maxHp);
    }

    // Set move for the Pokémon
    public void setMove(int index, int move) {
        moves[index] = move;
    }

    // Set all moves for the Pokémon
    public void setMoves(int[] moves) {
        this.moves = moves;
    }

    // Use move on the target Pokémon
    public int useMove(int moveId, Pokemon target) {
        PokemonMove move = pokemonMovesManager.getPokemonMoveById(moveId);

        if (move == null) {
            return 0; // normal effect
        }

        // Get the move power and accuracy
        Integer power = move.getPower();
        Integer accuracy = move.getAccuracy();
        if (power == null || accuracy == null) {
            return 0; // normal effect
        }

        // Calculate move hit based on accuracy and target's evasion
        boolean moveHits = isMoveSuccessful(accuracy, target);

        if (!moveHits) {
            Logger.printDebug("The move missed!");
            return 0; // normal effect
        }

        // Calculate the attack and defense stats based on IVs and EVs
        int attackerAttack = calculateStat("atk");
        int attackerSpAtk = calculateStat("spAtk");
        int targetDefense = target.calculateStat("def");
        int targetSpDef = target.calculateStat("spDef");

        // Determine if the move uses Attack or Special Attack
        int damage = 0;
        if (move.getCategory().equals("Physical")) {
            // Physical move: Use Attack and Target's Defense
            damage = calculateDamage(power, attackerAttack, targetDefense);
        } else {
            // Special move: Use Special Attack and Target's Special Defense
            damage = calculateDamage(power, attackerSpAtk, targetSpDef);
        }

        // Check effectiveness based on move type and target's types
        double effectiveness = calculateEffectiveness(move.getType(), target);

        // Apply effectiveness modifier to damage
        damage = (int) (damage * effectiveness);

        // Apply damage to target's HP, ensuring it doesn't go below 0
        target.hp -= damage;

        if (target.hp < 0) {
            target.hp = 0;
        }

        if (target.pokemonID != null) {
            target.savePokemonHp();
        }

        Logger.printDebug("Damage dealt: " + damage + " (Effectiveness: " + effectiveness + ")");
        Logger.printDebug("Target's HP is now: " + target.hp + "/" + target.maxHp);

        // return -1 if not very effective, 0 if normal, 1 if super effective
        return (effectiveness == 0.5) ? -1 : (effectiveness == 2.0) ? 1 : 0;
    }

    private double calculateEffectiveness(String moveType, Pokemon target) {
        // Define type effectiveness table (simplified)
        // Effectiveness: 2.0 = super effective, 0.5 = not very effective, 1.0 = neutral
        double effectiveness = 1.0;

        // Get target's types
        String targetType1 = new PokemonsManager().getPokemonDataByIndex(target.getId()).getType1();
        String targetType2 = new PokemonsManager().getPokemonDataByIndex(target.getId()).getType2();

        // Type effectiveness against the target
        if ((targetType1 != null && isSuperEffective(moveType, targetType1)) ||
                (targetType2 != null && isSuperEffective(moveType, targetType2))) {
            effectiveness = 2.0; // Super effective
        } else if ((targetType1 != null && isNotVeryEffective(moveType, targetType1)) ||
                (targetType2 != null && isNotVeryEffective(moveType, targetType2))) {
            effectiveness = 0.5; // Not very effective
        }

        return effectiveness;
    }

    private boolean isSuperEffective(String moveType, String targetType) {
        // List of type effectiveness for Super Effective (simplified)
        switch (moveType) {
            case "Flying":
                return targetType.equals("Bug") || targetType.equals("Fighting") || targetType.equals("Grass");
            case "Fire":
                return targetType.equals("Bug") || targetType.equals("Grass") || targetType.equals("Ice");
            case "Water":
                return targetType.equals("Fire") || targetType.equals("Ground") || targetType.equals("Rock");
            // Add more cases for different types
            default:
                return false;
        }
    }

    private boolean isNotVeryEffective(String moveType, String targetType) {
        // List of type effectiveness for Not Very Effective (simplified)
        switch (moveType) {
            case "Flying":
                return targetType.equals("Electric") || targetType.equals("Rock") || targetType.equals("Steel");
            case "Fire":
                return targetType.equals("Fire") || targetType.equals("Rock") || targetType.equals("Water");
            case "Water":
                return targetType.equals("Water") || targetType.equals("Grass") || targetType.equals("Electric");
            // Add more cases for different types
            default:
                return false;
        }
    }

    // Method to check if the move hits, factoring in the accuracy and evasion
    private boolean isMoveSuccessful(int moveAccuracy, Pokemon target) {
        int evasion = target.getEvasionModifier(); // Get target's evasion modifier
        int accuracyCheck = (int) (Math.random() * 100); // Random number between 0 and 99

        // Accuracy check formula: Move hits if random number is less than move
        // accuracy, adjusted by target's evasion modifier
        return accuracyCheck < (moveAccuracy - evasion);
    }

    // Get the evasion modifier for this Pokémon
    public int getEvasionModifier() {
        return evasionModifier; // This value will be adjusted by moves like Minimize
    }

    // Method to calculate stat value based on IVs and EVs
    private int calculateStat(String statType) {
        int baseStat = 0;
        int iv = 0;
        int ev = 0;

        switch (statType) {
            case "atk" -> {
                baseStat = new PokemonsManager().getPokemonDataByIndex(id).getAttack();
                iv = atkIv;
                ev = atkEv;
            }
            case "spAtk" -> {
                baseStat = new PokemonsManager().getPokemonDataByIndex(id).getSpecialAttack();
                iv = spAtkIv;
                ev = spAtkEv;
            }
            case "def" -> {
                baseStat = new PokemonsManager().getPokemonDataByIndex(id).getDefense();
                iv = defIv;
                ev = defEv;
            }
            case "spDef" -> {
                baseStat = new PokemonsManager().getPokemonDataByIndex(id).getSpecialDefense();
                iv = spDefIv;
                ev = spDefEv;
            }
            case "spd" -> {
                baseStat = new PokemonsManager().getPokemonDataByIndex(id).getSpeed();
                iv = spdIv;
                ev = spdEv;
            }
            case "hp" -> {
                baseStat = new PokemonsManager().getPokemonDataByIndex(id).getHp();
                iv = hpIv;
                ev = hpEv;
            }
        }

        return (int) (((2 * baseStat + iv + ev / 4) * level) / 100) + 5;
    }

    // Method to calculate damage using the formula
    private int calculateDamage(int power, int attackerStat, int targetStat) {
        // Using a simplified version of the damage formula
        return (int) ((2 * level / 5 + 2) * power * attackerStat / targetStat / 50 + 2);
    }

    public int getId() {
        return this.id;
    }

    public int getHp() {
        return this.hp;
    }

    public int getRandomMove() {
        // Don't pick null moves
        int move = 0;
        while (move == 0) {
            move = moves[(int) (Math.random() * 4)];
        }
        return move;
    }

    public void heal() {
        int maxHp = calculateHP(new PokemonsManager().getPokemonDataByIndex(id).getHp(), hpIv, hpEv);
        this.hp = maxHp;
        Logger.printDebug("hp: " + hp + "/" + maxHp);
        this.savePokemonHp();
    }

    // Method to adjust evasion (e.g., from moves like Minimize)
    public void adjustEvasion(int change) {
        evasionModifier += change;
    }

    public int getSpeed() {
        return calculateStat("spd");
    }

    private int getModifiedCatchRate(int baseCatchRate, int rateModified, int hpMax, int hpCurrent, int bonusStatus) {
        // Handling HP overflow (Halve twice for large HP)
        if (3 * hpMax > 255) {
            hpMax /= 2;
            hpCurrent /= 2;
        }

        // Calculate the catch rate 'a' using the base catch rate
        int a = Math.max((3 * hpMax - 2 * hpCurrent) * baseCatchRate * rateModified / (3 * hpMax), 1) + bonusStatus;

        // Cap the value of 'a' at 255
        return Math.min(a, 255);
    }

    private int getShakeProbability(int a) {
        // Shake probability table for Generation II
        if (a <= 1)
            return 63;
        if (a == 2)
            return 75;
        if (a == 3)
            return 84;
        if (a == 4)
            return 90;
        if (a == 5)
            return 95;
        if (a <= 7)
            return 103;
        if (a <= 10)
            return 113;
        if (a <= 15)
            return 126;
        if (a <= 20)
            return 134;
        if (a <= 30)
            return 149;
        if (a <= 40)
            return 160;
        if (a <= 50)
            return 169;
        if (a <= 60)
            return 177;
        if (a <= 80)
            return 191;
        if (a <= 100)
            return 201;
        if (a <= 120)
            return 211;
        if (a <= 140)
            return 220;
        if (a <= 160)
            return 227;
        if (a <= 180)
            return 234;
        if (a <= 200)
            return 240;
        if (a <= 220)
            return 246;
        if (a <= 240)
            return 251;
        if (a <= 254)
            return 253;
        return 255; // Catch rate of 255
    }

    private int getBaseCatchRate(int pokemonId) {
        switch (pokemonId) {
            case 16:
                return 255; // Pidgey
            default:
                return 255; // Default base catch rate
        }
    }

    public int tryCatch(int itemId) {
        int rateModified = 1; // Default catch rate multiplier
        int bonusStatus = 0; // Bonus status (e.g., asleep or frozen)

        // Retrieve the base catch rate for this Pokémon
        int baseCatchRate = getBaseCatchRate(this.id);

        // Get the modified catch rate 'a'
        int a = getModifiedCatchRate(baseCatchRate, rateModified, this.maxHp, this.hp, bonusStatus);
        // log the modified catch rate
        Logger.printDebug("Modified catch rate: " + a);

        // Get the shake probability 'b'
        int b = getShakeProbability(a);

        // First check for automatic capture
        int randomValue = (int) (Math.random() * 256);
        if (randomValue <= a) {
            Logger.printDebug("Pokemon was caught immediately!");
            return 0; // Successful catch on first try
        }

        // If no capture, simulate the shake checks (up to 3 shakes)
        int shakes = 0;
        for (int i = 0; i < 3; i++) {
            randomValue = (int) (Math.random() * 256);
            if (randomValue <= b) {
                shakes++;
            }
        }

        Logger.printDebug("Shake attempts: " + shakes);

        // If 3 shakes, the Pokémon is caught, otherwise, it is not
        if (shakes == 3) {
            Logger.printDebug("Pokemon was caught after " + shakes + " shakes!");
            return 1; // Successful catch
        }

        Logger.printDebug("Pokemon broke free after " + shakes + " shakes!");
        return -1; // Pokémon not caught
    }

    public int getLevel() {
        return this.level;
    }

    public void addXp(Pokemon defeatedPokemon, boolean isTrainerBattle, int participantsCount) {
        PokemonData data = new PokemonsManager().getPokemonDataByIndex(defeatedPokemon.getId());
        int baseExp = data.getXpYield();
        int defeatedLevel = defeatedPokemon.getLevel();

        // Trainer battle multiplier
        double a = isTrainerBattle ? 1.5 : 1.0;

        // Lucky Egg multiplier TODO: Implement Lucky Egg item
        double e = 1.0;

        // Outsider Pokémon multiplier
        double t = this.isOutsider ? 1.5 : 1.0;

        int s = participantsCount;

        // Generation II experience formula: ΔEXP = (b * L^3 * a) / (7 * s) * e * t
        int expGained = (int) (((baseExp * Math.pow(defeatedLevel, 3) * a) / (7 * s)) * e * t);

        // Add the experience to the Pokémon
        this.xp += expGained;

        this.savePokemonXp();
    }

}
