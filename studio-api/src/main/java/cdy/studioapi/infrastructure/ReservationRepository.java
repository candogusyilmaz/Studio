package cdy.studioapi.infrastructure;

import cdy.studioapi.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {
}