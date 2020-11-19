package com.ercart.codegame;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @author dkyryk
 */
public class GhostInTheCell {

    private static final int RESET = -10;

    private static final int MY = 1;
    private static final int NEUTRAL = 0;
    private static final int ENEMY = -1;

    private static int FACTORY_COUNT;
    private static int[] FACTORY_OWNER;
    private static int[] FACTORY_CYBORGS;
    private static int[] FACTORY_PRODUCTION;
    private static int[][] DISTANCES;
    private static Set<Integer> MY_FACTORIES = new HashSet<>();

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        FACTORY_COUNT = in.nextInt();
        DISTANCES = new int[FACTORY_COUNT][FACTORY_COUNT];

        int linkCount = in.nextInt();
        for (int i = 0; i < linkCount; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            int distance = in.nextInt();
            DISTANCES[a][b] = distance;
            DISTANCES[b][a] = distance;
        }

        FACTORY_OWNER = new int[FACTORY_COUNT];
        FACTORY_CYBORGS = new int[FACTORY_COUNT];
        FACTORY_PRODUCTION = new int[FACTORY_COUNT];
        int[][] enemiesAtArrival = new int[FACTORY_COUNT][FACTORY_COUNT];
        // game loop

        while (true) {
            MY_FACTORIES.clear();
            for (int i = 0; i < FACTORY_COUNT; i++) {
                FACTORY_OWNER[i] = RESET;
                FACTORY_CYBORGS[i] = RESET;
                FACTORY_PRODUCTION[i] = RESET;
                for (int j = 0; j < FACTORY_COUNT; j++) {
                    enemiesAtArrival[i][j] = RESET;
                }
            }
            int entityCount = in.nextInt(); // the number of entities (e.g. factories and troops)
            for (int i = 0; i < entityCount; i++) {
                int entityId = in.nextInt();
                String entityType = in.next();
                if (Type.FACTORY.name().equals(entityType)) {
                    int owner = in.nextInt();
                    FACTORY_OWNER[entityId] = owner;
                    if (owner == MY) {
                        MY_FACTORIES.add(entityId);
                    }
                    FACTORY_CYBORGS[entityId] = in.nextInt();
                    FACTORY_PRODUCTION[entityId] = in.nextInt();
                    in.nextInt(); //unused
                    in.nextInt(); //unused
                }
                if (Type.TROOP.name().equals(entityType)) {
                    int owner = in.nextInt();
                    int source = in.nextInt();
                    int target = in.nextInt();
                    int count = in.nextInt();
                    int arriveIn = in.nextInt();
                }
            }


            if (!move()) {
                System.out.println("WAIT");
            }
        }
    }

    private static boolean move() {
        for (int targetId = 0; targetId < FACTORY_COUNT; targetId++) {
            if (FACTORY_OWNER[targetId] != MY) {
                for (int myFactoryId : MY_FACTORIES) {
                    int atArrival = FACTORY_CYBORGS[targetId] + FACTORY_PRODUCTION[targetId] * DISTANCES[targetId][myFactoryId];
                    if (atArrival < FACTORY_CYBORGS[myFactoryId]) {
                        //move to factory
                        System.out.println("MOVE " + myFactoryId + " " + targetId + " " + FACTORY_CYBORGS[myFactoryId]);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private enum Type {
        FACTORY, TROOP
    }
}
