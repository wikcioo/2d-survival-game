package game.entities.creatures;

import game.Handler;
import game.entities.Entity;
import game.gfx.Animation;
import game.gfx.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Creature {

    // Animations
    private Animation animationDown, animationUp, animationLeft, animationRight;

    private long lastAttackTimer, attackCooldown = 800, attackTimer = attackCooldown;

    public Player(Handler handler, float x, float y) {
        super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);

        bounds.x = 8;
        bounds.y = 16;
        bounds.width = 18;
        bounds.height = 16;

        // Animations
        animationDown = new Animation(500, Assets.player_down);
        animationUp = new Animation(500, Assets.player_up);
        animationLeft = new Animation(500, Assets.player_left);
        animationRight = new Animation(500, Assets.player_right);
    }

    @Override
    public void tick() {
        // Animations
        animationDown.tick();
        animationUp.tick();
        animationLeft.tick();
        animationRight.tick();

        // Movement
        getInput();
        move();
        handler.getGameCamera().centerOnEntity(this);

        // Attack
        checkAttacks();
    }

    private void checkAttacks() {
        attackTimer += System.currentTimeMillis() - lastAttackTimer;
        lastAttackTimer = System.currentTimeMillis();
        if (attackTimer < attackCooldown) {
            return;
        }

        Rectangle cb = getCollisionBounds(0, 0);
        Rectangle ar = new Rectangle();
        int arSize = 20;
        ar.width = arSize;
        ar.height = arSize;

        if (handler.getKeyManager().aUp) {
            ar.x = cb.x + cb.width / 2 - arSize / 2;
            ar.y = cb.y - arSize;
        } else if (handler.getKeyManager().aDown) {
            ar.x = cb.x + cb.width / 2 - arSize / 2;
            ar.y = cb.y + cb.height;
        } else if (handler.getKeyManager().aLeft) {
            ar.x = cb.x - arSize;
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        } else if (handler.getKeyManager().aRight) {
            ar.x = cb.x + cb.width;
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        } else {
            return;
        }

        attackTimer = 0;

        for (Entity e : handler.getWorld().getEntityManager().getEntities()) {
            if (e.equals(this)) continue;
            if (e.getCollisionBounds(0, 0).intersects(ar)) {
                e.hurt(1);
                return;
            }
        }
    }

    @Override
    public void die() {
        System.out.println("You lose");
    }

    private void getInput() {
        xMove = 0;
        yMove = 0;

        if (handler.getKeyManager().up) yMove = -speed;
        if (handler.getKeyManager().down) yMove = speed;
        if (handler.getKeyManager().left) xMove = -speed;
        if (handler.getKeyManager().right) xMove = speed;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(
                getCurrentAnimationFrame(),
                (int) (x - handler.getGameCamera().getxOffset()),
                (int) (y - handler.getGameCamera().getyOffset()),
                width,
                height,
                null
        );
    }

    private BufferedImage getCurrentAnimationFrame() {
        if (xMove < 0) return animationLeft.getCurrentFrame();
        else if (xMove > 0) return animationRight.getCurrentFrame();
        else if (yMove < 0) return animationUp.getCurrentFrame();
        else return animationDown.getCurrentFrame();
    }
}
