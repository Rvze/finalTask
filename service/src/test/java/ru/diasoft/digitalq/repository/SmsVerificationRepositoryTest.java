package ru.diasoft.digitalq.repository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.diasoft.digitalq.domain.SmsVerification;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class SmsVerificationRepositoryTest {
    @Autowired
    private SmsVerificationRepository repository;

    @Test
    void smsVerificationCreateTest() {
        SmsVerification smsVerification = SmsVerification.builder()
                .processGuid(UUID.randomUUID().toString())
                .phoneNumber("65432")
                .secretCode("666")
                .status("waiting")
                .build();

        SmsVerification savedEntity = repository.save(smsVerification);
        assertThat(savedEntity.getVerificationId()).isNotNull();
    }

    @Test
    void findBySecretCodeAndProcessGuidAndStatusTest() {
        SmsVerification smsVerification = SmsVerification.builder()
                .processGuid(UUID.randomUUID().toString())
                .phoneNumber("65432")
                .secretCode("666")
                .status("waiting")
                .build();

        SmsVerification savedEntity = repository.save(smsVerification);
        assertThat(repository.findBySecretCodeAndProcessGuidAndStatus("666",
                        savedEntity.getProcessGuid(), savedEntity.getStatus()).
                orElse(SmsVerification.builder().build())).isEqualTo(savedEntity);
        assertThat(repository.findBySecretCodeAndProcessGuidAndStatus("666",
                savedEntity.getProcessGuid(), savedEntity.getStatus())).isEmpty();
    }

    @Test
    public void updateStatusByProcessGuidTest() {
        SmsVerification smsVerification = SmsVerification.builder()
                .processGuid(UUID.randomUUID().toString())
                .phoneNumber("65432")
                .secretCode("666")
                .status("waiting")
                .build();

        SmsVerification createdEntity = repository.save(smsVerification);

        repository.updateStatusByProcessGuid("OK", smsVerification.getProcessGuid());
        assertThat(repository.findById(createdEntity.getVerificationId())
                .orElse(SmsVerification.builder().build()).getStatus()).isEqualTo("OK");
    }
}