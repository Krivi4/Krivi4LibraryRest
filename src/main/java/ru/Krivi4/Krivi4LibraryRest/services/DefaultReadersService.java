package ru.Krivi4.Krivi4LibraryRest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Krivi4.Krivi4LibraryRest.models.Reader;
import ru.Krivi4.Krivi4LibraryRest.repositories.ReadersRepository;
import ru.Krivi4.Krivi4LibraryRest.web.exceptions.ReaderDuplicateEmailException;
import ru.Krivi4.Krivi4LibraryRest.web.exceptions.ReaderNotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DefaultReadersService implements ReaderService {

    private final ReadersRepository readersRepository;


    /** Выводит всех читателей*/
    @Override
    @Transactional(readOnly = true)
    public Page<Reader> findAll(Pageable pageable) { return readersRepository.findAll(pageable);
    }
    /** Выводит одного читателя по id */
    @Override
    @Transactional(readOnly = true)
    public Reader findById(int id){
        return readersRepository
                .findById(id)
                .orElseThrow(() -> new ReaderNotFoundException(id));
    }


    /** Сохраняет читателя + устанавливает время создания */
    @Override
    @Transactional
    public Reader save(Reader reader){
        if(readersRepository.existsByEmail(reader.getEmail())){
            throw new ReaderDuplicateEmailException(reader.getEmail());
        }
        enrichReader(reader);
        readersRepository.save(reader);
        return reader;
    }

    /** Обновляет данные читателя + устанавливает время обновления */
    @Override
    @Transactional
    public Reader update(int id, Reader updatedReader){
        if(readersRepository.existsByEmailAndIdNot(updatedReader.getEmail(), id)){
            throw new ReaderDuplicateEmailException(updatedReader.getEmail());
        }
        updatedReader.setId(id);
        enrichReader(updatedReader);
        readersRepository.save(updatedReader);

        return updatedReader;
    }

    /** Удаляет читателя */
    @Override
    @Transactional
    public void delete(int id){
        readersRepository.deleteById(id);
    }

    /** Устанавливает время создания и обновления */
    private void enrichReader(Reader reader){
        reader.setCreatedAt(LocalDateTime.now());
        reader.setUpdatedAt(LocalDateTime.now());
    }

}
