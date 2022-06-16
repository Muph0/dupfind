package kytkaj.dupfind.doc;

/**
 * Represents a text document which is internally represented as a collection
 * of fragments.
 */
public interface Document {

    /**
     * Find all fragments which have text with equal hashCode as {@code fragment}.
     *
     * @return Iterable of duplicates. If none, result is empty.
     */
    Iterable<Fragment> findDuplicateCandidatesOf(Fragment fragment);

    /**
     * Find all fragments which have the same text as {@code fragment}.
     *
     * @return Iterable of duplicates. If none, result is empty.
     */
    Iterable<Fragment> findDuplicatesOf(Fragment fragment);

    /**
     * Create an iterable that iterates over all fragments.
     *
     * @return The iterable.
     */
    Iterable<Fragment> allFragments();

}
