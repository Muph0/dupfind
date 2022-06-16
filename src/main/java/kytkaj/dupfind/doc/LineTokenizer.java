package kytkaj.dupfind.doc;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of {@link FileTokenizer} which produces an iterator over
 * the lines of a file as {@link Token}s.
 */
public class LineTokenizer implements FileTokenizer, Closeable {

    private final FileInputStream stream;
    private final File file;
    private boolean closed = false;

    public LineTokenizer(File file) throws FileNotFoundException {
        this.file = file;
        this.stream = new FileInputStream(file);
    }

    public LineTokenizer(String filename) throws FileNotFoundException {
        this(new File(filename));
    }

    @Override
    public Iterator<Token> iterator() {
        return new Iterator<Token>() {
            private boolean hasCached, eof = false;
            private Token cached;
            long offset = 0;

            @Override
            public boolean hasNext() {
                if (hasCached || closed || eof)
                    return hasCached;

                try {
                    var sb = new ByteArrayOutputStream();
                    long tokenStart = offset;

                    do {
                        offset++;
                        int ch = stream.read();

                        switch (ch) {
                            case '\r':
                                if (sb.size() == 0)
                                    tokenStart++;
                                break;

                            case -1:
                                eof = true;
                            case '\n':
                                var text = sb.toString("UTF-8").stripTrailing();
                                if (text.length() > 0) {
                                    cached = new Token(tokenStart, text);
                                    hasCached = true;
                                } else {
                                    tokenStart = offset;
                                }
                                sb.flush();
                                break;

                            default:
                                sb.write(ch);
                                break;
                        }
                    } while (!hasCached && !eof);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return hasCached;
            }

            @Override
            public Token next() {
                if (hasNext()) {
                    hasCached = false;
                    return cached;
                }
                throw new NoSuchElementException();
            }

        };
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void close() throws IOException {
        closed = true;
        stream.close();
    }

}
