package data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Methods not tested in other classes.
 *
 * @author nybakk1
 */

public class GroupTest {

    @Test
    public void testGetDescription() throws Exception {
        Group group = new Group(1,"ja","desc","ARne");
        assertEquals(group.getDescription(),"desc");
    }
    @Test
    public void testSetDescription() throws Exception {
        Group group = new Group(1,"ja","Arne","Bil");
        group.setDescription("Desc");
        assertEquals(group.getDescription(),"Desc");
    }
}
