package it.ivano.utility.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 * Provides utility methods for image processing.
 * 
 * @author <a href="mailto:ralf@terdic.de">Ralf Terdic</a>
 */
public class ImageUtilities {
	
	
	public static enum MaxImageSize {
		MAX_100K(100000), MAX_200K(200000), MAX_300K(300000), 
		MAX_400K(400000), MAX_500K(500000), MAX_1000K(1000000);
		
		private int maxSize;
 
		private MaxImageSize(int c) {
			maxSize = c;
		}

		public int getMaxSize() {
			return maxSize;
		}
	}
	
	public static enum MaxImageDimension {
		MAX_2048(2048), MAX_1280(1280), MAX_1024(1024), MAX_800(800), MAX_480K(480), MAX_320K(320);
		
		private int maxDim;
 
		private MaxImageDimension(int c) {
			maxDim = c;
		}

		public int getMaxDimension() {
			return maxDim;
		}
	}
	
	
	/**
	   * Returns the format of an image (as string).
	   * 
	   * @param stream
	   *            image source stream
	   * 
	   * @return format stream (e.g. "JPEG")
	   * 
	   * @throws IOException
	   *             if an I/O error occured
	   */
	  public static String getFormat(InputStream stream) throws IOException {
	    ImageInputStream iis = ImageIO.createImageInputStream(stream);
	    Iterator iter = ImageIO.getImageReaders(iis);
	    if (!iter.hasNext()) {
	      throw new IOException("Unsupported image format!");
	    }
	    ImageReader reader = (ImageReader) iter.next();
	    iis.close();
	    return reader.getFormatName();
	  }

	  /**
	   * Loads an image from a stream.
	   * 
	   * @param stream
	   *            input stream
	   * 
	   * @return the loaded image
	   * 
	   * @throws IOException
	   *             if the image could not be loaded
	   */
	  public static BufferedImage loadImage(InputStream stream)
	      throws IOException {
	    return ImageIO.read(stream);
	  }

	  /**
	   * Writes an image to an output stream as a JPEG file.
	   * 
	   * @param image
	   *            image to be written
	   * @param stream
	   *            target stream
	   * 
	   * @throws IOException
	   *             if an I/O error occured
	   */
	  public static void saveImageAsJPEG(BufferedImage image, OutputStream stream)
	      throws IOException {
	    ImageIO.write(image, "jpg", stream);
	  }

	  /**
	   * Writes an image to an output stream as a JPEG file. The JPEG quality can
	   * be specified in percent.
	   * 
	   * @param image
	   *            image to be written
	   * @param stream
	   *            target stream
	   * @param qualityPercent
	   *            JPEG quality in percent
	   * 
	   * @throws IOException
	   *             if an I/O error occured
	   * @throws IllegalArgumentException
	   *             if qualityPercent not between 0 and 100
	   */
	  public static void saveImageAsJPEG(BufferedImage image,
	      OutputStream stream, int qualityPercent) throws IOException {
	    if ((qualityPercent < 0) || (qualityPercent > 100)) {
	      throw new IllegalArgumentException("Quality out of bounds!");
	    }
	    float quality = qualityPercent / 100f;
	    ImageWriter writer = null;
	    Iterator iter = ImageIO.getImageWritersByFormatName("jpg");
	    if (iter.hasNext()) {
	      writer = (ImageWriter) iter.next();
	    }
	    ImageOutputStream ios = ImageIO.createImageOutputStream(stream);
	    writer.setOutput(ios);
	    ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
	    iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	    iwparam.setCompressionQuality(quality);
	    writer.write(null, new IIOImage(image, null, null), iwparam);
	    ios.flush();
	    writer.dispose();
	    ios.close();
	  }

	  /**
	   * Writes an image to an output stream as a PNG file.
	   * 
	   * @param image
	   *            image to be written
	   * @param stream
	   *            target stream
	   * 
	   * @throws IOException
	   *             if an I/O error occured
	   */
	  public static void saveImageAsPNG(BufferedImage image, OutputStream stream)
	      throws IOException {
	    ImageIO.write(image, "png", stream);
	  }

	  private static BufferedImage convertToARGB(BufferedImage srcImage) {
	    BufferedImage newImage = new BufferedImage(srcImage.getWidth(null),
	        srcImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	    Graphics bg = newImage.getGraphics();
	    bg.drawImage(srcImage, 0, 0, null);
	    bg.dispose();
	    return newImage;
	  }
	}