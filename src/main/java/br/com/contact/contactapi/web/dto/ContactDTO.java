package br.com.contact.contactapi.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ContactDTO {

    @NotNull( message = "{required.field}" )
    private String name;

    @CPF( message = "{invalid.cpf}" )
    @NotNull( message = "{required.field}" )
    private String documentNumber;

    @NotNull( message = "{required.field}" )
    private String phoneNumber;

}
