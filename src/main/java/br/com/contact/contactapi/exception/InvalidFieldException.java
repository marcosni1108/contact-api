package br.com.contact.contactapi.exception;

import br.com.contact.contactapi.exception.domain.ErrorDetailIdentifier;
import br.com.contact.contactapi.exception.enums.APIErrorsEnum;

public class InvalidFieldException extends APIException {

    public InvalidFieldException( String field, String value ) {
        super( APIErrorsEnum.FIELD_INVALID, ErrorDetailIdentifier.builder()
                .type( field )
                .value( value )
                .build() );
    }
}
