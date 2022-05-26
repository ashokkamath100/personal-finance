import java.util.ArrayList;
import java.util.Scanner;

public class CapitalGainTax 
{
	private String userFilingStatus ;
	double userIncome ; 
	ArrayList<Stock> stocksCurrentlyOwned = new ArrayList<Stock>() ;
	
	public CapitalGainTax(int filingStatus, double income)
	{
		if(filingStatus == 1)
		{
			userFilingStatus = "Single" ; 
		}else if(filingStatus == 2)
		{
			userFilingStatus = "Married" ; 
		}
		userIncome = income ; 
		ArrayList<Stock> stocksCurrentlyOwned = new ArrayList<Stock>() ; 
	}
	
	public void setUserFilingStatus(int filingStatus)
	{
		if(filingStatus == 1)
		{
			userFilingStatus = "Single" ; 
		}
		else if(filingStatus == 2)
		{
			userFilingStatus = "Married filing jointly" ; 
		}
	}	
	/**
	 * Finds all the matches of a stock in the 
	 * stocksCurrentlyOwned arrayList by looking only 
	 * at stockName
	 * @param stockName the name of the stock the method is finding matches for 
	 * @return an ArrayList of stocks with the same name that are currently owned 
	 * @throws CloneNotSupportedException
	 */
	public ArrayList<Stock> findStockMatches(String stockName) throws CloneNotSupportedException 
	{
		ArrayList<Stock> stocksMatch = new ArrayList<Stock>() ; 
		//find all matches to the stock and put in new arrayList 
		for(int i = 0 ; i < stocksCurrentlyOwned.size() ; i++)
		{
			if(stocksCurrentlyOwned.get(i).getStockName().equals(stockName))
			{
				stocksMatch.add(stocksCurrentlyOwned.get(i).clone()) ; 
			}
		}
		return stocksMatch ; 
	}
	/**
	 * Find the profit of the transaction based on average cost 
	 * @param stockName name of stock 
	 * @param sellingPrice price at which stock is sold
	 * @param quantToSell quantity of stock user is selling
	 * @param date when the stock is sold 
	 * @return the profit based on average cost methodology 
	 * @throws CloneNotSupportedException
	 */
	public double computeAvgCostProfit(String stockName, double sellingPrice, int quantToSell, String date) throws CloneNotSupportedException
	{
		ArrayList<Stock> stocksMatch = findStockMatches(stockName) ;
		double avgSum = 0 ; 
		double sharesTotal = 0 ;  
		for(int i  = 0 ; i < stocksMatch.size() ; i++)
		{
			//compute average purchase price 
			avgSum += stocksMatch.get(i).getPrice() * stocksMatch.get(i).getQuantBought() ; 
			sharesTotal += stocksMatch.get(i).getQuantBought() ; 
		}
		double avgPurchasePrice = avgSum / sharesTotal ; 
		double avgCostProfit= (quantToSell * sellingPrice) - ( avgPurchasePrice * quantToSell)  ; 
		
		return avgCostProfit ; 
	}
	/**
	 * Find the tax based on average cost profit
	 * @param stockName name of stock 
	 * @param sellingPrice price at which stock is sold
	 * @param quantToSell quantity of stock user is selling
	 * @param date when the stock is sold 
	 * @return the tax based on average cost methodology 
	 * @throws CloneNotSupportedException
	 */
	public double computeAvgCostTax(String stockName, double sellingPrice, int quantToSell, String date) throws CloneNotSupportedException
	{
		ArrayList<Stock> stocksMatch = findStockMatches(stockName) ;
		double avgSum = 0 ; 
		double sharesTotal = 0 ;  
		
		double stPurchaseCost = 0 ; 
		double stQuant = 0 ;
		
		double ltPurchaseCost = 0 ; 
		double ltQuant = 0 ; 
		
		for(int i  = 0 ; i < stocksMatch.size() ; i++)
		{
			//compute average purchase price 
			avgSum += stocksMatch.get(i).getPrice() * stocksMatch.get(i).getQuantBought() ; 
			sharesTotal += stocksMatch.get(i).getQuantBought() ; 
			
			Stock current = stocksMatch.get(i) ; 
			
			if(current.shortTermTrans(date))
			{
				stPurchaseCost += (current.getPrice() * current.getQuantBought()) ; 
				stQuant+= current.getQuantBought() ; 
			} 
			else
			{
				ltPurchaseCost += (current.getPrice() * current.getQuantBought()) ; 
				ltQuant += current.getQuantBought() ; 
			}	
		}
		
		double avgPurchasePrice = avgSum / sharesTotal ; 
		double avgCostProfit= (quantToSell * sellingPrice) - ( avgPurchasePrice * quantToSell)  ;
		
		double stAvgCostProfit = (stQuant * sellingPrice) - stPurchaseCost ; 
		double ltAvgCostProfit = (ltQuant * sellingPrice) - ltPurchaseCost ; 
		
		double avgCostTax = computeCapitalGainsTax(stAvgCostProfit,false) + computeCapitalGainsTax(ltAvgCostProfit, true) ; 
		
		return avgCostTax ; 
	}
	/**
	 * Find the FIFO profit based on methodology
	 * @param stockName name of stock 
	 * @param sellingPrice price at which stock is sold
	 * @param quantToSell quantity of stock user is selling
	 * @param date when the stock is sold 
	 * @return the profit based on FIFO methodology 
	 * @throws CloneNotSupportedException
	 */
	public double computeFIFOprofit(String stockName, double sellingPrice, int quantToSell, String date) throws CloneNotSupportedException
	{
		ArrayList<Stock> stocksMatch1 = findStockMatches(stockName) ;
		
		//short term capital gains variables 
		double stPurchaseCost = 0 ; 
		int stQuant = 0 ;
	
		//long term capital gains variables 
		double ltPurchaseCost = 0 ; 
		int ltQuant = 0 ; 
	
		int quant = quantToSell ; 
		//System.out.println(quant) ; 
		while(quant > 0)
		{
			//find earliest purchased stock
 			Stock oldest = Stock.findOldestStock(stocksMatch1) ; 
 			
 			if(oldest.getQuantBought() >= quant)
 			{
 				if(oldest.shortTermTrans(date))
 				{
 					stPurchaseCost += (oldest.getPrice() * quant) ; 
 					stQuant+= quant ; 
 				} 
 				else
 				{
 					ltPurchaseCost += (oldest.getPrice() * quant) ; 
 					ltQuant += quant ; 
 				}	
 				
 				quant = 0 ; 
 				oldest.setQuantBought(oldest.getQuantBought() - quant);
 			}
 			else if(oldest.getQuantBought() < quant)
 			{
 				if(oldest.shortTermTrans(date))
 				{
 					stPurchaseCost += (oldest.getPrice() * oldest.getQuantBought()) ; 
 					stQuant+= oldest.getQuantBought() ; 
 				} 
 				else
 				{
 					ltPurchaseCost += (oldest.getPrice() * oldest.getQuantBought()) ; 
 					ltQuant += oldest.getQuantBought() ; 
 				}	
 				quant = quant - oldest.getQuantBought() ; 
 				oldest.setQuantBought(0);
 				
 			}
 			//remove stock once quantbought == 0 ?
			if(oldest.getQuantBought() == 0)
			{
				for(int i = 0 ; i < stocksMatch1.size() ; i++)
				{
					if(stocksMatch1.get(i).getID() == oldest.getID())
					{
						stocksMatch1.remove(i) ; //remove stock from stocksMatch 
					}
				}
 			}
		}
		double stFIFO = (sellingPrice * stQuant) - stPurchaseCost ; 
		double ltFIFO = (sellingPrice * ltQuant) - ltPurchaseCost ;
		double totalFIFO = stFIFO + ltFIFO ; 
		
		return totalFIFO ; 
	
	}
	/**
	 * Find the tax based on FIFO profit
	 * @param stockName name of stock 
	 * @param sellingPrice price at which stock is sold
	 * @param quantToSell quantity of stock user is selling
	 * @param date when the stock is sold 
	 * @return the tax for the profit 
	 * @throws CloneNotSupportedException
	 */
	public double computeFIFOtax(String stockName, double sellingPrice, int quantToSell, String date) throws CloneNotSupportedException
	{
		ArrayList<Stock> stocksMatch1 = findStockMatches(stockName) ;
		
		//short term capital gains variables 
		double stPurchaseCost = 0 ; 
		int stQuant = 0 ;
	
		//long term capital gains variables 
		double ltPurchaseCost = 0 ; 
		int ltQuant = 0 ; 
	
		int quant = quantToSell ; 
		
		while(quant > 0)
		{
			//find earliest purchased stock
 			Stock oldest = Stock.findOldestStock(stocksMatch1) ; 
 			
 			if(oldest.getQuantBought() >= quant)
 			{
 				if(oldest.shortTermTrans(date))
 				{
 					stPurchaseCost += (oldest.getPrice() * quant) ; 
 					stQuant+= quant ; 
 				} 
 				else
 				{
 					ltPurchaseCost += (oldest.getPrice() * quant) ; 
 					ltQuant += quant ; 
 				}	
 				
 				quant = 0 ; 
 				oldest.setQuantBought(oldest.getQuantBought() - quant);
 			}
 			else if(oldest.getQuantBought() < quant)
 			{
 				if(oldest.shortTermTrans(date))
 				{
 					stPurchaseCost += (oldest.getPrice() * oldest.getQuantBought()) ; 
 					stQuant+= oldest.getQuantBought() ; 
 				} 
 				else
 				{
 					ltPurchaseCost += (oldest.getPrice() * oldest.getQuantBought()) ; 
 					ltQuant += oldest.getQuantBought() ; 
 				}	
 				quant = quant - oldest.getQuantBought() ; 
 				oldest.setQuantBought(0);
 				
 			}
 			//remove stock once quantbought == 0 ?
			if(oldest.getQuantBought() == 0)
			{
				for(int i = 0 ; i < stocksMatch1.size() ; i++)
				{
					if(stocksMatch1.get(i).getID() == oldest.getID())
					{
						stocksMatch1.remove(i) ; //remove stock from stocksMatch 
					}
				}
 			}
		}
		
		double stFIFO = (sellingPrice * stQuant) - stPurchaseCost ; 
		double ltFIFO = (sellingPrice * ltQuant) - ltPurchaseCost ;
		//compute capital gains tax on stFifo and ltFifo
		double totalFIFOtax = computeCapitalGainsTax(stFIFO, false) + computeCapitalGainsTax(ltFIFO, true) ; 
		//System.out.println("Capital gains tax under FIFO: " + totalFIFOtax) ;
		//System.out.println("Short term FIFO : "+ stFIFO) ; 
		//System.out.println("Long term FIFO : " + ltFIFO) ; 
		return totalFIFOtax  ; 
		
	}
	/**
	 * Find the profit based on LIFO methodology
	 * @param stockName name of stock 
	 * @param sellingPrice price at which stock is sold
	 * @param quantToSell quantity of stock user is selling
	 * @param date when the stock is sold 
	 * @return the tax based on LIFO methodology 
	 * @throws CloneNotSupportedException
	 */
	public double computeLIFOprofit(String stockName, double sellingPrice, int quantToSell, String date) throws CloneNotSupportedException
	{
		double stLIFOpc = 0 ; 
		int stLIFOquant = 0 ; 
		
		double ltLIFOpc = 0 ; 
		int ltLIFOquant = 0 ;
		
		ArrayList<Stock> stocksMatch1 = findStockMatches(stockName) ;
		
		int quant = quantToSell ; 
		
		while(quant > 0)
		{
			//find earliest purchased stock
 			Stock newest = Stock.findNewestStock(stocksMatch1) ; 
 			if(newest.getQuantBought() >= quant)
 			{
 				if(newest.shortTermTrans(date))
 				{
 					stLIFOpc += (newest.getPrice() * quant) ; 
 					stLIFOquant += quant ; 
 				} 
 				else
 				{
 					ltLIFOpc += (newest.getPrice() * quant) ; 
 					ltLIFOquant += quant ; 
 				}	
 				quant = 0 ; 
 				newest.setQuantBought(newest.getQuantBought() - quant);
 			}
 			else if(newest.getQuantBought() < quant)
 			{
 				if(newest.shortTermTrans(date))
 				{
 					stLIFOpc += (newest.getPrice() * newest.getQuantBought()) ; 
 					stLIFOquant+= newest.getQuantBought() ; 
 				} 
 				else
 				{
 					ltLIFOpc += (newest.getPrice() * newest.getQuantBought()) ; 
 					ltLIFOquant += newest.getQuantBought() ; 
 				}	
 				quant = quant - newest.getQuantBought() ; 
 				newest.setQuantBought(0);
 			}
 			//remove stock once quantbought is equal to zero 
			if(newest.getQuantBought() == 0)
			{
				for(int i = 0 ; i < stocksMatch1.size() ; i++)
				{
					if(stocksMatch1.get(i).getID() == newest.getID())
					{
						stocksMatch1.remove(i) ; //remove stock from stocksMatch 
					}
				}
 			}
			
			//System.out.println(LIFOprofit) ; 
		}
		double stLIFO = (sellingPrice * stLIFOquant) - stLIFOpc ; 
		double ltLIFO = (sellingPrice * ltLIFOquant) - ltLIFOpc ;
		
		double LIFOprofit = stLIFO + ltLIFO ; 
		return LIFOprofit ; 
	}
	/**
	 * Find the tax based on LIFO profit
	 * @param stockName name of stock 
	 * @param sellingPrice price at which stock is sold
	 * @param quantToSell quantity of stock user is selling
	 * @param date when the stock is sold 
	 * @return the tax 
	 * @throws CloneNotSupportedException
	 */
	public double computeLIFOtax(String stockName, double sellingPrice, int quantToSell, String date) throws CloneNotSupportedException
	{
		double stLIFOpc = 0 ; 
		int stLIFOquant = 0 ; 
		
		double ltLIFOpc = 0 ; 
		int ltLIFOquant = 0 ;
		
		ArrayList<Stock> stocksMatch1 = findStockMatches(stockName) ;
		//System.out.println(findStockMatches(stockName)) ; 
		
		
		int quant = quantToSell ; 
		
		while(quant > 0)
		{
			//find earliest purchased stock
 			Stock newest = Stock.findNewestStock(stocksMatch1) ; 
 			if(newest.getQuantBought() >= quant)
 			{
 				if(newest.shortTermTrans(date))
 				{
 					stLIFOpc += (newest.getPrice() * quant) ; 
 					stLIFOquant += quant ; 
 				} 
 				else
 				{
 					ltLIFOpc += (newest.getPrice() * quant) ; 
 					ltLIFOquant += quant ; 
 				}	
 				quant = 0 ; 
 				newest.setQuantBought(newest.getQuantBought() - quant);
 			}
 			else if(newest.getQuantBought() < quant)
 			{
 				if(newest.shortTermTrans(date))
 				{
 					stLIFOpc += (newest.getPrice() * newest.getQuantBought()) ; 
 					stLIFOquant+= newest.getQuantBought() ; 
 				} 
 				else
 				{
 					ltLIFOpc += (newest.getPrice() * newest.getQuantBought()) ; 
 					ltLIFOquant += newest.getQuantBought() ; 
 				}	
 				quant = quant - newest.getQuantBought() ; 
 				newest.setQuantBought(0);
 			}
 			//remove stock once quantbought is equal to zero 
			if(newest.getQuantBought() == 0)
			{
				for(int i = 0 ; i < stocksMatch1.size() ; i++)
				{
					if(stocksMatch1.get(i).getID() == newest.getID())
					{
						stocksMatch1.remove(i) ; //remove stock from stocksMatch 
					}
				}
 			}
			
			//System.out.println(LIFOprofit) ; 
		}
		double stLIFO = (sellingPrice * stLIFOquant) - stLIFOpc ; 
		double ltLIFO = (sellingPrice * ltLIFOquant) - ltLIFOpc ;
		
		double LIFOprofit = stLIFO + ltLIFO ; 
		
		//compute capital gains tax on stFifo and ltFifo
		double totalLIFOtax = computeCapitalGainsTax(stLIFO, false) + computeCapitalGainsTax(ltLIFO, true) ; 
		
		return totalLIFOtax ;
	}
	
