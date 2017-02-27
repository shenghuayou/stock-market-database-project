# stock-market-database-project #
This is a simple stock market transaction system which allow user to buy and sell stocks. Users are allowed to post a buy or a sell infomation in database, when a buy match a sell's price, name of stocks... the system will create trade. The system also allow user to cancel a buy or a sell, search for stocks.

## tools ##
* Mysql database
* Java with its GUI package
* if you run this program in VPS, you need to set up X windonw for GUI compilation.

## demo ##
I try to buy ACC stock...but it is not exist. (I only copy 500000 rows from stockmarket)
<img src=https://github.com/shenghuayou/stock-market-database-project/blob/master/demo/1.png />
***
Then, I try to buy AAC with PRICE=1000 and SIZE=1000. It shows “Inserted your input into quote”
<img src=https://github.com/shenghuayou/stock-market-database-project/blob/master/demo/2.png />
***
I use search to display this STOCK with this Price in a table.
<img src=https://github.com/shenghuayou/stock-market-database-project/blob/master/demo/3.png />
***
I sell AAC with PRICE=1000, SIZE=100. It shows “created trade”
<img src=https://github.com/shenghuayou/stock-market-database-project/blob/master/demo/4.png />
***
I display all the tables. Now the size in STOCK_QUOTE is 900. The STOCK_TRADE has a new trade with TRADING_SYMBOL=AAC, PRICE=1000, SIZE=100.
<img src=https://github.com/shenghuayou/stock-market-database-project/blob/master/demo/5.png />
***
I use Cancel Buy button to cancel the order. Now AAC is deleted from STOCK_QUOTE.
<img src=https://github.com/shenghuayou/stock-market-database-project/blob/master/demo/6.png />
