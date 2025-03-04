package com.g8e.gameserver.tile;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.g8e.gameserver.World;
import com.g8e.util.Logger;

public class TileManager {

    private World world;
    public Tile[] tile;
    public int[][] mapTileNumLayer1;

    public int chunkSize = 50;

    public TileManager(World world) {
        this.world = world;
        tile = new Tile[8000];

        // Initialize tile maps for each layer
        mapTileNumLayer1 = new int[world.maxWorldCol][world.maxWorldRow];

        getTiles();
        loadMap("/data/map/pokemon_online_map.csv", 1); // Load layer 1 map

    }

    public TilePosition getClosestWalkableTile(int x, int y) {
        try {
            int distance = 0;

            while (true) {
                // Search in increasing distance from the target tile
                for (int i = -distance; i <= distance; i++) {
                    for (int j = -distance; j <= distance; j++) {
                        // Only check the outermost tiles of the current square (Manhattan distance)
                        if (i == -distance || i == distance || j == -distance || j == distance) {
                            int newX = x + i;
                            int newY = y + j;

                            // Check bounds to avoid IndexOutOfBoundsException
                            if (newX >= 0 && newX < world.maxWorldCol && newY >= 0 && newY < world.maxWorldRow) {
                                if (!getCollisionByXandY(newX, newY)) {
                                    return new TilePosition(newX, newY);
                                }
                            }
                        }
                    }
                }
                // If no walkable tile found, expand the search area by increasing the distance
                distance++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // In case no walkable tile is found (very unlikely in a proper world setup)
    }

    public int getChunkByWorldXandY(int worldX, int worldY) {
        try {
            // world is divided into chunks of 10x10 tiles
            // starting from top left corner of the world
            int chunkX = worldX / chunkSize;
            int chunkY = worldY / chunkSize;
            return chunkX + chunkY * (world.maxWorldCol / chunkSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int[] getNeighborChunks(int chunk) {
        try {
            int[] neighbors = new int[8];
            int chunkX = chunk % (world.maxWorldCol / chunkSize);
            int chunkY = chunk / (world.maxWorldCol / chunkSize);

            neighbors[0] = chunkX - 1 + (chunkY - 1) * (world.maxWorldCol / chunkSize);
            neighbors[1] = chunkX + (chunkY - 1) * (world.maxWorldCol / chunkSize);
            neighbors[2] = chunkX + 1 + (chunkY - 1) * (world.maxWorldCol / chunkSize);
            neighbors[3] = chunkX - 1 + chunkY * (world.maxWorldCol / chunkSize);
            neighbors[4] = chunkX + 1 + chunkY * (world.maxWorldCol / chunkSize);
            neighbors[5] = chunkX - 1 + (chunkY + 1) * (world.maxWorldCol / chunkSize);
            neighbors[6] = chunkX + (chunkY + 1) * (world.maxWorldCol / chunkSize);
            neighbors[7] = chunkX + 1 + (chunkY + 1) * (world.maxWorldCol / chunkSize);

            return neighbors;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Is this even needed anymore?
    public Tile getTileByXandY(int x, int y) {
        try {
            // For now, return the tile from Layer 1
            int index = mapTileNumLayer1[x][y];
            return tile[index];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean getCollisionByXandY(int x, int y) {
        try {
            // Check if x and y are within the bounds of the map arrays
            if (x < 0 || y < 0 || x >= mapTileNumLayer1.length || y >= mapTileNumLayer1[0].length) {
                return false; // Out of bounds, no collision
            }

            int index1 = mapTileNumLayer1[x][y];

            // Check for valid tile indices and if any of them have a collision
            if ((index1 >= 0 && tile[index1].collision)) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getTiles() {
        try {
            BufferedImage tileSheet = ImageIO
                    .read(getClass().getResourceAsStream("/data/tilesheets/pokemon_online_tilesheet.png"));

            int numTilesAcross = tileSheet.getWidth() / 8;
            Logger.printInfo("Number of tiles across: " + numTilesAcross);
            tile = new Tile[numTilesAcross * 6];
            for (int col = 0; col < numTilesAcross; col++) {
                for (int row = 0; row < 6; row++) {
                    int index = col + row * numTilesAcross;

                    if (index >= 310 && index <= 313) {
                        tile[index] = new Tile(false, index);
                    }

                    else {
                        tile[index] = new Tile(true, index);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setup(int index, boolean collision) {
        try {
            tile[index] = new Tile(collision, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to load maps for different layers
    public void loadMap(String filePath, int layer) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < world.maxWorldCol && row < world.maxWorldRow) {
                String line = br.readLine();

                while (col < world.maxWorldCol) {
                    String numbers[] = line.split(",");
                    int num = Integer.parseInt(numbers[col]);

                    // Depending on the layer, assign the number to the respective map
                    if (layer == 1) {
                        mapTileNumLayer1[col][row] = num;
                    }
                    col++;
                }
                if (col == world.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean canWildPokemonAppearOnTile(int x, int y) {
        try {
            Tile tile = getTileByXandY(x, y);
            if (tile.numberRepresentation == 312) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
