package kytkaj.dupfind.doc;

import static com.google.common.collect.FluentIterable.from;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.FluentIterable;

/**
 * Implementation of {@link Document} which stores all fragments in an array.
 */
public final class ArrayDocument implements Document {

    final ArrayList<String> fragmentData;

    public ArrayDocument(Iterable<String> fromFragments) {
        this.fragmentData = new ArrayList<>();
        fromFragments.forEach(this.fragmentData::add);
    }

    public static ArrayDocument FromFile(String pathname) {
        return FromFile(new File(pathname));
    }

    public static ArrayDocument FromFile(File file) {
        try (var in = new Scanner(file)) {
            var result = new ArrayDocument(new ArrayList<String>());

            while (in.hasNextLine()) {
                var line = in.nextLine();
                if (line.length() != 0) {
                    result.fragmentData.add(line);
                }
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Fragment> findDuplicateCandidatesOf(Fragment fragment) {
        return internalDuplicatesOf(fragment);
    }

    @Override
    public Iterable<Fragment> findDuplicatesOf(Fragment fragment) {
        String text = fragment.getText();
        return internalDuplicatesOf(fragment).filter(fr -> fr.getText().equals(text));
    }

    private FluentIterable<Fragment> internalDuplicatesOf(Fragment frag) {
        return from(allFragments())
                .filter(f -> f.getTextHash() == frag.getTextHash());
    }

    @Override
    public Iterable<Fragment> allFragments() {
        return from(ContiguousSet.closedOpen(0, fragmentData.size()))
                .transform(i -> {
                    var text = fragmentData.get(i);
                    return new DocFragment(i, text.hashCode(), text);
                });
    }

    private class DocFragment implements Fragment {

        public final int index;
        public final int vector;
        private final String text;

        public DocFragment(int index, int vector, String text) {
            this.index = index;
            this.vector = vector;
            this.text = text;
        }

        @Override
        public Document getDocument() {
            return ArrayDocument.this;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public boolean equals(Fragment other) {
            if (other instanceof DocFragment frag) {
                return this == frag || (getDocument() == frag.getDocument()
                        && index == frag.index);
            }
            return false;
        }

        @Override
        public int getTextHash() {
            return vector;
        }

        @Override
        public long getOffset() {
            return this.index;
        }
    }
}
