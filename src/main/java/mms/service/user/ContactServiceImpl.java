package mms.service.user;

import jakarta.persistence.EntityNotFoundException;
import mms.dao.user.Customer;
import mms.dao.user.CustomerRepository;
import mms.dto.user.ContactResponse;
import mms.dto.user.UpsertContactRequest;
import mms.pubsub.message.UserDetailMessage;
import mms.pubsub.queue.UserDetailPublisherUserSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserDetailPublisherUserSvc userSvcUserDetailPublisher;

    @Override
    public ContactResponse saveContact(UpsertContactRequest request) throws Exception {
        Customer customer = customerRepository.save(new Customer(null, request.getPhoneNo(), request.getEmail(),
                request.getFirstName(),
                request.getLastName(), null, null));
        ContactResponse response = new ContactResponse(customer.getId(), customer.getPhoneNumber(), customer.getEmail(),
                customer.getFirstName(), customer.getLastName());
        userSvcUserDetailPublisher.publish(customer.getId().toString(), new UserDetailMessage(customer.getId(),
                customer.getPhoneNumber(), customer.getEmail(), customer.getFirstName(), customer.getLastName(),
                request.getMetaData()));
        return response;
    }

    @Override
    public ContactResponse getContact(Integer id) throws Exception {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isEmpty())
            throw new EntityNotFoundException("Customer not found");
        Customer customer = existing.get();
        ContactResponse response = new ContactResponse(customer.getId(), customer.getPhoneNumber(), customer.getEmail(),
                customer.getFirstName(), customer.getLastName());
        return response;
    }

    @Override
    public ContactResponse updateContact(Integer id, UpsertContactRequest request) throws Exception {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isEmpty())
            throw new EntityNotFoundException("Customer not found");
        Customer customer = existing.get();
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNo());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        Customer updatedCustomer = customerRepository.save(customer);
        ContactResponse response = new ContactResponse(updatedCustomer.getId(), updatedCustomer.getPhoneNumber(), updatedCustomer.getEmail(),
                updatedCustomer.getFirstName(), updatedCustomer.getLastName());
        userSvcUserDetailPublisher.publish(updatedCustomer.getId().toString(), new UserDetailMessage(updatedCustomer.getId(),
                updatedCustomer.getPhoneNumber(), updatedCustomer.getEmail(), updatedCustomer.getFirstName(), updatedCustomer.getLastName(),
                request.getMetaData()));
        return response;
    }

    @Override
    public void deleteContact(Integer id) throws Exception {
        customerRepository.deleteById(id);
    }
}
