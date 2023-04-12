package com.example.base.mobilesubscriber.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.base.mobilesubscriber.exception.BadFormatMobileException;
import com.example.base.mobilesubscriber.exception.BadServiceTypeException;
import com.example.base.mobilesubscriber.exception.DuplicateMobileNumberException;
import com.example.base.mobilesubscriber.exception.UnknownMobileNumberException;
import com.example.base.mobilesubscriber.exception.UnknownSubscriberException;
import com.example.base.mobilesubscriber.model.MobileSubscriber;
import com.example.base.mobilesubscriber.repository.MobileSubscriberRepository;

@Service
public class MobileSubscriberService {

    @Autowired
    private MobileSubscriberRepository mobSubsRepository;
    private Logger logger = LoggerFactory.getLogger(MobileSubscriberService.class);

    public enum PaymentType {
        PREPAID("MOBILE_PREPAID"), POSTPAID("MOBILE_POSTPAID");

        private String type;

        PaymentType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    // add a mobile number to the database
    @Transactional
    public MobileSubscriber createMobileSubscriber(MobileSubscriber mobSubs) {
        String msisdn = mobSubs.getMsisdn();
        validateMsisdn(msisdn);
        if (logger.isDebugEnabled()) {
            logger.debug("subscriber mobile number is valid format");
        }
        String serviceType = mobSubs.getServiceType();
        validateServiceType(serviceType);
        if (logger.isDebugEnabled()) {
            logger.debug("subscriber service type is valid");
        }
        Optional<MobileSubscriber> sub = findByMsisdn(msisdn);
        if (sub.isPresent()) {
            logger.error("subscriber mobile number already in database: " + msisdn);
            throw new DuplicateMobileNumberException();
        }
        return mobSubsRepository.save(mobSubs);
    }

    // return all mobile numbers details from the database
    public List<MobileSubscriber> getMobileSubscribers() {
        return mobSubsRepository.findAll();
    }

    // return all mobile numbers details from the database, which match a search
    // criteria
    public List<MobileSubscriber> findMobileSubscribersByCustomerIDUser(Long id) {
        return mobSubsRepository.findByCustomerIdUser(id);
    }

    // return all mobile numbers details from the database, which match a search
    // criteria
    public List<MobileSubscriber> findByServiceType(String serviceType) {
        return mobSubsRepository.findByServiceType(serviceType);
    }

    // return all mobile numbers details from the database, which match a search
    // criteria
    public List<MobileSubscriber> findByServiceStartDate(Long startDate) {
        return mobSubsRepository.findByServiceStartDate(startDate);
    }

    // return all mobile numbers details from the database, which match a search
    // criteria
    public Optional<MobileSubscriber> findByMsisdn(String msidsn) {
        List<MobileSubscriber> subs = mobSubsRepository.findByMsisdn(msidsn);
        Optional<MobileSubscriber> ret = Optional.empty();
        if (!subs.isEmpty()) {
            ret = Optional.of(subs.get(0));
        }
        return ret;
    }

    // return all mobile numbers details from the database, which match a search
    // criteria
    public Optional<MobileSubscriber> findById(Long id) {
        return mobSubsRepository.findById(id);
    }

    @Transactional
    public Optional<MobileSubscriber> deleteMobileSubscribersByMobileNumber(String msisdn) {
        Optional<MobileSubscriber> sub = findByMsisdn(msisdn);
        if (sub.isEmpty()) {
            logger.error("subscriber mobile number unknown in database: " + msisdn);
            throw new UnknownMobileNumberException();
        }
        mobSubsRepository.deleteById(sub.get().getId());
        if (logger.isDebugEnabled()) {
            logger.debug("deleted subscriber from database by mobile number: " + msisdn);
        }
        return sub;
    }

    @Transactional
    public MobileSubscriber assignMobileSubscriberUserAndOwner(Long id, MobileSubscriber mobSubs) {
        Optional<MobileSubscriber> sub = findById(id);
        if (sub.isEmpty()) {
            throw new UnknownSubscriberException();
        }
        MobileSubscriber s = sub.get();
        s.setCustomerIdOwner(mobSubs.getCustomerIdOwner());
        s.setCustomerIdUser(mobSubs.getCustomerIdUser());
        return updateMobileSubscriber(s.getId(), s);
    }

    @Transactional
    public MobileSubscriber changeMobileSubscriberPaymentPlanFromTo(String msisdn, PaymentType from, PaymentType to) {
        Optional<MobileSubscriber> sub = findByMsisdn(msisdn);
        if (sub.isEmpty()) {
            logger.error("subscriber mobile number unknown in database: " + msisdn);
            throw new UnknownMobileNumberException();
        }
        MobileSubscriber s = sub.get();
        if (from.toString().equals(s.getServiceType())) {
            s.setServiceType(to.getType());
            return updateMobileSubscriber(s.getId(), s);
        }
        return s;
    }

    // this is for unit tests
    public void setMobileSubscriberRepository(MobileSubscriberRepository repo) {
        this.mobSubsRepository = repo;
    }

    @Transactional
    private MobileSubscriber updateMobileSubscriber(Long id, MobileSubscriber mobSubsDetails) {
        MobileSubscriber subs = mobSubsRepository.findById(id).get();
        subs.setCustomerIdOwner(mobSubsDetails.getCustomerIdOwner());
        subs.setCustomerIdUser(mobSubsDetails.getCustomerIdUser());
        subs.setId(mobSubsDetails.getId());
        subs.setMsisdn(mobSubsDetails.getMsisdn());
        subs.setServiceStartDate(mobSubsDetails.getServiceStartDate());
        subs.setServiceType(mobSubsDetails.getServiceType());
        if (logger.isDebugEnabled()) {
            logger.debug("subscriber updating in database: " + id);
        }
        return mobSubsRepository.save(mobSubsDetails);
    }

    private void validateMsisdn(String msisdn) {
        if (msisdn == null || msisdn.isEmpty()) {
            throw new BadFormatMobileException();
        }
        if (msisdn.length() != 11) {
            throw new BadFormatMobileException();
        }
        if (!msisdn.matches("[0-9]+")) {
            throw new BadFormatMobileException();
        }
    }

    private void validateServiceType(String serviceType) {
        if (serviceType == null || serviceType.isEmpty()) {
            logger.error("no suscriber service type supplied");
            throw new BadServiceTypeException();
        }
        if (!PaymentType.PREPAID.toString().equals(serviceType)
                && !PaymentType.POSTPAID.toString().equals(serviceType)) {
            logger.error("Invalid suscriber service type supplied: " + serviceType);
            throw new BadServiceTypeException();
        }
    }

}
