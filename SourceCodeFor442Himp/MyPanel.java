
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MyPanel extends JPanel
{
 
int startX, flag, startY, endX, endY;

    BufferedImage grid;
    Graphics2D gc;
    int [] color_array;

	public MyPanel(int[] color_array)
	{
	   startX = startY = 0;
           endX = endY = 100;
           this.color_array = color_array;
 	}

     public void clear()
    {
       grid = null;
       repaint();
    }
    public void paintComponent(Graphics g)
    {  
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D)g;
         if(grid == null){
            int w = this.getWidth();
            int h = this.getHeight();
            grid = (BufferedImage)(this.createImage(w,h));
            gc = grid.createGraphics();

         }
         g2.drawImage(grid, null, 0, 0);
     }
    public void drawing()
    {
        for (int i = 0; i < 256; i++) {
            gc.drawLine(i, 600, i, 550-color_array[i]);
            repaint();
        }
        repaint();
    }
}
