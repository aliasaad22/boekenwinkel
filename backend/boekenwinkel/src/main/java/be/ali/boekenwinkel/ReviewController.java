
package be.ali.boekenwinkel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/book/{bookId}")
    public List<Review> getReviewsByBookId(
            @PathVariable Long bookId) {
        return reviewService.getReviewsByBookId(bookId);
    }

    @PostMapping
    public ReviewResponse createReview(
            @RequestBody ReviewRequest request) {

        Review review = new Review();
        review.setUserId(request.userId());
        review.setBookId(request.bookId());
        review.setScore(request.score());
        review.setText(request.text());

        Review savedReview = reviewService.saveReview(review);

        return new ReviewResponse(
                savedReview.getId(),
                savedReview.getUserId(),
                savedReview.getBookId(),
                savedReview.getScore(),
                savedReview.getText()
        );
    }
}
