package horsa.nibelungen.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import horsa.nibelungen.Global;


/**
 * Created by UserZ150 on 25/4/2560.
 */
public class LocalStorage
{
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String PortNumberKey = "port";

    private static int portNumber;

    public static int getPortNumber()
    {
        if (sharedPreferences != null)
        {
            if (portNumber == 0)
            {
                String value = sharedPreferences.getString(PortNumberKey, "");
                if (value != null && value.length() > 0)
                    portNumber = Integer.parseInt(value);
            }
        }

        return portNumber;
    }

    public static void setPortNumber(int value)
    {
        portNumber = value;
        if (editor != null)
        {
            editor.putString(PortNumberKey, Integer.toString(value));
            editor.commit();
        }
    }

    /*
    Clears the database.
     */
    public static void clear()
    {
        setPortNumber(0);
    }

    public static void initialize(Context context)
    {
        sharedPreferences = context.getSharedPreferences("horsaStorage", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}
