
package be.ali.boekenwinkel;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTests {

    @Test
    void alleBoekenOphalenGeeftAlleBoekenTerug() {
        BookRepository bookRepository = mock(BookRepository.class);
        BookService bookService = new BookService(bookRepository);

        Book book1 = new Book();
        Book book2 = new Book();

        when(bookRepository.findAll())
                .thenReturn(List.of(book1, book2));

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        verify(bookRepository).findAll();
    }

    @Test
    void boekOphalenOpBestaandIdGeeftBoekTerug() {
        BookRepository bookRepository = mock(BookRepository.class);
        BookService bookService = new BookService(bookRepository);

        Book book = new Book();
        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        assertSame(book, result.get());
    }

    @Test
    void boekOphalenOpOnbekendIdGeeftLeegResultaat() {
        BookRepository bookRepository = mock(BookRepository.class);
        BookService bookService = new BookService(bookRepository);

        when(bookRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void nieuwBoekOpslaanGeeftOpgeslagenBoekTerug() {
        BookRepository bookRepository = mock(BookRepository.class);
        BookService bookService = new BookService(bookRepository);

        Book book = new Book();
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.saveBook(book);

        assertSame(book, result);
        verify(bookRepository).save(book);
    }

    @Test
    void bestaandBoekAanpassenSlaatNieuweGegevensOp() {
        BookRepository bookRepository = mock(BookRepository.class);
        BookService bookService = new BookService(bookRepository);

        Book existingBook = new Book();
        existingBook.setIsbn("111");
        existingBook.setTitle("Oude titel");
        existingBook.setAuthors("Oude auteur");
        existingBook.setDescription("Oude beschrijving");
        existingBook.setPrice(new BigDecimal("10.00"));
        existingBook.setStock(2);

        Book updatedBook = new Book();
        updatedBook.setIsbn("222");
        updatedBook.setTitle("Nieuwe titel");
        updatedBook.setAuthors("Nieuwe auteur");
        updatedBook.setDescription("Nieuwe beschrijving");
        updatedBook.setPrice(new BigDecimal("20.00"));
        updatedBook.setStock(5);

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook))
                .thenReturn(existingBook);

        Optional<Book> result =
                bookService.updateBook(1L, updatedBook);

        assertTrue(result.isPresent());
        assertEquals("222", existingBook.getIsbn());
        assertEquals("Nieuwe titel", existingBook.getTitle());
        assertEquals("Nieuwe auteur", existingBook.getAuthors());
        assertEquals("Nieuwe beschrijving", existingBook.getDescription());
        assertEquals(new BigDecimal("20.00"), existingBook.getPrice());
        assertEquals(5, existingBook.getStock());

        verify(bookRepository).save(existingBook);
    }

    @Test
    void onbekendBoekAanpassenGeeftLeegResultaat() {
        BookRepository bookRepository = mock(BookRepository.class);
        BookService bookService = new BookService(bookRepository);

        when(bookRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Book> result =
                bookService.updateBook(99L, new Book());

        assertTrue(result.isEmpty());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void bestaandBoekVerwijderenGeeftTrueTerug() {
        BookRepository bookRepository = mock(BookRepository.class);
        BookService bookService = new BookService(bookRepository);

        when(bookRepository.existsById(1L)).thenReturn(true);

        boolean result = bookService.deleteBook(1L);

        assertTrue(result);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void onbekendBoekVerwijderenGeeftFalseTerug() {
        BookRepository bookRepository = mock(BookRepository.class);
        BookService bookService = new BookService(bookRepository);

        when(bookRepository.existsById(99L)).thenReturn(false);

        boolean result = bookService.deleteBook(99L);

        assertFalse(result);
        verify(bookRepository, never()).deleteById(anyLong());
    }
}
