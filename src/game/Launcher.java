package game;

public class Launcher {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        Game game = new Game("SurvivalGame",560, 360);
        game.start();
    }
}
