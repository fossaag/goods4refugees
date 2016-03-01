/**
 * Copyright (c) 2015 Frank Kaddereit, Anne Lachnitt, http://www.fossa.de/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.fossa.goods4refugees.core;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "abgabestelle")
@NamedQueries({ @NamedQuery(name = "org.fossa.goods4refugees.core.Abgabestelle.findAll", query = "SELECT a FROM Abgabestelle a ORDER BY name") })
public class Abgabestelle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "anschrift", nullable = false)
	private String anschrift;

	@Column(name = "oeffnungszeiten", nullable = false)
	private String oeffnungszeiten;

	@Column(name = "ansprechpartner", nullable = false)
	private String ansprechpartner;

	@Column(name = "telefon", nullable = false)
	private String telefon;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "internet", nullable = false)
	private String internet;

	@Column(name = "kurzvorstellung", nullable = false)
	private String kurzvorstellung;

	@ManyToOne
	@JoinColumn(name = "ort_id")
	private Ort ort;

	@ManyToOne
	@JoinColumn(name = "backendUser_id")
	private BackendUser backendUser;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAnschrift() {
		return anschrift;
	}

	public void setAnschrift(String anschrift) {
		this.anschrift = anschrift;
	}

	public String getOeffnungszeiten() {
		return oeffnungszeiten;
	}

	public void setOeffnungszeiten(String oeffnungszeiten) {
		this.oeffnungszeiten = oeffnungszeiten;
	}

	public String getAnsprechpartner() {
		return ansprechpartner;
	}

	public void setAnsprechpartner(String ansprechpartner) {
		this.ansprechpartner = ansprechpartner;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInternet() {
		return internet;
	}

	public void setInternet(String internet) {
		this.internet = internet;
	}

	public String getKurzvorstellung() {
		return kurzvorstellung;
	}

	public void setKurzvorstellung(String kurzvorstellung) {
		this.kurzvorstellung = kurzvorstellung;
	}

	public Ort getOrt() {
		return ort;
	}

	public void setOrt(Ort ort) {
		this.ort = ort;
	}

	public BackendUser getBackendUser() {
		return backendUser;
	}

	public void setBackendUser(BackendUser backendUser) {
		this.backendUser = backendUser;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Abgabestelle))
			return false;

		final Abgabestelle that = (Abgabestelle) o;

		return Objects.equals(this.id, that.id)
				&& Objects.equals(this.name, that.name)
				&& Objects.equals(this.anschrift, that.anschrift)
				&& Objects.equals(this.oeffnungszeiten, that.oeffnungszeiten);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, anschrift, oeffnungszeiten);
	}

}