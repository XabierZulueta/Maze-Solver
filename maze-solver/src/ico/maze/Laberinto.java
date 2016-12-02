package ico.maze;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class Laberinto {
	private File dijkstraFile = null;
	private File avaraFile = null;
	private File astarFile = null;
	
	/**
	 *  Vector de celdas, a.k.a Laberinto.
	 */
    private Celda[][] celdasLaberinto;
    /**
     *  Array con las celdas en abierto.
     */
    private ArrayList<Celda> abierto;
    /**
     *  Array con las celdas visitadas.
     */
    private ArrayList<Celda> cerrado;
    private Celda celdaInicial;
    /**
     * 
     */
    private Celda celdaFinal;
    /**
     * Tamaño del laberinto.
     */
    private int size;
    private Busqueda typeBusqueda = Busqueda.DIJKSTRA;
    /**
     * Constructor para el laberinto que se inicializan todos los atributos necesarios
     * @param size. El tamaño del tablero.
     */
    public Laberinto(int size) {
        this.size = size;
        celdasLaberinto = new Celda[size][size];
        abierto = new ArrayList<>();
        cerrado = new ArrayList<>();

        crearLaberinto();

        celdaInicial = celdasLaberinto[0][0];
        celdaInicial.setType(Celda.Type.INICIO);

        celdaFinal = celdasLaberinto[size-1][size-1];
        celdaFinal.setType(Celda.Type.FINAL);
        dijkstraFile = new File(typeBusqueda.DIJKSTRA.getPath().toString());
        avaraFile = new File(typeBusqueda.AVARA.getPath().toString());
        astarFile = new File(typeBusqueda.ASTAR.getPath().toString());
        resetearFichero(Busqueda.DIJKSTRA);
        resetearFichero(Busqueda.AVARA);
        resetearFichero(Busqueda.ASTAR);

    }
    /**
     * Es necesario crear los ficheros si no estan creados ya que para actualizar el fichero, necesitamos que sean Nuevos.
     */
    public void resetearFichero(Busqueda typeBusqueda){
    	PrintWriter writer = null;
    	switch(typeBusqueda){
    	case DIJKSTRA:
    		if(!dijkstraFile.exists()){
        		try {
    				new FileWriter(dijkstraFile);
    				writer = new PrintWriter(dijkstraFile);
    				writer.print("***********\nAutores:\tJorge Manzanares\t\t TIMESTAMP: "+new Date()+"\n\t\tXabier Zulueta\n\n");
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}else{
    	    	try {
    				writer = new PrintWriter(dijkstraFile);
    				writer.print("***********\nAutores:\tJorge Manzanares\t\t TIMESTAMP: "+new Date()+"\n\t\tXabier Zulueta\n\n");
    			} catch (FileNotFoundException e) {
    				e.printStackTrace();
    			}finally{
    				writer.close();
    			}
        	}
    		break;
    	case AVARA:
    		if(!avaraFile.exists()){
        		try {
    				new FileWriter(avaraFile);
    				writer = new PrintWriter(avaraFile);
    				writer.print("***********\nAutores:\tJorge Manzanares\t\t TIMESTAMP: "+new Date()+"\n\t\tXabier Zulueta\n\n");
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}else{
        		try {
    				writer = new PrintWriter(avaraFile);
    				writer.print("***********\nAutores:\tJorge Manzanares\t\t TIMESTAMP: "+new Date()+"\n\t\tXabier Zulueta\n\n");
    			} catch (FileNotFoundException e) {
    				e.printStackTrace();
    			}finally{
    				writer.close();
    			}
        	}
    		break;
    	case ASTAR:
    		if(!astarFile.exists()){
        		try {
    				new FileWriter(astarFile);
    				writer = new PrintWriter(astarFile);
    				writer.print("***********\nAutores:\tJorge Manzanares\t\t TIMESTAMP: "+new Date()+"\n\t\tXabier Zulueta\n\n");
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}else{
    	    	try {
    				writer = new PrintWriter(astarFile);
    				writer.print("***********\nAutores:\tJorge Manzanares\t\t TIMESTAMP: "+new Date()+"\n\t\tXabier Zulueta\n\n");
    			} catch (FileNotFoundException e) {
    				e.printStackTrace();
    			}finally{
    				writer.close();
    			}
        	}
    		break;
    	}
    }
    /**
     * Función para coger todas las celdas del laberinto
     * @return todas las celdas del laberinto
     */
    public Celda[][] getPosiciones() {
        return celdasLaberinto;
    }
    /**
     * Función para coger el tamaño con el que se ha creado el laberinto
     * @return
     */
    public int getsize() {
        return size;
    }
    /**
     * Se inicializa la busqueda del laberinto.
     */
    public void procesarBusqueda() {
    	/**
    	 * Añadimos la celda inicial (tipo START) a abiertos.
    	 */
    	
        abierto.add(celdaInicial);
        
        /**
         * Creamos constantemente 
         */
        while (!abierto.isEmpty()) {
            escribirEnFichero("\nAbiertos --> " + abierto, typeBusqueda.getPath());
            escribirEnFichero("\nCerrados --> " + cerrado+"\n", typeBusqueda.getPath());

            /**
             * Cogemos la celda que se encuentra al inicio de abierto.
             */
            Celda celdaActual = abierto.remove(0);

            /**
             *  Metemos la celda en cerrado.
             */
            cerrado.add(celdaActual);
            // Marcamos la celdaActual como celda visitada.
            celdaActual.setGenerado(true);
            // En caso de que sea la celda final entramos.
            if (esCeldaFinal(celdaActual)) {
            	// En caso de que sea dijkstra o astar calculamos el coste.
            	if(typeBusqueda != Busqueda.AVARA) {
            		celdaActual.setGn(celdaActual.getPadre().getGn() + 1);
            	}
            	celdaActual.setHn(calcularHeuristica(celdaActual.getX(), celdaActual.getY(), celdaFinal.getX(), celdaFinal.getY()));
            	celdaActual.setFn(celdaActual.getGn()+celdaActual.getHn()); 
				escribirEnFichero("\n\nCelda actual " +
				        celdaActual + " h(n): " +
				        celdaActual.getHn() + " g(n): " +
				        celdaActual.getGn() +  " f(n): "+
				        celdaActual.getFn()+" Celda padre: " +
				        celdaActual.getPadre()+"\n\nSolucion --> ", typeBusqueda.getPath());
                generarSolucion(celdaActual);
                return;
            } else {
            /**
             * Si no es final la posicion que estamos buscando, miramos sus sucesores.
             */
    	   		explorarCelda(celdaActual);                
                escribirEnFichero("\n\nCelda actual " +
				        celdaActual + " h(n): " +
				        celdaActual.getHn() + " g(n): " +
				        celdaActual.getGn() +  " f(n): "+
				        celdaActual.getFn()+" Celda padre: " +
				        celdaActual.getPadre(), typeBusqueda.getPath());
            }
        }
        /**
         * Si llega hasta aquí es que ha buscado todos los posibles estados y no ha encontrado la solución
         */

    }
    /**
     * Mostramos la solución desde la celda final, si se ha llegado a la solución.
     * Función con recursividad. Exploramos todos los padres de la celda.
     * @param celda
     */
	public void generarSolucion(Celda celda) {
        if (celda.getPadre() != null) {
            generarSolucion(celda.getPadre());
        }
        escribirEnFichero(celda.toString(), typeBusqueda.getPath());
        if (!celda.getType().equals(Celda.Type.INICIO) && !celda.getType().equals(Celda.Type.FINAL)){
        	celda.setSolucion(true);
            //celda.setType(Celda.Type.PATH);
        }
    }
	/**
	 * Devuelve true, si el elemento se encuentra en cerrados.
	 * @param celda
	 * @return boolean. Si la celda está o no en cerrados.
	 */
	public boolean estaCerrado(Celda celda){
		return cerrado.contains(celda);
	}
	/**
	 * Explora todos los sucesores de una celda y los añade a abiertos o cerrados según corresponda.
	 * @param celdaActual
	 */
    @SuppressWarnings("unchecked")
	private void explorarCelda(Celda celdaActual) {

    	// Generamos los hijos de la celdaActual.
        ArrayList<Celda> sucesores = generarHijos(celdaActual);
        escribirEnFichero("Hijos de "+celdaActual.toString()+" -->", typeBusqueda.getPath());
        // Bucle que pasa por cada sucesor.
        for (Celda sucesor : sucesores) {

            /**
             *  Si el sucesor no está en abierto ni en cerrado.
             */
            if (cerrado.indexOf(sucesor) == -1 && abierto.indexOf(sucesor) == -1) {

                /**
                 *  Añadir el sucesor a abierto.
                 */
            	escribirEnFichero(sucesor.toString(), typeBusqueda.getPath());
                abierto.add(sucesor);
                celdasLaberinto[sucesor.getX()][sucesor.getY()] = sucesor;

                // Ordenamos el array de abierto en función de F(n).
                Collections.sort(abierto);
            }
        }
    }

    /**
     * Calculo de la Distancia de Chebyshov, como heuristica en este espacio de estados.
     * @param x
     * @param y
     * @param xf
     * @param yf
     * 
     */
    public int calcularHeuristica(int x, int y, int xf, int yf) {
        return Math.max(Math.abs(xf - x),Math.abs(yf - y));
    }
    /**
     * Se generan todos los posibles sucesores a partir de una celda dada
     * @param celdaActual
     * @return ArrayList con todos los sucesores generados.
     */
    private ArrayList<Celda> generarHijos(Celda celdaActual) {

        ArrayList<Celda> sucesores = new ArrayList<>();
        Celda sucesor;
        // IZQUIERDA(ARRIBA)
        if (celdaActual.getX() > 0) {
            if (celdasLaberinto[celdaActual.getX() - 1][celdaActual.getY()].getType() != Celda.Type.MONTANA) {
            	sucesor = generarCelda(celdaActual, -1, 0);
                sucesores.add(sucesor);
            }
        }
        //DIAGONAL IZQUIERDA ARRIBA(D.D.AR)
        if (celdaActual.getY() < size - 1 && celdaActual.getX()>0) {
            if (celdasLaberinto[celdaActual.getX() - 1][celdaActual.getY()+1].getType() != Celda.Type.MONTANA) {
            	sucesor = generarCelda(celdaActual, -1, 1);
            	sucesores.add(sucesor);
            }
        }
        // ARRIBA(DERECHA)
        if (celdaActual.getY() < size - 1) {
            if (celdasLaberinto[celdaActual.getX()][celdaActual.getY() + 1].getType() != Celda.Type.MONTANA) {
            	sucesor = generarCelda(celdaActual, 0, 1);
                sucesores.add(sucesor);
            }
        }
        //DIAGONAL DERECHA ARRIBA(D.D.AB)
        if (celdaActual.getY() < size - 1 && celdaActual.getX()< size-1) {
            if (celdasLaberinto[celdaActual.getX() + 1][celdaActual.getY()+1].getType() != Celda.Type.MONTANA) {
            	sucesor = generarCelda(celdaActual, 1, 1);
            	sucesores.add(sucesor);
            }
        }
        // DERECHA(ABAJO)
        if (celdaActual.getX() < size - 1) {
            if (celdasLaberinto[celdaActual.getX() + 1][celdaActual.getY()].getType() != Celda.Type.MONTANA) {
            	sucesor = generarCelda(celdaActual, 1, 0);
            	sucesores.add(sucesor);
            }
        }
        //DIAGONAL DERECHA ABAJO(D.I.AB)
        if (celdaActual.getY() >0 && celdaActual.getX()< size-1) {
            if (celdasLaberinto[celdaActual.getX() + 1][celdaActual.getY()-1].getType() != Celda.Type.MONTANA) {
            	sucesor = generarCelda(celdaActual, 1, -1);
            	sucesores.add(sucesor);
            }
        }
        // ABAJO(IZQUIERDA)
        if (celdaActual.getY() > 0) {
            if (celdasLaberinto[celdaActual.getX()][celdaActual.getY() - 1].getType() != Celda.Type.MONTANA) {
            	sucesor = generarCelda(celdaActual, 0, -1);
                sucesores.add(sucesor);
            }
        }
        //DIAGONAL IZQUIERDA ABAJO(D.I.AR)
        if (celdaActual.getY() >0 && celdaActual.getX()>0) {
            if (celdasLaberinto[celdaActual.getX() - 1][celdaActual.getY()-1].getType() != Celda.Type.MONTANA) {
            	sucesor = generarCelda(celdaActual, -1, -1);
            	sucesores.add(sucesor);
            }
        }
     
        return sucesores;
    }
    /**
     * Genera una celda a partir del tipo de búsqueda en proceso..
     * @param celdaActual
     * @param x
     * @param y
     * @return una celda.
     */
    private Celda generarCelda(Celda celdaActual, int x, int y){
    	Celda sucesor = null;
    	switch(typeBusqueda) {
	    	case DIJKSTRA:
	    		sucesor = new Celda(celdaActual.getX() + x,
		                celdaActual.getY() + y,
		                0,
		                (int) (1/celdasLaberinto[celdaActual.getX() + x][celdaActual.getY() + y].getType().getVelocidad() + celdaActual.getGn()),
		                celdasLaberinto[celdaActual.getX() + x][celdaActual.getY() + y].getType());
	    		break;
	    	case ASTAR:
	    		sucesor = new Celda(celdaActual.getX() + x,
		                celdaActual.getY() + y,
		                (int) (calcularHeuristica(celdaActual.getX() + x, celdaActual.getY() + y, celdaFinal.getX(), celdaFinal.getY())/celdasLaberinto[celdaActual.getX() + x][celdaActual.getY() + y].getType().getVelocidad()),
		                (int) (1/celdasLaberinto[celdaActual.getX() + x][celdaActual.getY() + y].getType().getVelocidad() + celdaActual.getGn()),
		                celdasLaberinto[celdaActual.getX() + x][celdaActual.getY() + y].getType());
	    		break;
	    	case AVARA:
	    		sucesor = new Celda(celdaActual.getX() + x,
		                celdaActual.getY() + y,
		                (int) (calcularHeuristica(celdaActual.getX() + x, celdaActual.getY() + y, celdaFinal.getX(), celdaFinal.getY())/celdasLaberinto[celdaActual.getX() + x][celdaActual.getY() + y].getType().getVelocidad()),
		                0,
		                celdasLaberinto[celdaActual.getX() + x][celdaActual.getY() + y].getType());
	    		break;
			default:
				break;
    	}
    	sucesor.setPadre(celdaActual);
    	return sucesor;
    }
    /**
     * Generación de todos los estados nuevos, tipo grass.
     */
    private void crearLaberinto() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                celdasLaberinto[i][j] = new Celda(i, j, 0, 0, Celda.Type.CAMPO);
    }
    /**
     * Reseteamos el grid pero se conservan los valores anteriores.
     */
    public void limpiarLaberinto() {
        abierto.clear();
        cerrado.clear();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                    celdasLaberinto[i][j] = new Celda(i, j, 0, 0, celdasLaberinto[i][j].getType());
            }
        }
    }
    public void calcularHeuristicas() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                celdasLaberinto[i][j] = new Celda(i, j, (int) (calcularHeuristica(celdasLaberinto[i][j].getX(), celdasLaberinto[i][j].getY(), celdaFinal.getX(), celdaFinal.getY())/celdasLaberinto[celdasLaberinto[i][j].getX()][celdasLaberinto[i][j].getY() ].getType().getVelocidad()), 0, celdasLaberinto[i][j].getType());
    }
    /**
     * Devuelve si una celda es final o no
     * @param celdaActual
     * @return
     */
    private boolean esCeldaFinal(Celda celdaActual) {
        return celdaActual.getType().equals(Celda.Type.FINAL);
    }
    /**
     * Cambia el tipo de una celda a otro, siempre que no sea inicial o final.
     * @param celda
     */
    public void cambiarCelda(Celda celda) {
    	if(!celda.equals(celdaInicial) && !celda.equals(celdaFinal))
		{
			switch(celda.getType())
			{
				case CAMPO:
					celda.setType(Celda.Type.BOSQUE);
					break;
				case BOSQUE:
					celda.setType(Celda.Type.RIO);
					break;
				case RIO:
					celda.setType(Celda.Type.MONTANA);
					break;
				default:
					celda.setType(Celda.Type.CAMPO);
					break;
			}
		}
    }
    /**
     * Cambiar la celda de inicio.
     * @param celda
     */
    public void cambiarInicio(Celda celda) {
        celdasLaberinto[celdaInicial.getX()][celdaInicial.getY()].setType(Celda.Type.CAMPO);
        celda.setType(Celda.Type.INICIO);
        celdaInicial = celda;
    }
    /**
     * Cambiar la celda final.
     * @param celda
     */
    public void cambiarFinal(Celda celda) {
    	celdasLaberinto[celdaFinal.getX()][celdaFinal.getY()].setType(Celda.Type.CAMPO);
       celda.setType(Celda.Type.FINAL);
       celdaFinal = celda;
    }
    
	public Busqueda getTypeBusqueda() {
		return typeBusqueda;
	}

	public void setTypeBusqueda(Busqueda typeBusqueda) {
		this.typeBusqueda = typeBusqueda;
	}

	/**
	 * Enum que contempla las diferentes búsquedas.
	 * 
	 * Asociamos a cada busqueda el nombre del fichero correspondiente.
	 * Automaticamente se genera el path en el destino que tenga el archivo ejecutable.
	 *
	 */
	enum Busqueda {
    	DIJKSTRA("DIJKSTRA.txt"), ASTAR("ASTAR.txt"), AVARA("AVARA.txt");
    	public final Path path;
    	Busqueda(String s){
    		this.path = Paths.get(s);
    	}
    	
    	public Path getPath(){
    		return path;
    	}
    }
	
	public Celda getCeldaInicial() {
		return celdaInicial;
	}
	public void setCeldaInicial(Celda celdaInicial) {
		this.celdaInicial = celdaInicial;
	}
	public Celda getCeldaFinal() {
		return celdaFinal;
	}
	public void setCeldaFinal(Celda celdaFinal) {
		this.celdaFinal = celdaFinal;
	}
	public void escribirEnFichero(String string, Path path){
		try {
			Files.write(path, string.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
