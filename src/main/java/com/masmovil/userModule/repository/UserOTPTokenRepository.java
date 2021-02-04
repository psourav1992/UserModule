package com.masmovil.userModule.repository;

import com.masmovil.userModule.dto.User;
import com.masmovil.userModule.dto.UserOTPToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserOTPTokenRepository  extends JpaRepository<UserOTPToken, Long> {

    @Query("from UserOTPToken u where u.status = 'ACTIVE' and u.userId =:auth_user_id ")
    UserOTPToken findByUserId(@Param("auth_user_id") Long auth_user_id);

    @Modifying(clearAutomatically = true)
    @Query("update UserOTPToken set status = 'EXPIRED' where userId =:userId and status = 'ACTIVE'")
    void updateUserOTPToken(@Param("userId") Long userId);
}