	/**
	 * Display information to user by calling other
	 * methods and then actually sell the stock based on FIFO methodology 
	 * @param stockName name of stock 
	 * @param sellingPrice price at which stock is sold
	 * @param quantToSell quantity of stock user is selling
	 * @param date when the stock is sold 
	 * @throws CloneNotSupportedException
	 */
	public void sellStock(String stockName, double sellingPrice, int quantToSell, String date) throws CloneNotSupportedException
	{
		ArrayList<Stock> stocksMatch = new ArrayList<Stock>() ; 
		ArrayList<Stock> stocksMatch1 = findStockMatches(stockName) ; //create StocksMatch1 

		//System.out.println(findStockMatches(stockName)) ; 
		double avgCostProfit = 0 ; 
		double FIFOprofit = 0 ; 
		double LIFOprofit = 0 ; 
		
		double avgCostTax = 0 ; 
		double FIFOtax = 0 ; 
		double LIFOtax = 0 ; 
		
		if(stocksMatch1.size() == 0 )
		{
			System.out.println("You don't own this stock, so you cannot sell it") ; 
			
		}
		else
		{
			int sumOfShares = 0 ; 
			for(int i = 0 ; i < stocksMatch1.size(); i++)
			{
				sumOfShares += stocksMatch1.get(i).getQuantBought() ; 
			}
			if(sumOfShares < quantToSell)
			{
				System.out.println("You don't own this many shares. Please restart") ; 
			}
			else
			{ 
				//compute profits and display to user
				System.out.println("") ;
				System.out.println("PROFIT/LOSS FOR ALL THREE COST BASES") ; 
				avgCostProfit = computeAvgCostProfit(stockName, sellingPrice, quantToSell, date) ; 
				FIFOprofit = computeFIFOprofit(stockName, sellingPrice, quantToSell, date) ; 
				LIFOprofit = computeLIFOprofit(stockName, sellingPrice, quantToSell, date) ;
				System.out.println("Average Cost Profit/Loss: " + avgCostProfit) ; 
				System.out.println("FIFO Profit/Loss: " + FIFOprofit) ; 
				System.out.println("LIFO Profit/Loss: " + LIFOprofit) ; 
				System.out.println("") ; 
				//compute taxes and display to user  
				System.out.println("CAPITAL GAINS TAX FOR ALL THREE COST BASES") ; 
				avgCostTax = computeAvgCostTax(stockName, sellingPrice, quantToSell, date) ; 
				FIFOtax = computeFIFOtax(stockName, sellingPrice, quantToSell, date) ; 
				LIFOtax = computeLIFOtax(stockName, sellingPrice, quantToSell, date) ; 
				
				System.out.println("Tax on Average Cost Profits: " + avgCostTax) ; 
				System.out.println("Tax on FIFO Profits: " + FIFOtax) ; 
				System.out.println("Tax on LIFO Profits: " + LIFOtax) ; 
				System.out.println(); 
				
				//sell stocks on FIFO basis 
				int quantity = quantToSell ; 
				while(quantity > 0)
				{
					//find stocks that Match
					//do not call findStocksMatch because we don't want to create deep copy (which 
					// is what findStocksMatch does in its implementation 
					
					for(int i = 0 ; i < stocksCurrentlyOwned.size() ; i++)
					{
						if(stocksCurrentlyOwned.get(i).getStockName().equals(stockName))
						{
							stocksMatch.add(stocksCurrentlyOwned.get(i)) ; 
						}
					}
					
					ArrayList<Stock> stocksMatch2 = findStockMatches(stockName) ;
					
					//find earliest purchased stock
		 			Stock oldest = Stock.findOldestStock(stocksMatch) ; 
		 			
		 			if(oldest.getQuantBought() > quantity)
		 			{
		 				
		 				//oldest.setQuantBought(oldest.getQuantBought() - quantity);
		 				for(int i = 0 ; i < stocksCurrentlyOwned.size(); i++)
		 				{
		 					if(stocksCurrentlyOwned.get(i).getID() == oldest.getID())
		 					{
		 						stocksCurrentlyOwned.get(i).setQuantBought(stocksCurrentlyOwned.get(i).getQuantBought() - quantity);
		 					}
		 				}
		 				
		 				quantity = 0 ; 
		 			}
		 			else if (oldest.getQuantBought() < quantity)
		 			{
		 				quantity = quantity - oldest.getQuantBought() ; 
		 				for(int i = 0 ; i < stocksCurrentlyOwned.size() ; i++)
		 				{
		 					if(stocksCurrentlyOwned.get(i).getID() == oldest.getID())
		 					{
		 						
		 						stocksCurrentlyOwned.get(i).setQuantBought(0);
		 					}
		 				}
		 				//oldest.setQuantBought(0) ;
		 			}
		 			else if(oldest.getQuantBought() == quantity)
		 			{
		 				for(int i = 0 ; i < stocksCurrentlyOwned.size(); i++)
		 				{
		 					if(stocksCurrentlyOwned.get(i).getID() == oldest.getID())
		 					{
		 						stocksCurrentlyOwned.get(i).setQuantBought(stocksCurrentlyOwned.get(i).getQuantBought() - quantity);
		 					}
		 				}
		 				quantity = 0 ; 
		 			}
		 			if(oldest.getQuantBought() == 0)
		 			{
		 				removeStock(oldest.getID()) ; //remove stock from stocks currently owned 
		 				
		 				for(int i = 0 ; i < stocksMatch.size() ; i++)
		 				{
		 					if(stocksMatch.get(i).getID() == oldest.getID())
		 					{
		 						stocksMatch.remove(i) ; //remove stock from stocksMatch 
		 					}
		 				}
		 			}
				}
				showCurrentStocks() ; 
			}
		}
	}
	/**
	 * Remove stock from arrayList of currently owned stocks 
	 * @param id
	 */
	public void removeStock(int id)
	{
		for(int i = 0 ; i < stocksCurrentlyOwned.size() ; i++)
		{
			if(stocksCurrentlyOwned.get(i).getID() == id)
			{
				stocksCurrentlyOwned.remove(i) ; 
			}
		}
	}
	/**
	 * Use for loop to display information 
	 * about stocks that are currently owned 
	 */
	public void showCurrentStocks()
	{
		System.out.println("Stock name" + "         " + "Stock Price when Bought" + "         " + "Quantity Bought") ;
		if(stocksCurrentlyOwned.size() > 0)
		{
			for(int i = 0 ; i < stocksCurrentlyOwned.size(); i++)
			{
				
				System.out.print(stocksCurrentlyOwned.get(i).getStockName() + "                          ") ; 
				System.out.print(stocksCurrentlyOwned.get(i).getPrice() + "                              "  ) ; 
				System.out.println(stocksCurrentlyOwned.get(i).getQuantBought()) ; 
				
			}
		}
		else
		{
			System.out.println("No stocks held") ; 
		}
	}
	/**
	 * Find the capital gains tax on stock transaction 
	 * based on income and filing status 
	 * @param profitLoss the profit or loss 
	 * @param longTerm whether the stock is a short/long term transaction
	 * @return the capital gains tax 
	 */
	public double computeCapitalGainsTax(double profitLoss, boolean longTerm)
	{
		double taxLiab = 0 ; 
		
		if(profitLoss <= 0 )
		{
			taxLiab = 0 ; 
		}
		else
		{
			if(longTerm)
			{
				if(this.userFilingStatus.equals("Single")) 
				{
					if(this.userIncome <= 40000)
					{
						taxLiab = .00 * profitLoss ; 
					}
					else if(this.userIncome <= 441450 && this.userIncome > 40000)
					{
						taxLiab = .15 * profitLoss ;
					}
					else
					{
						taxLiab = .20 * profitLoss ;
					}
				}
				else if(this.userFilingStatus.equals("Married filing jointly"))
				{
					
					if(this.userIncome <= 80000)
					{
						taxLiab = .00 * profitLoss ; 
					}
					else if(this.userIncome <= 496600 && this.userIncome > 80000)
					{
						taxLiab = .15 * profitLoss ;
					}
					else
					{
						taxLiab = .20 * profitLoss ;
					}
				}
			}
			else //not long term 
			{
				if(this.userFilingStatus.equals("Single"))
				{
					if(this.userIncome <= 9875 )
					{
						taxLiab = .1 * profitLoss ; 
					}
					else if(this.userIncome >= 9876 && this.userIncome <= 40125)
					{
						taxLiab = .12 * profitLoss ; 
					}
					else if(this.userIncome >= 40126 && this.userIncome <= 85525)
					{
						taxLiab = .22 * profitLoss ; 
					}
					else if(this.userIncome >= 85526 && this.userIncome <= 163300)
					{
						taxLiab = .24 * profitLoss ; 
					}
					else if(this.userIncome >= 163301 && this.userIncome <= 207350)
					{
						taxLiab = .32 * profitLoss ;
					}
					else if(this.userIncome >= 207350 && this.userIncome <= 518400)
					{
						taxLiab = .35 * profitLoss ; 
					}
					else if(this.userIncome > 518400)
					{
						taxLiab = .37 * profitLoss ; 
					}
				else
				{
					if(this.userIncome <= 19750 )
					{
						taxLiab = .1 * profitLoss ; 
					}
					else if(this.userIncome >= 19751 && this.userIncome <= 80250)
					{
						taxLiab = .12 * profitLoss ; 
					}
					else if(this.userIncome >= 80251 && this.userIncome <= 171050)
					{
						taxLiab = .22 * profitLoss ; 
					}
					else if(this.userIncome >= 171051 && this.userIncome <= 326600)
					{
						taxLiab = .24 * profitLoss ; 
					}
					else if(this.userIncome >= 326601 && this.userIncome <= 414700)
					{
						taxLiab = .32 * profitLoss ;
					}
					else if(this.userIncome >= 414701 && this.userIncome <= 622050)
					{
						taxLiab = .57 * profitLoss ; 
					}
					else if(this.userIncome > 622050)
					{
						taxLiab = .35 * profitLoss ; 
					}
				}
				}
			}
		}
	return taxLiab ; 
	}

