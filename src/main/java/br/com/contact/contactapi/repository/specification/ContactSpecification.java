package br.com.contact.contactapi.repository.specification;

import br.com.contact.contactapi.domain.Contact;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;
import java.util.Objects;

@UtilityClass
public class ContactSpecification {

    public Specification< Contact > name( String name ) {
        return ( root, criteriaQuery, criteriaBuilder ) -> {
            if( Objects.isNull( name ) ) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like( criteriaBuilder.upper( root.get( "name" ) ), "%" + name.toUpperCase( Locale.ROOT ) + "%" );
        };
    }

    public Specification< Contact > phoneNumber( String phoneNumber ) {
        return ( root, criteriaQuery, criteriaBuilder ) -> {
            if( Objects.isNull( phoneNumber ) ) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like( root.get( "phoneNumber" ), "%" + phoneNumber + "%" );
        };
    }

    public Specification< Contact > documentNumber( String documentNumber ) {
        return ( root, criteriaQuery, criteriaBuilder ) -> {
            if( Objects.isNull( documentNumber ) ) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal( root.get( "documentNumber" ), documentNumber );
        };
    }
}
