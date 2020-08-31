package com.chungnv.microservice.service;

import com.chungnv.microservice.model.AccessTokenPayload;
import com.chungnv.microservice.util.AES;
import com.chungnv.microservice.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AccessTokenService {
    @Value( "${microservice.secretKey}" )
    private String secretKey;

    public String generateExternalToken(AccessTokenPayload payload) {
        // set padding so that the token length will be longer
        payload.setPadding("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");

        String payloadJson = CommonUtil.toJson(payload);
        String token = AES.encrypt(payloadJson, secretKey);
        return token;
    }

    public AccessTokenPayload getExternalTokenPayload(String externalToken) {
        String payloadJson = AES.decrypt(externalToken, secretKey);
        AccessTokenPayload payload = CommonUtil.fromJson(payloadJson, AccessTokenPayload.class);
        payload.setPadding(null);
        return payload;
    }

    public static String generateInternalToken(AccessTokenPayload payload) {
        String payloadJson = CommonUtil.toJson(payload);
        String payloadJsonBase64 = Base64.getEncoder().encodeToString(payloadJson.getBytes());
        return payloadJsonBase64;
    }

    public static AccessTokenPayload getInternalTokenPayload(String internalToken) {
        if (StringUtils.isBlank(internalToken)) {
            return new AccessTokenPayload();
        }
        String payloadJson = new String(Base64.getDecoder().decode(internalToken));
        AccessTokenPayload payload = CommonUtil.fromJson(payloadJson, AccessTokenPayload.class);
        return payload;
    }
}
