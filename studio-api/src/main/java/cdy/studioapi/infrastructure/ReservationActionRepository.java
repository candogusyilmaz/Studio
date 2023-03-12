package cdy.studioapi.infrastructure;

import cdy.studioapi.models.ReservationAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationActionRepository extends JpaRepository<ReservationAction, Integer>, JpaSpecificationExecutor<ReservationAction> {
}