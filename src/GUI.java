import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class GUI extends JPanel implements MouseListener
{
    private JFrame f;
    private JPanel p;
    private TreeMap<String, BufferedImage> images = new TreeMap<>();
    private LinkedHashMap<Integer, ArrayList<Integer>> winBidCoords = new LinkedHashMap<>();
    private LinkedHashMap<PowerPlant, Rectangle> powerPlantBidCoords = new LinkedHashMap<>();
    private LinkedHashMap<String, Rectangle> buttons = new LinkedHashMap<>();
    private int bid;
    private boolean isInitialBid;
    ArrayList<Integer> coalX = new ArrayList<>();
    ArrayList<Integer> oilX = new ArrayList<>();
    ArrayList<Integer> gX = new ArrayList<>();
    ArrayList<Integer> uX = new ArrayList<>();
    ArrayList<Integer> coalX2 = new ArrayList<>();
    ArrayList<Integer> oilX2 = new ArrayList<>();
    ArrayList<Integer> gX2 = new ArrayList<>();
    ArrayList<Integer> uX2 = new ArrayList<>();

    GameState gs;
    int coalcnt = 0;
    int oilcnt = 0;
    int gcnt = 0;
    int ucnt = 0;
    int totalcost = 0;  //new
    int currentRPlayer; //new
    int playerCounter = 0;
    LinkedHashMap<String, Integer> rsrc;
    int coal;
    int oil;
    int garbage;
    int uranium;
    JButton bc;
    JButton bo;
    JButton bg;
    JButton bu;
    JLabel c;
    JLabel o;
    JLabel g;
    JLabel u;
    JLabel cost;

    GUI() throws IOException {
        images.put("startorig", ImageIO.read(new File("startorig.png")));
        images.put("pwrcscr", ImageIO.read(new File("pwrcscr.png")));
        images.put("endsc", ImageIO.read(new File("endsc.png")));
        images.put("mapsc", ImageIO.read(new File("mpscr.png")));
        images.put("rsrcsc", ImageIO.read(new File("rsrcscr.png")));
        images.put("coal", ImageIO.read(new File("coal.png")));
        images.put("coalbw", ImageIO.read(new File("coalbw.png")));
        images.put("oil", ImageIO.read(new File("oil.png")));
        images.put("oilbw", ImageIO.read(new File("oilbw.png")));
        images.put("garbage", ImageIO.read(new File("garbage.png")));
        images.put("garbagebw", ImageIO.read(new File("garbagebw.png")));
        images.put("uranium", ImageIO.read(new File("uranium.png")));
        images.put("uraniumbw", ImageIO.read(new File("uraniumbw.png")));
        images.put("topBar", ImageIO.read(new File("topBar.png")));
        images.put("bottomBar", ImageIO.read(new File("bottomBar.png")));
        images.put("map", ImageIO.read(new File("map.png")));
        images.put("sideBar", ImageIO.read(new File("sideBar.png")));
        images.put("backgroundWBorder", ImageIO.read(new File("backgroundWBorder.png")));
        images.put("background", ImageIO.read(new File("background.png")));
        images.put("plus", ImageIO.read(new File("plus.png")));
        bid=-1;
        isInitialBid=true;
        ArrayList<String> colors = new ArrayList<String>(Arrays.asList("green", "yellow", "red", "blue", "purple", "black"));
        for(String color: colors)
        {
            images.put(color, ImageIO.read(new File(color+".png")));
        }

        for(int i = 3; i<=50; i++)
        {
            if(!(i==41||i==43||i==45||i==47||i==48||i==49))
                images.put(""+i, ImageIO.read(new File(i+".png")));
        }
        images.put("phase3", ImageIO.read(new File("phase3.png")));
        c = new JLabel("Coal Bought: ");
        o = new JLabel("Oil Bought: ");
        g = new JLabel("Garbage Bought: ");
        u = new JLabel("Uranium Bought: ");
        cost = new JLabel("Total Cost: ");
        gs = new GameState();
        addMouseListener(this);
        //setGame();
        setStartScreen();
    }
    public void setStartScreen() {
        JFrame sF = new JFrame("Welcome to Power Grid");
        sF.setSize(3840, 2160);
        sF.setDefaultCloseOperation(3);
        sF.getContentPane().setBackground(Color.BLACK);
        sF.setVisible(true);
        JPanel startPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(images.get("startorig"), 0, 0, 1920, 1080, null);
            }

        };
        startPanel.setPreferredSize(new Dimension(1920, 1080));
        startPanel.setLayout(null);
        sF.add(startPanel);
        sF.pack();
        setVisible(true);

        JButton b = new JButton();
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        startPanel.add(b);
        b.setBounds(810, 505, 310, 60);
        b.setVisible(true);

        b.addActionListener(e -> {
            setGame();
            sF.dispose();
        });

        JButton exit = new JButton();
        exit.setContentAreaFilled(false);
        exit.setBorderPainted(false);
        startPanel.add(exit);
        exit.setBounds(860, 650, 200, 60);
        exit.setVisible(true);
        exit.addActionListener(e -> sF.dispose());
    }

    public void setGame() {
        f = new JFrame("Power Grid");
        f.setDefaultCloseOperation(3);
        f.setSize(1920, 1080);
        f.setVisible(true);
        addMouseListener(this);
        f.add(this);


        p = new JPanel(){
            public void paintComponent(Graphics g)
            {
                winBidCoords = new LinkedHashMap<>();
                powerPlantBidCoords = new LinkedHashMap<>();

                g.drawImage(images.get("background"), 0, 0, 1920, 1080, null);

                if(gs.getScreenState()==ScreenState.BiddingScreen || gs.getScreenState()==ScreenState.PowerPlantChoice || gs.getScreenState()==ScreenState.ResourceScreen)
                {
                    g.setFont(new Font("verdana", Font.BOLD, 20));
                    g.setColor(Color.WHITE);
                    //top bar
                    g.drawImage(images.get("topBar"), 0, 0, 1920, 1500, null);
                    g.drawString("Step "+gs.getStep(), 940, 50);
                    g.drawString("Phase "+gs.getPhase(), 930, 80);


                    //bottom bar
                    g.drawImage(images.get("bottomBar"), 0, 0, 1920, 1000, null);
                    int location = 0;
                    for (int z = 0; z < gs.getBoard().getPlayers().size(); z++)
                    {
                        if (gs.getBoard().getPlayers().get(z).getName() == gs.getCurrentPlayer())
                            location = z;
                    }
                    PowerPlant [] plants = gs.getBoard().getPlayers().get(location).getPlants();
                    if (plants[0] != null) {
                        if (gs.getUserPlants()[0]) {
                            g.setColor(Color.green);
                            g.fillRect(620, 900, 200, 160);
                        }
                        g.drawImage(images.get("" + plants[0].getIdentity()), 625, 905, 190, 150, null);
                    }
                    if (plants[1] != null) {
                        if (gs.getUserPlants()[1]) {
                            g.setColor(Color.green);
                            g.fillRect(860, 900, 200, 160);
                        }
                        g.drawImage(images.get("" + plants[1].getIdentity()), 865, 905, 190, 150, null);
                    }
                    if (plants[2] != null) {
                        if (gs.getUserPlants()[2]) {
                            g.setColor(Color.green);
                            g.fillRect(1100, 900, 200, 160);
                        }
                        g.drawImage(images.get("" + plants[2].getIdentity()), 1105, 905, 190, 150, null);
                    }
                    if(gs.getScreenState() == ScreenState.PowerPlantChoice)
                    {
                        System.out.println(gs.getCurrentPlayer());
                        g.drawImage(images.get(gs.getBoard().getPlayers().get(gs.getPlayerLocation()).getPlayerColor()), 400, 870, null);
                        g.drawString(gs.getBoard().getPlayers().get(gs.getPlayerLocation()).getMoney() + " Elektros", 370, 938);
                    }
                    else if(gs.getScreenState() == ScreenState.ResourceScreen)
                    {
                        System.out.println(gs.getCurrentPlayer());
                        g.drawImage(images.get(gs.getBoard().getPlayers().get(gs.getPlayerLocation()).getPlayerColor()), 400, 870, null);
                        g.drawString(gs.getBoard().getPlayers().get(gs.getPlayerLocation()).getMoney() + " Elektros", 370, 938);
                    }


                }

                if(gs.getScreenState() == ScreenState.EndScreen)
                {
                    try
                    {
                        g.drawImage(images.get("endsc"), 0, 0, 1920, 1080, null);
                        g.setFont(new Font("verdana", Font.BOLD, 50));
                        g.drawString("Game Over!", 805, 340);
                        ArrayList<Integer> winners = gs.getWinner();
                        g.drawString("1st Place: Player " + (winners.get(0) + 1), 430, 420);
                        g.drawString("2nd Place: Player " + (winners.get(1) + 1), 430, 560);
                        g.drawString("3rd Place: Player " + (winners.get(2) + 1), 430, 500);
                        g.drawString("4th Place: Player " + (winners.get(3) + 1), 430, 540);
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error Loading Images" + e.getMessage());
                    }
                }
                else if(gs.getScreenState()==ScreenState.WinBidScreen)
                {
                    try
                    {
                        gs.setBaboon(gs.getBaboon() + 1);
                        g.drawImage(images.get("backgroundWBorder"), 0, 0, 1920, 1080, null);
                        g.setColor(Color.WHITE);
                        g.setFont(new Font("verdana", Font.BOLD, 80));

                        //check if player has 3 powerplants
                        boolean isFull = true;
                        int emptyP=0;
                        PowerPlant[] plants = gs.getBoard().getPlayers().get(gs.getBoard().getBuyingPlayer()).getPlants();
                        for(int i = 0; i<3; i++)
                        {
                            if(plants[i]==null)
                            {
                                isFull = false;
                                emptyP=i;
                                break;
                            }
                        }
                        if(isFull) //player has 3 powerplants, needs to choose one to replace
                        {
                            g.drawString("Player "+(gs.getBoard().getBuyingPlayer()+1)+" won the bid! Click on", 400, 200);
                            g.drawString("the power plant you want to replace.", 150, 300);
                            g.drawImage(images.get(""+gs.getBoard().getSelling().getIdentity()), 860, 400, 200, 200, null); //powerplant bought
                            ArrayList<Integer> coords = new ArrayList<>();
                            int x = 560;
                            for(int i = 0; i<3; i++)
                            {
                                g.drawImage(images.get(""+plants[i].getIdentity()), x, 700, 200, 200, null);
                                coords.add(x);
                                coords.add(700);
                                winBidCoords.put(i, coords);
                                coords = new ArrayList<>();
                                x+=300;
                            }
                        }
                        else //player has room left
                        {
                            g.drawString("Player "+(gs.getBoard().getBuyingPlayer()+1)+" won the bid!", 550, 200);
                            g.drawString("Powerplant has been added.", 300, 300);
                            g.drawImage(images.get(""+gs.getBoard().getSelling().getIdentity()), 860, 400, 200, 200, null);

                            g.setColor(Color.WHITE);
                            g.fillRoundRect(860, 800, 200, 50, 10, 10);
                            g.setColor(Color.BLACK);
                            g.setFont(new Font("verdana", Font.BOLD, 20));
                            g.drawString("Continue", 910, 830);
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error Loading Images" + e.getMessage());
                    }
                }
                else if(gs.getScreenState()==ScreenState.BiddingScreen)
                {
                    //System.out.println("in bid");
                    //bid round
                    //false == bought a powerplant, or send in false
                    //check ok or pass
                    //pass in to initial bid or higher bid
                    //bidround - tells who to set next current player to
                    //false = out of it
                    //higher bid automatically
                    //powerplant phase:
                    //buying powerplant this round or not
                    //true - buying a powerplant
                    //false -
                    gs.setPhase(2);

                    try
                    {
                        buttons = new LinkedHashMap<>();
                        if(gs.getBoard().getStage()==3) //6 powerplants all available
                        {
                            int y = 300;
                            int x = 500;
                            int count = 0;
                            ArrayList<PowerPlant> plantMarket = gs.getBoard().getPlantMarket();
                            for(int r = 0; r<2; r++)
                            {
                                for(int c = 0; c<3; c++)
                                {
                                    Rectangle powerPlant = new Rectangle(x, y, 200, 200);
                                    powerPlantBidCoords.put(plantMarket.get(count), powerPlant);
                                    g.drawImage(images.get(""+plantMarket.get(count).getIdentity()), x, y, 200, 200, null);
                                    count++;
                                    x+=260;
                                }
                                y+=220;
                                x = 500;
                            }

                            g.drawString("Highest Bid: " + gs.getBoard().getCurrentBid(), 1700, 60);
                            g.drawString("Current Player: " +(gs.getCurrentPlayer()+1), 100, 900);

                            g.drawString("Pick a powerplant to bid on or", 1550, 850);
                            g.drawString("increase bid, then press OK", 1560, 880);

                            g.setColor(Color.WHITE);
                            Rectangle minusButton = new Rectangle(1750, 890, 30, 30);
                            g.drawRect(1750, 890, 30, 30);
                            g.fillRect(1750, 890, 30, 30);
                            g.setColor(Color.BLACK);
                            g.drawString("-", 1762, 910);
                            buttons.put("minus", minusButton);

                            g.setColor(Color.WHITE);
                            Rectangle plusButton = new Rectangle(1670, 890, 30, 30);
                            g.drawRect(1670, 890, 30, 30);
                            g.fillRect(1670, 890, 30, 30);
                            g.setColor(Color.BLACK);
                            g.drawString("+", 1678, 910);
                            buttons.put("plus", plusButton);

                            g.drawString(""+bid, 1715, 915);

                            g.setColor(Color.WHITE);
                            Rectangle okButton = new Rectangle(1750, 930, 50, 30);
                            g.drawRect(1750, 930, 50, 30);
                            g.fillRect(1750, 930, 50, 30);
                            g.setColor(Color.BLACK);
                            g.drawString("OK", 1760, 950);
                            buttons.put("ok", okButton);

                            g.setColor(Color.WHITE);
                            Rectangle passButton = new Rectangle(1650, 930, 70, 30);
                            g.drawRect(1650, 930, 70, 30);
                            g.fillRect(1650, 930, 70, 30);
                            g.setColor(Color.BLACK);
                            g.drawString("PASS", 1660, 950);
                            buttons.put("pass", passButton);

                            if(gs.getBoard().getSelling()!=null)
                            {
                                Rectangle rect = powerPlantBidCoords.get(gs.getBoard().getSelling());
                                g.setColor(Color.YELLOW);
                                g.drawRect(rect.x, rect.y, rect.width, rect.height);
                            }
                        }
                        else
                        {
                            int y = 300;
                            int x = 470;
                            int count = 0;
                            ArrayList<PowerPlant> plantMarket = gs.getBoard().getPlantMarket();
                            for(int r = 0; r<2; r++)
                            {
                                for(int c = 0; c<4; c++)
                                {
                                    Rectangle powerPlant = new Rectangle(x, y, 200, 200);
                                    if(count<4) //only add top four
                                        powerPlantBidCoords.put(plantMarket.get(count), powerPlant);
                                    if(count>3) //fade out bottom row
                                    {
                                        AlphaComposite ac = java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5F);
                                        ((Graphics2D) g).setComposite(ac);
                                        g.drawImage(images.get(""+plantMarket.get(count).getIdentity()), x, y, 200, 200, null);
                                    }
                                    else
                                        g.drawImage(images.get(""+plantMarket.get(count).getIdentity()), x, y, 200, 200, null);
                                    count++;
                                    x+=260;
                                }
                                y+=220;
                                x = 470;
                            }
                            AlphaComposite ac = java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0F);
                            ((Graphics2D) g).setComposite(ac);

                            g.drawString("Highest Bid: " + gs.getBoard().getCurrentBid(), 1700, 60);
                            g.drawString("Current Player: " +(gs.getCurrentPlayer()+1), 100, 900);

                            g.drawString("Pick a powerplant to bid on or", 1550, 850);
                            g.drawString("increase bid, then press OK", 1560, 880);

                            g.setColor(Color.WHITE);
                            Rectangle minusButton = new Rectangle(1750, 890, 30, 30);
                            g.drawRect(1750, 890, 30, 30);
                            g.fillRect(1750, 890, 30, 30);
                            g.setColor(Color.BLACK);
                            g.drawString("-", 1762, 910);
                            buttons.put("minus", minusButton);

                            g.setColor(Color.WHITE);
                            Rectangle plusButton = new Rectangle(1670, 890, 30, 30);
                            g.drawRect(1670, 890, 30, 30);
                            g.fillRect(1670, 890, 30, 30);
                            g.setColor(Color.BLACK);
                            g.drawString("+", 1678, 910);
                            buttons.put("plus", plusButton);

                            g.drawString(""+bid, 1715, 915);

                            g.setColor(Color.WHITE);
                            Rectangle okButton = new Rectangle(1750, 930, 50, 30);
                            g.drawRect(1750, 930, 50, 30);
                            g.fillRect(1750, 930, 50, 30);
                            g.setColor(Color.BLACK);
                            g.drawString("OK", 1760, 950);
                            buttons.put("ok", okButton);

                            g.setColor(Color.WHITE);
                            Rectangle passButton = new Rectangle(1650, 930, 70, 30);
                            g.drawRect(1650, 930, 70, 30);
                            g.fillRect(1650, 930, 70, 30);
                            g.setColor(Color.BLACK);
                            g.drawString("PASS", 1660, 950);
                            buttons.put("pass", passButton);

                            if(gs.getBoard().getSelling()!=null)
                            {
                                Rectangle rect = powerPlantBidCoords.get(gs.getBoard().getSelling());
                                g.setColor(Color.YELLOW);
                                g.drawRect(rect.x, rect.y, rect.width, rect.height);
                            }
                        }

                        //players all true (all have bought powerplant/declined)
                        //if(gs.getBoard().getPlayers().get(gs.getBoard().getPlayers().size()-1).getName() == gs.getCurrentPlayer())
                        //gs.setScreenState(ScreenState.ResourceScreen);
                        //repaint();
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error Loading Images " + e.getMessage());
                    }
                }
                else if(gs.getScreenState() == ScreenState.MapScreen)
                {
                    try
                    {
                        g.drawImage(images.get("mapsc"), 0, 0, 1920, 1080, null);
                        LinkedHashMap <String, CityNode> map = gs.getBoard().getMap().getMap();
                        CityNode temp = null;;
                        String color = "";
                        ArrayList <String> cityNames = gs.getBoard().getMap().getCityNames();
                        g.drawImage(images.get("mapsc"), 0, 0, 1920, 1080, null);
                        g.setFont(new Font("verdana", Font.BOLD, 13));
                        for (int x = 0; x < cityNames.size(); x++)
                        {
                            g.drawString(cityNames.get(x), 1690, 100+(25*x));
                            temp = map.get(cityNames.get(x));
                            for (int z = 0; z < 3; z++)
                            {
                                if (temp.getOccupants()[z] != -1)
                                {
                                    for (int y = 0; y < gs.getBoard().getPlayers().size(); y++)
                                    {
                                        if (gs.getBoard().getPlayers().get(y).getName() == temp.getOccupants()[z])
                                            color = gs.getBoard().getPlayers().get(y).getPlayerColor();
                                    }
                                    if (color.equals("lilac"))
                                        g.setColor(Color.magenta);
                                    else if (color.equals("natural"))
                                        g.setColor(Color.black);
                                    else if (color.equals("green"))
                                        g.setColor(Color.green);
                                    else if (color.equals("red"))
                                        g.setColor(Color.red);
                                    else if (color.equals("yellow"))
                                        g.setColor(Color.yellow);
                                    else if (color.equals("blue"))
                                        g.setColor(Color.blue);
                                    g.fillRect(1812+(33*z), 89+(25*x), 13, 13);
                                }
                            }
                        }
                        g.setColor(Color.black);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error Loading Images" + e.getMessage());

                    }
                }

                else if(gs.getScreenState() == ScreenState.ResourceScreen)
                {
                    rsrc = gs.getResources();
                    coal = rsrc.get("coal");
                    oil = rsrc.get("oil");
                    garbage = rsrc.get("garbage");
                    uranium = rsrc.get("uranium");

                    int l = 0;

                    int j = 1;
                    int shiftExtra = 1;
                    int iters = 0;
                    int coalbw = 24 - coal;
                    int oilbw = 24 - oil;
                    int garbagebw = 24-garbage;
                    int uraniumbw = 12 - uranium;
                    int cNum = 0, oNum = 0, gNum = 0, uNum = 0;

                    try
                    {
                        g.drawImage(images.get("rsrcsc"), 0, 0, 1920, 1080, null);
                        int location = 0;
                        for (int z = 0; z < gs.getBoard().getPlayers().size(); z++)
                        {
                            if (gs.getBoard().getPlayers().get(z).getName() == gs.getCurrentPlayer())
                                location = z;
                        }
                        PowerPlant [] plants = gs.getBoard().getPlayers().get(location).getPlants();
                        if (plants[0] != null) {
                            if (gs.getUserPlants()[0]) {
                                g.setColor(Color.green);
                                g.fillRect(620, 900, 200, 160);
                            }
                            g.drawImage(images.get("" + plants[0].getIdentity()), 625, 905, 190, 150, null);
                        }
                        if (plants[1] != null) {
                            if (gs.getUserPlants()[1]) {
                                g.setColor(Color.green);
                                g.fillRect(860, 900, 200, 160);
                            }
                            g.drawImage(images.get("" + plants[1].getIdentity()), 865, 905, 190, 150, null);
                        }
                        if (plants[2] != null) {
                            if (gs.getUserPlants()[2]) {
                                g.setColor(Color.green);
                                g.fillRect(1100, 900, 200, 160);
                            }
                            g.drawImage(images.get("" + plants[2].getIdentity()), 1105, 905, 190, 150, null);
                        }
                        //System.out.println(gs.getBoard().getPlayers().get(gs.getPlayerLocation()).getPlayerColor());
                        g.drawImage(images.get(gs.getBoard().getPlayers().get(gs.getPlayerLocation()).getPlayerColor()), 130, 950, null);
                        g.drawString("Player " + gs.getBoard().getPlayers().get(gs.getPlayerLocation()).getName(), 100, 1000);
                        int index = gs.getPlayerLocation();


                        //coal
                        for(int i = 1; i <= 18; i++)
                        {
                            if(j <= coalbw)
                            {
                                if(shiftExtra <= 3)
                                {
                                    g.drawImage(images.get("coalbw"), ((30 + (70 * i)) + (100 * iters)), 230, 50, 50, null);
                                    j++;
                                    shiftExtra++;
                                    //System.out.println(((30 + (70 * i)) + (100 * iters)));

                                }
                                else
                                {
                                    iters++;
                                    shiftExtra = 1;
                                    g.drawImage(images.get("coalbw"), ((30 + (70 * i)) + (100 * iters)), 230, 50, 50, null);
                                    j++;
                                    shiftExtra++;
                                    //System.out.println(((30 + (70 * i)) + (100 * iters)));

                                }
                            }
                            else
                            {
                                if(shiftExtra <= 3)
                                {
                                    g.drawImage(images.get("coal"), ((30 + (70 * i)) + (100 * iters)), 230, 50, 50, null);
                                    shiftExtra++;
                                    cNum++;
                                    coalX.add(((30 + (70 * i)) + (100 * iters)));

                                }
                                else
                                {
                                    iters++;
                                    shiftExtra = 1;
                                    g.drawImage(images.get("coal"), ((30 + (70 * i)) + (100 * iters)), 230, 50, 50, null);
                                    shiftExtra++;
                                    cNum++;
                                    coalX.add(((30 + (70 * i)) + (100 * iters)));

                                }
                            }
                        }
                        int coal2 = 0;
                        j = 0;
                        shiftExtra = 1;
                        iters = 0;
                        coal2 = coal - cNum;
                        coalbw = 6 - coal2;

                        //coal
                        for(int i = 1; i <= 6; i++)
                        {

                            if(j < coalbw || coalbw == 1)
                            {
                                if(shiftExtra <= 3)
                                {
                                    g.drawImage(images.get("coalbw"), ((30 + (70 * i)) + (100 * iters)), 550, 50, 50, null);
                                    j++;
                                    shiftExtra++;
                                    if(coalbw == 1)
                                        coalbw = 0;
                                }
                                else
                                {
                                    iters++;
                                    shiftExtra = 1;
                                    g.drawImage(images.get("coalbw"), ((30 + (70 * i)) + (100 * iters)), 550, 50, 50, null);
                                    j++;
                                    shiftExtra++;

                                }
                            }
                            else
                            {
                                if(shiftExtra <= 3)
                                {
                                    g.drawImage(images.get("coal"), ((30 + (70 * i)) + (100 * iters)), 550, 50, 50, null);
                                    shiftExtra++;
                                    coalX2.add(((30 + (70 * i)) + (100 * iters)));

                                }
                                else
                                {
                                    iters++;
                                    shiftExtra = 1;
                                    g.drawImage(images.get("coal"), ((30 + (70 * i)) + (100 * iters)), 550, 50, 50, null);
                                    shiftExtra++;
                                    coalX2.add(((30 + (70 * i)) + (100 * iters)));

                                }
                            }
                        }


                        j = 1;
                        shiftExtra = 1;
                        iters = 0;
                        //oil
                        for(int i = 1; i <= 18; i++)
                        {
                            if(j <= oilbw)
                            {
                                if(shiftExtra <= 3)
                                {
                                    g.drawImage(images.get("oilbw"), ((30 + (70 * i)) + (100 * iters)), 290, 50, 50, null);
                                    j++;
                                    shiftExtra++;
                                }
                                else
                                {
                                    iters++;
                                    shiftExtra = 1;
                                    g.drawImage(images.get("oilbw"), ((30 + (70 * i)) + (100 * iters)), 290, 50, 50, null);
                                    j++;
                                    shiftExtra++;

                                }
                            }
                            else
                            {
                                if(shiftExtra <= 3)
                                {
                                    g.drawImage(images.get("oil"), ((30 + (70 * i)) + (100 * iters)), 290, 50, 50, null);
                                    shiftExtra++;
                                    oNum++;
                                    oilX.add(((30 + (70 * i)) + (100 * iters)));

                                }
                                else
                                {
                                    iters++;
                                    shiftExtra = 1;
                                    g.drawImage(images.get("oil"), ((30 + (70 * i)) + (100 * iters)), 290, 50, 50, null);
                                    shiftExtra++;
                                    oNum++;
                                    oilX.add(((30 + (70 * i)) + (100 * iters)));

                                }
                            }
                        }
                        j = 0;
                        shiftExtra = 1;
                        iters = 0;
                        int oil2 = 0;
                        oil2 = oil - oNum;
                        oilbw = 6 - oil2;

                        for(int i = 1; i <= 6; i++)
                        {
                            if(j < oilbw || oilbw == 1)
                            {
                                if(shiftExtra <= 3)
                                {
                                    g.drawImage(images.get("oilbw"), ((30 + (70 * i)) + (100 * iters)), 610, 50, 50, null);
                                    j++;
                                    shiftExtra++;
                                    if(oilbw == 1)
                                        oilbw = 0;
                                }
                                else
                                {
                                    iters++;
                                    shiftExtra = 1;
                                    g.drawImage(images.get("oilbw"), ((30 + (70 * i)) + (100 * iters)), 610, 50, 50, null);
                                    j++;
                                    shiftExtra++;

                                }
                            }
                            else
                            {
                                if(shiftExtra <= 3)
                                {
                                    g.drawImage(images.get("oil"), ((30 + (70 * i)) + (100 * iters)), 610, 50, 50, null);
                                    shiftExtra++;
                                    oilX2.add(((30 + (70 * i)) + (100 * iters)));

                                }
                                else
                                {
                                    iters++;
                                    shiftExtra = 1;
                                    g.drawImage(images.get("oil"), ((30 + (70 * i)) + (100 * iters)), 610, 50, 50, null);
                                    shiftExtra++;
                                    oilX2.add(((30 + (70 * i)) + (100 * iters)));

                                }
                            }
                        }

                        j = 1;
                        shiftExtra = 1;
                        iters = 0;
                        //garbage
                        for(int i = 1; i <= 18; i++)
                        {
                            if(j <= garbagebw)
                            {
                                if(shiftExtra <= 3)
                                {
                                    g.drawImage(images.get("garbagebw"), ((30 + (70 * i)) + (100 * iters)), 350, 50, 50, null);
                                    j++;
                                    shiftExtra++;
                                }
                                else
                                {
                                    iters++;
                                    shiftExtra = 1;
                                    g.drawImage(images.get("garbagebw"), ((30 + (70 * i)) + (100 * iters)), 350, 50, 50, null);
                                    j++;
                                    shiftExtra++;

                                }
                            }
                            else
                            {
                                if(shiftExtra <= 3)
                                {
                                    g.drawImage(images.get("garbage"), ((30 + (70 * i)) + (100 * iters)), 350, 50, 50, null);
                                    shiftExtra++;
                                    gNum++;
                                    gX.add(((30 + (70 * i)) + (100 * iters)));

                                }
                                else
                                {
                                    iters++;
                                    shiftExtra = 1;
                                    g.drawImage(images.get("garbage"), ((30 + (70 * i)) + (100 * iters)), 350, 50, 50, null);
                                    shiftExtra++;
                                    gNum++;
                                    gX.add(((30 + (70 * i)) + (100 * iters)));

                                }
                            }
                        }
                        j = 0;
                        shiftExtra = 1;
                        iters = 0;
                        int garbage2 = 0;
                        garbage2 = garbage - gNum;
                        garbagebw = 6 - garbage2;
                        for(int i = 1; i <= 6; i++)
                        {
                            if(j < garbagebw || garbagebw == 1)
                            {
                                if(shiftExtra <= 3)
                                {
                                    g.drawImage(images.get("garbagebw"), ((30 + (70 * i)) + (100 * iters)), 670, 50, 50, null);
                                    j++;
                                    shiftExtra++;
                                    if(garbagebw == 1)
                                        garbagebw = 0;
                                }
                                else
                                {
                                    iters++;
                                    shiftExtra = 1;
                                    g.drawImage(images.get("garbagebw"), ((30 + (70 * i)) + (100 * iters)), 670, 50, 50, null);
                                    j++;
                                    shiftExtra++;

                                }
                            }
                            else
                            {
                                if(shiftExtra <= 3)
                                {
                                    g.drawImage(images.get("garbage"), ((30 + (70 * i)) + (100 * iters)), 670, 50, 50, null);
                                    shiftExtra++;
                                    gX2.add(((30 + (70 * i)) + (100 * iters)));

                                }
                                else
                                {
                                    iters++;
                                    shiftExtra = 1;
                                    g.drawImage(images.get("garbage"), ((30 + (70 * i)) + (100 * iters)), 670, 50, 50, null);
                                    shiftExtra++;
                                    gX2.add(((30 + (70 * i)) + (100 * iters)));

                                }
                            }
                        }

                        j = 1;
                        for(int i = 0; i < 6; i++)
                        {
                            if(j <= uraniumbw)
                            {
                                g.drawImage(images.get("uraniumbw"), 170 + (310 * i), 410, 50, 50, null);
                                j++;


                            }
                            else
                            {
                                g.drawImage(images.get("uranium"), 170 + (310 * i), 410, 50, 50, null);
                                uNum++;
                                uX.add(170 + (310 * i));
                            }
                        }

                        int u2 = 0;
                        j = 0;
                        int f = 1;
                        u2 = uranium - uNum;
                        uraniumbw = 6 - u2;

                        for(int i = 0; i < 6; i++)
                        {
                            if(j < uraniumbw || uraniumbw == 1)
                            {
                                g.drawImage(images.get("uraniumbw"), 170 + (310 * i), 730, 50, 50, null);
                                j++;
                            }
                            else
                            {
                                g.drawImage(images.get("uranium"), 170 + (310 * i), 730, 50, 50, null);
                                uX2.add(170 + (310 * i));

                            }
                        }



                        //addButtons();

                    }
                    catch(Exception e)
                    {
                        System.out.println("Error Loading Images" + e.getMessage());
                    }
                }

                else if(gs.getScreenState() == ScreenState.PowerPlantChoice)
                {
                    try
                    {
                        int location = 0;
                        for (int z = 0; z < gs.getBoard().getPlayers().size(); z++)
                        {
                            if (gs.getBoard().getPlayers().get(z).getName() == gs.getCurrentPlayer())
                                location = z;
                        }
                        PowerPlant [] plants = gs.getBoard().getPlayers().get(location).getPlants();
                        g.drawImage(images.get("pwrcscr"), 0, 0, 1920, 1080, null);

                        plants[0] = plants[1];

                        if (plants[0] != null)
                        {
                            if (gs.getUserPlants()[0])
                            {
                                g.setColor(Color.green);
                                g.fillRect(620, 900, 200, 160);
                            }
                            g.drawImage(images.get("" + plants[0].getIdentity()), 625, 905, 190, 150, null);
                            if (plants[0].getResource().equals("hybrid"))
                            {
                                if (gs.getPowerPlantResources()[0].equals("coal"))
                                {
                                    g.fillRect(665, 935, 110, 40);
                                }
                                else if (gs.getPowerPlantResources()[0].equals("oil"))
                                {
                                    g.fillRect(665, 975, 110, 40);
                                }
                                g.setColor(Color.white);
                                g.fillRect(670, 940, 100, 30);
                                g.fillRect(670, 980, 100, 30);
                                g.setColor(Color.black);
                                g.setFont(new Font("verdana", Font.BOLD, 25));
                                g.drawString("coal", 690, 965);
                                g.drawString("oil", 705, 1005);
                            }
                        }
                        if (plants[1] != null)
                        {
                            if (gs.getUserPlants()[1])
                            {
                                g.setColor(Color.green);
                                g.fillRect(860, 900, 200, 160);
                            }
                            g.drawImage(images.get("" + plants[1].getIdentity()), 865, 905, 190, 150, null);
                            if (plants[1].getResource().equals("hybrid"))
                            {
                                if (gs.getPowerPlantResources()[1].equals("coal"))
                                {
                                    g.fillRect(905, 935, 110, 40);
                                }
                                else if (gs.getPowerPlantResources()[1].equals("oil"))
                                {
                                    g.fillRect(905, 975, 110, 40);
                                }
                                g.setColor(Color.white);
                                g.fillRect(910, 940, 100, 30);
                                g.fillRect(910, 980, 100, 30);
                                g.setColor(Color.black);
                                g.setFont(new Font("verdana", Font.BOLD, 25));
                                g.drawString("coal", 930, 965);
                                g.drawString("oil", 945, 1005);
                            }
                        }
                        if (plants[2] != null)
                        {
                            if (gs.getUserPlants()[2])
                            {
                                g.setColor(Color.green);
                                g.fillRect(1100, 900, 200, 160);
                            }
                            g.drawImage(images.get("" + plants[2].getIdentity()), 1105, 905, 190, 150, null);
                            if (plants[2].getResource().equals("hybrid"))
                            {
                                if (gs.getPowerPlantResources()[2].equals("coal"))
                                {
                                    g.fillRect(1145, 935, 110, 40);
                                }
                                else if (gs.getPowerPlantResources()[2].equals("oil"))
                                {
                                    g.fillRect(1145, 975, 110, 40);
                                }
                                g.setColor(Color.white);
                                g.fillRect(1150, 940, 100, 30);
                                g.fillRect(1150, 980, 100, 30);
                                g.setColor(Color.black);
                                g.setFont(new Font("verdana", Font.BOLD, 25));
                                g.drawString("coal", 1170, 965);
                                g.drawString("oil", 1185, 1005);
                            }
                        }
                        this.addMouseListener(new MouseAdapter()
                        {
                            @Override
                            public void mouseReleased(MouseEvent e)
                            {
                                int x, y;
                                x = e.getX();
                                y = e.getY();
                                if (gs.getScreenState() == ScreenState.PowerPlantChoice)
                                {
                                    int location = 0;
                                    for (int z = 0; z < gs.getBoard().getPlayers().size(); z++)
                                    {
                                        if (gs.getBoard().getPlayers().get(z).getName() == gs.getCurrentPlayer())
                                            location = z;
                                    }
                                    if (x > 1565 && y > 1010 && x < 1565+90 && y < 1010+30)
                                    {
                                        if (gs.getBoard().getPlayers().get(location).powerCities(gs.getUserPlants(), gs.getPowerPlantResources()))
                                        {
                                            if (gs.getBoard().getPlayers().get(gs.getBoard().getPlayers().size()-1).getName() == gs.getCurrentPlayer())
                                            {
                                                for (int z = 0; z < gs.getBoard().getPlayers().size(); z++)
                                                {
                                                    if (gs.getBoard().getPlayers().get(z).getNumOfCities() >= 17)
                                                    {
                                                        gs.setScreenState(ScreenState.EndScreen);
                                                        repaint();
                                                    }
                                                }
                                                gs.getBoard().checkStage(gs.getStage3Next());
                                                gs.phase1();
                                                gs.setCurrentPlayer(gs.getBoard().getPlayers().get(0).getName());
                                                gs.resetArrays();
                                                gs.setScreenState(ScreenState.BiddingScreen);
                                                repaint();
                                            }
                                            else
                                            {
                                                int temp = 0;
                                                for (int z = 0; z < gs.getBoard().getPlayers().size(); z++)
                                                {
                                                    if (gs.getBoard().getPlayers().get(z).getName() == gs.getCurrentPlayer())
                                                    {
                                                        temp = z;
                                                        break;
                                                    }
                                                }
                                                gs.setCurrentPlayer(temp+1);
                                                gs.resetArrays();
                                                repaint();
                                            }
                                        }
                                        else
                                        {
                                            gs.resetArrays();
                                            repaint();
                                        }

                                    }
                                    if (plants[0].getResource().equals("hybrid"))
                                    {
                                        if (x > 670 && y > 940 && x < 670+100 && y < 940+30)
                                        {
                                            String [] temp = gs.getPowerPlantResources();
                                            if (temp[0].equals(""))
                                                temp[0] = "coal";
                                            else
                                                temp[0] = "";
                                            gs.setPowerPlantResources(temp);
                                            boolean [] tempo = gs.getUserPlants();
                                            boolean tem = tempo[0];
                                            tempo[0] = !tem;
                                            gs.setUserPlants(tempo);
                                            repaint();
                                        }
                                        else if (x > 670 && y > 980 && x < 670+100 && y < 980+30)
                                        {
                                            String [] temp = gs.getPowerPlantResources();
                                            if (temp[0].equals(""))
                                                temp[0] = "oil";
                                            else
                                                temp[0] = "";
                                            gs.setPowerPlantResources(temp);
                                            boolean [] tempo = gs.getUserPlants();
                                            boolean tem = tempo[0];
                                            tempo[0] = !tem;
                                            gs.setUserPlants(tempo);
                                            repaint();
                                        }
                                    }
                                    else if (x > 625 && y > 905 && x < 625+190 && y < 905+150)
                                    {
                                        boolean [] tempo = gs.getUserPlants();
                                        boolean tem = tempo[0];
                                        tempo[0] = !tem;
                                        gs.setUserPlants(tempo);
                                        String [] temp = gs.getPowerPlantResources();
                                        temp[0] = plants[0].getResource();
                                        gs.setPowerPlantResources(temp);
                                        repaint();
                                    }
                                    // 910, 940, 100, 30
                                    // 910, 980, 100, 30
                                    if (plants[1].getResource().equals("hybrid"))
                                    {
                                        if (x > 910 && y > 940 && x < 910+100 && y < 940+30)
                                        {
                                            String [] temp = gs.getPowerPlantResources();
                                            if (temp[1].equals(""))
                                                temp[1] = "coal";
                                            else
                                                temp[1] = "";
                                            gs.setPowerPlantResources(temp);
                                            boolean [] tempo = gs.getUserPlants();
                                            boolean tem = tempo[1];
                                            tempo[1] = !tem;
                                            gs.setUserPlants(tempo);
                                            repaint();
                                        }
                                        else if (x > 910 && y > 980 && x < 910+100 && y < 980+30)
                                        {
                                            String [] temp = gs.getPowerPlantResources();
                                            if (temp[1].equals(""))
                                                temp[1] = "oil";
                                            else
                                                temp[1] = "";
                                            gs.setPowerPlantResources(temp);
                                            boolean [] tempo = gs.getUserPlants();
                                            boolean tem = tempo[1];
                                            tempo[1] = !tem;
                                            gs.setUserPlants(tempo);
                                            repaint();
                                        }
                                    }
                                    else if (x > 865 && y > 905 && x < 865+190 && y < 905+150)
                                    {
                                        boolean [] tempo = gs.getUserPlants();
                                        boolean tem = tempo[1];
                                        tempo[1] = !tem;
                                        gs.setUserPlants(tempo);
                                        String [] temp = gs.getPowerPlantResources();
                                        temp[1] = plants[1].getResource();
                                        gs.setPowerPlantResources(temp);
                                        repaint();
                                    }
                                    // 1150, 940, 100, 30
                                    // 1150, 980, 100, 30
                                    if (plants[2].getResource().equals("hybrid"))
                                    {
                                        if (x > 1150 && y > 940 && x < 1150+100 && y < 940+30)
                                        {
                                            String [] temp = gs.getPowerPlantResources();
                                            if (temp[2].equals(""))
                                                temp[2] = "coal";
                                            else
                                                temp[2] = "";
                                            gs.setPowerPlantResources(temp);
                                            boolean [] tempo = gs.getUserPlants();
                                            boolean tem = tempo[2];
                                            tempo[2] = !tem;
                                            gs.setUserPlants(tempo);
                                            repaint();
                                        }
                                        else if (x > 1150 && y > 980 && x < 1150+100 && y < 980+30)
                                        {
                                            String [] temp = gs.getPowerPlantResources();
                                            if (temp[2].equals(""))
                                                temp[2] = "oil";
                                            else
                                                temp[2] = "";
                                            gs.setPowerPlantResources(temp);
                                            boolean [] tempo = gs.getUserPlants();
                                            boolean tem = tempo[2];
                                            tempo[2] = !tem;
                                            gs.setUserPlants(tempo);
                                            repaint();
                                        }
                                    }
                                    else if (x > 1105 && y > 905 && x < 1105+190 && y < 905+105)
                                    {
                                        boolean [] tempo = gs.getUserPlants();
                                        boolean tem = tempo[2];
                                        tempo[2] = !tem;
                                        gs.setUserPlants(tempo);
                                        String [] temp = gs.getPowerPlantResources();
                                        temp[2] = plants[2].getResource();
                                        gs.setPowerPlantResources(temp);
                                        repaint();
                                    }
                                }
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error Loading Images" + e.getMessage());
                    }
                }


            }
        };
        p.addMouseListener(this);
        p.setPreferredSize(new Dimension(1920, 1080));
        f.add(p);
        setVisible(true);
    }

    public void updateText(String rsrc)
    {
        if(rsrc.equalsIgnoreCase("coal"))
        {
            p.setLayout(null);
            c.setFont(new Font("verdana", Font.BOLD, 15));
            c.setText("Coal Bought: " + coalcnt);
            c.setBounds(50, 820, 150, 30);
            p.add(c);
            c.setVisible(true);

        }
        else if(rsrc.equalsIgnoreCase("oil"))
        {
            p.setLayout(null);
            o.setFont(new Font("verdana", Font.BOLD, 15));
            o.setText("Oil Bought: " + oilcnt);
            o.setBounds(260, 820, 150, 30);
            p.add(o);
            o.setVisible(true);
        }
        else if(rsrc.equalsIgnoreCase("garbage"))
        {
            p.setLayout(null);
            g.setFont(new Font("verdana", Font.BOLD, 15));
            g.setText("Garbage Bought: " + gcnt);
            g.setBounds(460, 820, 200, 30);
            p.add(g);
            g.setVisible(true);
        }
        else
        {
            p.setLayout(null);
            u.setFont(new Font("verdana", Font.BOLD, 15));
            u.setText("Uranium Bought: " + ucnt);
            u.setBounds(690, 820, 200, 30);
            p.add(u);
            u.setVisible(true);
        }

        p.setLayout(null);
        cost.setFont(new Font("verdana", Font.BOLD, 15));
        cost.setText("Total Cost: " + totalcost);
        cost.setBounds(890, 820, 200, 30);
        p.add(cost);
        cost.setVisible(true);
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        int x, y;
        x = e.getX();
        y = e.getY();
        boolean bool = true;
        //System.out.println(x+","+y);
        //clicking powerplants
        if(gs.getScreenState()==ScreenState.BiddingScreen)
        {
            if(isInitialBid)
            {
                for(PowerPlant powerPlant: powerPlantBidCoords.keySet())
                {

                    if(powerPlantBidCoords.get(powerPlant).contains(x,y))
                    {
                        //System.out.println("1");
                        //System.out.println(powerPlantBidCoords.get(powerPlant));
                        gs.getBoard().setSelling(powerPlant);
                        bid=powerPlant.getIdentity();
                        //System.out.println(bid);
                        break;
                    }
                }
            }

            for(String str: buttons.keySet())
            {
                if(buttons.get(str).contains(x,y))
                {
                    //System.out.println("2");
                    if(str.equals("ok"))
                    {
                        if(isInitialBid)
                        {
                            int result = gs.getBoard().initialBid(true, gs.getSelling(), bid, gs.getCurrentPlayer());
                            isInitialBid=false;
                            if(result==1)
                            {
                                System.out.println("Result 1");
                                gs.setScreenState(ScreenState.WinBidScreen);
                                gs.setCurrentPlayer(gs.getBoard().getBuyingPlayer());
                                p.repaint();
                                break;
                            }
                            else if(result==2) //other players want it too
                            {
                                //System.out.println("ok-not win yet");
                                isInitialBid=false;
                                gs.nextBidPlayer();
                                break;
                            }
                        }
                        else //higher bid
                        {
                            if(bid<=gs.getBoard().getCurrentBid())
                                break;
                            bool=gs.getBoard().higherBid(true, bid, gs.getCurrentPlayer());
                            if(!bool)//bool is false
                            {
                                gs.setScreenState(ScreenState.WinBidScreen);
                                gs.setCurrentPlayer(gs.getBoard().getBuyingPlayer());
                                break;
                            }
                            else
                            {
                                gs.nextHigherBidPlayer();
                                break;
                            }
                        }
                    }
                    else if(str.equals("pass"))
                    {
                        if(!gs.getBoard().getFirstRound())
                        {
                            if(isInitialBid)
                            {
                                gs.getBoard().initialBid(false, null, bid, gs.getCurrentPlayer());
                                isInitialBid=false;
                                gs.nextBidPlayer();
                                break;
                            }
                            else
                            {
                                bool = gs.getBoard().higherBid(false, bid, gs.getCurrentPlayer());
                                if(!bool)
                                {
                                    gs.setScreenState(ScreenState.WinBidScreen);
                                    gs.setCurrentPlayer(gs.getBoard().getBuyingPlayer());
                                    break;
                                }
                                else
                                {
                                    gs.nextHigherBidPlayer();
                                    break;
                                }
                            }
                        }
                        else if(gs.getBoard().getFirstRound() && isInitialBid)
                        {
                            JOptionPane.showInputDialog(null, "Error: Must purchase a powerplant on first round.", null);
                            break;
                        }
                        else if(gs.getBoard().getFirstRound() && !isInitialBid)
                        {
                            bool = gs.getBoard().higherBid(false, bid, gs.getCurrentPlayer());
                            if(!bool)
                            {
                                gs.setScreenState(ScreenState.WinBidScreen);
                                gs.setCurrentPlayer(gs.getBoard().getBuyingPlayer());
                                break;
                            }
                            else
                            {
                                gs.nextHigherBidPlayer();
                                break;
                            }
                        }
                    }
                    else if(str.equals("plus"))
                    {
                        //System.out.println("plusbidbefore = " + bid);
                        bid = bid+1;
                        //System.out.println("plusbidafter = " + bid);
                        break;
                    }
                    else if(str.equals("minus"))
                    {
                        //System.out.println("minus bid before = " + bid);
                        if(bid-1>=gs.getSelling().getIdentity() && bid-1>gs.getBoard().getCurrentBid())
                            bid = bid-1;
                        //System.out.println("minus bid after = " + bid);
                        break;
                    }
                }
            }
            p.repaint();

        }
        else if(gs.getScreenState()==ScreenState.WinBidScreen)
        {
            Rectangle continueButton = new Rectangle(860, 800, 200, 50);
            System.out.println(gs.getBaboon());
            if(continueButton.contains(x,y))
            {
                if(gs.getBaboon() == 3)
                {
                    gs.setScreenState(ScreenState.ResourceScreen);
                    p.repaint();
                }
                else {
                    gs.getBoard().buyingPowerPlant();//INCLUDE METHOD TO ADD POWERPLANT
                    gs.getBoard().removeFromPlantMarket(gs.getBoard().getSelling());

                    if (gs.getBoard().addToPlantMarket().isRoundThree())
                        gs.stage3Next(true);
                    if (!gs.getBoard().getPowerPlantPhase().containsValue(true)) {
                        gs.getBoard().checkStage(gs.getStage3Next());
                        gs.setCurrentPlayer(gs.getBoard().getPlayers().get(3).getName()); //sets currentplayer to last player
                        gs.setScreenState(ScreenState.ResourceScreen);
                    } else {
                        gs.setCurrentPlayer(gs.nextBidPlayer());
                        gs.setScreenState(ScreenState.BiddingScreen);
                    }
                }
            }
            p.repaint();
            //clicking powerplants
            System.out.println(gs.getBaboon());
            for(Integer powerPlant: winBidCoords.keySet())
            {
                Rectangle button = new Rectangle(winBidCoords.get(powerPlant).get(0), winBidCoords.get(powerPlant).get(1), 200, 200);
                if(button.contains(x,y))
                {
                    if(gs.getBaboon() == 3)
                    {
                        gs.setScreenState(ScreenState.ResourceScreen);
                        p.repaint();
                    }
                    else {
                        gs.getBoard().buyingPowerPlant(powerPlant); //buy powerplant and switch
                        if (!gs.getBoard().getPowerPlantPhase().containsValue(true)) {
                            gs.getBoard().checkStage(gs.getStage3Next());
                            gs.setScreenState(ScreenState.ResourceScreen);
                        } else {
                            gs.getBoard().removeFromPlantMarket(gs.getBoard().getSelling());
                            if (gs.getBoard().addToPlantMarket().isRoundThree())
                                gs.stage3Next(true);
                            gs.setCurrentPlayer(gs.nextBidPlayer());
                            gs.setScreenState(ScreenState.BiddingScreen);
                        }
                    }
                }
            }
            p.repaint();
            isInitialBid=true;
            bid=-1;
            gs.getBoard().setCurrentBid(-1);
            p.repaint();
        } //elseif end

        else if(gs.getScreenState() == ScreenState.ResourceScreen)
        {
            currentRPlayer = gs.getCurrentPlayer();
            playerCounter++;
            x = e.getX();
            y = e.getY();
            updateText("coal");
            updateText("oil");
            updateText("garbage");
            updateText("uranium");
            gs.getBoard().getPlayers().get(gs.getPlayerLocation()).setMoney(4000);
            //System.out.println(gs.getBoard().getPlayers().get(gs.getPlayerLocation()).getMoney());


            if(coal <= 6)
            {
                if((x >= coalX2.get(0) && x <= (coalX2.get(coalX2.size() -1) + 50)) && (y >= 550 && y <= 600))
                {
                    int check = coalcnt + 1;
                    if(check > coal)
                    {
                        // System.out.println("+1");
                        JFrame error = new JFrame("Error");
                        error.setSize(800, 400);
                        error.setDefaultCloseOperation(3);
                        error.getContentPane().setBackground(Color.WHITE);
                        error.setVisible(true);
                        JPanel pf = new JPanel() {
                            public void paintComponent(Graphics f) {
                                f.setFont(new Font("verdana", Font.BOLD, 10));
                                f.drawString("Resource Not Available. Click Anywhere to Continue", 20, 200);
                                this.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseReleased(MouseEvent d) {
                                        error.dispose();
                                    }
                                });
                            }

                        };
                        //pf.setPreferredSize(new Dimension(1920, 1080));
                        pf.setLayout(null);
                        error.add(pf);
                        setVisible(true);
                    }
                    else {
                        coalcnt++;
                        // System.out.println("Coal add +1:  " + coalcnt);
                        totalcost += gs.getBoard().getIndResourceCost("coal", coal - coalcnt + 1);
                        updateText("coal");
                        //g.drawOval((coalX2.get(0)) + 50, 540, 15, 20);
                    }
                }
            }
            else
            {
                if(((x >= coalX.get(0) && x <= (coalX.get(coalX.size() -1) +50)) && (y >= 230 && y <= 280)) || ((x >= coalX2.get(0) && x <= (coalX2.get(coalX2.size() -1) + 50)) && (y >= 550 && y <= 600)))
                {
                    if((coalcnt) +1 > coal)
                    {
                        JFrame error = new JFrame("Error");
                        error.setSize(800, 400);
                        error.setDefaultCloseOperation(3);
                        error.getContentPane().setBackground(Color.WHITE);
                        error.setVisible(true);
                        JPanel startPanel = new JPanel() {
                            public void paintComponent(Graphics g) {
                                g.setFont(new Font("verdana", Font.BOLD, 20));
                                g.drawString("Resource Not Available. Click Anywhere to Continue", 200, 200);
                                this.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseReleased(MouseEvent d) {
                                        error.dispose();
                                    }
                                });
                            }

                        };
                        startPanel.setPreferredSize(new Dimension(1920, 1080));
                        startPanel.setLayout(null);
                        error.add(startPanel);
                        //error.pack();
                        setVisible(true);
                    }
                    else {
                        coalcnt++;
                        // System.out.println("2Coal add +1:  " + coalcnt);
                        totalcost += gs.getBoard().getIndResourceCost("coal", coal - coalcnt + 1);
                        updateText("coal");
                    }
                }
            }

            if(oil <= 6)
            {
                if((x >= oilX2.get(0) && x <= (oilX2.get(oilX2.size() -1) + 50)) && (y >= 610 && y <= 660))
                {
                    if(oilcnt + 1 > oil)
                    {
                        JFrame error = new JFrame("Error");
                        error.setSize(800, 400);
                        error.setDefaultCloseOperation(3);
                        error.getContentPane().setBackground(Color.WHITE);
                        error.setVisible(true);
                        JPanel startPanel = new JPanel() {
                            public void paintComponent(Graphics g) {
                                g.setFont(new Font("verdana", Font.BOLD, 20));
                                g.drawString("Resource Not Available. Click Anywhere to Continue", 200, 200);
                                this.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseReleased(MouseEvent d) {
                                        error.dispose();
                                    }
                                });
                            }

                        };
                        startPanel.setPreferredSize(new Dimension(1920, 1080));
                        startPanel.setLayout(null);
                        error.add(startPanel);
                        //error.pack();
                        setVisible(true);
                    }
                    else {
                        oilcnt++;
                        //  System.out.println("oil add +1:  " + oilcnt);
                        totalcost += gs.getBoard().getIndResourceCost("oil", oil - oilcnt + 1);
                        updateText("oil");
                    }

                }
            }
            else
            {
                if(((x >= oilX.get(0) && x <= (oilX.get(oilX.size() -1) +50)) && (y >= 290 && y <= 340)) || ((x >= oilX2.get(0) && x <= (oilX2.get(oilX2.size() -1) + 50)) && (y >= 610 && y <= 660)))
                {
                    if(oilcnt + 1 > oil)
                    {
                        JFrame error = new JFrame("Error");
                        error.setSize(800, 400);
                        error.setDefaultCloseOperation(3);
                        error.getContentPane().setBackground(Color.WHITE);
                        error.setVisible(true);
                        JPanel startPanel = new JPanel() {
                            public void paintComponent(Graphics g) {
                                g.setFont(new Font("verdana", Font.BOLD, 20));
                                g.drawString("Resource Not Available. Click Anywhere to Continue", 200, 200);
                                this.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseReleased(MouseEvent d) {
                                        error.dispose();
                                    }
                                });
                            }

                        };
                        startPanel.setPreferredSize(new Dimension(1920, 1080));
                        startPanel.setLayout(null);
                        error.add(startPanel);
                        //error.pack();
                        setVisible(true);
                    }
                    else {
                        oilcnt++;
                        //  System.out.println("oil add +1:  " + oilcnt);
                        totalcost += gs.getBoard().getIndResourceCost("oil", oil - oilcnt + 1);
                        updateText("oil");
                    }
                }
            }

            if(garbage <= 6)
            {
                if((x >= gX2.get(0) && x <= (gX2.get(gX2.size() -1) + 50)) && (y >= 670 && y <= 720))
                {
                    if(gcnt + 1 > garbage)
                    {
                        JFrame error = new JFrame("Error");
                        error.setSize(800, 400);
                        error.setDefaultCloseOperation(3);
                        error.getContentPane().setBackground(Color.WHITE);
                        error.setVisible(true);
                        JPanel startPanel = new JPanel() {
                            public void paintComponent(Graphics g) {
                                g.setFont(new Font("verdana", Font.BOLD, 20));
                                g.drawString("Resource Not Available. Click Anywhere to Continue", 200, 200);
                                this.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseReleased(MouseEvent d) {
                                        error.dispose();
                                    }
                                });
                            }

                        };
                        startPanel.setPreferredSize(new Dimension(1920, 1080));
                        startPanel.setLayout(null);
                        error.add(startPanel);
                        //error.pack();
                        setVisible(true);
                    }
                    else {
                        gcnt++;
                        // System.out.println("g add +1:  " + gcnt);
                        totalcost += gs.getBoard().getIndResourceCost("garbage", garbage - gcnt + 1);
                        updateText("garbage");
                    }
                }
            }
            else
            {
                if(((x >= gX.get(0) && x <= (gX.get(gX.size() -1) +50)) && (y >= 350 && y <= 400)) || ((x >= gX2.get(0) && x <= (gX2.get(gX2.size() -1) + 50)) && (y >= 670 && y <= 720)))
                {
                    if(gcnt + 1 > garbage)
                    {
                        JFrame error = new JFrame("Error");
                        error.setSize(800, 400);
                        error.setDefaultCloseOperation(3);
                        error.getContentPane().setBackground(Color.WHITE);
                        error.setVisible(true);
                        JPanel startPanel = new JPanel() {
                            public void paintComponent(Graphics g) {
                                g.setFont(new Font("verdana", Font.BOLD, 20));
                                g.drawString("Resource Not Available. Click Anywhere to Continue", 200, 200);
                                this.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseReleased(MouseEvent d) {
                                        error.dispose();
                                    }
                                });
                            }

                        };
                        startPanel.setPreferredSize(new Dimension(1920, 1080));
                        startPanel.setLayout(null);
                        error.add(startPanel);
                        //error.pack();
                        setVisible(true);
                    }
                    else {
                        gcnt++;
                        //  System.out.println("g add +1:  " + gcnt);
                        totalcost += gs.getBoard().getIndResourceCost("garbage", garbage - gcnt + 1);
                        updateText("garbage");
                    }
                }
            }

            if(uranium <= 6)
            {
                if((x >= uX2.get(0) && x <= (uX2.get(uX2.size() -1) + 50)) && (y >= 730 && y <= 780))
                {
                    if(ucnt + 1 > uranium)
                    {
                        JFrame error = new JFrame("Error");
                        error.setSize(800, 400);
                        error.setDefaultCloseOperation(3);
                        error.getContentPane().setBackground(Color.WHITE);
                        error.setVisible(true);
                        JPanel startPanel = new JPanel() {
                            public void paintComponent(Graphics g) {
                                g.setFont(new Font("verdana", Font.BOLD, 20));
                                g.drawString("Resource Not Available. Click Anywhere to Continue", 200, 200);
                                this.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseReleased(MouseEvent d) {
                                        error.dispose();
                                    }
                                });
                            }

                        };
                        startPanel.setPreferredSize(new Dimension(1920, 1080));
                        startPanel.setLayout(null);
                        error.add(startPanel);
                        //error.pack();
                        setVisible(true);
                    }
                    else {
                        ucnt++;
                        // System.out.println("u add +1:  " + ucnt);
                        totalcost += gs.getBoard().getIndResourceCost("uranium", uranium - ucnt + 1);
                        updateText("uranium");
                    }
                }
            }
            else
            {
                if(((x >= uX.get(0) && x <= (uX.get(uX.size() -1) +50)) && (y >= 410 && y <= 460)) || ((x >= uX2.get(0) && x <= (uX2.get(uX2.size() -1) + 50)) && (y >= 730 && y <= 780)))
                {
                    if(ucnt + 1 > uranium)
                    {
                        JFrame error = new JFrame("Error");
                        error.setSize(800, 400);
                        error.setDefaultCloseOperation(3);
                        error.getContentPane().setBackground(Color.WHITE);
                        error.setVisible(true);
                        JPanel startPanel = new JPanel() {
                            public void paintComponent(Graphics g) {
                                g.setFont(new Font("verdana", Font.BOLD, 20));
                                g.drawString("Resource Not Available. Click Anywhere to Continue", 200, 200);
                                this.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseReleased(MouseEvent d) {
                                        error.dispose();
                                    }
                                });
                            }

                        };
                        startPanel.setPreferredSize(new Dimension(1920, 1080));
                        startPanel.setLayout(null);
                        error.add(startPanel);
                        //error.pack();
                        setVisible(true);
                    }
                    else {
                        ucnt++;
                        // System.out.println("u add +1:  " + ucnt);
                        totalcost += gs.getBoard().getIndResourceCost("uranium", uranium - ucnt + 1);
                        updateText("uranium");
                    }
                }


            }
            if((x >= 1600 && x <= 1685) && (y >= 1015 && y <= 1042))
            {
                LinkedHashMap<String, Integer> buy = new LinkedHashMap<>();
                buy.put("coal", coalcnt);
                buy.put("oil", oilcnt);
                buy.put("garbage", gcnt);
                buy.put("uranium", ucnt);
                boolean b = gs.getBoard().buyResources(buy, currentRPlayer);
                //System.out.println(b);
                if(b)
                {
                    //System.out.println("Buy Successful");
                    if(playerCounter == 4)
                    {
                        gs.setScreenState(ScreenState.MapScreen);
                    }
                    else {
                        currentRPlayer = gs.nextPlayer();
                    }
                }
                else
                {
                    JFrame error = new JFrame("Error");
                    error.setSize(800, 400);
                    error.setDefaultCloseOperation(3);
                    error.getContentPane().setBackground(Color.GRAY);
                    error.setVisible(true);
                    JPanel startPanel = new JPanel() {
                        public void paintComponent(Graphics g) {
                            g.setFont(new Font("verdana", Font.BOLD, 20));
                            g.drawString("Not Enough Money/Not Enough Space", 200, 200);
                            g.drawString("Click Anywhere to Continue", 200, 300);
                            this.updateUI();
                            this.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseReleased(MouseEvent d) {
                                    error.dispose();
                                }
                            });
                        }

                    };
                    startPanel.setPreferredSize(new Dimension(1920, 1080));
                    startPanel.setLayout(null);
                    startPanel.setBackground(Color.GRAY);
                    error.add(startPanel);
                    //error.pack();
                    setVisible(true);
                    coalcnt = 0;
                    oilcnt = 0;
                    ucnt = 0;
                    gcnt = 0;
                    totalcost = 0;
                    updateText("coal");
                    updateText("oil");
                    updateText("garbage");
                    updateText("uranium");
                    //currentRPlayer = gs.nextPlayer();
                }
            }
            if((x >= 1485 && x < 1570) && (y >= 1010 && y <= 1045))
            {
                if(playerCounter == 4)
                {
                    p.remove(c);
                    p.remove(o);
                    p.remove(g);
                    p.remove(u);
                    p.remove(cost);
                    gs.setScreenState(ScreenState.MapScreen);
                }
                else {
                    currentRPlayer = gs.nextPlayer();
                }
            }
            p.repaint();


        }
        if (gs.getScreenState() == ScreenState.MapScreen)
        {
            for (int z = 0; z < gs.getBoard().getMap().getCityNames().size(); z++)
            {
                if (x > 1812+(33*z) && y > 89+(25*z) && x < 1825+(33*z) && y < 102+(25*z))
                {
                    boolean temp = gs.getBoard().buyCity(gs.getBoard().getMap().getCityNames().get(z), gs.getCurrentPlayer());
                    if (temp)
                    {
                        gs.setScreenState(ScreenState.PowerPlantChoice);
                        repaint();
                    }
                    else
                    {
                        repaint();
                    }
                }
            }
        }
    }



    @Override
    public void mouseEntered(MouseEvent e)
    {


    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static void main(String[] args) throws IOException {
        GUI g = new GUI();
    }
}