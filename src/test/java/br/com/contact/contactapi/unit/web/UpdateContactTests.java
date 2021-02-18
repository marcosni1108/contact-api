package br.com.contact.contactapi.unit.web;

import br.com.contact.contactapi.web.dto.ContactDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
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

import java.io.File;
import java.io.IOException;

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
class UpdateContactTests {

    private static final String UPDATE_CONTACT_SUCCESS = "src/test/resources/unit/contact/updatecontact/update-contact-success.json";

    @Autowired
    public MockMvc mockMvc;

    @Test
    public void givenUpdateContactNameFilteredByDocumentNumberMustReturnUpdatedContact() throws Exception {
        ContactDTO updateContact = jsonToObject( UPDATE_CONTACT_SUCCESS, ContactDTO.class );

        mockMvc.perform( put( "/contacts/{documentNumber}", "48209204050" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( objectToJson( ContactDTO.builder()
                        .name( "Teste 2 atualizado" )
                        .phoneNumber( "52963254125" )
                        .documentNumber( "48209204050" )
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

    @Test
    public void givenInvalidUpdateContactNameFilteredByDocumentNumberMustError() throws Exception {
        mockMvc.perform( put( "/contacts/{documentNumber}", "48209204050" )
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
    public void givenInvalidUpdateContactPhoneNumberFilteredByDocumentNumberMustError() throws Exception {
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
    public void givenUpdateContactPhoneNumberFilteredByInvalidDocumentNumberMustReturnNoContentException() throws Exception {
        mockMvc.perform( put( "/contacts/{documentNumber}", "97010674019" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( objectToJson( ContactDTO.builder()
                        .name( "Teste 2 atualizado" )
                        .phoneNumber( "52963254125" )
                        .documentNumber( "48209204050" )
                        .build() ) ) )
                .andExpect( status().isNoContent() );
    }

    private < M > M jsonToObject( String pathJson, Class< M > clazz ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue( new File( pathJson ), clazz );
        } catch( IOException var4 ) {
            return null;
        }
    }

    private String objectToJson( Object object ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString( object );
        } catch( JsonProcessingException var3 ) {
            return "";
        }
    }

}
