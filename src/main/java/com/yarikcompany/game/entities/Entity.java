package com.yarikcompany.game.entities;

import com.yarikcompany.game.gfx.Screen;
import com.yarikcompany.game.level.Level;


public abstract class Entity {
    protected int x, y;
    protected Level level;

    public Entity(Level level) {
        init(level);
    }

    public final void init(Level level) {
        this.level = level;
    }

    public abstract void tick();

    public abstract void render(Screen screen);
}
