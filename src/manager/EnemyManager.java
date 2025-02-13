package manager;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import entities.Enemy;
import utils.Resource;

public class EnemyManager {
    public final int NUM_ENEMY = 10;
    public final double MIN_RESPAWN_INTERVAL = 0.7;
    public final double MAX_VELX_ENEMY = 7;
    public final double MAX_VELY_ENEMY = 12;
    public final double TIME_DECREMENT_INTERVAL_RESPAWN = 8.0;

    // public Stack<Enemy> stackEnemies;
    public Queue<Enemy> queueEnemies;
    public ArrayList<Enemy> listEnemies;

    public double timerRespawnEnemy;
    public double intervalRespawn;
    public double elapsedTime;

    public double newVelXEnemy = Enemy.ENEMY_VELX_MODULE;
    public double newVelYEnemy = Enemy.ENEMY_VELY_MODULE;

    public EnemyManager() {
        intervalRespawn = 4.0;
        // this.stackEnemies = new Stack<>();
        this.queueEnemies = new LinkedList<>();
        this.listEnemies = new ArrayList<>();

        for (int i = 0; i < NUM_ENEMY; i++) {
            // stackEnemies.push(new Enemy());
            queueEnemies.add(new Enemy());
        }
        // listEnemies.add(stackEnemies.pop());
        listEnemies.add(queueEnemies.poll());
    }

    public void spawnEnemies() {
        timerRespawnEnemy += Resource.getInstance().deltaTime;
        elapsedTime += Resource.getInstance().deltaTime;

        // Verifique se é hora de reduzir o intervalo de respawn
        if (elapsedTime >= TIME_DECREMENT_INTERVAL_RESPAWN) {
            elapsedTime = 0.0; // Resete o contador de 10 segundos
            intervalRespawn = Math.max(intervalRespawn - 0.4, MIN_RESPAWN_INTERVAL);
            // intervalRespawn -= 0.5;
            newVelXEnemy = Math.min(newVelXEnemy + 0.2, MAX_VELX_ENEMY);
            newVelYEnemy = Math.min(newVelYEnemy + 0.4, MAX_VELY_ENEMY);
            System.out.println("Novo velocidade em Y: " + newVelYEnemy);
            // System.out.println("Novo intervalo de respawn: " + intervalRespawn);
        // }
        }

        if (!queueEnemies.isEmpty()) {
            // Condição para adicionar o primeiro inimigo ou espaçar o próximo inimigo
            boolean shouldSpawnEnemy = (Resource.getInstance().timeGame == 0.0) ||
                    (timerRespawnEnemy >= intervalRespawn);

            if (shouldSpawnEnemy) {
                Enemy e = queueEnemies.poll();
                e.respawn();
                e.velX = -newVelXEnemy;
                e.velY = e.velY > 0 ? newVelYEnemy : -newVelYEnemy;
                listEnemies.add(e);
                timerRespawnEnemy = 0.0;
            }
        }

        for (int i = 0; i < listEnemies.size(); i++) {
            listEnemies.get(i).move();
        }
    }

    public void restart() {
        while (!listEnemies.isEmpty()) {
            queueEnemies.add(listEnemies.remove(0));
        }

        intervalRespawn = 4.0;
        newVelXEnemy = Enemy.ENEMY_VELX_MODULE;
    }

    public void render(Graphics g) {
        for (int i = 0; i < listEnemies.size(); i++) {
            listEnemies.get(i).render(g);
        }
    }
}
