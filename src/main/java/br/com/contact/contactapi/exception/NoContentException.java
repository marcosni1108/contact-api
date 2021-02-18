package br.com.contact.contactapi.exception;

import br.com.contact.contactapi.exception.enums.APIErrorsEnum;

public class NoContentException extends APIException {

    public NoContentException() {
        super( APIErrorsEnum.NO_CONTENT );
    }

}
