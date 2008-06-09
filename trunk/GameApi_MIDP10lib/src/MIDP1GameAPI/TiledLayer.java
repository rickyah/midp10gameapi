/*
 * TiledLayer.java
 *
 * @author Ricardo Amores Hernández
 */

package MIDP1GameAPI;

import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;
import java.lang.IndexOutOfBoundsException;
import java.lang.Exception;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

//#if NOKIA_UI
//# import com.nokia.mid.ui.DirectGraphics;
//# import com.nokia.mid.ui.DirectUtils;
//# import MIDP1GameAPI_Utils.ImageSet;
//#endif


/**
 * Clase que representa un mapa de tiles.
 *
 * Un TiledLayer siempre ha de referenciar una imagen que contega las tiles
 * que lo forman. Esta imagen puede cambiarse en cualquier momento.
 * El mapa de tiles ha de definirse celda a celda con los métodos setCell()
 * o fillCells()
 * Permite también crear tiles animados.
 *
 * Implementa una funcionalidad semejante a la clase TiledLayer 
 * definida en la GameAPI MIDP2.0
 * @see javax.microedition.lcdui.game#TiledLayer
 */
public class TiledLayer extends Layer {

    
    /** 
     * Crea una nueva instancia de un TiledLayer usando una imagen con los tiles. 
     * Si se usa un movil nokia se utilizará un ImageSet para ahorrar memoria
     */
//#if NOKIA_UI
//#     public TiledLayer (int columns, int rows, ImageSet imageSet)
//#     {
//#         //Establecemos el tileset a usar
//#         _tileSet = imageSet;
//#         
//#         //Tamaño del tile
//#         _tileHeight = _tileSet.getFrameHeight();
//#         _tileWidth = _tileSet.getFrameWidth();     
//#         
//#         //Número total de tiles
//#         _totalTiles = _tileSet.getNumFrames();
//#         
//#         //Establecemos el ancho y alto del tiledLayer en pixels
//#         _width = columns * _tileWidth;
//#         _height = rows * _tileHeight;
//#         
//#         //Guardamos las filas y columnas que tiene el tilemap
//#         _numColumns = columns;
//#         _numRows = rows;                
//# 
//#         //Creamos el mapa de tiles. El valor de cada celda se inicializa a cero por defecto
//#         _tileMap = new int[rows*columns];
//#         
//#         // Array para referencia tiles animados.
//#         // Inicialmente con capacidad 2
//#         // 2enteros * 2bytes = 4bytes sin usar
//#         _numAnimatedTiles = 2;
//#         _animatedTileReferences = new int [_numAnimatedTiles];
//#         _indexAnimatedTiles = 0;
//#         
//#         //Inicialmente el layer es visible
//#         _visible = true;        
//#     }
//#endif
    
    public TiledLayer (int columns, int rows, Image image, int tileWidth, int tileHeight) 
    {
        
        //Comprobación de errores en los argumentos
//#if DEBUG
//#         if (columns < 1) throw new IllegalArgumentException("TiledLayer::TiledLayer - arg columns = " + columns);
//#         if (rows < 1) throw new IllegalArgumentException("TiledLayer::TiledLayer - arg rows = " + rows);
//#else
        if (columns < 1) throw new IllegalArgumentException();
        if (rows < 1) throw new IllegalArgumentException();
//#endif
       
        //Establecemos la imagen para el tileset
        setStaticTileSet(image, tileWidth, tileHeight);
        
        //Establecemos el ancho y alto del tiledLayer en pixels
        _width = columns * _tileWidth;
        _height = rows * _tileHeight;
        
        //Guardamos las filas y columnas que tiene el tilemap
        _numColumns = columns;
        _numRows = rows;                

        //Creamos el mapa de tiles. El valor de cada celda se inicializa a cero por defecto
        _tileMap = new int[rows*columns];
        
        // Array para referencia tiles animados.
        // Inicialmente con capacidad 2
        // 2enteros * 2bytes = 4bytes sin usar
        _numAnimatedTiles = 2;
        _animatedTileReferences = new int [_numAnimatedTiles];
        _indexAnimatedTiles = 0;
        
        //Inicialmente el layer es visible
        _visible = true;
 }

