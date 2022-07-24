package com.interviews.lld.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Board {

    @Getter
    private final Integer size;

    // For storing the snakes, we can use a hash set and check if the player lands on one of the snakes
    private final Map<Integer, Integer> snakes, ladders;

    private final Map<String, Integer> currentPosition;

    @Getter
    private final List<Player> playersList;

    private Dice dice;

    @Getter
    @Setter
    boolean isGameEnded = false;

    public Board(Integer size, List<Snake> snakeList, List<Ladder> ladderList, List<Player> players, Dice dice) {
        this.size = size;
        this.dice = dice;
        this.playersList = players;

        this.currentPosition = new HashMap<>();
        for (Player player: players) {
            currentPosition.put(player.getName(), 0);
        }

        this.snakes = new HashMap<>();
        this.ladders = new HashMap<>();

        for(Snake snake: snakeList) {
            snakes.put(snake.getStart(), snake.getEnd());
        }

        for(Ladder ladder: ladderList) {
            snakes.put(ladder.getStart(), ladder.getEnd());
        }
    }

    public Integer getCurrentPositionForPlayer(String playerName) {
        return currentPosition.getOrDefault(playerName, -1);
    }

    public void updateCurrentPositionForPlayer(String playerName, Integer newPos) {
        currentPosition.put(playerName, newPos);
    }

    public boolean isLandedOnSnake(Integer currPos) {
        return snakes.containsKey(currPos);
    }

    public boolean isLandedOnLadder(Integer currPos) {
        return ladders.containsKey(currPos);
    }

    public Integer getSnakeTail(Integer currPos) {
        return snakes.get(currPos);
    }

    public Integer getLadderEnd(Integer currPos) {
        return ladders.get(currPos);
    }

    public Integer rollDice() {
        return dice.roll();
    }
}
