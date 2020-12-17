import java.util.*;

//
class Board implements Comparable<Board>{
      public List<Board> children = new ArrayList<Board>();
      public static Map<String, Integer> closedPuzzles = new HashMap<String, Integer>();
      public Board parent;
      int col;
      int row;
      int len; 
      int distance;
      int priority;
      int blank;
      int inversion;
      public int[] puzzle;
      public String puzzleString;
      
      /**
       * Class Constructor
       * @param puzzle the initial puzzle
       * @param depth  the depth in the tree. 0 for root board
       */
      public Board(int[] puzzle, int depth){
        col = 3;
        row = 3;
        len = col * row;
        this.puzzle = new int[len];
        setPuzzle(puzzle);
        setInversion(puzzle);
        setPriorityWithManhatanDistance(puzzle, depth);
        //setPriorityWithHammingDistance(puzzle, depth);
        
        
      }

      /**
       * 
       * @return priority the priority of the board among it siblings in the game tree
       */
      public int getPriority(){
          return priority;
      }

      

      @Override
      public int compareTo(Board board){
        return this.getPriority() - board.getPriority();
      }

      /**
       * 
       * @param puzzle the initial puzzle for the board
       */
      public void setPuzzle(int[] puzzle){
        String str = "";
        for(int i = 0; i < puzzle.length; i++){
          this.puzzle[i] = puzzle[i];
          str += puzzle[i];
  
        }
        
        this.puzzleString = str;  
      }

      

      /**
       * Calculates the priority using the Manhantan distance 
       * plus the depth of the board in the GameTree.
       * Manhatan distance is the sum of the vertical and horizontal distance
       * from tiles to their goal position 
       * @param p puzzle
       * @param depth the depth in the game tree
       */
      public void setPriorityWithManhatanDistance(int[] p, int depth){
        // define a hashmap for goal tiles and row or column number
        HashMap<Integer, Integer> goalRows = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> goalCols = new HashMap<Integer, Integer>();
        goalRows.put(0, 0);
        goalRows.put(1, 0);
        goalRows.put(2, 0);

        goalRows.put(3, 1);
        goalRows.put(4, 1);
        goalRows.put(5, 1);

        goalRows.put(6, 2);
        goalRows.put(7, 2);
        goalRows.put(8, 2);

        goalCols.put(0, 0);
        goalCols.put(3, 0);
        goalCols.put(6, 0);

        goalCols.put(1, 1);
        goalCols.put(4, 1);
        goalCols.put(7, 1);

        goalCols.put(2, 2);
        goalCols.put(5, 2);
        goalCols.put(8, 2);

        
        int d = 0;
        int r,c;
        int gr, gc;
        int[] goal = {0,1,2,3,4,5,6,7,8};
        for(int i = 0; i < p.length; i++){
          if(goal[i] != p[i] && p[i] != 0){
            r = i / col;
            c = i % col;
            gr = goalRows.get(p[i]);
            gc = goalCols.get(p[i]);
            d += Math.abs(gr - r) + Math.abs(gc - c);
          }  
        }
        this.distance = d;
        this.priority = this.distance + depth;
      }

      public void setPriorityWithHammingDistance(int[] p, int n){
        // Hamming distance
        int d = 0;
        int[] goal = {0,1,2,3,4,5,6,7,8};
        for(int i = 0; i < p.length; i++){
          if(goal[i] != p[i]){
            ++d;
          }  
        }
        distance = d;
        priority = d + n;
          
      }
      /**
       * Determine if goal is reached 
       * @return true of false
       */
      public boolean isGoalPuzzle(){
        if(puzzle[0] != 0)
          return false;

        boolean isGoal = true;
        int k = puzzle[1];
        for(int i = 2; i < puzzle.length; i++){
          if (k > puzzle[i]){
              isGoal = false;
              break;
          } 
          k = puzzle[i];
        }
        return isGoal;
      }

      public String getPuzzleString(){
        return puzzleString;
      }
      
      /**
       * Makes a left move. since the puzzle is a flattened 2D array,
       * We determine if a left move is possible and make the move
       * @param p puzzle
       * @param i position of the blank space, represented by 0
       * @param depth the dept in the game tree
       */
      public void moveLeft(int[] p, int i, int depth){
        if(i % col > 0){
          int[] cp = new int[len];
          copyPuzzle(p, cp);
          int temp = cp[i - 1];
          cp[i - 1] = cp[i];
          cp[i] = temp;
          Board child = new Board(cp, depth);
          if(child.isSolvable()){
            this.children.add(child);
            child.parent = this;
          }
            

        }
      }

