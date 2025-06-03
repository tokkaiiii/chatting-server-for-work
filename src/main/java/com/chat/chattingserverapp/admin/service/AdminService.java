package com.chat.chattingserverapp.admin.service;

import com.chat.chattingserverapp.admin.query.IssueAdminToken;
import com.chat.chattingserverapp.admin.response.AdminResponse;
import java.util.Optional;

public interface AdminService {

  Optional<AdminResponse> findByEmail(IssueAdminToken query);

}
