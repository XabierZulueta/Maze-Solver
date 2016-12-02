package ico.maze;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ico.maze.Laberinto.Busqueda;

import java.awt.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


@SuppressWarnings("serial")
public class Interfaz extends JPanel implements MouseListener, KeyListener {

	public final static int tamanoCelda = 70;
	
	private int mazeSize = 4;	
    private Laberinto laberinto;

    private DisplayPanel displayPanel;
    private DisplayPanel displayPanel1;
    private DisplayPanel displayPanel2;
    private DisplayPanel displayPanel3;
    private Celda.Type ultimaPosicionClick;

    public Interfaz() {

        this.setLayout(new BorderLayout());
        try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        /**
         *  Recogemos el tamaño de nuestro laberinto mediante un JOptionPane.
         */
        String dialog = "4";
        dialog = JOptionPane.showInputDialog("Introduzca el tamaño del laberinto [4,10] : ", dialog);
        mazeSize = Integer.parseInt(dialog);
        if(mazeSize<4)
        	mazeSize=4;
        if(mazeSize>10)
        	mazeSize=10;
        
        /**
         *  Pasamos el tamaño del laberinto para crearlo. 
         */
        this.laberinto = new Laberinto(mazeSize);
        
        /**
         * Generamos la barra de pestañas.
         */
        JTabbedPane tabbedPane = new JTabbedPane();
        this.displayPanel = new DisplayPanel(laberinto);
        this.displayPanel1 = new DisplayPanel(laberinto);
        this.displayPanel2 = new DisplayPanel(laberinto);
        this.displayPanel3 = new DisplayPanel(laberinto);
        
        /**
         * Añadimos los listeners.
         */
        displayPanel.addMouseListener(this);
        displayPanel.addKeyListener(this);
        displayPanel1.addKeyListener(this);
        displayPanel2.addKeyListener(this);
        displayPanel3.addKeyListener(this);
        ultimaPosicionClick = null;
        
        /**
         * Añadimos los paneles y las pestañas.
         */
        tabbedPane.addTab("Laberinto",displayPanel);
        tabbedPane.addTab("Dijkstra",displayPanel1);  
        tabbedPane.addTab("Avara",displayPanel2);
        tabbedPane.addTab("A*",displayPanel3);
        
        /**
         * Dependiendo de la pestaña que pulsemos hace una cosa u otra.
         */
        tabbedPane.addChangeListener(new ChangeListener() { //add the Listener

            public void stateChanged(ChangeEvent e) {
                switch(tabbedPane.getSelectedIndex())
                {
                	case 0:
                		laberinto.limpiarLaberinto();
                		displayPanel.repaint();
                		break;
                	case 1:
                		
                		laberinto.setTypeBusqueda(Laberinto.Busqueda.DIJKSTRA);
                		laberinto.resetearFichero(Busqueda.DIJKSTRA);
                		laberinto.limpiarLaberinto();
                    	laberinto.procesarBusqueda();
                    	displayPanel.repaint();
                    	break;
                	case 2:
                		laberinto.setTypeBusqueda(Laberinto.Busqueda.AVARA);
                		laberinto.resetearFichero(Busqueda.AVARA);
                		laberinto.limpiarLaberinto();
                    	laberinto.procesarBusqueda();
                    	displayPanel2.repaint();
                    	break;
                	case 3:
                		laberinto.setTypeBusqueda(Laberinto.Busqueda.ASTAR);
                		laberinto.resetearFichero(Busqueda.ASTAR);
                    	laberinto.limpiarLaberinto();
                    	laberinto.procesarBusqueda();
                    	displayPanel3.repaint();
                    	break;

                }
            }
        });
        this.add(tabbedPane);
    }
        
