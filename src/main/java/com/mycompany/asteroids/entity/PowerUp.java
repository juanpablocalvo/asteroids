/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.asteroids.entity;

import com.mycompany.asteroids.Game;
import com.mycompany.asteroids.WorldPanel;
import com.mycompany.asteroids.util.Vector2;
import java.awt.Graphics2D;
import java.util.Random;

/**
 *
 * @author juanp
 */
public class PowerUp extends Entity{
    /**
     * @param args the command line arguments
     */
    /**
	 * The minimum speed at which the asteroid can rotate.
	 */
	private static final double MIN_ROTATION = 0.0075;
	
	/**
	 * The maximum speed at which the asteroid can rotate.
	 */
	private static final double MAX_ROTATION = 0.0175;
	
	/**
	 * The variation between the asteroid rotation speeds.
	 */
	private static final double ROTATION_VARIANCE = MAX_ROTATION - MIN_ROTATION;
	
	/**
	 * The minimum velocity at which the asteroid can move.
	 */
	private static final double MIN_VELOCITY = 0.75;
	
	/**
	 * The maximum velocity at which the asteroid can move.
	 */
	private static final double MAX_VELOCITY = 1.65;
	
	/**
	 * The variation between the asteroid velocities.
	 */
	private static final double VELOCITY_VARIANCE = MAX_VELOCITY - MIN_VELOCITY;
	
	/**
	 * The minimum distance from the player spawn that a new asteroid can spawn.
	 */
	private static final double MIN_DISTANCE = 200.0;
	
	/**
	 * The maximum distance from the player spawn that a new asteroid can spawn.
	 */
	private static final double MAX_DISTANCE = WorldPanel.WORLD_SIZE / 2.0;
	
	/**
	 * The variation between the spawn distances.
	 */
	private static final double DISTANCE_VARIANCE = MAX_DISTANCE - MIN_DISTANCE;
	
	/**
	 * The number of updates to execute after spawning.
	 */
	private static final float SPAWN_UPDATES = 10;
	
	/**
	 * The Size.
	 */
	private PowerUpSize size;
	
	/**
	 * The rotation speed.
	 */
	private double rotationSpeed;
        /**
	 * Creates a new Asteroid randomly in the world.
	 * @param random The Random instance.
	 */
	public PowerUp(Random random) {
		super(calculatePosition(random), calculateVelocity(random), PowerUpSize.Large.radius, PowerUpSize.Large.killValue);
		this.rotationSpeed = -MIN_ROTATION + (random.nextDouble() * ROTATION_VARIANCE);
		this.size = PowerUpSize.Large;
	}
        public PowerUp(Asteroid parent, PowerUpSize size, Random random) {
		super(new Vector2(parent.position), calculateVelocity(random), size.radius, size.killValue);
		this.rotationSpeed = MIN_ROTATION + (random.nextDouble() * ROTATION_VARIANCE);
		this.size = size;
		
		/*
		 * While not necessary, calling the update method here makes the asteroid
		 * appear to have a different starting position than it's parent or sibling.
		 */
		for(int i = 0; i < SPAWN_UPDATES; i++) {
			update(null);
		}
	}
        /**
	 * Calculates a random valid spawn point for an Asteroid.
	 * @param random The random instance.
	 * @return The spawn point.
	 */
	private static Vector2 calculatePosition(Random random) {
		Vector2 vec = new Vector2(WorldPanel.WORLD_SIZE / 2.0, WorldPanel.WORLD_SIZE / 2.0);
		return vec.add(new Vector2(random.nextDouble() * Math.PI * 2).scale(MIN_DISTANCE + random.nextDouble() * DISTANCE_VARIANCE));
	}
        /**
	 * Calculates a random valid velocity for an Asteroid.
	 * @param random The random instance.
	 * @return The velocity.
	 */
	private static Vector2 calculateVelocity(Random random) {
		return new Vector2(random.nextDouble() * Math.PI * 2).scale(MIN_VELOCITY + random.nextDouble() * VELOCITY_VARIANCE);
	}
        @Override
	public void update(Game game) {
		super.update(game);
		rotate(rotationSpeed); //Rotate the image each frame.
	}

	@Override
	public void draw(Graphics2D g, Game game) {
		g.drawPolygon(size.polygon); //Draw the Asteroid.
	}
        @Override
	public void handleCollision(Game game, Entity other) {
		//Prevent collisions with other asteroids.
		if(other.getClass() != Asteroid.class) {
                    //Only spawn "children" if we're not a Small asteroid.

                    //Delete this Asteroid from the world.
                    flagForRemoval();

                    //Award the player points for killing the Asteroid.
                    game.addScore(getKillScore());		
		}
	}
       
    public static void main(String args[]) {
        // TODO code application logic here
    }
}
