package yk.missgl;

import android.content.res.AssetManager;
import android.view.MotionEvent;

import java.io.IOException;

import static android.opengl.GLES10.GL_LIGHT0;
import static android.opengl.GLES10.GL_LIGHT1;

public class MyGame extends MISScene  {
        MISObject activeobject = null;
        touchhistory thi = new touchhistory();
        float [] gravity= {0.0f, -9.0f, 0.0f};
        float [] campos = {0.0f, 1.0f, 10.0f};
        float [] ballpos = {0.0f, 0.5f, 2.0f};

        MyGame (AssetManager assetManager) throws IOException {
            addobject(new MISObject("ball1", assetManager, "spheresmall.obj", 1.0f / 10.0f, "ball.jpg", 0.0f, 1.5f, 7.0f, 500.0f, 0.3f, 0.3f));
            addobject(new MISObject("ground", assetManager, "ground.obj", 200.0f, "wood.jpg", 0.0f, -1.0f, 0.0f,1000000.0f,0.0f,0.1f));
            addobject(new MISObject("walll", assetManager, "walll.obj", 200.0f, "tennis.jpg", -15f, -1.0f, 0.0f,1000000.0f,0.0f,0.0f));
            addobject(new MISObject("wallr", assetManager, "wallr.obj", 200.0f, "tennis.jpg", 15f, -1.0f, 0.0f,1000000.0f,0.0f,0.0f));
            addobject(new MISObject("button", assetManager, "button.obj", 1.0f/5.0f, "button.png", -0.4f, 0.9f, -2.5f,0.0f,0.0f,0.0f));
            addobject(new MISObject("BowlingPins0", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 0.0f, 1f, -5.0f, 200.0f, 0.5f, 0.5f));
            addobject(new MISObject("BowlingPins1", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", -1.5f, 1f, -8f, 200.0f, 0.5f, 0.5f));
            addobject(new MISObject("BowlingPins2", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 1.5f, 1f, -8f, 200.0f, 0.5f, 0.5f));
            addobject(new MISObject("BowlingPins3", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", -3f, 1f, -11f, 200.0f, 0.5f, 0.5f));
            addobject(new MISObject("BowlingPins4", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 0f, 1f, -11f, 200.0f, 0.5f, 0.5f));
            addobject(new MISObject("BowlingPins5", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 3f, 1f, -11f, 200.0f, 0.5f, 0.5f));
            addobject(new MISObject("BowlingPins6", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", -4.5f, 1f, -14f, 200.0f, 0.5f, 0.5f));
            addobject(new MISObject("BowlingPins7", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", -1.5f, 1f, -14f, 200.0f, 0.5f, 0.5f));
            addobject(new MISObject("BowlingPins8", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 1.5f, 1f, -14f, 200.0f, 0.5f, 0.5f));
            addobject(new MISObject("BowlingPins9", assetManager, "BowlingPins.obj", 1.0f / 10.0f, "bowling_pin_TEX.jpg", 4.5f, 1f, -14f, 200.0f, 0.5f, 0.5f));
            addlight(new MISLight(GL_LIGHT0,0.0f, 1.0f, -2.0f));
            addlight(new MISLight(GL_LIGHT1,1.0f, 10.0f, -1.0f));
            getobject("ball1").posacceleration(gravity);
            getobject("BowlingPins0").posacceleration(gravity);
            getobject("BowlingPins1").posacceleration(gravity);
            getobject("BowlingPins2").posacceleration(gravity);
            getobject("BowlingPins3").posacceleration(gravity);
            getobject("BowlingPins4").posacceleration(gravity);
            getobject("BowlingPins5").posacceleration(gravity);
            getobject("BowlingPins6").posacceleration(gravity);
            getobject("BowlingPins7").posacceleration(gravity);
            getobject("BowlingPins8").posacceleration(gravity);
            getobject("BowlingPins9").posacceleration(gravity);
            camera.moveto(campos);
        }

        public boolean statemachine() {
            touchhistory myth = new touchhistory();
            float [] posspeed= {0.0f, 0.0f, 0.0f};
            float [] zero= {0.0f, 0.0f, 0.0f};
            float [] dir = {0.0f, 0.0f, 0.0f};
            float dx;
            float dy;
            float dz;
            float dt;
            float speed;
            float l;
            MISObject object = getpickedobject();
            MISObject button = getobject("button");

            if(object != null) {
                activeobject=object;
            }
            activeobject=getobject("ball1");
            if(activeobject != null) {
//                camera.positionspeed[0] = (activeobject.position[0] - camera.position[0]) * 2.0f;
//                camera.positionspeed[2] = (activeobject.position[2] - (camera.position[2]-2.0f)) * 2.0f;
            }

            if(gettouchevent(myth)) { // touch event in the queue
                switch (myth.action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_POINTER_UP:
                        pickpointer[0] = myth.x1;
                        pickpointer[1] = myth.y1;
                        thi.action = myth.action;
                        thi.nb = myth.nb;
                        thi.x1 = myth.x1;
                        thi.x2 = myth.x2;
                        thi.y1 = myth.y1;
                        thi.y2 = myth.y2;
                        thi.t = myth.t;
                        break;
                    case MotionEvent.ACTION_UP:
                        dx = myth.x1 - thi.x1;
                        dy = myth.y1 - thi.y1;
                        l = (float) (Math.sqrt(dx * dx + dy * dy));
                        dz = (float) (-2.0f);
                        dt = myth.t - thi.t;
                        if (dt == 0) break;
                        speed = (float) (1000000000.0f / dt / 500.0f);
                        posspeed[0] = speed * dx;
                        posspeed[1] = -speed * dy/10;
                        posspeed[2] = speed * dz * l;
                        if (activeobject != null) {
                            //activeobject.posacceleration(gravity);
                            activeobject.posspeed(posspeed);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        pickpointer[0] = myth.x1;
                        pickpointer[1] = myth.y1;
                        break;
                }
            }

            if(button.picked) {
                if(activeobject != null) {
                    activeobject.moveto(ballpos);
                    activeobject.posspeed(zero);
                    activeobject.rotspeed(0);
                    //activeobject.posacceleration(zero);
                }
                camera.moveto(campos);
                camera.posspeed(zero);
            }
            return(true);
        }
    }
