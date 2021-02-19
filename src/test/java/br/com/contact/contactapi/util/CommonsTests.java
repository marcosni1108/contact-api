package br.com.contact.contactapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CommonsTests {

    protected static < M > List< M > jsonToList( String pathJson, Class< M > clazz ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue( new File( pathJson ), mapper.getTypeFactory()
                    .constructCollectionType( List.class, clazz ) );
        } catch( IOException e ) {
            return Collections.emptyList();
        }
    }

    protected < M > M jsonToObject( String pathJson, Class< M > clazz ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue( new File( pathJson ), clazz );
        } catch( IOException var4 ) {
            return null;
        }
    }

    protected String objectToJson( Object object ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString( object );
        } catch( JsonProcessingException var3 ) {
            return "";
        }
    }
}
