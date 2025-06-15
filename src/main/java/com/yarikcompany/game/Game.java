package com.yarikcompany.game;

import com.yarikcompany.game.entities.Player;
import com.yarikcompany.game.gfx.Colors;
import com.yarikcompany.game.gfx.Screen;
import com.yarikcompany.game.gfx.SpriteSheet;
import com.yarikcompany.game.gfx.Font;
import com.yarikcompany.game.level.Level;
import com.yarikcompany.game.net.GameClient;
import com.yarikcompany.game.net.GameServer;
import com.yarikcompany.game.net.packets.Packet00Login;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;

    private static final int WIDTH = 160;
    private static final int HEIGHT = WIDTH / 12 * 9;
    private static final int SCALE = 3;
    private static final int COLOR_SCALE_FACTOR = 255 / 5;
    private static final String NAME = "Tiny Adventure";

    private JFrame frame;

    private boolean running = false;
    private int tickCount = 0;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    private int[] colors = new int[216];

    private Screen screen;
    private InputHandler input;
    private Level level;

    private Player player;

    private GameClient socketClient;
    private GameServer socketServer;


    public Game() {
        setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame = new JFrame(NAME);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void init() {
        int index = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r * COLOR_SCALE_FACTOR);
                    int gg = (g * COLOR_SCALE_FACTOR);
                    int bb = (b * COLOR_SCALE_FACTOR);

                    colors[index++] = rr << 16 | gg << 8 | bb;
                }
            }
        }
        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
        input = new InputHandler(this);
        level = new Level("/levels/water_level.png");
//        player = new Player(level, 0, 0, input, JOptionPane.showInputDialog(this, "Please enter a username"));
//        level.addEntity(player);
//
//        socketClient.sendData("ping".getBytes());
        Packet00Login loginPacket = new Packet00Login(JOptionPane.showInputDialog(this, "Please enter a username"));
        loginPacket.writeData(socketClient);
    }

    public synchronized void start() {
        running = true;
        new Thread(this).start();

        if (JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0) {
            socketServer = new GameServer(this);
            socketServer.start();
        }

        socketClient = new GameClient(this, "localhost");
        socketClient.start();
    }

    public synchronized void stop() {
        running = false;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1_000_000_000D / 60D;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        init();

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;
                shouldRender = true;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                frame.setTitle("ticks: " + ticks + ", frames: " + frames);
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick() {
        tickCount++;
        level.tick();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        int xOffset = player.getX() - (screen.getWidth() / 2);
        int yOffset = player.getY() - (screen.getHeight() / 2);

        level.renderTiles(screen, xOffset, yOffset);

        for (int x = 0; x < level.getWidth(); x++) {
            int color = Colors.get(-1, -1, -1, 000);
            if (x % 10 == 0 && x != 0) {
                color = Colors.get(-1, -1, -1, 500);
            }
        }

        level.renderEntities(screen);

        for (int y = 0; y < screen.getHeight(); y++) {
            for (int x = 0; x < screen.getWidth(); x++) {
                int colorCode = screen.getPixels()[x + y * screen.getWidth()];
                if (colorCode < 255) pixels[x + y * WIDTH] = colors[colorCode];
            }
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new Game().start();
        
    }

    public Level getLevel () { return level; }
    public InputHandler getInput() { return input; }
    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }
}