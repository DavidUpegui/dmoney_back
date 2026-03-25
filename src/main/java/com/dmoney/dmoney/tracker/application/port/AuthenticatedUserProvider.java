package com.dmoney.dmoney.tracker.application.port;

import com.dmoney.dmoney.shared.domain.models.UserId;

public interface AuthenticatedUserProvider {

    UserId currentUserId();
}
