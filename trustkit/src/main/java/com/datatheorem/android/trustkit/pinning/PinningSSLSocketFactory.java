package com.datatheorem.android.trustkit.pinning;


import android.net.SSLCertificateSocketFactory;
import android.util.Log;

import com.datatheorem.android.trustkit.TrustKit;
import com.datatheorem.android.trustkit.config.PinnedDomainConfiguration;
import com.datatheorem.android.trustkit.config.TrustKitConfiguration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.TrustManager;

public class PinningSSLSocketFactory extends SSLCertificateSocketFactory {

    // TODO(ad): Figure this out
    public PinningSSLSocketFactory() {
        super(0);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddr, int localPort)
            throws IOException {
        // Force the use of our PinningTrustManager
        setTrustManagers(new TrustManager[]{createTrustManager(host, port)});
        return super.createSocket(host, port, localAddr, localPort);
    }

    @Override
    public Socket createSocket(Socket k, String host, int port, boolean close) throws IOException {
        // Force the use of our PinningTrustManager
        setTrustManagers(new TrustManager[]{createTrustManager(host, port)});
        return super.createSocket(k, host, port, close);
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        // Force the use of our PinningTrustManager
        setTrustManagers(new TrustManager[]{createTrustManager(host, port)});
        return super.createSocket(host, port);
    }

    private static TrustManager createTrustManager(String host, int port) {
        // Get this domain's pinning configuration if any
        TrustKitConfiguration config = TrustKit.getInstance().getConfiguration();
        String notedHostname = host;
        PinnedDomainConfiguration hostConfig = config.get(notedHostname);
        return new PinningTrustManager(host, port, notedHostname, hostConfig);
    }


//    @Override
//    public static void verifyHostname(Socket socket, String hostname) throws IOException {
        // TODO(ad): Send a report if hostname validation failed

//        Log.v("TETEEETET", "Perform hostname validation");
        //SSLCertificateSocketFactory.verifyHostname(socket, hostname);
//    }

}
