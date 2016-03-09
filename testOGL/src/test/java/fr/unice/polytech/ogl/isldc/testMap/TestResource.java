package fr.unice.polytech.ogl.isldc.testMap;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.unice.polytech.ogl.isldc.map.Resource;

public class TestResource {

	private String resource1 = "Bois";
	private String resource2 = "Fer";
	private String amount1 = "High";
	private String amount2 = "Low";
	private String cond1 = "Easy";
	private String cond2 = "Hard";
	private String unknown = "unknown";
	Resource r1 = new Resource(resource1,amount1,cond1);
	Resource r2 = new Resource(resource2,amount1,cond1);
	Resource r3 = new Resource(resource1,unknown,unknown);
	
	@Test
	public void testGet() {
		assertEquals(r1.getAmount(), amount1);
		assertEquals(r1.getCond(), cond1);
		assertEquals(r1.getName(), resource1);
	}

	@Test
	public void testEquals() {
		assertTrue(!r1.equals(r2));
		assertEquals(r1,new Resource(resource1,amount1,cond1));
		assertTrue(!r1.equals(r3));
	}

	@Test
	public void testHash() {
		assertEquals(r1.hashCode(), ((13 + resource1.hashCode()) * 27 + amount1.hashCode()) * 35 + cond1.hashCode());
	}
}
