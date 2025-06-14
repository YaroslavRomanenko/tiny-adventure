package com.yarikcompany.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {

    public InputHandler(Game game) {
        game.addKeyListener(this);
    }

    public class Key {
        private boolean pressed = false;

        public boolean isPressed() {
            return pressed;
        }

        public void toggle(boolean isPressed) {
            this.pressed = isPressed;
        }
    }

    private Key up = new Key();
    private Key down = new Key();
    private Key left = new Key();
    private Key right = new Key();

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        toggleKey(keyEvent.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        toggleKey(keyEvent.getKeyCode(), false);
    }

    public void toggleKey(int keyCode, boolean isPressed) {
        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) { up.toggle(isPressed); }
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) { down.toggle(isPressed); }
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) { left.toggle(isPressed); }
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) { right.toggle(isPressed); }
    }

    public Key getUp() { return up; }
    public Key getDown() { return down; }
    public Key getLeft() { return left; }
    public Key getRight() { return right; }
}