    public void keyPressed(KeyEvent e) { 
    	switch(e.getKeyCode())
    	{
    		/**
    		 *  Si pulsa R se reinicia la pestaña Laberinto.
    		 */
    		case KeyEvent.VK_R:
    			laberinto.limpiarLaberinto();
    			displayPanel.repaint();
    			break;
    		// Paso a paso.
    		//case KeyEvent.VK_SPACE:
	    		//break;
	    	/**
	    	 *  Cierra el programa con la tecla ESC desde cualquier pestaña.
	    	 */
            case KeyEvent.VK_ESCAPE:
				System.exit(1);
				break;
			/**
			 * Muestra las heurísticas en la pestaña Laberinto.
			 */
            case KeyEvent.VK_H:
            	laberinto.calcularHeuristicas();
            	displayPanel.repaint();
            	break;
    	}
    }
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) { }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    	/**
    	 *  Obtenemos la celda que ha sido clickada.
    	 */
        Celda celda = laberinto.getPosiciones()[e.getY() / tamanoCelda][e.getX() / tamanoCelda];
        if(ultimaPosicionClick == null){
        	ultimaPosicionClick = celda.getType();
        }else{
	        /**
	         *  En caso de que la celda pulsa sea tipo GOAL llamamos a cambiarFinal.
	         */
	        if (ultimaPosicionClick.equals(Celda.Type.FINAL)) {
	        	laberinto.cambiarFinal(celda);
	            ultimaPosicionClick = Celda.Type.CAMPO;
	
	        }
	        /**
	         *  En caso de que la celda pulsa sea tipo START llamamos a cambiarInicio.        
	         */
	        else if (ultimaPosicionClick.equals(Celda.Type.INICIO)) {
	        	laberinto.cambiarInicio(celda);
	            ultimaPosicionClick = Celda.Type.CAMPO;
	
	        }
	        /**
	         *  Como último caso, el resto de celdas cambian mediante cambiarCelda.
	         */
	        else{
	        	laberinto.cambiarCelda(celda);
	            ultimaPosicionClick = celda.getType();
	        }
        }
        /**
         *  Actualiza el estado de la interfaz del laberinto.
         */
        displayPanel.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public class DisplayPanel extends JPanel {

        private Laberinto laberinto;

        public DisplayPanel(Laberinto laberinto) {
            this.laberinto = laberinto;
        }

        public void setLaberinto(Laberinto laberinto) {
            this.laberinto = laberinto;
        }

        public void paintComponent(Graphics g) {

        	requestFocus();
            int sqsize = tamanoCelda;
            int yoffset;
            int xoffset;

            for (int i = 0; i < laberinto.getsize(); i++) {
                for (int j = 0; j < laberinto.getsize(); j++) {

                    Celda celda = laberinto.getPosiciones()[i][j];
                    yoffset = sqsize * i;
                    xoffset = sqsize * j;
                    
                    /**
                     *  Coloreamos las celdas dependiendo del tipo.
                     */
                    switch (celda.getType()) {
                        case INICIO:
                        	/**
                        	 *  Color del fondo del cuadrado.
                        	 */
                            g.setColor(new Color(0,153,0));
                            /**
                             *  Dibujamos el cuadrado.
                             */
                            g.fillRect(xoffset, yoffset, sqsize, sqsize);
                            /**
                             *  Color del borde.
                             */
                            g.setColor(Color.blue);
                            /**
                             *  Dibujamos el borde.
                             */
                            g.drawRect(xoffset, yoffset, sqsize, sqsize);
                            continue;
                        case FINAL:
                        	/**
                        	 *  Color del fondo del cuadrado.
                        	 */
                            g.setColor(Color.red);
                            break;
                        // De momento no hay diferencia entre sucesor, cerrado y grass.
                       // case CERRADO:
                        //case SUCESOR:
                        case CAMPO:
                        	/**
                        	 *  Color del fondo del cuadrado.
                        	 */
                            g.setColor(Color.black);
                            break;
                        case BOSQUE:
                        	/**
                        	 *  Color del fondo del cuadrado.
                        	 */
                        	g.setColor(new Color(153,153,0));
                            break;
                        case RIO:
                        	/**
                        	 *  Color del fondo del cuadrado.
                        	 */
                            g.setColor(new Color(0,204,204));
                            break;
                        case MONTANA:
                        	/**
                        	 *  Color del fondo del cuadrado.
                        	 */
                            g.setColor(new Color(0,0,204));
                            /**
                             *  Dibujamos el cuadrado.
                             */
                            g.fillRect(xoffset, yoffset, sqsize, sqsize);
                            /**
                             *  Color del borde.
                             */
                            g.setColor(Color.blue);
                            /**
                             *  Dibujamos el borde.
                             */
                            g.drawRect(xoffset, yoffset, sqsize, sqsize);
                            continue;
                        default:
                        	/**
                        	 *  Color del fondo del cuadrado.
                        	 */
                            g.setColor(Color.white);
                            break;
                    }
                	/**
                	 *  Dibujamos el cuadrado.
                	 */
                	g.fillRect(xoffset, yoffset, sqsize, sqsize);
                    /**
                     *  Color del borde.
                     */
                    g.setColor(Color.blue);
                    /**
                     *  Dibujamos el borde.
                     */
                    g.drawRect(xoffset, yoffset, sqsize, sqsize);
                    g.setColor(Color.white);
                    /**
                     *  Escribir heuristica
                     */
                    g.drawString(""+celda.getHn(), xoffset + 50, yoffset + 65);
                    /**
                     *  Escribir Coste
                     */
                    g.drawString(""+celda.getGn(), xoffset + 5, yoffset + 65);
                    /**
                     *  Escribir funcion
                     */
                    g.drawString("" + celda.getFn(), xoffset + 5, yoffset + 15);
	                    
	                /**
	                 *  En caso de que la celda sea parte de la solución tendrá otro dibujo, en este caso un borde amarillo y un punto rojo.
	                 */
                    if(celda.isSolucion()){
                    	/**
                    	 *  Color del fondo del cuadrado.
                    	 */
                    	g.setColor(celda.getType().getColor());
                    	/**
                    	 *  Dibujamos el cuadrado.
                    	 */
                    	g.fillRect(xoffset, yoffset, sqsize, sqsize);
                    	/**
                    	 *  Color del borde.
                    	 */
                        g.setColor(Color.yellow);
                        /**
                         *  Dibujamos el borde.
                         */
                        g.drawRect(xoffset, yoffset, sqsize-1, sqsize-1);
                        /**
                         *  Color del texto.
                         */
	                    g.setColor(Color.white);
	                    /**
	                     *  Escribir heuristica
	                     */
	                    g.drawString(""+celda.getHn(), xoffset + 50, yoffset + 65);
	                    /**
	                     *  Escribir Coste
	                     */
	                    g.drawString(""+celda.getGn(), xoffset + 5, yoffset + 65);
	                    /**
	                     *  Escribir funcion
	                     */
	                    g.drawString("" + celda.getFn(), xoffset + 5, yoffset + 15);
	                    /**
	                     *  Color del círculo.
	                     */
                    	g.setColor(Color.red);
                    	/**
                    	 *  Dibujamos el círculo.
                    	 */
                       g.fillOval(xoffset+23,yoffset+23,sqsize/3,sqsize/3);  
                    }// Cierra if.
                    else if(celda.isGenerado())
                    {
                    	/**
                    	 *  Color del fondo del cuadrado.
                    	 */
                    	g.setColor(celda.getType().getColor());
                    	/**
                    	 *  Dibujamos el cuadrado.
                    	 */
                    	g.fillRect(xoffset, yoffset, sqsize, sqsize);
                    	/**
                    	 *  Color del borde.
                    	 */
                        g.setColor(Color.green);
                        /**
                         *  Dibujamos el borde.
                         */
                        g.drawRect(xoffset, yoffset, sqsize-1, sqsize-1);
                        /**
                         *  Color del texto.
                         */
	                    g.setColor(Color.white);
	                    /**
	                     *  Escribir heuristica
	                     */
	                    g.drawString(""+celda.getHn(), xoffset + 50, yoffset + 65);
	                    /**
	                     *  Escribir Coste
	                     */
	                    g.drawString(""+celda.getGn(), xoffset + 5, yoffset + 65);
	                    /**
	                     *  Escribir funcion
	                     */
	                    g.drawString("" + celda.getFn(), xoffset + 5, yoffset + 15);
                    }// Cierra elseif.
                }// Cierra for.
            }// Cierra for.
        }// Cierra PaintComponent.
    }// Cierra DisplayPanel.

	public int getMazeSize() {
		return mazeSize;
	}

	public void setMazeSize(int mazeSize) {
		this.mazeSize = mazeSize;
	}
}


