package com.yarikcompany.game.gfx;

public class Screen {
    private static final int MAP_WIDTH = 64;
    private static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;

    private int[] pixels;

    private int xOffset = 0;
    private int yOffset = 0;

    private int width;
    private int height;

    private SpriteSheet sheet;

    public Screen(int width, int height, SpriteSheet sheet) {
        this.width = width;
        this.height = height;
        this.sheet = sheet;

        pixels = new int[width * height];
    }

    public void render(int xPos, int yPos, int tile, int color) {
        render (xPos, yPos, tile, color, false, false);
    }

    public void render(int xPos, int yPos, int tile, int color, boolean mirrorX, boolean mirrorY) {
        xPos -= xOffset;
        yPos -= yOffset;

        int xTile = tile % 32;
        int yTile = tile / 32;
        int tileOffset = (xTile << 3) + (yTile << 3) * sheet.getWidth();
        for (int y = 0; y < 8; y++) {
            if (y + yPos < 0 || y + yPos >= height) continue;
            int ySheet = y;

            if (mirrorY) ySheet = 7 - y;

            for (int x = 0; x < 8; x++) {
                if (x + xPos < 0 || x + xPos >= width) continue;
                int xSheet = x;

                if (mirrorX) xSheet = 7 - x;

                int col = (color >> (sheet.getPixels()[xSheet + ySheet * sheet.getWidth() + tileOffset] * 8)) & 255;

                if (col < 255) pixels[(x + xPos) + (y + yPos) * width] = col;
            }
        }
    }

    public int getXOffset() { return xOffset; }
    public int getYOffset() { return yOffset; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int[] getPixels() { return pixels; }

    public void setXOffset(int xOffset) { this.xOffset = xOffset; }
    public void setYOffset(int yOffset) { this.yOffset = yOffset; }
    public void setOffset(int xOffset, int yOffset) { this.xOffset = xOffset; this.yOffset = yOffset; }
}
