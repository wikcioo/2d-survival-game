package game;

import game.display.Display;
import game.gfx.Assets;
import game.gfx.GameCamera;
import game.input.KeyManager;
import game.input.MouseManager;
import game.states.GameState;
import game.states.MenuState;
import game.states.State;
import game.states.StateManager;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game implements Runnable {
    private Display display;

    private String title;
    private int width, height;

    private Thread thread;
    boolean running = false;

    private BufferStrategy bs;
    private Graphics g;

    // States
    public State gameState;
    public State menuState;

    // Input
    private KeyManager keyManager;
    private MouseManager mouseManager;

    // Camera
    private GameCamera  gameCamera;

    // Handler
    private Handler handler;

    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;

        keyManager = new KeyManager();
        mouseManager = new MouseManager();
    }

    private void init() {
        display = new Display(title, width, height);
        display.getFrame().addKeyListener(keyManager);
        display.getFrame().addMouseListener(mouseManager);
        display.getFrame().addMouseMotionListener(mouseManager);
        display.getCanvas().addMouseListener(mouseManager);
        display.getCanvas().addMouseMotionListener(mouseManager);

        Assets.init();

        handler = new Handler(this);
        gameCamera = new GameCamera(handler,0, 0);

        gameState = new GameState(handler);
        menuState = new MenuState(handler);

        StateManager.setState(menuState);
    }

    @Override
    public void run() {
        init();

        int fps = 60;
        double timePerTick = 1000000000d / fps;
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        long now;
        double delta = 0;
        int ticks = 0;
        int frames = 0;

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            lastTime = now;

            if (delta >= 1) {
                ticks();
                render();
                ticks++;
                frames++;
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println(ticks + " ticks, " + frames + " frames");
                ticks = 0;
                frames = 0;
                timer += 1000;
            }
        }

        stop();
    }

    private void ticks() {
        keyManager.tick();

        if (StateManager.getCurrentState() != null) {
            StateManager.getCurrentState().tick();
        }
    }

    private void render() {
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();
        g.clearRect(0, 0, width, height);

        if (StateManager.getCurrentState() != null) {
            StateManager.getCurrentState().render(g);
        }

        bs.show();
        g.dispose();

    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }

    public MouseManager getMouseManager() {
        return mouseManager;
    }

    public GameCamera getGameCamera() {
        return gameCamera;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
