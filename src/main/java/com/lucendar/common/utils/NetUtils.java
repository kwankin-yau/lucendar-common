package com.lucendar.common.utils;

import info.gratour.common.error.ErrorWithCode;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class NetUtils {

    public static boolean isValidPublicId(String ip) {
        InetAddress addr = null;

        try {
            addr = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            return false;
        }

        return !(
                addr.isSiteLocalAddress() ||
                        addr.isAnyLocalAddress() ||
                        addr.isLinkLocalAddress() ||
                        addr.isLoopbackAddress() ||
                        addr.isMulticastAddress());
    }

    public static boolean isValidIpV4Addr(String ipv4) {
        return InetAddressValidator.getInstance().isValidInet4Address(ipv4);
    }

    public static boolean isValidIpV6Addr(String ipv6) {
        return InetAddressValidator.getInstance().isValidInet6Address(ipv6);
    }

    public static boolean isValidIp(String ip) {
        return InetAddressValidator.getInstance().isValid(ip);
    }

    public static boolean isLocalAddr(String addr) {
        try {
            // Check if the address is a valid special local or loop back
            InetAddress inetAddr = InetAddress.getByName(addr);
            if (inetAddr.isAnyLocalAddress() || inetAddr.isLoopbackAddress())
                return true;

            // Check if the address is defined on any interface
            return NetworkInterface.getByInetAddress(inetAddr) != null;
        } catch (Throwable t) {
            return false;
        }
    }

    public static boolean isValidPortNum(int port) {
        return port > 0 && port < 65536;
    }

    public static final DomainValidator DomainValidatorAllowLocal = DomainValidator.getInstance(true);

    public static boolean isValidDomain(String domain) {
        return DomainValidatorAllowLocal.isValid(domain);
    }

    public static boolean isValidIpOrDomain(String ipOrDomain) {
        if (ipOrDomain == null || ipOrDomain.isEmpty())
            return false;
        else {
            if (Character.isDigit(ipOrDomain.charAt(0))) {
                boolean r = isValidIp(ipOrDomain);
                if (!r)
                    r = isValidDomain(ipOrDomain);
                return r;
            } else {
                boolean r = DomainValidatorAllowLocal.isValid(ipOrDomain);
                if (!r)
                    r = isValidIp(ipOrDomain);
                return r;
            }
        }
    }

    /**
     * Parse a socket address string like {ip}:{port}, or {host}:{port}, support IPv6.
     *
     * @param s a socket address string like {ip}:{port}, or {host}:{port}, support IPv6.
     * @return parsed socket address. return null if parse failed.
     */
    public static InetSocketAddress parseSocketAddr(String s) {
        try {
            int idx = s.lastIndexOf(":");
            if (idx < 0)
                return null;

            String portStr = s.substring(idx + 1);
            Integer port = StringUtils.tryParseInt(portStr);
            if (port == null || !isValidPortNum(port))
                return null;

            String hostStr = s.substring(0, idx);
            return InetSocketAddress.createUnresolved(hostStr, port);
        } catch (Throwable t) {
            return null;
        }
    }

    public static class SocketAddr {
        private final String host;
        private final int port;

        public SocketAddr(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String host() {
            return host;
        }

        public int port() {
            return port;
        }

        public InetSocketAddress toSocketAddress() {
            return new InetSocketAddress(host, port);
        }

        public InetSocketAddress toSocketAddressUnresolved() {
            return InetSocketAddress.createUnresolved(host, port);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", SocketAddr.class.getSimpleName() + "[", "]")
                    .add("host='" + host + "'")
                    .add("port=" + port)
                    .toString();
        }
    }

    /**
     * Parse a socket address string like {ip}[:{port}], or {host}[:{port}] where port part is optional, support IPv6.
     *
     * @param s a socket address string like {ip}[:{port}], or {host}[:{port}] where port part is optional, support IPv6.
     * @return parsed socket address. return null if parse failed.
     */
    public static SocketAddr parseSocketAddrPortDef(String s, int defaultPortNum) {
        try {
            int idx = s.lastIndexOf(":");
            if (idx < 0)
                return new SocketAddr(s, defaultPortNum);

            String portStr = s.substring(idx + 1);
            Integer port = StringUtils.tryParseInt(portStr);
            if (port == null || !isValidPortNum(port))
                return null;

            String hostStr = s.substring(0, idx);
            return new SocketAddr(hostStr, port);
        } catch (Throwable t) {
            return null;
        }
    }

    public static class HttpUtils {
        public static String calcBasicAuthorization(String username, String password, Charset charset) {
            byte[] bytes = (username + ":" + password).getBytes(charset);
            return "Basic " + Base64.getEncoder().encodeToString(bytes);
        }

        public static String calcBasicAuthorization(String username, String password) {
            return calcBasicAuthorization(username, password, StandardCharsets.US_ASCII);
        }

        public static String calcDigestAuthorization(
                String username,
                String password,
                String realm,
                String nonce,
                String httpMethod,
                String digestUri) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                String ha1 = Hex.encodeHexString(md5.digest((username + ":" + realm + ":" + password).getBytes()));

                md5.reset();
                String ha2 = Hex.encodeHexString(md5.digest((httpMethod + ":" + digestUri).getBytes()));

                md5.reset();
                String response = Hex.encodeHexString(md5.digest((ha1 + ":" + nonce + ":" + ha2).getBytes()));

                return String.format(
                        "Digest username=\"%s\", " +
                                "realm=\"%s\", " +
                                "nonce=\"%s\", " +
                                "uri=\"%s\", " +
                                "response=\"%s\"",
                        username, realm, nonce, digestUri, response);
            } catch (Exception t) {
                throw new RuntimeException(t);
            }
        }

        public static class BasicHttpAuthorization {
            private final String username;
            private final String password;

            public BasicHttpAuthorization(String username, String password) {
                this.username = username;
                this.password = password;
            }

            public String getUsername() {
                return username;
            }

            public String getPassword() {
                return password;
            }

            public String username() {
                return username;
            }

            public String password() {
                return password;
            }

            @Override
            public String toString() {
                return new StringJoiner(", ", BasicHttpAuthorization.class.getSimpleName() + "[", "]")
                        .add("username='" + username + "'")
                        .add("password='" + password + "'")
                        .toString();
            }
        }

        public static class HttpAuthorization {
            private final String scheme;
            private final String[] params;

            public HttpAuthorization(String scheme, String[] params) {
                this.scheme = scheme;
                this.params = params;
            }

            public String getScheme() {
                return scheme;
            }

            public String[] getParams() {
                return params;
            }

            public String scheme() {
                return scheme;
            }

            public String[] params() {
                return params;
            }

            public boolean isBasic() {
                return "basic".equals(scheme.toLowerCase());
            }

            public boolean isDigest() {
                return "digest".equals(scheme.toLowerCase());
            }

            public BasicHttpAuthorization asBasic() {
                if (!isBasic())
                    throw ErrorWithCode.internalError("Non Basic authorization");

                if (params == null || params.length == 0)
                    throw ErrorWithCode.invalidParam("authorization");

                String s = params[0];
                String concatenation = new String(Base64.getDecoder().decode(s), StandardCharsets.US_ASCII);
                int p = concatenation.indexOf(':');
                if (p <= 0)
                    throw ErrorWithCode.invalidParam("authorization");

                return new BasicHttpAuthorization(
                        concatenation.substring(0, p),
                        concatenation.substring(p + 1)
                );
            }

            @Override
            public String toString() {
                return new StringJoiner(", ", HttpAuthorization.class.getSimpleName() + "[", "]")
                        .add("scheme='" + scheme + "'")
                        .add("params=" + Arrays.toString(params))
                        .toString();
            }
        }

        public static HttpAuthorization parseAuthorization(String authorization) {
            String a = authorization.trim();
            int p = a.indexOf(' ');
            if (p <= 0)
                throw ErrorWithCode.invalidParam("authorization");

            String scheme = a.substring(0, p);
            String s2 = a.substring(p + 1);
            String[] params = s2.split(",");
            return new HttpAuthorization(scheme, params);
        }

        public static Map<String, String> decodeQueryString(String rawQueryStr) {
            if (rawQueryStr == null)
                return null;

            Map<String, String> r = new HashMap<>();
            for (String s : rawQueryStr.split("&")) {
                String[] kv = s.split("=", 2);
                if (kv.length == 2) {
                    r.put(kv[0], kv[1]);
                } else if (kv.length > 0) {
                    r.put(kv[0], "1");
                }
            }
            return r;
        }

        public static MultiValueMap decodeQueryStrMultiValue(String rawQueryStr) {
            if (rawQueryStr == null)
                return null;

            MultiValueMap r = new MultiValueMap();
            for (String s : rawQueryStr.split("&")) {
                String[] kv = s.split("=", 2);
                if (kv.length == 2) {
                    r.put(kv[0], kv[1]);
                } else if (kv.length > 0) {
                    r.put(kv[0], "1");
                }
            }

            return r;
        }
    }
}
