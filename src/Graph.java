import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Graph
{
    private LinkedHashMap <String, CityNode> map;//6 colors 7 cities in each
    private String[] purples; private String[] greens;
    private String[] blues;	private String[] reds;
    private String[] oranges; private String[] yellows;
    private ArrayList<String[]> regs;
    private ArrayList <String> cityNames;

    public Graph(ArrayList <String> regions) throws IOException//I tried to do it efficiently, but i think i made it more difficult :/ should work tho lol
    {
        Scanner sc = new Scanner(new File("Cities.txt"));
        map = new LinkedHashMap<String, CityNode>();
        regs = new ArrayList <String[]> ();
        purples = new String[7];
        blues = new String[7];
        reds = new String[7];
        yellows = new String[7];
        greens = new String[7];
        oranges = new String[7];
        cityNames = new ArrayList <String> ();
        for(int x = 0; x < 6; x++)
        {
            for(int y = 0; y < 7; y++)
            {
                if(x == 0)
                {
                    purples[y] = sc.nextLine();
                }
                else if(x == 1)
                {
                    blues[y] = sc.nextLine();
                }
                else if(x == 2)
                {
                    reds[y] = sc.nextLine();
                }
                else if(x == 3)
                {
                    yellows[y] = sc.nextLine();
                }
                else if(x == 4)
                {
                    greens[y] = sc.nextLine();
                }
                else
                {
                    oranges[y] = sc.nextLine();
                }
            }
        }
        setRegs(regions);
        for(int x = 0; x < regs.size(); x++)
        {
            for(int y = 0; y < regs.get(x).length; y++)
            {
                CityNode c = split(regs.get(x)[y]);
                cityNames.add(c.getName());
                map.put(c.getName(), c);
            }
        }
    }
    public CityNode split(String array)
    {
        String[] arr = array.split("/");
        ArrayList<String> ray = new ArrayList<String>();
        LinkedHashMap<Integer, String> path = new LinkedHashMap<Integer, String>();
        for(int x = 1; x < arr.length; x++)
        {
            ray.add(arr[x]);
        }
        String name = ray.remove(0);
        while(ray.size() != 0)
        {
            path.put(Integer.parseInt(ray.remove(1)), ray.remove(0));
        }
        int[] occ = new int[3];
        occ[0] = -1;
        occ[1] = -1;
        occ[2] = -1;
        CityNode c = new CityNode(name, occ, path, true);
        return c;
    }
    public void setRegs(ArrayList<String> regions)
    {
        if(regions.contains("Purple"))
            regs.add(purples);
        if(regions.contains("Green"))
            regs.add(greens);
        if(regions.contains("Red"))
            regs.add(reds);
        if(regions.contains("Yellow"))
            regs.add(yellows);
        if(regions.contains("Blue"))
            regs.add(blues);
        if(regions.contains("Orange"))
            regs.add(oranges);
    }
    public Graph()
    {
        map = new LinkedHashMap <String, CityNode> ();
    }

    public CityNode getCity(String name)
    {
        return map.get(name);
    }

    public int shortestPath(String name, int player)
    {
        LinkedHashMap <Integer, String> temp;
        CityNode buy = map.get(name);
        CityNode city;
        temp = buy.getPaths();
        ArrayList <Integer> lengths = new ArrayList <Integer> (temp.keySet());
        Collections.sort(lengths);
        ArrayList <CityNode> cities = new ArrayList <CityNode> ();
        for (int x = 0; x < lengths.size(); x++)
        {
            city = map.get(temp.get(lengths.get(x)));
            if (city.isOccupiedBy(player))
                return lengths.get(x);
            if (!(city.getAvailability()))
                cities.add(city);
            else
                lengths.remove(x);
        }
        ArrayList <Integer> newLengths = new ArrayList <Integer> ();
        for (int x = 0; x < cities.size(); x++)
        {
            temp = cities.get(x).getPaths();
            newLengths = new ArrayList <Integer> (temp.keySet());
            Collections.sort(newLengths);
            newLengths.remove(lengths.get(x));
            for (int y = 0; y < newLengths.size(); y++)
            {
                city = map.get(temp.get(newLengths.get(y)));
                if (city.isOccupiedBy(player))
                    return newLengths.get(y) + lengths.get(x);
            }
        }
        return -1;
    }

    public LinkedHashMap <String, CityNode> getMap()
    {
        return map;
    }

    public void setMap(LinkedHashMap <String, CityNode> ma)
    {
        map = ma;
    }

    public ArrayList <String> getCityNames()
    {
        return cityNames;
    }
}