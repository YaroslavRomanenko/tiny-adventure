package com.yarikcompany.game.level.tiles;

import com.yarikcompany.game.gfx.Colors;
import com.yarikcompany.game.gfx.Screen;
import com.yarikcompany.game.level.Level;

import java.awt.*;

public abstract class Tile {
    private static final Tile[] tiles = new Tile[256];
    public static final Tile VOID = new BasicSolidTile(0, 0, 0, Colors.get(000, -1, -1, -1), 0xFF000000);
    public static final Tile STONE = new BasicSolidTile(1, 1, 0, Colors.get(131, 222, 333, 444), 0xFF555555);
    public static final Tile GRASS = new BasicTile(2, 2, 0, Colors.get(-1, 131, 141, -1), 0xFF00FF00);

    protected byte id;
    protected boolean solid;
    protected boolean emitter;

    private int levelColor;

    public Tile(int id, boolean isSolid, boolean isEmitter, int levelColor) {
        this.id = (byte)id;
        if (tiles[id] != null) throw new RuntimeException("Duplicate tile id on " + id);

        this.solid = isSolid;
        this.emitter = isEmitter;
        this.levelColor = levelColor;

        tiles[id] = this;
    }

    public abstract void render(Screen screen, Level level, int x, int y);

    public static Tile[] getTiles() { return tiles; }
    public byte getId() { return id; }
    public boolean isSolid() { return solid; }
    public boolean isEmitter() { return emitter; }
    public int getLevelColor() { return levelColor; }
}
