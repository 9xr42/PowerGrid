import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class GameState
{
    private Board game;
    private static int counter = 1;
    private ScreenState screenState;
    private int phase;
    private boolean stage3Next;
    private int currentPlayer;
    private boolean [] userPlants;
    private String [] powerPlantResources;
    private int step;
    private int baboon;


    public GameState() throws IOException
    {
        game = new Board();
        screenState = ScreenState.BiddingScreen;
        stage3Next = false;
        currentPlayer = 0;
        userPlants = new boolean [3];
        powerPlantResources = new String [3];
        for (int x = 0; x < 3; x++)
        {
            userPlants[x] = false;
            powerPlantResources[x] = "";
        }
        phase = 1;
        step=1;
        baboon = 0;
    }

    public int nextBidPlayer()
    {
        //find the next player with a true in powerplant phase
        ArrayList<Player> players = game.getPlayers();
        for(int i = 0; i<4; i++)
        {
            if(game.getPowerPlantPhase().get(players.get(i).getName())==true && players.get(i).getName()!=currentPlayer)
            {
                setCurrentPlayer(players.get(i).getName());
                return players.get(i).getName();
            }

        }

        return -1;
    }
    public int getBaboon()
    {
        return baboon;
    }
    public void setBaboon(int t)
    {
        baboon = t;
    }

    public int nextHigherBidPlayer()
    {
        //find the next player with a true in powerplant phase
        ArrayList<Player> players = game.getPlayers();
        for(int i = 0; i<4; i++)
        {
            if(game.getBidRound().get(players.get(i).getName())==true && players.get(i).getName()!=currentPlayer)
            {
                setCurrentPlayer(players.get(i).getName());
                return players.get(i).getName();
            }

        }

        return -1;
    }


    public int setNextBidPlayer()
    {
        int nextPlayer = 0;
        ArrayList<Player> players = game.getPlayers();
        if(getPlayerLocation()==3)
        {
            for(int i = 0; i<3; i++)
            {
                if(game.getPowerPlantPhase().get(players.get(i).getName())==true)
                {
                    nextPlayer = players.get(i).getName();
                    setCurrentPlayer(nextPlayer);
                    return currentPlayer;
                }
            }
        }
        else if(getPlayerLocation()==2)
        {
            if(game.getPowerPlantPhase().get(players.get(3).getName())==true)
            {
                nextPlayer = players.get(3).getName();
                setCurrentPlayer(nextPlayer);
                return currentPlayer;
            }
            for(int i = 0; i<2; i++)
            {
                if(game.getPowerPlantPhase().get(players.get(i).getName())==true)
                {
                    nextPlayer = players.get(i).getName();
                    setCurrentPlayer(nextPlayer);
                    return currentPlayer;
                }
            }
        }
        else if(getPlayerLocation()==1)
        {
            for(int i = 2; i<4; i++)
            {
                if(game.getPowerPlantPhase().get(players.get(i).getName())==true)
                {
                    nextPlayer = players.get(i).getName();
                    setCurrentPlayer(nextPlayer);
                    return currentPlayer;
                }
            }
            if(game.getPowerPlantPhase().get(players.get(0).getName())==true)
            {
                nextPlayer = players.get(0).getName();
                setCurrentPlayer(nextPlayer);
                return currentPlayer;
            }
        }
        else if(getPlayerLocation()==0)
        {
            for(int i = 1; i<4; i++)
            {
                if(game.getPowerPlantPhase().get(players.get(i).getName())==true)
                {
                    nextPlayer = players.get(i).getName();
                    setCurrentPlayer(nextPlayer);
                    return currentPlayer;
                }
            }
        }
        return setNextPlayer();
    }

    public int setNextPlayer()
    {
        int nextPlayer = 0;
        if(getPlayerLocation()==3)
            nextPlayer = getBoard().getPlayers().get(0).getName();
        else
            nextPlayer= getBoard().getPlayers().get(getPlayerLocation()+1).getName();
        setCurrentPlayer(nextPlayer);
        return currentPlayer;
    }

    public int getPhase()
    {
        return phase;
    }

    public void setPhase(int temp)
    {
        phase = temp;
    }

    public int getStep()
    {
        return step;
    }

    public void setStep(int temp)
    {
        step = temp;
    }

    public PowerPlant getSelling()
    {
        return game.getSelling();
    }

    public int nextPlayer()
    {
        int nextPlayer = 0;
        if(getPlayerLocation()==3)
            nextPlayer = getBoard().getPlayers().get(0).getName();
        else
            nextPlayer= getBoard().getPlayers().get(getPlayerLocation()+1).getName();
        setCurrentPlayer(nextPlayer);
        return currentPlayer;
    }

    public int getPlayerLocation()
    {
        for(int i = 0; i<game.getPlayers().size();i++)
        {
            if(game.getPlayers().get(i).getName()==currentPlayer)
                return i;
        }
        return -1;
    }

    public int getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setCurrentPlayer(int temp)
    {
        currentPlayer = temp;
    }

    public ArrayList<Player> getOrder()
    {
        ArrayList<Player> players = game.getPlayers();
        ArrayList<Player> order = new ArrayList<Player>();
        if(counter == 1)
        {
            for(int i = 0; i < 4; i ++)
            {
                int min = 0;
                int max = 3;
                order.add(players.get((int)(Math.random() * (max - min) + 1) + min));
                max--;
            }
            return order;
        }
        else
        {
            //Collections.sort(players);
        }

        return players;
    }

    public void stage3Next(boolean temp)
    {
        stage3Next = temp;
    }

    public boolean getStage3Next()
    {
        return stage3Next;
    }

    public ScreenState getScreenState()
    {
        return screenState;
    }

    public void setScreenState(ScreenState temp)
    {
        screenState = temp;
    }
    public Board getBoard()
    {
        return game;
    }

    public LinkedHashMap<String, Integer> getResources()
    {
        LinkedHashMap<String, Integer> overallResources = getBoard().getOverallResources();
        return overallResources;
    }

    public LinkedHashMap <String, Integer> getBoughtResources()
    {
        LinkedHashMap <String, Integer> boughtResources = new LinkedHashMap <String, Integer> ();
        boughtResources.put("coal", 24 - game.getOverallResources().get("coal"));
        boughtResources.put("oil", 24 - game.getOverallResources().get("oil"));
        boughtResources.put("garbage", 24 - game.getOverallResources().get("garbage"));
        boughtResources.put("uranium", 12 - game.getOverallResources().get("uranium"));
        return boughtResources;
    }

    public ArrayList<Integer> getWinner()//this should work up to comparing a tie between 2 people only
    {
        TreeMap<Integer, Integer> byPoweredCities = new TreeMap<Integer, Integer>();
        ArrayList<Integer> orderedPlayers = new ArrayList<Integer>();
        ArrayList<Integer> replaced = new ArrayList<Integer>();

        for(int x = 0; x < game.getPlayers().size(); x++)
        {
            if(byPoweredCities.containsKey(game.getPlayers().get(x).getPoweredCities()))
            {
                replaced.add(byPoweredCities.get(game.getPlayers().get(x).getPoweredCities()));
                byPoweredCities.put(game.getPlayers().get(x).getPoweredCities(), game.getPlayers().get(x).getName());
            }
            else
                byPoweredCities.put(game.getPlayers().get(x).getPoweredCities(), game.getPlayers().get(x).getName());
        }
        Iterator bpcit = byPoweredCities.keySet().iterator();
        while(bpcit.hasNext())
        {
            orderedPlayers.add(byPoweredCities.get(bpcit.next()));
        }
        if(replaced.size() == 1)
        {
            if(game.getPlayers().get(replaced.get(0)-1).getMoney() < game.getPlayers().get(byPoweredCities.get(game.getPlayers().get(replaced.get(0)).getPoweredCities()) - 1).getMoney())
                orderedPlayers.add(orderedPlayers.indexOf(byPoweredCities.get(game.getPlayers().get(replaced.get(0)-1)))+1, replaced.get(0));
            else if(game.getPlayers().get(replaced.get(0)-1).getMoney() > game.getPlayers().get(byPoweredCities.get(game.getPlayers().get(replaced.get(0)).getPoweredCities()) - 1).getMoney())
                orderedPlayers.add(orderedPlayers.indexOf(byPoweredCities.get(game.getPlayers().get(replaced.get(0)-1)))-1, replaced.get(0));
            else
            {
                if(game.getPlayers().get(replaced.get(0)-1).getNumOfCities() < game.getPlayers().get(byPoweredCities.get(game.getPlayers().get(replaced.get(0)).getPoweredCities()) - 1).getNumOfCities())
                    orderedPlayers.add(orderedPlayers.indexOf(byPoweredCities.get(game.getPlayers().get(replaced.get(0)-1)))+1, replaced.get(0));
                else if(game.getPlayers().get(replaced.get(0)-1).getNumOfCities() > game.getPlayers().get(byPoweredCities.get(game.getPlayers().get(replaced.get(0)).getPoweredCities()) - 1).getNumOfCities())
                    orderedPlayers.add(orderedPlayers.indexOf(byPoweredCities.get(game.getPlayers().get(replaced.get(0)-1)))-1, replaced.get(0));
                else
                    orderedPlayers.add(orderedPlayers.indexOf(byPoweredCities.get(game.getPlayers().get(replaced.get(0)-1)))+1, replaced.get(0));
            }
        }

        return orderedPlayers;
    }

    public void phase1()
    {
        Collections.sort(getBoard().getPlayers());
        if (getBoard().getFirstRound())
            getBoard().setFirstRound(false);
        LinkedHashMap <Integer, Boolean> plantPhase = getBoard().getPowerPlantPhase();
        for (int x = 0; x < 4; x++)
        {
            plantPhase.put(x, true);
        }
        getBoard().setPowerPlantPhase(plantPhase);
    }

    public boolean [] getUserPlants()
    {
        return userPlants;
    }

    public void setUserPlants(boolean [] plants)
    {
        userPlants = plants;
    }

    public String [] getPowerPlantResources()
    {
        return powerPlantResources;
    }

    public void setPowerPlantResources(String [] plantsResources)
    {
        powerPlantResources = plantsResources;
    }

    public void resetArrays()
    {
        for (int x = 0; x < 3; x++)
        {
            userPlants[x] = false;
            powerPlantResources[x] = "";
        }
    }
}
