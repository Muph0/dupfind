package kytkaj.dupfind.doc;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.PrintStream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BigFileTest {

    final String MARKER_STRING = "MARKER";

    File prepareFile(int lines, int numbersOffset, int markerCount) throws Exception {
        File file = File.createTempFile("test-", ".txt");
        int markersDown = 0;

        try (var out = new PrintStream(file)) {
            for (int l = 0; l < lines; l++) {
                boolean placeMarker = l % (lines / markerCount) == 0
                        && markersDown < markerCount;

                if (!placeMarker) {
                    out.printf("%10d\n", l + numbersOffset);
                } else {
                    out.println(MARKER_STRING);
                    markersDown++;
                }
            }
        }

        return file;
    }

    @ParameterizedTest
    @CsvSource({
            "1000, 10, 1000, 10",
            "10000, 7, 10000, 13",
            "100000, 50, 100000, 50",
    })
    public void fileDocument_shouldFindExpectedAmountOfDuplicates(
            int lines1, int markers1, int lines2, int markers2) throws Exception {

        var doc1 = FileDocument.FromFile(prepareFile(lines1, 0, markers1));
        var doc2 = FileDocument.FromFile(prepareFile(lines2, lines1, markers2));

        int dupeCount = 0;
        for (var frag : doc1.allFragments()) {
            for (var dup : doc2.findDuplicatesOf(frag)) {
                dup.getDocument();
                dupeCount++;
            }
        }

        doc1.file.delete();
        doc2.file.delete();

        assertEquals(markers1 * markers2, dupeCount);
    }

}
