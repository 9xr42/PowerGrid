import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

public class Board {
    private ArrayList <Player> players;
    private LinkedHashMap <String, Integer> overallResources;
    private ArrayList<PowerPlant> plantMarket;
    private LinkedHashMap <Integer, Boolean> bidRound;
    private LinkedHashMap <Integer, Boolean> powerPlantPhase;
    private int stage;
    private Graph map;
    private Deck cards;
    private int currentBid;
    private PowerPlant selling;
    private int buyingPlayer;
    private boolean isFirstRound;

    public Board() throws IOException
    {
        players = new ArrayList<>();

        //setting initial resources
        overallResources = new LinkedHashMap<>();
        overallResources.put("coal", 24);
        overallResources.put("oil", 18);
        overallResources.put("garbage", 6);
        overallResources.put("uranium", 2);

        buyingPlayer = -1;
        selling = null;
        currentBid = -1;
        isFirstRound = true;
        powerPlantPhase = new LinkedHashMap <Integer, Boolean> ();
        bidRound = new LinkedHashMap <Integer, Boolean> ();
        stage = 1;
        ArrayList <String> regions = new ArrayList <String> ();
        regions.add("Purple");
        regions.add("Blue");
        regions.add("Red");
        regions.add("Yellow");
        regions.add("Green");
        regions.add("Orange");
        Collections.shuffle(regions);
        regions.remove(0);
        regions.remove(1);
        map = new Graph(regions);
        cards = new Deck();
        plantMarket = new ArrayList<>();

        for (int x = 0; x < 4; x++)
        {
            players.add(new Player(x));
        }
        for (int x = 0; x < 8; x++)
        {
            plantMarket.add(cards.getNextPlant());
        }
        Collections.sort(plantMarket);
        cards.firstShuffle();

        //randomize player color
        ArrayList<String> colors = new ArrayList<String>(Arrays.asList("green", "yellow", "red", "blue", "purple", "black"));
        int num = 0;
        for(Player p: players)
        {
            num = (int)Math.floor(Math.random()*colors.size());
            p.setPlayerColor(colors.get(num));
            colors.remove(num);
        }
        Collections.shuffle(players);
        /*selling = plantMarket.get(0); //remove later
        PowerPlant[] plants = players.get(0).getPlants();
        plants[0] = cards.getNextPlant();
        plants[1] = cards.getNextPlant();
        plants[2] = cards.getNextPlant();
        players.get(0).setPlants(plants);
        buyingPlayer = 0;*/

        for (int x = 0; x < 4; x++)
        {
            bidRound.put(x, true);
            powerPlantPhase.put(x, true);
        }

    }

    public boolean buyResources(LinkedHashMap <String, Integer> temp, int player)
    {
        //resources: coal, oil, garbage, uranium
        //prereq: already checked if resources were available


        for(String r: temp.keySet()) //checking if resource bought is over the limit
        {
            //check if bought resource + current resource is over max
            if((temp.get(r)+players.get(player).getResources().get(r))>players.get(player).getMaxResources().get(r))
                return false;
        }

        int totalPrice = 0; //price of the resources

        ArrayList<Integer> costAndNum = new ArrayList<>();
        //first number = cost
        //second number = number of resources at that cost

        for(String resource: temp.keySet())
        {
            while(temp.get(resource)>0) //number of resources still greater than 0
            {
                costAndNum = getCost(resource); //get cost and num
                temp.put(resource, temp.get(resource).intValue()-costAndNum.get(1));//update overall resources available, get price of one section
                totalPrice += costAndNum.get(0);
            }
        }

        if(totalPrice>players.get(player).getMoney())
            return false;
        else
        {
            LinkedHashMap<String, Integer> playerResources = players.get(player).getResources();
            for(String resource: temp.keySet())
            {
                overallResources.put(resource, overallResources.get(resource).intValue()-temp.get(resource)); //subtracting from overall resources what the player bought
                playerResources.put(resource, playerResources.get(resource).intValue()+temp.get(resource)); //add new bought resources to player resources
            }
            players.get(player).setResources(playerResources); //update player resources
            players.get(player).setMoney(players.get(player).getMoney()-totalPrice); //update player's money
            return true;
        }
    }

