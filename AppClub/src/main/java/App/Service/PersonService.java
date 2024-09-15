package App.Service;

/**
 * @author Arbaces Restrepo, Jhogan Vianch, Kevin Parra
 */

import App.Dao.PersonDao;
import App.Dao.UserDao;
import App.Dto.PersonDto;
import App.Dto.UserDto;
import App.Service.Intefaces.PersonServiceInterface;

public class PersonService implements PersonServiceInterface {
    private final PersonDao personDao = new PersonDao();
    
    @Override
    public PersonDto createPerson( ) throws Exception {
        PersonDto personDto = new PersonDto();
        personDto.getPersonDocumentDto();
        
        if ( this.personDao.existsByDocument( personDto ) ) {
            personDto = this.personDao.findByDocument( personDto );
            System.out.println("Ya existe: " + personDto.getName() );
            personDto = this.personDao.findByDocument( personDto );
            return personDto;
        }
        
        personDto.getPersonNameDto();
        personDto.getPersonCellNumberDto();
        
        this.personDao.createPerson( personDto );
        
        personDto = this.personDao.findByDocument( personDto );
        return personDto;
    }

    @Override
    public PersonDto updatePerson( ) throws Exception {
        PersonDto personDto = new PersonDto();
        personDto.getPersonDocumentDto();
        
        if ( this.personDao.existsByDocument( personDto ) ) {
            throw new Exception( "No se encentra el número identificación");
        }
        
        personDto.getPersonCellNumberDto();
        
        this.personDao.updatePerson( personDto );
        
        personDto = this.personDao.findByDocument( personDto );
        return personDto;        
    }

    @Override
    public void deletePerson( ) throws Exception {
        PersonDto personDto = new PersonDto();
        personDto.getPersonDocumentDto();
        
        if ( this.personDao.existsByDocument( personDto ) ) {
            throw new Exception( "No se encentra el número identificación");
        }
        personDto = this.personDao.findByDocument( personDto );
                
        UserDao userDao = new UserDao();
        UserDto userDto = userDao.findByPersonId( personDto );
        if ( userDto != null ){
            throw new Exception("La persona tiene usuario" );
        }
        
        System.out.println("Borrar a: " + personDto.getName() );
        this.personDao.deletePerson( personDto );        
    }
}