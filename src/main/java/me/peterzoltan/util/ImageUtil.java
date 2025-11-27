package me.peterzoltan.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtil {
    public static BufferedImage rotate(BufferedImage img, double degrees)
    {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        Graphics2D g2 = newImage.createGraphics();

        g2.rotate(Math.toRadians(degrees), (double) width / 2, (double) height / 2);
        g2.drawImage(img, null, 0, 0);

        return newImage;
    }
}
