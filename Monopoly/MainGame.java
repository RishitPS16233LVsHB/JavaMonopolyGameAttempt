// date created 1/11/22
// last date modified 6/11/22

import java.util.*;

public class MainGame {
    public static void main(String[] args)
    {
        try{
            GameScreen MainGameScreen = new GameScreen(160,160);
            Game game = new Game(MainGameScreen);

            game.AskForAllFourPlayerNames();
            while(!game.HasGameEnded())
            {
                game.RollDice();
                game.InputChoices();
                MainGameScreen.ClearScreen();
            }            
        }
        catch(Exception error){
            System.out.println(error.getMessage());
            System.out.println(error.getStackTrace());
        }
    }

    
}

class Point{
    public int x,y;
    public Point(int X,int Y)
    {
        x = X;
        y = Y;        
    }
    public Point()
    {
        x = -1;
        y = -1;
    }
}


class Game
{
    private GameScreen screen;
    private int Dice;
    private Player[] Players;
    private int CurrentPlayerIndex;


    private String[] PlayerSymbols = 
    {
        "<---------<<",
        "--------|==0",
        "|||=========",
        "<====[$]====>"
    };



    private Location[] LowerLocations = 
    {
        new PurchasableLocation("Crow's perch","Red",2000,1000,300,500,600,700),
        new PurchasableLocation("Devil's pit","Red",2000,1000,300,500,600,700),
        new LuckLocation(),
        new PurchasableLocation("Isolated hut","Red",2000,1000,300,500,600,700),
        new PurchasableLocation("Crippled Kate's","Red",2000,1000,300,500,600,700),
    };
    private Location[] LeftLocations = 
    {
        new PurchasableLocation("Crow's perch","Red",2000,1000,300,500,600,700),
        new PurchasableLocation("Devil's pit","Red",2000,1000,300,500,600,700),
        new MoneyLuckLocation(),
        new GovernmentLocation("Airport","gray",200000,10000,8000),
        new PurchasableLocation("Crippled Kate's","Red",2000,1000,300,500,600,700),
    };
    private Location[] UpperLocations = 
    {
        new PurchasableLocation("Crow's perch","Red",2000,1000,300,500,600,700),
        new PurchasableLocation("Devil's pit","Red",2000,1000,300,500,600,700),
        new MoneyLuckLocation(),
        new PurchasableLocation("Isolated hut","Red",2000,1000,300,500,600,700),
        new PurchasableLocation("Crippled Kate's","Red",2000,1000,300,500,600,700),
    };
    private Location[] RightLocations = 
    {
        new PurchasableLocation("Crow's perch","Red",2000,1000,300,500,600,700),
        new LuckLocation(),
        new GovernmentLocation("Railway Station","gray",100000,8000,10000),
        new PurchasableLocation("Isolated hut","Red",2000,1000,300,500,600,700),
        new PurchasableLocation("Crippled Kate's","Red",2000,1000,300,500,600,700),
    };

    private Point[] UpperPoints = {
        new Point(1,23),
        new Point(1,23+21),
        new Point(1,23+(21*2)),
        new Point(1,23+(21*3)),
        new Point(1,23+(21*4))
    };
    private Point[] LowerPoints = {
        new Point(128,23),
        new Point(128,23+21),
        new Point(128,23+(21*2)),
        new Point(128,23+(21*3)),
        new Point(128,23+(21*4))
    };

    private Point[] LeftPoints = {
        new Point(23,1),
        new Point(23+21,1),
        new Point(23+(21*2),1),            
        new Point(23+(21*3),1),            
        new Point(23+(21*4),1),
    };
    private Point[] RightPoints = {
        new Point(23,128),
        new Point(23+21,128),
        new Point(23+(21*2),128),            
        new Point(23+(21*3),128),            
        new Point(23+(21*4),128),
    };

    private Point[] CornerPoints = {
        new Point(128,128),
        new Point(1,1),
        new Point(1,128),
        new Point(128,1)
    };

    private Point[] PlayerLandingCoordinates = 
    {
        CornerPoints[0],
        LowerPoints[4],
        LowerPoints[3],
        LowerPoints[2],
        LowerPoints[1],
        LowerPoints[0],

        CornerPoints[3],
        LeftPoints[4],
        LeftPoints[3],
        LeftPoints[2],
        LeftPoints[1],
        LeftPoints[0],

        CornerPoints[1],
        UpperPoints[0],
        UpperPoints[1],
        UpperPoints[2],
        UpperPoints[3],
        UpperPoints[4],
        
        CornerPoints[2],
        RightPoints[0],
        RightPoints[1],
        RightPoints[2],
        RightPoints[3],
        RightPoints[4],
    };

