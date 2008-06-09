/*
 * Sprite.java
 *
 * @author Ricardo Amores Hernández
 */

package MIDP1GameAPI;

import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;
import java.lang.ArrayIndexOutOfBoundsException;

import javax.microedition.lcdui.*;

//#if NOKIA_UI
//# import com.nokia.mid.ui.*;
//# import MIDP1GameAPI_Utils.ImageSet;
//#endif

/**
 * Representa un elemento visual del juego con diferentes frames de animación.
 * Los frames se guardan en una imagen, por lo que todo sprite debe referenciar
 * una imagen con al menos un frame. El sprite también define una secuencia de 
 * frames, que no tiene por qué coincidir con el número de frames del mismo.
 * Si el dispositivo soporta transformaciones avanzadas de imagenes 
 * (rotaciones y reflejos) es posible aplicarlas al sprite. 
 * Todo sprite tiene un punto de referencia a partir del cual se aplican las 
 * trasnformaciones (translaciones, rotaciones o reflejos). Este punto puede
 * definirse en cualquier momento.
 * También es posible definir un rectángulo de colisión para el sprite, y se 
 * proporcionan varios métodos para comprobar colisiones con diferentes elementos
 * visuales.
 *
 * Implementa una funcionalidad semejante a la clase Sprite definida en MIDP2.0
 * @see javax.microedition.lcdui.game#Sprite
 */
public class Sprite extends Layer {
    
    //Las transformaciones sólo están disponibles para un movil que soporte NokiaUI
//#if NOKIA_UI
//#     public static final int TRANS_NONE = 0;
//#     public static final int TRANS_ROT90 = DirectGraphics.ROTATE_90;
//#     public static final int TRANS_ROT180 = DirectGraphics.ROTATE_180;
//#     public static final int TRANS_ROT270 = DirectGraphics.ROTATE_270;
//#     public static final int TRANS_MIRROR = DirectGraphics.FLIP_HORIZONTAL;
//#     public static final int TRANS_MIRROR_ROT90 = TRANS_ROT90 | DirectGraphics.FLIP_VERTICAL;
//#     public static final int TRANS_MIRROR_ROT180 = DirectGraphics.FLIP_VERTICAL;
//#     public static final int TRANS_MIRROR_ROT270 = TRANS_ROT270 | DirectGraphics.FLIP_VERTICAL;
//#endif

    /**
     * Crea un Sprite no animado con la imagen pasada como parámetro
     */
    public Sprite (Image image)
    {
//#if DEBUG
//#         if (image == null) throw new NullPointerException("Sprite::Sprite - arg imagen is null");
//#else
        if (image == null) throw new NullPointerException();
//#endif

//#if NOKIA_UI
//#        _frames =  new ImageSet(image,  image.getWidth(), image.getHeight() );
//#        _transform = TRANS_NONE;
//#else
   _frames = image;
//#endif
       _frameSequence = new int[1];
       _frameSequenceLength = 1;
       _actualFrameIndex = _frameSequence[0];
              
       _collisionRectX = _collisionRectY = 0;
       _collisionRectW = image.getWidth();
       _collisionRectH = image.getHeight();       
       
       //de Layer
       _posx = _posy = 0;
       _visible = true;
    }
   
    /**
     * Crea un Sprite animado usando los frames contenidos en la imagen
     */  
//#if NOKIA_UI
//#     /**
//#      * Crea un Sprite animado con los frames que contiene el ImageSet
//#      */
//#     public Sprite(ImageSet imageSet) 
//#     {
//#         setImage(imageSet);
//#     
//#         //de Layer
//#         _posx = _posy = 0;
//#         _visible = true;
//#     } 
//#endif
    public Sprite (Image image, int frameWidth, int frameHeight)
    {
        setImage(image, frameWidth, frameHeight);
               
       _visible = true;
    }
    
