package All.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;
    private String filetype;
    @Lob
    private Blob image;
    private String downloadUrl;


    @JsonIgnore
@ManyToOne
@JoinColumn(name="product_id")
    private Product product;
}


/*
------>BLOB (Binary Large Object)
A BLOB is a data type that can store large amounts of binary data, which are often images,
audio files, multimedia files, and other complex data types. Here's a bit more detail:
Storage: BLOBs are designed to hold large amounts of data. They can store binary data such
as images, audio files, videos, and other types of multimedia.
Usage: Commonly used in databases to store objects that don't fit into standard data types
like integers or strings. For example, if you need to store a user's profile picture,
a BLOB might be the right choice.

Manipulation: Operations on BLOBs are different from standard data types because they
involve manipulating raw binary data. You'll often need special functions to handle these
 data types.

 ------>@GeneratedValue(strategy = GenerationType.IDENTITY)
 strategy means that the database itself is responsible for generating the primary key value using an
 auto-increment column.
 */