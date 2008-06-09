/*
 * GameCanvas.java
 *
 * @author Ricardo Amores Hernández
 */

package MIDP1GameAPI;


import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;

//#if NOKIA_UI
//# import com.nokia.mid.ui.FullCanvas;
//#endif


/**
 * Implementacion de una aproximación al GameAPI disponible en el perfil MIDP 2.0 para dispositivos
 * MIDP 1.0
 *
 * Existen algunas limitaciones importantes, como la falta de getGraphics(), que 
 * obliga a seguir haciendo nuestros graficos por medio de la redefinición de un 
 * método, Paint() en este caso.
 *
 * @see javax.microedition.lcdui.game#GameCanvas
 */
public abstract class GameCanvas  
//#if NOKIA_FULLSCREEN
//#         extends FullCanvas
//#else
    extends Canvas
//#endif
{
    
    public static final int LEFT_PRESSED    = 0x0004;
    public static final int RIGHT_PRESSED   = 0x0020;
    public static final int UP_PRESSED      = 0x0002;
    public static final int DOWN_PRESSED    = 0x0040;
    public static final int FIRE_PRESSED    = 0x0100;
    public static final int GAME_A_PRESSED  = 0x0200;
    public static final int GAME_B_PRESSED  = 0x0400;
    public static final int GAME_C_PRESSED  = 0x0600;
    public static final int GAME_D_PRESSED  = 0x1000;

   
    /** 
     * Crea una nueva instancia de GameCanvas 
     * <p>
     * @param b No tiene efecto en esta implementacion, se usa por 
     * compatibilidad con javax.microedition.lcdui.game.GameCanvas
     */
    public GameCanvas( boolean b) {
        super();
        
        //Comprobamos si el dispositivo soporta double buffer. En caso contrario
        //creamos una imagen para usarla de buffer
        if (!isDoubleBuffered())
        {
            _doubleBuffering =false;
            _backBuffer = Image.createImage( getWidth(), getHeight() );
            _backBufferGraphics = _backBuffer.getGraphics();
        }
        
        else 
        {
            _doubleBuffering = true;
        }
    }
    
    /**
     * Implementación de la función callback keyPressed.
     * <p>
     * Sirve para implementar un registro de las teclas activadas en el MIDlet.
     * <p>
     * Importante: si se redefine este método en nuestra instancia de GameCanvas hay que 
     * llamar a la función de la clase base para que este sistema funcione
     * super.keyPressed()
     */
    protected void keyPressed(int keyCode)
    {
        switch(getGameAction(keyCode))
        {
            case UP:
                _keys = _keys | UP_PRESSED;
                break;
                
            case DOWN:
               _keys = _keys | DOWN_PRESSED;
                break;
                
            case LEFT:
                _keys = _keys | LEFT_PRESSED;
                break;
                
            case RIGHT:
                _keys = _keys | RIGHT_PRESSED;
                break;
                
            case GAME_A:
                _keys = _keys | GAME_A_PRESSED;
                break;
                
            case GAME_B:
                _keys = _keys | GAME_B_PRESSED;
                break;
                
            case GAME_C:
                _keys = _keys | GAME_C_PRESSED;
                break;
                
            case GAME_D:
                _keys = _keys | GAME_D_PRESSED;
                break;             
        }
    
    }
    
    /**
     * Implementación de la función callback keyReleased.
     * <p>
     * Sirve para implementar un registro de las teclas activadas en el MIDlet.
     * <p>
     * Importante: si se redefine este método en nuestra instancia de GameCanvas hay que 
     * llamar a la función de la clase base para que este sistema funcione
     * super.keyReleased()
     */
    protected void keyReleased(int keyCode)
    {
        switch(getGameAction(keyCode))
        {
            case UP:
                _keys = _keys ^ UP_PRESSED;
                break;
                
            case DOWN:
               _keys = _keys ^ DOWN_PRESSED;
                break;
                
            case LEFT:
                _keys = _keys ^ LEFT_PRESSED;
                break;
                
            case RIGHT:
                _keys = _keys ^ RIGHT_PRESSED;
                break;
                
            case GAME_A:
                _keys = _keys ^ GAME_A_PRESSED;
                break;
                
            case GAME_B:
                _keys = _keys ^ GAME_B_PRESSED;
                break;
                
            case GAME_C:
                _keys = _keys ^ GAME_C_PRESSED;
                break;
                
            case GAME_D:
                _keys = _keys ^ GAME_D_PRESSED;
                break;             
        }
        
    }
    
    /**
     * Devuelve el estado de las teclas del terminal. 
     * <p>
     * Se puede consultar cada tecla individual con operaciones de bit OR
     */
     public final int getKeyStates()
    {
       return _keys;
    }
    
    /**
     * Actualiza la pantalla con los graficos del backbuffer.
     * <p>
     * No se implementa la posibilidad de actualizar sólo parte de la pantalla
     */
    public final void flushGraphics(){
        
        //Simplemente mandamos repintar la pantalla
        repaint();
        serviceRepaints();            
    }
   
    /**
     * Método para dibujado en pantalla.
     * <p>
     * Este metodo deberá redefinirse para crear nuestros graficos en 
     * el MIDlet, en contraposición al perfil MIDP2.0 que permite obtener en cualquier momento
     * una referencia al objeto Graphics encargado de dibujar en pantalla.
     */
    public abstract void paint(Graphics screenGraphics);
     
    
    ///////////////////////////////////////////////////////////////////////////
    // Variables necesarias
    ///////////////////////////////////////////////////////////////////////////   
    
    //Registro con el estado de las teclas
    protected static int _keys = 0;
    
    //Backbuffer para utilizar si el dispositivo no soporta doubleBuffering
    protected Image _backBuffer;
    protected Graphics _backBufferGraphics;
    
    //Indica si el dispositivo tiene capacidad de double buffer
    protected boolean _doubleBuffering;
}
