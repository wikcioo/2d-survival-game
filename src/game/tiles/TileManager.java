package game.tiles;

public class TileManager {

    public static Tile[] tiles = new Tile[256];

    public static Tile grassTile = new GrassTile(0);
    public static Tile dirtTile = new DirtTile(1);
    public static Tile rockTile = new RockTile(2);

}
