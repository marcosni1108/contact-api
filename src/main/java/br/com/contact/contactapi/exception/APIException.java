package br.com.contact.contactapi.exception;

import br.com.contact.contactapi.exception.enums.APIErrorsEnum;
import br.com.contact.contactapi.exception.message.ApiViolationMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class APIException extends RuntimeException {

    private final transient Object detail;
    private final transient ApiViolationMessage apiViolationMessage;
    private final transient Object[] args;

    public APIException( final APIErrorsEnum apiErrorsEnum ) {
        this( apiErrorsEnum, null, null );
    }

    public APIException( final APIErrorsEnum apiErrorsEnum, final Object detail ) {
        this( apiErrorsEnum, detail, null );
    }

    public APIException( final APIErrorsEnum apiErrorsEnum, final Object detail, Throwable throwable ) {
        this( ( ApiViolationMessage ) apiErrorsEnum, detail, throwable );
    }

    @Builder
    public APIException( @NonNull final ApiViolationMessage apiViolationMessage, final Object detail, final Throwable throwable, final Object... args ) {
        super( apiViolationMessage.message( args ), throwable );
        this.apiViolationMessage = apiViolationMessage;
        this.detail = detail;
        this.args = args;
    }

}
