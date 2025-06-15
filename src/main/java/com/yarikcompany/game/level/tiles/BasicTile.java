package com.yarikcompany.game.level.tiles;

import com.yarikcompany.game.gfx.Screen;
import com.yarikcompany.game.level.Level;

public class BasicTile extends Tile {
    protected int tiled;
    protected int tileColor;

    public BasicTile(int id, int x, int y, int tileColor, int levelColor) {
        super(id, false, false, levelColor);
        this.tiled = x + y;
        this.tileColor = tileColor;
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x, y, tiled, tileColor, 0x00, 1);
    }
}
