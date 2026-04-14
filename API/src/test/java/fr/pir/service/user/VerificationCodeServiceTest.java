package fr.pir.service.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.pir.exception.VerificationCodeException;
import fr.pir.model.user.VerificationCode;
import fr.pir.repository.user.VerificationCodeRepository;
import fr.pir.service.email.EmailService;

@ExtendWith(MockitoExtension.class)
public class VerificationCodeServiceTest {

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private VerificationCodeService verificationCodeService;

    private static final String EMAIL = "user@example.com";

    // ── addVerificationCode ─────────────────────────────────────────────────

    @Test
    void addVerificationCode_savesAndReturnsCode() {
        when(verificationCodeRepository.findVerificationCodeByEmail(EMAIL)).thenReturn(null);
        when(verificationCodeRepository.save(any(VerificationCode.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        VerificationCode code = verificationCodeService.addVerificationCode(EMAIL, 1);

        assertNotNull(code);
        assertNotNull(code.getCode());
        verify(verificationCodeRepository).save(any(VerificationCode.class));
    }

    @Test
    void addVerificationCode_reusesExistingCodeId() {
        VerificationCode existing = new VerificationCode("123456", EMAIL, 1);
        existing.setId(99L);
        when(verificationCodeRepository.findVerificationCodeByEmail(EMAIL)).thenReturn(existing);
        when(verificationCodeRepository.save(any(VerificationCode.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        VerificationCode code = verificationCodeService.addVerificationCode(EMAIL, 1);

        assertNotNull(code);
        verify(verificationCodeRepository).save(any(VerificationCode.class));
    }

    // ── correctVerificationCode ─────────────────────────────────────────────

    @Test
    void correctVerificationCode_success() throws Exception {
        VerificationCode vc = new VerificationCode("654321", EMAIL, 1);
        when(verificationCodeRepository.findVerificationCodeByEmail(EMAIL)).thenReturn(vc);

        boolean result = verificationCodeService.correctVerificationCode(EMAIL, "654321");

        assertTrue(result);
        verify(verificationCodeRepository).delete(vc);
    }

    @Test
    void correctVerificationCode_wrongCode_throwsException() {
        VerificationCode vc = new VerificationCode("654321", EMAIL, 1);
        when(verificationCodeRepository.findVerificationCodeByEmail(EMAIL)).thenReturn(vc);

        assertThrows(VerificationCodeException.class, () ->
                verificationCodeService.correctVerificationCode(EMAIL, "000000"));
    }

    @Test
    void correctVerificationCode_expiredCode_throwsAndSendsNewCode() throws Exception {
        VerificationCode expired = new VerificationCode("111111", EMAIL, 1);
        Calendar past = Calendar.getInstance();
        past.add(Calendar.HOUR, -2);
        expired.setExpirationDate(past.getTime());

        when(verificationCodeRepository.findVerificationCodeByEmail(EMAIL)).thenReturn(expired);
        when(verificationCodeRepository.save(any(VerificationCode.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(emailService).sendEmailWithTemplate(anyString(), anyString(), any());

        assertThrows(VerificationCodeException.class, () ->
                verificationCodeService.correctVerificationCode(EMAIL, "111111"));

        verify(verificationCodeRepository).delete(expired);
    }

    @Test
    void correctVerificationCode_noCodeExists_throwsAndSendsNewCode() throws Exception {
        when(verificationCodeRepository.findVerificationCodeByEmail(EMAIL)).thenReturn(null);
        when(verificationCodeRepository.save(any(VerificationCode.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(emailService).sendEmailWithTemplate(anyString(), anyString(), any());

        assertThrows(VerificationCodeException.class, () ->
                verificationCodeService.correctVerificationCode(EMAIL, "anything"));
    }
}
