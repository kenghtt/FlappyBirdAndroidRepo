package com.jeremy.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;   //sprites

    Texture[] birds;
    int flapState = 0;  //shows state of which bird is current
    float birdY = 0;
    double velocity = 0;  //how fast bird moves

    int gameState = 0;
    double gravity = .01;

    Texture topTube;
    Texture bottomTube;
    float gap =400;
    float maxTubeOffset ;

    Random randomGenerator;
    float tubeOffset;



    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        topTube = new Texture("toptube.png");
        bottomTube =new Texture("bottomtube.png");

         maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
        randomGenerator = new Random();

    }

    @Override
    public void render() {
        batch.begin();
        //background starts on bottom left  0,0  then has width of GDX library of our screen and same as height
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        if (gameState != 0) {


            if (Gdx.input.justTouched()) {
                velocity = -3;

                tubeOffset = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);   //.nextFloat makes between 0-1

            }

            batch.draw(topTube, Gdx.graphics.getWidth()/2 - topTube.getWidth()/2, Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset);
            batch.draw(bottomTube, Gdx.graphics.getWidth()/2-bottomTube.getWidth()/2, Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight()+ tubeOffset);


            if(birdY > 0 || velocity < 0){  //prevents bird from falling below screen
                velocity = velocity + gravity;
                birdY-= velocity;
            }


        } else {

            if (Gdx.input.justTouched()) {

                gameState = 1;

            }
        }

        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }
        //make bird in centered, but it is centered at bottom left of the sprite, so need to subtract the width of bird
        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        batch.end();
    }

//
//    @Override
//    public void dispose() {
//        batch.dispose();
//        background.dispose();
//    }
}
