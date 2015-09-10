package no.nb.microservices.catalogdeliverytext.utility;

import java.util.ArrayList;
import java.util.List;

public final class PageUtils {

    private PageUtils() {
        super();
    }

    public static List<Integer> toPageList(String pages) {

        List<Integer> arrayOfInts = new ArrayList<Integer>();

        if (pages != null && !pages.isEmpty()) {
            String[] pageArray = pages.split(",");

            for (String pageString : pageArray) {
                if (pageString.contains("-")) {
                    String[] tmpList = pageString.split("-");
                    int a = Integer.parseInt(tmpList[0]);
                    int b = Integer.parseInt(tmpList[1]);
                    for (int i = a; i <= b; i++) {
                        arrayOfInts.add(i);
                    }
                } else {
                    arrayOfInts.add(Integer.parseInt(pageString));
                }
            }
        }

        return arrayOfInts;
    }

}
