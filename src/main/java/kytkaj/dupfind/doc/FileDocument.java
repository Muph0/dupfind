package kytkaj.dupfind.doc;

import static com.google.common.collect.FluentIterable.from;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.FluentIterable;

public final class FileDocument implements Document {

    final Map<Integer, ArrayList<DocFragment>> data = new HashMap<>();
    public final File file;

    public FileDocument(FileTokenizer tokenizer) {
        this.file = tokenizer.getFile();
        for (Token token : tokenizer) {
            this.put(token);
        }
    }

    public static FileDocument FromFile(String pathname) throws FileNotFoundException {
        return FromFile(new File(pathname));
    }
    public static FileDocument FromFile(File file) throws FileNotFoundException {
        try (var tokenizer = new LineTokenizer(file)) {
            return new FileDocument(tokenizer);
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void put(Token token) {
        var frag = new DocFragment(token.offset(), token.text().hashCode());

        var list = data.get(frag.textHash);
        if (list == null) {
            list = new ArrayList<>();
            data.put(frag.textHash, list);
        }

        list.add(frag);
    }

    @Override
    public Iterable<Fragment> findDuplicatesOf(Fragment fragment) {
        String text = fragment.getText();
        return internalDuplicates(fragment).filter(fr -> fr.getText().equals(text));
    }

    @Override
    public Iterable<Fragment> findDuplicateCandidatesOf(Fragment fragment) {
        return internalDuplicates(fragment);
    }

    private FluentIterable<Fragment> internalDuplicates(Fragment frag) {
        var values = data.get(frag.getTextHash());
        if (values != null) {
            return from(values).transform(fr -> fr);
        } else {
            return from(List.of());
        }
    }

    @Override
    public Iterable<Fragment> allFragments() {
        return from(data.values()).transformAndConcat(fr -> fr);
    }

    class DocFragment implements Fragment {

        public final long offset;
        public final int textHash;

        public DocFragment(long offset, int hash) {
            this.offset = offset;
            this.textHash = hash;
        }

        @Override
        public Document getDocument() {
            return FileDocument.this;
        }

        @Override
        public String getText() {
            try (var in = new RandomAccessFile(file, "r")) {
                in.seek(offset);
                var bytes = readUntil(in, '\n');
                return bytes.toString("UTF-8").stripTrailing();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public boolean equals(Fragment other) {
            if (this == other) {
                return true;
            } else if (other instanceof DocFragment frag) {
                return getDocument() == frag.getDocument()
                        && offset == frag.offset;
            }
            return false;
        }

        @Override
        public int getTextHash() {
            return textHash;
        }

        @Override
        public String toString() {
            return String.format(
                    "FileDocument.Fragment[hash=%08x, offset=%d, text=\"%s\"]",
                    this.textHash,
                    this.offset,
                    this.getText());
        }

        @Override
        public long getOffset() {
            return this.offset;
        }
    }

    private ByteArrayOutputStream readUntil(RandomAccessFile in, int stop) throws IOException {
        var result = new ByteArrayOutputStream();
        while (true) {
            int b = in.read();
            if (b == stop || b == -1) {
                break;
            }
            result.write(b);
        }
        return result;
    }
}
