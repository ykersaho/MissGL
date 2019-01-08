package com.example.yves_k1.mis;

import android.content.res.AssetManager;

import java.io.IOException;

/**
 * Created by yves on 08/11/18.
 */

public class MyObjects {
    class MyBall extends MISObject {
        public MyBall(AssetManager asset) throws IOException {
            super("ball", asset, "spheresmall.obj", 1.0f / 50.0f, "ball.jpg", 0.0f, 0.5f, 2.0f);
            elasticity(0.7f);
            weight(20);
        }
    }
    class MyWall extends MISObject {
        public MyWall(AssetManager asset) throws IOException {
            super("ground", asset, "wall.obj", 10.0f, "wall.jpg", 0.0f, 2.5f, -5.0f);
            weight(1000000);
        }
    }
    class MyGround extends MISObject {
        public MyGround(AssetManager asset) throws IOException {
            super("ground", asset, "ground.obj", 10.0f, "tennis.jpg", 0.0f, 0.0f, 0.0f);
            weight(1000000);
        }
    }
    class MyButton extends MISObject {
        public MyButton(AssetManager asset) throws IOException {
            super("button", asset, "button.obj", 1.0f/5.0f, "button.png", -0.4f, 0.9f, -1.5f);
            weight(0);
        }
    }
}

