package me.peterzoltan.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtil {

    /**
     * Rotates the BufferedImage clockwise by the given degrees, produces a new image, the original is unchanged.
     * @param image the BufferedImage to be rotated.
     * @param degrees the degrees the BufferedImage is rotated by.
     * @return the rotated version of the BufferedImage.
     */
    public static BufferedImage rotate(BufferedImage image, double degrees)
    {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage newImage = new BufferedImage(width, height, image.getType());

        Graphics2D graphics = newImage.createGraphics();

        graphics.rotate(Math.toRadians(degrees), (double) width / 2, (double) height / 2);
        graphics.drawImage(image, null, 0, 0);

        return newImage;
    }
}