    public Sprite (Sprite s)
    {
        
        //#if DEBUG
//#             if (s == null) throw new NullPointerException("Sprite::Sprite - Sprite is null");
        //#else
            if (s == null) throw new NullPointerException();            
        //#endif
        
        //#if NOKIA_UI
//#         _transform = s._transform;
//#         _refPixelNoTransformX = s._refPixelNoTransformX;
//#         _refPixelNoTransformY = s._refPixelNoTransformY;
        //#else
        //frames del sprite
        _frames = s._frames;
        _numColFrames = s._numColFrames;
        _numRowFrames = s._numRowFrames;
        //#endif
        _frameSequence = new int[s._frameSequenceLength];
        System.arraycopy( s._frameSequence, 0, _frameSequence,0, s._frameSequenceLength);
        _frameSequenceLength = s._frameSequenceLength;
        _actualFrameIndex = s._actualFrameIndex;
        
        _refPixelX = s._refPixelX;
        _refPixelY = s._refPixelY;
        
        _collisionRectX = s._collisionRectX;
        _collisionRectY = s._collisionRectY;
        _collisionRectW = s._collisionRectW;
        _collisionRectH = s._collisionRectH;
    } 

//#if NOKIA_UI
//#     /** 
//#     *Cambia el FrameSet que contiene los frames del sprite
//#     */
//#     public void setImage (ImageSet imageSet)
//#     {
//#      _frames = imageSet;
//# 
//#      _frameSequenceLength = imageSet.getNumFrames();
//#      
//#      _frameSequence = new int [_frameSequenceLength];
//#        
//#      //Secuencia básica al cambiar la imagen
//#      for (int i=0; i < _frameSequenceLength; ++i)
//#          _frameSequence[i] = i; 
//# 
//#      //tamaño del sprite
//#      _width = imageSet.getFrameWidth();
//#      _height = imageSet.getFrameHeight();
//#      
//#      //Rectángulo de colision
//#      _collisionRectX = _collisionRectY = 0;
//#      _collisionRectW = _width;
//#      _collisionRectH = _height;
//#      
//#     }
//#endif
    
    /**
     * Cambia la imagen que contiene los frames del sprite
     */
    public void setImage( Image img, int frameWidth, int frameHeight)
    {   
       
//#if DEBUG
//#         if (img == null) 
//#             throw new NullPointerException("Sprite::setImage - arg img is null");
//#         
//#         if (frameWidth < 1)
//#             throw new IllegalArgumentException("Sprite::setImage - arg frameWidth = " + frameWidth);
//#         
//#         if (frameHeight < 1)
//#             throw new IllegalArgumentException("Sprite::setImage - arg frameHeight = " + frameHeight);        
//#         
//#         if ( ( img.getWidth() % frameWidth ) != 0 )
//#             throw new IllegalArgumentException("Sprite::setImage - mismatch between frame and image width");
//#         
//#         if ( ( img.getHeight() % frameHeight ) != 0 )
//#             throw new IllegalArgumentException("Sprite::setImage - mismatch between frame and image height");
//#else
        if (img == null) 
            throw new NullPointerException();
        
        if ( (frameWidth < 1) || (frameHeight < 1) )
            throw new IllegalArgumentException();
        
        if ( ( ( img.getWidth() % frameWidth ) != 0 ) ||  
             ( ( img.getHeight() % frameHeight ) != 0 ) )
            throw new IllegalArgumentException();
//#endif
     
        
//#if NOKIA_UI
//#      //En el caso de movil nokia simplemente creamos un nuevo ImageSet para los frames
//#      this.setImage( new ImageSet (img, frameWidth, frameHeight) );
//#else
     _frames = img;
     _width = frameWidth;
     _height = frameHeight;
     
     //Calculamos el número de frames horizontales y verticales
     _numColFrames = img.getWidth() / frameWidth;
     _numRowFrames = img.getHeight() / frameHeight;
     _numTotalFrames = _numColFrames * _numRowFrames;
     
     //Establecemos la secuencia
     _frameSequenceLength = _numColFrames * _numRowFrames;
     _frameSequence = new int [_frameSequenceLength];
     
     for (int i=0; i < _frameSequenceLength; ++i)
         _frameSequence[i] = i;
     

     //Establecemos el rectángulo de colisión
     _collisionRectX = _collisionRectY = 0;
     _collisionRectW = _width;
     _collisionRectH = _height;
   
//#endif
    }
    
    /**
     * Devuelve el número de frames totales del sprite
     */
    public final int getRawFrameCount() 
    {
//#if NOKIA_UI
//#         return _frames.getNumFrames();
//#else
        return _numTotalFrames;
//#endif
    }

    /**
     * Devuelve el número de elementos de la secuencia de frames
     */
    public final int getFrameSecuenceLength() 
    {
        return _frameSequenceLength;
    }

