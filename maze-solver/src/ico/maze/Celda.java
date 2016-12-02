package ico.maze;

import java.awt.Color;

@SuppressWarnings("rawtypes")
public class Celda implements Comparable {

    private int x;
    private int y;
    private Type tipo;
    private Celda padre;
    private int hn;
    private int gn;
    private int fn;
    private boolean cerrado;
    private boolean solucion;

    /**
     *  Constructor Celda con todos sus parámetros.
     * @param x
     * @param y
     * @param hn
     * @param gn
     * @param tipo
     */
    public Celda(int x, int y, int hn, int gn, Type tipo) {
        this.x = x;
        this.y = y;
        this.gn = gn;
        this.hn = hn;
        this.tipo = tipo;
        this.solucion = false;
        this.cerrado = false;
        this.fn = this.gn + this.hn;
    }

    public boolean isGenerado() {
		return cerrado;
	}

	public void setGenerado(boolean generado) {
		this.cerrado = generado;
	}

	public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Type getType() {
        return tipo;
    }

    public void setType(Type tipo) {
        this.tipo = tipo;
    }

    public int getHn() {
        return hn;
    }

    public void setHn(int hn) {
        this.hn = hn;
    }

    public int getGn() {
        return gn;
    }

    public void setGn(int gn) {
        this.gn = gn;
    }

    public int getFn() {
        return fn;
    }

    public void setFn(int fn) {
        this.fn = fn;
    }

    public Celda getPadre() {
        return padre;
    }

    public void setPadre(Celda padre) {
        this.padre = padre;
    }

    /**
     *  Comprueba si un objeto es igual a otro.
     */
    @Override
    public boolean equals(Object obj) {
        Celda celda = (Celda) obj;
        return celda.getX() == this.getX() && celda.getY() == this.getY();
    }

    @Override
    public String toString() {
        return "("+getX()+","+getY()+","+getType().getLetra()+")";
    }

    /**
     *  Compara los objetos en función de parámetros de éstos.
     */
    @Override
    public int compareTo(Object o) {
        Celda celda = (Celda) o;
        if (celda.getFn() > this.getFn())
            return -1;
        return 1;
    }

	public boolean isSolucion() {
		return solucion;
	}

	public void setSolucion(boolean solucion) {
		this.solucion = solucion;
	}
    
    /**
     *  Enum que contiene los tipos con sus velocidades y colores en la interfaz.
     */
    enum Type {
       FINAL(1, Color.RED), INICIO(1, new Color(0,153,0)), CAMPO(1, Color.BLACK), 
       MONTANA(0, new Color(0,0,204)), RIO(0.25, new Color(0,204,204)), BOSQUE(0.5, new Color(153,153,0));
    	
       private final double velocidad;
       private Color color = Color.YELLOW;
       private final char letra;
       
       Type(double velocidad, Color color) {
           this.velocidad = velocidad;
           this.setColor(color);
           this.letra = this.name().charAt(0);
       }
       
       Type(double velocidad){
    	   this.velocidad = velocidad;
           this.letra = this.name().charAt(0);
       }
       
       public double getVelocidad() {
           return this.velocidad;
       }
       
       public Color getColor() {
    	   return color;
       }
       public void setColor(Color color) {
    	   this.color = color;
       }
       public char getLetra(){
    	   return this.letra;
       }
    }
}
