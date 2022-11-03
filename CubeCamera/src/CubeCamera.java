
import javax.swing.JFrame;
import javax.swing.JOptionPane;

// TODO
// Make camera move like minecraft
// Make wireframe ground
// Clean up code into more objects
// rotate vector by angle?

public class CubeCamera
{
	public static void main(String args[])
	{
		CubeStuff game = new CubeStuff(Integer.parseInt(JOptionPane.showInputDialog("Side Length")));
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setTitle("Square");
		game.pack();
		// game.show();
	}
}