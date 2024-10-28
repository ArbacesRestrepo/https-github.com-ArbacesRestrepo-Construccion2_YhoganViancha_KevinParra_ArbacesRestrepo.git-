package App.Service;

/**
 * @author Arbaces Restrepo, Jhogan Viancha
 */


import App.Service.Intefaces.GuestServiceInterface;

import App.Dao.PersonDao;
import App.Dao.UserDao;
import App.Dao.PartnerDao;
import App.Dao.GuestDao;
import App.Dao.InvoiceDao;
import App.Dao.InvoiceDetailDao;

import App.Dto.PersonDto;
import App.Dto.UserDto;
import App.Dto.GuestDto;
import App.Dto.InvoiceDto;
import App.Dto.PartnerDto;

public class GuestService implements GuestServiceInterface {
    private final UserService userService = new UserService();
    private final PersonDao personDao = new PersonDao();
    private final UserDao userDao = new UserDao();
    private final PartnerDao partnerDao = new PartnerDao();
    private final GuestDao guestDao = new GuestDao();
    private final InvoiceDao invoiceDao = new InvoiceDao();
    private final InvoiceDetailDao invoiceDetailDao = new InvoiceDetailDao();

    @Override
    public void createGuest( ) throws Exception {
        UserDto userDtoLocate = this.userService.createUserGuest();

        if ( userDtoLocate == null ) {
            throw new Exception("No se encontró ningún usuario");            
        }

        PersonDto personDto = new PersonDto();
        personDto.getPersonDocumentDto( "Ingrese el documento del socio que invita" );
        personDto = this.personDao.findByDocument(personDto);
        if ( personDto == null ) {
            throw new Exception("No se encontró ningúna persona con el numero de identificación");            
        }
        
        UserDto userDtoInvite = this.userDao.findByPersonId( personDto );
        PartnerDto partnerDto = this.partnerDao.findByUserId( userDtoInvite );
        if ( partnerDto == null ) {
            throw new Exception( personDto.getName() + " no es socio del club");            
        }
                
        GuestDto guestDto = new GuestDto();
        guestDto.setUserId( userDtoLocate.getId() );
        guestDto.setPartnerId( partnerDto.getId() );
        guestDto.setStatus( "ACTIVO" );
        
        this.guestDao.createGuest( guestDto );
    }
    
    @Override
    public void createGuest( UserDto userDto ) throws Exception {
        PartnerDto partnerDto = this.partnerDao.findByUserId( userDto );

        GuestDto guestDto = new GuestDto();
        guestDto.setUserId( userDto.getId() );
        guestDto.setPartnerId( partnerDto.getId() );
        guestDto.setStatus( "ACTIVO" );
        
        this.guestDao.createGuest( guestDto );
    }
    
    @Override
    public void updateGuest( ) throws Exception {
        
    }

    @Override
    public void deleteGuest( ) throws Exception {
        PersonDto personDtoLocale = new PersonDto();
        personDtoLocale.getPersonDocumentDto();
        personDtoLocale = this.personDao.findByDocument( personDtoLocale );
        
        if ( personDtoLocale == null ){
            throw new Exception("No existe la persona");
        }
        
        double amountActiveInvoices = this.invoiceDao.amountActiveInvoices( personDtoLocale );
        if ( amountActiveInvoices > 0 ){
            throw new Exception( personDtoLocale.getName() + " tiene facturas pendientes de pago");
        }
        
        UserDto userDtoLocate = this.userDao.findByPersonId( personDtoLocale );
        if ( userDtoLocate == null ) {
            throw new Exception("No se encontró ningún usuario con el número de identificación ingresado");            
        }

        GuestDto guestDto = this.guestDao.findByUserId( userDtoLocate );
        
        if ( guestDto == null ){
            throw new Exception("No existe el invitado");                            
        }
        
        this.guestDao.deleteGuest( guestDto );
    }    

    @Override
    public void changeGuestToPartner( UserDto userDto ) throws Exception {
        PersonDto personDtoLocale = this.personDao.findByUserId( userDto );
        
        if ( personDtoLocale == null ){
            throw new Exception("No existe la persona");
        }
        
        double amountActiveInvoices = this.invoiceDao.amountActiveInvoices( personDtoLocale );
        if ( amountActiveInvoices > 0 ){
            throw new Exception( personDtoLocale.getName() + " tiene facturas pendientes de pago");
        }

        GuestDto guestDto = this.guestDao.findByUserId( userDto );
        if ( guestDto == null ){
            throw new Exception( personDtoLocale.getName() + " no es un invitado");            
        }

        PartnerDto partnerDto = this.partnerDao.findByUserId( userDto );
        if ( partnerDto != null ){
            throw new Exception( personDtoLocale.getName() + " ya es SOCIO del club");
        }
        
        partnerDto = new PartnerDto();
        partnerDto.setUserId( userDto.getId() );
        partnerDto.getPartnerTypeDto();
        if ( partnerDto.getType().equals( "VIP" ) ){
            long numberVIP = this.partnerDao.numberPartnersVIP();
            if ( numberVIP >= 5 ){
                throw new Exception("Cupo de socios VIP copado");                
            }
        }
        partnerDto.getPartnerAmountDto();
        
        userDto.setRole( "SOCIO" );
        
        InvoiceDto invoiceDto = this.invoiceDao.firstInvoiceByPersonId( personDtoLocale );
        while ( invoiceDto != null ){
            this.invoiceDetailDao.deleteInvoiceDetail( invoiceDto );
            this.invoiceDao.deleteInvoice( invoiceDto );
            invoiceDto = this.invoiceDao.firstInvoiceByPersonId( personDtoLocale );
        }
        this.guestDao.deleteGuest( guestDto );
        this.partnerDao.createPartner( partnerDto );
        this.userDao.updateRoleUser( userDto );
    }
}