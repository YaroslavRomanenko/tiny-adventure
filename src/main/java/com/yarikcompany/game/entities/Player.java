package com.yarikcompany.game.entities;

import com.yarikcompany.game.InputHandler;
import com.yarikcompany.game.gfx.Colors;
import com.yarikcompany.game.gfx.Screen;
import com.yarikcompany.game.level.Level;

public class Player extends Mob {

    private InputHandler input;
    private int color = Colors.get(-1, 111, 131, 543);
    private int scale = 1;

    public Player(Level level, int x, int y, InputHandler input) {
        super(level, "Player", x, y, 1);
        this.input = input;
    }

    @Override
    public void tick() {
        int xa = 0;
        int ya = 0;

        if (input.getUp().isPressed()) ya--;
        if (input.getDown().isPressed()) ya++;
        if (input.getLeft().isPressed()) xa--;
        if (input.getRight().isPressed()) xa++;

        if (xa != 0 || ya != 0) {
            move(xa, ya);
            isMoving = true;
        } else {
            isMoving = false;
        }
    }

    @Override
    public void render(Screen screen) {
        int xTile = 0;
        int yTile = 28;
        int walkingSpeed = 4;
        int flipTop = (numSteps >> walkingSpeed) & 1;
        int flipBottom = (numSteps >> walkingSpeed) & 1;

        if (movingDir == 1) {
            xTile += 2;
        } else if (movingDir > 1) {
            xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
            flipTop = (movingDir - 1) % 2;
        }

        int modifier = 8 * scale;
        int xOffset = x - modifier / 2;
        int yOffset = y - modifier / 2 - 4;

        screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, color, flipTop, scale);
        screen.render(xOffset  + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, color, flipTop, scale);
        screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (1 + yTile) * 32, color, flipBottom, scale);
        screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, color, flipBottom, scale);
    }

    @Override
    public boolean hasCollided(int xa, int ya) {
        int xMin = 0;
        int xMax = 7;
        int yMin = 3;
        int yMax = 7;

        for (int x = xMin; x < xMax; x++) {
            if (isSolidTile(xa, ya, x, yMin)) {
                return true;
            }
        }
        for (int x = xMin; x < xMax; x++) {
            if (isSolidTile(xa, ya, x, yMax)) {
                return true;
            }
        }
        for (int y = yMin; y < yMax; y++) {
            if (isSolidTile(xa, ya, xMin, y)) {
                return true;
            }
        }
        for (int y = yMin; y < yMax; y++) {
            if (isSolidTile(xa, ya, xMax, y)) {
                return true;
            }
        }
        return false;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
