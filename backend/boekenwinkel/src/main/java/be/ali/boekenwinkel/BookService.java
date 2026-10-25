
package be.ali.boekenwinkel;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Bevat de servicelogica voor het beheren van boeken.
 * De databasebewerkingen worden uitgevoerd via BookRepository.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Haalt alle boeken op.
     *
     * @return een lijst met alle boeken
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Zoekt een boek op basis van zijn ID.
     *
     * @param id het ID van het boek
     * @return het boek, of Optional.empty() als het niet bestaat
     */
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Slaat een nieuw boek op.
     *
     * @param book het boek dat opgeslagen moet worden
     * @return het opgeslagen boek
     */
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Werkt de gegevens van een bestaand boek bij.
     *
     * @param id het ID van het boek dat aangepast moet worden
     * @param updatedBook de nieuwe gegevens van het boek
     * @return het bijgewerkte boek, of Optional.empty() als het niet bestaat
     */
    public Optional<Book> updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setIsbn(updatedBook.getIsbn());
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthors(updatedBook.getAuthors());
                    book.setDescription(updatedBook.getDescription());
                    book.setPrice(updatedBook.getPrice());
                    book.setStock(updatedBook.getStock());

                    return bookRepository.save(book);
                });
    }

    /**
     * Verwijdert een boek als het bestaat.
     *
     * @param id het ID van het te verwijderen boek
     * @return true als het boek verwijderd is, anders false
     */
    public boolean deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            return false;
        }

        bookRepository.deleteById(id);
        return true;
    }
}
