package br.com.contact.contactapi.unit.web;

import br.com.contact.contactapi.util.CommonsTests;
import br.com.contact.contactapi.web.dto.ContactDTO;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
class UpdateContactTests extends CommonsTests {

    private static final String UPDATE_CONTACT_SUCCESS = "src/test/resources/unit/contact/updatecontact/update-contact-success.json";

    @Autowired
    public MockMvc mockMvc;

    @ParameterizedTest
    @DisplayName( "Dado um número de documento válido, deve atualizar o nome" )
    @CsvSource( { "Teste 2 atualizado, 52963254125, 48209204050" } )
    void givenUpdateContactNameFilteredByDocumentNumberMustReturnUpdatedContact( String name, String phoneNumber, String documentNumber ) throws Exception {
        ContactDTO updateContact = jsonToObject( UPDATE_CONTACT_SUCCESS, ContactDTO.class );

        mockMvc.perform( put( "/contacts/{documentNumber}", "48209204050" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( objectToJson( ContactDTO.builder()
                        .name( name )
                        .phoneNumber( phoneNumber )
                        .documentNumber( documentNumber )
                        .build() ) ) )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath( "$.name", is( updateContact.getName() ) ) )
                .andExpect( jsonPath( "$.documentNumber", is( updateContact.getDocumentNumber() ) ) )
                .andExpect( jsonPath( "$.phoneNumber", is( updateContact.getPhoneNumber() ) ) );

        mockMvc.perform( get( "/contacts" )
                .contentType( MediaType.APPLICATION_JSON )
                .param( "documentNumber", updateContact.getDocumentNumber() ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$", hasSize( 1 ) ) )
                .andExpect( jsonPath( "$[0].name", is( updateContact.getName() ) ) )
                .andExpect( jsonPath( "$[0].documentNumber", is( updateContact.getDocumentNumber() ) ) )
                .andExpect( jsonPath( "$[0].phoneNumber", is( updateContact.getPhoneNumber() ) ) );
    }

    @ParameterizedTest
    @DisplayName( "Dado um número de documento válido e um nome null, deve retornar erro de campo obrigatório para nome" )
    @ValueSource( strings = "48209204050" )
    void givenInvalidUpdateContactNameFilteredByDocumentNumberMustError( String documentNumber ) throws Exception {
        mockMvc.perform( put( "/contacts/{documentNumber}", documentNumber )
                .contentType( MediaType.APPLICATION_JSON )
                .content( objectToJson( ContactDTO.builder()
                        .name( null )
                        .phoneNumber( "52963254125" )
                        .documentNumber( "48209204050" )
                        .build() ) ) )
                .andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.code", CoreMatchers.is( "MSG004" ) ) )
                .andExpect( jsonPath( "$.message", CoreMatchers.is( "Required field" ) ) )
                .andExpect( jsonPath( "$.detail[0].type", CoreMatchers.is( "name" ) ) );
    }

    @Test
    @DisplayName( "Dado um número de documento válido e um telefone null, deve retornar erro de campo obrigatório para telefone" )
    void givenInvalidUpdateContactPhoneNumberFilteredByDocumentNumberMustError() throws Exception {
        mockMvc.perform( put( "/contacts/{documentNumber}", "48209204050" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( objectToJson( ContactDTO.builder()
                        .name( "Teste 2 atualizado" )
                        .phoneNumber( null )
                        .documentNumber( "48209204050" )
                        .build() ) ) )
                .andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.code", CoreMatchers.is( "MSG004" ) ) )
                .andExpect( jsonPath( "$.message", CoreMatchers.is( "Required field" ) ) )
                .andExpect( jsonPath( "$.detail[0].type", CoreMatchers.is( "phoneNumber" ) ) );
    }

    @Test
    @DisplayName( "Dado um número de documento inválido e um telefone, deve retornar isNoContent" )
    void givenUpdateContactPhoneNumberFilteredByInvalidDocumentNumberMustReturnNoContentException() throws Exception {
        mockMvc.perform( put( "/contacts/{documentNumber}", "97010674019" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( objectToJson( ContactDTO.builder()
                        .name( "Teste 2 atualizado" )
                        .phoneNumber( "52963254125" )
                        .documentNumber( "48209204050" )
                        .build() ) ) )
                .andExpect( status().isNoContent() );
    }

}
