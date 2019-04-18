# MissGL
Make it so simple 3D graphical library

This project aims to share a very simple 3D library source code that can be used as a tutorial or for developing 3D demo applications.
It's a real time implementation that takes into account gravity and collisions.
The collision algorithm is simple and accurate and eliminate most obvious cases rapidly.

I did it just for fun and mainly wanted to have some experience in open source project environment.
It is written in Java and C++ under android studio. 

Below a sample code which shows a spinning cube.
Just describe the scene and the engine will do the rest.

	public class MySpinningCubeScene extends MISScene {
    	float[] cuberotationaxis = {0.5f, 1.0f, 0.0f};
    	float cuberotationspeed = 180.0f;

    	MySpinningCubeScene(AssetManager assetManager) throws IOException {
        	addobject((MISObject) new MISObject("cube", assetManager, "cube.obj", 1.0f, "cube.png", 0.0f, 0.0f, -3.0f));
        	getobject("cube").rotspeed(cuberotationspeed);
        	getobject("cube").rotationaxis(cuberotationaxis);
        	// that's it !
    		}
	}
