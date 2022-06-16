package kytkaj.dupfind.doc;

import java.io.File;

/**
 * Iterable over a collection of {@link Token}s associated with a {@link File}.
 */
public interface FileTokenizer extends Iterable<Token> {

    File getFile();
}
