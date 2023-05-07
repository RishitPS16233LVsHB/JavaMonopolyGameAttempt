
import java.util.*;

// public class GameObject {
    
//     public static void main(String[] args)
//     {
        
//     }

// }


class Location
{
    
}

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

    private boolean hasJailed;
    private boolean hasBankrupted;

    public Player(String Name)
    {
        PlayerName = Name;
        BankBalance = 200;
        OwnedLocation = new HashMap<String,Location>();
        hasJailed = false;
        hasBankrupted = false;
    }

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