    /**
     * Devuelve el índice actual de la secuencia de frames
     */
    public final int getFrame() 
    {
        return _actualFrameIndex;
    }

    /**
     * Establece un frame concreto dentro de la secuencia de frames
     */
    public void setFrame(int frameIndex) 
    {
        
//#if DEBUG
//#         if (frameIndex < 0) throw new IndexOutOfBoundsException("Sprite::setFrame - arg frameIndex = " + frameIndex);
//#         if (frameIndex >= _frameSequenceLength) throw new IndexOutOfBoundsException("Sprite::setFrame - arg frameIndex = " + frameIndex);
//#else
        if (frameIndex < 0) throw new IndexOutOfBoundsException();
        if (frameIndex >= _frameSequenceLength) throw new IndexOutOfBoundsException();
//#endif
                
        this._actualFrameIndex = frameIndex;
    }

    /**
     *
     */
    public void setFrameSequence(int[] sequence)
    {
//#if DEBUG
//#         if (sequence.length < 1) throw new IllegalArgumentException("Sprite::setFrameSequence - Empty sequence");
//#else
        if (sequence.length < 1) throw new IllegalArgumentException();
//#endif
       //comprobamos cada elemento para ver si el índice de frame es correcto
        for (int i = 0; i < sequence.length; ++i)
        
           
//#if NOKIA_UI
//#             if ( (sequence[i] > _frames.getNumFrames()) || (sequence[1] < 0) )
//#else
        if ( (sequence[i] > _numTotalFrames) || (sequence[1] < 0) )
//#endif
                
//#if DEBUG
//#                 throw new ArrayIndexOutOfBoundsException("Sprite::setFrameSequence - frame sequence incorrect");
//#else
            throw new ArrayIndexOutOfBoundsException();
//#endif
    
    _frameSequence = sequence;
    _frameSequenceLength = sequence.length;
    }
            
    /**
     * Obtiene la posición horizonal del píxel de referencia del sprite
     */
    public final int getRefPixelX() 
    {
        return _refPixelX;
    }
    
    /**
     * Obtiene la posición vertical del píxel de referencia del sprite
     */
    public final int getRefPixelY() 
    {
        return _refPixelY;
    }    
    
    /**
     * Cambia al siguiente frame de la secuencia
     */
    public final void nextFrame()
    {
        _actualFrameIndex++;
            
        if (_actualFrameIndex >= _frameSequenceLength)
            _actualFrameIndex = 0;
    }
    
    /**
     * Cambia al anterior frame de la secuencia
     */
    public final void prevFrame()
    {
        _actualFrameIndex--;
        if (_actualFrameIndex < 0)
            _actualFrameIndex = _frameSequenceLength -1;    
    }
    
    /** 
     * Fija la posición del sprite en el sistema de coordenadas de pintado
     */
    public void setPixelRefPosition(int x, int y)
    {
        //recalculamos la posición del rectángulo de colision
        _collisionRectX += _refPixelX - x;
        _collisionRectY += _refPixelY - y;
        
        //Asignamos el píxel de referencia
        _refPixelX = x;
        _refPixelY = y;

//#if NOKIA_UI
//#         //Hacemos una copia
//#         _refPixelNoTransformX = x;
//#         _refPixelNoTransformY = y;
//#         
//#         //Si hay una transformacion aplicada es necesario aplicarla de nuevo
//#         //para reajustar el pixel de referencia
//#         if (_transform != this.TRANS_NONE)
//#             this.setTransform(_transform);
//#endif

    }

    /**
     * Define el rectángulo de colisión del Sprite relativo a la posición 
     * superior derecha del sprite, sin tener en cuenta su transformacion, 
     * ni tampoco su pixel de referencia.
     */
    public void defineCollisionRectangle (int x, int y, int width, int height)
    {
//#if DEBUG
//#         if (width < 0) throw new IllegalArgumentException("Sprite::defineCollisionRectangle - width = " + width);
//#         if (height < 0) throw new IllegalArgumentException("Sprite::defineCollisionRectangle - height = " + height);
//#else
        if ( (width < 0) || (height < 0) ) throw new IllegalArgumentException();
//#endif
    
    _collisionRectX = x + _posx - _refPixelX;
    _collisionRectY = y + _posy - _refPixelY;
    _collisionRectW = width;
    _collisionRectH = height;
        
    }
    