    private Location[] PlayerLocations = 
    {
        new Start(),
        LowerLocations[4],
        LowerLocations[3],
        LowerLocations[2],
        LowerLocations[1],
        LowerLocations[0],

        new Jail(),
        LeftLocations[4],
        LeftLocations[3],
        LeftLocations[2],
        LeftLocations[1],
        LeftLocations[0],
        
        new Club(),
        UpperLocations[0],
        UpperLocations[1],
        UpperLocations[2],
        UpperLocations[3],
        UpperLocations[4],

        new Club(),
        RightLocations[0],
        RightLocations[1],
        RightLocations[2],
        RightLocations[3],
        RightLocations[4]        
    };

    private boolean hasEnded;

    public Game(GameScreen gamescreen)
    {
        screen = gamescreen;
        hasEnded = false;
        Players = new Player[4];
        Dice = 0;
        CurrentPlayerIndex = 0;
    }

    public void ImprintCurrentBoardOnScreen()
    {
        DrawBoardBorders();
        DisplayLocationDetails();
        PrintCorners();
        DisplayPlayersAtThereMapLocationIndex();
    }

   

    public void AskForAllFourPlayerNames()
    {
        System.out.println("Greetings to the Game of monopoly made by Rishit Selia.....");
        System.out.println("Enter all four players names for game to begin......");
        
        Scanner input = new Scanner(System.in);
        String Name;

        for(int i=0;i<4;i++)
        {
            System.out.println("Enter Player" + (i+1) + "'s name:- ");
            Name = input.nextLine();
            Players[i] = new Player(Name);
        }
        System.out.println("Good job now let us begin the game....");        
    }

    public void InputChoices()
    {
        try
        {
            ImprintCurrentBoardOnScreen();
            screen.ShowScreen(true);
            System.out.println("==========================================================================");
            System.out.println(" The Dice has rolled into number:- " + Dice);
            System.out.println("==========================================================================");
            System.out.println(" It is \""+ Players[CurrentPlayerIndex].GetPlayerName() +"\"'s Turn.....");
            System.out.println("==========================================================================");

            Scanner input = new Scanner(System.in);
            System.out.println("To end game press '9'.....");
            System.out.println("To buy property on which the Current Player is standing press '1'.....");
            System.out.println("To Sell property on which the Current Player is standing press '2'.....");
            System.out.println("To upgrade the property by building a hotel press '3'.....");
            System.out.println("To pass go this roll press any key.....");
            System.out.println("Enter your choice.....");
            int in = input.nextInt();

            if(in == 1)
                BuyProperty(PlayerLocations[Players[CurrentPlayerIndex].GetMapLocationIndex()],Players[CurrentPlayerIndex]);
            else if(in == 2)
                SellProperty(PlayerLocations[Players[CurrentPlayerIndex].GetMapLocationIndex()],Players[CurrentPlayerIndex]);
            else if(in == 3)
                UpgradeProperty(PlayerLocations[Players[CurrentPlayerIndex].GetMapLocationIndex()],Players[CurrentPlayerIndex]);
            else if(in == 9)
                hasEnded = true;

            CurrentPlayerIndex++;
            CurrentPlayerIndex %= 4;        
        }
        catch(Exception error){
            System.out.println(error.getMessage());
            System.out.println(error.getStackTrace());
        }
    }


    private void BuyProperty(Location l,Player p)
    {
        if(l != null && p != null)
        {
            if(l instanceof PurchasableLocation)
            {                
                PurchasableLocation pl = (PurchasableLocation)l;
                if(pl.GetLocationOwner() != "")
                    return;
                int TotalDeductionAmount = pl.GetLocationPrice();
                if(pl.GetHasLocationHotel())
                    TotalDeductionAmount += pl.GetLocationHotelPrice();
                p.SetBankBalance(-TotalDeductionAmount);
                p.AddProperty(pl,pl.GetLocationName());
                pl.PurchaseLocation(p.GetPlayerName());
                screen.HorizontalText(158,5,p.GetPlayerName() + " has Spend Price amount :- " + TotalDeductionAmount + " by purchasing " + pl.GetLocationName());
            }
            else if(l instanceof GovernmentLocation)
            {
                GovernmentLocation gl = (GovernmentLocation)l;
                if(gl.GetLocationOwner() != "")
                    return;
                int TotalDeductionAmount = gl.GetLocationPrice();
                p.SetBankBalance(-TotalDeductionAmount);
                p.AddProperty(gl,gl.GetLocationName());
                gl.PurchaseLocation(p.GetPlayerName());
                screen.HorizontalText(158,8,p.GetPlayerName() + " has Spend Price amount :- " + TotalDeductionAmount + " by purchasing " + gl.GetLocationName());
            }
        }
    }

    private void UpgradeProperty(Location l,Player p)
    {
        if(l != null && p != null)
        {
            if(l instanceof PurchasableLocation)
            {
                PurchasableLocation pl = (PurchasableLocation)l;
                int TotalDeductionAmount = pl.GetLocationPrice();
                if(!pl.GetHasLocationHotel())
                {
                    TotalDeductionAmount = pl.GetLocationHotelPrice();                    
                    p.SetBankBalance(-TotalDeductionAmount);
                    pl.PurchaseHotel();
                    screen.HorizontalText(158,5,p.GetPlayerName() + " has spend upgrade amount :- " + TotalDeductionAmount + " from upgrading " + pl.GetLocationName());
                }
            }
        }
    }

