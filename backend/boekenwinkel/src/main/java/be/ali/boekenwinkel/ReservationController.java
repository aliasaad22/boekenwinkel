
package be.ali.boekenwinkel;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final BookUserService bookUserService;

    public ReservationController(
            ReservationService reservationService,
            BookUserService bookUserService) {
        this.reservationService = reservationService;
        this.bookUserService = bookUserService;
    }

    @PostMapping
    public Reservation createReservation(
            @RequestBody ReservationRequest request,
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Je moet ingelogd zijn om te reserveren."
            );
        }

        BookUser ingelogdeGebruiker = bookUserService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "De ingelogde gebruiker werd niet gevonden."
                ));

        if (!ingelogdeGebruiker.getId().equals(request.userId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Je mag alleen voor jezelf reserveren."
            );
        }

        return reservationService.createReservation(
                ingelogdeGebruiker.getId(),
                request.bookId(),
                request.pickupDate()
        );
    }

    @PutMapping("/{id}/confirm")
    public Reservation confirmReservation(@PathVariable Long id) {
        return reservationService.confirmReservation(id);
    }

    @GetMapping
    public List<Reservation> getMyReservations(
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Je moet ingelogd zijn om reserveringen te bekijken."
            );
        }

        BookUser ingelogdeGebruiker = bookUserService
                .getUserByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "De ingelogde gebruiker werd niet gevonden."
                ));

        return reservationService.getReservationsByUserId(
                ingelogdeGebruiker.getId()
        );
    }

    @PutMapping("/{id}/pay")
    public Reservation markAsPaid(@PathVariable Long id) {
        return reservationService.markAsPaid(id);
    }


}
