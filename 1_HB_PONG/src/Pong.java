import processing.core.PApplet;
import processing.sound.*;

public class Pong extends PApplet
{
    public static void main(String[] args)
    {
        PApplet.main("Pong");
    }

    // * SETUP * //
    // PLAYGROUND //
    int wPlayground = 1200;                                         // Width Playground
    int hPlayground = 600;                                          // Height Playground
    int weightBoarder = 30;                                         // Thickness of the Boarders
    int spacingBoarder = 10;                                        // Spacing from Boarder to Window
    int winH = hPlayground + 2 * (weightBoarder + spacingBoarder);  // Set window height (I have to set it, because Processing's global 'height' is only accessible after the setup())
    int cBackground = 60;                                           // Color Playground (Grayscale)
    int cBoarder = 0;                                               // Color Boarder
    int topPG;                                                      // Top Boarder of Playground
    int bottomPG;                                                   // Bottom Boarder of Playground

    // BALL //
    int rBall = 20;                                 // Radius of the Ball
    int cBall = 200;                                // Color of the Ball
    float vMulti = 0;                                   // Multiplier of Ball Speed (to change over time)
    float vMultiStart = 5.0f;                       // Starting value for vMulti
    float vMultiChange = 1.003f;                    // Factor for changing vMulti over time
    float vRight;                                   // Speed of the Ball in horizontal Direction
    float vDown;                                    // Speed of the Ball in vertical Direction
    float[] pBall = {wPlayground / 2, winH / 2};    // Position of the Ball

    // PADDEL //
    // Left Paddel
    int xPadL = 10;                 // x-Position of the left Paddel
    int yPadL = winH/2;             // y-Position of the left Paddel
    int hPadL = 120;                // Height of the left Paddel
    int cPadL = color(100);    // Color of the left Paddel

    // Right Paddel
    int xPadR = wPlayground - xPadL;    // x-Position of the right Paddel
    int yPadR = winH/2;                 // y-Position of the right Paddel
    int hPadR = hPadL;                  // Height of the right Paddel
    int cPadR = color(100);        // Color of the right Paddel

    // PLAYERS //
    int scoreL = 0;
    int scoreR = 0;
    int scorer = 0; // 0 = no scorer, -1 = left side, 1 = right side
    int cScore = color(255);

    // SOUND //
    // Initialising some sounds
    SoundFile collisionSound;
    SoundFile goalSound;
    SoundFile restartSound;
    SoundFile lvlupSound;
    SoundFile song_high;
    SoundFile song_low;

    // MODES //
    boolean fullGrafix = false; // for full graphics mode
    int scene = 0;              // to switch through game scenes

    public void settings()
    {
        size(wPlayground, winH);
    }

    public void setup()
    {
        surface.setTitle("PONG by Henning Brode"); // Set the title of the window

        collisionSound = new SoundFile(this, "collision.wav");
        goalSound = new SoundFile(this, "goal.wav");
        restartSound = new SoundFile(this, "restart.wav");
        lvlupSound = new SoundFile(this, "lvlup.wav");
        song_high = new SoundFile(this, "song_high.wav");
        song_low = new SoundFile(this, "song_low.wav");


        // Setting up the Playground for the first time
        topPG = weightBoarder + spacingBoarder;
        bottomPG = height - weightBoarder - spacingBoarder;

        colorMode(HSB);
        noStroke();

        song_low.loop(1, 0.5f);
    }

    public void draw()
    {
        setupPlayground();
        movePaddel();
        moveBall();

        //showStats();  // to see some parameter values to check
    }

    public void mousePressed()
    {
        if(scene == 0){
            resetBall();
            vMulti = vMultiStart; // Start ball movement
            scorer = 0;
            scene = 1;
        }
    }

    public void keyPressed() {
        if (key == 'g') {
            // for full graphics mode
            fullGrafix = !fullGrafix;   // Switch graphics mode

            if(fullGrafix)
            {
                song_low.stop();
                lvlupSound.play(1.2f);
                song_high.loop(1, 0.6f);
            }
            else
            {
                song_high.stop();
                collisionSound.play(0.3f);
                song_low.loop(1, 0.5f);
            }
        }
        else if (key == 'r') {
            // to reset the game
            restartSound.play();
            scoreR = 0;
            scoreL = 0;
            scorer = 0;
            scene = 0;
            setupPlayground();
            resetBall();
            drawBall();
            drawPaddel();
        }
    }

