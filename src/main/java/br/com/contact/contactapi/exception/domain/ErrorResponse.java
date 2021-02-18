package br.com.contact.contactapi.exception.domain;

import br.com.contact.contactapi.exception.enums.APIErrorsEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ErrorResponse {

    private String code;
    private String message;
    private Object detail;

    public ErrorResponse( String code, String message ) {
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse adapt( ConstraintViolation constraintViolation ) {
        String messageTemplate = constraintViolation.getMessageTemplate().replace( "{", "" ).replace( "}", "" );
        APIErrorsEnum apiEnum = APIErrorsEnum.getByMessage( messageTemplate );
        return apiEnum != null ? new ErrorResponse( apiEnum.getCode(), apiEnum.getMessage(), adaptDetail( constraintViolation )
        ) : null;

    }

    public static ErrorResponse adapt( TypeMismatchException constraintViolation ) {

        Class< ? > requiredType = constraintViolation.getRequiredType();
        String requiredTypeName = Optional.ofNullable( requiredType )
                .map( Class :: getSimpleName )
                .orElse( null );

        if( requiredType != null && requiredType.isEnum() ) {
            return new ErrorResponse(
                    APIErrorsEnum.INVALID_FIELD_ENUM.getCode(),
                    APIErrorsEnum.INVALID_FIELD_ENUM.getMessage(
                            constraintViolation.getPropertyName(),
                            String.valueOf( constraintViolation.getValue() ) ),
                    ErrorDetailIdentifier.builder()
                            .type( constraintViolation.getPropertyName() )
                            .value( String.valueOf( constraintViolation.getValue() ) )
                            .build()
            );
        }

        return new ErrorResponse(
                APIErrorsEnum.INVALID_FIELD_TYPE.getCode(),
                APIErrorsEnum.INVALID_FIELD_TYPE.getMessage(
                        constraintViolation.getPropertyName(),
                        String.valueOf( constraintViolation.getValue() ),
                        requiredTypeName ),
                ErrorDetailIdentifier.builder()
                        .type( constraintViolation.getPropertyName() )
                        .value( String.valueOf( constraintViolation.getValue() ) )
                        .build()
        );
    }

    public static ErrorResponse adapt( FieldError fieldError ) {
        return new ErrorResponse(
                APIErrorsEnum.INVALID_FIELD_VALUE.getCode(),
                APIErrorsEnum.INVALID_FIELD_VALUE.getMessage(
                        fieldError.getField(),
                        String.valueOf( fieldError.getRejectedValue() )
                ),
                ErrorDetailIdentifier.builder()
                        .type( fieldError.getField() )
                        .value( String.valueOf( fieldError.getRejectedValue() ) )
                        .build()
        );
    }


    private static ErrorDetailIdentifier adaptDetail( ConstraintViolation constraintViolation ) {
        String type = null;
        String value = null;
        if( constraintViolation.getPropertyPath() != null ) {
            type = constraintViolation.getPropertyPath()
                    .toString();
        }
        if( constraintViolation.getInvalidValue() != null ) {
            value = constraintViolation.getInvalidValue()
                    .toString();
        }
        return ErrorDetailIdentifier.builder()
                .type( type )
                .value( value )
                .build();
    }

    public static ErrorResponse adapt( ObjectError objectError ) {

        if( objectError.contains( ConstraintViolation.class ) ) {
            return adapt( objectError.unwrap( ConstraintViolation.class ) );
        }

        if( objectError.contains( TypeMismatchException.class ) ) {
            return adapt( objectError.unwrap( TypeMismatchException.class ) );
        }

        if( objectError instanceof FieldError ) {
            FieldError fieldError = ( FieldError ) objectError;
            return adapt( fieldError );
        }

        return new ErrorResponse(
                APIErrorsEnum.FIELD_INVALID.getCode(),
                APIErrorsEnum.FIELD_INVALID.getMessage(),
                ErrorDetailIdentifier.builder()
                        .type( "detailMessage" )
                        .value( objectError.getDefaultMessage() )
                        .build()
        );

    }

}
