import java.util.*;

class Main {
  public static void main(String[] args) {
    deck d = new deck();
    Scanner na = new Scanner(System.in);
    System.out.println("What's your name?");
    String name = na.next();
    Scanner totalMoney = new Scanner(System.in);
    System.out.println("How much money are you playing with today?");
    int tMoney = totalMoney.nextInt();
    person p = new person(name, tMoney,0);
    Scanner betting = new Scanner(System.in);
    
    while (p.getMoney()>0)
    {
    d.shuffle();
    System.out.println("");
    System.out.println("How much do you wager?");
    int wager = betting.nextInt();
    while (wager>p.getMoney()||wager<=0){
      if (wager>p.getMoney())
      {System.out.println("Enter less than total amount: "+p.getMoney());}
      else 
      {System.out.println("Enter positive value");}
      wager = betting.nextInt();
    }
    
    int cardNum = 0; //index in deck of current card about to be drawn, but NOT YET drawn.
    int dealerTotal =0;
    int playerTotal =0;
    card[] myDeck = new card[2];
    for (int i =0; i<myDeck.length;i++)
    {
      myDeck[i] = new card(d.getDeckAt(i),d.getDeckNameAt(i),d.getDeckSuitAt(i));
      playerTotal += d.getDeckAt(i);
      cardNum++;
    }
    card[] dealerDeck = new card[2];
    for (int i =0; i<dealerDeck.length;i++)
    {
      dealerDeck[i] = new card(d.getDeckAt(i+cardNum),d.getDeckNameAt(i+cardNum),d.getDeckSuitAt(i+cardNum));
      dealerTotal += d.getDeckAt(i+cardNum);
      cardNum++;
    }


    while (playerTotal<=21)
    {
      System.out.println("");
      System.out.println(Arrays.toString(myDeck));
      System.out.println("Dealer Card Showing:"+dealerDeck[0]);
      Scanner yn = new Scanner(System.in);
      System.out.println("Do you want to hit?");
      if ((yn.next()).equals("yes"))
      {
        myDeck = updateDeck(myDeck,1,cardNum,d);
        cardNum++;
        playerTotal =calcTotal(myDeck);
        if (playerTotal>=21)
        {break;}
      }
      else
      {break;}
    }

    System.out.println("");
    System.out.println(Arrays.toString(myDeck));
    System.out.println("Dealer Card Showing:"+dealerDeck[0]);
    if (calcTotal(myDeck)>21)
    {dwin(name,tMoney,wager,p);}
    else if (calcTotal(myDeck)==21)
    {pwin(name,tMoney,wager,p);}
    else {
    while (dealerTotal<17)
    {
      dealerTotal = calcTotal(dealerDeck);
      if (dealerTotal <17)
      {
        dealerDeck = updateDeck(dealerDeck,1,cardNum,d);
        cardNum++;
        dealerTotal = calcTotal(dealerDeck);
        if (dealerTotal>=21)
        {break;}
      }
      else
      {break;}
    }
    System.out.println("");
    System.out.println("Player Deck:"+Arrays.toString(myDeck));
    System.out.println("Dealer Deck:"+Arrays.toString(dealerDeck));
    if (dealerTotal == 21)
    {dwin(name,tMoney,wager,p);}
    else if (dealerTotal > 21)
    {pwin(name,tMoney,wager,p);}
    else if (dealerTotal == playerTotal)
    {System.out.println("Tied Game");}
    else if (dealerTotal > playerTotal)
    {dwin(name,tMoney,wager,p);}
    else {pwin(name,tMoney,wager,p);}
    }
    }
    System.out.println("You ran out of money!");
  }

  public static card[] updateDeck(card[] c,int a,int atCard,deck d) 
  {
    card[] c2 = new card[c.length+a];
    for (int i =0; i<c.length;i++)
    {c2[i]= new card(c[i].getNum(),c[i].getName(),c[i].getSuit());}//first puts in all the orginal card values from c
    for (int i =c.length;i<c.length+a;i++ )
    {c2[i]=new card(d.getDeckAt(atCard+i-c.length),d.getDeckNameAt(atCard+i-c.length),d.getDeckSuitAt(atCard+i-c.length));}
    //then add the rest from the deck
    return c2;
  }

  public static int calcTotal(card[] c)
  { int t =0;
    for (int i =0; i < c.length; i++)
    {t+=c[i].getNum();}
    return t;
  }

  public static void pwin(String name, int total, int wager, person p)
  {
    System.out.println(name + " wins!");
    p.changeMoney(wager);
    System.out.println("You now have $" + p.getMoney());
  }

  public static void dwin(String name, int total, int wager, person p)
  {
    System.out.println("Dealer wins!");
    p.changeMoney(-wager);
    System.out.println("You now have $" + p.getMoney());
  }
}

class person{
  private String name;
  private int money;
  private int gamesWon;

  public person(String s, int a, int b){
    name = s;
    money = a;
    gamesWon = b;
  }

  public void gameWon(){
    gamesWon++;
  }
  public void changeMoney(int a)
  {
    money += a;
  }
  public int getMoney()
  {return money;}
}

class card{
  private int num;
  private String suit;
  private String name;

  public card(int i,String na,String s){
    num = i;
    name = na;
    suit = s;
    if (num>10)
    {num =10;}
  }

  public void setNum(int i)
  {num =i;}

  public int getNum()
  {return num;}

  public void setSuit(String s)
  {suit =s;  }

  public void setName(String s)
  {name = s;}

  public String getName()
  {return name;}

  public String getSuit()
  {return suit;}

  public String toString()
  {return name + suit;}
}

class deck{
  private card[] d = new card[52];

  public deck(){
    String[] cards = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
    String[] suits = {"C","H","S","D"};
    for (int i=0; i <4;i++)
    {
      for (int v =0; v < 13; v++)
      {
        d[i*13+v] = new card(v+1,cards[v],suits[i]);
      }
    }
  }

  public void shuffle()
  { 
    int[] p = new int[52];
    for(int i =0; i<52;i++)
    {p[i]=i;}
    for (int i =0; i<52;i++)
    {
      int r = (int)(52*Math.random());
      int t = (int)(52*Math.random());
      p[r]=t;
      p[t]=r;
    }
    card[] f = new card[52];
    for (int i =0; i<52;i++)
    {
      f[i] = new card(d[p[i]].getNum(),d[p[i]].getName(),d[p[i]].getSuit());
    } 
    d=f;
  }

  public String toStringDeck()
  {return Arrays.toString(d);}

  public int getDeckAt(int a)
  {return d[a].getNum();}

  public String getDeckNameAt(int a)
  {return d[a].getName();}

  public String getDeckSuitAt(int a)
  {return d[a].getSuit();}
}