    private void setupPlayground()
    {
        // * DRAWING PLAYGROUND * //

        // Set some parameters according to the graphics mode
        if(fullGrafix) // Extra cool GRAFIXXX!!1!
        {
            // Settings
            background(0.7f * 255, 0.6f * 255, 0.2f * 255);
            fill(130, 0.5f * 255, 0.4f * 255); // Color for boarders

            cPadL = color(130, 0.7f * 255, 0.8f * 255);
            cPadR = color(130, 0.7f * 255, 0.8f * 255);
            cBall = color(30, 0.7f * 255, 0.9f * 255);
            cScore = color(30, 0.8f * 255, 1.0f * 255);


            // Drawing CrossCircle
            int radius = 20;
            drawCrossCircle(radius);
        }
        else // Normal Graphics
        {
            // Settings
            background(cBackground);
            fill(cBoarder); // Color for boarders

            cPadL = color(140);
            cPadR = color(140);
            cBall = color(200);
            cScore = color(180);
        }

        // Drawing boarders top & bottom
        rect(0, 0, width, weightBoarder + spacingBoarder);
        rect(0, bottomPG, width, weightBoarder + spacingBoarder);

        showScore();

        // Information for user interactions
        fill(cScore);
        textAlign(CENTER);
        textSize(weightBoarder * 0.6f);
        text("CONTROLS: 'R' to reset score | 'G' for full graphics", width/2, bottomPG + weightBoarder * 0.8f);

        // handling some game scenes
        if(scene == 0){ // start position of the ball
            // Information to start ball movement
            textLeading(30);
            text("CLICK TO START\nMove mouse to control paddles.", width/2, height/2 + 50);

            if(scorer != 0){ // if there is a goal
                // Show score sign
                fill(0, 200, 150);
                rectMode(CENTER);
                rect(width/2, height/2 - 55, width/2.5f, 40);
                fill(cScore);

                // write down the scorer
                if(scorer == 1) // right player scores
                {
                    text("-_ -    Right Player scores!    ~(^-^)~", width/2, height/2 - 50);
                }
                else // left player scores
                {
                    text("~(^-^)~    Left Player scores!    - _-", width/2, height/2 - 50);
                }
            }

        }
    }

    private void drawCrossCircle(int radius)
    {
        /* What we want to draw:

            X
          X X X
        X X X X X   ||  it depends on the radius
          X X X
            X

         */

        int midX = width / 2;   // midpoint X of playground
        int midY = height / 2;  // midpoint Y of playground
        float spacingX = 12;    // spacing in X between crosses
        float spacingY = 11;    // spacing in Y between crosses

        // let the spacing oscillate
        spacingX *= sin(frameCount / 30f) * 0.1 + 1;
        spacingY *= -sin(frameCount / 30f) * 0.05 + 1;

        // Style
        int hue = 0;        // Starting hue value
        int hueChange = 7;  // Hue changes over iteration in the loop below
        strokeWeight(1);

        // to draw the crosses in every column
        for(int i = 0; i < radius; i++)
        {
            // to draw the crosses in every row
            for(int j = radius - i; j > 0; j--)
            {
                // right side
                if(scorer == -1) // if left player scores
                {
                    stroke(240, 255, 255); // set color of the right "arrow" side to red (to have a feedback on a goal)
                }
                else // else:
                {
                    stroke(120 + j * hueChange/8f, 255, hue + 2f * j * hueChange); // set to the pattern color
                }
                // bottom right part
                drawCross(midX + i * spacingX, midY + j * spacingY - spacingY);
                // top right part
                drawCross(midX + i * spacingX, midY - j * spacingY + spacingY);

                // left side
                if(scorer == 1) // if right player scores
                {
                    stroke(240, 255, 255); // set color of the left "arrow" side to red (to have a feedback on a goal)
                }
                else // else:
                {
                    stroke(120 + j * hueChange/8f, 255, hue + 2f * j * hueChange); // set to the pattern color
                }
                // bottom left part
                drawCross(midX - i * spacingX, midY + j * spacingY - spacingY);
                // top left part
                drawCross(midX - i * spacingX, midY - j * spacingY + spacingY);

                spacingY *= 0.998f; // spacing changes over iteration to get this curved shape
            }
            hue += hueChange; // change hue after every iteration
            spacingX *= 1.02f; // spacing changes over iteration to get this curved shape
        }
        noStroke(); // reset to noStroke for every other shape in my sketch
    }

