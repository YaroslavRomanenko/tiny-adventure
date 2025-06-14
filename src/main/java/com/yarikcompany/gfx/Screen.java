package com.yarikcompany.gfx;

import java.util.Map;

public class Screen {
    private static final int MAP_WIDTH = 64;
    private static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;

    private int[] tiles = new int[MAP_WIDTH * MAP_WIDTH];
    private int[] colors = new int[MAP_WIDTH * MAP_WIDTH * 4];

    private int xOffset = 0;
    private int yOffset = 0;

    private int width;
    private int height;

    private SpriteSheet sheet;

    public Screen(int width, int height, SpriteSheet sheet) {
        this.width = width;
        this.height = height;
        this.sheet = sheet;

        for (int i = 0; i < MAP_WIDTH * MAP_WIDTH; i++) {
            colors[i * 4 + 0] = 0xff00ff;
            colors[i * 4 + 1] = 0x00ffff;
            colors[i * 4 + 2] = 0xffff00;
            colors[i * 4 + 3] = 0xffffff;
        }
    }

    public void render(int[] pixels, int offset, int row) {
        for (int yTile = yOffset >> 3; yTile <= (yOffset + height) >> 3; yTile++) {
            int yMin = yTile * 8 - yOffset;
            int yMax = yMin + 8;

            if (yMin < 0) yMin = 0;
            if (yMax > height) yMax = height;

            for (int xTile = xOffset >> 3; xTile <= (xOffset + width) >> 3; xTile++) {
                int xMin = xTile * 8 - xOffset;
                int xMax = xMin + 8;

                if (xMin < 0) xMin = 0;
                if (xMax > width) xMax = width;

                int tileindex = (xTile & (MAP_WIDTH_MASK)) + (yTile & (MAP_WIDTH_MASK)) * MAP_WIDTH;

                for (int y = yMin; y < yMax; y++) {
                   int sheetPixel = ((y + yOffset) & 7) * sheet.getWidth() + ((xMin + xOffset) & 7);
                   int tilePixel = offset + xMin + y * row;
                   for (int x = xMin; x < xMax; x++) {
                       int color = tileindex * 4 + sheet.getPixels()[sheetPixel++];
                       pixels[tilePixel++] = colors[color];
                   }
                }
            }
        }
    }

    public int getXOffset() { return xOffset; }
    public int getYOffset() { return yOffset; }

    public void setXOffset(int xOffset) { this.xOffset = xOffset; }
    public void setYOffset(int yOffset) { this.yOffset = yOffset; }
}
