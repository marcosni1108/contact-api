package br.com.contact.contactapi.mapper;

import br.com.contact.contactapi.domain.Contact;
import br.com.contact.contactapi.web.dto.ContactDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor( access = AccessLevel.PRIVATE )
public class ContactMapper {

    public static Contact toContact( ContactDTO dto ) {
        return Contact.builder()
                .name( dto.getName() )
                .documentNumber( dto.getDocumentNumber() )
                .phoneNumber( dto.getPhoneNumber() )
                .build();
    }

    public static ContactDTO toContactDTO( Contact contact ) {
        return ContactDTO.builder()
                .name( contact.getName() )
                .documentNumber( contact.getDocumentNumber() )
                .phoneNumber( contact.getPhoneNumber() )
                .build();
    }

    public static List< ContactDTO > toContactDTO( List< Contact > contacts ) {
        return contacts.stream()
                .map( c -> ContactDTO.builder()
                        .name( c.getName() )
                        .documentNumber( c.getDocumentNumber() )
                        .phoneNumber( c.getPhoneNumber() )
                        .build() )
                .collect( Collectors.toList() );
    }
}
