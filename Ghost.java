import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Ghost extends Rectangle {

	/**
	 * 4 ghosts that chase pacman 
	 */
	
	private static final long serialVersionUID = 1L;
	private Color playerColor;
	private int directionIdx;
	
	public Ghost(int x, int y, int height, int width, Color color) {
		setBounds(x, y, width, height);
		this.playerColor = color;
		this.directionIdx = 0;
	}
	
	public void movePlayer(int dx, int dy) {
		// if ghost moves through tunnel, update x accordingly 
		if (this.x+dx < 0) {
			this.x = 18*20;
		} else if (this.x+dx >= 19*20) {
			this.x = 0;
		} else {
			this.x += dx;
		}
		this.y += dy;
	}
	
	public void setDirection(int direction) {
		this.directionIdx = direction;
	}
	
	public int getDirection() {
		return this.directionIdx;
	}
	
	public void setPlayerColor(Color color) {
		this.playerColor = color;
	}
	
	public void drawPlayer(Graphics2D g) {
		g.setColor(this.playerColor);
		g.fillRect(this.x, this.y, this.width, this.height);
	}

}
