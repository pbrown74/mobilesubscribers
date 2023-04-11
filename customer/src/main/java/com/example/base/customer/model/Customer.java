package com.example.base.customer.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "customer")
public class Customer {
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="id")
        private Long id;

		@Version
        @Column(name="version")
		private Long version;

        @Column(name="id_card")
        private String idCard;
        
        @Column(name="name")
        private String name;
        
        @Column(name="surname")
        private String surname;
        
        @Column(name="address")
        private String address;

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

		public String getIdCard() {
			return idCard;
		}

		public void setIdCard(String idCard) {
			this.idCard = idCard;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSurname() {
			return surname;
		}

		public void setSurname(String surname) {
			this.surname = surname;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		@Override
		public int hashCode() {
			return Objects.hash(address, id, idCard, name, surname, version);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Customer other = (Customer) obj;
			return Objects.equals(address, other.address) && Objects.equals(id, other.id)
					&& Objects.equals(idCard, other.idCard) && Objects.equals(name, other.name)
					&& Objects.equals(surname, other.surname) && Objects.equals(version, other.version);
		}
		
}
