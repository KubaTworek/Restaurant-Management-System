package pl.jakubtworek.auth;

class SecurityConstants {
    static String JWT_KEY = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4";
    String JWT_HEADER = "Authorization";
    Long JWT_EXPIRE_TIME = System.currentTimeMillis() + 10800000;
}