    public int createAnimatedTile (int staticTileIndex)
    {
        
        //índice de tile estático incorrecto
        if ( (staticTileIndex < 0) || ( staticTileIndex > _totalTiles ) )
//#if DEBUG
//#             throw new IllegalArgumentException("TiledLayer::createAnimatedTile - arg staticTileIndex = " + staticTileIndex);
//#else
        throw new IllegalArgumentException();    
//#endif
        
        //Guardamos referencia
        _animatedTileReferences[_indexAnimatedTiles++] = staticTileIndex;
        
        //Redimensionamos el array de 3 en 3 posiciones si es necesario
        if (_indexAnimatedTiles == _numAnimatedTiles)
        {
            int[] animTilesRefTemp = new int[_numAnimatedTiles + 3];
   
            System.arraycopy(_animatedTileReferences,  0, animTilesRefTemp, 0, _numAnimatedTiles);
            
            _numAnimatedTiles += 3;
            
           _animatedTileReferences = animTilesRefTemp;
           
           animTilesRefTemp = null;
           
           System.gc();
        }
            
        return -_indexAnimatedTiles;
    }
    
    public final int getAnimatedTile (int animatedTileIndex)
    {
//#if DEBUG
//#          if ( ( animatedTileIndex > 0) || ( (- animatedTileIndex) > _indexAnimatedTiles) )
//#             throw new IndexOutOfBoundsException("TiledLayer::setAnimatedTile - arg animatedTileIndex = " + animatedTileIndex);
//#else

         if ( ( animatedTileIndex > 0) || ( (- animatedTileIndex) >  _indexAnimatedTiles ) )
            throw new IndexOutOfBoundsException();
//#endif
    
    return _animatedTileReferences[- animatedTileIndex];
    }
    
    public void setAnimatedTile( int animatedTileIndex, int staticTileIndex)
    {
        
//#if DEBUG
//#         if ( (staticTileIndex < 0) || ( staticTileIndex > (_totalTiles ) ) )
//#             throw new IndexOutOfBoundsException("TiledLayer::setAnimatedTile - arg staticTileIndex = " + animatedTileIndex);
//#         if ( ( animatedTileIndex > 0) || ( (- animatedTileIndex) > _indexAnimatedTiles) )
//#             throw new IndexOutOfBoundsException("TiledLayer::setAnimatedTile - arg animatedTileIndex = " + animatedTileIndex);
//#else 
   if ( (staticTileIndex < 0) || ( staticTileIndex > _totalTiles ) ) 
        throw new IndexOutOfBoundsException();   
   if ( ( animatedTileIndex > 0) || ( (-animatedTileIndex) > _indexAnimatedTiles) )
        throw new IndexOutOfBoundsException();
//#endif
    
        _animatedTileReferences[- animatedTileIndex] = staticTileIndex;
    }
    
