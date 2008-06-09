/*
 * ImageSet.java
 *
 * Created on 19 de diciembre de 2005, 13:25
 *
 */

package MIDP1GameAPI_Utils;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;
import java.lang.Exception;

//#ifdef NOKIA_UI
//# import com.nokia.mid.ui.*;
//#endif

/**
 * Encapsula un conjundo de frames para una imagen. 
 * 
 * Requiere Nokia UI para crear imágenes con transparencia
 * 
 * @author Ricardo Amores Hernández
 */
public class ImageSet {
    
    /** Crea una nueva instancia de un ImageSet */
    public ImageSet(Image image, int frameWidth, int frameHeight) 
    {
        try{
            setImage(image, frameWidth, frameHeight);
        } 
        catch(Exception e)
        {
            e.toString();
        }
        
    }
    /**
     *
     */
    public void setImage(Image image, int frameWidth, int frameHeight)
    {
   //#if DEBUG
//#         if (image == null) throw new NullPointerException("ImageSet Constructor- arg Image");
//#         if (frameWidth < 1) throw new IllegalArgumentException("ImageSet Constructor- arg frameWidth < 1");
//#         if (frameHeight < 1) throw new IllegalArgumentException("ImageSet Constructor- arg frameHeight <1");
//#         if ( (image.getHeight() % frameHeight) != 0 ) throw new IllegalArgumentException("ImageSet Constructor- invalid frame or image height");
//#         if ( (image.getWidth() % frameWidth) != 0 ) throw new IllegalArgumentException("ImageSet Constructor- invalid frame or image width");
//#         
//#else
            if (image == null)
                throw new NullPointerException();
            if ((frameWidth < 1) || (frameHeight < 1))
                throw new IllegalArgumentException();
            if ( ( (image.getHeight() % frameHeight) != 0 ) || ( (image.getWidth() % frameWidth) != 0 ) )
                throw new IllegalArgumentException();
//#endif
            
        //Graficos de nokia
        DirectGraphics dg; 

        //Total de frames en el ImageSet
        _totalFrames = (image.getWidth() / frameWidth) * (image.getHeight() / frameHeight);
        
        //Creamos un array de imágenes
        _frameSet = new Image[ _totalFrames ];
        
        //Copiamos las imagenes
        for (int i=0; i<(image.getHeight() / frameHeight); ++i)
            for (int j=0; j<(image.getWidth() / frameWidth); ++j)
            {
                //imagen con transparencia
                try
                {
                    Image tmp = DirectUtils.createImage(frameWidth, frameHeight, DirectGraphics.TYPE_INT_8888_ARGB);
                    dg = DirectUtils.getDirectGraphics(tmp.getGraphics());
                    dg.drawImage(image, -(j*frameWidth), -(i*frameHeight),  Graphics.TOP |Graphics.LEFT, 0);
                    _frameSet[i * (image.getWidth() / frameWidth) + j] = tmp;
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }

            }
        
        //Tamaño de cada frame
        _frameWidth = frameWidth;
        _frameHeight = frameHeight;
     
    }
    
    public final int getNumFrames()
    {
        return _totalFrames;
    }
    
    public final int getFrameWidth() 
    {
        return _frameWidth;
    }

    public final int getFrameHeight() 
    {
        return _frameHeight;
    }
    
    public final Image getFrameAt (int index)
    {
        return _frameSet[index];
    }
    
    // Datos privados //////////////////////////////////////////////////////////    
    
    //Array con los frames en los que se divide la imagen
    private Image[] _frameSet;
    
    //Total de frames del frameSet
    private int _totalFrames;
    
    //Tamaño de cada frame
    private int _frameWidth, _frameHeight;
    
    
}
