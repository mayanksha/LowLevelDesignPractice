package com.practice.lld.driver;

import com.practice.lld.model.*;
import com.practice.lld.service.BoardGame;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {

    public void parseInputAndStartGame() throws Exception {
        InputStream inputStream = Driver.class.getClassLoader().getResourceAsStream("input.txt");
        Scanner scanner = new Scanner(inputStream);

        Integer boardSize = scanner.nextInt();

        Integer diceSize = scanner.nextInt();
        Dice dice = new Dice(diceSize);

        Integer numSnakes = scanner.nextInt();
        List<Snake> snakeList = new ArrayList<>();
        for(int i = 0; i < numSnakes; i++) {
            Integer head = scanner.nextInt(), tail = scanner.nextInt();
            snakeList.add(new Snake(head, tail));
        }

        Integer numLadders = scanner.nextInt();
        List<Ladder> ladderList = new ArrayList<>();
        for(int i = 0; i < numLadders; i++) {
            Integer start = scanner.nextInt(), end = scanner.nextInt();
            ladderList.add(new Ladder(start, end));
        }

        Integer numPlayers = scanner.nextInt();

        // Consume the raw line ending
        scanner.nextLine();

        List<Player> playerList = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++) {
            playerList.add(new Player(scanner.nextLine()));
        }

        Board board = new Board(boardSize, snakeList, ladderList, playerList, dice);

        BoardGame boardGame = new BoardGame(board);
        boardGame.start();
    }
}
