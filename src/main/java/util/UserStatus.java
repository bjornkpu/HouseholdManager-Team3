package util;

/**
 *
 * Class for keeping track of user invite statuses in database.
 *
 * @author nybakk1
 * @version 0.1
 *
 */

public class UserStatus {

    /**
     * User is pending authorization to join the group he wishes to access.
     *
     */

    private final static int PENDING = 0;

    /**
     * The user is a member of the group
     *
     */

    private final static int MEMBER = 1;

    /**
     * The user is a member of the group and the admin of the group.
     *
     */

    private final static int ADMIN = 2;

    /**
     * The user account is a member of the group but have had his access disabled.
     */

    private final static int DISABLED = 3;

    /**
     * The user is banned from the group.
     *
     */

    private final static int BANNED = -1;
}
