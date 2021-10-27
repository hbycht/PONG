import processing.core.PApplet;

public class Sketch extends PApplet {
    public static void main(String args []){
        PApplet.main("Sketch");

    }
    public void settings()
    {
        size(600,600);
    }
    public void setup()
    {
        background(255);
    }
    public void draw()
    {
        fill(0,50);
        noStroke();
        ellipse(mouseX,mouseY,10,10);
    }
}