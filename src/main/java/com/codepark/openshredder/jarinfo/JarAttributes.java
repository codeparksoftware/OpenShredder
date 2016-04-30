package com.codepark.openshredder.jarinfo;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JarAttributes {
	private Manifest manifest;
	private String vendor;
	private String productName;
	private String version;
	private String licence;
	private String appName;
	private String updatePath;
	private JarURLConnection jarConnection;

	private static final Logger logger = Logger.getLogger(JarAttributes.class.getName());

	public JarAttributes(URL url) {

		try {

			jarConnection = (JarURLConnection) url.openConnection();
			JarFile jarFile = jarConnection.getJarFile();
			this.setManifest(jarFile.getManifest());
			setVendor(getInfo("Specification-Vendor"));
			setVersion(getInfo("Specification-Version"));
			setLicence(getInfo("Licence"));
			setProductName(getInfo("Specification-Title"));
			setAppName(getInfo("Name"));
			setUpdatePath(getInfo("Update-Path"));
			jarFile.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	private String getInfo(String attr) {
		java.util.jar.Attributes attrs = (java.util.jar.Attributes) getManifest().getMainAttributes();

		for (Iterator it = (Iterator) attrs.keySet().iterator(); ((Iterator) it).hasNext();) {
			java.util.jar.Attributes.Name attrName = (java.util.jar.Attributes.Name) it.next();
			if (attrName.toString().equals(attr))
				return attrs.getValue(attrName);

		}
		return null;
	}

	private void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getVendor() {
		return vendor;
	}

	public String getLicence() {
		return licence;
	}

	private void setLicence(String licence) {
		this.licence = licence;
	}

	private void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public String getProductName() {
		return productName;
	}

	private void setProductName(String productName) {
		this.productName = productName;
	}

	protected void finalize() throws Throwable {
		if (jarConnection != null) {
			jarConnection = null;
		}
		if (getManifest() != null) {
			setManifest(null);
		}
	}

	private Manifest getManifest() {
		return manifest;
	}

	private void setManifest(Manifest manifest) {
		this.manifest = manifest;
	}

	public String getAppName() {
		return appName;
	}

	private void setAppName(String appName) {
		this.appName = appName;
	}

	public String getUpdatePath() {
		return updatePath;
	}

	private void setUpdatePath(String updatePath) {
		this.updatePath = updatePath;
	}

}
