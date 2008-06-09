/*
 * Layer.java
 *
 * @author Ricardo Amores Hern�ndez
 */

package MIDP1GameAPI;

import javax.microedition.lcdui.Graphics;

/**
 * Clase abstracta que representa un elemento visual del juego. 
 * 
 * Cada capa tiene una posici�n, un tama�o y puede ser visible (activada). 
 * La posici�n inicial es el (0,0), y visible por defecto
 * Sus clases derivadas deben implementar el m�todo paint(Graphics) que 
 * las dibujar�.
 * 
 * Implementa una funcionalidad semejante a la clase Layer definida en MIDP2.0
 * @see javax.microedition.lcdui.game#Layer
 */
public abstract class Layer {
    
    ///////////////////////////////////////////////////////////////////////////
    // Miembros privados
    ///////////////////////////////////////////////////////////////////////////
    
    //Posici�n del Layer
    protected int _posx;
    protected int _posy;
    
    //el Layer es visible?
    protected boolean _visible;
    
    //Dimensiones del Layer
    protected int _height;
    protected int _width;
    
    ///////////////////////////////////////////////////////////////////////////
    // M�todos
    ///////////////////////////////////////////////////////////////////////////
    
    /**
     * Pinta esta capa si es visible
     */
    public abstract void paint (Graphics g);
    
    /**
     * Establece la posici�n del layer de forma que su esquina superior derecha se posicione
     * en (x,y) segun el sistema de coordenadas del objeto de Gr�ficos.
     */
    public void setPosition(int x, int y)
    {
        _posx = x;
        _posy = y;
    }
    
    /**
     * Mueve el layer segun las distancias horizontal y vertical especificadas.
     */
    public void move (int dx, int dy)
    {
        //TODO: � Necesario comprobar overflow (Integer.MAX_VALUE) y underflow (Integer.MIN_VALUE) ?
        _posx += dx;
        _posy += dy;
    }
    
    /**
     * Establece la visibilidad de este layer.
     */
    public void setVisible (boolean visible)
    {
        _visible = visible;
    }
    
    /**
     * Obtiene la posici�n horizontal de este layer.
     */
    public final int getX()
    {
        return _posx;
    }
    
    /**
     * Obtiene la posici�n vertical de este layer.
     */
    public final int getY()
    {
        return _posy;
    }

    /**
     * Obtiene el ancho de este layer, en p�xeles.
     */    
    public final int getWidth()
    {
        return _width;
    }
    
    /**
     * Obtiene la altura de este layer, en p�xeles.
     */
    public final int getHeight()
    {
        return _height;
    }

    /**
     * Obtiene la visibilidad de este layer.
     */
    public final boolean isVisible()
    {
        return _visible;
    }
}
