import java.util.LinkedHashMap;

public class CityNode {
    private String name;
    private boolean isAvailable;
    private int[] occupants;
    private LinkedHashMap <Integer, String> paths;

    public CityNode(String na, int [] occ, LinkedHashMap <Integer, String> pa, boolean avail)
    {
        setName(na);
        setOccupants(occ);
        setPaths(pa);
        setAvailability(avail);
    }

    public void addOccupant(int player, int stage)
    {
        if (occupants[0] == 0)
        {
            occupants[0] = player;
            if (stage == 1)
                isAvailable = false;
        }
        else if (occupants[1] == 0)
        {
            occupants[1] = player;
            if (stage == 2)
                isAvailable = false;
        }
        else if (occupants[2] == 0)
        {
            occupants[2] = player;
            if (stage == 3)
                isAvailable = false;
        }
    }

    public boolean isOccupiedBy(int player)
    {
        if (occupants[0] == player || occupants[1] == player || occupants[2] == player)
            return true;
        return false;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String na)
    {
        name = na;
    }

    public int[] getOccupants()
    {
        return occupants;
    }

    public void setOccupants(int[] occ)
    {
        occupants = occ;
    }

    public boolean getAvailability()
    {
        return isAvailable;
    }

    public void setAvailability(boolean avail)
    {
        isAvailable = avail;
    }

    public LinkedHashMap <Integer, String> getPaths()
    {
        return paths;
    }

    public void setPaths(LinkedHashMap <Integer, String> pa)
    {
        paths = pa;
    }
}