    public void fillCells(int col, int row, int numCols, int numRows, int tileIndex)
    {   
        //Comprobamos parámetros
//#if DEBUG      
//#         if ( col < 0 ) 
//#             throw new IndexOutOfBoundsException("TiledLayer::fillCells - arg col = " + col);
//#         if ( row < 0 ) 
//#             throw new IndexOutOfBoundsException("TiledLayer::fillCells - arg row = " + row);
//#         if ( numRows + row > getRows() ) 
//#             throw new IndexOutOfBoundsException("TiledLayer::fillCells - Region rows overflow");
//#         if (numCols < 0) 
//#             throw new IllegalArgumentException("TiledLayer::fillCells - arg numCols = " + numCols);
//#         if (numRows < 0) 
//#             throw new IllegalArgumentException("TiledLayer::fillCells - arg numRows  = " + numRows);
//#         if ( numCols + col > getColumns() ) 
//#             throw new IndexOutOfBoundsException("TiledLayer::fillCells - Region columns overflow");
//#         if ( (tileIndex >= 0) && ( tileIndex > ( getRows() * getColumns() ) ) )
//#             throw new IndexOutOfBoundsException("TiledLayer::fillCells - Arg tileIndex incorrect");
//#         if ( (tileIndex < 0) && ((-tileIndex) > _indexAnimatedTiles))
//#             throw new IndexOutOfBoundsException("TiledLayer::fillCells - Arg tileIndex incorrect");
//#else

    if ( (col < 0) || (row < 0) ) 
        throw new IndexOutOfBoundsException();
    if ( (numRows + row > getRows())  || (numCols + col > getColumns() ) )
        throw new IndexOutOfBoundsException();
    if ( (numCols < 0) || (numRows < 0) ) 
        throw new IllegalArgumentException();
   if ( (tileIndex >= 0) && ( tileIndex > ( getRows() * getColumns() ) ) )
        throw new IndexOutOfBoundsException();    
   if ( (tileIndex < 0) && ((-tileIndex) > _indexAnimatedTiles))         
        throw new IndexOutOfBoundsException();
//#endif  
 
        int tempCol = col;
        
        for (int x = 0; x < numRows; ++x, ++row)
        {
            col=tempCol;
            for (int y = 0; y < numCols; ++y, ++col)
                _tileMap[row * _numColumns + col] = tileIndex;        
        }

        
        
    }
       
    public final int getCell(int col, int row)
    {
//#if DEBUG  
//#         if ( col < 0 )   
//#             throw new IndexOutOfBoundsException("TiledLayer::getCell - arg col = " + col);
//#         
//#         if ( col >= getColumns() )
//#             throw new IndexOutOfBoundsException("TiledLayer::getCell - arg col =" + col);
//#             
//#         
//#         if  ( row < 0 )  
//#             throw new IndexOutOfBoundsException("TiledLayer::getCell - arg row = " + row);
//#         
//#         if ( row >= getRows() ) 
//#             throw new IndexOutOfBoundsException("TiledLayer::getCell - arg row = " + row);
//#else
        if ( ( col < 0 ) || ( col >= getColumns() ) ) 
            throw new IndexOutOfBoundsException();
        
        if ( ( row <0 ) || ( row >= getRows() ) )
            throw new IndexOutOfBoundsException();
//#endif

        return _tileMap[row * _numRows + col];

    }
    
    public void setCell (int col, int row, int tileIndex)
    {
//#if DEBUG
//#         if ( ( col < 0 ) || ( col >= getColumns() ) ) 
//#             throw new IndexOutOfBoundsException("TiledLayer::setCell - arg col = " + col);
//#         
//#         if ( ( row <0 ) || ( row >= getRows() ) )
//#             throw new IndexOutOfBoundsException("TiledLayer::setCell - arg row = " + row);
//#         
//#         if ( (tileIndex >= 0) && ( tileIndex > _totalTiles ) )
//#             throw new IndexOutOfBoundsException("TiledLayer::setCell - Arg tileIndex incorrect");
//#else
        if ( ( col < 0 ) || ( col >= getColumns() ) ) 
            throw new IndexOutOfBoundsException();
        
        if ( ( row <0 ) || ( row >= getRows() ) )
            throw new IndexOutOfBoundsException();
        
        if ( (tileIndex >= 0) && ( tileIndex > _totalTiles ) )
            throw new IndexOutOfBoundsException();
//#endif

        _tileMap[row * _numRows + col] = tileIndex;
    }
    
    public final int getCellWidth()
    {
        return _tileWidth;
    }

    public final int getCellHeight() 
    {
        return _tileHeight;
    }
    
    public final int getColumns()
    {
        return _numColumns;
    }
    
    public final int getRows() 
    {
        return _numRows;
    }
    
