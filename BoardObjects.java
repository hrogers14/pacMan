import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class BoardObjects extends Rectangle{
	
	/**
	 * Objects placed on game board
	 */
	private static final long serialVersionUID = 1L;
	private ObjectType objType;
	private boolean objVisible;
	
	public BoardObjects(int x, int y, int width, int height, ObjectType type) {
		setBounds(x, y, width, height);
		this.objType = type;
		this.objVisible = true;
	}
	
	public ObjectType getObjectType() {
		return this.objType;
	}
	
	public void setObjectType(ObjectType type) {
		this.objType = type;
	}
	
	public boolean getObjVisible() {
		return this.objVisible;
	}
	
	public void setObjVisible(boolean visible) {
		this.objVisible = visible;
	}
	
	public void drawObject(Graphics g) {
		if (this.objVisible) {
			switch (this.objType) {
			case DOT:
				g.setColor(Color.white);
				g.fillOval(this.x+8, this.y+8, 4, 4);
				break;
			case ENERGIZER:
				g.setColor(Color.white);
				g.fillOval(this.x+4, this.y+4, 8, 8);
				break;
			case WALL:
				g.setColor(Color.blue);
				g.fillRect(this.x, this.y, width, height);
				break;
			default:
				break;
			}
		}
	}
}
