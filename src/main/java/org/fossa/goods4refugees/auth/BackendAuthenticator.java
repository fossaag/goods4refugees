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

package org.fossa.goods4refugees.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.fossa.goods4refugees.core.BackendUser;
import org.fossa.goods4refugees.db.BackendUserDAO;

import com.google.common.base.Optional;

public class BackendAuthenticator implements Authenticator<BasicCredentials, BackendUser> {
    private final BackendUserDAO backendUserDAO;

	public BackendAuthenticator(BackendUserDAO backendUserDAO) {
		this.backendUserDAO = backendUserDAO;
	}

	@Override
    public Optional<BackendUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
		try {
			for (BackendUser backendUser : backendUserDAO.findAll()) {
				if (backendUser.getName().equals(credentials.getUsername())) {
					byte[] bytesOfOPassword = credentials.getPassword().getBytes("UTF-8");
					if (getHash(bytesOfOPassword).equals(backendUser.getMd5password())) {
						return Optional.of(backendUser);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Optional.absent();
    }
        
	public static String getHash(byte[] bytes) {
		MessageDigest md = getMessageDigest();
        byte[] messageDigest = md.digest(bytes);
        BigInteger number = new BigInteger(1, messageDigest);
        String hashtext = number.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
	}

	private static MessageDigest getMessageDigest() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
