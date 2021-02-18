package br.com.contact.contactapi.exception.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ErrorDetail {

    private String code;
    private String text;
    private List< ErrorDetailIdentifier > identifier;

}
