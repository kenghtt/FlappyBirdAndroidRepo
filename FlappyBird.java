package com.jeremy.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;   //sprites
    //    ShapeRenderer shapeRenderer;
    Texture gameOver;
    int score = 0;
    int scoringTube = 0;
    BitmapFont font;

    Texture[] birds;
    int flapState = 0;  //shows state of which bird is current
    float birdY = 0;
    double velocity = 0;  //how fast bird moves
    Circle birdCircle;

    int gameState = 0;
    double gravity = 2;    //was originally .01

    Texture topTube;
    Texture bottomTube;
    float gap = 400;
    float maxTubeOffset;

    Random randomGenerator;

    float tubeVelocityMovingLeft = 4;    //was originally 1


    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];

    float distanceBetweenTubes;

    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;


    @Override
    public void create() {

        batch = new SpriteBatch();
//        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        font = new BitmapFont();
        font.setColor(Color.WHITE);   //Set Color Font
        font.getData().setScale(10);    //Set Scale


        background = new Texture("bg.png");
        gameOver = new Texture("gameover.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
//        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth();  //Making games appear further apart

        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

//        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

//        for (int i = 0; i < numberOfTubes; i++) {
//
//            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);   //.nextFloat makes between 0-1
//
//            //Has 4 tubes to make it look like loop and first tube always start on right
//            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
//
//            topTubeRectangles[i] = new Rectangle();
//            bottomTubeRectangles[i] = new Rectangle();
//        }

        startGame();


    }

    public void startGame(){

        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        for (int i = 0; i < numberOfTubes; i++) {

            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);   //.nextFloat makes between 0-1

            //Has 4 tubes to make it look like loop and first tube always start on right
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void render() {
        batch.begin();
        //background starts on bottom left  0,0  then has width of GDX library of our screen and same as height
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        if (gameState == 1) {

            //scoring system
            if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2) {
                score++;
                Gdx.app.log("Score", String.valueOf(score));
                if(scoringTube < numberOfTubes -1){
                    scoringTube++;
                }
                else
                    scoringTube = 0;
            }


            if (Gdx.input.justTouched()) {
                velocity = -30;   //was originally -2


            }

            for (int i = 0; i < numberOfTubes; i++) {
                if (tubeX[i] < -topTube.getWidth()) {

                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);   //.nextFloat makes between 0-1

                } else {
                    tubeX[i] = tubeX[i] - tubeVelocityMovingLeft;  //moves tubes to left by speed - 1

                }


                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);


                //x , y, weidth, and height for the size of the top rectangle pipes
                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle((tubeX[i]), Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

            }

            if (birdY > 0 ) {  //prevents bird from falling below screen
                velocity = velocity + gravity;
                birdY -= velocity;
            }
            else{
                gameState =2;
            }




        } else if (gameState == 0 ){

            if (Gdx.input.justTouched()) {

                gameState = 1;

            }
        }
        else if(gameState == 2){
            batch.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()/2 - gameOver.getWidth()/2);
            if (Gdx.input.justTouched()) {

                gameState = 1;
                startGame();
                score = 0;
                scoringTube = 0;
                velocity = 0;


            }
        }

        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }


        //Make bird in centered, but it is centered at bottom left of the sprite, so need to subtract the width of bird
        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);

        //Location of Score on bottom left
        font.draw(batch, String.valueOf(score), 100, 200);

        batch.end();


        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);  // get center of screen


//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);  //see color of shape
//        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius); //Render a circle  //creating the RED circle i think

        for (int i = 0; i < numberOfTubes; i++) {
            //creating the Red Rectangle pipes
//            shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
//            shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

            //overlap circle and rectangle
            if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
//                Gdx.app.log("Collision", "Shapes have Hit!!!");
                gameState = 2;

            }


        }

//        shapeRenderer.end();

    }


}


//
//    @Override
//    public void dispose() {
//        batch.dispose();
//        background.dispose();
//    }