/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import javax.swing.ImageIcon;

/**
 *
 * @author Lawrence, Jacob
 */
public class QRCode {
    public String value = "";
    public int width = 100;
    public int height = 100;
    
    public static final int BLACK = 0xFF000000;
    public static final int WHITE = 0xFFFFFFFF;
    
    private BitMatrix bits = null;
    
    QRCode(String val) {
        this.value = val;
        generateQRCode();
    }
    
    private void generateQRCode() {
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetEncoder encoder = charset.newEncoder();
        byte[] b = null;
        
        try {
            //Convert string to ISO-8859-1 bytes
            ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(this.value));
            b = bbuf.array();
        } catch (CharacterCodingException e) {
            System.out.println(e.getMessage());
        }
        
        String data = null;
        try {
            data = new String(b, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        
        //get a byte a matrix
        BitMatrix matrix = null;
        Writer wrt = new QRCodeWriter();
        try {
            matrix = wrt.encode(data, BarcodeFormat.QR_CODE, this.width, this.height);
        } catch (WriterException e) {
            System.out.println(e.getMessage());
        }
        
        this.bits = matrix;
    }
    
    //returns the QRCode as an ImageIcon
    public ImageIcon getImageIcon() {
        BufferedImage i = toBufferedImage(this.bits);
        //the image icon constructor only support Images, not BufferedImages
        //but they can be typecast
        return new ImageIcon((Image)i);
    }
    
    //Converts BitMatrix to BufferedImage (copied from zxing)
    //Returns a BufferedImage
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getWidth();
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int x=0; x<w; x++) {
            for (int y=0; y<h; y++) {
                img.setRGB(x,y,matrix.get(x,y) ? BLACK : WHITE);
            }
        }
        return img;
    }
    
}
