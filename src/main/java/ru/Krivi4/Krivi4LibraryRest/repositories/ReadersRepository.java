package ru.Krivi4.Krivi4LibraryRest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Krivi4.Krivi4LibraryRest.models.Reader;


@Repository
public interface ReadersRepository extends JpaRepository<Reader, Integer> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Integer id);

}
