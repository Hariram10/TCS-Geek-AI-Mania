import java.util.List;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class Hello {
	static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    static Mat imag = null;
 
    public static void main(String[] args) {
 
        JFrame jframe = new JFrame("HUMAN MOTION DETECTOR");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel vidpanel = new JLabel();
        jframe.setContentPane(vidpanel);
        jframe.setSize(640, 480);
        jframe.setVisible(true);
 
        Mat frame = new Mat();
        Mat outerBox = new Mat();
        Mat diff_frame = null;
        Mat tempon_frame = null;
        ArrayList<Rect> array = new ArrayList<Rect>();
        
        VideoCapture camera = new VideoCapture("C:/Users/HP-PC/Pictures/Camera Roll/WIN_20181007_06_35_52_Pro.mp4");
        Size sz = new Size(640, 480);
        int i=0;
        while (true) {
            if (camera.read(frame)) {
                Imgproc.resize(frame, frame, sz);
                outerBox = new Mat(frame.size(), CvType.CV_8UC1);
                Imgproc.cvtColor(frame, outerBox, Imgproc.COLOR_BGR2GRAY);
                Imgproc.GaussianBlur(outerBox, outerBox, new Size(3, 3), 0);
                
              /*  if (i == 0) {
                    jframe.setSize(frame.width(), frame.height());
                    diff_frame = new Mat(outerBox.size(), CvType.CV_8UC1);
                    tempon_frame = new Mat(outerBox.size(), CvType.CV_8UC1);
                    diff_frame = outerBox.clone();
                }
                if (i == 1) {
                    Core.subtract(outerBox, tempon_frame, diff_frame);
                    Imgproc.adaptiveThreshold(diff_frame, diff_frame, 255,
                            Imgproc.ADAPTIVE_THRESH_MEAN_C,
                            Imgproc.THRESH_BINARY_INV, 5, 2);
                    array = detection_contours(diff_frame);
                    if (array.size() > 0) {
 
                        Iterator<Rect> it2 = array.iterator();
                        while (it2.hasNext()) {
                            Rect obj = it2.next();
                            Imgproc.rectangle(imag, obj.br(), obj.tl(),
                                    new Scalar(0, 255, 0), 1);
                       }
                    }
                }
                i = 1;
                
                              tempon_frame = outerBox.clone();
 
            }
            }*/
                ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
                vidpanel.setIcon(image);
                vidpanel.repaint();
            }
        }
    }
    public static BufferedImage Mat2bufferedImage(Mat image) {
        MatOfByte bytemat = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, bytemat);
        byte[] bytes = bytemat.toArray();
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage img = null;
        try {
            img = ImageIO.read(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }
    public static ArrayList<Rect> detection_contours(Mat outmat) {
        Mat v = new Mat();
        Mat vv = outmat.clone();
       List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(vv, contours, v, Imgproc.RETR_LIST,
                Imgproc.CHAIN_APPROX_SIMPLE);
 
        double maxArea = 100;
        int maxAreaIdx = -1;
        Rect r = null;
        ArrayList<Rect> rect_array = new ArrayList<Rect>();
 
        for (int idx = 0; idx < contours.size(); idx++) { Mat contour = contours.get(idx); double contourarea = Imgproc.contourArea(contour); if (contourarea > maxArea) {
                //maxArea = contourarea;
                maxAreaIdx = idx;
                r = Imgproc.boundingRect(contours.get(maxAreaIdx));
                rect_array.add(r);
              //  Imgproc.drawContours(imag, contours, maxAreaIdx, new Scalar(0,0, 255));
                Imgproc.drawContours(imag, contours, maxAreaIdx, new Scalar(0, 0, 255));
            }
 
        }
        v.release();
 
        return rect_array;
 
    }
    
 }
	