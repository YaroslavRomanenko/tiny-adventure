package com.yarikcompany.game.gfx;

public class Screen {
    private static final int MAP_WIDTH = 64;
    private static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;

    private static final byte BIT_MIRROR_X = 0x01;
    private static final byte BIT_MIRROR_Y = 0x02;

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

    public void render(int xPos, int yPos, int tile, int color, int mirrorDir, int scale) {
        xPos -= xOffset;
        yPos -= yOffset;

        boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
        boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

        int scaleMap = scale - 1;
        int xTile = tile % 32;
        int yTile = tile / 32;
        int tileOffset = (xTile << 3) + (yTile << 3) * sheet.getWidth();
        for (int y = 0; y < 8; y++) {

            int ySheet = y;

            if (mirrorY) ySheet = 7 - y;

            int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3) / 2);


            for (int x = 0; x < 8; x++) {
                int xSheet = x;

                if (mirrorX) xSheet = 7 - x;

                int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << 3) / 2);
                int col = (color >> (sheet.getPixels()[xSheet + ySheet * sheet.getWidth() + tileOffset] * 8)) & 255;

                if (col < 255) {
                    for (int yScale = 0; yScale < scale; yScale++) {
                        if (yPixel + yScale < 0 || yPixel + yScale >= height) continue;
                        for (int xScale = 0; xScale < scale; xScale++) {
                            if (xPixel + xScale < 0 || xPixel + xScale >= width) continue;
                            pixels[(xPixel + xScale) + (yPixel + yScale) * width] = col;
                        }
                    }


                }
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
