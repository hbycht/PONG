import processing.core.PApplet;

public class ProcessingApp extends PApplet
{
    public static void main(String[] args)
    {
        PApplet.main("ProcessingApp");
    }

    /// * Space for Inits. * \\\
    // Corners of the Triangle
    // Point A
    int xA = 200;
    int yA = 700;
    // Point B
    int xB = 500;
    int yB = 700;
    // Point C
    int xC = 500;
    int yC = 400;

    // Point Sizes as radius
    int rA = 10;
    int rB = 10;
    int rC = 10;

    // Lengths of the sides
    float lA;
    float lB;
    float lC;

    // Textpositions for the labels of the lengths
    int xTA;
    int yTA;
    int xTB;
    int yTB;
    int xTC;
    int yTC;

    // Status
    boolean pickedA; // Point A is picked?
    boolean pickedB; // Point B is picked?
    boolean pickedC; // Point C is picked?


    public void settings()
    {
        size(900, 900);
    }

    public void setup()
    {

    }

    public void draw()
    {
        /// Calculations \\\
        // Lengths
        lA = xB - xA;
        lB = yB - yC;
        lC = pythagoras(lA, lB);

        // Positions of the labels
        xTA = (xB - xA) / 2 + xA;
        yTA = yA + 30;
        xTB = xB + 30;
        yTB = (yB - yC) / 2 + yC;
        xTC = xTA - 20;
        yTC = yTB - 20;

        // Control if point A is hovered OR picked
        if(hoverA() || pickedA)
        {
            // change the radius of the point, so the user has an optical feedback from his interaction
            rA = 30;

            if(mousePressed) {
                pickedA = true;

                // move xPos of Point A to xPos of the Mouse
                xA = mouseX;
            }
            else {
                pickedA = false;
            }
        }
        else
        {
            rA = 10;
        }

        // Control if point B is hovered OR picked AND point A & B is NOT hovered (last thing is important, otherwise two points would be changed)
        if((hoverB() || pickedB) && !hoverA() && !pickedA && !hoverC() && !pickedC)
        {
            // change the radius of the point, so the user has an optical feedback from his interaction
            rB = 30;

            if(mousePressed) {
                pickedB = true;

                // move Point B to mouse position
                xB = mouseX;
                yB = mouseY;
                // move Point A & Point C relatively to Point B
                xA = xB - (int)lA;
                yA = yB;
                xC = xB;
                yC = yB - (int)lB;
            }
            else {
                pickedB = false;
            }
        }
        else
        {
            rB = 10;
        }

        // Control if point C is hovered OR picked
        if(hoverC() || pickedC)
        {
            // change the radius of the point, so the user has an optical feedback from his interaction
            rC = 30;

            if(mousePressed) {
                pickedC = true;

                // move xPos of Point C to xPos of the Mouse
                yC = mouseY;
            }
            else {
                pickedC = false;
            }
        }
        else
        {
            rC = 10;
        }

        background(50);

        // Draw triangle
        noFill();
        stroke(255, 255);
        triangle(xA, yA, xB, yB, xC, yC);

        // Draw Points on Corners
        fill(255, 0,0);
        noStroke();
        ellipse(xA, yA, rA, rA);
        ellipse(xB, yB, rB, rB);
        ellipse(xC, yC, rC, rC);

        // Write labels of the lengths
        fill(255, 0, 0);
        textSize(20);
        textAlign(CENTER);
        text("a", xTA, yTA);
        text("b", xTB, yTB);
        text("c", xTC, yTC);

        // Write down Lengths of the sides to the top left corner
        textAlign(LEFT);
        text("a = " + lA, 30, 60);
        text("b = " + lB, 30, 80);
        text("c = " + lC, 30, 100);
    }

    private float pythagoras(float length1, float length2)
    {
        return sqrt(sq(length1) + sq(length2));
    }

    private boolean hoverA()
    {
        // Return true or false if the distance between mousepointer and point is <= 15
        return pythagoras(xA - mouseX, yA - mouseY) <= 15;
    }

    private boolean hoverB()
    {
        // Return true or false if the distance between mousepointer and point is <= 15
        return pythagoras(xB - mouseX, yB - mouseY) <= 15;
    }

    private boolean hoverC()
    {
        // Return true or false if the distance between mousepointer and point is <= 15
        return pythagoras(xC - mouseX, yC - mouseY) <= 15;
    }
}
