package br.com.contact.contactapi.exception.enums;

import br.com.contact.contactapi.exception.message.ApiViolationMessage;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.ResourceBundle;

@Getter
public enum APIErrorsEnum implements ApiViolationMessage {


    INVALID_CPF( "MSG001", "invalid.cpf", HttpStatus.BAD_REQUEST.value() ),
    NO_CONTENT( "MSG002", "no.content", HttpStatus.BAD_REQUEST.value() ),
    FIELD_INVALID( "MSG003", "invalid.field", HttpStatus.BAD_REQUEST.value() ),
    FIELD_REQUERID( "MSG004", "required.field", HttpStatus.BAD_REQUEST.value() ),
    INVALID_FIELD_ENUM( "MSG005", "invalid.field.enum", HttpStatus.BAD_REQUEST.value() ),
    INVALID_FIELD_TYPE( "MSG006", "invalid.field.type", HttpStatus.BAD_REQUEST.value() ),
    INVALID_FIELD_VALUE( "MSG007", "invalid.field.value", HttpStatus.BAD_REQUEST.value() ),
    INTERNAL_SERVER_ERROR( "MSG008", "internal.server.error", HttpStatus.BAD_REQUEST.value() ),
    DUPLICATED_REGISTRATION( "MSG009", "duplicated.registration", HttpStatus.BAD_REQUEST.value() ),
    ;


    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle( "ValidationMessages" );

    private final String code;
    private final String message;
    private final int httpCode;

    APIErrorsEnum( final String code, final String message, int httpCode ) {
        this.code = code;
        this.message = message;
        this.httpCode = httpCode;
    }

    public static APIErrorsEnum getByMessage( String messagekey ) {
        if( StringUtils.isEmpty( messagekey ) ) {
            return null;
        }
        for( APIErrorsEnum value : values() ) {
            if( value.message.equals( messagekey ) ) {
                return value;
            }
        }
        return null;
    }

    public String getMessage() {
        return resourceBundle.getString( message );
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return resourceBundle.getString( message );
    }

    @Override
    public String message( Object... args ) {
        return String.format( resourceBundle.getString( message ), args );
    }

    @Override
    public String messageTemplate() {
        return resourceBundle.getString( message );
    }

    @Override
    public int httpCode() {
        return httpCode;
    }
}
