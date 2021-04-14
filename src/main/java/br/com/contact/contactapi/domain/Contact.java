package br.com.contact.contactapi.domain;

import br.com.contact.contactapi.web.dto.ContactDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Optional;

@Data
@Builder
@Entity
@Table( name = "CONTACT" )
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "ID" )
    private Long id;

    @Column( name = "NAME" )
    private String name;

    @Column( name = "DOCUMENT_NUMBER" )
    private String documentNumber;

    @Column( name = "PHONE_NUMBER" )
    private String phoneNumber;

    public Contact( ContactDTO contactDTO ) {
        Optional.ofNullable( contactDTO )
                .ifPresent( c -> {
                    setDocumentNumber( c.getDocumentNumber() );
                    setName( c.getName() );
                    setPhoneNumber( c.getPhoneNumber() );
                } );
    }
}
