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
    int spacingBoarder = 20;    // Spacing from Boarder to Window
    int winH = hPlayground + 2 * weightBoarder + spacingBoarder; // Set window height
    int cBackground = 140;      // Color Playground (Grayscale)
    int cBoarder = 60;          // Color Boarder
    int topPG;                  // Top Boarder of Playground
    int bottomPG;               // Bottom Boarder of Playground

    // BALL //
    int rBall = 20;                                 // Radius of the Ball
    int cBall = 200;                                // Color of the Ball
    float vMulti;                                   // Multiplier of Ball Speed (to change over time)
    float vMultiStart = 3.0f;                       // Starting value for vMulti
    float vMultiChange = 1.002f;                    // Factor for changing vMulti over time
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
    int scoreL = 123456789;
    int scoreR = 999998;

    // STYLES //

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
        /*// Additional elements top & bottom;
        rect(0, 0, width, topPG);
        rect(0, bottomPG, width, topPG);*/

        // Drawing middle line
        int numLines = 12;
        int lenLines = hPlayground / (numLines * 2 + 1);
        fill(255);
        for(int i = 0; i < numLines; i++)
        {
            rect(   width/2 - weightBoarder/8,
                    i * 2 * lenLines + lenLines + spacingBoarder + weightBoarder,
                    weightBoarder / 4,
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
        vMulti *= vMultiChange;

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
        vMulti = vMultiStart;
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
        fill(200);
        textSize(16);
        textLeading(20);
        // Left Score
        textAlign(RIGHT);
        text("("+scoreL+")", width/2 - 20, 20);
        text(translateNumToWords(scoreL), width/2 - 20, 46);
        // Right Score
        textAlign(LEFT);
        text("("+scoreR+")", width/2 + 20, 20);
        text(translateNumToWords(scoreR), width/2 + 20, 46);
    }

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

                // delete the actual block
                blockList = (int[][]) subset(blockList, 1, blockList.length - 1);
            }

            return outputWords.toUpperCase();
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

