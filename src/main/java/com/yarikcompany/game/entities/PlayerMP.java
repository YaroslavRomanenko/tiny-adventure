package com.yarikcompany.game.entities;

import com.yarikcompany.game.InputHandler;
import com.yarikcompany.game.level.Level;

import java.net.InetAddress;

public class PlayerMP extends Player {
    private InetAddress ipAddress;
    private int port;

    public PlayerMP(Level level, int x, int y, InputHandler input, String username, InetAddress ipAddress, int port) {
        super(level, x, y, input, username);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PlayerMP(Level level, int x, int y, String username, InetAddress ipAddress, int port) {
        super(level, x, y, null, username);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void tick() {
        super.tick();
    }

    public InetAddress getIpAddress() { return ipAddress; }
    public int getPort() { return port; }
}