    public final void setTransform(int transform)
    {
//#if NOKIA_UI
//#         //Var temporal para intercambios
//#         int temp;
//#         
//#          
//#         //alguna transformacion anterior
//#         int initialWidth = _frames.getFrameWidth();
//#         int initialHeight = _frames.getFrameHeight();
//# 
//#        
//#         //Si queremos eliminar la transformacion dejamos todo como estaba
//#         if (transform == TRANS_NONE)
//#         {
//#             
//#             //Dependiendo de la transformacion existente anterioromente
//#             //Habrá que intercambiar el alto y ancho del rectángulo de colision
//#             if ( ( _transform == TRANS_ROT90 ) ||
//#                  ( _transform == TRANS_MIRROR_ROT90 ) ||
//#                  ( _transform == TRANS_MIRROR_ROT270 ) ||
//#                  ( _transform == TRANS_ROT270 )
//#                )
//#             {
//#                 temp = _collisionRectH;
//#                 _collisionRectH = _collisionRectW;
//#                 _collisionRectW = temp;
//#             }
//#             
//#             //Recuperamos los valores iniciales de alto y ancho del sprite, así
//#             //como el valor del pixel de referencia original
//#             _width = initialWidth;
//#             _height = initialHeight;
//#             _refPixelX = _refPixelNoTransformX;
//#             _refPixelY = _refPixelNoTransformY;
//#             
//#             //Aplicamos la transformacion
//#             _transform = transform;
//#             return;
//#         }
//#         
//#         /**
//#          * Pequeña optimización
//#          * Si el píxel de referencia esta situado en el centro del sprite, éste no
//#          * cambia cualquiera que sea la transformacion, asi que solo tendremos que
//#          * intercambiar el alto y el ancho en las transformaciones necesarias
//#          * */
//#         if ( (_refPixelY == (initialWidth / 2) )  && ( _refPixelX == (initialHeight /2)) )
//#         {
//#             //En estas transformaciones lo único que hay que hacer es intercambiar
//#             //el alto y ancho del sprite y de su rectángulo de colisiones            
//#             switch(transform)
//#             {                    
//#                 case TRANS_ROT90:
//#                 case TRANS_MIRROR_ROT90:                
//#                 case TRANS_MIRROR_ROT270:
//#                 case TRANS_ROT270:
//# 
//#                     temp = _width;
//#                     _width = _height;
//#                     _height = temp;
//#                     
//#                     temp = _collisionRectH;
//#                     _collisionRectH = _collisionRectW;
//#                     _collisionRectW = temp;
//#                     break;           
//#             }
//#             
//#             _transform = transform;
//#             return;
//#         }
//#         
//#         
//#         /*
//#          * Según la transformación puede ser necesario modificar tanto la posición del 
//#          * píxel de referencia como el alto y ancho del sprite y del rectangulo de colisión
//#          */
//#         switch(transform)
//#         {
//#             
//#             case TRANS_MIRROR:
//#                 _refPixelX = _width - _refPixelX ;
//#                 break;
//#                 
//#             case TRANS_ROT90:
//#                 temp = _refPixelY;
//#                 _refPixelY = _refPixelX;
//#                 _refPixelX = _height - temp;
//#                                 
//#                 temp = _width;
//#                 _width = _height;
//#                 _height = temp;
//#                 break;
//#                 
//#             case TRANS_ROT180:
//#                 _refPixelY = _height - _refPixelY;
//#                 _refPixelX =  _width - _refPixelX;
//#                 break;
//#                 
//#             case TRANS_ROT270:
//#                 temp = _refPixelX;
//#                 _refPixelX = _refPixelY;
//#                 _refPixelY = _width - temp;
//#                 
//#                 temp = _width;
//#                 _width = _height;
//#                 _height = temp;
//#                 
//#                 temp = _collisionRectH;
//#                 _collisionRectH = _collisionRectW;
//#                 _collisionRectW = temp;                
//#                 break;
//#                 
//#             case TRANS_MIRROR_ROT90:
//#                 temp = _refPixelX;
//#                 _refPixelY = _width - _refPixelX;
//#                 _refPixelX = _height - _refPixelY;
//#                 
//#                 temp = _width;
//#                 _width = _height;
//#                 _height = temp;
//#                 
//#                 temp = _collisionRectH;
//#                 _collisionRectH = _collisionRectW;
//#                 _collisionRectW = temp;
//#                 break;
//#                 
//#             case TRANS_MIRROR_ROT180:
//#                 _refPixelY =  _height - _refPixelY;
//#                 break;
//#                 
//#             case TRANS_MIRROR_ROT270:
//#                 temp = _refPixelY;
//#                 _refPixelY = _refPixelX;
//#                 _refPixelX = temp;
//#                 
//#                 temp = _width;
//#                 _width = _height;
//#                 _height = temp;
//#                 
//#                 temp = _collisionRectH;
//#                 _collisionRectH = _collisionRectW;
//#                 _collisionRectW = temp;                
//#                 break;
//#             
//#         }
//#         _transform = transform;
//#endif
    }
    
