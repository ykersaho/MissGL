package com.example.yves.missglndk;

import android.content.res.AssetManager;
import android.view.MotionEvent;

import java.io.IOException;

import static android.opengl.GLES10.GL_LIGHT0;
import static android.opengl.GLES10.GL_LIGHT1;

/**
 * Created by Yves_K1 on 19/06/2018.
 */

public class MyPongScene extends MISScene {
    MISScene.touchhistory thi = new MISScene.touchhistory();
    int state = 0;
    float [] currentpos= {0.0f, -9.0f, 0.0f};
    float [] currentrot= {0.0f, -9.0f, 0.0f};
    float [] gravity= {0.0f, -15.0f, 0.0f};
    float [] zero= {0.0f, 0.0f, 0.0f};
    float [] campos = {0.0f, 1.5f, 4.0f};
    float [] camrot = {0.0f, 0.0f, 0.0f};
    float [] camrotspeed = {-10.0f, 0.0f, 0.0f};
    float [] camposspeed = {0.0f, 0.0f, -1.0f};
    float [] pos1 = {0.0f, 1.0f, 1.0f};
    float [] rot1 = {10.0f, 20.0f, 0.0f};
    float [] spos1 = {0.0f, -0.0f, 0.0f};
    float [] srot1 = {100.0f, 20.0f, 00.0f};
    float [] postable = {0.0f, 0.0f, -2.0f};
    MISObject sphere1 = new MISObject("sphere1");
    MISObject button1 = new MISObject("button1");
    MISObject button2 = new MISObject("button2");
    MISObject rack1 = new MISObject("rack1");
    MISObject rack2 = new MISObject("rack2");
    MISObject table = new MISObject("table");
    MISObject net = new MISObject("net");
    float [] tabledb = {0.0f, -1000000.0f, 0.0f};
    float [] posbutton1= {-0.4f, 0.9f, -2.0f};
    float [] rotbutton1= {0.0f, 0.0f, 0.0f};
    float [] posbutton2= {0.4f, 0.9f, -2.0f};
    float [] rotbutton2= {0.0f, 0.0f, 0.0f};
    float [] posnet= {0.0f, 0.0f, -2.0f};
    float [] rotnet= {0.0f, 0.0f, 0.0f};
    float [] srotnet = {0.0f, 0.0f, 0.0f};
    MISLight light1 = new MISLight(GL_LIGHT0,2.0f, 10.0f, -1.0f);
    MISLight light2 = new MISLight(GL_LIGHT1,-2.0f, 10.0f, -1.0f);
    boolean translation;
    MISObject pickedsphere=sphere1;

    private class touchhistory {
        int action;
        int nb;
        float x1=0.0f;
        float x2=0.0f;
        float y1=0.0f;
        float y2=0.0f;
        float t=0.0f;
    }

    MyPongScene(AssetManager assetManager) throws IOException {
        button1.loadobj(assetManager, "face.obj", 1.0f/5.0f);
        button1.moveto(posbutton1);
        button1.loadtexture(assetManager, "camera.png" );
        //button1.rotspeed(rotbutton1);
        button1.weight(0);
        button2.loadobj(assetManager, "face.obj", 1.0f/5.0f);
        button2.loadtexture(assetManager, "ball.png" );
        button2.moveto(posbutton2);
        //button2.rotspeed(rotbutton2);
        button2.weight(0);
        rack1.loadobj(assetManager, "face.obj", 1.0f);
        rack1.loadtexture(assetManager, "rack.png" );
        rack1.weight(50);
        sphere1.loadobj(assetManager, "spheresmall.obj", 1.0f/90.0f);
        sphere1.loadtexture(assetManager, "cochonnet.jpg" );
        sphere1.moveto(pos1);
        //sphere1.rotateto(rot1);
        sphere1.posspeed(spos1);
        //sphere1.rotspeed(srot1);
        sphere1.weight(10);
        sphere1.elasticity(0.9f);
        table.setbarycenter(tabledb);
        table.loadobj(assetManager, "table.obj", 1.0f);
        table.loadtexture(assetManager, "table.jpg" );
        table.weight(1000000);
        table.moveto(postable);
        net.loadobj(assetManager, "net.obj", 1.0f);
        net.loadtexture(assetManager, "wood.jpg" );
        net.moveto(posnet);
        //net.rotateto(rotnet);
        //net.rotspeed(srotnet);
        net.weight(1000000);
        sphere1.posacceleration(gravity);
        addobject((MISObject)sphere1);
        addobject((MISObject)table);
        addobject((MISObject)net);
        addobject((MISObject)button1);
        addobject((MISObject)button2);
        addobject((MISObject)rack1);
        addlight(light1);
        addlight(light2);
        camera.moveto(campos);
        //camera.rotateto(camrot);
        //camera.rotspeed(camrotspeed);
        camera.posspeed(camposspeed);
    }

