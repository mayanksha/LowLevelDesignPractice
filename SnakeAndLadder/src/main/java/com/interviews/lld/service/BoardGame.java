package com.interviews.lld.service;

import com.interviews.lld.model.Board;
import com.interviews.lld.model.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BoardGame {

    private final Board board;

    public BoardGame(Board board) {
        this.board = board;
    }

    public void start() throws Exception {
        while (!board.isGameEnded()) {
            for (Player player: board.getPlayersList()) {
                movePlayer(player.getName());
                if (board.isGameEnded())
                    break;
            }
        }
    }

    private void movePlayer(String playerName) throws Exception {
        log.debug("Moving player: {}", playerName);
        if (board.isGameEnded()) {
            log.error("The game has already ended!");
            return;
        }

        if (board.getCurrentPositionForPlayer(playerName) == -1) {
            throw new Exception(String.format("Player with the name %s doesn't exist.", playerName));
        }

        Integer diceRolledValue = board.rollDice();
        Integer newPosition = board.getCurrentPositionForPlayer(playerName) + diceRolledValue;

        log.debug("[OLD] Position. player: {}, pos: {}", playerName, board.getCurrentPositionForPlayer(playerName));
        if (newPosition > board.getSize()) {
            log.info("Player last move has rolled out of board size. playerName: {}, newPosition: {}", playerName, newPosition);
            return;
        }

        Integer currPos = newPosition;
        log.debug("[NEW] Position before snakes or ladders. player: {}, pos: {}", playerName, currPos);
        while (board.isLandedOnSnake(currPos) || board.isLandedOnLadder(currPos)) {
            // If the currentPos contains either a ladder or a snake, the new position will be the end of that
            // snake or ladder
            if (board.isLandedOnSnake(currPos)) {
                currPos = board.getSnakeTail(currPos);
                continue;
            }

            if (board.isLandedOnLadder(currPos)) {
                currPos = board.getLadderEnd(currPos);
            }
        }

        log.info("{} rolled a {} and moved from {} to {}", playerName, diceRolledValue, board.getCurrentPositionForPlayer(playerName), currPos);

        log.debug("[NEW] Position after snakes or ladders. player: {}, pos: {}", playerName, currPos);
        if (currPos == board.getSize()) {
            board.setGameEnded(true);
            log.info("{} wins the game!", playerName);
        }

        // Update the current position
        board.updateCurrentPositionForPlayer(playerName, currPos);
    }


}
