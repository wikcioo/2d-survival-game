package game.worlds;

import game.Handler;
import game.entities.EntityManager;
import game.entities.creatures.Player;
import game.entities.statics.Rock;
import game.entities.statics.Tree;
import game.items.ItemManager;
import game.tiles.Tile;
import game.tiles.TileManager;
import game.utils.Utils;

import java.awt.*;

public class World {

    private Handler handler;
    private int width, height;
    private int spawnX, spawnY;
    private int[][] tiles;

    // Entities
    private EntityManager entityManager;

    // Item
    private ItemManager itemManager;

    public World(Handler handler, String path) {
        this.handler = handler;
        entityManager = new EntityManager(handler, new Player(handler, 100, 100));
        itemManager = new ItemManager(handler);

        entityManager.addEntity(new Tree(handler, 40, 30));
        entityManager.addEntity(new Tree(handler, 40, 50));
        entityManager.addEntity(new Tree(handler, 40, 150));
        entityManager.addEntity(new Tree(handler, 70, 200));
        entityManager.addEntity(new Tree(handler, 90, 250));
        entityManager.addEntity(new Rock(handler, 100, 100));

        loadWorld(path);

        entityManager.getPlayer().setX(spawnX);
        entityManager.getPlayer().setY(spawnY);
    }

    public void tick() {
        entityManager.tick();
        itemManager.tick();
    }

    public void render(Graphics g) {
        int xStart = (int) Math.max(0, handler.getGameCamera().getxOffset() / Tile.TILE_WIDTH);
        int xEnd = (int) Math.min(width, (handler.getGameCamera().getxOffset() + handler.getWidth()) / Tile.TILE_WIDTH + 1);
        int yStart = (int) Math.max(0, handler.getGameCamera().getyOffset() / Tile.TILE_HEIGHT);
        int yEnd = (int) Math.min(height, (handler.getGameCamera().getyOffset() + handler.getHeight()) / Tile.TILE_HEIGHT + 1);

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                getTile(x, y).render(
                        g,
                        (int) (x * Tile.TILE_WIDTH - handler.getGameCamera().getxOffset()),
                        (int) (y * Tile.TILE_HEIGHT - handler.getGameCamera().getyOffset())
                );
            }
        }

        itemManager.render(g);
        entityManager.render(g);
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return TileManager.grassTile;

        Tile t = TileManager.tiles[tiles[x][y]];
        if (t == null) {
            return TileManager.dirtTile;
        }
        return t;
    }

    private void loadWorld(String path) {
        String file = Utils.loadFileAsString(path);
        String[] tokens = file.split("\\s+");

        width = Utils.parseInt(tokens[0]);
        height = Utils.parseInt(tokens[1]);
        spawnX = Utils.parseInt(tokens[2]);
        spawnY = Utils.parseInt(tokens[3]);

        tiles = new int[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[x][y] = Utils.parseInt(tokens[(y * width + x) + 4]);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }
}
