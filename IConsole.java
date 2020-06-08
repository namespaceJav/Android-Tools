package jav.android.console;

import android.content.*;
import android.os.*;
import jav.android.Msg;


interface IAndroidConsoleConstants
{
 ComponentName CONSOLE_APP_INTERFACE_NAME = new ComponentName("jav.android.console","jav.android.console.IAndroidConsole");

 int INIT     = IBinder.FIRST_CALL_TRANSACTION + 1;
 int DESTROY  = IBinder.FIRST_CALL_TRANSACTION + 2;
 int WRITE    = IBinder.FIRST_CALL_TRANSACTION + 3;
 int READ     = IBinder.FIRST_CALL_TRANSACTION + 4;
 int ON_RECIEVE_CLIENT_ID = IBinder.FIRST_CALL_TRANSACTION + 5;
 int ON_RECIEVE_INPUT     = IBinder.FIRST_CALL_TRANSACTION + 6;
}

public class IConsole implements ServiceConnection,
                                 IAndroidConsoleConstants
{
    Context ctx;
    int clientId;
    IBinder serviceBinder, clientBinder;
    String input;
    final Object threadLock = new Object();


    public IConsole(Context ctx) throws RuntimeException
    {
         this.ctx = ctx;
         clientId = android.os.Process.myPid();

         Intent intent = new Intent();
         intent.setComponent(CONSOLE_APP_INTERFACE_NAME);

         try{ ctx.bindService(intent,this,Context.BIND_AUTO_CREATE); }
         catch(Exception e){ throw new RuntimeException("failed to connect to service : " + e + e.getMessage()); }
    }

    public void disconnect()
    {
     ctx.unbindService(this);
    }

    @Override public void finalize()
    {
     disconnect();
    }

    @Override
    public void onServiceConnected(ComponentName name,IBinder serviceBinder)
    {
     this.serviceBinder = serviceBinder;
     if(!createConsole()) disconnect();
    }

    @Override
    public void onServiceDisconnected(ComponentName name){}

    public String readString()
    {
     return readConsole();
    }

    public Boolean readBool()
    {
     return new Boolean(input);
    }

    public Byte readByte()
    {
     return new Byte(input);
    }

    public Character readChar()
    {
     return new Character(input.charAt(0));
    }

    public Short readShort()
    {
     return new Short(input);
    }

    public Integer readInt()
    {
     return new Integer(input);
    }

    public Long readLong()
    {
     return new Long(input);
    }

    public Float readFloat()
    {
     return new Float(input);
    }

    public Double readDouble()
    {
     return new Double(input);
    }

    public void writeString(String data)
    {
     if(data == null)  writeConsole("null");
     else writeConsole(data);
    }

    public void writeBool(boolean data)
    {
     writeConsole(String.valueOf(data));
    }

    public void writeByte(byte data)
    {
     writeConsole(String.valueOf(data));
    }

    public void writeChar(char data)
    {
     writeConsole(String.valueOf(data));
    }

    public void writeShort(short data)
    {
     writeConsole(String.valueOf(data));
    }

    public void writeInt(int data)
    {
     writeConsole(String.valueOf(data));
    }

    public void writeLong(long data)
    {
     writeConsole(String.valueOf(data));
    }

    public void writeFloat(float data)
    {
     writeConsole(String.valueOf(data));
    }

    public void writeDouble(double data)
    {
     writeConsole(String.valueOf(data));
    }
/*
    private void readInstruction(jav.rFile file)
    {
        try
        {
            switch(file.readInt())
            {
             case DESTROY: destroyClient(); break;
             case ON_RECIEVE_INPUT: onRecieveInput(file.readString());
             case ON_RECIEVE_CLIENT_ID: onRecieveClientId(file.readInt());
            }
        }
        catch(Exception e){ Msg.shortToast("IConsole.readInstruction - " + e.getMessage()); }
    }
*/
    private boolean createConsole()
    {
     Parcel data = Parcel.obtain();
     Parcel reply = Parcel.obtain();

     data.writeInt(clientId);
     try { serviceBinder.transact(INIT,data,reply,0); }
     catch(Exception e){ return false; }

     data.recycle();
     reply.recycle();
     return true;
    }

    private String readConsole() throws RuntimeException
    {/*
        synchronized(threadLock)
        {
             try
             {
              buffer.putInt(READ);
              buffer.putInt(clientId);

                // serviceWriter.writeInt(DESTROY);
                 //serviceWriter.writeInt(clientId);

              serviceWriter.write(buffer.array(),0,buffer.position());
              buffer.clear();

              threadLock.wait();
             }
             catch(InterruptedException e){}
             catch(java.io.IOException e){ Msg.box(ctx,"AnroidConsoleInterface.readConsole failed: " + e + " " + e.getMessage()); }
        }

        return input;*/
        return null;
    }

    private boolean writeConsole(String s_data)
    {
         Parcel data = Parcel.obtain();
         Parcel reply = Parcel.obtain();

         try
         {
          data.writeInt(clientId);
          data.writeString(s_data);
          serviceBinder.transact(WRITE,data,reply,0);
         }
         catch(Exception e){ Msg.box(ctx,"failed to write to service : " + e + e.getMessage());  }

         data.recycle();
         reply.recycle();
         return true;
    }
/*
    private void onRecieveInput(String input)
    {
     this.input = input;
     threadLock.notify();
    }*/
}
