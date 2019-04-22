package com.example.yves.missglndk;

import android.content.res.AssetManager;
import android.view.MotionEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static android.opengl.GLES10.GL_LIGHT0;
import static android.opengl.GLES10.GL_LIGHT1;

public class MyBocce extends MISScene  {
        String state="initstateintro";
        String ball = "cochonnet";
        touchhistory thi = new touchhistory();
        long t0,t1;
        float [] gravity= {0.0f, -9.0f, 0.0f};
        float [] zero= {0.0f, 0.0f, 0.0f};

        MyBocce(AssetManager assetManager) throws IOException {
            addobject(new MISObject("ground", assetManager, "ground.obj", 100.0f, "ground.jpg", 0.0f, 0.0f, -5.0f,1000000.0f,0.0f,0.3f));
            addobject(new MISObject("cochonnet", assetManager, "spheresmall.obj", 1.0f / 30.0f, "cochonnet.jpg", 0.0f, 0.4f, -10.0f, 100f, 0.5f, 0.1f));
            addobject(new MISObject("ball1p1", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque1.png", 2.5f, 2.0f, 20.0f, 1000.0f, 0.3f, 0.3f));
            addobject(new MISObject("ball2p1", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque1.png", 2.5f, 2.0f, 20.0f, 1000.0f, 0.3f, 0.3f));
            addobject(new MISObject("ball3p1", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque1.png", 0.0f, 0.4f, -10.0f, 100f, 0.3f, 0.3f));
            addobject(new MISObject("ball1p2", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque3.jpg", 2.5f, 2.0f, 20.0f, 1000.0f, 0.3f, 0.3f));
            addobject(new MISObject("ball2p2", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque3.jpg", 0.0f, 0.4f, -10.0f, 100f, 0.3f, 0.3f));
            addobject(new MISObject("ball3p2", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque3.jpg", 2f, 1.0f, -10.0f, 1000f, 0.3f, 0.3f));
            addobject(new MISObject("buttonrotate", assetManager, "button.obj", 1.0f/5.0f, "button.png", -0.4f, 0.8f, -2.5f,0.0f,0.0f,0.0f));
            addobject(new MISObject("buttonlaunch", assetManager, "button.obj", 1.0f/5.0f, "lancer.jpg", 0.0f, 0.8f, -2.5f,0.0f,0.0f,0.0f));
            addobject(new MISObject("buttoncamera", assetManager, "button.obj", 1.0f/5.0f, "button.png", 0.4f, 0.8f, -2.5f,0.0f,0.0f,0.0f));
            addlight(new MISLight(GL_LIGHT0,0.0f, 10.0f, -20.0f));
            addlight(new MISLight(GL_LIGHT1,1.0f, 100.0f, -1.0f));
            getobject("cochonnet").posacceleration(gravity);
            getobject("ball1p1").posacceleration(gravity);
            getobject("ball2p1").posacceleration(gravity);
            getobject("ball3p1").posacceleration(gravity);
            getobject("ball1p2").posacceleration(gravity);
            getobject("ball2p2").posacceleration(gravity);
            getobject("ball3p2").posacceleration(gravity);
        }

        public String initstateintro() {
            // intro
            t0 = System.nanoTime();
            getobject("cochonnet").moveto(1.5f, 11.0f, 1.0f);
            getobject("ball1p1").moveto(2.0f, 9.0f, 2.0f);
            getobject("ball2p1").moveto(1.0f, 9.0f, 2.0f);
            getobject("ball3p1").moveto(2.0f, 9.0f, 1.0f);
            getobject("ball1p2").moveto(1.0f, 10.0f, 1.0f);
            getobject("ball2p2").moveto(2.0f, 10.0f, 2.0f);
            getobject("ball3p2").moveto(1.0f, 10.0f, 2.0f);
            camera.moveto(0.0f,4.0f,20.0f);
            return("stateintro");
        }

        public String stateintro() {
            long t1 = System.nanoTime();
            long ldt = t1 - t0;
            if(ldt > 10L*1000000000L)
                return("initstategame");
            return("stateintro");
        }

        public String initstategame() {
            getobject("cochonnet").m=0;
            getobject("ball1p1").m=0;
            getobject("ball2p1").m=0;
            getobject("ball3p1").m=0;
            getobject("ball1p2").m=0;
            getobject("ball2p2").m=0;
            getobject("ball3p2").m=0;
            getobject("cochonnet").moveto(0.0f, 8.0f, -20.0f);
            getobject("ball1p1").moveto(-6f, 8.0f, -20.0f);
            getobject("ball2p1").moveto(-4f, 8.0f, -20.0f);
            getobject("ball3p1").moveto(-2f, 8.0f, -20.0f);
            getobject("ball1p2").moveto(2f, 8.0f, -20.0f);
            getobject("ball2p2").moveto(4f, 8.0f, -20.0f);
            getobject("ball3p2").moveto(6f, 8.0f, -20.0f);
            getobject("cochonnet").posacceleration(zero);
            getobject("ball1p1").posacceleration(zero);
            getobject("ball2p1").posacceleration(zero);
            getobject("ball3p1").posacceleration(zero);
            getobject("ball1p2").posacceleration(zero);
            getobject("ball2p2").posacceleration(zero);
            getobject("ball3p2").posacceleration(zero);
            getobject("cochonnet").posspeed(zero);
            getobject("ball1p1").posspeed(zero);
            getobject("ball2p1").posspeed(zero);
            getobject("ball3p1").posspeed(zero);
            getobject("ball1p2").posspeed(zero);
            getobject("ball2p2").posspeed(zero);
            getobject("ball3p2").posspeed(zero);
            getobject("cochonnet").rotationaxis(90.0f,0.0f,0.0f);
            getobject("ball1p1").rotationaxis(90.0f,90.0f,0.0f);
            getobject("ball2p1").rotationaxis(90.0f,0.0f,90.0f);
            getobject("ball3p1").rotationaxis(90.0f,90.0f,90.0f);
            getobject("ball1p2").rotationaxis(0.0f,90.0f,0.0f);
            getobject("ball2p2").rotationaxis(0.0f,90.0f,90.0f);
            getobject("ball3p2").rotationaxis(0.0f,0.0f,90.0f);
            return("stategame");
        }

        public String stategame() {
            touchhistory myth = new touchhistory();
            if(gettouchevent(myth)) { // touch event in the queue
                switch (myth.action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_POINTER_UP:
                        pickpointer[0] = myth.x1;
                        pickpointer[1] = myth.y1;
                        break;
                }
            }

            if(getobject("buttoncamera").picked == true)
                return("initstatecamera");
            else
                if(getobject("buttonlaunch").picked == true)
                return("initstatelaunch");
            else
                if(getobject("buttonrotate").picked == true)
                return("initstaterotate");
            return("stategame");
        }

        public String initstaterotate() {
            // rotate ball
            t0 = System.nanoTime();
            return("staterotate");
        }

        public String staterotate() {
            return("stategame");
        }

        public String initstatecamera() {
            // move camera
            t0 = System.nanoTime();
            return("statecamera");
        }

        public String statecamera() {
            return("stategame");
        }

        public String initstatelaunch() {
            // launch ball
            t0 = System.nanoTime();
            getobject(ball).moveto(0.0f, 3.0f, 10.0f);
            getobject(ball).m=100;
            touchhistory myth = new touchhistory();
            if(gettouchevent(myth)) { // touch event in the queue
                switch (myth.action) {
                    case MotionEvent.ACTION_UP:
                        return("statelaunch");
                }
            }
            return("initstatelaunch");
        }

        public String statelaunch() {
            touchhistory myth = new touchhistory();
            float dx,dy,dz,speed,l,dt;
            float posspeed[] = { 0.0f,0.0f,0.0f};
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
                        posspeed[1] = -speed * dy;
                        posspeed[2] = speed * dz * l;
                        getobject(ball).posspeed(posspeed);
                        getobject(ball).m=100;
                        getobject(ball).posacceleration(gravity);
                        return("statewaitresult");
                }
            }
            return("statelaunch");
        }

        public String statewaitresult() {
            long t1 = System.nanoTime();
            long ldt = t1 - t0;
            if(ldt > 10L*1000000000L){
                if(ball=="cochonnet") ball="ball1p1";
                else if(ball=="ball1p1") ball="ball1p2";
                else if(ball=="ball1p2") ball="ball2p1";
                else if(ball=="ball2p1") ball="ball2p2";
                else if(ball=="ball2p2") ball="ball3p1";
                else if(ball=="ball3p1") ball="ball3p2";
                else {
                    ball = "cochonnet";
                    return ("initstategame");
                }
                return("initstatelaunch");
            }
            return("statewaitresult");
        }


        public boolean statemachine() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            state = (String) getClass().getMethod(state).invoke(this);
            return(true);
        }
    }
