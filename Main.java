import java.awt.*; 
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Random;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends JFrame {
//initial all the buttons,labels,textfileds
      static JButton sellButton=new JButton("Sell");
      static JButton buyButton=new JButton("Buy");
       static JLabel SellStock=new JLabel("StockName");
       static JLabel BuyStock=new JLabel("StockName");
       static JLabel SellPrice=new JLabel("Price");
       static JLabel BuyPrice=new JLabel("Price");
       static JLabel SellQuantity=new JLabel("Size");
       static JLabel BuyQuantity=new JLabel("Size");
       static JLabel CancelName=new JLabel("StockName");
       static JLabel CancelPrice=new JLabel("Price");
       static JTextField Text1=new JTextField(15);
       static JTextField Text2=new JTextField(15);
       static JTextField Text3=new JTextField(15);
       static JTextField Text4=new JTextField(15);
       static JTextField Text5=new JTextField(15);
       static JTextField Text6=new JTextField(15);
       static JTextField Text7=new JTextField(15);
       static JTextField Text8=new JTextField(15);
       static JTextField Text9=new JTextField(15);
       static JTextField Text10=new JTextField(15);
       static JTextField Text11=new JTextField(15);
       static JButton CancelBuyButton=new JButton("Cancel Buy");
       static JButton CancelSellButton=new JButton("Cancel Sell");

       static JLabel SearchName=new JLabel("StockName");
       static JLabel SearchPrice=new JLabel("Price");
       static JButton SearchQuote=new JButton("Search");
       static JButton ShowTrade=new JButton("ShowTrade");

//initial connection
       private Connection con;
    static private Statement stt;
    private String str;
    private BufferedReader br;

//connect to DB and create table
public Main()
{
  connectToMySQLDB();
}

//connect to SQL
private void connectToMySQLDB() {
    try {
      try {
         Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Driver loaded");
      }
      catch (Exception ex) {
        System.out.println(" CLASS NOT FOUND EXCEPTION .");
      }
      con = DriverManager.getConnection(
        "jdbc:mysql://134.74.126.107:3306/F15336syou", "F15336syou",
        "password");
    System.out.println(" CONNECTED TO MySQL DB......");
    stt = con.createStatement();
    }
    catch (Exception ex) {
        System.out.println("ERROR OCCURED.");
        ex.printStackTrace();
    }
  }

// Create layout
 public static void SellPane(Container pane) {
      pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(SellStock);
        pane.add(Text4);
        pane.add(SellPrice);
        pane.add(Text5);
        pane.add(SellQuantity);
        pane.add(Text6);
        pane.add(sellButton);
    }
 public static void BuyPane(Container pane) {
      pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(BuyStock);
        pane.add(Text1);
        pane.add(BuyPrice);
        pane.add(Text2);
        pane.add(BuyQuantity);
        pane.add(Text3);
        pane.add(buyButton);
    }

  public static void CancelPane(Container pane) {
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(CancelName);
        pane.add(Text7);
        pane.add(CancelPrice);
        pane.add(Text8);
        pane.add(CancelBuyButton);
        pane.add(CancelSellButton);

    }

      public static void QuotePane(Container pane) {
        pane.add(SearchName);
        pane.add(Text9);
        pane.add(SearchPrice);
        pane.add(Text10);
        pane.add(SearchQuote);
        pane.add(ShowTrade);
    }


//create trade
public static void CreateTradeFunction(String symbol, String p, String s,String m_id,String m_num)
{
  try{
        stt.execute("use F15336team2");
        stt.execute("INSERT INTO STOCK_TRADE VALUES("+m_id+",now(),"+m_num+","+"'"+symbol+"'"+",now(),"+p+","+s+")");
	System.out.println("Created Trade");
    }

  catch (Exception ex) 
  {
        System.out.println("can't fetch data in TradeFunction.");
   } 
}

//insert into quote
public static void InsertIntoQuote(String m_id, String m_symbol, String m_ap, String m_as, String m_bp, String m_bs)
{
    try{
        stt.execute("use F15336team2");
        ResultSet rs=stt.executeQuery("SELECT MAX(QUOTE_SEQ_NBR) FROM STOCK_QUOTE");
        if(rs.next())
        {
          int q_num=rs.getInt("MAX(QUOTE_SEQ_NBR)");
          int current_index=q_num+1;
          String s=Integer.toString(current_index);
          stt.execute("insert into STOCK_QUOTE VALUES("+m_id+",now(),"+s+",'"+m_symbol+"',now(),"+m_ap+","+m_as+","+m_bp+","+m_bs+")");
          System.out.println("Inserted your input into Quote");
        }
    }

  catch (Exception ex) 
  {
        System.out.println("can't fetch data in InsertIntoQuote function.");
   } 
}

//push buy information to database
public static void BuyFunction()
{
     String s_name=Text1.getText();
        String s_price=Text2.getText();
        String s_size=Text3.getText();
         try{
             stt.execute("use F15336team2");
            ResultSet rs=stt.executeQuery("select * from STOCK_QUOTE WHERE BID_PRICE="+s_price+" and TRADING_SYMBOL='"+s_name+"'");
              if(rs.next())
              {
               	float q=rs.getFloat("BID_PRICE");
		            int q2=rs.getInt("BID_SIZE");
	             	float f=Float.parseFloat(s_price);
	             	int f2=Integer.parseInt(s_size); 
                String m_id=rs.getString("INSTRUMENT_ID");
                String m_num=rs.getString("QUOTE_SEQ_NBR");
			             if (q==f && q2>=f2)
                   {
                      CreateTradeFunction(s_name,s_price,s_size,m_id,m_num);  
					             int a=q2-f2;
                      stt.execute("UPDATE STOCK_QUOTE SET BID_SIZE="+a+" WHERE BID_PRICE="+s_price+" and TRADING_SYMBOL='"+s_name+"' Limit 1");
                   }
		            	else
	             		{
		              		System.out.println("BID_SIZE less than Your input size");
	             		}
                }
              else
              {
                  ResultSet rs2=stt.executeQuery("select * from STOCK_QUOTE WHERE TRADING_SYMBOL='"+s_name+"'");
                  if(rs2.next())
                  {
                    String m_id=rs2.getString("INSTRUMENT_ID");
                    InsertIntoQuote(m_id,s_name,s_price,s_size,"0","0");
                  }
                  else
                  {
                    System.out.println("STOCK doesn't exist");
                  }
              }
           }
        catch (Exception ex) {
        System.out.println("can't get data");
      } 
}


//push sell information to database
public static void SellFunction()
{
  String s_name=Text4.getText();
  String s_price=Text5.getText();
  String s_size=Text6.getText();
    try{
             stt.execute("use F15336team2");
            ResultSet rs=stt.executeQuery("select * from STOCK_QUOTE WHERE ASK_PRICE="+s_price+" and TRADING_SYMBOL='"+s_name+"'");
              if(rs.next())
              {
                float q=rs.getFloat("ASK_PRICE");
                int q2=rs.getInt("ASK_SIZE");
                float f=Float.parseFloat(s_price);
                int f2=Integer.parseInt(s_size); 
                String m_id=rs.getString("INSTRUMENT_ID");
                String m_num=rs.getString("QUOTE_SEQ_NBR");
                   if (q==f && q2>=f2)
                   {
                      CreateTradeFunction(s_name,s_price,s_size,m_id,m_num);
                       int a=q2-f2;
			               stt.execute("UPDATE STOCK_QUOTE SET ASK_SIZE="+a+" WHERE ASK_PRICE="+s_price+" and TRADING_SYMBOL='"+s_name+"' Limit 1");
                   }
                  else
                  {
                      System.out.println("ASK_SIZE less than Your input size");
                  }
             }
              else
              {
                  ResultSet rs2=stt.executeQuery("select * from STOCK_QUOTE WHERE TRADING_SYMBOL='"+s_name+"'");
                  if(rs2.next())
                  {

                    String m_id=rs2.getString("INSTRUMENT_ID");
                    InsertIntoQuote(m_id,s_name,"0","0",s_price,s_size);
            
                  }
                  else
                  {
                    System.out.println("STOCK doesn't exist");
                  }
              }
       }
        catch (Exception ex) {
        System.out.println("can't get data");
      } 
}

//able to cancel a order after 1 minute. the initial CANCEL_DATE is 1 minute greater than UPLOAD_DATE
//In this function, UPLOAD_DATE=now(). if UPLOAD_DATE>CANCEL_DATE,then delete this trade from DB.
public static void CancelSellFunction()
{
  String n=Text7.getText();
  String n2=Text8.getText();
  try{
       stt.execute("use F15336team2");
       stt.execute("DELETE FROM STOCK_QUOTE WHERE TRADING_SYMBOL='"+n+"' AND BID_PRICE="+n2);
       System.out.println("canceled order");
       }
  catch (Exception ex) {
        System.out.println("can't fetch data.");
      }
}

//same sa CancelSellFunction()
public static void CancelBuyFunction()
{
  String n=Text7.getText();
  String n2=Text8.getText();
  try{
       stt.execute("use F15336team2");
       stt.execute("DELETE FROM STOCK_QUOTE WHERE TRADING_SYMBOL='"+n+"' AND ASK_PRICE="+n2);
       System.out.println("canceled order");
     }
  catch (Exception ex) {
        System.out.println("can't fetch data.");
      }
}

public static void SearchQuoteFunction()
{
    String s_name=Text9.getText();
    String s_price=Text10.getText();
    JFrame f = new JFrame("STOCK_QUOTE");
    JLabel title = new JLabel("ID   QUOTE_DATE   QUOTE_SEQ_NBR   TRADING_SYMBOL   QUOTE_TIME   ASK_PRICE   ASK_SIZE   BID_PRICE   BID_SIZE");
    JPanel jp = new JPanel();
    jp.setPreferredSize(new Dimension(800, 600));
    jp.add(title);
     try{
       stt.execute("use F15336team2");
       ResultSet rs=stt.executeQuery("select * from STOCK_QUOTE WHERE TRADING_SYMBOL='"+s_name+"' and (ASK_PRICE="+s_price+" or BID_PRICE="+s_price+")");
       while(rs.next())
      {
        String l0=rs.getString("INSTRUMENT_ID");
         String l1=rs.getString("QUOTE_DATE");
         int l2=rs.getInt("QUOTE_SEQ_NBR");
         String l3=rs.getString("TRADING_SYMBOL");
          String l4=rs.getString("QUOTE_TIME");
          float l5=rs.getFloat("ASK_PRICE");
          int l6=rs.getInt("ASK_SIZE");
          float l7=rs.getFloat("BID_PRICE");
          int l8=rs.getInt("BID_SIZE");
          JLabel q = new JLabel(l0+"    "+l1+"    "+l2+"    "+l3+"    "+l4+"    "+l5+"    "+l6+"    "+l7+"    "+l8);
          jp.add(q);
      }
       
     }
  catch (Exception ex) {
        System.out.println("can't fetch data.");
      }

     f.add(jp);
     f.pack();
     f.setLocationRelativeTo(null);
     f.setVisible(true);
}

private static void ShowTradeFunction()
{
    JFrame f = new JFrame("STOCK_TRADE");
    JLabel title = new JLabel("ID   TRADE_DATE   TRADE_SEQ_NBR   TRADING_SYMBOL   TRADE_TIME   TRADE_PRICE   TRADE_SIZE");
    JPanel jp = new JPanel();
    jp.setPreferredSize(new Dimension(666, 400));
    jp.add(title);
     try{
       stt.execute("use F15336team2");
       ResultSet rs=stt.executeQuery("select * from STOCK_TRADE");
       while(rs.next())
      {
        String l0=rs.getString("INSTRUMENT_ID");
         String l1=rs.getString("TRADE_DATE");
         int l2=rs.getInt("TRADE_SEQ_NBR");
         String l3=rs.getString("TRADING_SYMBOL");
          String l4=rs.getString("TRADE_TIME");
          int l5=rs.getInt("TRADE_PRICE");
          int l6=rs.getInt("TRADE_SIZE");
          JLabel q = new JLabel(l0+"    "+l1+"    "+l2+"    "+l3+"    "+l4+"    "+l5+"    "+l6);
          jp.add(q);
      }
     }
  catch (Exception ex) {
        System.out.println("can't fetch data.");
      }

     f.add(jp);
     f.pack();
     f.setLocationRelativeTo(null);
     f.setVisible(true);
}
private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("StockMarket");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
         
         BuyPane(frame.getContentPane());
         SellPane(frame.getContentPane());
        CancelPane(frame.getContentPane());
        QuotePane(frame.getContentPane());
        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

public static void main(String[] args) {

      new Main();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();

//Button trigger functions
    sellButton.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent e) { 
        SellFunction();
    }});

    SearchQuote.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent e) { 
        SearchQuoteFunction();
    }});

    ShowTrade.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent e) { 
        ShowTradeFunction();
    }});

    buyButton.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent e) { 
        BuyFunction();
    }});

  CancelSellButton.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e) {
        CancelSellFunction();
    }});

  CancelBuyButton.addActionListener(new ActionListener(){
     public void actionPerformed(ActionEvent e) {
        CancelBuyFunction();
    }});

            }
        });
    }
}

