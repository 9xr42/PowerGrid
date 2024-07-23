import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Deck {
    private ArrayList <PowerPlant> plants;

    public Deck() throws IOException
    {
        Scanner sc = new Scanner(new File("powerplants.txt"));
        ArrayList <String> str = new ArrayList <String> ();
        plants = new ArrayList<>();
        while(sc.hasNextLine())
        {
            str.addAll(Arrays.asList(sc.nextLine().split("/")));
            // | = empty space / = doesn't register as empty space
            // | = bad / = good
            plants.add(new PowerPlant(Integer.parseInt(str.get(0)), str.get(1), Integer.parseInt(str.get(2)), Integer.parseInt(str.get(3)), false));
            str.clear();
        }
    }

    public void firstShuffle()
    {
        PowerPlant temp = plants.remove(2);
        shuffle();
        for (int x = 0; x < 4; x++)
        {
            plants.remove(0);
        }
        plants.add(new PowerPlant(51, null, -1, -1, true));
        plants.add(0, temp);
    }

    public void shuffle()
    {
        Collections.shuffle(plants);
    }

    public PowerPlant getNextPlant()
    {
        return plants.remove(0);
    }

    public void addToBotDeck(PowerPlant temp)
    {
        plants.add(temp);
    }

    public ArrayList <PowerPlant> getDeck()
    {
        return plants;
    }

    public void setDeck(ArrayList <PowerPlant> temp)
    {
        plants = temp;
    }
}
