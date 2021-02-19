package br.com.contact.contactapi.unit.web;

import br.com.contact.contactapi.util.CommonsTests;
import br.com.contact.contactapi.web.dto.ContactDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith( SpringJUnit4ClassRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
@SqlGroup( {
        @Sql( value = "/unit/contact/sql/clear-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD ),
        @Sql( value = "/unit/contact/sql/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD ),
        @Sql( value = "/unit/contact/sql/clear-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD )
} )
class FindContactsTests extends CommonsTests {

    private static final String FIND_CONTACTS_SUCCESS = "src/test/resources/unit/contact/findcontacts/find-contacts-success.json";
    private static final String FIND_CONTACT_BY_DOCUMENT_NUMBER_SUCCESS = "src/test/resources/unit/contact/findcontacts/find-contact-by-document-number-success.json";
    private static final String FIND_CONTACT_BY_NAME_SUCCESS = "src/test/resources/unit/contact/findcontacts/find-contact-by-name-success.json";

    @Autowired
    public MockMvc mockMvc;

    @Test
    void givenEmptyFilterMustReturnThreeContacts() throws Exception {
        List< ContactDTO > contacts = jsonToList( FIND_CONTACTS_SUCCESS, ContactDTO.class );
        mockMvc.perform( get( "/contacts" )
                .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$", hasSize( 3 ) ) )
                .andExpect( jsonPath( "$[0].name", is( contacts.get( 0 ).getName() ) ) )
                .andExpect( jsonPath( "$[0].documentNumber", is( contacts.get( 0 ).getDocumentNumber() ) ) )
                .andExpect( jsonPath( "$[0].phoneNumber", is( contacts.get( 0 ).getPhoneNumber() ) ) )

                .andExpect( jsonPath( "$[1].name", is( contacts.get( 1 ).getName() ) ) )
                .andExpect( jsonPath( "$[1].documentNumber", is( contacts.get( 1 ).getDocumentNumber() ) ) )
                .andExpect( jsonPath( "$[1].phoneNumber", is( contacts.get( 1 ).getPhoneNumber() ) ) )

                .andExpect( jsonPath( "$[2].name", is( contacts.get( 2 ).getName() ) ) )
                .andExpect( jsonPath( "$[2].documentNumber", is( contacts.get( 2 ).getDocumentNumber() ) ) )
                .andExpect( jsonPath( "$[2].phoneNumber", is( contacts.get( 2 ).getPhoneNumber() ) ) );
    }

    @Test
    void givenValidFilterByDocumentNumberMustReturnOneContact() throws Exception {
        List< ContactDTO > contacts = jsonToList( FIND_CONTACT_BY_DOCUMENT_NUMBER_SUCCESS, ContactDTO.class );
        mockMvc.perform( get( "/contacts" )
                .contentType( MediaType.APPLICATION_JSON )
                .param( "documentNumber", "54270940085" ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$", hasSize( 1 ) ) )
                .andExpect( jsonPath( "$[0].name", is( contacts.get( 0 ).getName() ) ) )
                .andExpect( jsonPath( "$[0].documentNumber", is( contacts.get( 0 ).getDocumentNumber() ) ) )
                .andExpect( jsonPath( "$[0].phoneNumber", is( contacts.get( 0 ).getPhoneNumber() ) ) );
    }

    @Test
    void givenValidFilterByNameMustReturnOneContact() throws Exception {
        List< ContactDTO > contacts = jsonToList( FIND_CONTACT_BY_NAME_SUCCESS, ContactDTO.class );
        mockMvc.perform( get( "/contacts" )
                .contentType( MediaType.APPLICATION_JSON )
                .param( "name", "Teste 2" ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$", hasSize( 1 ) ) )
                .andExpect( jsonPath( "$[0].name", is( contacts.get( 0 ).getName() ) ) )
                .andExpect( jsonPath( "$[0].documentNumber", is( contacts.get( 0 ).getDocumentNumber() ) ) )
                .andExpect( jsonPath( "$[0].phoneNumber", is( contacts.get( 0 ).getPhoneNumber() ) ) );
    }

    @Test
    void givenValidFilterByHalfNameMustReturnThreeContacts() throws Exception {
        List< ContactDTO > contacts = jsonToList( FIND_CONTACTS_SUCCESS, ContactDTO.class );
        mockMvc.perform( get( "/contacts" )
                .contentType( MediaType.APPLICATION_JSON )
                .param( "name", "teste" ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$", hasSize( 3 ) ) )
                .andExpect( jsonPath( "$[0].name", is( contacts.get( 0 ).getName() ) ) )
                .andExpect( jsonPath( "$[0].documentNumber", is( contacts.get( 0 ).getDocumentNumber() ) ) )
                .andExpect( jsonPath( "$[0].phoneNumber", is( contacts.get( 0 ).getPhoneNumber() ) ) )

                .andExpect( jsonPath( "$[1].name", is( contacts.get( 1 ).getName() ) ) )
                .andExpect( jsonPath( "$[1].documentNumber", is( contacts.get( 1 ).getDocumentNumber() ) ) )
                .andExpect( jsonPath( "$[1].phoneNumber", is( contacts.get( 1 ).getPhoneNumber() ) ) )

                .andExpect( jsonPath( "$[2].name", is( contacts.get( 2 ).getName() ) ) )
                .andExpect( jsonPath( "$[2].documentNumber", is( contacts.get( 2 ).getDocumentNumber() ) ) )
                .andExpect( jsonPath( "$[2].phoneNumber", is( contacts.get( 2 ).getPhoneNumber() ) ) );
    }

    @Test
    void givenNonexistentDocumentNumberMustReturnEmptyList() throws Exception {
        mockMvc.perform( get( "/contacts" )
                .contentType( MediaType.APPLICATION_JSON )
                .param( "documentNumber", "97010674019" ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$", empty() ) );
    }

}
