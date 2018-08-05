# MissGL
Make it so simple 3D graphical library

This project aims to share a very simple 3D library source code that can be used as a tutorial or for developing 3D demo applications.
It's a real time implementation that takes into account gravity and collisions.
The recursive collision algorithm is very simple and accurate.

I did it just for fun and mainly wanted to have some experience in open source project environment.
It is written in Java for Android but I will probably port it in C++. 

Below a sample code which shows the collision between one cube and a sphere with gravity interaction.
Just describe the scene and the engine will do the rest.

		public class MyDemoScene  extends MISScene {
				float[] gravity = {0.0f, -9.0f, 0.0f};  // gravity 9 m/s2
				float[] zero = {0.0f, 0.0f, 0.0f};
				float[] cameraposition = {0.0f, 1.0f, 6.0f};   // camera postion in meter
				float[] camerarotation = {0.0f, 0.0f, 0.0f};   // camera rotation in degrees
				float[] camerapositionspeed = {0.0f, 1.0f, .0f};  // camera position speed int meters/s
				float[] camerarotationspeed = {-5.0f, 0.0f, 0.0f};  // camera rotation speed degrees/s
				float[] sphereposition = {1.0f, 3.0f, -4.0f};
				float[] spherespeed = {-1.0f, 0.0f, 0.0f};
				float[] sphererotationspeed = {20.0f, 60.0f, 0.0f};
				int sphereweight = 500;  // weight in gram
				float sphereelasticity = 0.7f;   // medium elasticity
				float[] cubeposition = {-1.0f, 1.0f, -4.0f};
				float[] cubespeed = {1.0f, 0.0f, 0.0f};
				float[] cuberotationspeed = {70.0f, 30.0f, 10.0f};
				float cubeelasticity = 0.8f;   // very elastic
				int cubeweight = 800;  // weight in gram
				float[] groundposition = {0.0f, 0.0f, -5.0f};
				MISObject sphere = new MISObject("sphere");
				MISObject cube = new MISObject("cube");
				MISObject ground = new MISObject("ground");

				MyDemoScene(GL10 gl, AssetManager assetManager) throws IOException {
					ground.loadobj(assetManager, "ground.obj", 20.0f);
					ground.loadtexture(gl, assetManager, "ground.jpg");
					ground.weight(1000000); // earth is very heavy ! never move.
					ground.moveto(groundposition);
					sphere.loadobj(assetManager, "sphere.obj", 1.0f / 40.0f);
					sphere.loadtexture(gl, assetManager, "sphere.gif");
					sphere.moveto(sphereposition);
					sphere.posspeed(spherespeed);
					sphere.rotspeed(sphererotationspeed);
					sphere.weight(sphereweight);
					sphere.posacceleration(gravity); // apply gravity else the object will float
					sphere.elasticity(sphereelasticity);
					cube.loadobj(assetManager, "cube.obj", 1.0f/2.0f);
					cube.loadtexture(gl, assetManager, "cube.png");
					cube.moveto(cubeposition);
					cube.posspeed(cubespeed);
					cube.posacceleration(gravity);
					cube.elasticity(cubeelasticity);
					cube.rotspeed(cuberotationspeed);
					cube.weight(cubeweight);
					camera.moveto(cameraposition);
					camera.rotateto(camerarotation);
					camera.rotspeed(camerarotationspeed);
					camera.posspeed(camerapositionspeed);
					addobject((MISObject)sphere);
					addobject((MISObject)cube);
					addobject((MISObject)ground);
					// that's it !
			}

			// main scene state machine
			public void statemachine() {
					// not used for this demo
			}

			// touch screen event management
			public boolean onTouchEvent(MotionEvent e) {
					// not used for this demo
					return (false);
			}
	}
	
