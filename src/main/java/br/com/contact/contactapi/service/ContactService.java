package br.com.contact.contactapi.service;

import br.com.contact.contactapi.domain.Contact;
import br.com.contact.contactapi.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository repository;

    public Contact saveContact( Contact contact ) {
        return repository.save( contact );
    }

    public List< Contact > findContacts( Contact filter ) {
        return Optional.ofNullable( repository.findContacts( filter ) ).orElseGet( Collections :: emptyList );
    }

    public void deleteContactByDocumentNumber( String documentNumber ) {
        repository.deleteByDocumentNumber( documentNumber );
    }

    public Contact updateContactByDocumentNumber( String documentNumber, Contact contact ) {
        repository.findByDocumentNumber( documentNumber ).ifPresent( c -> repository.save( contact ) );
        return contact;
    }
}
