package ru.diasoft.digitalq.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.diasoft.digitalq.domain.*;
import ru.diasoft.digitalq.repository.SmsVerificationRepository;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class SmsVerificationPrimaryServiceTest {

    @Mock
    private SmsVerificationRepository repository;

    @Autowired
    private SmsVerificationPrimaryService service;

    private final String PHONE_NUMBER = "+79991740271";
    private final String VALID_CODE = "12345";
    private final String NOT_VALID_CODE = "11111";
    private final String GUID = UUID.randomUUID().toString();
    private final String STATUS_OK = "OK";

    @Before
    public void init() {
        service = new SmsVerificationPrimaryService(repository);
        SmsVerification smsVerification = SmsVerification.builder()
                .processGuid(UUID.randomUUID().toString())
                .phoneNumber(PHONE_NUMBER)
                .secretCode(VALID_CODE)
                .status(STATUS_OK)
                .build();
    }

    @Test
    void dsSmsVerificationCheck() {
        SmsVerificationCheckRequest smsVerificationCheckRequest = new SmsVerificationCheckRequest();
        smsVerificationCheckRequest.setProcessGUID(GUID);
        smsVerificationCheckRequest.setCode(VALID_CODE);
        SmsVerificationCheckResponse response = service.dsSmsVerificationCheck(smsVerificationCheckRequest);

        assertThat(response).isNotNull();
        assertThat(response.getCheckResult()).isTrue();


    }

    @Test
    void dsSmsVerificationCreate() {
        log.info("Service " + service);
        SmsVerificationPostRequest smsVerificationPostRequest = new SmsVerificationPostRequest();
        smsVerificationPostRequest.setPhoneNumber(PHONE_NUMBER);
        SmsVerificationPostResponse response = service.dsSmsVerificationCreate(smsVerificationPostRequest);

        assertThat(response).isNotNull();
        assertThat(response.getProcessGUID()).isNotEmpty();
    }

    @Test
    void dsSmsVerificationCheck2() {
        SmsVerificationCheckRequest smsVerificationCheckRequest = new SmsVerificationCheckRequest();
        smsVerificationCheckRequest.setProcessGUID(GUID);
        smsVerificationCheckRequest.setCode(NOT_VALID_CODE);
        SmsVerificationCheckResponse response = service.dsSmsVerificationCheck(smsVerificationCheckRequest);

        assertThat(response).isNotNull();
        assertThat(response.getCheckResult()).isFalse();
    }
}
