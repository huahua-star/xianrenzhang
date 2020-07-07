package org.jeecg.modules.zzj.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="AppLoginResponse")
public class AppLoginResponse {
    private Boolean appLoginResult;
}
