/*
 * LayerManager.java
 *
 * @author Ricardo Amores Hernández
 */

package MIDP1GameAPI;

import javax.microedition.lcdui.*;

/**
 * Gestiona un grupo de capas para su dibujado.
 *
 * Permite gestionar grupos de capas para su posterior dibujado en pantalla
 * El método SetViewWindow permite establecer la ventana de visualización
 * la cual servirá para dibujar una parte del conjunto de las capas. 
 *
 * Las capas se dibujan según el orden de inserción, dibujando primero las  
 * capas insertadas en última posición (LIFO)

 * Implementa una funcionalidad semejante a la clase LayerManager definida en 
 * MIDP 2.0
 * @see javax.microedition.lcdui.game#LayerManager
 */
public class LayerManager 
{
/**
 * Nodo para guardar las diferentes layers en un LayerManager
 */
private class ListNode 
{
    //Layer de juego
    public Layer layer;
    
    //Lista doblemente enlazada
    public ListNode next;
    public ListNode last;
    
    ListNode() {
        layer = null;
        next = last = null;
    }
    
    ListNode(Layer l) {
        layer = l;
        next = last = null;
    }
}

    /**
     * Crea un nuevo LayerManager
     **/
    public LayerManager() 
    {
        //Inicializamos a null las referencias a los elementos de la lista
        _firstElement = _lastElement = null;
        
        _VWwidth = _VWheight = Integer.MAX_VALUE;
    }
    
    /**
     * Añade una capa al layerManager al final de la lista, lo que a efectos prácticos
     * equivale a dibujarla por encima de todas las demás.
     */
    public void append(Layer l) 
    {
        //Comprobamos errores en los argumentos
        //#if DEBUG
//#         if (l == null)
//#             throw new NullPointerException("LayerManager::append - Layer arg is null");
        //#else
        if (l == null)
           throw new NullPointerException();
        //#endif         
        //Eliminamos el layer si existia antes en la lista
        remove(l);
        
        //Creamos un nuevo nodo con el layer
        itr = new ListNode(l);
        
        //Lo introducimos al final de la lista
        //Si la lista esta vacia es un caso especial
        if (_numLayers == 0)
            _firstElement = _lastElement = itr;
        
        //Lista no vacia
        else
        {
            itr.last = _lastElement;
            _lastElement.next = itr;
            _lastElement = itr;
        }
        
        //Actualizamos contador de layers
        _numLayers++;
        
        //Eliminamos referencias
        itr = null;
    }
    
    /**
     * Inserta una capa en una posición determinada
     */
    public void insert(Layer l, int index) 
    {
        //Comprobamos errores en los argumentos
        //#if DEBUG
//#         if (l == null)
//#             throw new NullPointerException("LayerManager::insert - Layer arg is null");
//#         if (index < 0 || index >= _numLayers )
//#             throw new IndexOutOfBoundsException("LayerManager::insert - index = " + index);
        //#else
        if (l == null)
           throw new NullPointerException();
        if (index < 0 || index >= _numLayers )
            throw new IndexOutOfBoundsException();
//#endif         
        //Borramos el layer por si estaba insertado
        remove(l);
        
        //Recorremos la lista completamente. Al llegar al índice indicado 
        //insertamos el layer, y
        
        //Recorremos la lista
        int i = 0;
        itr = _firstElement;
        ListNode tmpNode;
        
        while (itr != null)
        {
            
            //Si llegamos al índice insertamos el nodo
            if (index == i)
            {
                //Si intentamos insertar el mismo layer en la misma posición 
                //retornamos sin hacer nada
                if (itr.layer == l)
                {
                    //Borramos referencias
                    itr = null;
                    return;
                }
                
                //Creamos el nuevo nodo que contrendra al layer
                tmpNode= new ListNode(l);
                
                //Caso: primer elemento
                if (itr == _firstElement)
                {
                    tmpNode.next = _firstElement;
                    itr.last = tmpNode;
                    
                    //El nodo insertado es ahora el primer elemento
                    _firstElement = tmpNode;
                }
                //Caso: último elemento
                else if (itr == _lastElement)
                {
                    //Enlazamos el nuevo nodo
                    tmpNode.last = itr.last;
                    tmpNode.next = itr;
                    
                    itr.last.next = tmpNode;
                    itr.last = tmpNode;
                }
                //Insercción en otra posición
                else
                {
                    tmpNode.last = itr.last;
                    tmpNode.next = itr;
                    
                    //Insertamos el layer
                    itr.last.next = tmpNode;
                    itr.last = tmpNode;
                    

                }
                
                //Actualizamos contador de layers
                _numLayers++;
            }
            
            //siguiente elemento
            itr = itr.next;
            i++;
        }
        
        //Caso especial: si insertamos en la última posición y el layer estaba
        //repetido, hay que insertar el elemento al final de la cola
        if (i == _numLayers && _lastElement.layer != l)
        {
            tmpNode = new ListNode(l);
            
            _lastElement.next = tmpNode;
            tmpNode.last = _lastElement;
            _lastElement = tmpNode;
            
            //Actualizamos contador de layers
            _numLayers++;
        }
        
        //Eliminamos referencias
        itr = null;
    }
    
