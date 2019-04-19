package com.rewieer.brostagram.service.Image;

public class SampleUtils {
    public static int getSampleSize(int imageWidth, int imageHeight, int desiredWidth, int desiredHeight) {
        // If the sample sizes are different, we need to take the minimum
        // So that the image will be as big as it is necessary for all dimensions to be respected
        return Math.min(
            getNearestMultipleOfTwo(imageHeight, desiredHeight),
            getNearestMultipleOfTwo(imageWidth, desiredWidth));
    }

    private static int getNearestMultipleOfTwo(int higherValue, int divider) {
        int sample = 1;
        while (higherValue > divider) {
            higherValue /= 2;
            sample *= 2;
        }

        // Our sample is 2x bigger than what's desired at this point
        return sample / 2;
    }
}
