package com.yarikcompany.game.level;

import com.yarikcompany.game.entities.Entity;
import com.yarikcompany.game.gfx.Screen;
import com.yarikcompany.game.level.tiles.Tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Level {
    private byte[] tiles;
    private int width;
    private int height;
    private String imagePath;
    private BufferedImage image;

    private List<Entity> entities = new ArrayList<Entity>();

    public Level(String imagePath) {
        if (imagePath != null) {
            this.imagePath = imagePath;
            this.loadLevelFromFile();
        } else {
            this.width = 64;
            this.height = 64;
            tiles = new byte[width * height];
            this.generateLevel();
        }
    }

    private void loadLevelFromFile() {
        try {
            this.image = ImageIO.read(Level.class.getResourceAsStream(this.imagePath));
            this.width = image.getWidth();
            this.height = image.getHeight();

            tiles = new byte[width * height];
            this.loadTiles();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTiles() {
        int[] tileColors = this.image.getRGB(0, 0, width, height, null, 0, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tileCkeck: for (Tile t : Tile.getTiles()) {
                    if (t != null && t.getLevelColor() == tileColors[x + y * width]) {
                        this.tiles[x + y * width] = t.getId();
                        break tileCkeck;
                    }
                }
            }
        }
    }

    private void saveLevelToFile() {
        try {
            ImageIO.write(image, "png", new File(Level.class.getResource(this.imagePath).getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void alterTile(int x, int y, Tile newTile) {
        this.tiles[x + y * width] = newTile.getId();
        image.setRGB(x, y, newTile.getLevelColor());
    }

    public void generateLevel() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x * y % 10 < 7) {
                    tiles[x + y * width] = Tile.GRASS.getId();
                } else {
                    tiles[x + y * width] = Tile.STONE.getId();
                }
            }
        }
    }

    public void tick() {
        for (Entity e : entities) {
            e.tick();
        }
    }

    public void renderTiles(Screen screen, int xOffset, int yOffset) {
        if (xOffset < 0) xOffset = 0;
        if (xOffset >= ((width << 3) - screen.getWidth())) xOffset = ((width << 3) - screen.getWidth());
        if (yOffset < 0) yOffset = 0;
        if (yOffset >= ((height << 3) - screen.getHeight())) yOffset = ((height << 3) - screen.getHeight());

        screen.setOffset(xOffset, yOffset);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                getTile(x, y).render(screen, this, x << 3, y << 3);
            }
        }
    }

    public void renderEntities(Screen screen) {
        for (Entity e : entities) {
            e.render(screen);
        }
    }

    public Tile getTile(int x, int y) {
        if (0 > x || x > width || 0 > y || y >= height) return Tile.VOID;
        return Tile.getTiles()[tiles[x + y * width]];
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
