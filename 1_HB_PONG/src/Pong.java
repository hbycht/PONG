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
    int weightBoarder = 20;     // Thickness of the Boarders
    int spacingBoarder = 10;    // Spacing from Boarder to Window
    int winH = hPlayground + 2 * weightBoarder + spacingBoarder; // Set window height
    int cBackground = 140;      // Color Playground (Grayscale)
    int cBoarder = 60;          // Color Boarder
    int topPG;                  // Top Boarder of Playground
    int bottomPG;               // Bottom Boarder of Playground

    // BALL //
    int rBall = 20;                                 // Radius of the Ball
    int cBall = 200;                                // Color of the Ball
    float vMulti = 3.0f;                            // Multiplier of Ball Speed (to change over time)
    float vRight;                                   // Speed of the Ball in horizontal Direction
    float vDown;                                    // Speed of the Ball in vertical Direction
    float[] pBall = {wPlayground / 2, winH / 2};    // Position of the Ball

    // PADDEL //
    // Left Paddel
    int xPadL = 10;     // x-Position of the left Paddel
    int yPadL = winH/2; // y-Position of the left Paddel
    int hPadL = 120;    // Height of the left Paddel
    int cPadL = 40;     // Color of the left Paddel

    // Right Paddel
    int xPadR = wPlayground - xPadL;    // x-Position of the right Paddel
    int yPadR = winH/2;                 // y-Position of the right Paddel
    int hPadR = hPadL;                  // Height of the right Paddel
    int cPadR = 40;                     // Color of the right Paddel

    // PLAYERS //
    int scoreL = 0;
    int scoreR = 0;

    public void settings()
    {
        size(wPlayground, winH);
    }

    public void setup()
    {
        // Setting up the Playground for the first time
        topPG = weightBoarder + spacingBoarder;
        bottomPG = height - weightBoarder - spacingBoarder;

        noLoop();
    }

    public void draw()
    {
        setupPlayground();
        movePaddel();
        moveBall();

        showStats();
    }

    public void mousePressed()
    {
        pBall[0] = width/2;
        pBall[1] = height/2;
        setBallDirections();
        loop();
    }

    private void setupPlayground()
    {
        // * DRAWING PLAYGROUND * //
        // Settings
        background(cBackground);
        fill(cBoarder);
        noStroke();
        rectMode(CORNER);

        // Drawing boarders top & bottom
        rect(0, spacingBoarder, width, weightBoarder);
        rect(0, bottomPG, width, weightBoarder);

        // Drawing middle line
        int numLines = 8;
        int lenLines = hPlayground / (numLines * 2 + 1);
        fill(255);
        for(int i = 0; i < numLines; i++)
        {
            rect(   width/2 - weightBoarder/4,
                    i * 2 * lenLines + lenLines + spacingBoarder + weightBoarder,
                    weightBoarder / 2,
                    lenLines
            );
        }

        showScore();
    }

    private void movePaddel()
    {
        /// PADDEL LEFT ///
        // Control if Paddel touches top or bottom boarder
        // when it's true limit the y-Pos to the top or bottom boarder
        if(mouseY - hPadL/2 < topPG)
        {
            yPadL = topPG + hPadL/2;
        }
        else if(mouseY + hPadL/2 > bottomPG)
        {
            yPadL = bottomPG - hPadL/2;
        }
        // Else refresh Paddels yPos to mouseY
        else
        {
            yPadL = mouseY;
        }

        /// PADDEL RIGHT ///
        // Control if Paddel touches top or bottom boarder
        // when it's true limit the y-Pos to the top or bottom boarder
        if(mouseY - hPadR/2 < topPG)
        {
            yPadR = topPG + hPadR/2;
        }
        else if(mouseY + hPadR/2 > bottomPG)
        {
            yPadR = bottomPG - hPadR/2;
        }
        // Else refresh Paddels yPos to mouseY
        else
        {
            yPadR = mouseY;
        }

        drawPaddel();
    }

    private void drawPaddel()
    {
        rectMode(CENTER);

        // draw PADDEL LEFT
        fill(cPadL);
        rect(xPadL, yPadL, weightBoarder, hPadL);
        // draw PADDEL RIGHT
        fill(cPadR);
        rect(xPadR, yPadR, weightBoarder, hPadR);

        rectMode(CORNER);
    }

    private void moveBall()
    {
        // Check if the Ball hits a boarder
        checkCollision(pBall);
        // Refresh Ball Position
        pBall[0] += vRight * vMulti;
        pBall[1] += vDown * vMulti;
        // Speed up the Ball speed a bit
        vMulti *= 1.002;

        drawBall();

    }

    private void drawBall()
    {
        // Draw Ball
        fill(cBall);
        ellipse(pBall[0], pBall[1], rBall*2, rBall*2);
    }

    private void checkCollision(float[] pB)
    {
        // X - Collision with Paddels
        if(pB[0] - rBall < xPadL + weightBoarder/2 && pB[1] > yPadL - hPadL/2 && pB[1] < yPadL + hPadL/2)
        {
            // set Ball direction to absolute positive (to the right)
            vRight = abs(vRight);
        }
        else if(pB[0] + rBall > xPadR - weightBoarder/2 && pB[1] > yPadR - hPadR/2 && pB[1] < yPadR + hPadR/2)
        {
            // set Ball direction to absolute negative (to the left)
            vRight = abs(vRight) * -1;
        }

//        if(pB[0] - rBall < xPadL + weightBoarder/2 || pB[0] + rBall > xPadR - weightBoarder/2)
//        {
//            if(pB[1] > yPadL - hPadL/2 && pB[1] < yPadL + hPadL/2 || pB[1] > yPadR - hPadR/2 && pB[1] < yPadR + hPadR/2)
//            {
//                vRight *= -1;
//            }
//            else
//            {
//                vMulti *= 1.5;
//            }
//        }

        // Y - Collision with top & bottom boarder
        if(pB[1] - rBall < topPG || pB[1] + rBall > bottomPG)
        {
            vDown *= -1;
        }

        // check if someone scores (the ball goes over the right or left edge of the window)
        handleGoal();
    }

    private void setBallDirections()
    {
        vMulti = 3.0f;
        vRight = random(-1, 1) < 0 ? random(-0.5f, -1) : random(0.5f, 1);
        vDown = random(-1, 1);
    }

    private void handleGoal()
    {
        // Ball over left edge
        if(pBall[0] - rBall < 0)
        {
            scoreR++;
            noLoop();

            setupPlayground();
            drawPaddel();
        }

        // Ball over right edge
        if(pBall[0] + rBall > width)
        {
            scoreL++;
            noLoop();

            setupPlayground();
            drawPaddel();
        }
    }

    private void showStats()
    {
        // Show some stats //
        fill(200, 220, 0);
        textSize(12);
        textAlign(LEFT);
        text(
                // PADDEL
                "topPG: " + topPG + " bottomPG: "+ bottomPG +"\n"+
                        "yPadL: "+ yPadL +"\n"+
                        "yPadR: "+ yPadR +"\n\n"+
                        // MOUSE
                        "Mx   My" +"\n"+
                        mouseX + " " + mouseY +"\n\n"+
                        // BALL
                        "Bx   By" +"\n"+
                        (float)round(vRight*100)/100 + " " + (float)round(vDown*100)/100 +"\n"+
                        "vMulti: " + vMulti + "\n" +

                        "", 100, 200
        );
    }

    private void showScore()
    {
        // Show Scores
        textSize(50);
        fill(0);
        // Left Score
        textAlign(RIGHT);
        text(scoreL, width/2 - 20, topPG + 50);
        // Right Score
        textAlign(LEFT);
        text(scoreR, width/2 + 20, topPG + 50);
    }

    private String translateNumbers(int num)
    {

        char tens = 'x';
        char ones = 'x';

        String sNum = ""+num;
        if(sNum.length() == 2){
            tens = sNum.charAt(1);
            ones = sNum.charAt(2);
        }
        else {
            char E = sNum.charAt(1);
        }

        String numInWords = null;

        if(tens != '1'){
            switch (tens){
                case '9':
                    numInWords = "NINTY-";
                    break;
                case '8':
                    numInWords = "EIGHTY-";
                    break;
                case '7':
                    numInWords = "SEVENTY-";
                    break;
                case '6':
                    numInWords = "SIXTY-";
                    break;
                case '5':
                    numInWords = "FIFTY-";
                    break;
                case '4':
                    numInWords = "FORTY-";
                    break;
                case '3':
                    numInWords = "THIRTY-";
                    break;
                case '2':
                    numInWords = "TWENTY-";
                    break;
                default:
                    numInWords = "";
                    break;
            }
        }
        else
        {
            switch (ones){
                case '9':
                    numInWords = "NINETEEN";
                    break;
                case '8':
                    numInWords = "EIGHTEEN";
                    break;
                case '7':
                    numInWords = "SEVENTEEN";
                    break;
                case '6':
                    numInWords = "SIXTEEN";
                    break;
                case '5':
                    numInWords = "FIFTEEN";
                    break;
                case '4':
                    numInWords = "FOURTEEN";
                    break;
                case '3':
                    numInWords = "THIRTEEN";
                    break;
                case '2':
                    numInWords = "TWELVE";
                    break;
                case '1':
                    numInWords = "ELEVEN";
                    break;
                case '0':
                    numInWords = "TEN";
                    break;
            }
        }

        return numInWords;





    }
}