    private ArrayList<Integer> getCost(String resource)
    {
        int num = overallResources.get(resource).intValue();//number of resources available
        int cost = 0;
        int number = 0;

        if(resource.equalsIgnoreCase("uranium"))
        {
            number = 1; //for each price = can buy one
            if(num==12)
                cost = 1;
            else if(num==11)
                cost = 2;
            else if(num==10)
                cost = 3;
            else if(num==9)
                cost = 4;
            else if(num==8)
                cost = 5;
            else if(num==7)
                cost = 6;
            else if(num==6)
                cost = 7;
            else if(num==5)
                cost = 8;
            else if(num==4)
                cost = 10;
            else if(num==3)
                cost = 12;
            else if(num==2)
                cost = 14;
            else if(num==1)
                cost = 16;
        }
        else //if resource is coal, oil, or garbage (same numbers and prices)
        {
            if(num>21)
            {
                cost = 1;
                number = num-21;
            }
            else if(num>18)
            {
                cost = 2;
                number = num-18;
            }
            else if(num>15)
            {
                cost = 3;
                number = num-15;
            }
            else if(num>12)
            {
                cost = 4;
                number = num-12;
            }
            else if(num>9)
            {
                cost = 5;
                number = num-9;
            }
            else if(num>6)
            {
                cost = 6;
                number = num-6;
            }
            else if(num>3)
            {
                cost = 7;
                number = num-3;
            }
            else if(num>0)
            {
                cost = 8;
                number = num;
            }
        }

        ArrayList<Integer> costAndNum = new ArrayList<>();
        costAndNum.add(cost);
        costAndNum.add(number);
        return costAndNum;
    }
    public int getIndResourceCost(String rsrc, int num)
    {
        int cost = 0;
        if(rsrc.equalsIgnoreCase("uranium"))
        {
            if(num==12)
                cost = 1;
            else if(num==11)
                cost = 2;
            else if(num==10)
                cost = 3;
            else if(num==9)
                cost = 4;
            else if(num==8)
                cost = 5;
            else if(num==7)
                cost = 6;
            else if(num==6)
                cost = 7;
            else if(num==5)
                cost = 8;
            else if(num==4)
                cost = 10;
            else if(num==3)
                cost = 12;
            else if(num==2)
                cost = 14;
            else if(num==1)
                cost = 16;
        }
        else //if resource is coal, oil, or garbage (same numbers and prices)
        {
            if(num>21)
            {
                cost = 1;
            }
            else if(num>18)
            {
                cost = 2;
            }
            else if(num>15)
            {
                cost = 3;
            }
            else if(num>12)
            {
                cost = 4;
            }
            else if(num>9)
            {
                cost = 5;
            }
            else if(num>6)
            {
                cost = 6;
            }
            else if(num>3)
            {
                cost = 7;
            }
            else if(num>0)
            {
                cost = 8;
            }
        }

        return cost;

    }

    public boolean buyCity(String city, int player)
    {
        //not finished
        //check if cities are occupied now
        //check if player can afford cities

        CityNode current = null;
        int cost = 0;

        //checking if all cities are available

        current = map.getCity(city); //city node
        cost = map.shortestPath(city, player);
        if(current.getAvailability()==false)
            return false;


        if(cost>players.get(player).getMoney()) //if player cannot afford
            return false;

        //else buy cities for player
        current = map.getCity(city); //city node
        current.addOccupant(player, stage);
        players.get(player).setNumOfCities(players.get(player).getNumOfCities()+1);

        players.get(player).setMoney(players.get(player).getMoney()-cost); //subtract cost from player money

        return true;
    }

