package br.com.contact.contactapi.exception;

import br.com.contact.contactapi.exception.domain.ErrorDetailIdentifier;
import br.com.contact.contactapi.exception.enums.APIErrorsEnum;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RequiredFieldException extends APIException {

    public RequiredFieldException( String... fields ) {
        super( APIErrorsEnum.FIELD_REQUERID, Arrays.asList( fields ).stream()
                .map( field -> ErrorDetailIdentifier.builder()
                        .type( field ).build() )
                .collect( Collectors.toList() ) );
    }
}
