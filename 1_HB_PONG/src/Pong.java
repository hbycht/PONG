import processing.core.PApplet;

public class Pong extends PApplet
{
    public static void main(String[] args)
    {
        PApplet.main("Pong");
    }

    // * SETUP * //
    // PLAYGROUND //
    int wPlayground = 1000;     // Width Playground
    int hPlayground = 500;      // Height Playground
    int cBackground = 140;      // Color Playground (Grayscale)
    int cBoarder = 60;          // Color Playground
    int weightBoarder = 20;     // Thickness of the Boarders
    int spacingBoarder = 10;    // Spacing from Boarder to Window
    int topPG;
    int bottomPG;

    // BALL //
    int rBall = 20;                             // Radius of the Ball
    int cBall = 200;                            // Color of the Ball
    float vMulti = 3.0f;                        // Multiplier of Ball Speed (to change over time)
    float vRight;                               // Speed of the Ball in horizontal Direction
    float vDown;                                // Speed of the Ball in vertical Direction
    float[] pBall = {width / 2, height / 2};    // Position of the Ball

    public void settings()
    {
        size(wPlayground, hPlayground + 2 * weightBoarder + spacingBoarder);
    }

    public void setup()
    {
        // Setting up the Playground for the first time
        topPG = weightBoarder + spacingBoarder;
        bottomPG = height - weightBoarder - spacingBoarder;
        setupPlayground();

        // Setting up the Ball for the first time
        setBallDirections();
    }

    public void draw()
    {
        setupPlayground();
        moveBall();
        text(mouseX + " " + mouseY + " " + hPlayground, 100, 400);
    }

    public void mousePressed()
    {
        pBall[0] = width/2;
        pBall[1] = height/2;
        setBallDirections();
    }

    private void setupPlayground()
    {
        // * DRAWING PLAYGROUND * //
        background(cBackground);
        fill(cBoarder);
        noStroke();
        // Drawing boarders top & bottom
        rect(0, spacingBoarder, width, weightBoarder);
        rect(0, bottomPG, width, weightBoarder);
        // Drawing middle line
        int numLines = 10;
        int lenLines = hPlayground / (numLines * 2 +1);
        fill(255);
        //rectMode(CENTER);
        for(int i = 0; i < numLines; i++)
        {
            stroke(100, 0, 0);
            line(0, i * 2 * lenLines + lenLines + spacingBoarder + weightBoarder, width, i * 2 * lenLines + lenLines + spacingBoarder + weightBoarder);
            rect(   width/2 - weightBoarder/2,
                    i * 2 * lenLines + lenLines + spacingBoarder + weightBoarder,
                    weightBoarder / 2,
                    lenLines
            );
            noStroke();
        }
        //rectMode(CORNER);
    }

    private void moveBall()
    {
        // Check if the Ball hits a boarder
        checkCollision(pBall);
        // Refresh Ball Position
        pBall[0] += vRight * vMulti;
        pBall[1] += vDown * vMulti;
        // Draw Ball
        fill(cBall);
        ellipse(pBall[0], pBall[1], rBall*2, rBall*2);
        // Speed up the Ball speed a bit
        vMulti *= 1.001;

    }

    private void setBallDirections()
    {
        vMulti = 3.0f;
        vRight = random(-1, 1) > 0 ? random(0.5f, 1) : random(-1, -0.5f);
        vDown = random(-1, 1);
        println(vRight + " " + vDown);
    }

    private void checkCollision(float[] pB)
    {
        if(pB[0] < 0 + rBall || pB[0] > width - rBall)
        {
            vRight *= -1;
        }
        if(pB[1] < topPG + rBall || pB[1] > bottomPG - rBall)
        {
            vDown *= -1;
        }
    }
}
