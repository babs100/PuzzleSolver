# PuzzleSolver

PuzzleSolver is an implementation of the A* search algorithm to solve an 8-puzzle on a 3 by 3 matrix board. 

Using Hamming and Manhattan distances.  We can measure how close a board is to the goal, we specify two notions of distance. 

### The Hamming distance
This is the distance betweeen a board and the goal by calculating the number of tiles in the wrong position. 

### The Manhattan distance
This is the distance between a board and the goal board using the sum of the Manhattan distances (i.e sum of the vertical and horizontal distance) from the tiles to their goal positions.