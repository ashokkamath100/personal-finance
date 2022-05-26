import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

class CapitalGainTaxTest 
{

	@Test
	void test() throws CloneNotSupportedException 
	{
		CapitalGainTax user1 = new CapitalGainTax(1,50000) ; 
		
		Stock tesla1 = new Stock("tsla",100,10,"03/20/2020") ;
		Stock tesla2 = new Stock("tsla",390,10,"10/26/2020") ; 
		
		user1.stocksCurrentlyOwned.add(tesla1) ; 
		user1.stocksCurrentlyOwned.add(tesla2) ;
		//avgCost profit test
		assertEquals(9075.0, user1.computeAvgCostProfit("tsla", 850, 15,"04/01/2021")) ; 
		
		//FIFO profit test 
		assertEquals(9800,user1.computeFIFOprofit("tsla", 850, 15,"04/01/2021")) ; 
		
		//LIFO profit test
		assertEquals(8350,user1.computeLIFOprofit("tsla", 850, 15,"04/01/2021")) ; 
		
		//avgCosttax test
		assertEquals(2137, user1.computeAvgCostTax("tsla", 850, 15,"04/01/2021")) ; 
		
		//FIFOtax test entirely short term transactions
		assertEquals(2156.0, user1.computeFIFOtax("tsla", 850, 15,"01/04/2021")) ; 
		
		//FIFOtax test half short and half long term transaction 
		assertEquals(1631.0, user1.computeFIFOtax("tsla", 850, 15,"04/01/2021")) ; 
	
		//LIFOtax test entirely short term transactions 
		assertEquals(1837.0, user1.computeLIFOtax("tsla", 850, 15,"01/04/2021")) ; 
		
		//LIFOtax test half short, half long term transactions 
		assertEquals(1574.5, user1.computeLIFOtax("tsla", 850, 15,"04/01/2021")) ; 
		
		
		
		//avgCostTax tests
		
	
		//FIFOtax tests
		/*
		CapitalGainTax user = new CapitalGainTax(1,100000) ; 
		
		Stock myStock = new Stock("aapl", 150, 60, "12/20/2000") ; 
		Stock secStock = new Stock("aapl", 120, 40, "12/05/2000") ;
		Stock thrdStock = new Stock("aapl", 110,40, "12/01/2000") ; 
	
		user.stocksCurrentlyOwned.add(myStock) ; 
		user.stocksCurrentlyOwned.add(secStock) ; 
		user.stocksCurrentlyOwned.add(thrdStock) ; 
		assertEquals(48.0,user.computeFIFOtax("aapl", 110, 20, "12/20/2000")) ;
		assertEquals(28170.0,user.computeFIFOtax("aapl", 2000, 100, "12/21/2021")) ;
		
		//LIFOtax tests
		assertEquals(48.0,user.computeLIFOtax("aapl", 110, 20, "12/20/2000")) ;
		assertEquals(27930,user.computeLIFOtax("aapl", 2000, 100, "12/21/2021")) ;
		*/

		/*
		CapitalGainTax user = new CapitalGainTax(1,100000) ; 
		
		Stock myStock = new Stock("aapl", 150, 60, "12/20/2000") ; 
		Stock secStock = new Stock("aapl", 120, 40, "12/05/2000") ;
		Stock thrdStock = new Stock("aapl", 110,40, "12/01/2000") ; 
	
		user.stocksCurrentlyOwned.add(myStock) ; 
		user.stocksCurrentlyOwned.add(secStock) ; 
		user.stocksCurrentlyOwned.add(thrdStock) ; 
		//System.out.println(user.stocksCurrentlyOwned.get(1).getID()) ;
		System.out.println(user.sellStock("aapl", 200, 50, "12/21/2021")) ; 
		
		
		// 
		user.computeFIFOtax("aapl", 200, 100, "12/21/2021") ;
		user.computeLIFOtax("aapl", 2000, 100, "12/21/2021") ;
		
		user.computeFIFOtax("aapl", 2000, 100, "12/21/2021") ;
		
		CapitalGainTax user1 = new CapitalGainTax(1,50000) ; 
		
		Stock tesla1 = new Stock("tsla",100,10,"03/20/2020") ;
		Stock tesla2 = new Stock("tsla",390,10,"10/26/2020") ; 
		
		user1.stocksCurrentlyOwned.add(tesla1) ; 
		user1.stocksCurrentlyOwned.add(tesla2) ; 
		
		System.out.println(user1.computeFIFOtax("tsla", 850, 15,"04/01/2021")) ;  
		System.out.println(user1.computeAvgCostProfit("tsla", 850, 15,"04/01/2021")) ; 
		System.out.println(user1.computeAvgCostTax("tsla", 850, 15,"04/01/2021")) ; 
		//user1.computeLIFOtax("tsla", 850, 15,"04/01/2021") ;  
		
		
		*/
		
		
		
		
	}

}
