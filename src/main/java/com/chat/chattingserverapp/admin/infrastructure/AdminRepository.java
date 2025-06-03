package com.chat.chattingserverapp.admin.infrastructure;

import com.chat.chattingserverapp.admin.domain.Admin;
import java.util.Optional;

public interface AdminRepository {

  Optional<Admin> findByEmail(String email);

}
