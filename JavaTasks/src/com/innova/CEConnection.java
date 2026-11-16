package com.innova;


import javax.security.auth.Subject;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;

public class CEConnection {

	    static String uri = "http://mitsecmsitapp.acs.net:9080/wsi/FNCEWS40MTOM/";
	    static String username = "fnwasadmin";
	    static String password = "BxFX(3b*TeNKynD#Ch)Uk[";

	  
	    public static Connection ceconnection() {
	        Connection conn = null;
	        try {
	            conn = Factory.Connection.getConnection(uri);

	            // Create and establish the security context globally on this thread
	            Subject subject = UserContext.createSubject(conn, username, password, "FileNetP8WSI");
	            UserContext.get().pushSubject(subject);

	            System.out.println("conn--->" + conn.getURI());
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e; // Fail fast if connection fails
	        }
	        return conn;
	    }

	   
	    public static Domain domain(Connection connection, String domainName) {
	        Domain domain = null;
	        try {
	            domain = Factory.Domain.fetchInstance(connection, domainName, null);
	            System.out.println("Domain--->" + domain.get_Name());
	        } catch (Exception e) {
	            System.err.println("domain error--->" + e.getMessage());
	            throw e;
	        }
	        return domain;
	    }

	   
	    public static ObjectStore objectStore(Domain domain, String storeName) {
	        ObjectStore objectStore = null;
	        try {
	            // FIX: Fetch instance directly using the Domain object context passed in
	            objectStore = Factory.ObjectStore.fetchInstance(domain, storeName, null);
	            System.out.println("objectStore--->" + objectStore.get_Name());
	        } catch (Exception e) {
	            System.err.println("objectStore error--->" + e.getMessage());
	            throw e;
	        }
	        return objectStore;
	    }


	   
	    public static void main(String[] args) {
	        boolean subjectPushed = false;

	        try {
	            // 1. Initialize connection and push subject to current thread
	            Connection con = CEConnection.ceconnection();
	            subjectPushed = true;

	            // 2. Fetch domain passing the valid connection variable
	            Domain dom = CEConnection.domain(con, "P8DOMAIN");

	            // 3. Fetch object store passing the valid domain variable
	            ObjectStore os = CEConnection.objectStore(dom, "FNOS");
	            

	        } catch (Exception e) {
	            System.err.println("Pipeline failed: " + e.getMessage());
	        } finally {
	            // 4. FIX: The pop occurs safely at the very end of your program execution context
	            if (subjectPushed) {
	                UserContext.get().popSubject();
	                System.out.println("Security context safely popped from thread.");
	            }
	        }
	    }
	}
