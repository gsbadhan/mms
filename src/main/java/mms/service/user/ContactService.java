package mms.service.user;

import mms.dto.user.ContactResponse;
import mms.dto.user.UpsertContactRequest;

public interface ContactService {
    ContactResponse saveContact(UpsertContactRequest request) throws Exception;

    ContactResponse getContact(Integer id) throws Exception;

    ContactResponse updateContact(Integer id, UpsertContactRequest request) throws Exception;;

    void deleteContact(Integer id) throws Exception;;
}
