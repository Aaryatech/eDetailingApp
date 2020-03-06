package com.ats.edetailingapp.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Utility {

    public static final String FILE_PATH = "FileName";
    public static final String FILES_DOWNLOAD_LIST = "fileDownloadList";
    public static final String SIMPLE_HIDDEN_DIRECTORY = ".qazplm";
    public static final String SIMPLE_HIDDEN_TEMP_DIRECTORY = SIMPLE_HIDDEN_DIRECTORY + "/.asdf";

    public static final String DOWNLOAD_FILES_INTENT = "com.svisionit.marketing.DOWNLOAD_FILES";


    public static String getImei(Context context) {
        String devId;
        try {
            TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if ((devId = mngr.getDeviceId()) == null)
                devId = getMACAddress("wlan0");
        } catch (Exception e) {
            devId = getMACAddress("wlan0");
        }
        return devId;
        //	return "911336450227463";
    }

    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName))
                        continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null)
                    return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0)
                    buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {

        }
        return "";
    }

    public static void deleteFolderContents(String folderName) {
        try {
            Log.d("Larry","here " + folderName);
            File folder = new File(folderName);
            for(String fileName : folder.list()){
                File file = new File(folder, fileName);
                Log.d("Larry","here " + fileName);
                if(file.isDirectory()) {
                    deleteFolderContents(fileName);
                }
                try {
                    Log.d("Larry", "deletion " + file.delete());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }
    }

}
