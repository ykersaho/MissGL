package com.example.yves.missglndk;

import android.content.res.AssetManager;
import android.view.MotionEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static android.opengl.GLES10.GL_LIGHT0;
import static android.opengl.GLES10.GL_LIGHT1;

public class MyBocce extends MISScene  {
        String state="initstateintro";
        String ballname = "cochonnet";
        int ballp1=0;
        int ballp2=0;
        int score1 = 0;
        int score2 = 0;
        touchhistory thi = new touchhistory();
        long t0,t1;
        float [] gravity= {0.0f, -9.0f, 0.0f};
        float [] zero= {0.0f, 0.0f, 0.0f};
        float [] icampos = {0.0f, 15.0f, 20.0f};
        float [] campos = {0.0f, 15.0f, 20.0f};
        float [] camrot = {-1.0f, 0.0f, 0.0f};


    MyBocce(AssetManager assetManager) throws IOException {
            addobject(new MISObject("ground", assetManager, "ground.obj", 200.0f, "ground.jpg", 0.0f, 0.0f, -5.0f,1000000.0f,0.0f,0.3f));
            addobject(new MISObject("cochonnet", assetManager, "spheresmall.obj", 1.0f / 30.0f, "cochonnet.jpg", 0.0f, 0.4f, -10.0f, 100f, 0.5f, 0.1f));
            addobject(new MISObject("ball1p1", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque1.png", 2.5f, 2.0f, 20.0f, 1000.0f, 0.3f, 0.1f));
            addobject(new MISObject("ball2p1", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque1.png", 2.5f, 2.0f, 20.0f, 1000.0f, 0.3f, 0.1f));
            addobject(new MISObject("ball3p1", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque1.png", 0.0f, 0.4f, -10.0f, 1000f, 0.3f, 0.1f));
            addobject(new MISObject("ball1p2", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque3.jpg", 2.5f, 2.0f, 20.0f, 1000.0f, 0.3f, 0.1f));
            addobject(new MISObject("ball2p2", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque3.jpg", 0.0f, 0.4f, -10.0f, 1000f, 0.3f, 0.1f));
            addobject(new MISObject("ball3p2", assetManager, "spheresmall.obj", 1.0f / 10.0f, "petanque3.jpg", 2f, 1.0f, -10.0f, 1000f, 0.3f, 0.1f));
            addobject(new MISObject("circle", assetManager, "circle.obj", 10.0f, "circle.png", 0.0f, 0.1f, -5.0f,1000000.0f,0.0f,0.3f));
            addobject(new MISObject("buttonrotate", assetManager, "button.obj", 1.0f/5.0f, "rotate.png", -0.6f, 0.6f, -2.5f,0.0f,0.0f,0.0f));
            addobject(new MISObject("buttonlaunch", assetManager, "button.obj", 1.0f/5.0f, "launch.png", -0.6f, 0.3f, -2.5f,0.0f,0.0f,0.0f));
            addobject(new MISObject("buttoncamera", assetManager, "button.obj", 1.0f/5.0f, "cam.png", -0.6f, 0.0f, -2.5f,0.0f,0.0f,0.0f));
            addobject(new MISObject("score1", assetManager, "button.obj", 1.0f/10.0f, "0.jpg", -0.6f, 0.9f, -2.5f,0.0f,0.0f,0.0f));
            addobject(new MISObject("score2", assetManager, "button.obj", 1.0f/10.0f, "0.jpg", 0.6f, 0.9f, -2.5f,0.0f,0.0f,0.0f));
            addlight(new MISLight(GL_LIGHT0,0.0f, 10.0f, -20.0f));
            addlight(new MISLight(GL_LIGHT1,1.0f, 100.0f, -1.0f));
            getobject("cochonnet").posacceleration(gravity);
            getobject("ball1p1").posacceleration(gravity);
            getobject("ball2p1").posacceleration(gravity);
            getobject("ball3p1").posacceleration(gravity);
            getobject("ball1p2").posacceleration(gravity);
            getobject("ball2p2").posacceleration(gravity);
            getobject("ball3p2").posacceleration(gravity);
            int i;
            for(i=1;i<14;i++) {
                getobject("score1").loadtexture(assetManager,i+".jpg");
                getobject("score2").loadtexture(assetManager,i+".jpg");
            }

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
            camera.moveto(campos);
            camera.rotationaxis(camrot);
            camera.rotationangle(30);
            return("stateintro");
        }

        public String stateintro() {
            long t1 = System.nanoTime();
            long ldt = t1 - t0;
            if(ldt > 10L*1000000000L)
                return("initscore");
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
            getobject("cochonnet").moveto(0.0f, 11.0f, -25.0f);
            getobject("ball1p1").moveto(-6f, 11.0f, -25.0f);
            getobject("ball2p1").moveto(-4f, 11.0f, -25.0f);
            getobject("ball3p1").moveto(-2f, 11.0f, -25.0f);
            getobject("ball1p2").moveto(2f, 11.0f, -25.0f);
            getobject("ball2p2").moveto(4f, 11.0f, -25.0f);
            getobject("ball3p2").moveto(6f, 11.0f, -25.0f);
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
            getobject("cochonnet").rotspeed(90);
            getobject("ball1p1").rotspeed(90);
            getobject("ball2p1").rotspeed(90);
            getobject("ball3p1").rotspeed(90);
            getobject("ball1p2").rotspeed(90);
            getobject("ball2p2").rotspeed(90);
            getobject("ball3p2").rotspeed(90);
            ballp1=0;
            ballp2=0;
            ballname="cochonnet";
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
            getobject(ballname).moveto(0.0f, 1.0f, 10.0f);
            getobject(ballname).m=100;
            touchhistory myth = new touchhistory();
            if(gettouchevent(myth)) { // touch event in the queue
                switch (myth.action) {
                    case MotionEvent.ACTION_UP:
                        return("staterotate");
                }
            }
            return("initstaterotate");
        }

        public String staterotate() {
            touchhistory myth = new touchhistory();
            float dx,dy,dz,speed,l,dt;
            float rotationaxis[] = { 1.0f,0.0f,0.0f};
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
                        dz = (float) (-1.0f);
                        dt = myth.t - thi.t;
                        if (dt == 0) break;
                        speed = (float) (1000000000.0f / dt / 500.0f);
                        rotationaxis[0] =  -dy;
                        rotationaxis[1] =  dx;
                        rotationaxis[2] = 0.0f;
                        getobject(ballname).rotationaxis(rotationaxis);
                        getobject(ballname).rotspeed(l);
                        return("stategame");
                }
            }
            return("staterotate");
        }

        public String initstatecamera() {
            // move camera
            t0 = System.nanoTime();
            return("statecamera");
        }

        public String statecamera() {

            touchhistory myth = new touchhistory();
            float dx,dy,dz,speed,l,dt;
            float cp[] = { 0.0f,0.0f,0.0f};
            if(gettouchevent(myth)) { // touch event in the queue
                switch (myth.action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
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
                        campos[0] = campos[0] + dx/50;
                        campos[1] = campos[1];
                        campos[2] = campos[2] + dy/50;
                        camera.moveto(campos);
                        return("statecamera");
                    case MotionEvent.ACTION_MOVE:
                        dx = myth.x1 - thi.x1;
                        dy = myth.y1 - thi.y1;
                        cp[0] = campos[0] + dx/50;
                        cp[1] = campos[1];
                        cp[2] = campos[2] + dy/50;
                        camera.moveto(cp);
                        return("statecamera");
                }
            }
            if(getobject("buttonlaunch").picked == true)
                return("initstatelaunch");
            else
            if(getobject("buttonrotate").picked == true)
                return("initstaterotate");
            return("statecamera");
        }

        public String initstatelaunch() {
            // launch ball
            t0 = System.nanoTime();
            campos[0] = icampos[0];
            campos[1] = icampos[1];
            campos[2] = icampos[2];
            camera.moveto(campos);
            getobject(ballname).moveto(0.0f, 1.0f, 10.0f);
            getobject(ballname).m=100;
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
                        if(l<200) break;
                        dz = (float) (-1.0f);
                        dt = myth.t - thi.t;
                        if (dt == 0) break;
                        speed = (float) (1000000000.0f / dt / 500.0f);
                        posspeed[0] =  dx/100;
                        posspeed[1] = -dy/100;
                        posspeed[2] = speed * dz * l;
                        getobject(ballname).posspeed(posspeed);
                        getobject(ballname).m=100;
                        getobject(ballname).posacceleration(gravity);
                        return("statewaitresult");
                }
            }
            return("statelaunch");
        }

        public float distance(String n) {
            float pos[] = {0.0f,0.1f,0.0f};
            if(getobject(n).m==0)
                return(1000000.0f);
            pos[0] = getobject(n).position[0]-getobject("cochonnet").position[0];
            pos[1] = getobject(n).position[1]-getobject("cochonnet").position[1];
            pos[2] = getobject(n).position[2]-getobject("cochonnet").position[2];
            return(pos[0]*pos[0]+pos[2]*pos[2]);
        }

        public String statewaitresult() {
            float pos[] = {0.0f,0.1f,0.0f};
            long t1 = System.nanoTime();
            long ldt = t1 - t0;
            float speed[]  = getobject(ballname).getpositionspeed(ballname);
            float s = speed[0]*speed[0] + speed[1]*speed[1]+speed[2]*speed[2];
            pos[0] = getobject("cochonnet").position[0];
            pos[2] = getobject("cochonnet").position[2];
            getobject("circle").moveto(pos);
            if((s < 0.1) || (ldt > 20L*1000000000L)) {
                if(ballname=="cochonnet") {ballname="ball1p1";ballp1=1;}
                else {
                    int player = 1, ball=1;
                    float min = 1000000.0f;
                    float d;
                    d = distance("ball1p1");if( d < min) {min = d;player=2;}
                    d = distance("ball2p1");if( d < min) {min = d;player=2;}
                    d = distance("ball3p1");if( d < min) {min = d;player=2;}
                    d = distance("ball1p2");if( d < min) {min = d;player=1;}
                    d = distance("ball2p2");if( d < min) {min = d;player=1;}
                    d = distance("ball3p2");if( d < min) {min = d;player=1;}
                    if(player==1) {
                        ballp1++;
                        ball = ballp1;
                        if (ballp1 > 3) {
                            player = 2;
                            ballp2++;
                            ball = ballp2;
                        }
                    }
                    else {
                        ballp2++;
                        ball = ballp2;
                        if (ballp2 > 3) {
                            player = 1;
                            ballp1++;
                            ball = ballp1;
                        }
                    }
                    ballname = "ball"+ball+"p"+player;
                    if(ballp1 >3 && ballp2 > 3)
                        return ("stateupdatescore");
                }
                return("stategame");
            }
            return("statewaitresult");
        }

        public String initscore() {
            score1=0;
            score2=0;
            getobject("score1").texture=score1;
            getobject("score2").texture=score2;
            return("initstategame");
        }

        public String waittouch() {
            touchhistory myth = new touchhistory();
            if(gettouchevent(myth)) { // touch event in the queue
                switch (myth.action) {
                    case MotionEvent.ACTION_UP:
                        return ("initstategame");
                }
            }
            return("waittouch");
        }

        public String stateupdatescore() {
            touchhistory myth = new touchhistory();
            float p1d1 = distance("ball1p1");
            float p1d2 = distance("ball2p1");
            float p1d3 = distance("ball3p1");
            float p2d1 = distance("ball1p2");
            float p2d2 = distance("ball2p2");
            float p2d3 = distance("ball3p2");
            if((p1d1 < p2d1) && (p1d1 < p2d2) && (p1d1 < p2d3)) score1++;
            if((p1d2 < p2d1) && (p1d2 < p2d2) && (p1d2 < p2d3)) score1++;
            if((p1d3 < p2d1) && (p1d3 < p2d2) && (p1d3 < p2d3)) score1++;
            if((p2d1 < p1d1) && (p2d1 < p1d2) && (p2d1 < p1d3)) score2++;
            if((p2d2 < p1d1) && (p2d2 < p1d2) && (p2d2 < p1d3)) score2++;
            if((p2d3 < p1d1) && (p2d3 < p1d2) && (p2d3 < p1d3)) score2++;
            if(score1 >=13) score1=13;
            if(score2 >=13) score2=13;
            getobject("score1").texture=score1;
            getobject("score2").texture=score2;
            if(score1 ==13) return("endgame");
            if(score2 ==13) return("endgame");
            return("waittouch");
        }

        public String endgame() {
            return("initscore");
        }

        public boolean statemachine() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            state = (String) getClass().getMethod(state).invoke(this);
            return(true);
        }
    }
