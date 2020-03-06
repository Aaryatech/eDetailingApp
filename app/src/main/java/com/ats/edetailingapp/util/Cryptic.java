package com.ats.edetailingapp.util;

import android.content.Context;
import android.util.Log;

import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Cryptic {
	/*used facebook encryption library openssl used libconceal.jar and lib
	 * http://www.ssaurel.com/blog/make-fast-cryptographic-operations-on-android-with-conceal/*/
	private Crypto crypto;
	private Entity entity = new Entity("crypto");
	
	public Cryptic(Context context) {
		crypto = new Crypto(new SharedPrefsBackedKeyChain(context), new SystemNativeCryptoLibrary());
		Log.e("CRYPTO","---------------------"+crypto);
	}

	public void Encrypt(File file, byte[] plainTextBytes) throws Exception {
		if (!crypto.isAvailable()) {
			return;
		}
		OutputStream fileStream = new BufferedOutputStream(new FileOutputStream(file));
		OutputStream outputStream = crypto.getCipherOutputStream(fileStream,entity);
		outputStream.write(plainTextBytes);
		outputStream.close();
	}
	/*/home/pc6/android-ndk-r10d*/
	public void Encrypt(File file, File plainTextFile) throws Exception {
		if (!crypto.isAvailable()) {
			Log.e("ENCRYPT","-------------NOT AVAILABLE---------");
			return;
		}
		Log.e("ENCRYPT","-------------FileInputStream---------");
		FileInputStream fIStream = new FileInputStream(plainTextFile);
		byte[] buffer = new byte[1024];
		OutputStream fileStream = new BufferedOutputStream(new FileOutputStream(file));
		OutputStream outputStream = crypto.getCipherOutputStream(fileStream, entity);
		while (fIStream.read(buffer) != -1) {
			outputStream.write(buffer);
		}
		Log.e("ENCRYPT : ","----------------------------"+file+"______"+plainTextFile);
		outputStream.flush();
		outputStream.close();
		fIStream.close();
	}

	public void Decrypt(File file, File tempFile) throws Exception {
		FileInputStream fileStream = new FileInputStream(file);
		InputStream inputStream = crypto.getCipherInputStream(fileStream, entity);
		int read;
		byte[] buffer = new byte[1024];
		OutputStream out = new FileOutputStream(tempFile);
		while ((read = inputStream.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		out.flush();
		out.close();
		inputStream.close();
	}
}