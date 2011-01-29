package com.vaadin.testbench.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

public class ImageUtil {

    /**
     * Decodes target string from base64 to byteArray that is converted to an
     * image
     * 
     * @param imageString
     *            Base64 encoded image
     * @return BufferedImage
     */
    public static BufferedImage stringToImage(String imageString) {
        // string to ByteArrayInputStream
        BufferedImage bImage = null;
        Base64 b64dec = new Base64();
        try {
            byte[] output = b64dec.decode(imageString.getBytes());
            ByteArrayInputStream bais = new ByteArrayInputStream(output);
            bImage = ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bImage;
    }

    /**
     * Encodes target image to a Base64 string
     * 
     * @param image
     *            BufferedImage to encode to String
     * @return Base64 encoded String of image
     */
    public static String encodeImageToBase64(BufferedImage image) {
        String encodedImage = "";
        Base64 encoder = new Base64();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] encodedBytes = encoder.encode(baos.toByteArray());
            encodedImage = new String(encodedBytes);
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedImage;
    }

    /**
     * Generates a grayscale image of give image.
     * 
     * @param image
     *            Image to turn to grayscale
     * @return BufferedImage [TYPE_BYTE_GRAY]
     */
    public static BufferedImage grayscaleImage(BufferedImage image) {
        BufferedImage gray = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D g = (Graphics2D) gray.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();

        return gray;
    }

    /**
     * Generate a black and white image.
     * 
     * @param image
     *            Image to turn to a b&w image
     * @return BufferedImage in B&W
     */
    public static BufferedImage getBlackAndWhiteImage(BufferedImage image) {
        BufferedImage bw = grayscaleImage(image);
        threshold(bw);
        return bw;
    }

    /**
     * Generates a white image with the image differences in black. Image sizes
     * are expected to be the same.
     * 
     * @param image1
     *            First image
     * @param image2
     *            Second image
     * @return B&W image
     */
    public static BufferedImage getDifference(BufferedImage image1,
            BufferedImage image2) {
        // Get black and white images for both images
        BufferedImage bw1 = getBlackAndWhiteImage(image1);
        BufferedImage bw2 = getBlackAndWhiteImage(image2);

        // Create empty image
        BufferedImage diff = new BufferedImage(image1.getWidth(),
                image1.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        // For each pixel, if pixel in both images equal "draw" white pixel.
        for (int x = 0; x < bw1.getWidth(); x++) {
            for (int y = 0; y < bw1.getHeight(); y++) {
                int color1 = bw1.getRGB(x, y);
                int color2 = bw2.getRGB(x, y);
                if (color1 == color2) {
                    diff.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    diff.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return diff;
    }

    /**
     * Makes a matrix convolution on pixel. Used to find edges.
     * 
     * @param kernel
     *            convolution kernel
     * @param kernWidth
     * @param kernHeight
     * @param src
     *            Source image
     * @param x
     *            X position of pixel
     * @param y
     *            Y position of pixel
     * @param rgb
     *            int[] to save new r, g and b values
     * @return new rgb values
     */
    private static int[] convolvePixel(float[] kernel, int kernWidth,
            int kernHeight, BufferedImage src, int x, int y, int[] rgb) {
        if (rgb == null) {
            rgb = new int[3];
        }

        int halfWidth = kernWidth / 2;
        int halfHeight = kernHeight / 2;

        /*
         * This algorithm pretends as though the kernel is indexed from
         * -halfWidth to halfWidth horizontally and -halfHeight to halfHeight
         * vertically. This makes the center pixel indexed at row 0, column 0.
         */

        for (int component = 0; component < 3; component++) {
            float sum = 0;
            for (int i = 0; i < kernel.length; i++) {
                int row = (i / kernWidth) - halfWidth;
                int column = (i - (kernWidth * row)) - halfHeight;

                // Check range
                if (x - row < 0 || x - row > src.getWidth()) {
                    continue;
                }
                if (y - column < 0 || y - column > src.getHeight()) {
                    continue;
                }
                int srcRGB = src.getRGB(x - row, y - column);
                sum = sum + kernel[i]
                        * ((srcRGB >> (16 - 8 * component)) & 0xff);
            }
            rgb[component] = (int) sum;
        }

        return rgb;
    }
 /**
     * Creates a b&w image of grayscale image.
     * 
     * @param image
     */
    private static void threshold(BufferedImage image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                double lum = lum(color);
                if (lum >= 150) {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
    }

    /**
     * Gets the luminance value for given color
     * 
     * @param color
     * @return Luminance value
     */
    private static double lum(Color color) {
        // return the monochrome luminance of given color
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return .299 * r + .587 * g + .114 * b;
    }

}
