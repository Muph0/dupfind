package kytkaj.dupfind;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import kytkaj.dupfind.doc.FileDocument;

public class App {

    static void printUsageTo(PrintStream out) {
        out.println("Usage: dupfind <file1> <file2>");
    }

    public static void main(String[] args) {

        if (args.length != 2) {
            printUsageTo(System.err);
            return;
        }

        var file1 = new File(args[0]);
        var file2 = new File(args[1]);

        try {
            System.err.printf("Reading \"%s\"...\n", file1);
            FileDocument doc1 = FileDocument.FromFile(file1);

            System.err.printf("Reading \"%s\"...\n", file2);
            FileDocument doc2 = FileDocument.FromFile(file2);

            int dupCount = 0;
            for (var frag1 : doc1.allFragments()) {
                String text = null;

                for (var frag2 : doc2.findDuplicatesOf(frag1)) {
                    if (text == null)
                        text = frag1.getText();

                    dupCount++;
                    System.out.printf("%8d:%-8d: %s\n",
                            frag1.getOffset(),
                            frag2.getOffset(), text);
                }
            }

            System.out.printf("Total duplicates: %d\n", dupCount);

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }

    }
}
