package br.com.contact.contactapi.service;

import br.com.contact.contactapi.annotations.LogExecution;
import br.com.contact.contactapi.domain.Contact;
import br.com.contact.contactapi.exception.APIException;
import br.com.contact.contactapi.exception.NoContentException;
import br.com.contact.contactapi.exception.RequiredFieldException;
import br.com.contact.contactapi.exception.enums.APIErrorsEnum;
import br.com.contact.contactapi.repository.ContactRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
public class ContactService {

    @Autowired
    private ContactRepository repository;

    @LogExecution
    public Contact saveContact( Contact contact ) {
        if( repository.findByDocumentNumber( contact.getDocumentNumber() ).isPresent() ) {
            throw new APIException( APIErrorsEnum.DUPLICATED_REGISTRATION );
        }
        return repository.save( contact );
    }

    @LogExecution
    public List< Contact > findContacts( Contact filter ) {
        return ofNullable( repository.findContacts( filter ) ).orElseGet( Collections :: emptyList );
    }

    @LogExecution
    public void deleteContactByDocumentNumber( String documentNumber ) {
        repository.deleteByDocumentNumber( documentNumber );
    }

    @LogExecution
    public Contact updateContactByDocumentNumber( String documentNumber, Contact updatedContact ) {
        if( StringUtils.isBlank( updatedContact.getName() ) ) {
            throw new RequiredFieldException( "name" );
        }

        if( StringUtils.isBlank( updatedContact.getPhoneNumber() ) ) {
            throw new RequiredFieldException( "phoneNumber" );
        }

        Contact contact = repository.findByDocumentNumber( documentNumber )
                .orElseThrow( NoContentException :: new );

        return repository.save( Contact.builder()
                .id( contact.getId() )
                .documentNumber( contact.getDocumentNumber() )
                .name( updatedContact.getName() )
                .phoneNumber( updatedContact.getPhoneNumber() )
                .build() );
    }
}