    private void SellProperty(Location l,Player p)
    {
        if(l != null && p != null)
        {
            if(l instanceof PurchasableLocation)
            {
                PurchasableLocation pl = (PurchasableLocation)l;
                if(pl.GetLocationOwner() == "")
                    return;
                int TotalAdditionAmount = pl.GetLocationPrice();
                if(pl.GetHasLocationHotel())
                    TotalAdditionAmount += pl.GetLocationHotelPrice();
                p.SetBankBalance(TotalAdditionAmount);
                p.RemoveProperty(pl.GetLocationName());
                pl.SellLocation();
                screen.HorizontalText(158,5,p.GetPlayerName() + " has Recieved Selling Price amount :- " + TotalAdditionAmount + " from selling " + pl.GetLocationName());
            }
            else if(l instanceof GovernmentLocation)
            {
                GovernmentLocation gl = (GovernmentLocation)l;
                if(gl.GetLocationOwner() == "")
                    return;
                int TotalAdditionAmount = gl.GetLocationPrice();
                p.SetBankBalance(TotalAdditionAmount);
                p.RemoveProperty(gl.GetLocationName());
                gl.SellLocation();
                screen.HorizontalText(158,5,p.GetPlayerName() + " has Recieved Selling Price amount :- " + TotalAdditionAmount + " from selling " + gl.GetLocationName());
            }
        }
    }

    private void AddRevenuesFromLocations(Player p)
    {
        if(p != null)
        {
            int TotalRevenue = 0;
            for(Map.Entry<String,Location> MapEntry:p.GetLocationsOwned().entrySet())
            {
                if(MapEntry.getValue() instanceof PurchasableLocation)
                {
                    PurchasableLocation pl = (PurchasableLocation)MapEntry.getValue();
                    TotalRevenue += pl.GetSightSeeingRevenue();
                    if(pl.GetHasLocationHotel())
                        TotalRevenue += pl.GetHotelRevenue();
                }
                else if(MapEntry.getValue() instanceof GovernmentLocation)
                {
                    GovernmentLocation gl = (GovernmentLocation)MapEntry.getValue();
                    TotalRevenue += gl.GetRevenue();                    
                }
            }
            if(TotalRevenue >= 100)
                screen.HorizontalText(157,5,p.GetPlayerName() + " has Recieved Revenues from his/her businesses amounting " + TotalRevenue + " Dollars");
            p.SetBankBalance(TotalRevenue);
        }
    }

    private boolean IsThisPlayerOnStartAgain(Player p)
    {
        if(PlayerLocations[p.GetMapLocationIndex()] instanceof Start)
            return true;
        return false;
    }

    private void CheckDebts()
    {
        if(Players[CurrentPlayerIndex].GetHasBankrupted())
            return;

        if(Players[CurrentPlayerIndex].GetBankBalance() < 0)
            Players[CurrentPlayerIndex].ChangeDebtCount(1);
        else
            Players[CurrentPlayerIndex].SetDebtCount(0);
        
        if(Players[CurrentPlayerIndex].GetDebtCount() > 3)
        {
            screen.HorizontalText(154,5,Players[CurrentPlayerIndex].GetPlayerName() + " is out of business...... farewell " + Players[CurrentPlayerIndex].GetPlayerName() + " you played well....");
            Players[CurrentPlayerIndex].BankruptPlayer();
        }
    }

