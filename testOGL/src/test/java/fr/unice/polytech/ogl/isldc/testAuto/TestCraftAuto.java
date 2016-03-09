package fr.unice.polytech.ogl.isldc.testAuto;
import fr.unice.polytech.ogl.isldc.automate.Auto;

import fr.unice.polytech.ogl.isldc.automate.CraftAuto;
import fr.unice.polytech.ogl.isldc.automate.ExploreAuto;
import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Test;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static fr.unice.polytech.ogl.isldc.automate.CraftAuto.bestAmount;
import static fr.unice.polytech.ogl.isldc.automate.CraftAuto.craft;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class will test the Craft Class
 * Created by guillaume on 22/05/15.
 */
public class TestCraftAuto {
    private Auto auto =new Auto();
    private ExploreAuto explorer = new ExploreAuto(auto);
    @Test
    /**
     * This method will test the craft without resources
     */
    public void testVoidCraft(){
        String tmp=craft(auto);
        assertEquals(tmp, "");
    }

    @Test
    /**
     * This method will test the craft of an ingot
     */
    public void testCraftIngot(){
        explorer.getAI().start("{\"creek\": \"creek_id\", \"budget\": 600, \"men\": 50, \"objective\": [ { \"resource\": \"INGOT\", \"amount\": 2 } ] }");
        Map<String, Integer> tmp=new HashMap();
        tmp.put("WOOD", 25);
        tmp.put("ORE", 25);
        tmp.put("INGOT", 2);
        auto.setResourceHave(tmp);
        String tmpS=craft(auto);
        assertEquals(tmpS, "{   \"action\": \"transform\", \"parameters\": { \"WOOD\": 10, \"ORE\": 10 } }");
    }

    @Test
    /**
     * This method will test the craft of a glass
     */
    public void testCraftGlass(){
        explorer.getAI().start("{\"creek\": \"creek_id\", \"budget\": 600, \"men\": 50, \"objective\": [ { \"resource\": \"GLASS\", \"amount\": 1 } ] }");
        Map<String, Integer> tmp=new HashMap();
        tmp.put("WOOD", 5);
        tmp.put("QUARTZ", 10);
        tmp.put("GLASS", 1);
        auto.setResourceHave(tmp);
        String tmpS=craft(auto);
        assertEquals(tmpS, "{   \"action\": \"transform\", \"parameters\": { \"WOOD\": 5, \"QUARTZ\": 10 } }");
    }

    @Test
    /**
     *This method will test the craft of the leather
     */
    public void testCraftLeather(){
        explorer.getAI().start("{\"creek\": \"creek_id\", \"budget\": 600, \"men\": 50, \"objective\": [ { \"resource\": \"LEATHER\", \"amount\": 50 } ] }");
        Map<String, Integer> tmp=new HashMap();
        tmp.put("FUR", 50);
        tmp.put("LEATHER", 50);
        auto.setResourceHave(tmp);
        String tmpS=craft(auto);
        assertEquals(tmpS, "{   \"action\": \"transform\", \"parameters\": { \"FUR\": 48 } }");
    }

    @Test
    /**
     *This method will test the craft of the plank
     */
    public void testCraftPlank(){
        explorer.getAI().start("{\"creek\": \"creek_id\", \"budget\": 600, \"men\": 50, \"objective\": [ { \"resource\": \"PLANK\", \"amount\": 500 } ] }");
        Map<String, Integer> tmp=new HashMap();
        tmp.put("WOOD", 15000);
        tmp.put("PLANK", 500);
        auto.setResourceHave(tmp);
        String tmpS=craft(auto);
        assertEquals(tmpS, "{   \"action\": \"transform\", \"parameters\": { \"WOOD\": 125 } }");
        tmpS=craft(auto);
        assertEquals("", tmpS);
    }

    /**
     * This method will test craft of plank and craft of glass.
     *
     * @throws Exception
     */
    @Test
    public void testCrafts()throws Exception{
        explorer.getAI().start("{\"creek\": \"creek_id\", \"budget\": 600, \"men\": 50, \"objective\": [ { \"resource\": \"PLANK\", \"amount\": 500 }, { \"resource\": \"GLASS\", \"amount\": 1 } ] } ] }");
        Map<String, Integer> tmp=new HashMap();
        tmp.put("WOOD", 15000);
        tmp.put("PLANK", 500);
        tmp.put("WOOD", 5);
        tmp.put("QUARTZ", 10);
        tmp.put("GLASS", 1);
        auto.setResourceHave(tmp);
        JSONObject tmpJson;
        String tmpS = craft(auto);
        boolean firstPlank = true, firstGlass = true;
        while (tmpS != "") {
            tmpJson = new JSONObject(tmpS);
            assertEquals(tmpJson.getString("action"), "transform");
            if("{\"WOOD\":5}".equals(tmpJson.getString("parameters")) && firstPlank){
                firstPlank = false;
            }else if("{\"WOOD\":125}".equals(tmpJson.getString("parameters")) && firstGlass){
                firstGlass = false;
            }else{
                fail("not a good choice: firstPlank= \" + firstPlank + \" firstGlass= \" + firstGlass\n" + tmpJson.getString("parameters"));
            }
            tmpS = craft(auto);
        }
        if (firstPlank && firstGlass)
            fail("not enough craft done: firstPlank= " + firstPlank + " firstGlass= " + firstGlass);
    }

    @Test
    public void testBestAmount(){
        int size = 1, bestAmount = 0;
        int[] currentAmount = new int[size], rate = new int[size + 1];
        currentAmount[0] = 50;
        rate[0] = 2;
        rate[1] = 3;
        int[] result = CraftAuto.bestAmount(bestAmount, currentAmount, rate);
        // first a error case:
        for(int i : result)
            assertEquals(0, i);

        // then a correct result:
        bestAmount = 5;
        result = CraftAuto.bestAmount(bestAmount, currentAmount, rate);
        assertEquals(2 * rate[0], result[0]); // two is the number of craft
        assertEquals(2 * rate[1], result[size]);

        // after that, a limited result:
        bestAmount = 80;
        result = CraftAuto.bestAmount(bestAmount, currentAmount, rate);
        assertEquals(25 * rate[0], result[0]); // 25 is the number of craft
        assertEquals(25 * rate[1], result[size]);

        // and a more complicated case:
        size = 3; bestAmount = 10;
        currentAmount = new int[size]; rate = new int[size + 1];
        currentAmount[0] = 50;
        currentAmount[1] = 50;
        currentAmount[2] = 50;
        rate[0] = 1;
        rate[1] = 2;
        rate[2] = 5;
        rate[3] = 6;
        result = CraftAuto.bestAmount(bestAmount, currentAmount, rate);
        for(int i = 0; i <= size; i++)
            assertEquals(2 * rate[i], result[i]);

    }
}
