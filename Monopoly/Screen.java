
// public class Screen{

//     public static void main(String[] args)
//     {
//         GameScreen Screen = new GameScreen(0,0);
//         Screen.PutChar(2, 2, 'F');
//         Screen.HorizontalText(3, 1,"Hello Hetvi");
//         Screen.VerticalText(3,1, "Hello Hetvi");
//         Screen.ShowScreen(true);
//         Screen.ClearScreen();
//         Screen.Circle(10,10,10,'0');
//         Screen.ShowScreen(true);
//     }

// }


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

        System.out.println(steps);
        System.out.println(XIncrement);
        System.out.println(YIncrement);

        int counter = 0;float x = x1,y = y1;
        while(counter < steps)
        {
            x += XIncrement;
            y += YIncrement;
            PutChar((int)x, (int)y, c);
            counter++;
        }
    }

    public void PutChar(int x,int y,char c)
    {
        try
        {
            ScreenArray[x][y] = c;
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