package com.devstream.phever.utilities;

import android.graphics.Color;

/**
 * A class with methods to help with colors.
 * 
 */

public class ColorTool {

/**
 * Return true if actual color and expected color are a close match.
 * To be a good match, all three color values (RGB) must be within the tolerance value given.
 * 
 * @param color1 int
 * @param color2 int
 * @param tolerance int - the max difference that is allowed for any of the RGB components
 * @return boolean
 */

	//compare the colour at the x y cordintes of the mask with the expected within a tolerance  (x y got from actual press on actual image)
public boolean closeMatch (int color1, int color2, int tolerance) {
    if ((int) Math.abs (Color.red (color1) - Color.red (color2)) > tolerance ) return false;
    if ((int) Math.abs (Color.green (color1) - Color.green (color2)) > tolerance ) return false;
    if ((int) Math.abs (Color.blue (color1) - Color.blue (color2)) > tolerance ) return false;
    return true;
} // end closematch

} // end class
