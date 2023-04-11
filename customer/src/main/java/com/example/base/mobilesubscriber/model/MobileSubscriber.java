package com.example.base.mobilesubscriber.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "mobilesubscriber")
public class MobileSubscriber {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

	@Version
    @Column(name="version")
	private Long version;

    @Column(name="msisdn")
    private String msisdn;
    
    @Column(name="customer_id_owner")
    private Long customerIdOwner;

    @Column(name="customer_id_user")
    private Long customerIdUser;

    @Column(name="service_type")
    private String serviceType;
    
    @Column(name="service_start_date")
    private Long serviceStartDate;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

    public Long getId() {
    	return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Long getCustomerIdOwner() {
		return customerIdOwner;
	}

	public void setCustomerIdOwner(Long customerIdOwner) {
		this.customerIdOwner = customerIdOwner;
	}

	public Long getCustomerIdUser() {
		return customerIdUser;
	}

	public void setCustomerIdUser(Long customerIdUser) {
		this.customerIdUser = customerIdUser;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Long getServiceStartDate() {
		return serviceStartDate;
	}

	public void setServiceStartDate(Long serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerIdOwner, customerIdUser, id, msisdn, serviceStartDate, serviceType, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MobileSubscriber other = (MobileSubscriber) obj;
		return Objects.equals(customerIdOwner, other.customerIdOwner)
				&& Objects.equals(customerIdUser, other.customerIdUser) && Objects.equals(id, other.id)
				&& Objects.equals(msisdn, other.msisdn) && Objects.equals(serviceStartDate, other.serviceStartDate)
				&& Objects.equals(serviceType, other.serviceType) && Objects.equals(version, other.version);
	}

}
