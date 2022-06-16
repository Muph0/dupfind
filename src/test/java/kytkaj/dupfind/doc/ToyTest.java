package kytkaj.dupfind.doc;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class ToyTest {

    protected String[] text1 = {
            "A grasshopper spent the summer hopping about in the sun and singing to his heart's content. One day, an ant went hurrying by, looking very hot and weary.",
            "\"Why are you working on such a lovely day?\" said the grasshopper.",
            "\"I'm collecting food for the winter,\" said the ant, \"and I suggest you do the same.\" And off she went, helping the other ants to carry food to their store. The grasshopper carried on hopping and singing. When winter came the ground was covered with snow. The grasshopper had no food and was hungry. So he went to the ants and asked for food.",
            "\"What did you do all summer when we were working to collect our food?\" said one of the ants.",
            "\"I was busy hopping and singing,\" said the grasshopper.",
            "\"Well,\" said the ant, \"if you hop and sing all summer, and do no work, then you must starve in the winter.\"",
    };

    protected String[] text2 = {
            "One day the Hare laughed at the short feet and slow speed of the Tortoise. The Tortoise replied:",
            "\"You may be as fast as the wind, but I will beat you in a race!\"",
            "The Hare thought this idea was impossible and he agreed to the proposal. It was agreed that the Fox should choose the course and decide the end.",
            "\"Why are you working on such a lovely day?\" said the grasshopper.",
            "The day for the race came, and the Tortoise and Hare started together.",
            "The Tortoise never stopped for a moment, walking slowly but steadily, right to the end of the course. The Hare ran fast and stopped to lie down for a rest. But he fell fast asleep. Eventually, he woke up and ran as fast as he could. But when he reached the end, he saw the Tortoise there already, sleeping comfortably after her effort.",
    };

    @Test
    public void arrayDocument_shouldFindDuplicates() {
        var expected = new Object[] { "\"Why are you working on such a lovely day?\" said the grasshopper." };

        var doc1 = new ArrayDocument(Arrays.asList(text1));
        var doc2 = new ArrayDocument(Arrays.asList(text2));

        var dupes = new ArrayList<String>();
        for (var frag : doc1.allFragments()) {
            for (var dupe : doc2.findDuplicatesOf(frag)) {
                dupes.add(dupe.getText());
            }
        }

        assertArrayEquals(expected, dupes.toArray());
    }

    @Test
    public void testFiles_shouldBePresent() throws Exception {

        var file = new File(
                getClass().getResource("test-A.txt").getFile());

        try (var in = new Scanner(file)) {
            assertEquals("Getting started", in.nextLine());
        }
    }

    @Test
    public void fileDocument_shouldFindDuplicates() throws Exception {
        var expected = new Object[] { "IntelliJ IDEA" };

        var testA = getClass().getResource("test-A.txt").getFile();
        var testB = getClass().getResource("test-B.txt").getFile();

        var doc1 = FileDocument.FromFile(testA);
        var doc2 = FileDocument.FromFile(testB);

        var dupes = new ArrayList<String>();
        for (var frag : doc1.allFragments()) {
            for (var dupe : doc2.findDuplicatesOf(frag)) {
                dupes.add(dupe.getText());
            }
        }

        assertArrayEquals(expected, dupes.toArray());
    }
}