      /**
       * Makes a right move. since the puzzle is a flattened 2D array,
       * We determine if a right move is possible and make the move
       * @param p puzzle
       * @param i position of the blank space, represented by 0
       * @param depth the dept in the game tree
       */
      public void moveRight(int[] p, int i, int depth){
        if(i % col < col - 1){
          int[] cp = new int[len];
          copyPuzzle(p, cp);
          int temp = cp[i + 1];
          cp[i + 1] = cp[i];
          cp[i] = temp;
          Board child = new Board(cp, depth);
          if(child.isSolvable()){
            this.children.add(child);
            child.parent = this;
          }

        }
      }

      /**
       * Makes a up move. since the puzzle is a flattened 2D array,
       * We determine if a up move is possible and make the move
       * @param p puzzle
       * @param i position of the blank space, represented by 0
       * @param depth the dept in the game tree
       */
      public void moveUp(int[] p, int i, int depth){
        if(i - col >= 0){
          int[] cp = new int[len];
          copyPuzzle(p, cp);
          int temp = cp[i - col];
          cp[i - col] = cp[i];
          cp[i] = temp;
          Board child = new Board(cp, depth);
          if(child.isSolvable()){
            this.children.add(child);
            child.parent = this;
          }

        }
      }

      /**
       * Makes a down move. since the puzzle is a flattened 2D array,
       * We determine if a down move is possible and make the move
       * @param p puzzle
       * @param i position of the blank space, represented by 0
       * @param depth the dept in the game tree
       */
      public void moveDown(int[] p, int i, int depth){
        if(i + col < len){
          int[] cp = new int[len];
          copyPuzzle(p, cp);
          int temp = cp[i + col];
          cp[i + col] = cp[i];
          cp[i] = temp;
          Board child = new Board(cp, depth);
          if(child.isSolvable()){
            this.children.add(child);
            child.parent = this;
          }

        }
      }

      /**
       * make a copy of a puzzle
       * @param src source puzzle
       * @param dest destination puzzle
       */
      public void copyPuzzle(int[] src, int[] dest){
        for(int i = 0; i < src.length; i++)
          dest[i] = src[i];
      }

      /**
       * Print the puzzle for debugging
       */
      public void printPuzzle(){
        int k = 0;
        System.out.println();
        for(int i = 0; i < col; i++){
          for(int j = 0; j < col; j++){
            System.out.print(puzzle[k] + " ");
            k++;
          }
          System.out.println();
        }
        System.out.println("inversion = " + inversion);
      }

      /**
       * Compare if this puzzle is thesame with the supplied puzzle
       * @param p
       * @return
       */
      public boolean puzzleIsSame(int[] p){
        boolean isSame = true;
        for(int i = 0; i < len; i++){
          if(puzzle[i] != p[i])
            isSame = false;
        }
        
        return isSame;

      }

      /**
       * Determine the inversion of the puzzle
       * @param np puzzle
       */
      public void setInversion(int[] np){
          // define a puzzle with n-1 length from current board without space (zero)  
        int[] p = new int[len-1];
        int k = 0;
        for(int i = 0; i < np.length; i++){
          if(np[i] == 0) continue;
          p[k] = np[i];
          ++k;
        }

        // calculate the inversion.
        // inversion is the sum of the no of times in the sequence i,j,
        // where j comes before i e.g [1,2,4,3,5] = 1

        int inv = 0;
        for(int i = 0; i < p.length - 1; i++){
          for(int j = i+1; j < p.length; j++){
            if(p[i] > p[j])
            {
                ++inv;
            }
            
          }
        }
        this.inversion = inv;
      }

      /**
       * Deternine if the puzzle is solvable or not using inversion.
       * inversion is the sum of the no of times in the sequence i,j,
       * where j comes before i e.g [1,2,4,3,5] = 1
       * for a odd sized board (i.e n*n). The puzzle is solvable only when 
       * number of inversions is even  
       * @return true or false
       */
      public boolean isSolvable(){
        return (inversion % 2 == 0);
      }

      public boolean isVisited(){
        return (closedPuzzles.get(puzzleString) != null)? true:false;
      }
      
      /**
       * Initiate the moves
       * @param depth
       */
      public void makeMoves(int depth){
        for(int i = 0; i < puzzle.length; i++){
          if(puzzle[i] == 0)
            blank = i;
        }
        moveLeft(puzzle, blank, depth);
        moveRight(puzzle, blank, depth);
        moveUp(puzzle, blank, depth);
        moveDown(puzzle, blank, depth);
      }

  }

 // The main class  
