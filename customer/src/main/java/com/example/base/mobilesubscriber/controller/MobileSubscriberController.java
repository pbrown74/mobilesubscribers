package com.example.base.mobilesubscriber.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.base.mobilesubscriber.model.MobileSubscriber;
import com.example.base.mobilesubscriber.service.MobileSubscriberService;
import com.example.base.mobilesubscriber.service.MobileSubscriberService.PaymentType;

/**
 * This class is just delegating to the service layer, its here to define the
 * mappings for REST endpoints only
 * 
 * @author User
 *
 */
@RestController
@RequestMapping("/api")
public class MobileSubscriberController {

    @Autowired
    private MobileSubscriberService mobSubsService;

    // return all mobile numbers details from database
    @RequestMapping(value = "/mobilesubscriber", method = RequestMethod.GET)
    public List<MobileSubscriber> getMobileSubscribers() {
        return mobSubsService.getMobileSubscribers();
    }

    // return all mobile numbers details that match a criteria
    @RequestMapping(value = "/mobilesubscriber/find-by-st/{serviceType}", method = RequestMethod.GET)
    public List<MobileSubscriber> findMobileSubscribersByServiceType(
            @PathVariable(value = "serviceType") String serviceType) {
        return mobSubsService.findByServiceType(serviceType);
    }

    // return all mobile numbers details that match a criteria
    @RequestMapping(value = "/mobilesubscriber/find-by-sd/{serviceStartDate}", method = RequestMethod.GET)
    public List<MobileSubscriber> findMobileSubscribersByServiceStartDate(
            @PathVariable(value = "serviceStartDate") Long serviceStartDate) {
        return mobSubsService.findByServiceStartDate(serviceStartDate);
    }

    // return all mobile numbers details that match a criteria
    @RequestMapping(value = "/mobilesubscriber/find-by-user-id/{customerIdUser}", method = RequestMethod.GET)
    public List<MobileSubscriber> findMobileSubscribersByCustomerIdUser(
            @PathVariable(value = "customerIdUser") Long customerIdUser) {
        return mobSubsService.findMobileSubscribersByCustomerIDUser(customerIdUser);
    }

    // return all mobile numbers details that match a criteria
    @RequestMapping(value = "/mobilesubscriber/find-by-msisdn/{msisdn}", method = RequestMethod.GET)
    public Optional<MobileSubscriber> findMobileSubscribersByMobileNumber(
            @PathVariable(value = "msisdn") String msisdn) {
        return mobSubsService.findByMsisdn(msisdn);
    }

    // add a mobile number to the database
    @RequestMapping(value = "/mobilesubscriber", method = RequestMethod.POST)
    public MobileSubscriber createMobileSubscriber(@RequestBody MobileSubscriber mobSubs) {
        return mobSubsService.createMobileSubscriber(mobSubs);
    }

    // change a mobile number plan from postpaid to prepaid
    @RequestMapping(value = "/mobilesubscriber/change-to-prepaid/{msisdn}", method = RequestMethod.PUT)
    public MobileSubscriber changeMobileSubscriberPaymentPlanToPrepaid(@PathVariable(value = "msisdn") String msisdn) {
        return mobSubsService.changeMobileSubscriberPaymentPlanFromTo(msisdn, PaymentType.POSTPAID,
                PaymentType.PREPAID);
    }

    // change a mobile number plan from prepaid to postpaid
    @RequestMapping(value = "/mobilesubscriber/change-to-postpaid/{msisdn}", method = RequestMethod.PUT)
    public MobileSubscriber changeMobileSubscriberPaymentPlanToPostpaid(@PathVariable(value = "msisdn") String msisdn) {
        return mobSubsService.changeMobileSubscriberPaymentPlanFromTo(msisdn, PaymentType.PREPAID,
                PaymentType.POSTPAID);
    }

    // delete a mobile number from the database
    @RequestMapping(value = "/mobilesubscriber/delete-by-msisdn/{msisdn}", method = RequestMethod.DELETE)
    public Optional<MobileSubscriber> deleteMobileSubscribersByMobileNumber(
            @PathVariable(value = "msisdn") String msisdn) {
        return mobSubsService.deleteMobileSubscribersByMobileNumber(msisdn);
    }

    // assign a user and owner to a subscriber
    @RequestMapping(value = "/mobilesubscriber/assign/{subsId}", method = RequestMethod.PUT)
    public MobileSubscriber assignMobileSubscriberUserAndOwner(@PathVariable(value = "subsId") Long id,
            @RequestBody MobileSubscriber mobSubs) {
        return mobSubsService.assignMobileSubscriberUserAndOwner(id, mobSubs);
    }

}