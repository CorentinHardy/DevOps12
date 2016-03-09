package fr.unice.polytech.ogl.isldc.testExplorer;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.unice.polytech.ogl.isldc.Objective;

public class TestObjective {
	
	String resource = "Wood";
	String resource2 = "Iron";
	int subValue = 400;
	int amount = 42;
	int amount2 = 666;
	Objective obj = new Objective(resource,amount);
	Objective obj2 = new Objective(resource2,amount2);
	
	@Test
	public void testEquals() {
		assertTrue(!obj.equals(obj2));
		assertEquals(obj, new Objective(resource, amount));
		assertTrue(!obj.equals(new Objective(resource, amount2)));
		assertTrue(!obj.equals(new Objective(resource2, amount)));
	}
	
	@Test
	public void testSub() {
		obj.subValue(subValue);
		assertTrue(obj.getAmount() == amount - subValue);
	}

	@Test
	public void testAdd() {
		obj.addAmount(amount);
		assertTrue(obj.getAmount() == amount*2);
	}

	@Test
	public void testHash() {
		assertEquals(obj.hashCode(), (13+amount)*22+resource.hashCode());
	}

	@Test
	public void testFinish() {
		obj = new Objective(resource,amount);
		obj.subValue(subValue);
		obj2.subValue(subValue);
		assertTrue(obj.isComplete());
		assertFalse(obj2.isComplete());
	}

}