    /**
     * Obtiene la referencia a una capa en una posición determinada
     */
    public Layer getLayerAt(int index) 
    {
        //Comprobamos errores en los argumentos
        //#if DEBUG
//#         if (index < 0 || index >= _numLayers )
//#             throw new IndexOutOfBoundsException("LayerManager::getLayerAt - index = " + index);
        //#else
        if (index < 0 || index >= _numLayers )
            throw new IndexOutOfBoundsException();
        //#endif         
        //Recorremos el bucle, al salir del mismo itr apunta al nodo con el 
        //layer requerido
        int i = 0;
        itr = _firstElement;
        
        //Siempre encontraremos una coincidencia con el indice ya que hemos
        //comprobado la validez de index al inicio del método.
        while (i != index)
        {
            itr = itr.next;
            i++;
        }
        
        //temporal para el layer
        Layer tmp = itr.layer;
        
        //Eliminamos referencias
        itr = null;
        
        return tmp;
    }
    
    /**
     * Devuelve el número de capas contenidas en el LayerManager
     */
    public int getSize() 
    {
        return _numLayers;
    }
    
    /**
     * Realiza el dibujado de las capas contenidas en el layerManager
     */
    public void paint(Graphics g, int x, int y)
    {
        if (_numLayers == 0)
            return;
        
        //Guardamos el rectángulo de clipping anterior
        cx = g.getClipX();
        cy = g.getClipY();
        ch = g.getClipHeight();
        cw = g.getClipWidth();
        
        //Fijamos el nuevo rectángulo de clipping con las coordenadas
        //del viewWindow actual
        g.setClip(x, y, _VWwidth, _VWheight);
        
        //Recorremos la lista al reves
        itr = _lastElement;
        
        while (itr != null)
        {
            //Sólo dibujamos si el layer es visible
            if (itr.layer._visible)
            {
                //movemos el layer en la posición           
                itr.layer.setPosition(itr.layer._posx -_VWx + x, itr.layer._posy - _VWy + y);
                
                //Dibujamos el layer
                itr.layer.paint(g);
                
                //Dejamos la posición del layer como estaba
                itr.layer.setPosition(itr.layer._posx +_VWx - x, itr.layer._posy + _VWy - y);                
            
            }
            
            //Siguiente elemento
            itr = itr.last;
        }
        
        //Reestablecemos el rectángulo de clipping
        g.setClip(cx, cy, cw, ch);
    }
   
    /**
     * Elimina una capa determinada
     */
    public void remove(Layer l) 
    {
        //Comprobamos errores en los argumentos
        //#if DEBUG
//#         if (l == null)
//#             throw new NullPointerException("LayerManager::remove - Layer arg is null");
        //#else
        if (l == null)
           throw new NullPointerException();
        //#endif         
        //Caso especial: si sólo hay un elemento en la lista y hay coincidencia 
        //lo borramos directamente
        if (_numLayers == 1 )
        {
            //Si hay coincidencia borramos el layer
            if (_firstElement.layer == l)
            {
                //Borramos la referencia al layer.
                _firstElement.layer = null;
                _firstElement = _lastElement = null;
                _numLayers = 0;
            }
            //si no la hay retornamos directamente
            else return;
        }

        //Recorremos la lista y eliminamos el layer si hay una coincidencia
        itr = _firstElement;
        
        while (itr != null)
        {
            //Hay coincidencia, eliminamos el layer
            if (itr.layer == l)
            {
                //Actualizamos lista
                
                //casos especiales: borrar el primer y el ultimo elemento de la lista
                //Primer elemento
                if (itr == _firstElement)
                {
                    itr.next.last = null;
                    _firstElement = itr.next;
                }
                //segundo elemento
                else if (itr == _lastElement)
                {
                    itr.last.next = null;
                    _lastElement = itr.last;
                }
                
                else
                {
                    itr.last.next = itr.next;
                    itr.next.last = itr.last;
                }
                //Actualizamos contador de layers
                _numLayers--;
            }
            
            //Siguiente elemento
            itr = itr.next;
        }
        
        //Eliminamos referencias
        itr = null;        
    }
    
    /**
     *
     */
    public  void setViewWindow(int x, int y, int width, int height) 
    {
        //#if DEBUG
//#             if ( width < 0)
//#                 throw new IllegalArgumentException("LayerManager::setViewWindow - width = " + width);
//#             if ( height < 0)
//#                 throw new IllegalArgumentException("LayerManager::setViewWindow - height = " + width);            
        //#else
            if ( width < 0 || height < 0 )
                throw new IllegalArgumentException();            
        //#endif
            
        _VWx = x;
        _VWy = y;
        _VWwidth = width;
        _VWheight = height;
    }
    
    ///////////////////////////////////////////////////////////////////
    //Elementos privados
    
    //Referencias a los elementos inicial y final de la lista
    private ListNode _firstElement, _lastElement;
    
    //Variable temporal para recorrer la lista
    private ListNode itr;
    
    //Total de layers
    private int _numLayers;
    
    //Posición y tamaño del viewWindow
    int _VWx, _VWy, _VWwidth, _VWheight;
    
    //Variables para guardar el rectángulo de clipping y restaurarlo
    int cx, cy, cw, ch;
}

