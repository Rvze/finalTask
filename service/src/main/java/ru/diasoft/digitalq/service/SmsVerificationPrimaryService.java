package ru.diasoft.digitalq.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.diasoft.digitalq.domain.*;
import ru.diasoft.digitalq.repository.SmsVerificationRepository;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Primary
public class SmsVerificationPrimaryService implements SmsVerificationService {

    private final SmsVerificationRepository repository;

    @Override
    public SmsVerificationCheckResponse dsSmsVerificationCheck(SmsVerificationCheckRequest smsVerificationCheckRequest) {

        Optional<SmsVerification> smsVerification = repository.
                findBySecretCodeAndProcessGuidAndStatus(smsVerificationCheckRequest.getCode(),
                        smsVerificationCheckRequest.getProcessGUID(), "OK");
        SmsVerificationCheckResponse response = new SmsVerificationCheckResponse();
        response.setCheckResult(smsVerification.isPresent());

        return response;
    }

    @Override
    public SmsVerificationPostResponse dsSmsVerificationCreate(SmsVerificationPostRequest smsVerificationPostRequest) {
        String guid = UUID.randomUUID().toString();
        String secret = String.format("%4d", new Random().nextInt(10000));
        SmsVerification smsVerification = SmsVerification.builder()
                .phoneNumber(smsVerificationPostRequest.getPhoneNumber())
                .processGuid(guid)
                .secretCode(secret)
                .status("WAITING")
                .build();
        SmsVerification savedEntity = repository.save(smsVerification);

        SmsVerificationPostResponse response = new SmsVerificationPostResponse();
        response.setProcessGUID(savedEntity.getProcessGuid());
        return response;
    }
}
