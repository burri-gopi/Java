package com.innova;

import java.io.File;
import java.io.FileInputStream;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;

public class UploadDocuments_FN {
	
	
	public static void uploadDocument(ObjectStore os, String localFilePath, String docTitle, String targetFolderPath) throws Exception {
        File file = new File(localFilePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("The specified local file does not exist: " + localFilePath);
        }

        // 1. Instantiate a new document of the default "Document" base class
        Document doc = Factory.Document.createInstance(os, "Document");

        // 2. Set Custom Metadata/Properties
        doc.getProperties().putValue("DocumentTitle", docTitle);
        doc.set_MimeType("application/pdf"); // Explicitly define content type

        // 3. Attach file data using ContentTransfer element
        ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
        FileInputStream fileStream = new FileInputStream(file);
        
        contentTransfer.setCaptureSource(fileStream);
        contentTransfer.set_RetrievalName(file.getName());

        // Create a container list, append content element, and bind to document
        ContentElementList contentList = Factory.ContentElement.createList();
        contentList.add(contentTransfer);
        doc.set_ContentElements(contentList);

        // 4. Check-in document as a major production version
        doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
        doc.save(RefreshMode.REFRESH); // Save initial object configuration to engine

        // 5. File the document into the target FileNet folder path
        Folder folder = Factory.Folder.fetchInstance(os, targetFolderPath, null);
        ReferentialContainmentRelationship rcr = folder.file(
                doc, 
                AutoUniqueName.AUTO_UNIQUE, 
                docTitle, 
                DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE
        );
        rcr.save(RefreshMode.NO_REFRESH); // Finalise location assignment transaction
        
        // Close file stream safely
        fileStream.close();
    }
	
	
	public static void main(String[] args) {
		
		CEConnection connection=new CEConnection();
		
	}
	
	
}



