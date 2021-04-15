package br.com.contact.contactapi.unit.service;

import br.com.contact.contactapi.domain.Contact;
import br.com.contact.contactapi.exception.APIException;
import br.com.contact.contactapi.exception.NoContentException;
import br.com.contact.contactapi.repository.ContactRepository;
import br.com.contact.contactapi.service.ContactService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ContactServiceTests {

    @Autowired
    private ContactService service;

    @MockBean
    private ContactRepository repository;

    @Test
    @DisplayName( "Buscar contato por número de documento deverá retornar 1 contato" )
    void givenValidDocumentNumberMustReturnOneContact() {
        List< Contact > contacts = singletonList( new Contact( 2L, "Teste 2", "48209204050",
                "52963254125" ) );

        doReturn( contacts )
                .when( repository ).findContacts( any() );

        List< Contact > returnContacts = service.findContacts( Contact.builder().documentNumber( "48209204050" ).build() );

        Assertions.assertAll(
                () -> assertFalse( returnContacts.isEmpty(), "Contato não encontrado" ),
                () -> assertEquals( 1, returnContacts.size(), "Retornou mais do que um contato" ),
                () -> assertSame( returnContacts, contacts, "O contato retornado não é o mesmo do mock" ),
                () -> assertTrue( 4 > 5, "Algo de errado não está certo!" ),
                () -> assertEquals( 5, 6, "5 não é igual a 6!" )
        );
    }

    @Test
    @DisplayName( "Buscar contato por número de documento inválido deverá retornar uma lista vazia" )
    void givenInvalidDocumentNumberMustReturnEmptyList() {
        doReturn( null )
                .when( repository ).findContacts( any() );
        assertTrue( service.findContacts( Contact.builder().documentNumber( "1" ).build() ).isEmpty() );
    }

    @Test
    @DisplayName( "Quando salvar um contato duplicado deverá retornar erro de duplicidade" )
    void whenSavingDuplicateContactShouldReturnError() {
        when( repository.findByDocumentNumber( any() ) )
                .thenReturn( Optional.of( mockContact() ) );

        Throwable exception = assertThrows( APIException.class, () -> service.saveContact( mockContact() ) );
        assertEquals( "Duplicated Registration", exception.getMessage() );
    }

    @Test
    @DisplayName( "Quando atualizar um contato passando um número de documento inválido, deve retornar NoContentException" )
    void whenUpdateContactWithInvalidDocumentNumberShouldReturnError() {
        doReturn( Optional.empty() )
                .when( repository ).findByDocumentNumber( any() );

        assertThrows( NoContentException.class, () -> service.updateContactByDocumentNumber( "48209204050", mockContact() ) );
    }

    @Test
    @Tag( "duplicated" ) // mvn test -DexcludedGroups="duplicated"
    @DisplayName( "Quando atualizar um contato passando um número de documento inválido, deve retornar NoContentException" )
    void whenUpdateContactWithInvalidDocumentNumberShouldReturnError2() {
        doReturn( Optional.empty() )
                .when( repository ).findByDocumentNumber( any() );

        assertThrows( NoContentException.class, () -> service.updateContactByDocumentNumber( "48209204050", mockContact() ) );
    }

    private static Contact mockContact() {
        return Contact.builder()
                .id( 2L )
                .name( "Teste 2" )
                .documentNumber( "48209204050" )
                .phoneNumber( "52963254125" )
                .build();
    }

}
