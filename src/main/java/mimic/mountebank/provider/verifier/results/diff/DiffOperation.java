package mimic.mountebank.provider.verifier.results.diff;

import java.util.Arrays;

public enum DiffOperation {

    EQUAL(new String[]{"EQUAL"}),

    /**
     * The "remove" operation removes the value at the target location.
     * The target location MUST exist for the operation to be successful.
     *
     *    For example:
     *    { "op": "remove", "path": "/a/b/c" }
     *
     * If removing an element from an array, any elements above the
     * specified index are shifted one position to the left.
     *
     * @see <a href="https://tools.ietf.org/html/rfc6902" target="_blank">https://tools.ietf.org/html/rfc6902</a>
     */
    REMOVE(new String[]{"REMOVE", "DELETE"}),

    /**
     * The operation object MUST contain a "value" member whose content
     * specifies the value to be added.
     *
     *    For example:
     *    { "op": "add", "path": "/a/b/c", "value": [ "foo", "bar" ] }
     */
    ADD(new String[]{"ADD", "INSERT"}),

    /**
     * The "replace" operation replaces the value at the target location
     * with a new value.  The operation object MUST contain a "value" member
     * whose content specifies the replacement value.
     * The target location MUST exist for the operation to be successful.
     *
     *    For example:
     *    { "op": "replace", "path": "/a/b/c", "value": 42 }
     *
     * This operation is functionally identical to a "remove" operation for
     * a value, followed immediately by an "add" operation at the same
     * location with the replacement value.
     */
    REPLACE(new String[]{"REPLACE"}),

    /**
     * The "copy" operation copies the value at a specified location to the
     * target location.
     * The operation object MUST contain a "from" member, which is a string
     * containing a JSON Pointer value that references the location in the
     * target document to copy the value from.
     * The "from" location MUST exist for the operation to be successful.
     *
     *    For example:
     *    { "op": "copy", "from": "/a/b/c", "path": "/a/b/e" }
     *
     * This operation is functionally identical to an "add" operation at the
     * target location using the value specified in the "from" member
     */
    COPY(new String[]{"COPY"}),

    /**
     * The "move" operation removes the value at a specified location and
     * adds it to the target location.
     * The operation object MUST contain a "from" member, which is a string
     * containing a JSON Pointer value that references the location in the
     * target document to move the value from.
     * The "from" location MUST exist for the operation to be successful.
     *
     *    For example:
     *    { "op": "move", "from": "/a/b/c", "path": "/a/b/d" }
     *
     * This operation is functionally identical to a "remove" operation on
     * the "from" location, followed immediately by an "add" operation at
     * the target location with the value that was just removed.
     */
    MOVE(new String[]{"MOVE"});

    private String[] ops;

    DiffOperation(String[] ops) {
        this.ops = ops;
    }

    public static DiffOperation getEnum(String operation) {
        String opName = operation.trim();

        DiffOperation op = Arrays.stream(values())
                .filter(v -> Arrays.stream(v.ops).anyMatch(o ->  o.equalsIgnoreCase(opName)))
                .findFirst().get();

        return op;
    }
}