	public static void main(String[] args) throws CloneNotSupportedException 
	{
		Scanner scan = new Scanner(System.in) ; 
		
		System.out.println("What is your income?") ; 
		double income = scan.nextDouble();
		System.out.println("What is your filing status? Enter 1 for Single and 2 for Married") ; 
		int filingStat = scan.nextInt() ; 
		
		CapitalGainTax user = new CapitalGainTax(filingStat, income) ;
		//System.out.println(user.userIncome) ; 
		//System.out.println(user.userFilingStatus) ; 
		
		int x = 0 ;
		do {
				System.out.println("Menu \n(1) Purchase stocks.\n(2) "
						+ "Show current investments.\n(3) Sell stocks.\n(4) Quit.") ; 
				x = scan.nextInt() ;
	
				if(x == 1)
				{
					System.out.println("What is the stock name?") ; 
					String stockName = scan.next() ; 
					
					System.out.println("What is the stock price? (do not put in dollar sign)") ; 
					double price = scan.nextDouble() ; 
					
					System.out.println("What is the stock quantity") ; 
					int quan = scan.nextInt() ; 
					
					System.out.println("What is the date of purchase? (format 02/01/2000)") ; 
					String date = scan.next() ;
					Stock stock = new Stock(stockName, price, quan, date) ; 
					//System.out.println(stock.getQuantBought()) ; 
					user.stocksCurrentlyOwned.add(stock) ; 
				
				} 
				else if(x == 2)
				{
					user.showCurrentStocks() ; 
				}
				else if(x == 3)
				{
					
					System.out.println("What is the stock name?") ; 
					String stockName = scan.next() ; 
					
					System.out.println("What is the stock's selling price? (do not put in dollar sign)") ; 
					double price = scan.nextDouble() ; 
					
					System.out.println("What is the quantity sold") ; 
					int quan = scan.nextInt() ; 
					
					System.out.println("What is the date you are selling the stock? (format 02/01/2000)") ; 
					String date = scan.next() ;
					user.sellStock(stockName, price, quan, date) ; 
				
				}
			}
		while(x != 4); 
	}
}
	