    public boolean statemachine() {
        MISScene.touchhistory myth = new MISScene.touchhistory();
        float [] rot= {0.0f, 90.0f, 0.0f};
        float [] dir= {0.0f, 90.0f, 0.0f};
        float [] pos= {0.0f, 0.0f, 0.0f};
        float [] pos0= {0.0f, 1.0f, -6.0f};
        float [] rot0= {-20.0f, 0.0f, 0.0f};
        float [] rotspeed= {0.0f, 0.0f, 0.0f};
        float [] posspeed= {0.0f, 0.0f, 0.0f};
        float [] zero= {0.0f, 0.0f, 0.0f};
        float dx;
        float dy;
        float dxi;
        float dyi;
        float dz;
        float dt;
        float speed;
        float l;

        //rack1 pos
        if(sphere1.positionspeed[2] < 0.0) {
            dir[0]=10.0f*(sphere1.position[0] - rack1.position[0]);
            dir[1]=10.0f*(sphere1.position[1] - rack1.position[1]);
            dir[2]=2.0f*(sphere1.position[2] - rack1.position[2]);
            if((dir[2] < 0.5f) && (dir[2] > 0.0f))
                dir[2] = 10;
        }
        else {
            dir[0] = (pos0[0] - rack1.position[0])*2;
            dir[1] = (pos0[1] - rack1.position[1])*2;
            dir[2] = (pos0[2] - rack1.position[2])*2;
        }
        rack1.posspeed(dir);
        //rack1.rotateto(rot0);
        if(rack1.position[1] < 0.0) rack1.position[1] = 0.0f;

        if(gettouchevent(myth)) { // touch event in the queue
            switch (myth.action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_POINTER_UP:
                    pickpointer[0] = myth.x1;
                    pickpointer[1] = myth.y1;
                    thi.action=myth.action;
                    thi.nb=myth.nb;
                    thi.x1=myth.x1;
                    thi.x2=myth.x2;
                    thi.y1=myth.y1;
                    thi.y2=myth.y2;
                    thi.t=myth.t;
                    if(state==0) {
                        currentpos[0]=camera.position[0];
                        currentpos[1]=camera.position[1];
                        currentpos[2]=camera.position[2];
          //              currentrot[0]=camera.rotation[0];
            //            currentrot[1]=camera.rotation[1];
              //          currentrot[2]=camera.rotation[2];
                    }
                    else {
                        currentpos[0]=pickedsphere.position[0];
                        currentpos[1]=pickedsphere.position[1];
                        currentpos[2]=pickedsphere.position[2];
                //        currentrot[0]=pickedsphere.rotation[0];
                  //      currentrot[1]=pickedsphere.rotation[1];
                    //    currentrot[2]=pickedsphere.rotation[2];
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    dx = myth.x1 - thi.x1;
                    dy = myth.y1 - thi.y1;
                    l = (float)(Math.sqrt(dx*dx+dy*dy));
                    dz = (float)(-2.0f);
                    dt = myth.t - thi.t;
                    speed = (float)(1000000000.0f/dt/500.0f);
                    if(state==1){
                        posspeed[0]=speed*dx;
                        posspeed[1]=-speed*dy;
                        posspeed[2]=speed*dz*l;
                        pickedsphere.posacceleration(gravity);
                        pickedsphere.posspeed(posspeed);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(myth.nb>1) {
                        dx = myth.x2 - myth.x1;
                        dy = myth.y2 - myth.y1;
                        dxi = thi.x2 - thi.x1;
                        dyi = thi.y2 - thi.y1;
                        if(state==0) {
                            pos[0]=currentpos[0];
                            pos[1]=currentpos[1];
                            pos[2]=currentpos[2] - (dx-dxi) / 100;
                            camera.moveto(pos);
                        }
                    }
                    else
                    if(state==0) {
                        dx = myth.x1 - thi.x1;
                        dy = myth.y1 - thi.y1;
                        rot[0]=currentrot[0]+dy/10;
                        rot[1]=currentrot[1]+dx/10;
                        rot[2]=currentrot[2];
                      //  camera.rotateto(rot);
                    }
                    break;
            }
        }

        switch(state) {
            case 0 : // camera move
                if(button2.picked) {
          //          button2.rotspeed(rot);
            //        button1.rotspeed(zero);
                    //button1.rotateto(zero);
                    button2.picked = false;
                    pickedsphere.moveto(pos1);
              //      pickedsphere.rotateto(rot1);
          //          pickedsphere.rotspeed(zero);
                    pickedsphere.posspeed(zero);
                    state=1;
                }
                if(button1.picked) {
                //    button1.rotspeed(rot);
                    camera.moveto(campos);
                    //camera.rotateto(camrot);
                  //  camera.rotspeed(zero);
                    camera.posspeed(zero);
                    button1.picked = false;
                }
                break;
            case 1 : // object move
                if(button1.picked) {
                    //button1.rotspeed(rot);
                    //button2.rotspeed(zero);
                    //button2.rotateto(zero);
                    state=0;
                    camera.moveto(campos);
                    //camera.rotateto(camrot);
                    //camera.rotspeed(zero);
                    camera.posspeed(zero);
                    button1.picked = false;
                }
                if(button2.picked) {
                    //button2.rotspeed(rot);
                    pickedsphere.moveto(pos1);
                    //pickedsphere.rotateto(rot1);
                    //pickedsphere.rotspeed(zero);
                    pickedsphere.posspeed(zero);
                    button2.picked = false;
                }
                break;
        }
        return(true);
    }
}
