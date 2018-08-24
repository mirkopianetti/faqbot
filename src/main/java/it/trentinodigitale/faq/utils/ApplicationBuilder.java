package it.trentinodigitale.faq.utils;

import org.jvnet.hk2.annotations.Service;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Created by Luca Mosetti in 2017
 * <p>
 * Useful class which:
 * - save the user in a local variable
 * - define the method getMessage(...)
 */
@Service
public class ApplicationBuilder {

    /**
     * Returns the value of 'msg' contained in the user's Locale ResourceBundle
     *
     * @param msg    name
     * @param params params
     * @return value of 'msg' contained in the user's Locale ResourceBundle
     */
    public String getMessage(String msg, String... params) {


        ResourceBundle bundle = ResourceBundle.getBundle("application");
        MessageFormat formatter = new MessageFormat("");
        /*formatter.setLocale("");*/
        formatter.applyPattern(bundle.getString(msg));
        return formatter.format(params);
    }
}
