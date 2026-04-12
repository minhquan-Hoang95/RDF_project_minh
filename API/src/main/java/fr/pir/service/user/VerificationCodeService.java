package fr.pir.service.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.pir.exception.VerificationCodeException;
import fr.pir.model.user.VerificationCode;
import fr.pir.repository.user.VerificationCodeRepository;
import fr.pir.service.email.EmailService;

@Service
public class VerificationCodeService {

    private static final Logger L = LogManager.getLogger(VerificationCodeService.class);

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private EmailService emailService;

    private String generateCode() {
        L.debug("");

        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int chiffre = random.nextInt(10);
            code.append(chiffre);
        }

        return code.toString();
    }

    protected VerificationCode createAndSendVerificationCode(String email, String templateName) throws Exception {
        L.debug("email : {}, templateName : {}", email, templateName);

        VerificationCode verificationCode = this.addVerificationCode(email, 1);

        Map<String, Object> infos = new HashMap<>();
        infos.put("email", email);
        infos.put("code", verificationCode.getCode());

        this.emailService.sendEmailWithTemplate(email, templateName, infos);

        return verificationCode;
    }

    @Transactional
    protected VerificationCode addVerificationCode(String email, int delta) {
        L.debug("email : {}, delta {}", email, delta);

        VerificationCode oldVerificationCode = this.verificationCodeRepository.findVerificationCodeByEmail(email);

        String code = this.generateCode();
        VerificationCode verificationCode = new VerificationCode(code, email, delta);

        if (oldVerificationCode != null) {
            verificationCode.setId(oldVerificationCode.getId());
        }

        return this.verificationCodeRepository.save(verificationCode);
    }

    @Transactional
    public boolean correctVerificationCode(String email, String code) throws Exception {
        L.debug("email : {}", email);

        VerificationCode verificationCode = this.verificationCodeRepository.findVerificationCodeByEmail(email);

        if (verificationCode == null || verificationCode.getExpirationDate().before(new Date())) {
            if (verificationCode != null) {
                this.verificationCodeRepository.delete(verificationCode);
            }

            L.error("Verification code expired : email : {}", email);

            this.createAndSendVerificationCode(email, "verification_code_expired");

            throw new VerificationCodeException("Verification code expired.");
        }

        if (!verificationCode.getCode().equals(code)) {
            L.error("Incorrect verification code : email : {}", email);

            throw new VerificationCodeException("Incorrect verification code.");
        }

        this.verificationCodeRepository.delete(verificationCode);

        return true;
    }

}
