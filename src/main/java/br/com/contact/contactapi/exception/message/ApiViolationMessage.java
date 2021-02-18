package br.com.contact.contactapi.exception.message;

/**
 * Interface to represent a criticism launched by the API.
 *
 * @author fanderson
 * @since 2.39.0
 */
public interface ApiViolationMessage {

    /**
     * The code of violation
     *
     * @return code of violation
     */
    String code();

    /**
     * User friendly message
     *
     * @return User friendly message
     */
    String message();

    /**
     * User friendly message with parameters.
     *
     * @param args args for message interpolation
     * @return User friendly message
     */
    String message( Object... args );

    /**
     * User-friendly message template
     *
     * @return User-friendly message template
     */
    String messageTemplate();

    /**
     * Custom http code for message
     *
     * @return Custom http code for message
     */
    int httpCode();

    /**
     * Return if is required args for message interpolation
     *
     * @return if is required args for message interpolation
     */
//    boolean requireArgsForMessage();

    /**
     *
     * Return type of args
     *
     * @return type of args
     */
//    Collection<Class<?>> argsTypes();

}
