package uk.co.miami_nice.screenshot.net;

import uk.co.miami_nice.screenshot.Driver;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

/**
 * Class used to add the server's certificate to the KeyStore
 * with your trusted certificates.
 */
public class InstallCert {

    private String host;

    private int port = 443;

    private char[] passphrase = "changeit".toCharArray();

    public InstallCert(String host) throws Exception {
        this.host = host;

        init();
    }

    public InstallCert(String host, int port, String passphrase) throws Exception {
        this.host = host;
        this.port = port;
        this.passphrase = passphrase.toCharArray();

        init();
    }

    private void init() throws Exception {
        File file = new File("jssecacerts");
        if (!file.isFile()) {
            char SEP = File.separatorChar;
            File dir = new File(System.getProperty("java.home") + SEP
                    + "lib" + SEP + "security");
            file = new File(dir, "jssecacerts");
            if (!file.isFile()) {
                file = new File(dir, "cacerts");
            }
        }
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest("Loading KeyStore " + file + "...");
        InputStream in = new FileInputStream(file);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, passphrase);
        in.close();

        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory factory = context.getSocketFactory();

        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest("Opening connection to " + host + ":" + port + "...");
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        socket.setSoTimeout(10000);
        try {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest("Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest("No errors, certificate is already trusted");
        } catch (SSLException e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest(e.getMessage());
        }

        X509Certificate[] chain = tm.chain;
        if (chain == null) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest("Could not obtain server certificate chain");
            return;
        }

        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest("Server sent " + chain.length + " certificate(s):");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest
                    (" " + (i + 1) + " Subject " + cert.getSubjectDN());
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest("   Issuer  " + cert.getIssuerDN());
            sha1.update(cert.getEncoded());
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest("   sha1    " + toHexString(sha1.digest()));
            md5.update(cert.getEncoded());
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest("   md5     " + toHexString(md5.digest()));
        }

        X509Certificate cert = chain[0];
        String alias = host + "-" + (1);
        ks.setCertificateEntry(alias, cert);

        OutputStream out = new FileOutputStream(Driver.getConfig().getSSL_TRUST_STORE_LOCATION());
        ks.store(out, passphrase);
        out.close();

        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest(String.valueOf(cert));
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest
                ("Added certificate to keystore 'jssecacerts' using alias '"
                        + alias + "'");
    }

    private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        for (int b : bytes) {
            b &= 0xff;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
            sb.append(' ');
        }
        return sb.toString();
    }

    private static class SavingTrustManager implements X509TrustManager {

        private final X509TrustManager tm;
        private X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        public X509Certificate[] getAcceptedIssuers() {
            throw new UnsupportedOperationException();
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }

}
