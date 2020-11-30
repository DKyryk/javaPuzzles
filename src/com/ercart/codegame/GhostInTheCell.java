package com.ercart.codegame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

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
    private static Set<Integer> PLANNED_MY = new HashSet<>();
    private static Set<Integer> BOMB_TARGET = new HashSet<>();

    private static Set<Troop> TROOPS = new HashSet<>();
    private static int HIGHEST_PRODUCTION = 0;

    private static int BOMB_LEFT = 2;

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

        // game loop

        while (true) {
            clear();

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
                    TROOPS.add(new Troop(owner, target, count, arriveIn));
                }
                if (Type.BOMB.name().equals(entityType)) {
                    int owner = in.nextInt();
                    int source = in.nextInt();
                    int target = in.nextInt();
                    int arriveIn = in.nextInt();
                    if (owner == MY) {
                        BOMB_TARGET.add(target);
                    }
                    in.nextInt(); //unused
                }
            }
            HIGHEST_PRODUCTION = IntStream.of(FACTORY_PRODUCTION)
                    .max()
                    .orElse(3);

            List<Action> actions = getActions();
            if (actions.isEmpty()) {
                System.out.println("WAIT");
            }
            else {
                System.out.println(actions.stream().map(Action::print).collect(joining(";")));
            }
        }
    }

    private static void clear() {
        MY_FACTORIES.clear();
        PLANNED_MY.clear();
        BOMB_TARGET.clear();
        TROOPS.clear();
        for (int i = 0; i < FACTORY_COUNT; i++) {
            FACTORY_OWNER[i] = RESET;
            FACTORY_CYBORGS[i] = RESET;
            FACTORY_PRODUCTION[i] = RESET;
        }
    }

    private static List<Action> getActions() {
        List<Action> result = new ArrayList<>();
        result.addAll(moveToOccupy());
        result.addAll(clearWithZeroProduction());

        if (BOMB_LEFT != 0) {
            Action bombApplicable = bomb();
            if (bombApplicable != null) {
                result.add(bombApplicable);
                BOMB_LEFT--;
            }
        }
        return result;
    }

    private static Action bomb() {

        for (int targetId = 0; targetId < FACTORY_COUNT; targetId++) {
            if (FACTORY_OWNER[targetId] == ENEMY
                    && FACTORY_PRODUCTION[targetId] == HIGHEST_PRODUCTION
                    && !BOMB_TARGET.contains(targetId)) {
                Optional<Integer> id = findNearestNonPlannedMy(targetId);
                if (id.isPresent()) {
                    return new Bomb(id.get(), targetId);
                }
            }
        }
        return null;
    }

    private static Optional<Integer> findNearestNonPlannedMy(int targetId) {
        Set<Integer> nonPlannedMy = new HashSet<>();
        nonPlannedMy.addAll(MY_FACTORIES);
        nonPlannedMy.removeAll(PLANNED_MY);
        return nonPlannedMy.stream()
                .map(id -> new Pair(id, DISTANCES[id][targetId]))
                .min(Pair::compareTo)
                .map(pair -> pair.id);
    }

    private static List<Action> moveToOccupy() {
        Set<Integer> nonPlannedMy = new HashSet<>();
        nonPlannedMy.addAll(MY_FACTORIES);
        nonPlannedMy.removeAll(PLANNED_MY);
        List<Action> actions = new ArrayList<>();
        for (int myFactoryId : nonPlannedMy) {
            List<Pair> possibleOptions = new ArrayList<>();
            for (int targetId = 0; targetId < FACTORY_COUNT; targetId++) {
                if (FACTORY_OWNER[targetId] != MY && !BOMB_TARGET.contains(targetId)) {
                    int atArrival = calculateTroopsAtArrival(myFactoryId, targetId);
                    if (atArrival < FACTORY_CYBORGS[myFactoryId] && atArrival > 0) {
                        possibleOptions.add(new Pair(targetId, FACTORY_PRODUCTION[targetId]));
                    }
                }
            }
            Optional<Action> action = possibleOptions.stream()
                    .max(Pair::compareTo)
                    .map(p -> new Move(myFactoryId, p.id, FACTORY_CYBORGS[myFactoryId]));
            if (action.isPresent()) {
                actions.add(action.get());
                PLANNED_MY.add(myFactoryId);
            }
        }
        return actions;
    }

    private static int calculateTroopsAtArrival(int myFactoryId, int targetId) {
        int arrivalTime = DISTANCES[targetId][myFactoryId];
        int count = FACTORY_CYBORGS[targetId] + FACTORY_PRODUCTION[targetId] * arrivalTime;
        for (Troop troop : TROOPS) {
            if (troop.target == targetId && troop.arriveIn < arrivalTime) {
                if (troop.owner == ENEMY) {
                    count = count + troop.count;
                } else if (troop.owner == MY) {
                    count = count - troop.count;
                }
            }
        }

        return count;
    }

    private static List<Action> clearWithZeroProduction() {
        Set<Integer> nonPlannedMy = new HashSet<>();
        nonPlannedMy.addAll(MY_FACTORIES);
        nonPlannedMy.removeAll(PLANNED_MY);
        List<Action> actions = new ArrayList<>();

        for (int myFactoryId : nonPlannedMy) {
            if (FACTORY_PRODUCTION[myFactoryId] == 0) {
                int target = findNearestMyNonZeroProduction(myFactoryId);
                if (target != -1) {
                    actions.add(new Move(myFactoryId, target, FACTORY_CYBORGS[myFactoryId]));
                    PLANNED_MY.add(myFactoryId);
                }
            }
        }

        return actions;
    }

    private static int findNearestMyNonZeroProduction(int source) {

        Optional<Pair> result = MY_FACTORIES.stream()
                .mapToInt(i -> i)
                .filter(id -> id != source)
                .filter(id -> FACTORY_PRODUCTION[id] != 0)
                .mapToObj(id -> new Pair(id, DISTANCES[source][id]))
                .min(Pair::compareTo);

        return result.map(p -> p.id)
                .orElse(-1);
    }

    private static class Pair implements Comparable<Pair> {
        int id;
        int value;

        private Pair(int id, int value) {
            this.id = id;
            this.value = value;
        }

        @Override
        public int compareTo(Pair o) {
            return Integer.compare(value, o.value);
        }
    }

    private static abstract class Action {
        private Action(int source, int destination) {
            this.source = source;
            this.destination = destination;
        }

        int source;
        int destination;
        abstract String print();
    }

    private static class Troop {
        int owner;
        int target;
        int count;
        int arriveIn;

        private Troop(int owner, int target, int count, int arriveIn) {
            this.owner = owner;
            this.target = target;
            this.count = count;
            this.arriveIn = arriveIn;
        }
    }

    private static class Move extends Action {
        int count;

        private Move(int source, int destination, int count) {
            super(source, destination);
            this.count = count;
        }

        @Override
        String print() {
            return "MOVE " + source + " " + destination + " " + count;
        }
    }

    private static class Bomb extends Action {

        private Bomb(int source, int destination) {
            super(source, destination);
        }

        @Override
        String print() {
            return "BOMB " + source + " " + destination;
        }
    }

    private enum Type {
        FACTORY, TROOP, BOMB
    }
}