    public int initialBid(boolean choice, PowerPlant sell, int bid, int player)
    {
        powerPlantPhase.put(player, choice);
        if (choice)
        {
            bidRound.put(player, choice);
            ArrayList <Integer> temp = remainingBooleans(powerPlantPhase, true);
            for (int x = 0; x < temp.size(); x++)
            {
                bidRound.put(temp.get(x), true);
            }
            selling = sell;
            currentBid = bid;
            buyingPlayer = player;
            if (temp.size() == 1)
            {
                return 1;
            }
            return 2;
        }
        return 3;
    }

    public boolean higherBid(boolean choice, int bid, int player)
    {
        bidRound.put(player, choice);
        if (choice)
        {
            currentBid = bid;
            buyingPlayer = player;
            return true;
        }
        else
        {
            if (remainingBooleans(bidRound, true).size() == 1)
            {
                powerPlantPhase.put(buyingPlayer, false);
                return false;
            }
            return true;
        }
    }

    public ArrayList <Integer> remainingBooleans(LinkedHashMap <Integer, Boolean> round, boolean choice)
    {
        ArrayList <Integer> remaining = new ArrayList <Integer> ();
        for (int x = 0; x < 4; x++)
        {
            if (round.get(x) == choice)
            {
                remaining.add(x);
            }
        }
        return remaining;
    }

    public void buyingPowerPlant(int replaced)
    {
        for (int x = 0; x < players.size(); x++)
        {
            if (players.get(x).getName() == buyingPlayer)
            {

                players.get(x).replacePlant(selling, replaced);
                players.get(x).setMoney(players.get(x).getMoney()-currentBid);
                break;
            }
        }
    }
    public void buyingPowerPlant()
    {
        for (int x = 0; x < players.size(); x++)
        {
            if (players.get(x).getName() == buyingPlayer)
            {

                players.get(x).addPlant(selling);
                players.get(x).setMoney(players.get(x).getMoney()-currentBid);
                break;
            }
        }
    }

    public void updateResources()
    {
        if(stage==1)
        {
            int coalAdd = (overallResources.get("coal")) + 5;
            if(coalAdd > 24)
            {
                coalAdd = 24;
            }
            int oilAdd = (overallResources.get("oil")) + 3;
            if(oilAdd > 24)
            {
                oilAdd = 24;
            }
            int garbageAdd = (overallResources.get("garbage")) + 2;
            if(garbageAdd > 24)
            {
                garbageAdd = 24;
            }
            int uAdd = (overallResources.get("uranium")) + 1;
            if(uAdd > 24)
            {
                uAdd = 24;
            }
            overallResources.put("coal", coalAdd);
            overallResources.put("oil", oilAdd);
            overallResources.put("garbage", garbageAdd);
            overallResources.put("uranium", uAdd);
        }
        else if(stage==2)
        {
            int coalAdd = (overallResources.get("coal")) +6;
            if(coalAdd > 24)
            {
                coalAdd = 24;
            }
            int oilAdd = (overallResources.get("oil")) + 4;
            if(oilAdd > 24)
            {
                oilAdd = 24;
            }
            int garbageAdd = (overallResources.get("garbage")) + 3;
            if(garbageAdd > 24)
            {
                garbageAdd = 24;
            }
            int uAdd = (overallResources.get("uranium")) + 2;
            if(uAdd > 24)
            {
                uAdd = 24;
            }
            overallResources.put("coal", coalAdd);
            overallResources.put("oil", oilAdd);
            overallResources.put("garbage", garbageAdd);
            overallResources.put("uranium", uAdd);
        }
        else
        {
            int coalAdd = (overallResources.get("coal")) +4;
            if(coalAdd > 24)
            {
                coalAdd = 24;
            }
            int oilAdd = (overallResources.get("oil")) + 5;
            if(oilAdd > 24)
            {
                oilAdd = 24;
            }
            int garbageAdd = (overallResources.get("garbage")) + 4;
            if(garbageAdd > 24)
            {
                garbageAdd = 24;
            }
            int uAdd = (overallResources.get("uranium")) + 2;
            if(uAdd > 24)
            {
                uAdd = 24;
            }
            overallResources.put("coal", coalAdd);
            overallResources.put("oil", oilAdd);
            overallResources.put("garbage", garbageAdd);
            overallResources.put("uranium", uAdd);
        }
    }

