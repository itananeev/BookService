package messaging;

import com.bezkoder.springjwt.models.Book;
import com.bezkoder.springjwt.services.BookServiceImplementation;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookListener {
    @Autowired
    private BookServiceImplementation bookService;

    @RabbitListener(queues = "book-queue")
    public void handleBook(Book book) {
        bookService.save(book);
    }
}

