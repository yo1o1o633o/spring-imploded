package com.imploded.push.apns;

import com.imploded.push.utils.P12Util;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class ApnsClientBuilder {
    private InetSocketAddress apnsServerAddress;

    private X509Certificate clientCertificate;
    private PrivateKey privateKey;
    private String privateKeyPassword;

    public static final String DEVELOPMENT_APNS_HOST = "api.sandbox.push.apple.com";

    public static final String PRODUCTION_APNS_HOST = "api.push.apple.com";

    public static final int DEFAULT_APNS_PORT = 443;

    public ApnsClientBuilder setApnsServer(final String hostname) {
        return this.setApnsServer(hostname, DEFAULT_APNS_PORT);
    }

    public ApnsClientBuilder setApnsServer(final String hostname, final int port) {
        this.apnsServerAddress = InetSocketAddress.createUnresolved(hostname, port);
        return this;
    }

    public ApnsClientBuilder setClientCredentials(final InputStream p12InputStream, final String p12Password) throws SSLException, IOException {
        final X509Certificate x509Certificate;
        final PrivateKey privateKey;

        try {
            final KeyStore.PrivateKeyEntry privateKeyEntry = P12Util.getFirstPrivateKeyEntryFromP12InputStream(p12InputStream, p12Password);

            final Certificate certificate = privateKeyEntry.getCertificate();

            if (!(certificate instanceof X509Certificate)) {
                throw new KeyStoreException("Found a certificate in the provided PKCS#12 file, but it was not an X.509 certificate.");
            }

            x509Certificate = (X509Certificate) certificate;
            privateKey = privateKeyEntry.getPrivateKey();
        } catch (final KeyStoreException e) {
            throw new SSLException(e);
        }

        return this.setClientCredentials(x509Certificate, privateKey, p12Password);
    }

    public ApnsClientBuilder setClientCredentials(final X509Certificate clientCertificate, final PrivateKey privateKey, final String privateKeyPassword) {
        this.clientCertificate = clientCertificate;
        this.privateKey = privateKey;
        this.privateKeyPassword = privateKeyPassword;

        return this;
    }

    public ApnsClient build() throws SSLException {
        if (this.apnsServerAddress == null) {
            throw new IllegalStateException("No APNs server address specified.");
        }
        return null;
    }
}
