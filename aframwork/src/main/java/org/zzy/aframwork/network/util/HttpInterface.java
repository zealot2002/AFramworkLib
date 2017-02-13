package org.zzy.aframwork.network.util;

import org.json.JSONException;

public interface HttpInterface {
    interface DataCallback<T> {
        public void requestCallback(int result, Object data, Object tagData);
    }
    interface JsonParser {
        public Object[] parse(String str) throws JSONException;
    }
    interface Decrypter{
        public String decrypt(String s);
    }
    interface Validator {
        void validate(Object obj) throws NetDataInvalidException;
    }
}
