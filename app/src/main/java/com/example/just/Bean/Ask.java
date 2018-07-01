package com.example.just.Bean;

/**
 * Created by yg on 18-6-30.
 */

public class Ask {
    /**
     * reqType : 0
     * perception : {"inputText":{"text":"你叫什么"}}
     * userInfo : {"apiKey":"79f03711ab3d4e9cb92380a43d2a7d0e","userId":"285381"}
     */
    private int reqType;
    private PerceptionBean perception;
    private UserInfoBean userInfo;

    public Ask() {

    }

    public Ask(PerceptionBean perception) {
        this.perception = perception;
    }

    public int getReqType() {
        return reqType;
    }

    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    public PerceptionBean getPerception() {
        return perception;
    }

    public void setPerception(PerceptionBean perception) {
        this.perception = perception;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class PerceptionBean {
        /*"inputText":{"text":"你叫什么"}*/

        private InputTextBean inputText;

        public PerceptionBean(InputTextBean inputText) {
            this.inputText = inputText;
        }

        public InputTextBean getInputText() {
            return inputText;
        }

        public void setInputText(InputTextBean inputText) {
            this.inputText = inputText;
        }

        public static class InputTextBean {
            /*"text":"你叫什么"*/
            private String text;

            public InputTextBean(String text) {
                this.text = text;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }

    public static class UserInfoBean {
        /*
        "apiKey":"79f03711ab3d4e9cb92380a43d2a7d0e"
        "userId":"285381"
        */
        private String apiKey;
        private String userId;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
