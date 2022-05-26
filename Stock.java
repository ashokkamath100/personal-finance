import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Stock implements Cloneable
{
	//each stock object holds the stocks bought in one transaction 
	private String stockName ; 
	private double price ; 
	private int quantBought ; 
	private String date ; 
	private int day ; 
	private int month ; 
	private int year ; 
	private int stockTransID ; 
	static int stockTransIDCounter ; 
	
	public Stock(String name, double p, int quantity, String date)
	{
		stockName = name ; 
		price = p ;
		quantBought = quantity ; 
		stockTransID = stockTransIDCounter ; 
		stockTransIDCounter++ ; 
		this.date  = date ; 
		month = Integer.valueOf(date.substring(0,2)) ; 
		day = Integer.valueOf(date.substring(3,5)) ; 
		year = Integer.valueOf(date.substring(6,date.length())) ; 
		
	}
	/**
	 * Create a clone of a stock object
	 * @return a Stock object 
	 * @throws CloneNotSupportedException 
	 */
	public Stock clone() throws CloneNotSupportedException 
	{
		Stock s = (Stock)super.clone() ; 
		s.stockName = this.stockName ; 
		s.price = this.price ; 
		s.quantBought = this.quantBought ; 
		s.date = this.date ; 
		s.day = this.day ; 
		s.month = this.month ; 
		s.year = this.year ; 
		s.stockTransID = this.stockTransID ; 
		
		
		
		return s ; 
	}
	
	public String getStockName()
	{
		return stockName ; 
	}
	
	public double getPrice()
	{
		return price ; 
	}
	
	public int getQuantBought()
	{
		return quantBought ; 
	}
	
	
	public int getID()
	{
		return stockTransID ; 
	}
	
	public String getDate()
	{
		return date ; 
	}
	
	public int getDay()
	{
		return day ; 
	}
	
	public int getMonth()
	{
		return month ;
	}
	
	public int getYear()
	{
		return year ; 
	}
	
	public void setPrice(double price)
	{
		if(price > 0)
		{
			this.price = price ; 
		}
	}
	
	public void setQuantBought(int quantBought)
	{
		this.quantBought = quantBought ; 
		
	}
	/**
	 * Find the oldest stock object in an arrayList 
	 * @param stocks ArrayList of stock objects
	 * @return Stock object that is the oldest 
	 */
	public static Stock findOldestStock(ArrayList<Stock> stocks)
	{
		Stock oldestStock = stocks.get(0); 
		
		for(int i = 0 ; i < stocks.size() ; i++)
		{
			if(oldestStock.getQuantBought() > 0)
			{
				if(stocks.get(i).getYear() < oldestStock.getYear()) //if index stock has year older than current oldest
				{
					oldestStock = stocks.get(i) ; 
				}
				else if(stocks.get(i).getYear() == oldestStock.getYear() // if index stock has year same and month older than current oldest
						&& stocks.get(i).getMonth() < oldestStock.getMonth()) 
				{
					oldestStock = stocks.get(i) ; 
				}
				else if(stocks.get(i).getYear() == oldestStock.getYear() // if index stock has year, month same and day is older than current oldest 
						&& stocks.get(i).getMonth() == oldestStock.getMonth()
						&& stocks.get(i).getDay() < oldestStock.getDay())
				{
					oldestStock = stocks.get(i) ; 
				}
			}
		}
		return oldestStock ; 
	}
	/**
	 * Find the newest stock in an arrayList 
	 * @param stocks arrayList of stocks 
	 * @return Stock object that is newest 
	 */
	public static Stock findNewestStock(ArrayList<Stock> stocks)
	{
		Stock newestStock = stocks.get(0) ; 
		for(int i = 0 ; i < stocks.size() ; i++)
		{
			if(newestStock.getQuantBought() > 0 )
			{
				if(stocks.get(i).getYear() > newestStock.getYear()) //if index stock has year newer than current oldest
				{
					newestStock = stocks.get(i) ; 
				}
				else if(stocks.get(i).getYear() == newestStock.getYear() // if index stock has year same and month newer than current oldest
						&& stocks.get(i).getMonth() > newestStock.getMonth()) 
				{
					newestStock = stocks.get(i) ; 
				}
				else if(stocks.get(i).getYear() == newestStock.getYear() // if index stock has year, month same and day is newer than current oldest 
						&& stocks.get(i).getMonth() == newestStock.getMonth()
						&& stocks.get(i).getDay() > newestStock.getDay())
				{
					newestStock = stocks.get(i) ; 
				}
			}
			
		}
		//System.out.println(newestStock.getQuantBought()) ; 
		return newestStock ; 
		
	}
	
	/**
	 * Find whether a transaction is short or long term 
	 * @param today the day when the user sells the stock 
	 * @return boolean of whether the transaction was short term 
	 */
	public boolean shortTermTrans(String today)
	{
		boolean isShortTerm = false ;
		int todayMonth = Integer.valueOf(today.substring(0,2)) ; 
		int todayDay = Integer.valueOf(today.substring(3,5)) ; 
		int todayYear = Integer.valueOf(today.substring(6,today.length())) ; 
		
		if(todayYear - this.year > 1 )
		{
			isShortTerm = false ; 
		}
		else if(todayYear - this.year == 1 )
		{
			if(todayMonth - this.month > 0 )
			{
				isShortTerm = false ; 
			}
			else if(todayMonth - this.month == 0 )
			{
				if(todayDay - this.day > 0)
				{
					isShortTerm = false ; 
				}
				else
				{
					isShortTerm = true ; 
				}
			}
			else
			{
				isShortTerm = true ; 
			}
		}
		else if(todayYear - this.year == 0 )
		{
			isShortTerm = true ; 
		}
		
		
		return isShortTerm ; 
		
	}
	public static void main(String [] args)
	{
		/*
		Stock myStock = new Stock("aapl", 100, 60, "12/20/2000") ; 
		Stock secStock = new Stock("aapl", 100, 40, "12/05/2000") ;
		Stock thrdStock = new Stock("aapl", 100,40, "12/01/2000") ; 
		
		System.out.println(myStock.getID()) ; 
		System.out.println(secStock.getID()) ; 
		System.out.println(thrdStock.getID()) ; 
		
		ArrayList<Stock> stocksMatchLCopy = new ArrayList<Stock>() ;
		
		stocksMatchLCopy.add(myStock) ;
		stocksMatchLCopy.add(secStock) ; 
		stocksMatchLCopy.add(thrdStock) ; 
		System.out.println(Stock.findNewestStock(stocksMatchLCopy).getQuantBought()) ; 
		*/
		//System.out.println(myStock.shortTermTrans("07/20/2001")) ; 
		 
	}
	
	
}
