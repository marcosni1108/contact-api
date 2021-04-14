package br.com.contact.contactapi.web.dto;

import br.com.contact.contactapi.domain.Contact;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude( JsonInclude.Include.NON_NULL )
public class ContactDTO {

    @NotBlank( message = "{required.field}" )
    private String name;

    @CPF( message = "{invalid.cpf}" )
    @NotNull( message = "{required.field}" )
    private String documentNumber;

    @NotNull( message = "{required.field}" )
    private String phoneNumber;

    public ContactDTO( Contact contact ) {
        Optional.ofNullable( contact )
                .ifPresent( c -> {
                    setDocumentNumber( c.getDocumentNumber() );
                    setName( c.getName() );
                    setPhoneNumber( c.getPhoneNumber() );
                } );
    }

}
