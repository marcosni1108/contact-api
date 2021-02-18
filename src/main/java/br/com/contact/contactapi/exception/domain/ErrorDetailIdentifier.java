package br.com.contact.contactapi.exception.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ErrorDetailIdentifier {

    private String type;
    private String value;

}
