package yk.missgl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MISLight extends MISObject {
    public MISLight(int id, float posx, float posy, float posz) {
        super("light");
        float [] lightpos = {posx, posy, posz};
        moveto(lightpos);
    }
    public void move() {
    }
}