    public void setStaticTileSet(Image image, int tileWidth, int tileHeight)
    {
//#if DEBUG
//#         
//#         if (image == null) throw new NullPointerException("TiledLayer::setStaticTileSet- arg Image");
//#         if (tileWidth < 1) throw new IllegalArgumentException("TiledLayer::setStaticTileSet - arg tileWidth = " + tileWidth);
//#         if (tileHeight < 1) throw new IllegalArgumentException("TiledLayer::setStaticTileSet- arg tileHeight = " + tileHeight);
//#         if ( (image.getHeight() % tileHeight) != 0 ) 
//#             throw new IllegalArgumentException("TiledLayer::setStaticTileSet - invalid tile or image height");
//#         if ( (image.getWidth() % tileWidth) != 0 ) 
//#             throw new IllegalArgumentException("TiledLayer::setStaticTileSet - invalid tile or image width");
//#         
//#else
            if (image == null)
                throw new NullPointerException();
            if ((tileWidth < 1) || (tileHeight < 1))
                throw new IllegalArgumentException();
            if ( ( (image.getHeight() % tileHeight) != 0 ) || ( (image.getWidth() % tileWidth) != 0 ) )
                throw new IllegalArgumentException();
//#endif

//#ifdef NOKIA_UI        
//#         //Guardamos la imagen con el grupo de tiles en un tileSet
//#         _tileSet.setImage(image, tileWidth, tileHeight);
//#         //Guardamos el total de tiles del tileSet
//#         _totalTiles = _tileSet.getNumFrames();
//#else
        //Guardamos la imagen con el grupo de tiles que usará este TileLayer        
        _tileSet = image;
        //Guardamos el número de tiles por filas y columnas que contiene la imagen
        _numColTiles = image.getWidth() / tileWidth;
        _numRowTiles = image.getHeight() / tileHeight;
        //Guardamos el total de tiles de la imagen        
        _totalTiles =  _numColTiles * _numRowTiles;        
//#endif
        //Tamaño del tile
        _tileHeight = tileHeight;
        _tileWidth = tileWidth;
    }
   
    // Paint Genérico
    public void paint (Graphics g)
    {
        if (!_visible)
            return;
        
//#if DEBUG
//#         //Establecemos el color para dibujar los límites de cada celda
//#         //en modo Debug
//#         g.setColor(255,0,255);
//#endif

        //Datos originales del rectángulo de clipping
        _clipX = g.getClipX();
        _clipY = g.getClipY();
        _clipH = g.getClipHeight();
        _clipW = g.getClipWidth();
       
        // Calculamos los tiles por los que vamos a iterar y, por tanto, dibujar
        // "intersecando" el mapa de tiles con el rectangulo de clipping actual
        // para obtener el primer y último tile del mapa que por los que tendremos
        // que iterar para dibujar la parte comprendida en el rectángulo de clipping
        _tileInicioX = (-_posx + _clipX) / _tileWidth;
        _tileInicioY = (-_posy + _clipY) / _tileHeight;
        _tileFinX = ( (_clipX + _clipW - _posx ) / _tileWidth ) +1;
        _tileFinY = ( (_clipY + _clipH - _posy ) /_tileHeight ) +1;        
        
         //Comprobamos límites, por si nos pasamos en los índices de los tiles
        if (_tileInicioX < 0) _tileInicioX = 0;
        if (_tileInicioY < 0) _tileInicioY = 0;
        if (_tileFinX >= _numColumns) _tileFinX = _numColumns;
        if (_tileFinY >= _numRows) _tileFinY = _numRows;
        
        
        _currentCellY = _tileInicioY * _tileHeight;
        
        //Iteramos por los tiles visibles en pantalla
        for (currentCellRow = _tileInicioY ; currentCellRow < _tileFinY; ++currentCellRow)
        {
            _currentCellX = _tileInicioX *_tileWidth;
            for (currentCellColumn = _tileInicioX; currentCellColumn < _tileFinX; ++currentCellColumn)
            {

                //Posición del tile a dibujar 
                tileIndex = _tileMap[currentCellRow * _numColumns + currentCellColumn];
                
                //Si el tile es 0 no dibujamos nada
                if (tileIndex == 0) 
                {

//#if DEBUG
//#ifndef NOKIA_UI                
//#                 //Marcamos con un rectángulo la posicion del tile actual
//#                 //Fijamos el clipping del tile  
//#                 g.setClip(_posx + _currentCellX,
//#                         _posy + _currentCellY, 
//#                         _tileWidth, 
//#                         _tileHeight);
//#endif                
//#                 //Intersecamos con el rectángulo de clipping de la pantalla
//#                 g.clipRect(_clipX, _clipY, _clipW, _clipH);                    
//#                     g.drawRect(
//#                            _posx + _currentCellX,
//#                            _posy + _currentCellY,
//#                             _tileWidth, _tileHeight);
//#endif                       
                    
                    //Actualizamos posicion celda actual
                    _currentCellX += _tileWidth;
                    
                    continue;  
                }
                                  
                //Tile animado
                if (tileIndex < 0)
                    //Cambiamos la referencia
                    tileIndex = _animatedTileReferences[-(tileIndex)];
             
                //Tiles normales (diferencia de cero)
                tileIndex--;
                
//#ifndef NOKIA_UI
                 //Obtenemos la posición del tile a dibujar en el tileSet
                 _currentTileRowOffset = (tileIndex / this._numRowTiles) * _tileHeight;
                 _currentTileColOffset = (tileIndex % this._numColTiles) * _tileWidth;
                
                //Fijamos el clipping del tile  
                g.setClip(_posx + _currentCellX,
                        _posy + _currentCellY, 
                        _tileWidth, 
                        _tileHeight);
                
                //Intersecamos con el rectángulo de clipping de la pantalla
                g.clipRect(_clipX, _clipY, _clipW, _clipH);
                
                //Dibujamos
                g.drawImage(_tileSet, 
                        _posx + _currentCellX - _currentTileColOffset,
                        _posy +_currentCellY - _currentTileRowOffset,
                        Graphics.TOP | Graphics.LEFT);
//#else
//# 
//#                 g.drawImage(_tileSet.getFrameAt(tileIndex), 
//#                         _posx + _currentCellX , 
//#                         _posy + _currentCellY , 
//#                         Graphics.TOP | Graphics.LEFT);
//# 
//#endif
                
//#if DEBUG
//#                 //Marcamos con un rectángulo la posicion del tile actual
//#                 g.drawRect(
//#                            _posx + _currentCellX,
//#                            _posy + _currentCellY,
//#                             _tileWidth, _tileHeight);
//#endif                
               //Actualizamos posicion celda actual
               _currentCellX += _tileWidth;  
            }
             
            //Actualizamos posicion celda actual 
            _currentCellY += _tileHeight;
            
        }
        //Restauramos el tamaño de clip
//#ifndef NOKIA_UI
        g.setClip(_clipX, _clipY, _clipW, _clipH);
//#endif
    }
    
     
    // Datos privados //////////////////////////////////////////////////////////
        
