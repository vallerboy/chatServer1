package pl.oskarpolak.chat.models.respositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.oskarpolak.chat.models.LogModel;

@Repository
public interface LogRepository extends CrudRepository<LogModel, Integer> {

}