    public boolean collidesWith(Image img, int x, int y, boolean b)
    {
//#if DEBUG
//#         if (img == null) throw new NullPointerException("Sprite::collidesWith - image is null");
//#else
       if (img == null) throw new NullPointerException();
//#endif  
        if (!this._visible)
            return false;
        
                
        //Equivale a return hayColisionEjeHorizontal && hayColisionEjeVertical
        return checkAxisCollision(
                    _collisionRectX + (_collisionRectW / 2),   //Posición sprite
                    x + (img.getWidth() / 2),                                       //Posición imagen
                    (_collisionRectW / 2) + (img.getWidth() / 2)                    //Distancia de colisión
                    )               
                
                &&
                
                checkAxisCollision(
                        _collisionRectY + (_collisionRectH / 2),
                        y + (img.getHeight() / 2),
                        (_collisionRectH / 2) + (img.getHeight() / 2)
                    );
                
    }
    
    public boolean collidesWith(Sprite s, boolean b)
    {
//#if DEBUG
//#         if (s == null) throw new NullPointerException("Sprite::collidesWith - sprite is null");
//#else
       if (s == null) throw new NullPointerException();
//#endif
        
        //Comprobamos visibilidad
        if (!this._visible || !s._visible )
            return false;
        
        return checkAxisCollision(
                    _collisionRectX + (_collisionRectW / 2), 
                   s._collisionRectX  + (s._collisionRectW / 2),
                    (_collisionRectW / 2) + (s._collisionRectW / 2) 
                )
                &&
                checkAxisCollision(
                        _collisionRectY + (_collisionRectH / 2),
                        s._collisionRectY + (s._collisionRectH / 2),
                        (_collisionRectH / 2) + (s._collisionRectH / 2)
                    );  
        
    }
    
    public boolean collidesWith(TiledLayer t, boolean b)
    {

//#if DEBUG
//#         if (t == null) throw new NullPointerException("Sprite::collidesWith - TiledLayer is null");
//#else
       if (t == null) throw new NullPointerException();
//#endif
       
        //Comprobamos visiblidad
        if (!this._visible || !t._visible )
            return false;
      
        //Comprobamos si el sprite está dentro de los límites del TileLayer
        if ( ( _collisionRectX > (t._posx + t._width ) ) 
        || ( ( _collisionRectX + _collisionRectW ) < t._posx ) ) 
            return false;
       
        if (  (_collisionRectY > (t._posy + t._height) ) 
        || ( ( _collisionRectY + _collisionRectH ) < t._posy ) )
            return false;
        
       //Buscar los tiles que ocupa el sprite y buscar colisiones en ellos
       tileInicialX = (_collisionRectX - t._posx) / t._tileWidth;
       tileInicialY = (_collisionRectY - t._posy) / t._tileHeight;
       tileFinalX = (_collisionRectX - t._posx + _collisionRectH) / t._tileWidth;
       tileFinalY = (_collisionRectY - t._posy + _collisionRectW) / t._tileHeight;
       
       //Comprobamos que no nos vamos fuera de rango con los tiles
       if (tileInicialX < 0) 
           tileInicialX = 0;
       if (tileInicialY < 0) 
           tileInicialY = 0;
       if (tileFinalX + (_collisionRectW / t._tileWidth) >= t._numColumns )
           tileFinalX = t._numColumns-1;
       if (tileFinalY + (_height / t._tileHeight) >= t._numRows )
           tileFinalY = t._numRows-1;
        
       //Buscamos si los tiles que ocupa el sprite están ocupados por un tile
       for (int i = tileInicialX; i <= tileFinalX; ++i)
           for (int j = tileInicialY; j <= tileFinalY; ++j)
               if (t._tileMap[ (j * t._numColumns) + i] != 0)
                   return true;
        return false;
    }
    
