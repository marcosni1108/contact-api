package br.com.contact.contactapi.web;

import br.com.contact.contactapi.mapper.ContactMapper;
import br.com.contact.contactapi.service.ContactService;
import br.com.contact.contactapi.web.dto.ContactDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping( "contacts" )
public class ContactController {

    @Autowired
    private ContactService service;

    @PostMapping
    public ResponseEntity< ContactDTO > saveContact( @RequestBody @Valid ContactDTO contact ) {

        ContactDTO contactDTO = ContactMapper.toContactDTO( service.saveContact( ContactMapper.toContact( contact ) ) );
        return new ResponseEntity<>( contactDTO, HttpStatus.CREATED );
    }

    @GetMapping
    public ResponseEntity< List< ContactDTO > > findContacts( @Valid ContactDTO request ) {
        List< ContactDTO > contacts = ContactMapper.toContactDTO( service.findContacts( ContactMapper.toContact( request ) ) );
        return new ResponseEntity<>( contacts, HttpStatus.OK );
    }

    @DeleteMapping( value = "/{documentNumber}" )
    @ResponseStatus( HttpStatus.OK )
    public void deleteContactByDocumentNumber( @PathVariable( value = "documentNumber" ) String documentNumber ) {
        service.deleteContactByDocumentNumber( documentNumber );
    }

    @PutMapping( value = "/{documentNumber}" )
    public ResponseEntity< ContactDTO > updateContactByDocumentNumber( @PathVariable( value = "documentNumber" ) String documentNumber,
                                                                       @RequestBody @Valid ContactDTO contact ) {
        ContactDTO contactDTO = ContactMapper.toContactDTO( service.updateContactByDocumentNumber( documentNumber, ContactMapper.toContact( contact ) ) );
        return new ResponseEntity<>( contactDTO, HttpStatus.CREATED );
    }

}
