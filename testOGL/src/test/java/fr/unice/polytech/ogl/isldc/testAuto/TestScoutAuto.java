package fr.unice.polytech.ogl.isldc.testAuto;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fr.unice.polytech.ogl.isldc.automate.Auto;
import fr.unice.polytech.ogl.isldc.automate.ScoutAuto;
import fr.unice.polytech.ogl.isldc.map.IslandTile;
import fr.unice.polytech.ogl.isldc.map.Resource;

public class TestScoutAuto {
	ScoutAuto ai = new ScoutAuto(new Auto());
	private int cost = 42;
	private String creek_id = "0000-4444-5655-ercc";
	private int budget = 3459;
	private int men = 5343;
	private int altitude = -23;
	private String resource1 = "WOOD";
	private String resource2 = "FLOWER";
	private String resource3 = "FUR";
	private int amount1 = 5343;
	private int amount2 = 234;
	private String init = "{    \"creek\": \"" + creek_id + "\", \"budget\": "
			+ budget + ", \"men\": " + men
			+ ", \"objective\": [ { \"resource\": \"" + resource1
			+ "\", \"amount\":" + amount1 + "}, { \"resource\": \""
			+ resource2 + "\", \"amount\":" + amount2 + "} ] }";

	/**
	 * gere les scouts, dans l'ordre du tableau de directions
	 */
	@Test
	public void testScoutTile() {
		ai.getAI()
				.start("{\"creek\": \"creek_id\", \"budget\": 600, \"men\": 50, \"objective\": [ { \"resource\": \"WOOD\", \"amount\": 600 } ] }");
		try {
			assertNotNull(ai.getAI().getMap().getCase(0, 0));
			int size = Auto.ALL_DIRECTION.length;
			char scout = Auto.ALL_DIRECTION[size - 1];
			for (int i = 0; i < size; i++) {
				scout = ai.nextScoutTile();
				// order of nextScoutTile can change, but normally it is in that order
				assertEquals(scout, Auto.ALL_DIRECTION[i]);
				ai.getAI().setDirection(scout);
				assertEquals((String) ai.actionScout(scout),
						"{\"action\": \"scout\", \"parameters\": {\"direction\": \""
								+ scout + "\" } }");
				assertEquals(ai.getAI().getPrevAction(), "scout");
				ai.getAI()
						.actionResult(
								"{\"status\": \"OK\", \"cost\": 50, \"extras\": { \"resources\": [\"WOOD\", \"FUR\", \"FLOWER\"], \"altitude\": "
										+ (10 + i) + "}}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getStackTrace().toString());
		}

	}

	/**
	 * Test the result of a a scout
	 */
	@Test
	public void testResultScout() {
		String prevAction = "scout";
		String action = "{\"status\":\"OK\",\"cost\":" + cost
				+ ", \"extras\": { \"resources\":[\"" + resource1 + "\", \""
				+ resource2 + "\", \"" + resource3 + "\"], \"altitude\":"
				+ altitude + "}}";
		IslandTile tile2 = new IslandTile(altitude, true);
		ai.getAI().start(init);
		ai.getAI().setDirection('N');
		ai.getAI().setPrevAction(prevAction);
		ai.getAI().actionResult(action);
		assertEquals("OK", ai.getAI().getStatus());
		assertEquals(budget - cost, ai.getAI().getBudget());
		IslandTile tile = ai
				.getAI()
				.getMap()
				.getCase(ai.getAI().getX(),
						ai.getAI().switchY(ai.getAI().getY(), 'N', 1));
		assertEquals(altitude, tile.getAltitude());
		List<Resource> res = tile.getResources();
		List<String> resString = new ArrayList<String>();

	}
}
