## Snake and Ladder game design

### Questions to ask -- 

* Should the size of board be kept extensible? If so, what are the limits we're looking at? Will the board be square?
* Should the dice's range of values be extensible? 
* What all attributes can a player have? Do we store the past user's history of their wins?
  * Normally we'll have Name at least as an attribute.

### Design

#### Entities

 1. **Board**
    1. at the start of a game, we should specify the size of the board to instantiate it, and we'll add the list of players playing the game.
    2. The board will also take up the set of ladders and snakes at the instantiation.
    3. 
    4. Considering the SOLID principles, we'll keep all our logic to move the
 2. **Player**
    1. Should have some kind of ID and a string Name
 3. **Snake**
    1. Should have "start" and "end" fields
 4. **Ladder**
    1. Should have "start" and "end" fields
 5. **Dice**
    1. Should be instantiated with a no. which denotes the size

Design -> 

Board should contain the