    //Tamaño del tile
    protected int _tileWidth;
    protected int _tileHeight;
       
    //Tiles que utilizara este tileLayer;
//#if NOKIA_UI
//#    //Tiles que utilizara este tileLayer;    
//#     protected ImageSet _tileSet;
//#else
    //Tiles que utilizara este tileLayer;
    protected Image _tileSet;
    //Número de tiles horizontales y verticales contenidas en la imagen
    protected int _numRowTiles, _numColTiles;
//#endif
    //Numero de filas y columnas
    protected int _numRows;
    protected int _numColumns;
    
    //Número de tiles del tileSet
    protected int _totalTiles;
    
    //Tile map
    protected int[] _tileMap;
    
    //Tamaño del tile map

    //Referencias a tiles animados
    protected int[] _animatedTileReferences;
    protected int _numAnimatedTiles;
    protected int _indexAnimatedTiles;
    
    
    //Necesarias para el dibujado ///////////////////////////////////////////
    
    //Vars para dibujar las celdas
    protected int currentCellColumn;
    protected int currentCellRow;
    protected int tileIndex;
    protected int _currentCellX, _currentCellY;
    
    //Inicio y final de las tiles
    protected int _tileInicioX, _tileInicioY, _tileFinX, _tileFinY;
    
//#ifndef NOKIA_UI
    //offset del tileset para dibujar el tile necesario en cada posición.
    int _currentTileColOffset, _currentTileRowOffset;
//#endif     
    //Variables temporales para guardar el rect de clipping original
    private int _clipX, _clipY, _clipH, _clipW;

 
}
