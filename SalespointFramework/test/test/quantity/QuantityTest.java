package test.quantity;

import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.Unit;
import org.salespointframework.core.quantity.rounding.RoundingStrategy;

public class QuantityTest {

	@Test
	public void multiply() {
		Quantity q = new Quantity(5, Metric.PIECES, RoundingStrategy.ROUND_ONE);
		Money m = new Money(2);
		Money r = q.multiply_(m);
		System.out.println("Result: " + r.toString());
	}
	
	@Test
	public void unitTest() {
		Unit u = new Unit(4);
		Money m = new Money(15.76);
		Money r = u.multiply_(m);
		System.out.println("Result of " + u.toString() + " * " + m.toString() + " = " + r.toString());
	}
}