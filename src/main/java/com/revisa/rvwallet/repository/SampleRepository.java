package com.revisa.rvwallet.repository;

import com.revisa.rvwallet.domain.Sample;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sample entity.
 */
public interface SampleRepository extends JpaRepository<Sample,Long> {

}
