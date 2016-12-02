package ico.maze;

import javax.swing.JFrame;

public class Main {
	
	public static final int marginX = 5;
	public static final int marginY = 29;
	
	public static void main(String[] args) {
        Interfaz interfaz = new Interfaz();
        // Generamos nuestra ventana.
        JFrame frame = new JFrame();
        // Le añadimos el JPanel que contiene la Interfaz.
        frame.setContentPane(interfaz);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Quita la barra superior de la ventana.
        frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);
        // El +1 es porque se come el borde de abajo y de la derecha.
        frame.setSize(interfaz.getMazeSize()*Interfaz.tamanoCelda+marginX , interfaz.getMazeSize()*Interfaz.tamanoCelda+marginY);
        // Centra la ventana.
        frame.setLocationRelativeTo(null);
	}
}
