package org.jackalope.study.designPattern.prototype;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Client {

    private MobilePhone mobilePhone;

    public void copyMobilePhone(MobilePhone mobilePhone){
        try {
            this.mobilePhone = (MobilePhone) mobilePhone.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public MobilePhone getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(MobilePhone mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

}