    private void FinePlayer(Player p,Location l)
    {
        if(l != null && p != null)
        {
            if(l instanceof PurchasableLocation)
            {
                PurchasableLocation pl = (PurchasableLocation)l;

                if(pl.GetLocationOwner() == p.GetPlayerName())
                    return;

                int Fine = pl.GetLocationFine();
                if(pl.GetHasLocationHotel())
                    Fine = pl.GetLocationHotelFine();

                if(pl.GetLocationOwner() != "" && pl.GetLocationOwner() != p.GetPlayerName())
                {
                    p.SetBankBalance(-Fine);
                    screen.HorizontalText(155,5,p.GetPlayerName() + " has been fined:- " + Fine + " Amount");
                    for (Player p1 : Players) 
                    {
                        if(p1.GetPlayerName() == pl.GetLocationOwner())
                        {
                            p1.SetBankBalance(Fine);
                            screen.HorizontalText(156,5,p1.GetPlayerName() + " recieved fine amounting " + Fine + " Dollars");
                            break;
                        }
                    }
                }
            }
            else if(l instanceof GovernmentLocation)
            {
                GovernmentLocation gl = (GovernmentLocation)l;
                int Fine = gl.GetLocationFine();
                if(gl.GetLocationOwner() != "" &&  gl.GetLocationOwner() != p.GetPlayerName())
                {
                    if(gl.GetLocationOwner() == p.GetPlayerName())
                    return;

                    p.SetBankBalance(-Fine);
                    screen.HorizontalText(155,5,p.GetPlayerName() + " has been fined:- " + Fine + " Amount");
                    for (Player p1 : Players) 
                    {
                        if(p1.GetPlayerName() == gl.GetLocationName())
                        {
                            screen.HorizontalText(156,5,p1.GetPlayerName() + " recieved fine amounting " + Fine + " Dollars");
                            p1.SetBankBalance(Fine);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void RollDice()
    {
        Random r = new Random();
        Dice = 1 + r.nextInt(6);

        if(!Players[CurrentPlayerIndex].GetHasBankrupted())
        {
            if(!Players[CurrentPlayerIndex].GetHasJailed())            
            {
                Players[CurrentPlayerIndex].ChangeMapLocation(Dice);
                FinePlayer(Players[CurrentPlayerIndex],PlayerLocations[Players[CurrentPlayerIndex].GetMapLocationIndex()]);
                IsThisPlayerOnLuckLocations(Players[CurrentPlayerIndex]);
                PrintLocationDetailsOnCenter(PlayerLocations[Players[CurrentPlayerIndex].GetMapLocationIndex()]);
                if(IsThisPlayerOnStartAgain(Players[CurrentPlayerIndex]))
                {
                    CheckDebts();
                    AddRevenuesFromLocations(Players[CurrentPlayerIndex]);
                }
            }
        }
        HasGameBeenWon();
    }

    private void HasGameBeenWon()
    {
        LinkedList<Player> l = new LinkedList<Player>();

        for(Player p: Players)
            if(!p.GetHasBankrupted())
                l.push(p);
        
        if(l.size() == 1)
        {
            System.out.println(l.get(0).GetPlayerName() + " has won the game");
            hasEnded = true;
        }
    }





    private void DisplayLocationDetails()
    {
        for(int i=0;i<5;i++)
            PrintLocationDetails(UpperLocations[i],i,UpperPoints);
        for(int i=0;i<5;i++)
            PrintLocationDetails(LowerLocations[i],i,LowerPoints);
        for(int i=0;i<5;i++)
            PrintLocationDetails(LeftLocations[i],i,LeftPoints);
        for(int i=0;i<5;i++)
            PrintLocationDetails(RightLocations[i],i,RightPoints);
    }

    private void PrintCorners()
    {
        screen.HorizontalText(CornerPoints[0].x+5,CornerPoints[0].y+5,"Start");
        screen.HorizontalText(CornerPoints[0].x+6,CornerPoints[0].y+5,"<------");

        screen.HorizontalText(CornerPoints[1].x+5,CornerPoints[1].y+5,"Club!");
        screen.HorizontalText(CornerPoints[1].x+6,CornerPoints[1].y+5,"=====");
        
        screen.HorizontalText(CornerPoints[2].x+5,CornerPoints[2].y+5,"Club!");
        screen.HorizontalText(CornerPoints[2].x+6,CornerPoints[2].y+5,"=====");

        screen.HorizontalText(CornerPoints[3].x+5,CornerPoints[3].y+5,"Jail!");
        screen.HorizontalText(CornerPoints[3].x+6,CornerPoints[3].y+5,"=======");
        screen.HorizontalText(CornerPoints[3].x+7,CornerPoints[3].y+5,"| | | |");
        screen.HorizontalText(CornerPoints[3].x+8,CornerPoints[3].y+5,"=======");
    }

    private void DisplayPlayersAtThereMapLocationIndex()
    {
        if(Players != null)
        {
            for(int i=0;i<Players.length;i++)
            {
                if(Players[i] != null && !Players[i].GetHasBankrupted())
                {
                    Point p = PlayerLandingCoordinates[Players[i].GetMapLocationIndex()];
                    screen.HorizontalText(p.x+11+(i*2),p.y,PlayerSymbols[i]);
                    screen.HorizontalText((p.x+11+(i*2))+1,p.y,Players[i].GetPlayerName() + " (" + Players[i].GetBankBalance() +")" );
                }
            }
        }
    }

    private void PrintLocationDetails(Location l,int Index,Point[] points)
    {
        if(l != null)
        {
            if(l instanceof PurchasableLocation)
            {
                Point point = points[Index];
                PurchasableLocation p = (PurchasableLocation)l;

                screen.HorizontalText(point.x + 1,point.y,p.GetLocationName());
                screen.HorizontalText(point.x + 2,point.y,"(" + p.GetLocationPrice() + ")");
                screen.HorizontalText(point.x + 3,point.y,"Hotel price:- " + p.GetLocationHotelPrice());

                screen.HorizontalText(point.x + 6,point.y,"Fine:- " + p.GetLocationFine());
                screen.HorizontalText(point.x + 7,point.y,"Hotel:- " + p.GetLocationHotelFine());
                screen.HorizontalText(point.x + 8,point.y,"Color:- " + p.GetLocationColor());
            }
            else if(l instanceof GovernmentLocation)
            {
                Point point = points[Index];
                GovernmentLocation g = (GovernmentLocation)l;

                screen.HorizontalText(point.x+1,point.y,g.GetLocationName());
                screen.HorizontalText(point.x+2,point.y,"(" + g.GetLocationPrice() + ")");

                screen.HorizontalText(point.x + 6,point.y,"Fine:- " + g.GetLocationFine());
                screen.HorizontalText(point.x + 7,point.y,"Color:- " + g.GetLocationColor());
            }
            else if(l instanceof MoneyLuckLocation)
            {
                Point point = points[Index];
                screen.HorizontalText(point.x+1,point.y+1,"Your Money Luck");
            }
            else if(l instanceof LuckLocation)
            {
                Point point = points[Index];                
                screen.HorizontalText(point.x+1,point.y+1,"Your Luck!");                
            }
        }
    }

    private void PrintLocationDetailsOnCenter(Location l)
    {
        if(l != null)
        {
            if(l instanceof PurchasableLocation)
            {
                PurchasableLocation p = (PurchasableLocation)l;

                screen.HorizontalText(60,60,p.GetLocationName());
                screen.HorizontalText(60+1,60,"(" + p.GetLocationPrice() + ")");
                screen.HorizontalText(60+2,60,"Hotel price:- " + p.GetLocationHotelPrice());
                screen.HorizontalText(60+3,60,"Fine:- " + p.GetLocationFine());
                screen.HorizontalText(60+4,60,"Hotel:- " + p.GetLocationHotelFine());
                screen.HorizontalText(60+5,60,"Color:- " + p.GetLocationColor());
                screen.HorizontalText(60+6,60,"Revenue:- " + p.GetSightSeeingRevenue());
                screen.HorizontalText(60+7,60,"Hotel Revenue:- " + p.GetHotelRevenue());
                screen.HorizontalText(60+8,60,"Owner:- " + p.GetLocationOwner());
                screen.HorizontalText(60+9,60,"Owner:- " + p.GetHasLocationHotel());
                if(p.GetHasLocationHotel())
                    screen.HorizontalText(60+10,60,"Selling Price:- " + (p.GetLocationHotelPrice()+p.GetLocationPrice()));
                else
                    screen.HorizontalText(60+10,60,"Selling Price:- " + p.GetLocationPrice());
            }
            else if(l instanceof GovernmentLocation)
            {
                GovernmentLocation g = (GovernmentLocation)l;

                screen.HorizontalText(60,60,g.GetLocationName());
                screen.HorizontalText(60+1,60,"(" + g.GetLocationPrice() + ")");
                screen.HorizontalText(60+2,60,"Fine:- " + g.GetLocationFine());
                screen.HorizontalText(60+3,60,"Color:- " + g.GetLocationColor());
                screen.HorizontalText(60+4,60,"Owner:- " + g.GetLocationOwner());
                screen.HorizontalText(60+5,60,"Revenue:- " + g.GetRevenue());
                screen.HorizontalText(60+6,60,"Selling Price:- " + g.GetLocationPrice());
            }
            else if(l instanceof MoneyLuckLocation)
            {
                screen.HorizontalText(60,60,"Your Money Luck");
                screen.HorizontalText(60+1,60,"1: Hospital bills $50 payment...");
                screen.HorizontalText(60+2,60,"2: You $70 won Lottery.....");
                screen.HorizontalText(60+3,60,"3: School fees $100 payment");
                screen.HorizontalText(60+4,60,"4: Its your birthday collect 10 dollars from each player");
                screen.HorizontalText(60+5,60,"5: Legal penalties pay up $150......");
                screen.HorizontalText(60+6,60,"6: Your Debt is paid by an unknown individual");
            }
            else if(l instanceof LuckLocation)
            {
                screen.HorizontalText(60,60,"Your Luck");
                screen.HorizontalText(60+1,60,"1: get some rest and go to club and pay $50...");
                screen.HorizontalText(60+2,60,"2: Arrested for Tax Evasion go to jail.....");
                screen.HorizontalText(60+3,60,"3: Go to Airport to pick up your friend....");
                screen.HorizontalText(60+4,60,"4: Go to Railways to see off your best friend....");
                screen.HorizontalText(60+5,60,"5: Recieved $100,000 from selling a warehouse of antiquities....");
                screen.HorizontalText(60+6,60,"6: your Business suffered natural calamity bank balance reduced by 50%");            
            }
            else if(l instanceof Jail)
                screen.HorizontalText(60,60,"Jailed can only move after three passes.....");
            else if(l instanceof Start)
                screen.HorizontalText(60,60,"A fresh Start for " + Players[CurrentPlayerIndex].GetPlayerName());
                
        }
    }

    private void IsThisPlayerOnLuckLocations(Player p)
    {
        Location l = PlayerLocations[p.GetMapLocationIndex()];
        if(l instanceof MoneyLuckLocation)
        {
            if(Dice == 1)
            {
                p.SetBankBalance(-50);
                screen.HorizontalText(158,5,p.GetPlayerName() + " spend 50 dollars on Hospital bills");
            }
            else if(Dice == 2)
            {
                p.SetBankBalance(70);
                screen.HorizontalText(158,5,p.GetPlayerName() + " won 70 dollars on a Lottery");            
            }
            else if(Dice == 3)
            {
                p.SetBankBalance(-100);
                screen.HorizontalText(158,5,p.GetPlayerName() + " spend 100 dollars on School fees");
            }
            else if(Dice == 4)
            {
                p.SetBankBalance(30);
                screen.HorizontalText(158,5,p.GetPlayerName() + " spend 30 dollars from each player as birthday present");
                for (Player pl : Players) {
                    if(pl != p)
                        pl.SetBankBalance(-10);
                }
            }            
            else if(Dice == 5)
            {
                p.SetBankBalance(-150);
                screen.HorizontalText(158,5,p.GetPlayerName() + " broke the law on guards's watch and payed the court 150 dollars as fine");
            }
            else if(Dice == 6)
            {
                if(p.GetBankBalance() < 0)
                {
                    p.SetBankBalance(-p.GetBankBalance());
                    screen.HorizontalText(158,5,p.GetPlayerName() + " got his/her debts all paid up...hmm shaddy");
                }
            }
        }
        else if(l instanceof LuckLocation)
        {
            if(Dice == 1)
            {
                p.SetMapLocationIndex(12);
                p.SetBankBalance(-50);
                screen.HorizontalText(158,5,p.GetPlayerName() + " spend 50 dollars on self indulgences and hookers XD");
            }
            else if(Dice == 2)
            {
                p.SetMapLocationIndex(6);
                p.JailPlayer();
                screen.HorizontalText(158,5,p.GetPlayerName() + " got jailed for doing hookers or i dont know.....");
            }
            else if(Dice == 3)
            {
                p.SetMapLocationIndex(8);
                screen.HorizontalText(158,5,p.GetPlayerName() + " went to airport to pickup friend");
            }
            else if(Dice == 4)
            {
                p.SetMapLocationIndex(17);
                screen.HorizontalText(158,5,p.GetPlayerName() + " went to railway station to see off friend");
            }
            else if(Dice == 5)
            {
                p.SetBankBalance(100000);
                screen.HorizontalText(158,5,p.GetPlayerName() + " got very lucky by selling that warehouse and earned 100000 dollars");
            }
            else if(Dice == 6)
            {
                p.SetBankBalance(-(int)((float)p.GetBankBalance()/2.0f));
                screen.HorizontalText(158,5,p.GetPlayerName() + " got very unlucky and lost half of his/her fortune in the act of god");
            }
        }
    }

    private void DrawBoardBorders()
    {
        // outer borders
        screen.PatternLine(0, 0, 147, 0, 21, '-', '+');
        screen.PatternLine(0, 0, 0, 147, 21, '|', '+');
        screen.PatternLine(0, 148, 147, 148, 21, '-', '+');
        screen.PatternLine(147, 0, 147, 147, 21, '|', '+');
        // corner points
        screen.PutChar(0,0,'+');
        screen.PutChar(147,0,'+');
        screen.PutChar(0,148,'+');
        screen.PutChar(147, 148, '+');
        // upper and vertical horizontal Location border
        screen.PatternLine(0, 22, 147, 22, 21, '-', '+');
        screen.PutChar(147,22,'+');
        screen.PatternLine(0, 127, 147, 127, 21, '-', '+');
        screen.PutChar(147,127,'+');
        // right and left vertical Location border
        screen.PatternLine(127, 0, 127, 147, 21, '|', '+');
        screen.PatternLine(22, 0, 22, 147, 21, '|', '+');
        // upper vertical Location section border
        screen.Line(43, 0, 43,21, '|');
        screen.Line(64, 0, 64,21, '|');
        screen.Line(85, 0, 85,21, '|');
        screen.Line(106, 0, 106,21, '|');
        // Lower vertical Location Section Border
        screen.Line(43, 127, 43,147, '|');
        screen.Line(64, 127, 64,147, '|');
        screen.Line(85, 127, 85,147, '|');
        screen.Line(106, 127, 106,147, '|');
        // left horizontal Location Section Border
        screen.Line(0, 43, 21,43, '-');
        screen.Line(0, 64, 21,64, '-');
        screen.Line(0, 85, 21,85, '-');
        screen.Line(0, 106, 21,106, '-');
        // right horizontal Location Section Border
        screen.Line(127, 43, 146,43, '-');
        screen.Line(127, 64, 146,64, '-');
        screen.Line(127, 85, 146,85, '-');
        screen.Line(127, 106, 146,106, '-');


        screen.Line(0, 27, 21,27, '-');
        screen.Line(0, 32, 21,32, '-');

        screen.Line(0, 48, 21,48, '-');
        screen.Line(0, 53, 21,53, '-');

        screen.Line(0, 69, 21,69, '-');
        screen.Line(0, 74, 21,74, '-');
        
        screen.Line(0, 90, 21,90, '-');
        screen.Line(0, 95, 21,95, '-');

        screen.Line(0, 111, 21,111, '-');
        screen.Line(0, 116, 21,116, '-');
        


        screen.Line(127, 27, 146,27, '-');
        screen.Line(127, 32, 146,32, '-');
        
        screen.Line(127, 48, 146,48, '-');
        screen.Line(127, 53, 146,53, '-');

        screen.Line(127, 69, 146,69, '-');
        screen.Line(127, 74, 146,74, '-');

        screen.Line(127, 90, 146,90, '-');
        screen.Line(127, 95, 146,95, '-');

        screen.Line(127, 111, 146,111, '-');
        screen.Line(127, 116, 146,116, '-');




        screen.PatternLine(21, 5, 126, 5, 21, '-', '+');
        screen.PutChar(127,5,'+');
        screen.PutChar(22,5,'+');
            
        screen.PatternLine(21, 10, 126, 10, 21, '-', '+');
        screen.PutChar(127,10,'+');
        screen.PutChar(22,10,'+');
        




        screen.PatternLine(21, 132, 126, 132, 21, '-', '+');
        screen.PutChar(127,132,'+');
        screen.PutChar(22,132,'+');

        screen.PatternLine(21, 137, 126, 137, 21, '-', '+');
        screen.PutChar(22,137,'+');
        screen.PutChar(127,137,'+');
    }

    public boolean HasGameEnded(){return hasEnded;}
}


class Location {}

class PurchasableLocation extends Location
{
    private String LocationName;
    private String LocationColor;

    private int Price;
    private int HotelPrice;

    private int SightSeeingRevenue;
    private int HotelRevenue;

    private int Fine;
    private int HotelFine;
    
    private String OwnerName;
    private boolean hasHotel;

    public PurchasableLocation(String name,String color,int price,int hotelPrice,int fine,int hotelFine,int revenue,int hotelrevenue)
    {
        LocationName = name;
        LocationColor = color;
        
        Price = price;
        HotelPrice = hotelPrice;
        
        Fine = fine;
        HotelFine = hotelFine;

        HotelRevenue = hotelrevenue;
        SightSeeingRevenue = revenue;
         
        OwnerName = "";
        hasHotel = false;
    }

    public void PurchaseHotel()
    {
        hasHotel = true;        
    }
    public void PurchaseLocation(String Name)
    {
        OwnerName = Name;
    }

    public void SellLocation()
    {
        OwnerName = "";        
    }

    public String GetLocationName(){return LocationName;}
    public String GetLocationColor(){return LocationColor;}
    public int GetLocationPrice(){return Price;}
    public int GetLocationHotelPrice(){return HotelPrice;}
    public int GetLocationFine(){return Fine;}
    public int GetLocationHotelFine(){return HotelFine;}
    public String GetLocationOwner(){return OwnerName;}
    public boolean GetHasLocationHotel(){return hasHotel;}
    public int GetSightSeeingRevenue(){return SightSeeingRevenue;}
    public int GetHotelRevenue(){return HotelRevenue;}
}

class GovernmentLocation extends Location
{
    private String LocationName;
    private String LocationColor;

    private int Price;

    private int Revenue;

    private int Fine;
    
    private String OwnerName;

    public GovernmentLocation(String name,String color,int price,int fine,int revenue)
    {
        LocationName = name;
        LocationColor = color;
        Price = price;
        Fine = fine;
        Revenue = revenue;        
        OwnerName = "";
    }

    public void PurchaseLocation(String Name)
    {
        OwnerName = Name;
    }
    public void SellLocation()
    {
        OwnerName = "";
    }


    public int GetRevenue(){return Revenue;}
    public String GetLocationName(){return LocationName;}
    public String GetLocationColor(){return LocationColor;}
    public int GetLocationPrice(){return Price;}
    public int GetLocationFine(){return Fine;}
    public String GetLocationOwner(){return OwnerName;}
}

class LuckLocation extends Location{}
class MoneyLuckLocation extends Location{}
class Jail extends Location{}
class Start extends Location{}
class Club extends Location{}


class Player 
{
    private String PlayerName;
    private int BankBalance;
    private HashMap<String,Location> OwnedLocation;
    private int MapLocationIndex;



    private boolean hasJailed;
    private boolean hasBankrupted;

    private int DebtCount;

    public int GetMapLocationIndex(){return MapLocationIndex;}
    public void SetMapLocationIndex(int i){MapLocationIndex = i;}
    public void ChangeMapLocation(int ChangeBy)
    {   
        MapLocationIndex += ChangeBy;
        MapLocationIndex %= 24;
    }

    public Player(String Name)
    {
        PlayerName = Name;
        BankBalance = 200;
        OwnedLocation = new HashMap<String,Location>();
        hasJailed = false;
        hasBankrupted = false;
        MapLocationIndex = 0;
        DebtCount = 0;
    }
    public int GetDebtCount(){return DebtCount;}
    public void SetDebtCount(int d){DebtCount = d;}
    public void ChangeDebtCount(int d){DebtCount += d;}

    public void SetBankBalance(int Amount)
    {
        BankBalance += Amount;
    }

    public void AddProperty(Location l,String Name)
    {
        if(l instanceof PurchasableLocation || l instanceof GovernmentLocation){
            if(OwnedLocation.get(Name) == null)
                OwnedLocation.put(Name, l);
            else
                System.out.println("Location already owned");
        }
        else
            System.out.println("Location cannot be bought");
    }


    public void RemoveProperty(String Name)
    {
        Location l = OwnedLocation.get(Name);
        if(l != null)
            OwnedLocation.remove(Name);
        else
            System.out.println("Location not available");
    }

    public void JailPlayer(){ hasJailed = true;}
    public void ReleasePlayer(){ hasJailed = false;}
    public void BankruptPlayer(){ hasBankrupted = true;}
    public HashMap<String,Location> GetLocationsOwned(){return OwnedLocation;}
    public int GetBankBalance(){return BankBalance;}
    public boolean GetHasJailed(){return hasJailed;}
    public boolean GetHasBankrupted(){return hasBankrupted;}
    public String GetPlayerName(){return PlayerName;}
}

class GameScreen{
    private char[][] ScreenArray;
    private int width;
    private int length; 

    public GameScreen(int w,int l)
    {
        if(w > 20 && l > 20)
        {
            ScreenArray = new char[w][l];
            width = w;
            length = l;
        }
        else
        {
            ScreenArray = new char[20][40];
            width = 20;
            length = 40;
        }

        for(int i=0;i<width;i++)
            for(int j=0;j<length;j++)
                ScreenArray[i][j] = ' ';


    }

    public void Circle(int xc,int yc,int r,char c)
    {
        float deg = 0;
        double x,y;
        while(deg <= 359.0)
        {
            x = (float)xc + (Math.cos((deg*3.14)/180) * (float)r);
            y = (float)yc + (Math.sin((deg*3.14)/180) * (float)r);
            PutChar((int)x,(int)y,c);
            deg += 1/(float)r;
        }
    }

    public void Line(int x1,int y1,int x2,int y2,char c)
    {
        int dx = x2 - x1;
        int dy = y2 - y1;

        int steps;
        if(Math.abs(dx) > Math.abs(dy))
            steps = dx;
        else
            steps = dy;
        
        float XIncrement = (float)dx/steps;
        float YIncrement = (float)dy/steps;
        int counter = 0;float x = x1,y = y1;
        while(counter < steps)
        {
            x += XIncrement;
            y += YIncrement;
            PutChar((int)x, (int)y, c);
            counter++;
        }
    }

    public void PatternLine(int x1,int y1,int x2,int y2,int lim,char c,char d)
    {
        int dx = x2 - x1;
        int dy = y2 - y1;

        int steps;
        if(Math.abs(dx) > Math.abs(dy))
            steps = dx;
        else
            steps = dy;
        
        float XIncrement = (float)dx/steps;
        float YIncrement = (float)dy/steps;
        int counter = 0;float x = x1,y = y1;
        int PatternCounter = 0;
        while(counter < steps)
        {
            x += XIncrement;
            y += YIncrement;
            if(PatternCounter >= lim)
            {
                PutChar((int)x, (int)y, d);
                PatternCounter = 0;                
            }
            else
                PutChar((int)x, (int)y, c);
            counter++;
            PatternCounter++;
        }
    }


    public void PutChar(int x,int y,char c)
    {
        try
        {
            ScreenArray[y][x] = c;
        }
        catch(Exception error){}
    }

    public void ClearScreen()
    {
        for(int i=0;i<width;i++)
            for(int j=0;j<length;j++)
                ScreenArray[i][j] = ' ';
    }

    public void HorizontalText(int x,int y,String Text)
    {
        try
        {
            if(y >= 0 && y < length)
                for(int i=0;i<Text.length();i++)
                    ScreenArray[x][y+i] = Text.charAt(i);
        }
        catch(Exception error){}
    }
    
    public void VerticalText(int x,int y,String Text)
    {
        try
        {
            if(x >= 0 && x < width)
                for(int i=0;i<Text.length();i++)
                    ScreenArray[x+i][y] = Text.charAt(i);
        }
        catch(Exception error){}
    }


    public void ShowScreen(boolean ShowBorder)
    {
        if(ShowBorder)
        {
            for(int i=0;i<length+2;i++)
                System.out.print("=");
            System.out.print("\n");
        }

        for(int i=0;i<width;i++)
        {
            if(ShowBorder)
                System.out.print("=");    

            for(int j=0;j<length;j++)
                System.out.print(ScreenArray[i][j]);
            if(ShowBorder)
                System.out.print("=");
                
            System.out.print("\n");
        }
        
        if(ShowBorder)
        {
            for(int i=0;i<length+2;i++)
                System.out.print("=");
            System.out.print("\n");
        }

    }
}
