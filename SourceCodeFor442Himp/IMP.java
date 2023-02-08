/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class. 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.prefs.Preferences;

class IMP implements MouseListener{
   JFrame frame;
   JPanel mp;
   JButton start;
   JScrollPane scroll;
   JMenuItem openItem, exitItem, resetItem;
   Toolkit toolkit;
   File pic;
   ImageIcon img;
   int colorX, colorY;
   int [] pixels;
   int [] results;
   //Instance Fields you will be using below
   
   //This will be your height and width of your 2d array
   int height=0, width=0;
   int firstWidth;
   int firstHeight;
   
   //your 2D array of pixels
    int picture[][];

    /* 
     * In the Constructor I set up the GUI, the frame the menus. The open pull down
     * menu is how you will open an image to manipulate. 
     */
   IMP()
   {
      toolkit = Toolkit.getDefaultToolkit();
      frame = new JFrame("Image Processing Software by Hunter");
      JMenuBar bar = new JMenuBar();
      JMenu file = new JMenu("File");
      JMenu functions = getFunctions();
      frame.addWindowListener(new WindowAdapter(){
            @Override
              public void windowClosing(WindowEvent ev){quit();}
            });
      openItem = new JMenuItem("Open");
      openItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ handleOpen(); }
           });
      resetItem = new JMenuItem("Reset");
      resetItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ reset(); }
           });     
      exitItem = new JMenuItem("Exit");
      exitItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ quit(); }
           });
      file.add(openItem);
      file.add(resetItem);
      file.add(exitItem);
      bar.add(file);
      bar.add(functions);
      frame.setSize(600, 600);
      mp = new JPanel();
      mp.setBackground(new Color(0, 0, 0));
      scroll = new JScrollPane(mp);
      frame.getContentPane().add(scroll, BorderLayout.CENTER);
      JPanel butPanel = new JPanel();
      butPanel.setBackground(Color.black);
      start = new JButton("start");
      start.setEnabled(false);
      start.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){ fun1(); }
           });
      butPanel.add(start);
      frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
      frame.setJMenuBar(bar);
      frame.setVisible(true);      
   }
   
   /* 
    * This method creates the pull down menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods
    * for handling the choice, fun1, fun2, fun3, fun4, etc. etc. 
    */
   
  private JMenu getFunctions()
  {
     JMenu fun = new JMenu("Functions");
     
     JMenuItem firstItem = new JMenuItem("MyExample - fun1 method");
    
     firstItem.addActionListener(new ActionListener(){
            @Override
          public void actionPerformed(ActionEvent evt){fun1();}
           });

      JMenuItem Rotate = new JMenuItem("Rotate");

      Rotate.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){rotate_90();}
      });

      JMenuItem GrayScale = new JMenuItem("Grayscale");

      GrayScale.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){grayscale();}
      });

      JMenuItem Blur = new JMenuItem("Blur");

      Blur.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){blur();}
      });

      JMenuItem FiveByFive = new JMenuItem("5x5");

      FiveByFive.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){fiveByFiveMask();}
      });

      JMenuItem Histogram = new JMenuItem("Histogram");

      Histogram.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){histogram();}
      });

       
      fun.add(firstItem);
      fun.add(Rotate);
      fun.add(GrayScale);
      fun.add(Blur);
      fun.add(FiveByFive);
      fun.add(Histogram);

      return fun;   

  }
  
  /*
   * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame. 
   * You don't need to worry about this method. 
   */
    private void handleOpen()
  {  
     img = new ImageIcon();
     JFileChooser chooser = new JFileChooser();
      Preferences pref = Preferences.userNodeForPackage(IMP.class);
      String path = pref.get("DEFAULT_PATH", "");

      chooser.setCurrentDirectory(new File(path));
     int option = chooser.showOpenDialog(frame);
     
     if(option == JFileChooser.APPROVE_OPTION) {
        pic = chooser.getSelectedFile();
        pref.put("DEFAULT_PATH", pic.getAbsolutePath());
       img = new ImageIcon(pic.getPath());
      }
     width = img.getIconWidth();
     height = img.getIconHeight();
     firstHeight = height;
     firstWidth = width;
     
     JLabel label = new JLabel(img);
     label.addMouseListener(this);
     pixels = new int[width*height];
     
     results = new int[width*height];
  
          
     Image image = img.getImage();
        
     PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width );
     try{
         pg.grabPixels();
     }catch(InterruptedException e)
       {
          System.err.println("Interrupted waiting for pixels");
          return;
       }
     for(int i = 0; i<width*height; i++)
        results[i] = pixels[i];  
     turnTwoDimensional();
     mp.removeAll();
     mp.add(label);
     
     mp.revalidate();
  }
  
  /*
   * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
   * So this method changes the one dimensional array to a two-dimensional. 
   */
  private void turnTwoDimensional()
  {
     picture = new int[height][width];
     for(int i=0; i<height; i++)
       for(int j=0; j<width; j++)
          picture[i][j] = pixels[i*width+j];
      
     
  }
  /*
   *  This method takes the picture back to the original picture
   */
  private void reset()
  {
        for(int i = 0; i<width*height; i++)
             pixels[i] = results[i]; 
       Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width)); 

      JLabel label2 = new JLabel(new ImageIcon(img2));    
       mp.removeAll();
       mp.add(label2);
       mp.revalidate();
       width = firstWidth;
       height = firstHeight;
       turnTwoDimensional();

    }
  /*
   * This method is called to redraw the screen with the new image. 
   */
  private void resetPicture()
  {
       for(int i=0; i<height; i++)
           for (int j = 0; j < width; j++)
               pixels[i * width + j] = picture[i][j];
           Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));

           JLabel label2 = new JLabel(new ImageIcon(img2));
           mp.removeAll();
           mp.add(label2);

           mp.revalidate();
           mp.repaint();
    }
    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
  private int [] getPixelArray(int pixel)
  {
      int temp[] = new int[4];
      temp[0] = (pixel >> 24) & 0xff;
      temp[1]   = (pixel >> 16) & 0xff;
      temp[2] = (pixel >>  8) & 0xff;
      temp[3]  = (pixel      ) & 0xff;
      return temp;
      
    }
    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer. 
     */
  private int getPixels(int rgb[])
  {
         int alpha = 0;
         int rgba = (rgb[0] << 24) | (rgb[1] <<16) | (rgb[2] << 8) | rgb[3];
        return rgba;
  }
  
  public void getValue()
  {
      int pix = picture[colorY][colorX];
      int temp[] = getPixelArray(pix);
      System.out.println("Color value " + temp[0] + " " + temp[1] + " "+ temp[2] + " " + temp[3]);
    }
  
  /**************************************************************************************************
   * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is 
   * used. As long as you have a picture open first then when your fun1, fun2, fun....etc method is called you will
   * have a 2D array called picture that is holding each pixel from your picture. 
   *************************************************************************************************/
   /*
    * Example function that just removes all red values from the picture. 
    * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array 
    * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
    * getPixelArray will breaks down your single int to 4 ints, so you can manipulate the values for each level of R, G, B.
    * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
    * integer value, so you can give it back to the program and display the new picture.
    */
  private void fun1()
  {
     
    for(int row=0; row<height; row++)
       for(int column=0; column<width; column++)
       {   
          int rgbArray[] = new int[4];
         
          //get three ints for R, G and B
          rgbArray = getPixelArray(picture[row][column]);
         
        
           rgbArray[1] = 0;
           //take three ints for R, G, B and put them back into a single int
           picture[row][column] = getPixels(rgbArray);
        } 
     resetPicture();
  }


  private void rotate_90()
  {
      //Turn old image black.
      int[][] coveredImage = picture.clone();
      picture = new int[height][width];
      resetPicture();
      picture = coveredImage.clone();

      //Rotate image
      int[][] rotatedPic = new int[width][height];

      for(int i=0;i<width;i++) {
          for(int j=0;j<height;j++) {
              rotatedPic[i][j] = picture[j][i];
          }
      }

      for(int i=0;i<width;i++) {
          int start = 0;
          int end = rotatedPic[i].length - 1;
          while(start < end) {
              int temp = rotatedPic[i][start];
              rotatedPic[i][start] = rotatedPic[i][end];
              rotatedPic[i][end] = temp;
              start++;
              end--;
          }
      }

      picture = rotatedPic;
      int temp = width;
      width = height;
      height = temp;
      resetPicture();
  }

  private void grayscale()
  {
      int[] rgbArray;

      for(int col=0; col<height; col++) {
          for (int row = 0; row < width; row++) {
              //get three ints for R, G and B
              rgbArray = getPixelArray(picture[col][row]);
              int gray = (int) ((rgbArray[1]*0.2126) + (rgbArray[2]*0.7152) + (rgbArray[3]*0.0722));
              rgbArray[1] = gray;
              rgbArray[2] = gray;
              rgbArray[3] = gray;

              //take three ints for R, G, B and put them back into a single int
              picture[col][row] = getPixels(rgbArray);
          }
      }
      resetPicture();
  }

  private void blur()
  {
      grayscale();
      int[][] blurredImage = new int[height][width];
      int[] rgbArray = new int[4];

      for (int row = 1; row < height-1; row++) {
          for (int column = 1; column < width-1; column++) {
              int[] total = new int[4];
              total[0] = 255;
              rgbArray = getPixelArray(picture[row-1][column-1]);
              total[1] += rgbArray[1];
              total[2] += rgbArray[2];
              total[3] += rgbArray[3];

              rgbArray = getPixelArray(picture[row-1][column]);
              total[1] += rgbArray[1];
              total[2] += rgbArray[2];
              total[3] += rgbArray[3];

              rgbArray = getPixelArray(picture[row-1][column+1]);
              total[1] += rgbArray[1];
              total[2] += rgbArray[2];
              total[3] += rgbArray[3];

              rgbArray = getPixelArray(picture[row][column-1]);
              total[1] += rgbArray[1];
              total[2] += rgbArray[2];
              total[3] += rgbArray[3];

              rgbArray = getPixelArray(picture[row][column+1]);
              total[1] += rgbArray[1];
              total[2] += rgbArray[2];
              total[3] += rgbArray[3];

              rgbArray = getPixelArray(picture[row+1][column-1]);
              total[1] += rgbArray[1];
              total[2] += rgbArray[2];
              total[3] += rgbArray[3];

              rgbArray = getPixelArray(picture[row+1][column]);
              total[1] += rgbArray[1];
              total[2] += rgbArray[2];
              total[3] += rgbArray[3];

              rgbArray = getPixelArray(picture[row+1][column+1]);
              total[1] += rgbArray[1];
              total[2] += rgbArray[2];
              total[3] += rgbArray[3];

              total[1] /= 8;
              total[2] /= 8;
              total[3] /= 8;
              blurredImage[row][column] = getPixels(total);
          }
      }
      picture = blurredImage;
      resetPicture();
  }

  private int[][] fiveByFiveMask(){
      int[][] maskedImage = new int[height][width];
      int[] rgbArray = new int[4];
      grayscale();
      for (int row = 2; row < height-2; row++) {
          for (int column = 2; column < width - 2; column++) {
              int total = 0;
              for (int i = -2; i < 2; i+=4) {
                  for (int j = -2; j < 2; j++) {
                      rgbArray = getPixelArray(picture[row+i][column+j]);
                      total += -1 * rgbArray[1];
                  }
                  for (int j = 2; j < 3; j++) {
                      rgbArray = getPixelArray(picture[row+i][column+j]);
                      total += -1 * rgbArray[1];
                  }
              }
              rgbArray = getPixelArray(picture[row-1][column-2]);
              total += -1 * rgbArray[1];

              rgbArray = getPixelArray(picture[row][column-2]);
              total += -1 * rgbArray[1];

              rgbArray = getPixelArray(picture[row+2][column-2]);
              total += -1 * rgbArray[1];

              rgbArray = getPixelArray(picture[row+1][column-2]);
              total += -1 * rgbArray[1];

              rgbArray = getPixelArray(picture[row][column]);
              total += 16 * rgbArray[1];

              if(total > 1500){
                  rgbArray = getPixelArray(picture[row][column]);
                  for (int i = 0; i < 4; i++) {
                      rgbArray[i] = 255;
                  }
              }else {
                  rgbArray[0] = 255;
                  for (int i = 1; i < 4; i++) {
                      rgbArray[i] = 0;
                  }
              }
              maskedImage[row][column] = getPixels(rgbArray);
          }
      }
      picture = maskedImage;
      resetPicture();
      return maskedImage;
  }


  private void histogram() {
      int [] red = new int[255];
      //red array = getHistogram


      JFrame hist = new JFrame();
      hist.setLayout(new BorderLayout(0,0));
      hist.setSize(900, 300);

      MyPanel redPanel = new MyPanel(red);
      redPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      redPanel.setOpaque(true);
      redPanel.setPreferredSize(new Dimension(300,300));
      hist.add(redPanel, BorderLayout.WEST);

      hist.setVisible(true);
  }

  private void quit()
  {  
     System.exit(0);
  }

    @Override
   public void mouseEntered(MouseEvent m){}
    @Override
   public void mouseExited(MouseEvent m){}
    @Override
   public void mouseClicked(MouseEvent m){
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        start.setEnabled(true);
    }
    @Override
   public void mousePressed(MouseEvent m){}
    @Override
   public void mouseReleased(MouseEvent m){}
   
   public static void main(String [] args)
   {
      IMP imp = new IMP();
   }
 
}