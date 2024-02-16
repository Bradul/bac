package org.example.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
/* this works the same way as @Entity for JPA
   instead of mapping to an RDB table it maps
   to a NOSQL  / MongoDB document.

   In this case, users is the collection, or
   in JPA it would be the table type.
*/
@Document(collection = "users")
public class User {
    @Id
    private String username;
}
