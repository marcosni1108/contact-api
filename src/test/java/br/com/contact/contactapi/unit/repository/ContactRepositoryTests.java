package br.com.contact.contactapi.unit.repository;

import br.com.contact.contactapi.repository.ContactRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@SqlGroup( {
        @Sql( value = "/unit/contact/sql/clear-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD ),
        @Sql( value = "/unit/contact/sql/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD ),
        @Sql( value = "/unit/contact/sql/clear-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD )
} )
public class ContactRepositoryTests {

    @Autowired
    private ContactRepository repository;

    @ParameterizedTest
    @ValueSource( strings = { "51952318009", "48209204050", "ABC1234", "54270940085" } )
    @DisplayName( "Quando enviar um número de documento válido deve retornar o contato" )
    void givenValidDocumentNumberMustReturnContact( String documentNumber ) {
        assertTrue( repository.findByDocumentNumber( documentNumber ).isPresent(), "Contato com o número de documento " + documentNumber + " não encontrado " );
    }
}
