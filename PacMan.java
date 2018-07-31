import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class PacMan extends Rectangle {

	/**
	 *  Pacman player piece
	 */
	
	private static final long serialVersionUID = 1L;
	private Color playerColor;

	public PacMan(int x, int y, int height, int width, Color playerColor) {
		setBounds(x, y, width, height);
		this.playerColor = playerColor;
	}
	
	public void movePlayer(int dx, int dy) {
		// if player goes through tunner, update x val accordingly
		if (this.x+dx < 0) {
			this.x = 18*20;
		} else if (this.x+dx >= 19*20) {
			this.x = 0;
		} else {
			this.x += dx;
		}
		this.y += dy;
	}
	
	public void drawPlayer(Graphics2D g) {
		g.setColor(this.playerColor);
		g.fillOval(this.x, this.y, this.width, this.height);
	}
}