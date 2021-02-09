import display.Display;

public class Game implements Runnable {
    private Display display;
    public int width, height;
    public String title;

    private Thread thread;
    boolean running = false;

    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    private void init() {
        display = new Display(title, width, height);
    }

    @Override
    public void run() {
        init();

        while (running) {
            update();
            render();
        }

        stop();
    }

    private void update() {

    }

    private void render() {

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
}
