package fr.unice.polytech.ogl.isldc.testMap;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.unice.polytech.ogl.isldc.map.Biome;

public class TestBiome {

	private String name = "Wild";
	private String name2 = "Lake";
	private double percentage = 100.0;
	private double percentage2 = 100;
	private double percentage3 = 100.00;
	private double percentage4 = 99.9;
	private double percentage5 = 50.0;
	private Biome biome = new Biome(name,percentage);
	private Biome biome2 = new Biome(name2,percentage5);

	@Test
	public void testEquals() {
		assertTrue(!biome.equals(biome2));
		assertEquals(biome, biome);
		assertEquals(biome, new Biome(name, percentage));
		assertTrue(!(biome.equals(new Biome(name2, percentage))));
		assertTrue(!(biome.equals(new Biome(name ,percentage5))));
	}

	@Test
	public void testAlone(){
		biome = new Biome(name,percentage);
		assertTrue(biome.getAlone());
		biome = new Biome(name,percentage2);
		assertTrue(biome.getAlone());
		biome = new Biome(name,percentage3);
		assertTrue(biome.getAlone());
		biome = new Biome(name,percentage4);
		assertFalse(biome.getAlone());
	}

	@Test
	public void testHash() {
		assertEquals(biome.hashCode(), (17+name.hashCode())*32 + (int) percentage);
	}
	
	@Test
	public void testSame() {
		assertEquals(percentage2,percentage3,0);
	}
}
