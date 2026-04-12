package fr.pir.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.pir.model.user.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    VerificationCode findVerificationCodeByCode(String code);

    VerificationCode findVerificationCodeByEmail(String email);

}
