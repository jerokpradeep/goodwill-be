package in.codifi.brokerage.utility;

import java.time.LocalDate;
import java.time.YearMonth;

public class Test {

//		   LocalDate currentDate = LocalDate.now();
//	        currentDate.getMonth();
//	        LocalDate lastDayOfMonthDate  = currentDate.withDayOfMonth(
//	                                        currentDate.getMonth().length(currentDate.isLeapYear()));
//			System.out.println("Last date of the month: " + lastDayOfMonthDate);

	public static LocalDate nthDayOfFollowingMonth(int desiredDayOfMonth, LocalDate currentDate) {
		LocalDate nextMonth = YearMonth.from(currentDate).plusMonths(1).atDay(desiredDayOfMonth);
		return nextMonth;

	}
}