    private void drawCross(float xPos, float yPos)
    {
        // helper function to handle the drawing of the crosses
        int diameter = 6;
        // horizontal line
        line(xPos - diameter/2, yPos, xPos + diameter/2, yPos);
        // vertical line
        line(xPos, yPos - diameter/2, xPos, yPos + diameter/2);
    }

    private void movePaddel()
    {
        /*
        I have implemented the control of both paddles separately
        even though I have them both controlled synchronously via the mouse.
        This allows me to easily add a two-player mode in the future
        that controls the paddles with the keyboard, for example.
        */

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
        // Speed up the Ball speed a bit, but maximum to 10
        vMulti = min(vMulti * vMultiChange, 10);

        drawBall();

    }

    private void drawBall()
    {
        // Draw Ball
        fill(cBall);
        circle(pBall[0], pBall[1], rBall*2);
    }

    private void checkCollision(float[] pB)
    {
        // I implemented the checkCollision function with a ball parameter because maybe I want to have more balls in the future

        // X - Collision with Paddels
        if(pB[0] - rBall < xPadL + weightBoarder/2 && pB[1] > yPadL - hPadL/2 && pB[1] < yPadL + hPadL/2)
        {
            // set Ball direction to absolute positive (towards the right)
            vRight = abs(vRight);
            collisionSound.play(1.4f); // play sound with a higher pitch than the boarder collision sound
        }
        else if(pB[0] + rBall > xPadR - weightBoarder/2 && pB[1] > yPadR - hPadR/2 && pB[1] < yPadR + hPadR/2)
        {
            // set Ball direction to absolute negative (towards the left)
            vRight = abs(vRight) * -1;
            collisionSound.play(1.4f); // play sound with a higher pitch than the boarder collision sound
        }

        // Y - Collision with top & bottom boarder
        if(pB[1] - rBall < topPG || pB[1] + rBall > bottomPG)
        {
            vDown *= -1;
            collisionSound.play(1);
        }

        // check if someone scores ( = the ball goes over the right or left edge of the window)
        handleGoal();
    }

    private void resetBall()
    {
        // Reset ball position
        pBall[0] = width / 2;
        pBall[1] = height / 2;

        // Reset ball directions
        vMulti = 0;
        vRight = random(-1, 1) < 0 ? random(-0.5f, -1) : random(0.5f, 1); // it limits the random values to negative -1.0 to -0.5 to positive 0.5 to 1.0
        vDown = random(-1, 1);
    }

    private void handleGoal()
    {
        // is Ball over left edge?
        if(pBall[0] - rBall < 0)
        {
            goalSound.play(1, 0.7f);
            scoreR++;
            scorer = 1;
            scene = 0;

            resetBall();
        }

        // is Ball over right edge?
        if(pBall[0] + rBall > width)
        {
            goalSound.play(1, 0.7f);
            scoreL++;
            scorer = -1;
            scene = 0;

            resetBall();
        }
    }

    private void showStats()
    {
        // Show some params for debugging //
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
        fill(cScore);
        textSize(weightBoarder * 0.6f);
        textLeading(weightBoarder * 0.8f);
        // Left Score
        textAlign(RIGHT);
        text("("+scoreL+")", width/2 - 20, topPG - 12);     // score in numbers
        text(translateNumToWords(scoreL), width/2 - 20, 60);    // score in words
        // Right Score
        textAlign(LEFT);
        text("("+scoreR+")", width/2 + 20, topPG - 12);     // score in numbers
        text(translateNumToWords(scoreR), width/2 + 20, 60);    // score in words
    }


