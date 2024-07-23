import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Player implements Comparable
{
    private int name;
    private ArrayList<Integer> moneyEarned;
    private int money;
    private PowerPlant[] plants;
    private int numOfCities;
    private int poweredCities;
    private LinkedHashMap<String, Integer> resources;
    private String regionColor;
    private String playerColor;
    private LinkedHashMap<String, Integer> maxResources;

    public Player(int na) throws IOException
    {
        name = na;
        money = 50;
        plants = new PowerPlant[3];
        for (int x = 0; x < 3; x++)
        {
            plants[x] = null;
        }
        numOfCities = 0;
        poweredCities = 0;
        resources = new LinkedHashMap<String, Integer>();
        resources.put("coal", 0);
        resources.put("oil", 0);
        resources.put("uranium", 0);
        resources.put("garbage", 0);
        maxResources = new LinkedHashMap<>();
        maxResources.put("coal", 0);
        maxResources.put("oil", 0);
        maxResources.put("uranium", 0);
        maxResources.put("garbage", 0);
        moneyEarned = new ArrayList<Integer>();
        regionColor = "none";
        playerColor = "none";

        Scanner sc = new Scanner(new File("MoneyEarned.txt"));
        while(sc.hasNext())
        {
            moneyEarned.add(Integer.parseInt(sc.next()));
        }
    }
    public void setPlayerColor(String c)
    {
        playerColor = c;
    }
    public String getPlayerColor()
    {
        return playerColor;
    }
    public void setRegionColor(String set)
    {
        regionColor = set;
    }
    public String getRegionColor()
    {
        return regionColor;
    }
    public int calculateMoney(ArrayList<PowerPlant> used)
    {
        int total = 0;
        for(int x = 0; x < used.size(); x++)
        {
            total += used.get(x).getCitiesPowered();
        }
        money = moneyEarned.get(total);

        return money;
    }
    private void updateMaxResources()
    {
        maxResources.put("coal", 0);
        maxResources.put("oil", 0);
        maxResources.put("uranium", 0);
        maxResources.put("garbage", 0);

        for(int i = 0; i<3; i++)
        {
            if(plants[i]!=null)
            {
                if(plants[i].getResource().equalsIgnoreCase("hybrid"))
                {
                    maxResources.put("coal", maxResources.get("coal")+2);
                    maxResources.put("oil", maxResources.get("oil")+2);
                }
                else if(plants[i].getResource().equalsIgnoreCase("none"))
                {
                    return;
                }
                else
                    maxResources.put(plants[i].getResource(), maxResources.get(plants[i].getResource())+plants[i].getAmount()*2);
            }
        }
    }
    public LinkedHashMap<String, Integer> getMaxResources()
    {
        return maxResources;
    }
    public void addPlant(PowerPlant n)
    {
        for (int x = 0; x < 3; x++)
        {
            if (plants[x] == null)
            {
                plants[x] = n;
                break;
            }
        }
        updateMaxResources();
    }
    public PowerPlant replacePlant(PowerPlant n, int replaced)
    {
        PowerPlant removed = plants[replaced];
        plants[replaced] = n;
        updateMaxResources();
        return removed;
    }
    public int compareTo(Object obj)
    {
        int compared = 0;
        Player temp = (Player) obj;
        compared = ((Integer)getNumOfCities()).compareTo((Integer)temp.getNumOfCities());
        if (compared == 0)
        {
            int iden1 = 0;
            int iden2 = 0;
            for (int x = 0; x < getPlants().length; x++)
            {
                if (getPlants()[x].getIdentity() > iden1)
                {
                    iden1 = getPlants()[x].getIdentity();
                }
            }
            for (int x = 0; x < temp.getPlants().length; x++)
            {
                if (temp.getPlants()[x].getIdentity() > iden2)
                {
                    iden2 = temp.getPlants()[x].getIdentity();
                }
            }
            compared = ((Integer)iden1).compareTo((Integer)iden2);
        }
        return compared;
    }
    public boolean powerCities(boolean [] powerPlants, String [] res)
    {
        LinkedHashMap <String, Integer> temp = new LinkedHashMap <String, Integer> ();
        temp.put("coal", resources.get("coal"));
        temp.put("oil", resources.get("oil"));
        temp.put("garbage", resources.get("garbage"));
        temp.put("uranium", resources.get("uranium"));
        ArrayList <PowerPlant> used = new ArrayList <PowerPlant> ();
        for (int x = 0; x < powerPlants.length; x++)
        {
            if (powerPlants[x])
            {
                used.add(plants[x]);
                if (!(res[x].equals("none")))
                {
                    temp.put(res[x], resources.get(res[x]) - plants[x].getAmount());
                }
            }
        }
        if (temp.get("coal") > -1 && temp.get("oil") > -1 && temp.get("garbage") > -1 && temp.get("uranium") > -1)
        {
            setMoney(getMoney() + calculateMoney(used));
            poweredCities = 0;
            for (int x = 0; x < used.size(); x++)
            {
                poweredCities += used.get(x).getCitiesPowered();
            }
            if (poweredCities > numOfCities)
                poweredCities = numOfCities;
            resources = temp;
            return true;
        }
        return false;
    }
    public int getMoney()
    {
        return money;
    }
    public void setMoney(int temp)
    {
        money = temp;
    }
    public PowerPlant[] getPlants()
    {
        return plants;
    }
    public void setPlants(PowerPlant[] temp)
    {
        plants = temp;
    }
    public int getNumOfCities()
    {
        return numOfCities;
    }
    public void setNumOfCities(int temp)
    {
        numOfCities = temp;
    }
    public LinkedHashMap<String, Integer> getResources()
    {
        return resources;
    }
    public void setResources(LinkedHashMap<String, Integer> temp)
    {
        resources = temp;
    }
    public int getName()
    {
        return name;
    }
    public void setName(int newName)
    {
        name = newName;
    }
    public int getPoweredCities()
    {
        return poweredCities;
    }
    public void setPoweredCities(int powCities)
    {
        poweredCities = powCities;
    }

}