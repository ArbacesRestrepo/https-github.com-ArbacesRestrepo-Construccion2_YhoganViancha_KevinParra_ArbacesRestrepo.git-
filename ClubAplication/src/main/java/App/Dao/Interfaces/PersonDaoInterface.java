package App.Dao.Interfaces;

/**
 * @author Arbaces Restrepo, Jhogan Viancha
 */

import App.Dto.PersonDto;

public interface PersonDaoInterface {
    public boolean existsByDocument(PersonDto personDto) throws Exception;

    public void createPerson(PersonDto personDto) throws Exception;

    public void updatePerson(PersonDto personDto) throws Exception;

    public void deletePerson(PersonDto personDto) throws Exception;

    public PersonDto findByDocument(PersonDto personDto) throws Exception;    
}