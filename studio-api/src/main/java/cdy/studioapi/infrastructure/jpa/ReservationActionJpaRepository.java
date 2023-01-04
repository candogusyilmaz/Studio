package cdy.studioapi.infrastructure.jpa;

import cdy.studioapi.models.ReservationAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReservationActionJpaRepository extends JpaRepository<ReservationAction, Integer>, JpaSpecificationExecutor<ReservationAction> {
}