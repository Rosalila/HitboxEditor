/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rosalila.studio;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

    public BufferedImage original_image;
    public BufferedImage scaled_image;
    public int x;
    public int y;
    public double scale;
    Graphics g;

    public ImagePanel() {
        x=0;
        y=0;
        scale=1;
       try {
          original_image = ImageIO.read(new File("LogoEngine.png"));
       } catch (IOException ex) {
            // handle exception...
       }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        myScale();
        int pos_x=this.getWidth()/2-scaled_image.getWidth()/2+x;
        int pos_y=this.getHeight()-scaled_image.getHeight()-y;
        this.getHeight();
        g.drawImage(scaled_image, pos_x,pos_y, null);
    }

    void myScale()
    {
        scaled_image = new BufferedImage((int)((double)original_image.getWidth()*scale),(int)((double)original_image.getHeight()*scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaled_image.createGraphics();
        AffineTransform xform = AffineTransform.getScaleInstance(scale, scale);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.drawImage(original_image, xform, null);
        graphics2D.dispose();
    }
    void setPosition(int x,int y)
    {
        this.x=x;
        this.y=y;
    }
    void setImage(String path)
    {
        try {
          original_image = ImageIO.read(new File(path));
        } catch (IOException ex) {
            // handle exception...
        }
        repaint();
    }
}