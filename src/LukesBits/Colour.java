package LukesBits;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author Luke
 */
public class Colour implements Serializable {

    public static Colour red=new Colour(255,0,0);
    public static Colour green=new Colour(0,255,0);
    public static Colour blue=new Colour(0,0,255);
    
    public final int r;
    public final int g;
    public final int b;

    public Color toColor() {
        return new Color(r, g, b);
    }

    public Colour(int _r, int _g, int _b) {
        r = _r < 0 ? 0 : (_r > 255 ? 255 : _r);
        g = _g < 0 ? 0 : (_g > 255 ? 255 : _g);
        b = _b < 0 ? 0 : (_b > 255 ? 255 : _b);
    }

    //dim:0-1
    public Colour dim(double dim) {
        //int test = tidy( (int)Math.round((double)r*dim) );

        return new Colour(tidy((int) Math.round((double) r * dim)), tidy((int) Math.round((double) g * dim)), tidy((int) Math.round((double) b * dim)));
    }

    //bit of a hack to deal with colour light on an object
    public Colour times(Colour colour) {
        double timesr = (double) colour.r / 255.0;
        double timesg = (double) colour.g / 255.0;
        double timesb = (double) colour.b / 255.0;

        return new Colour(tidy((int) Math.round((double) r * timesr)), tidy((int) Math.round((double) g * timesg)), tidy((int) Math.round((double) b * timesb)));
    }

    public Colour add(Colour colour) {
        return new Colour(tidy(r + colour.r), tidy(g + colour.g), tidy(b + colour.b));
    }

    private int tidy(int me) {
        if (me < 0) {
            me = 0;
        }

        if (me > 255) {
            me = 255;
        }
        return me;
    }
    //return
    //0xAARRGGBB
//    public function returnGD(img){
//        /*
//        redHex=dechex(this.r);
//        if(strlen(redHex)<2)
//            redHex="0".redHex;
//
//        greenHex=dechex(this.g);
//        if(strlen(greenHex)<2)
//            greenHex="0".greenHex;
//
//        blueHex=dechex(this.b);
//        if(strlen(blueHex)<2)
//            blueHex="0".blueHex;
//
//        return "0x00".redHex.greenHex.blueHex;*/
//
//
//        return imagecolorallocate(img,this.r,this.g,this.b);
//
//    }
    //methods like get brighter, make duller

    @Override
    public Colour clone() {
        return new Colour(r, g, b);
    }

    @Override
    public String toString() {
        return "(" + r + "," + g + "," + b + ")";
    }

    /**
 *
 *http://mjijackson.com/2008/02/rgb-to-hsl-and-rgb-to-hsv-color-model-conversion-algorithms-in-javascript
 *
 * Converts an HSV color value to RGB. Conversion formula
 * adapted from http://en.wikipedia.org/wiki/HSV_color_space.
 * Assumes h, s, and v are contained in the set [0, 1] and
 * returns r, g, and b in the set [0, 255].
 *
 * @param   Number  h       The hue
 * @param   Number  s       The saturation
 * @param   Number  v       The value
 * @return  Array           The RGB representation
 */
    public static Colour hsvToRgb(double h, double s, double v) {
        double r, g, b;

        int i = (int) Math.floor(h * 6);
        double f = h * 6 - i;
        double p = v * (1 - s);
        double q = v * (1 - f * s);
        double t = v * (1 - (1 - f) * s);

        switch (i % 6) {
            case 0:
                r = v;
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = v;
                b = p;
                break;
            case 2:
                r = p;
                g = v;
                b = t;
                break;
            case 3:
                r = p;
                g = q;
                b = v;
                break;
            case 4:
                r = t;
                g = p;
                b = v;
                break;
            case 5:
            default:
                r = v;
                g = p;
                b = q;
                break;
        }

        //return [r * 255, g * 255, b * 255];
        return new Colour((int) Math.round(r * 255), (int) Math.round(g * 255), (int) Math.round(b * 255));
    }
}