    private final boolean checkAxisCollision(int posObjA, int posObjB, int collideDist)
    {
        return ( Math.abs(posObjA - posObjB) <= collideDist) ;
    }

    public void setPosition(int x, int y) 
    {
        //Actualizamos la posición del rectángulo de colision
        _collisionRectX = x + (_collisionRectX - _posx) - _refPixelX;
        _collisionRectY = y + (_collisionRectY - _posy) - _refPixelY;
        
        //Actualizamos la posicion
        super.setPosition(x,y);
        
    }
    
    public void move (int dx, int dy)
    {
        _collisionRectX += dx;
        _collisionRectY += dy;
        
        super.move(dx, dy);
    }
    
    public void paint(Graphics g)
    {
        if (!_visible)
           return;


//#if NOKIA_UI
//#         DirectGraphics dg = DirectUtils.getDirectGraphics(g);
//#         
//#         dg.drawImage(_frames.getFrameAt(_actualFrameIndex),
//#               _posx -_refPixelX, 
//#               _posy -_refPixelY, 
//#               Graphics.TOP|Graphics.LEFT ,  
//#               _transform);     
//#else
        //Guardamos el anterior rectángulo de colisión
        int clipX, clipY, clipW, clipH;
        clipX = g.getClipX();
        clipY = g.getClipY();
        clipW = g.getClipWidth();
        clipH = g.getClipHeight();
        
        //Fijamos el nuevo rectangulo de clipping
        g.clipRect(_posx, _posy, _width, _height);
        int xOffset, yOffset;
        xOffset = (_frameSequence[_actualFrameIndex] % _numColFrames ) * _width;
        yOffset = (_frameSequence[_actualFrameIndex] % _numRowFrames ) * _height;
        
        //Dibujamos el sprite con el frame determinado
        g.drawImage(_frames,_posx - xOffset, _posy - yOffset, Graphics.TOP | Graphics.LEFT);
        
        //Dejamos el rectángulo de clipping como estaba
        g.setClip(clipX, clipY, clipW, clipH);
//#endif

       
//#if DEBUG
//#         //En el modo debug dibujamos el rectángulo de colisión del Sprite        
//#         g.setColor(255,0,255);
//#         g.drawRect(_collisionRectX, _collisionRectY, _collisionRectW, _collisionRectH);
//#endif      
    
    }
    
    // Datos privados //////////////////////////////////////////////////////////
   
    //Imagenes del sprite
//#if NOKIA_UI
//#     //Frames del Sprite
//#     ImageSet _frames;
//#     
//#     //Transformación aplicada al sprite
//#     protected int _transform;  
//#    
//#    //Necesario para guardar el pixel de referencia sin modificar ya que 
//#    //las tranformaciones lo cambian    
//#     protected int _refPixelNoTransformX, _refPixelNoTransformY;    
//#else
    //Frames del sprite
    protected Image _frames;
    //Número de frames horizontales del sprite
    protected int _numColFrames;
    //Número de frames verticales del sprite
    protected int _numRowFrames;
    //Numero total de frames
    protected int _numTotalFrames;
//#endif
    
    //Control de secuencia de frames
    protected int [] _frameSequence;    
    protected int _frameSequenceLength;
    protected int _actualFrameIndex;

    //Posición del pixel de referencia del sprite, inicialmente 0,0
    protected int _refPixelX, _refPixelY;
    
    //rectángulo de colision 
    //Guardamos la posición ABSOLUTA en el mapa, no la relativa al sprite
    //y TENIENDO en cuenta el píxel de referencia
    protected int _collisionRectX, _collisionRectY, _collisionRectW, _collisionRectH;
    
    //Variables para agilizar el cálculo de colisiones
    //Cada una contiene la posición del rectángulo de colisión teniendo en cuenta
    //el píxel de referencia.
    private int tileInicialX, tileFinalX, tileInicialY, tileFinalY;
}

