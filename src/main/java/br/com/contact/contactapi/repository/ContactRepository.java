package br.com.contact.contactapi.repository;

import br.com.contact.contactapi.domain.Contact;
import br.com.contact.contactapi.repository.specification.ContactSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public interface ContactRepository extends JpaRepository< Contact, Long >, JpaSpecificationExecutor< Contact > {

    default List< Contact > findContacts( Contact contact ) {
        return findAll( where( ContactSpecification.name( contact.getName() ) )
                .and( ContactSpecification.phoneNumber( contact.getPhoneNumber() ) )
                .and( ContactSpecification.documentNumber( contact.getDocumentNumber() ) ) );
    }

    @Transactional
    @Modifying
    @Query( "delete from Contact where document_number = :documentNumber" )
    void deleteByDocumentNumber( @Param( "documentNumber" ) String documentNumber );

    Optional< Contact > findByDocumentNumber( String documentNumber );
}