class PuzzleSolver {
  
  
  
  

  public static void main(String[] args) {

    Board rootBoard;
    // A list to contains a goal board to it root in the game tree
    List<Board> goalPath = new ArrayList<Board>();

    // A list of boards to expand. just like a queue
    List<Board> expandableBoards = new ArrayList<Board>(); 
    // A list of visited boards
    List<Board> closedBoards = new ArrayList<Board>();
    
    
    
    
    //source
    /*
    int[][] initialState = {
        {1, 2, 4},
        {3, 0, 5},
        {7, 6, 8}
    };
    */

    /*
    int[][] initialState = {
        {1, 6, 3},
        {8, 7, 2},
        {4, 0, 5}
    };
    */


    /*
    int[][] initialState = {
        {0, 1, 3},
        {4, 2, 5},
        {7, 8, 6}
    }; 
    */
    
    /*
    // my
    int[][] initialState = {
        {1, 2, 5},
        {3, 7, 4},
        {6, 0, 8}
    };
    
    */
    
    /*
    //yt 23
    
    int[][] initialState = {
        {8, 6, 3},
        {0, 7, 1},
        {2, 4, 5}
    };
    */
    
    

    
    //other 20
    int[][] initialState = {
        {2, 7, 1},
        {6, 3, 5},
        {0, 4, 8}
    };
    
    
    /*
     //unsolvable -1
    int[][] initialState = {
      {1, 2, 3},
      {4, 5, 6},
      {8, 0, 7}
    };
    */
    

    /*
    // 30 moves
    int[][] initialState = {
        {0, 1, 3},
        {4, 2, 5},
        {7, 8, 6}
      };
    */
    
    // flatten the 2D array into 1D array for the puzzle;
    // to improve performance
    int cols = initialState[0].length;
    int rows = initialState.length;
    int l = cols * rows;
    int[] puzzle = new int[l];
    int k = 0;
    for(int i = 0; i < rows; i++){
      for(int j = 0; j < cols; j++){
          puzzle[k] = initialState[i][j];
          k++;
      }
    }

    // create the root board
    rootBoard = new Board(puzzle, 0);

    // check if the root board is already in goal state
    // then no move is made (0 moves)
    if(rootBoard.isGoalPuzzle()){
      rootBoard.printPuzzle();
      System.out.println("0 moves");
      System.exit(0);
    }
    
    // check if a board is not solvable. 
    if(!rootBoard.isSolvable()){
        rootBoard.printPuzzle();
        System.out.println("-1");
        System.exit(0);
      }
    
      
    expandableBoards.add(rootBoard);
    boolean goalFound = false;
    int depth = 1;

    System.out.println("please wait...");
    int boardCount = 1;

    while(expandableBoards.size() > 0 && !goalFound)
    {

      Collections.sort(expandableBoards);  
      Board currentBoard = expandableBoards.get(0);
      expandableBoards.remove(0);
      //closedBoards.add(currentBoard);
      Board.closedPuzzles.put(currentBoard.getPuzzleString(), 1);
      currentBoard.makeMoves(depth);
      currentBoard.printPuzzle();
      //Collections.sort(currentBoard.children);

      ++boardCount;
      for(Board currentChild: currentBoard.children){

        if(currentChild.isGoalPuzzle()){
          goalFound = true;
          tracePath(goalPath, currentChild);
          System.out.println("Solution found!");
          System.out.println("Expandable board size = " + expandableBoards.size());
          break;
        }
        

        if(!listContains(expandableBoards, currentChild) &&
          !currentChild.isVisited()
        ){
            if(currentChild.isSolvable()){
              expandableBoards.add(currentChild);
            }
                
        }
      }
      
      ++depth;
    }

    if(goalFound){
      System.out.println("moves = " + (goalPath.size() - 1));
      Collections.reverse(goalPath);
      for(Board board : goalPath)
          board.printPuzzle();
      System.out.println("Board count = " + boardCount);  
    }
    else{
      System.out.println("-1");
    }

    
  }

  // this fuction traces a board to its root in the game tree
  public static void tracePath(List<Board> path, Board board){
    Board currentBoard = board;
    path.add(currentBoard);
    while(currentBoard.parent != null){
      currentBoard = currentBoard.parent;
      path.add(currentBoard);
    }
  }

  // checks if a board already exist in a list and returns true 
  // if exist. otherwise, returns false 
  public static boolean listContains(List<Board> list,Board item){
      boolean contains = false;

      for(Board board: list){
        if(board.puzzleIsSame(item.puzzle)){
          contains = true;

        }
        
      }
      return contains;
    }
}