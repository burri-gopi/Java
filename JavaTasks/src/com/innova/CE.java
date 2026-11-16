package com.innova;

import javax.security.auth.Subject;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;

public class CE {

	public static void main(String[] args) {

		try {

			Connection conn = Factory.Connection.getConnection("http://mitsecmsitapp.acs.net:9080/wsi/FNCEWS40MTOM");
			Subject subject = UserContext.createSubject(conn, "fnwasadmin", "BxFX(3b*TeNKynD#Ch)Uk[", "FileNetP8WSI");
			UserContext.get().pushSubject(subject);
			Domain domain = Factory.Domain.fetchInstance(conn, null, null);
			ObjectStore store = Factory.ObjectStore.fetchInstance(domain, "FNOS", null);

			System.out.println("conn--->" + conn.getURI());
			System.out.println("Domain--->" + domain.get_Name());
			System.out.println("objectStore--->" + store.get_Name());

		} catch (Exception e) {
			System.out.println("Exception--->" + e.getMessage());

		}
	}

}