    public void checkStage(boolean stage3Next)
    {
        //after everytime calling checkStage, set stage3next to false
        boolean stage2=false;
        for(Player p: players)
        {
            if(p.getNumOfCities()>=7)
            {
                stage2=true;
                break;
            }
        }
        if(stage2)
        {
            stage = 2;
            if(removeReplaceSmallest())
                stage3Next = true;
        }

        if(stage3Next)
        {
            plantMarket.remove(plantMarket.size()-1); //get rid of step 3 card
            plantMarket.remove(0);
            stage = 3;
            cards.shuffle();
            //adding discard pile
        }
    }

    public boolean removeReplaceSmallest()
    {
        //remove and replace smallest powerplant in plant market
        //returns if replacement is round three (boolean)

        Collections.sort(plantMarket);
        plantMarket.remove(0); //adding removed powerplant to discard pile
        PowerPlant p = cards.getNextPlant();
        plantMarket.add(p);
        if(p.isRoundThree())
            return true;
        return false;
    }

    public ArrayList <Player> getPlayers()
    {
        return players;
    }

    public void setPlayers(ArrayList <Player> temp)
    {
        players = temp;
    }

    public LinkedHashMap <String, Integer> getOverallResources()
    {
        return overallResources;
    }

    public void setOverallResources(LinkedHashMap <String, Integer> temp)
    {
        overallResources = temp;
    }

    public ArrayList<PowerPlant> getPlantMarket()
    {
        return plantMarket;
    }

    public void setPlantMarket(ArrayList<PowerPlant> temp)
    {
        plantMarket = temp;
    }

    public PowerPlant removeFromPlantMarket(PowerPlant temp)
    {
        plantMarket.remove(temp);
        return temp;
    }

    public PowerPlant addToPlantMarket()
    {
        PowerPlant temp = cards.getNextPlant();
        plantMarket.add(temp);
        Collections.sort(plantMarket);
        return temp;
    }

    public LinkedHashMap <Integer, Boolean> getBidRound()
    {
        return bidRound;
    }

    public void setBidRound(LinkedHashMap <Integer, Boolean> temp)
    {
        bidRound = temp;
    }

    public int getStage()
    {
        return stage;
    }

    public void setStage(int temp)
    {
        stage = temp;
    }

    public Graph getMap()
    {
        return map;
    }

    public void setMap(Graph temp)
    {
        map = temp;
    }

    public Deck getCards()
    {
        return cards;
    }

    public void setCards(Deck temp)
    {
        cards = temp;
    }

    public int getCurrentBid()
    {
        return currentBid;
    }

    public void setCurrentBid(int newBid)
    {
        currentBid = newBid;
    }

    public PowerPlant getSelling()
    {
        return selling;
    }

    public void setSelling(PowerPlant sell)
    {
        selling = sell;
    }

    public int getBuyingPlayer()
    {
        return buyingPlayer;
    }

    public void setBuyingPlayer(int newPlayer)
    {
        buyingPlayer = newPlayer;
    }

    public boolean getFirstRound()
    {
        return isFirstRound;
    }

    public void setFirstRound(boolean firstRound)
    {
        isFirstRound = firstRound;
    }

    public LinkedHashMap <Integer, Boolean> getPowerPlantPhase()
    {
        return powerPlantPhase;
    }

    public void setPowerPlantPhase(LinkedHashMap <Integer, Boolean> plantPhase)
    {
        powerPlantPhase = plantPhase;
    }
}
