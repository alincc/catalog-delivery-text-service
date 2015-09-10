package no.nb.microservices.catalogdeliverytext.utility;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PageUtilsTest {


    @Test
    public void toPageListTest() {
        String pages = "1-3,5-7,12";
        List<Integer> expected = Arrays.asList(1,2,3,5,6,7,12);
        List<Integer> result = PageUtils.toPageList(pages);

        assertNotNull(result);
        assertEquals(expected,result);
    }

    @Test
    public void whenPagesIsEmptyEmptyListShouldBeReturned() {
        String pages = "";
        List<Integer> result = PageUtils.toPageList(pages);
        assertNotNull(result);
        assertEquals(0,result.size());
    }

    @Test
    public void whenPagesIsNullEmptyListShouldBeReturned() {
        String pages = null;
        List<Integer> result = PageUtils.toPageList(pages);
        assertNotNull(result);
        assertEquals(0,result.size());
    }
}
