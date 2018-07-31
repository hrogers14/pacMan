import javax.swing.JFrame;

public class PacmanGame extends JFrame {
	/**
	 * Frame to initialize pacman game 
	 */
	
	private static final long serialVersionUID = 1L;

	public PacmanGame() {
		initGame();
	}
	
	private void initGame() {
		add(new PacmanBoard());
		setSize(380, 510);
		
		setTitle("Pacman");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
	}
	
	public static void main(String[] args) {
		PacmanGame game = new PacmanGame();
		game.setVisible(true);
	}

}