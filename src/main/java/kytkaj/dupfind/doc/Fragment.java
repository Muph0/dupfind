package kytkaj.dupfind.doc;

/**
 * Represents a fragment of a document identified by a vector representation of
 * that fragment.
 */
public interface Fragment {

    /**
     * @return The document this fragment is from.
     */
    Document getDocument();

    /**
     * Get the text of the fragment. If not cached, this operation may
     * read from disk.
     *
     * @return The text of the fragment.
     */
    String getText();

    /**
     * Checks if this fragment represents exactly same fragment as {@code other}.
     * Two fragments with identical texts are not equal.
     * For text equality, use {@link #getText()}.equals()
     *
     * @return {@code true} if equal.
     */
    boolean equals(Fragment other);

    /**
     * Get hash of the vector representation of the fragment.
     * @return The hash
     */
    int getTextHash();

    /**
     * Get offset in the document.
     * @return The offset.
     */
    long getOffset();

}