package br.com.contact.contactapi.exception.handlers;

import br.com.contact.contactapi.exception.APIException;
import br.com.contact.contactapi.exception.NoContentException;
import br.com.contact.contactapi.exception.domain.ErrorResponse;
import br.com.contact.contactapi.exception.enums.APIErrorsEnum;
import br.com.contact.contactapi.exception.message.ApiViolationMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity< Object > handleMethodArgumentNotValid( MethodArgumentNotValidException ex,
                                                                     HttpHeaders headers, HttpStatus status,
                                                                     WebRequest request ) {
        log.debug( "[GlobalExceptionHandler.handleMethodArgumentNotValid] " + ex.getMessage() );
        final List< ErrorResponse > errorList = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map( ErrorResponse :: adapt )
                .collect( Collectors.toList() );
        return new ResponseEntity<>( errorList, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler( APIException.class )
    public ResponseEntity< Object > handlerAPIException( APIException exception ) {
        log.warn( "[GlobalExceptionHandler.handlerAPIException] " + exception.getMessage(), exception );
        ApiViolationMessage apiViolationMessage = exception.getApiViolationMessage();
        final ErrorResponse errorResponse = new ErrorResponse( apiViolationMessage.code(), apiViolationMessage.message( exception.getArgs() ), exception.getDetail() );
        return ResponseEntity.status( apiViolationMessage.httpCode() )
                .body( errorResponse );
    }

    @ResponseStatus( HttpStatus.NO_CONTENT )
    @ExceptionHandler( NoContentException.class )
    public void handlerNoContentException( WebRequest request ) {
        log.debug( "[GlobalExceptionHandler.handlerNoContentException] " + request.getContextPath() );
    }

    @Override
    protected ResponseEntity< Object > handleBindException( BindException exception, HttpHeaders headers, HttpStatus status, WebRequest request ) {
        log.debug( "[GlobalExceptionHandler.handlerBindException] " + exception.getMessage(), exception );
        final List< ErrorResponse > errorList = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map( ErrorResponse :: adapt )
                .collect( Collectors.toList() );
        return new ResponseEntity<>( errorList, HttpStatus.BAD_REQUEST );
    }

    @Override
    protected ResponseEntity< Object > handleExceptionInternal( Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request ) {

        logger.error( "[handleExceptionInternal]" + ex.getMessage(), ex );

        if( HttpStatus.INTERNAL_SERVER_ERROR.equals( status ) ) {
            request.setAttribute( WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST );
        }

        return new ResponseEntity<>( createInternalServerErrorResponse(), headers, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    private static ErrorResponse createInternalServerErrorResponse() {
        return new ErrorResponse(
                APIErrorsEnum.INTERNAL_SERVER_ERROR.getCode(),
                APIErrorsEnum.INTERNAL_SERVER_ERROR.getMessage()
        );
    }

}