    /// In the following all functions to translate numbers into words ///
    private String translateNumToWords(int inputNum)
    {

        // If the number is 0 the word is ZERO
        if(inputNum == 0)
        {
            return "ZERO";
        }
        //  Else we have to do some operations
        else
        {
            // Init some local variables
            String outputWords = "";
            int[] digitList = new int[0];
            int[][] blockList = new int[0][];

            // Convert the whole int into an array of int digits
            do{
                digitList = append(digitList, inputNum % 10); // add the last digit of inputNum to the digitList array
                inputNum /= 10;                                     // delete the last digit
            } while(inputNum > 0);                                  // do it while there are digits in the inputNum

            // Add as many zeros to the digitList until it's neatly divisible into blocks of three
            while(digitList.length % 3 > 0)
            {
                digitList = append(digitList, 0);
            }

            // Reverse the digitList because the last operations add the values in the opposite order
            digitList = reverse(digitList);

            // Divide the digitList in blocks of three and save these blocks into the blockList
            for(int i = 0; i < digitList.length / 3; i++)
            {
                blockList = (int[][]) append(blockList, subset(digitList, i * 3, 3));
            }

            // execute for every block in blockList
            while(blockList.length > 0){
                // but just if the actual block is NOT 000
                if(blockList[0][0] + blockList[0][1] + blockList[0][2] > 0)
                {
                    outputWords += translateBlock(blockList[0]);                    // Translation of the actual block of three digits
                    outputWords += " " + addBlockEnding(blockList.length) + "\n";   // Add the corresponding ending to the block e.g. "million"
                }

                // delete the actual block from blockList
                blockList = (int[][]) subset(blockList, 1, blockList.length - 1);
            }

            return outputWords.toUpperCase(); // return in CAP letters
        }
    }

    private String translateBlock(int[] block)
    {
        String outputWords = "";

        // translate the hundreds digit
        outputWords += translateHundreds(block[0]);
        outputWords += block[1]+block[2] > 0 && block[0] > 0 ? " " : ""; // just add a space to the string, if tens or ones are NOT 0 AND hundreds are also NOT 0

        // translate the tens digit
        outputWords += translateTens(block[1]);
        outputWords += block[2] > 0 && block[1] > 1 ? " " : ""; // just add a space to the string, if ones are NOT 0 AND tens are also NOT 0 or 1

        // translate the ones digit
        outputWords += translateOnes(block[1], block[2]); // we have to transmit the tens AND the ones for also handling the numbers 10-19

        return outputWords;
    }

    private String translateHundreds(int hundreds)
    {
        if(hundreds > 0)
        {
            return translateOnes(hundreds) + " hundred";
        }
        return "";
    }

    private String translateTens(int tens)
    {
        switch (tens) {
            case 2:
                return "twenty";
            case 3:
                return "thirty";
            case 4:
                return "forty";
            case 5:
                return "fifty";
            case 6:
                return "sixty";
            case 7:
                return "seventy";
            case 8:
                return "eighty";
            case 9:
                return "ninety";
            default:
                return "";
        }

    }

    private String translateOnes(int ones)
    {
        switch (ones) {
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            case 8:
                return "eight";
            case 9:
                return "nine";
            default:
                return "";
        }
    }

    private String translateOnes(int tens, int ones)
    {
        if(tens != 1) {
            return translateOnes(ones);
        }
        else
        {
            switch (ones) {
                case 0:
                    return "ten";
                case 1:
                    return "eleven";
                case 2:
                    return "twelve";
                case 3:
                    return "thirteen";
                case 4:
                    return "fourteen";
                case 5:
                    return "fifteen";
                case 6:
                    return "sixteen";
                case 7:
                    return "seventeen";
                case 8:
                    return "eighteen";
                case 9:
                    return "nineteen";
                default:
                    return "";
            }
        }
    }

    private String addBlockEnding(int index)
    {
        switch(index)
        {
            case 2:
                return "thousand";
            case 3:
                return "million";
            case 4:
                return "billion";
            case 5:
                return "trillion";
            case 6:
                return "quadrillion";
            case 7:
                return "quintillion";
            case 8:
                return "sextillion";
            case 9:
                return "septillion";
            default:
                return "";
        }
    }
}

