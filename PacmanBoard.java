import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PacmanBoard extends JPanel implements KeyListener, ActionListener {
	/**
	 *  Pacman board containing all game objects
	 */

	private static final long serialVersionUID = 1L;
	private final int BLOCK_SIZE = 20;
	private final int NUM_X_BLOCKS = 19;
	private final int NUM_Y_BLOCKS = 23;
	private final int NUM_GHOSTS = 4;
	private final Color[] GHOSTS_COLORS = {Color.red, Color.cyan, Color.pink, Color.orange};

	private final int PACMAN_START_X = 9;
	private final int PACMAN_START_Y = 17;
	private final int[] GHOSTS_START_X = {9, 8, 10, 9};
	private final int[] GHOSTS_START_Y = {7, 9, 9, 10};
	private final int[] GHOST_SCATTER_X_CORDS = {NUM_X_BLOCKS, 0, NUM_X_BLOCKS, 0};
	private final int[] GHOST_SCATTER_Y_CORDS = {0, 0, NUM_Y_BLOCKS, NUM_Y_BLOCKS};
	private final int TIMER_SPEED = 10;
	private final int ONE_SECOND_TICK = 1000/TIMER_SPEED; 
	private final int TOTAL_DOTS = 185;
	private int[] X_DIRS = {1,0,-1,0};
	private int[] Y_DIRS = {0,1,0,-1};
	
	private final int[][] BOARD_OBJS = new int[][] {
		{1,1,1,1,1,1,1,0,0,1,0,1,0,0,1,1,1,1,1,1,1,1,1},
		{1,2,3,2,2,2,1,0,0,1,0,1,0,0,1,2,2,3,1,2,2,2,1},
		{1,2,1,2,1,2,1,0,0,1,0,1,0,0,1,2,1,2,2,2,1,2,1},
		{1,2,1,2,1,2,1,1,1,1,0,1,1,1,1,2,1,1,1,2,1,2,1},
		{1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,2,1},
		{1,2,1,2,1,1,1,1,1,1,2,1,1,1,1,2,1,2,1,1,1,2,1},
		{1,2,1,2,2,2,1,2,2,2,2,2,2,2,2,2,1,2,2,2,1,2,1},
		{1,2,1,2,1,2,1,2,1,1,1,1,1,2,1,2,1,2,1,2,1,2,1},
		{1,2,2,2,1,2,2,2,1,0,0,0,1,2,1,2,2,2,1,2,2,2,1},
		{1,1,1,2,1,1,1,2,1,0,0,0,1,2,1,1,1,2,1,1,1,2,1},
		{1,2,2,2,1,2,2,2,1,0,0,0,1,2,1,2,2,2,1,2,2,2,1},
		{1,2,1,2,1,2,1,2,1,1,1,1,1,2,1,2,1,2,1,2,1,2,1},
		{1,2,1,2,2,2,1,2,2,2,2,2,2,2,2,2,1,2,2,2,1,2,1},
		{1,2,1,2,1,1,1,1,1,1,2,1,1,1,1,2,1,2,1,1,1,2,1},
		{1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,2,1},
		{1,2,1,2,1,2,1,1,1,1,0,1,1,1,1,2,1,1,1,2,1,2,1},
		{1,2,1,2,1,2,1,0,0,1,0,1,0,0,1,2,1,2,2,2,1,2,1},
		{1,2,3,2,2,2,1,0,0,1,0,1,0,0,1,2,2,3,1,2,2,2,1},
		{1,1,1,1,1,1,1,0,0,1,0,1,0,0,1,1,1,1,1,1,1,1,1}
	};
	
	private int ghostMode = 0; // variable to determine if ghost is scattering or chasing
	private int ghostPenReleaseTicks = 6000/TIMER_SPEED;  //rate at which ghosts are released
	// [time ghosts in chase mode, time ghosts in scatter mode, time ghosts in frightened mode]
	private int[] ghostTicks = {12000/TIMER_SPEED, 10000/TIMER_SPEED, 8000/TIMER_SPEED}; 
	private boolean ghostsFrightened = false;
	private int ghostModeTimer = 0; // timer to determine when ghost mode should change
	private int frightenedTimer = 0; // timer to count time in frightened mode
	private int ghostsMoveTimer = 0; // timer to determine when to move ghosts
	private int ghostSpeed = 20; // number of ticks before moving ghosts
	LinkedList<Rectangle> ghostsInPen = new LinkedList<Rectangle>(); // queue to hold ghosts in pen
	private int penTimer = 0; // timer for releasing ghosts from pen
	
	private int pacManCurKeyDir = 1; // last key pressed direction
	private int pacManCurMoveDir = 1; // direction pacman is moving in
	private int pacManMoveTimer = 0; // timer to determine when to move pacman
	private int pacManSpeed = 17; // number of ticks before moving pacman
	
	// variable for doing countdown before starting game
	private int countDownTimer = 0;
	private boolean countDownRunning = false;
	private int countDownNum = 3;
	
	private int score = 0;
	private int lives = 3;
	private int level = 1;
	private int dotsEaten = 0;
	
	PacMan pacman = new PacMan(PACMAN_START_X*BLOCK_SIZE,PACMAN_START_Y*BLOCK_SIZE,BLOCK_SIZE, BLOCK_SIZE, Color.yellow);
	ArrayList<Ghost> ghostsArr = new ArrayList<Ghost>();
	Timer timer = new Timer(TIMER_SPEED, this);
	BoardObjects[][] boardObjArr = new BoardObjects[NUM_X_BLOCKS][NUM_Y_BLOCKS];
	
	public PacmanBoard() {
		addKeyListener(this);
		setBackground(Color.black);
		setFocusable(true);
		
		// initialize all board objects
		initBoardObjects();
		
		// initializes all four ghosts
		for (int ghostIdx = 0; ghostIdx < NUM_GHOSTS; ghostIdx++) {
			ghostsArr.add(new Ghost(GHOSTS_START_X[ghostIdx]*BLOCK_SIZE,GHOSTS_START_Y[ghostIdx]*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, GHOSTS_COLORS[ghostIdx]));
		}
		
		timer.start();
		setupLevel();
	}
	
	public void initBoardObjects() {
		// place all board objects in the correct locations
	    	for (int x = 0; x < NUM_X_BLOCKS; x++) {
	    		for (int y = 0; y < NUM_Y_BLOCKS; y++) {
	    			boardObjArr[x][y] = new BoardObjects(x*BLOCK_SIZE, y*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, ObjectType.getObjectType(BOARD_OBJS[x][y]));
	    		}
	    	}
	}
	
	public void startCountDown() {
		// count down before starting
		countDownNum = 3;
		countDownRunning = true;
		countDownTimer = 0;
	}
	
	public void setupLevel() {
    	
		if (ghostsFrightened) {
			ghostFrightenedEnable(false);
		}
		
		if (lives > 0) {
			startCountDown();
	    		ghostsInPen.clear();
	    		// re-init the ghosts location, placing 3 ghosts in pen
	    		for (int ghostIdx = 0; ghostIdx < ghostsArr.size(); ghostIdx++) {
	    			ghostsArr.get(ghostIdx).setLocation(GHOSTS_START_X[ghostIdx]*BLOCK_SIZE, GHOSTS_START_Y[ghostIdx]*BLOCK_SIZE);
	    			ghostsArr.get(ghostIdx).setDirection(0);
	    			if (ghostIdx > 0) {
	    				ghostsInPen.addLast(ghostsArr.get(ghostIdx));
	    			}
	    		}
	    		
	    		// set pacman location and direction
	    		pacman.setLocation(PACMAN_START_X*BLOCK_SIZE, PACMAN_START_Y*BLOCK_SIZE);
	    		pacManCurKeyDir = 1;
	    		pacManCurMoveDir = 1;
	    		
	    		// re-init ghost timer, and set ghost mode to chase
	    		ghostModeTimer = 0;
	    		ghostMode = 0;
		} else {
			// game over, stop timer report game over and exit game
			timer.stop();
			JOptionPane.showMessageDialog(this, "You lose!", "Game Over", JOptionPane.YES_NO_OPTION);
			System.exit(ABORT);
		}
	}
    
    public void nextLevel() {
    		level++;
    		dotsEaten = 0;
    		// set all dots/energizers visible again
    		for (int x = 0; x < NUM_X_BLOCKS; x++) {
	    		for (int y = 0; y < NUM_Y_BLOCKS; y++) {
	    			boardObjArr[x][y].setObjVisible(true);
	    		}
    		}
    		
    		// increase ghost movement speed after level 3 until speed is at 16
    		if ((level > 2) && (ghostSpeed > 16))
    			ghostSpeed--;
    		
    		// release ghosts from pen faster as level increase
    		if (ghostPenReleaseTicks > 2000/TIMER_SPEED)
    			ghostPenReleaseTicks -= 50;
    		
    		// decrease the time that ghosts stay in scatter mode as level increases
    		if (ghostTicks[1] > 2000/TIMER_SPEED)
	    		ghostTicks[1] -= 50;
    		
    		// decrease the time that ghsots stay in frightened mode as level increases
    		if (ghostTicks[2] > 2000/TIMER_SPEED)
	    		ghostTicks[2] -= 50;
    }
	
    public void updateStats(Graphics2D g) {
    		g.setColor(Color.blue);
    		// draw score, number lives left, and level on board
    		g.drawString("SCORE:" + score, BLOCK_SIZE, (NUM_Y_BLOCKS+1)*BLOCK_SIZE);
    		g.drawString("LIVES:" + lives, (NUM_X_BLOCKS-3)*BLOCK_SIZE, (NUM_Y_BLOCKS+1)*BLOCK_SIZE);
    		if (countDownRunning) {
    			g.drawString(""+countDownNum, NUM_X_BLOCKS*BLOCK_SIZE/2, (NUM_Y_BLOCKS+1)*BLOCK_SIZE);
    		} else {
    			g.drawString("LEVEL:"+level, (NUM_X_BLOCKS-2)*BLOCK_SIZE/2, (NUM_Y_BLOCKS+1)*BLOCK_SIZE);
    		}
    }
	
	public boolean directionValid(int x, int y, int dx, int dy) {
		// if new location is out of bounds, player is going through tunnel so return true
		if ( ( (x+dx < 0) || (x+dx >= NUM_X_BLOCKS) ) ) {
			return true;
		}
		// if new location is a wall, return false
		if (boardObjArr[x+dx][y+dy].getObjectType() == ObjectType.WALL) {
			return false;
		}
		return true;
	}
	
	public int playerEnteringTunnel(int x) {
		// determine newX location if player is going through the tunnel
		if ( ( (x < 0) || (x >= NUM_X_BLOCKS) ) ) {
			return (x < 0) ? NUM_X_BLOCKS-1 : 0;
		} 
		return x;
	}
	
	public int ghostFrightened(int curX, int curY, int curDir) {
		int validDirs = 0;
		int direction;
		boolean isOppositeDir;
		int[] validDirsArr = new int[4];
		// find all valid directions that ghost can go from current location
		for (direction = 0; direction < 4; direction++) {
			isOppositeDir = ( (X_DIRS[direction] == (~X_DIRS[curDir]+1) ) && (Y_DIRS[direction] == (~Y_DIRS[curDir]+1) ) );
			if (directionValid(curX, curY, X_DIRS[direction], Y_DIRS[direction]) && !isOppositeDir) {
				validDirsArr[validDirs] = direction;
				validDirs++;
			}
		}
		// randomly select a direction from all valid directions 
		int randDirIdx = new Random().nextInt(validDirs);
		return validDirsArr[randDirIdx];
	}
	
	public int ghostChase(int curX, int curY, int curDir, int targetX, int targetY) {
		int direction;
		LinkedList<Point> pathsToCheck = new LinkedList<Point>();
		
		// visitedArr determines which blocks have been considered in finding shortest path
		// when a block is "visited" the value stored will be the original direction the ghost would move in to get there
		int[][] visitedArr = new int[NUM_X_BLOCKS][NUM_Y_BLOCKS];
		for (int x = 0; x < NUM_X_BLOCKS; x++) {
			for (int y = 0; y < NUM_Y_BLOCKS; y++) {
				visitedArr[x][y] = -1; //mark all blocks as unvisited
			}
		}
		
		int newX; 
		int newY;
		boolean isOppositeDir;
		// check each possible direction, and if the direction is valid, add it to the queue of paths to check, keeping track of original direction
		for (direction = 0; direction < 4; direction++) {
			isOppositeDir = ( (X_DIRS[direction] == (~X_DIRS[curDir]+1) ) && (Y_DIRS[direction] == (~Y_DIRS[curDir]+1) ) );
			// check if player is entering tunnel, and update x val accordingly
			newX = playerEnteringTunnel(curX+X_DIRS[direction]);
			newY = curY+Y_DIRS[direction];
			if (directionValid(curX, curY, X_DIRS[direction], Y_DIRS[direction]) && !isOppositeDir) {
				pathsToCheck.addLast(new Point(newX, newY));
				visitedArr[newX][newY] = direction;
			}
		}
		
		int curDirection;
		double distance;
		double shortestDist = 100;
		Point shortestLoc = new Point();
		Point curPath = new Point();
		while (pathsToCheck.size() > 0) {
			// check each possible path that could be taken
			curPath = pathsToCheck.removeFirst();
			curDirection = visitedArr[curPath.x][curPath.y];
			// check the distance of the current block to the target
			distance = Math.sqrt((targetX-curPath.x)*(targetX-curPath.x) + (targetY-curPath.y)*(targetY-curPath.y));
			if (distance < shortestDist) {
				// keep track of block with shortest distance to target
				shortestDist = distance;
				shortestLoc = curPath;
			}
			// look through all the next possible directions, and add valid paths to the pathsToCheck
			for (direction = 0; direction < 4; direction++) {
				newX = playerEnteringTunnel((int) curPath.getX() + X_DIRS[direction]);
				newY = (int)curPath.getY() + Y_DIRS[direction];
				if (directionValid((int)curPath.getX(), (int)curPath.getY(), X_DIRS[direction], Y_DIRS[direction]) && (visitedArr[newX][newY] == -1)) {
					pathsToCheck.addLast(new Point(newX, newY));
					visitedArr[newX][newY] = curDirection;
				}
			}
		}
		// return the direction that had the shortest distance from the target
		return visitedArr[shortestLoc.x][shortestLoc.y];
	}

	@Override 
	public void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintBoard(g);
    }

    private void paintBoard(Graphics g) {

    		Graphics2D g2d = (Graphics2D) g;
        
    		// draw board objects, pacman, and ghosts
    		pacman.drawPlayer(g2d);
    	    	for (int x = 0; x < NUM_X_BLOCKS; x++) {
    	    		for (int y = 0; y < NUM_Y_BLOCKS; y++) {
    	    			boardObjArr[x][y].drawObject(g);
    	    		}
    	    	}
        for (int ghostIdx = 0; ghostIdx < ghostsArr.size(); ghostIdx++) {
        		ghostsArr.get(ghostIdx).drawPlayer(g2d);
        }
        
        updateStats(g2d);
    }
	
	@Override
	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		// change pacmans direction based off of the keys pressed
		switch(e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			pacManCurKeyDir = 0;
			break;
		case KeyEvent.VK_DOWN:
			pacManCurKeyDir = 1;
			break;
		case KeyEvent.VK_LEFT:
			pacManCurKeyDir = 2;
			break;
		case KeyEvent.VK_UP:
			pacManCurKeyDir = 3;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	// enable ghost frightened mode
	public void ghostFrightenedEnable(boolean enable) {
		ghostsFrightened = enable;
		for (int ghostIdx = 0; ghostIdx < ghostsArr.size(); ghostIdx++) {
			ghostsArr.get(ghostIdx).setPlayerColor(enable ? Color.darkGray : GHOSTS_COLORS[ghostIdx]);
		}
		frightenedTimer = 0;
	}
	
	public void checkCollision() {
		Ghost curGhost;
		// check if any of the ghosts collided with pacman 
		for (int ghostIdx = 0; ghostIdx < ghostsArr.size(); ghostIdx++) {
			curGhost = ghostsArr.get(ghostIdx);
			if (pacman.intersects(curGhost)) {
				if (ghostsFrightened) { // in frightened mode, send ghosts back to pen
					ghostsInPen.addLast(curGhost);
					curGhost.setLocation(9*BLOCK_SIZE, 9*BLOCK_SIZE);
					score += 200;
				} else { // if regular mode, life lost
					lives--;
					setupLevel();
				}
			}
		}
	}
	
	// determine the target that the ghosts should be chasing dependent on ghost type and ghost mode
	public Point getChaseTarget(int ghostIdx) {
		Point targetLoc = new Point();
		switch(ghostIdx) {
		case 0: //blinky - moves directly for pacMan
			targetLoc.x = pacman.x/BLOCK_SIZE;
			targetLoc.y = pacman.y/BLOCK_SIZE;
			break;
		case 1: //inky - 1 tile ahead of pacman * distance from blinky to pacman
			int blinkyXDist = Math.abs((int) (pacman.getX() - ghostsArr.get(0).getX()))/BLOCK_SIZE;
			int blinkyYDist = Math.abs((int) (pacman.getY() - ghostsArr.get(0).getY()))/BLOCK_SIZE;
			targetLoc.x = pacman.x/BLOCK_SIZE + blinkyXDist * X_DIRS[pacManCurMoveDir];
			targetLoc.y = pacman.y/BLOCK_SIZE + blinkyYDist * Y_DIRS[pacManCurMoveDir];
			break;
		case 2: //pinky - 3 tiles above pacman; if pacman is moving upward target is also 3 tiles to the left
			int extraX = (pacManCurMoveDir == 1) ? 3 : 0;
			targetLoc.x = pacman.x/BLOCK_SIZE + 3*X_DIRS[pacManCurMoveDir] - extraX;
			targetLoc.y = pacman.y/BLOCK_SIZE + 3*Y_DIRS[pacManCurMoveDir];
			break;
		case 3: //clyde - if within 6 tiles of pacman, scatter; otherwise target pacmans location
			if ((Math.abs(ghostsArr.get(ghostIdx).x - pacman.x) <= 6*BLOCK_SIZE) || (Math.abs(ghostsArr.get(ghostIdx).y - pacman.y) <= 6*BLOCK_SIZE)) {
				targetLoc.x = 0;
				targetLoc.y = NUM_Y_BLOCKS;
			} else {
				targetLoc.x = pacman.x/BLOCK_SIZE;
				targetLoc.y = pacman.y/BLOCK_SIZE;
			}
			break;
		}
		return targetLoc;
	}
	
	public void moveGhosts() {
		int direction;
		Ghost curGhost;
		for (int ghostIdx = 0; ghostIdx < ghostsArr.size(); ghostIdx++) {
			curGhost = ghostsArr.get(ghostIdx);
			if (ghostsFrightened) {
				direction = ghostFrightened(curGhost.x/BLOCK_SIZE, curGhost.y/BLOCK_SIZE, ghostsArr.get(ghostIdx).getDirection());
			} else {
				if (ghostMode == 0) { // chase mode
					Point target = getChaseTarget(ghostIdx);
					direction = ghostChase(curGhost.x/BLOCK_SIZE, curGhost.y/BLOCK_SIZE, ghostsArr.get(ghostIdx).getDirection(), target.x, target.y);
				} else { //scatter mode
					direction = ghostChase(curGhost.x/BLOCK_SIZE, curGhost.y/BLOCK_SIZE, ghostsArr.get(ghostIdx).getDirection(), GHOST_SCATTER_X_CORDS[ghostIdx], GHOST_SCATTER_Y_CORDS[ghostIdx]);
				}
			}
			curGhost.movePlayer(X_DIRS[direction]*BLOCK_SIZE, Y_DIRS[direction]*BLOCK_SIZE);
			ghostsArr.get(ghostIdx).setDirection(direction);
		}
		checkCollision();
	}
	
	public void movePacMan() {
		if (directionValid(pacman.x/BLOCK_SIZE, pacman.y/BLOCK_SIZE, X_DIRS[pacManCurKeyDir], Y_DIRS[pacManCurKeyDir])) {
			pacManCurMoveDir = pacManCurKeyDir; // only change pacmans direction if the last key pressed is a valid move
		}
		if (directionValid(pacman.x/BLOCK_SIZE, pacman.y/BLOCK_SIZE, X_DIRS[pacManCurMoveDir], Y_DIRS[pacManCurMoveDir])) {
			pacman.movePlayer(X_DIRS[pacManCurMoveDir]*BLOCK_SIZE, Y_DIRS[pacManCurMoveDir]*BLOCK_SIZE);
			BoardObjects curBoardObj = boardObjArr[(int) (pacman.getX()/BLOCK_SIZE)][(int) pacman.getY()/BLOCK_SIZE];
			if (curBoardObj.getObjVisible()) {
				// if pacman intersects with a dot or energizer, update the board accordingly
				if (curBoardObj.getObjectType() == ObjectType.DOT) {
					curBoardObj.setObjVisible(false);
					dotsEaten++;
					score += 10;
				} else if (curBoardObj.getObjectType() == ObjectType.ENERGIZER) {
					curBoardObj.setObjVisible(false);
					dotsEaten++;
					score += 100;
					ghostFrightenedEnable(true);
				}
			}
			if (dotsEaten >= TOTAL_DOTS) {
				nextLevel();
				setupLevel();
			}
			checkCollision();
		}
	}
	
	public void ghostPenRelease() {
		Rectangle removedGhost = ghostsInPen.removeFirst();
		removedGhost.setLocation(9*BLOCK_SIZE, 7*BLOCK_SIZE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (countDownRunning) {
			if (countDownTimer > ONE_SECOND_TICK) {
				countDownNum--;
				countDownTimer = 0;
				if (countDownNum == 0) {
					countDownRunning = false;
				}
			}
			countDownTimer++;
		} else { 
			if (ghostsFrightened){
				frightenedTimer++;
				if (frightenedTimer > ghostTicks[2]) {
					ghostFrightenedEnable(false);
				}
			} else {
				ghostModeTimer++;
				if (ghostModeTimer > ghostTicks[ghostMode]) {
					ghostMode = (ghostMode + 1) & 0x1; // switch between chase and scatter mode
					ghostModeTimer = 0;
				}
			}
			
			// check if it is time to move pacman
			if (pacManMoveTimer > pacManSpeed) {
				movePacMan();
				pacManMoveTimer = 0;
			}
			pacManMoveTimer++;
			
			// check if it is time to move ghosts
			if (ghostsMoveTimer > ghostSpeed) {
				moveGhosts();
				ghostsMoveTimer = 0;
			}
			ghostsMoveTimer++;
			
			// check if a ghost should be released from the pen
			if (ghostsInPen.size() > 0) {
				if (penTimer > ghostPenReleaseTicks) {
					ghostPenRelease();
					penTimer = 0;
				}
				penTimer++;
			}
		}
		
		repaint();
		
	}
}