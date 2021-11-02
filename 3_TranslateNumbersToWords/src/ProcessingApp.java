import processing.core.PApplet;

public class ProcessingApp extends PApplet
{
    public static void main(String[] args)
    {
        PApplet.main("ProcessingApp");
    }

    // Space for Inits.


    public void settings()
    {
        size(600, 300);
    }

    public void setup()
    {
        background(0);
        fill(200);
        textSize(20);
        text(100, 200, 200);
        int number = 123000789;
        println(number);
        println(translateNumToWords(number));
    }

    public void draw()
    {

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
                    outputWords += translateBlock(blockList[0]);       // Translation of the actual block of three digits
                    outputWords += addBlockEnding(blockList.length);   // Add the typical ending to the block e.g. "million"

                    //println(blockList[0]);
                    //outputWords += blockList.length;
                }

                // delete the actual block
                blockList = (int[][]) subset(blockList, 1, blockList.length - 1);
            }


            return outputWords;
        }
    }

    private String translateBlock(int[] block)
    {
        String outputWords = "";

        outputWords += translateHundreds(block[0]);
        outputWords += translateTens(block[1]);
        outputWords += translateOnes(block[2]);

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

    private String translateTens(int tens) {
        switch (tens) {
            case 2:
                return " twenty";
            case 3:
                return " thirty";
            case 4:
                return " forty";
            case 5:
                return " fifty";
            case 6:
                return " sixty";
            case 7:
                return " seventy";
            case 8:
                return " eighty";
            case 9:
                return " ninety";
            default:
                return "";
        }
    }

    private String translateOnes(int ones)
    {
        switch (ones) {
            case 1:
                return " one";
            case 2:
                return " two";
            case 3:
                return " three";
            case 4:
                return " four";
            case 5:
                return " five";
            case 6:
                return " six";
            case 7:
                return " seven";
            case 8:
                return " eight";
            case 9:
                return " nine";
            default:
                return "";
        }
    }

    private String addBlockEnding(int index)
    {
        switch(index)
        {
            case 2:
                return " thousand";
            case 3:
                return " million";
            case 4:
                return " billion";
            case 5:
                return " trillion";
            case 6:
                return " quadrillion";
            case 7:
                return " quintillion";
            case 8:
                return " sextillion";
            case 9:
                return " septillion";
            default:
                return "";
        }
    }

    /* PSEUDO-CODE aus Computational Thinking

    sprichZahl(ZiffernListe)
        WENN ZiffernListe ist = "0" DANN
            sprich "zero"
        SONST
            BlockListe = zerteileInDreierBloecke(ZiffernListe)

            SOLANG die Anzahl der Elemente in BlockListe > 0                 //REMINDER: Ein Element sind 3 Ziffern
                WENN erster Block aus BlockListe ist UNGLEICH "000" DANN
                    sprichDreierBlock(erster Block aus BlockListe)
                    sprichDreierAnhang(Anzahl der Elemente in BlockListe)

                lösche den ersten Block aus der BlockListe

    zerteileInDreierBloecke(ZiffernListe)
        füge der ZiffernListe an erste Stelle so viele "0" hinzu, bis Länge der ZiffernListe geteilt durch 3 keinen Rest ergibt     // zB. 4321 wird zu 00 . 4321 = 004321

        erstelle eine Liste mit dem Namen BlockListe

        SOLANG die Länge von ZiffernListe > 0
            füge der BlockListe die letzten drei Ziffern aus der ZiffernListe an erster Stelle hinzu      // zB. wenn wir einer Liste = [321] eine "4" hinzufügen, entsteht [4321] und nicht [3214]
            lösche die letzten drei Ziffern aus der ZiffernListe

        gib die Liste BlockListe zurück

    sprichDreierAnhang(Position)
        WENN Position ist > 1 DANN
                sprich abhängig von der Position:
                "2": "thousand"
                "3": "million"
                "4": "billion"
                "5": "trillion"
                "6": "quadrillion"
                "7": "quintillion"
                "8": "sextillion"
                "9": "septillion"

    sprichHunderter(HunderterZiffer)
        WENN HunderterZiffer ist > 0 DANN
            sprichEiner(HunderterZiffer)
            hänge an "hundred"

    sprichZehner(ZehnerZiffer)
        WENN ZehnerZiffer ist > 1 DANN
            sprich abhängig von der ZehnerZiffer:
                "2": "twenty"
                "3": "thirty"
                "4": "fourty"
                "5": "fifty"
                "6": "sixty"
                "7": "seventy"
                "8": "eighty"
                "9": "ninety"

    sprichEiner(EinerZiffer)
        WENN EinerZiffer ist > 0 DANN
            sprich abhängig von der EinerZiffer:
                "1": "one"
                "2": "two"
                "3": "three"
                "4": "four"
                "5": "five"
                "6": "six"
                "7": "seven"
                "8": "eight"
                "9": "nine"


     */

}
