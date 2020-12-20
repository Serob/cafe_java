package com.serob.cafe.repositories;

import com.serob.cafe.entities.CafeTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends CrudRepository<CafeTable, Long>  {
}
