package com.example.base.mobilesubscriber.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.base.mobilesubscriber.model.MobileSubscriber;

@Repository
public interface MobileSubscriberRepository extends JpaRepository<MobileSubscriber, Long> {

    List<MobileSubscriber> findByServiceType(String serviceType);

    List<MobileSubscriber> findByServiceStartDate(Long startDate);

    List<MobileSubscriber> findByCustomerIdUser(Long id);

    List<MobileSubscriber> findByMsisdn(String msisdn);

}