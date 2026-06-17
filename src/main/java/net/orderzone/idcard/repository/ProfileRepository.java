package net.orderzone.idcard.repository;

import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUuid(String uuid);

    Optional<Profile> findByRegistrationNumber(String registrationNumber);

    boolean existsByUuid(String uuid);

    boolean existsByRegistrationNumber(String registrationNumber);

    List<Profile> findByType(ProfileType type);

    List<Profile> findByDepartment(String department);

    @Query("SELECT p FROM Profile p WHERE " +
           "LOWER(p.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.registrationNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Profile> search(@Param("keyword") String keyword);

    /** Helper to find the latest registration number for sequence number generation. */
    Optional<Profile> findFirstByRegistrationNumberStartingWithOrderByRegistrationNumberDesc(String prefix);
}
