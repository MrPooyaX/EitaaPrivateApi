package com.example.eitaaprivateapi;

import static com.example.eitaaprivateapi.Utils.decompress;
import static com.example.eitaaprivateapi.Utils.getNextRandomId;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eitaaprivateapi.tgnet.AbstractSerializedData;
import com.example.eitaaprivateapi.tgnet.EitaaTLRPC;
import com.example.eitaaprivateapi.tgnet.SerializedData;
import com.example.eitaaprivateapi.tgnet.TLObject;
import com.example.eitaaprivateapi.tgnet.TLRPC;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    EditText edtToken;
    Button btnGetMyProfile;
    ImageView imgProfile;
    EditText edtTargetUsername;
    EditText edtSearchUsername;
    EditText edtTargetUserId;
    EditText edtTargetAccessHash;
    EditText edtTargetMessage;
    EditText edtPhone;
    EditText edtCode;
    Button btnGetTargetUserInfo;
    Button btnSearch;

    Button btnJoinChannel;
    Button btnJoinGP;
    Button btnGetFullChannel;

    Button btnGetLastMessage;
    Button btnSeenMessages;
    Button btnSendMessage;
    Button btnMyChats;
    Button btnSendCode;
    Button btnSendCodeNextStep;
    Button btnCheckCode;
    int pts = 99999;
    boolean is_user = false;
    public static SharedPref sharedPref;
    String phone_code_hash = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = new SharedPref(this);
        String token = sharedPref.getToken();


        boolean is_channel = true;
        edtPhone = findViewById(R.id.edtPhone);
        edtCode = findViewById(R.id.edtCode);
        edtToken = findViewById(R.id.edtToken);
        imgProfile = findViewById(R.id.imgProfile);
        edtTargetUsername = findViewById(R.id.edtTargetUsername);
        edtTargetMessage = findViewById(R.id.edtTargetMessage);
        edtSearchUsername = findViewById(R.id.edtSearchUsername);
        edtTargetUserId = findViewById(R.id.edtTargetUserId);
        edtTargetAccessHash = findViewById(R.id.edtTargetAccessHash);
        btnSendCode = findViewById(R.id.btnSendCode);
        btnSendCodeNextStep = findViewById(R.id.btnSendCodeNextStep);
        btnCheckCode = findViewById(R.id.btnCheckCode);
        btnGetTargetUserInfo = findViewById(R.id.btnGetTargetUserInfo);
        btnMyChats = findViewById(R.id.btnMyChats);
        btnGetMyProfile = findViewById(R.id.btnGetMyProfile);
        btnGetFullChannel = findViewById(R.id.btnGetFullChannel);
        btnGetLastMessage = findViewById(R.id.btnGetLastMessage);
        btnSeenMessages = findViewById(R.id.btnSeenMessages);
        btnSearch = findViewById(R.id.btnSearch);
        btnJoinChannel = findViewById(R.id.btnJoinChannel);
        btnJoinGP = findViewById(R.id.btnJoinGP);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        edtToken.setText(token);

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(edtPhone.getText().toString());
            }
        });
        btnSendCodeNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReSendCode(edtPhone.getText().toString(),phone_code_hash); //nextype - mamulan call mikone
            }
        });
        btnCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edtPhone.getText().toString(),phone_code_hash, edtCode.getText().toString());
            }
        });
        btnGetMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFullUser(true,0,0,edtToken.getText().toString());
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchId(edtSearchUsername.getText().toString(),edtToken.getText().toString());
            }
        });
        btnGetTargetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveUsername(edtTargetUsername.getText().toString(),edtToken.getText().toString());
            }
        });
        btnJoinChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinChannel(Long.parseLong(edtTargetUserId.getText().toString()),Long.parseLong(edtTargetAccessHash.getText().toString()),edtToken.getText().toString());
            }
        });
        btnJoinGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // joinGp("",edtToken.getText().toString());
                checkGp("",edtToken.getText().toString());
            }
        });
        btnGetFullChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFullChannel(Long.parseLong(edtTargetUserId.getText().toString()),Long.parseLong(edtTargetAccessHash.getText().toString()),edtToken.getText().toString());
            }
        });
        btnGetLastMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetHistory((!is_user ? "channel" : "user"),Long.parseLong(edtTargetUserId.getText().toString()),Long.parseLong(edtTargetAccessHash.getText().toString()),edtToken.getText().toString());
            }
        });
        btnSeenMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeenMessages((!is_user ? "channel" : "user"),pts,Long.parseLong(edtTargetUserId.getText().toString()),Long.parseLong(edtTargetAccessHash.getText().toString()),edtToken.getText().toString());
            }
        });
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessages((!is_user ? "channel" : "user"),edtTargetMessage.getText().toString(),Long.parseLong(edtTargetUserId.getText().toString()),Long.parseLong(edtTargetAccessHash.getText().toString()),edtToken.getText().toString());
            }
        });
        btnMyChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myChats(edtToken.getText().toString());
            }
        });
        // getConfig();
        //GetChannelMessages(channel_id,channel_access_hash,token);
    }
    public void toast(String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getConfig()
    {
        EitaaTLRPC.TL_help_getConfig tl_help_getConfig = new EitaaTLRPC.TL_help_getConfig();
        tl_help_getConfig.user_id = 0;
        tl_help_getConfig.appInfo = Utils.getAppInfo();
        byte[] mainData = mainDatas(tl_help_getConfig,"");

        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                SerializedData serializedData1 = new SerializedData(response);
                Log.i("myapp",serializedData1.readInt32(true) + "");

                EitaaTLRPC.TL_config deserialize = new EitaaTLRPC.TL_config();
                deserialize.readParams(serializedData1,false);


                for (TLRPC.TL_dcOption dcOption : deserialize.dc_options) {
                    Log.i("myapp", dcOption.ip_address + "");
                    Log.i("myapp", dcOption.port + "");
                }
            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public void sendCode(String phone_number)
    {
        final TLRPC.TL_auth_sendCode tl_auth_sendCode = new TLRPC.TL_auth_sendCode();
        tl_auth_sendCode.api_hash = Application.APP_HASH;
        tl_auth_sendCode.api_id = Application.APP_ID;
        tl_auth_sendCode.phone_number = phone_number;
        TLRPC.TL_codeSettings tl_codeSettings = new TLRPC.TL_codeSettings();
        tl_auth_sendCode.settings = tl_codeSettings;
        tl_codeSettings.allow_flashcall = false;
        tl_codeSettings.allow_app_hash = false;

        byte[] mainData = mainDatas(tl_auth_sendCode,"");

        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    toast(deserialize.code + "\n" + deserialize.text);
                    return;
                }
                EitaaTLRPC.TL_auth_sentCode deserialize = new EitaaTLRPC.TL_auth_sentCode();
                deserialize.readParams(serializedData1,false);
                toast("type: " + deserialize.type.type + "\n" +
                        "next_type: " +  deserialize.next_type.type  + "\n" +
                        "phone_code_hash: " +  deserialize.phone_code_hash);
                phone_code_hash = deserialize.phone_code_hash;
            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }

    public void ReSendCode(String phone_number,String phone_code_hash)
    {
        final TLRPC.TL_auth_resendCode tl_auth_resendCode = new TLRPC.TL_auth_resendCode();
        tl_auth_resendCode.phone_code_hash = phone_code_hash;
        tl_auth_resendCode.phone_number = phone_number;
        byte[] mainData = mainDatas(tl_auth_resendCode,"");
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                Log.i("myapp", constract + "");
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    toast(deserialize.code + "\n" + deserialize.text);
                    return;
                    //error
                }
                EitaaTLRPC.TL_auth_sentCode deserialize = new EitaaTLRPC.TL_auth_sentCode();
                deserialize.readParams(serializedData1,false);

                toast("type: " + deserialize.type.type + "\n" +
                        "next_type: " +  deserialize.next_type.type  + "\n" +
                        "phone_code_hash: " +  deserialize.phone_code_hash);
            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public void signIn(String phone_number,String phone_code_hash,String phone_code)
    {
        final TLRPC.TL_auth_signIn tl_auth_signIn = new TLRPC.TL_auth_signIn();
        tl_auth_signIn.phone_code_hash = phone_code_hash;
        tl_auth_signIn.phone_number = phone_number;
        tl_auth_signIn.phone_code = phone_code;
        byte[] mainData = mainDatas(tl_auth_signIn,"");
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("myapp", Base64.encodeToString(response,2));
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                Log.i("myapp", constract + "");
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    toast(deserialize.code + "\n" + deserialize.text);
                    return;
                }
                if (constract == -855308010)
                {
                    //user exist
                    EitaaTLRPC.TL_auth_authorization deserialize = new EitaaTLRPC.TL_auth_authorization();
                    deserialize.readParams(serializedData1,false);

                    sharedPref.setToken(deserialize.token);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edtToken.setText(deserialize.token);
                        }
                    });
                    toast("token: " + deserialize.token  + "\n" +
                            "first_name: " +  deserialize.user.first_name );
                }
                else {
                    toast("need signup");
                }

            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }

    public void getFullUser(boolean user_self,long id,long access_hash,String token)
    {
        TLRPC.InputUser tl_inputUser;
        if (user_self)
        {
            tl_inputUser = new TLRPC.TL_inputUserSelf();
        }else {
            tl_inputUser = new TLRPC.TL_inputUser();
            tl_inputUser.user_id = id;
            tl_inputUser.access_hash = access_hash;
        }
        int arr[] = {1,4};
        int i = arr[0];

        final TLRPC.TL_users_getFullUser tl_users_getFullUser = new TLRPC.TL_users_getFullUser();
        tl_users_getFullUser.id = tl_inputUser;
        byte[] mainData = mainDatas(tl_users_getFullUser,token);
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("myapp", Base64.encodeToString(response,2));
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    //Log.i("myapp", deserialize.code + "");
                   // Log.i("myapp", deserialize.text + "");
                    toast(deserialize.code + "\n" + deserialize.text);
                    return;
                    //error
                }
                Log.i("myapp", constract + "");
                TLRPC.UserFull userFull = TLRPC.TL_userFull.TLdeserialize(serializedData1,constract,true);
                // Log.i("myapp", userFull.user.id + "");
                // Log.i("myapp", userFull.user.username + "");
                //  Log.i("myapp", userFull.user.first_name + "");
                //  Log.i("myapp", userFull.user.phone + "");
                Log.i("myapp", userFull.folder_id + "");
                Log.i("myapp", userFull.profile_photo.access_hash + "");
                Log.i("myapp", userFull.profile_photo.flags + "");
                toast(userFull.user.id + "\n"
                        + userFull.user.username + "\n"+
                        userFull.user.first_name + "\n"+
                        userFull.user.phone + "\n");
                Log.i("myapp", userFull.profile_photo.sizes.get(0).location.local_id + "");
                Log.i("myapp", userFull.profile_photo.sizes.get(0).location.volume_id + "");
                long volume_id = userFull.profile_photo.sizes.get(0).location.volume_id;
                int local_id = userFull.profile_photo.sizes.get(0).location.local_id;
                getUserProfilePhoto(user_self,userFull.user.id,userFull.user.access_hash,volume_id,local_id,token);
            }
            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }

    private void getUserProfilePhoto(boolean user_self,long id,long access_hash,long volume_id,int local_id,String token) {
        TLRPC.TL_photos_getUserPhotos tl_photos_getUserPhotos = new TLRPC.TL_photos_getUserPhotos();
        TLRPC.InputUser tl_inputUser;

        if (user_self)
        {
            tl_inputUser = new TLRPC.TL_inputUserSelf();
        }else {
            tl_inputUser = new TLRPC.TL_inputUser();
            tl_inputUser.user_id = id;
            tl_inputUser.access_hash = access_hash;
        }
        tl_photos_getUserPhotos.user_id = tl_inputUser;
        byte[] mainData = mainDatas(tl_photos_getUserPhotos,token);
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("myapp", Base64.encodeToString(response,2));
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    toast(deserialize.code + "\n" + deserialize.text);
                    return;
                    //error
                }
                Log.i("myapp", constract + "");
                TLObject result = tl_photos_getUserPhotos.deserializeResponse(serializedData1,constract,true);
                if (result instanceof TLRPC.TL_photos_photos)
                {
                    TLRPC.TL_photos_photos photos_photos = (TLRPC.TL_photos_photos)result;
                    Log.i("myapp", photos_photos.count + "");
                    Log.i("myapp", photos_photos.photos.size() + "");
                    Log.i("myapp", photos_photos.photos.get(0).id+ "");
                    Log.i("myapp", photos_photos.photos.get(0).access_hash+ "");
                    TLRPC.TL_inputPeerUser tl_inputPeerUser = new TLRPC.TL_inputPeerUser();
                    tl_inputPeerUser.user_id = id;
                    tl_inputPeerUser.access_hash = access_hash;
                    TLRPC.TL_upload_getFile getFile = new TLRPC.TL_upload_getFile();


                    TLRPC.TL_inputPeerPhotoFileLocation inputPeerPhotoFileLocation = new TLRPC.TL_inputPeerPhotoFileLocation();
                    inputPeerPhotoFileLocation.big = true;
                    inputPeerPhotoFileLocation.photo_id = photos_photos.photos.get(0).id;
                    inputPeerPhotoFileLocation.volume_id = volume_id;
                    inputPeerPhotoFileLocation.local_id = local_id;
                    inputPeerPhotoFileLocation.peer = tl_inputPeerUser;
                    getFile.cdn_supported = true;
                    getFile.location = inputPeerPhotoFileLocation;
                    getFile.limit = 100;
                    getFile.offset = 0;
                    byte[] mainData = mainDatas(getFile,token);
                    HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
                    connection.send(mainData, new HelperHttp.Result() {
                        @Override
                        public void onResponse(byte[] response) {
                            Log.i("myapp", Base64.encodeToString(response,2));
                            SerializedData serializedData1 = new SerializedData(response);
                            int constract = serializedData1.readInt32(true);
                            if(constract == -994444869)
                            {
                                TLRPC.TL_error deserialize = new TLRPC.TL_error();
                                deserialize.readParams(serializedData1,false);
                                toast(deserialize.code + "\n" + deserialize.text);
                                return;
                                //error
                            }
                            Log.i("myapp", constract + "");
                        }

                        @Override
                        public void onFailure(String str) {

                        }
                    });
                }else if (result instanceof TLRPC.TL_photos_photosSlice){
                    Log.i("myapp", "TL_photos_photosSlice");
                    Log.i("myapp", ((TLRPC.TL_photos_photosSlice) result).photos.get(0).dc_id + "");
                    TLRPC.TL_photos_photosSlice photos_photosSlice = (TLRPC.TL_photos_photosSlice)result;
                    Log.i("myapp", photos_photosSlice.count + "");
                    Log.i("myapp", photos_photosSlice.photos.size() + "");
                    Log.i("myapp", photos_photosSlice.photos.get(0).id+ "");

                    Log.i("myapp", photos_photosSlice.photos.get(0).access_hash+ "");
                    TLRPC.TL_inputPeerUser tl_inputPeerUser = new TLRPC.TL_inputPeerUser();
                    tl_inputPeerUser.user_id = id;
                    tl_inputPeerUser.access_hash = access_hash;

                    TLRPC.TL_upload_getFile getFile = new TLRPC.TL_upload_getFile();


                    TLRPC.TL_inputPeerPhotoFileLocation inputPeerPhotoFileLocation = new TLRPC.TL_inputPeerPhotoFileLocation();
                    inputPeerPhotoFileLocation.big = true;
                    inputPeerPhotoFileLocation.photo_id = photos_photosSlice.photos.get(0).id;
                    inputPeerPhotoFileLocation.peer = tl_inputPeerUser;
                    inputPeerPhotoFileLocation.volume_id = volume_id;
                    inputPeerPhotoFileLocation.local_id = local_id;
                    getFile.cdn_supported = true;
                    getFile.location = inputPeerPhotoFileLocation;
                    getFile.limit = 10000;
                    getFile.offset = 0;
                    byte[] mainData = mainDatas(getFile,token);
                    HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
                    connection.send(mainData, new HelperHttp.Result() {
                        @Override
                        public void onResponse(byte[] response) {
                            Log.i("myapp", Base64.encodeToString(response,2));
                            SerializedData serializedData1 = new SerializedData(response);
                            int constract = serializedData1.readInt32(true);
                            if(constract == -994444869)
                            {
                                TLRPC.TL_error deserialize = new TLRPC.TL_error();
                                deserialize.readParams(serializedData1,false);
                                toast(deserialize.code + "\n" + deserialize.text);
                                return;
                            }
                            Log.i("myapp", constract + "");
                            TLObject result = getFile.deserializeResponse(serializedData1,constract,true);
                            if (result instanceof TLRPC.TL_upload_file)
                            {
                                Log.i("myapp", "TL_upload_file" + "");
                                TLRPC.TL_upload_file upload_file = (TLRPC.TL_upload_file)result;
                                Log.i("myapp", upload_file.type + "");
                                Log.i("myapp", upload_file.type.constructor_ + "");

                                Log.i("myapp", Base64.encodeToString(upload_file.bytes,2) + "");

                            }else
                            {
                                Log.i("myapp", "TL_upload_fileCdnRedirect" + "");
                            }
                        }

                        @Override
                        public void onFailure(String str) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    private void getChannelAndGPProfilePhoto(long id,long access_hash,long photo_id,long volume_id,int local_id,String token) {
        TLRPC.InputPeer inputPeer = new TLRPC.TL_inputPeerChat();
        inputPeer.user_id = id;
        inputPeer.access_hash = access_hash;
        TLRPC.TL_upload_getFile getFile = new TLRPC.TL_upload_getFile();

        TLRPC.InputFileLocation inputFileLocation;
        if (id == 0 || access_hash == 0)
        {
            TLRPC.TL_inputFileLocation tl_inputFileLocation = new TLRPC.TL_inputFileLocation();
            tl_inputFileLocation.volume_id = volume_id;
            tl_inputFileLocation.local_id = local_id;
            inputFileLocation = tl_inputFileLocation;
            inputFileLocation.secret = 0;
            inputFileLocation.access_hash = 0;
            inputFileLocation.file_reference = new byte[0];

        }else {
            TLRPC.TL_inputPeerPhotoFileLocation inputPeerPhotoFileLocation = new TLRPC.TL_inputPeerPhotoFileLocation();
            inputPeerPhotoFileLocation.big = true;
            inputPeerPhotoFileLocation.photo_id = photo_id;
            inputPeerPhotoFileLocation.peer = inputPeer;
            inputPeerPhotoFileLocation.volume_id = volume_id;
            inputPeerPhotoFileLocation.local_id = local_id;
            inputFileLocation = inputPeerPhotoFileLocation;
        }

        getFile.cdn_supported = true;
        getFile.location = inputFileLocation;
        getFile.limit = 10000;
        getFile.offset = 0;
        byte[] mainData = mainDatas(getFile,token);
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("myapp", Base64.encodeToString(response,2));
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    toast(deserialize.code + "\n" + deserialize.text);
                    return;
                }
                Log.i("myapp", constract + "");
                TLObject result = getFile.deserializeResponse(serializedData1,constract,true);
                if (result instanceof TLRPC.TL_upload_file)
                {
                    Log.i("myapp", "TL_upload_file" + "");
                    TLRPC.TL_upload_file upload_file = (TLRPC.TL_upload_file)result;
                    Log.i("myapp", upload_file.type + "");
                    Log.i("myapp", upload_file.type.constructor_ + "");

                    Log.i("myapp", Base64.encodeToString(upload_file.bytes,2) + "");

                }else
                {
                    Log.i("myapp", "TL_upload_fileCdnRedirect" + "");
                }
            }

            @Override
            public void onFailure(String str) {

            }
        });


    }
    public void resolveUsername(String username,String token)
    {

        final TLRPC.TL_contacts_resolveUsername tl_contacts_resolveUsername = new TLRPC.TL_contacts_resolveUsername();
        tl_contacts_resolveUsername.username = username;
        byte[] mainData = mainDatas(tl_contacts_resolveUsername,token);
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("myapp", Base64.encodeToString(response,2));
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    toast(deserialize.code + "\n" + deserialize.text);
                    return;
                    //error
                }
                Log.i("myapp", constract + "");
                TLRPC.TL_contacts_resolvedPeer result = (TLRPC.TL_contacts_resolvedPeer)tl_contacts_resolveUsername.deserializeResponse(serializedData1,constract,true);

                if (result.chats != null && result.chats.size() > 0)
                {
                    for (TLRPC.Chat chat : result.chats) {
                        toast("Title:" + chat.title + "\n" +
                                "username:" +chat.username + "\n" +
                                "id:" +chat.id + "\n" +
                                "access_hash:" +chat.access_hash + "\n" +
                                "users_count:" +chat.participants_count + "\n"
                        );
                        if (chat.photo != null && chat.photo.photo_small != null)
                            getChannelAndGPProfilePhoto(chat.id,chat.access_hash,chat.photo.photo_id,chat.photo.photo_small.volume_id,chat.photo.photo_small.local_id,token);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                edtTargetUserId.setText(String.valueOf(chat.id));
                                edtTargetAccessHash.setText(String.valueOf(chat.access_hash));
                                is_user = false;
                                pts = 999999;
                            }
                        });
                    }
                }
                if (result.users != null && result.users.size() > 0)
                {
                    for (TLRPC.User user : result.users) {
                        Log.i("myapp", user.photo.photo_big.local_id + "");
                        Log.i("myapp",  user.photo.photo_big.volume_id + "");
                        long volume_id = user.photo.photo_big.volume_id;
                        int local_id = user.photo.photo_big.local_id;

                        getUserProfilePhoto(false,user.id,user.access_hash,volume_id,local_id,token);
                        toast(user.username + "\n" +
                                "id:" +user.id + "\n" +
                                "first_name:" +user.first_name  + "\n" +
                                "access_hash:" +user.access_hash + "\n"
                        );
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                edtTargetUserId.setText(String.valueOf(user.id));
                                edtTargetAccessHash.setText(String.valueOf(user.access_hash));
                                is_user = true;
                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public void joinChannel(long channel_id,long access_hash,String token)
    {
        if (is_user)
        {
            toast("isuser!!!");
            return;
        }
        TLRPC.TL_inputChannel inputChannel = new TLRPC.TL_inputChannel();
        inputChannel.channel_id = channel_id;
        inputChannel.access_hash = access_hash;
        final TLRPC.TL_channels_joinChannel tl_channels_joinChannel = new TLRPC.TL_channels_joinChannel();
        tl_channels_joinChannel.channel = inputChannel;
        byte[] mainData = mainDatas(tl_channels_joinChannel,token);
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("myapp", Base64.encodeToString(response,2));
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    Log.i("myapp", deserialize.code + "");
                    Log.i("myapp", deserialize.text + "");
                    return;
                    //error
                }
                Log.i("myapp", constract + "");
                TLRPC.Updates updates = (TLRPC.Updates)tl_channels_joinChannel.deserializeResponse(serializedData1,constract,true);
                Log.i("myapp",  updates.message+ "");
                Log.i("myapp",  updates.chat_id+ "");
                Log.i("myapp",  updates.chats.get(0).username+ "");
            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public void searchId(String query,String token)
    {
        final TLRPC.TL_contacts_search tl_contacts_search = new TLRPC.TL_contacts_search();
        tl_contacts_search.q = query;
        tl_contacts_search.limit = 5;
        byte[] mainData = mainDatas(tl_contacts_search,token);
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    toast(deserialize.code + "\n" + deserialize.text);
                    return;
                    //error
                }
                Log.i("myapp", constract + "");
                TLRPC.TL_contacts_found result = new TLRPC.TL_contacts_found();
                result.readParams(serializedData1, true);
                if (result.chats != null && result.chats.size() > 0)
                {
                    for (TLRPC.Chat chat : result.chats) {

                        toast("Title:" + chat.title + "\n" +
                                "username:" +chat.username + "\n" +
                                "id:" +chat.id + "\n" +
                                "access_hash:" +chat.access_hash + "\n" +
                                "users_count:" +chat.participants_count + "\n"
                        );
                    }
                }
                if (result.my_results != null && result.my_results.size() > 0)
                {
                    for (TLRPC.Peer peer : result.my_results) {
                        //Log.i("myapp", peer.chat_id + "");
                       // Log.i("myapp", peer.channel_id + "");
                    }
                }
                if (result.users != null && result.users.size() > 0)
                {
                    for (TLRPC.User user : result.users) {
                        toast(user.username + "\n" +
                                "id:" +user.id + "\n" +
                                "first_name:" +user.first_name  + "\n" +
                                "access_hash:" +user.access_hash + "\n"
                        );
                    }
                }
                if (result.results != null && result.results.size() > 0)
                {
                    for (TLRPC.Peer peer : result.results) {
                    //    Log.i("myapp", peer.chat_id + "");
                    //    Log.i("myapp", peer.channel_id + "");
                    }
                }
            //    Log.i("myapp", result.results.get(0).channel_id + "");
             //   Log.i("myapp", result.users.get(0).username + "");
            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public void getFullChannel(long channel_id,long access_hash,String token)
    {
        if (is_user)
        {
            toast("isuser!!!");
            return;
        }
        TLRPC.InputChannel inputChannel = new TLRPC.TL_inputChannel();
        inputChannel.channel_id = channel_id;
        inputChannel.access_hash = access_hash;
        final TLRPC.TL_channels_getFullChannel tl_channels_getFullChannel = new TLRPC.TL_channels_getFullChannel();
        tl_channels_getFullChannel.channel = inputChannel;
        byte[] mainData = mainDatas(tl_channels_getFullChannel,token);
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("myapp", Base64.encodeToString(response,2));
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    toast(deserialize.code + "\n" + deserialize.text);
                    return;
                    //error
                }
                TLRPC.TL_messages_chatFull result = new TLRPC.TL_messages_chatFull();
                result.readParams(serializedData1, true);
                Log.i("myapp",  result.full_chat.about+ "");
                Log.i("myapp",  result.full_chat.unread_count+ "");
                Log.i("myapp",  result.full_chat.pts+ ""); //id akharin payam
                pts = result.full_chat.pts;
                if (result.chats != null && result.chats.size() > 0)
                {
                    for (TLRPC.Chat chat : result.chats) {
                        toast("Title:" + chat.title + "\n" +
                                "username:" +chat.username + "\n" +
                                "id:" +chat.id + "\n" +
                                "access_hash:" +chat.access_hash + "\n" +
                                "users_count:" +chat.participants_count + "\n"
                        );
                        if (chat.photo != null && chat.photo.photo_small != null)
                            getChannelAndGPProfilePhoto(chat.id,chat.access_hash,chat.photo.photo_id,chat.photo.photo_small.volume_id,chat.photo.photo_small.local_id,token);
                    }
                }

                if (result.users != null && result.users.size() > 0)
                {
                    for (TLRPC.User user : result.users) {
                        toast(
                                "username:" +user.username + "\n" +
                                "id:" +user.id + "\n" +
                                "access_hash:" +user.access_hash + "\n" +
                                "first_name:" +user.first_name + "\n"
                        );
                    }
                }

            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public void GetChannelMessages(long channel_id,long access_hash,String token)
    {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(371);

        TLRPC.InputChannel inputChannel = new TLRPC.TL_inputChannel();
        inputChannel.channel_id = channel_id;
        inputChannel.access_hash = access_hash;

        TLRPC.TL_channels_getMessages tL_channels_getMessages = new TLRPC.TL_channels_getMessages();
        tL_channels_getMessages.channel = inputChannel;
        tL_channels_getMessages.id = arrayList;


        byte[] mainData = mainDatas(tL_channels_getMessages,token);
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("myapp", Base64.encodeToString(response,2));
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    Log.i("myapp", deserialize.code + "");
                    Log.i("myapp", deserialize.text + "");
                    return;
                    //error
                }
                Log.i("myapp", constract + "");
                TLRPC.messages_Messages result = (TLRPC.messages_Messages)tL_channels_getMessages.deserializeResponse(serializedData1,constract,true);
                Log.i("myapp",  result.count + "");
                Log.i("myapp",  result.pts + "");
                Log.i("myapp",  result.next_rate + "");
                Log.i("myapp",  result.users.size() + "");

                if (result.chats != null && result.chats.size() > 0)
                {
                    for (TLRPC.Chat chat : result.chats) {
                        Log.i("myapp", chat.title + "");
                        Log.i("myapp", chat.username + "");
                        Log.i("myapp", chat.id + "");
                        Log.i("myapp", chat.access_hash + "");
                        Log.i("myapp", chat.participants_count + "");
                    }
                }
                if (result.messages != null && result.messages.size() > 0)
                {
                    for (TLRPC.Message message : result.messages) {
                        Log.i("myapp", message.message + "");
                        Log.i("myapp", message.id + "");
                    }
                }
                if (result.users != null && result.users.size() > 0)
                {
                    for (TLRPC.User user : result.users) {
                        Log.i("myapp", user.username + "");
                        Log.i("myapp", user.id + "");
                        Log.i("myapp", user.first_name + "");
                        Log.i("myapp", user.access_hash + "");
                    }
                }

            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public void GetHistory(String type, long id,long access_hash,String token)
    {
        TLRPC.InputPeer inputPeer = null;
        if (type.equals("chat"))
        {
            TLRPC.TL_inputPeerChat tl_inputPeerChat = new TLRPC.TL_inputPeerChat();
            tl_inputPeerChat.chat_id = id;
            inputPeer = tl_inputPeerChat;
        }else if (type.equals("user")){
            TLRPC.TL_inputPeerUser tl_inputPeerUser = new TLRPC.TL_inputPeerUser();
            tl_inputPeerUser.user_id = id;
            inputPeer = tl_inputPeerUser;
        }else if (type.equals("channel")){
            TLRPC.TL_inputPeerChannel inputPeerChannel = new TLRPC.TL_inputPeerChannel();
            inputPeerChannel.channel_id = id;
            inputPeerChannel.access_hash = access_hash;
            inputPeer = inputPeerChannel;
        }

        TLRPC.TL_messages_getHistory tl_messages_getHistory = new TLRPC.TL_messages_getHistory();
        tl_messages_getHistory.limit = 5;
        tl_messages_getHistory.offset_id = pts;
        tl_messages_getHistory.offset_date = 0;
        tl_messages_getHistory.max_id = 0;
        tl_messages_getHistory.min_id = 0;
        tl_messages_getHistory.hash = 0;
        tl_messages_getHistory.add_offset = -1;
        tl_messages_getHistory.peer = inputPeer;


        byte[] mainData = mainDatas(tl_messages_getHistory,token);
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    toast(deserialize.code + "\n" + deserialize.text);
                    return;
                    //error
                }
                if(constract == 812830625)
                {
                    EitaaTLRPC.TL_gzip_packed deserialize = new EitaaTLRPC.TL_gzip_packed();
                    deserialize.readParams(serializedData1,false);
                    //gzip
                    response = decompress(deserialize.packed_data);
                    serializedData1 = new SerializedData(response);
                    constract = serializedData1.readInt32(true);
                }
                TLRPC.messages_Messages result = (TLRPC.messages_Messages)tl_messages_getHistory.deserializeResponse(serializedData1,constract,true);
                //Log.i("myapp",  result.count + "");
               // Log.i("myapp",  result.pts + "");
               // Log.i("myapp",  result.next_rate + "");
              //  Log.i("myapp",  result.users.size() + "");

                if (result.chats != null && result.chats.size() > 0)
                {
                    for (TLRPC.Chat chat : result.chats) {
                        toast("Title:" + chat.title + "\n" +
                                "username:" +chat.username + "\n" +
                                "id:" +chat.id + "\n" +
                                "access_hash:" +chat.access_hash + "\n" +
                                "users_count:" +chat.participants_count + "\n"
                        );
                    }
                }
                ArrayList<Integer> arrayList = new ArrayList<>();
                if (result.messages != null && result.messages.size() > 0)
                {
                    for (TLRPC.Message message : result.messages) {
                        Log.i("myapp", message.message + "");
                        Log.i("myapp", message.ttl + "");
                        Log.i("myapp", message.views + "");
                        Log.i("myapp", message.id + "");
                        toast(
                                "message:" +message.message + "\n" +
                                        "id:" +message.id + "\n"
                        );
                        arrayList.add(message.id);
                    }
                    getMessagesViews(type,id,access_hash,arrayList,token);
                }
                if (result.users != null && result.users.size() > 0)
                {
                    for (TLRPC.User user : result.users) {
                       /* toast(
                                "username:" +user.username + "\n" +
                                        "id:" +user.id + "\n" +
                                        "access_hash:" +user.access_hash + "\n" +
                                        "first_name:" +user.first_name + "\n"
                        );*/
                    }
                }

            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public void SeenMessages(String type, int max_id,long id,long access_hash,String token)
    {
        TLRPC.InputPeer inputPeer = null;
        if (type.equals("chat"))
        {
            TLRPC.TL_inputPeerChat tl_inputPeerChat = new TLRPC.TL_inputPeerChat();
            tl_inputPeerChat.chat_id = id;
            inputPeer = tl_inputPeerChat;
        }else if (type.equals("user")){
            TLRPC.TL_inputPeerUser tl_inputPeerUser = new TLRPC.TL_inputPeerUser();
            tl_inputPeerUser.user_id = id;
            inputPeer = tl_inputPeerUser;
        }else if (type.equals("channel")){
            inputPeer = new TLRPC.TL_inputPeerChannel();
        }
        byte[] mainData = null;
        TLRPC.TL_messages_readHistory tl_messages_readHistory = null;
        TLRPC.TL_channels_readHistory tl_channels_readHistory = null;
        if (inputPeer instanceof TLRPC.TL_inputPeerChannel) {
            tl_channels_readHistory = new TLRPC.TL_channels_readHistory();
            TLRPC.InputChannel inputChannel = new TLRPC.TL_inputChannel();
            inputChannel.channel_id = id;
            inputChannel.access_hash = access_hash;

            tl_channels_readHistory.channel = inputChannel;
            tl_channels_readHistory.max_id = max_id;
            mainData = mainDatas(tl_channels_readHistory,token);

        } else {
            tl_messages_readHistory = new TLRPC.TL_messages_readHistory();
            tl_messages_readHistory.peer = inputPeer;
            tl_messages_readHistory.max_id = max_id;
             mainData = mainDatas(tl_messages_readHistory,token);

        }
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        TLRPC.InputPeer finalInputPeer = inputPeer;
        TLRPC.TL_channels_readHistory finalTl_channels_readHistory = tl_channels_readHistory;
        TLRPC.TL_messages_readHistory finalTl_messages_readHistory = tl_messages_readHistory;
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("myapp", Base64.encodeToString(response,2));
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    toast(deserialize.code + "\n" + deserialize.text);

                    return;
                }
                if (finalInputPeer instanceof TLRPC.TL_inputPeerChannel) {
                    TLRPC.Bool result = (TLRPC.Bool) finalTl_channels_readHistory.deserializeResponse(serializedData1,constract,true);
                    if (result instanceof TLRPC.TL_boolTrue)
                    {
                        toast("seen ok");
                    }else
                    {
                        toast("seen not ok");
                    }
                } else {
                    TLRPC.TL_messages_affectedMessages result = (TLRPC.TL_messages_affectedMessages) finalTl_messages_readHistory.deserializeResponse(serializedData1,constract,true);
                    toast(result.pts + "\n" + result.pts_count);

                }
            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public void SendMessages(String type, String message,long id,long access_hash,String token)
    {
        TLRPC.InputPeer inputPeer = null;
        if (type.equals("chat"))
        {
            TLRPC.TL_inputPeerChat tl_inputPeerChat = new TLRPC.TL_inputPeerChat();
            tl_inputPeerChat.chat_id = id;
            inputPeer = tl_inputPeerChat;
        }else if (type.equals("user")){
            toast("send to user");
            TLRPC.TL_inputPeerUser tl_inputPeerUser = new TLRPC.TL_inputPeerUser();
            tl_inputPeerUser.user_id = id;
            tl_inputPeerUser.access_hash = access_hash;
            inputPeer = tl_inputPeerUser;
        }else if (type.equals("channel")){
            TLRPC.TL_inputPeerChannel tl_inputPeerChannel = new TLRPC.TL_inputPeerChannel();
            tl_inputPeerChannel.channel_id = id;
            tl_inputPeerChannel.access_hash = access_hash;
            inputPeer = tl_inputPeerChannel;
        }
        TLRPC.TL_messages_sendMessage tl_messages_sendMessage = new TLRPC.TL_messages_sendMessage();
        tl_messages_sendMessage.message = message;
        tl_messages_sendMessage.peer = inputPeer;
        tl_messages_sendMessage.clear_draft = false;
        tl_messages_sendMessage.silent = false;
        tl_messages_sendMessage.no_webpage = true;
        tl_messages_sendMessage.random_id = getNextRandomId();

        byte[] mainData = mainDatas(tl_messages_sendMessage,token);
        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        TLRPC.InputPeer finalInputPeer = inputPeer;
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("myapp", Base64.encodeToString(response,2));
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    toast(deserialize.code + "\n" + deserialize.text);
                    return;
                }
                if (finalInputPeer instanceof TLRPC.TL_inputPeerChannel) {
                    TLRPC.Updates result = (TLRPC.Updates) tl_messages_sendMessage.deserializeResponse(serializedData1,constract,true);

                } else {

                }

            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public void myChats(String token)
    {
        TLRPC.TL_messages_getDialogs tl_messages_getDialogs = new TLRPC.TL_messages_getDialogs();
        tl_messages_getDialogs.limit = 100;
        tl_messages_getDialogs.exclude_pinned = true;
        tl_messages_getDialogs.offset_id = 0;
        tl_messages_getDialogs.offset_date = 0;
        tl_messages_getDialogs.offset_peer = new TLRPC.TL_inputPeerEmpty();

        byte[] mainData = mainDatas(tl_messages_getDialogs,token);

        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                Log.i("myapp", Base64.encodeToString(response,2));
                SerializedData serializedData1 = new SerializedData(response);
                int constract = serializedData1.readInt32(true);
                if(constract == -994444869)
                {
                    TLRPC.TL_error deserialize = new TLRPC.TL_error();
                    deserialize.readParams(serializedData1,false);
                    toast(deserialize.code + "\n" + deserialize.text);
                    return;
                }
                Log.i("myapp", constract + "");
                TLRPC.messages_Dialogs result = (TLRPC.messages_Dialogs) tl_messages_getDialogs.deserializeResponse(serializedData1,constract,true);
                //if (result instanceof TLRPC.TL_messages_dialogs || result instanceof TLRPC.TL_messages_dialogsSlice)
               // {
                    if (result.chats != null && result.chats.size() > 0)
                    {
                        for (TLRPC.Chat chat : result.chats) {
                            toast("Title:" + chat.title + "\n" +
                                    "username:" +chat.username + "\n" +
                                    "id:" +chat.id + "\n" +
                                    "access_hash:" +chat.access_hash + "\n" +
                                    "users_count:" +chat.participants_count + "\n"
                            );
                            if (chat.photo != null && chat.photo.photo_small != null)
                                getChannelAndGPProfilePhoto(chat.id,chat.access_hash,chat.photo.photo_id,chat.photo.photo_small.volume_id,chat.photo.photo_small.local_id,token);
                        }
                    }

                    if (result.users != null && result.users.size() > 0)
                    {
                        for (TLRPC.User user : result.users) {
                            toast(
                                    "username:" +user.username + "\n" +
                                            "id:" +user.id + "\n" +
                                            "access_hash:" +user.access_hash + "\n" +
                                            "first_name:" +user.first_name + "\n"
                            );
                            if (user.photo != null && user.photo.photo_small != null)
                                getUserProfilePhoto(false,user.id,user.access_hash,user.photo.photo_big.volume_id,user.photo.photo_big.local_id,token);

                        }
                    }
                    if (result.messages != null && result.messages.size() > 0)
                    {
                        for (TLRPC.Message message : result.messages) {
                            Log.i("myapp", message.id + "");
                            Log.i("myapp", message.message + "");
                            toast(
                                    "id:" +message.id + "\n" +
                                            "message:" +message.message + "\n"
                            );
                        }
                    }
               // }else
              //  {
              //      Log.i("myapp",  "myChats not ok");
             //   }
            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }

    public void checkGp(String url,String token)
    {
        //http://eitaa.com/joinchat/2210070560Cae996bb68e
        String hash = "2210070560Cae996bb68e";
        EitaaTLRPC.TL_messages_EitaaCheckChatInvite tl_messages_eitaaCheckChatInvite = new EitaaTLRPC.TL_messages_EitaaCheckChatInvite();
        tl_messages_eitaaCheckChatInvite.hash = hash;
        byte[] mainData = mainDatas(tl_messages_eitaaCheckChatInvite,token);

        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Log.i("myapp", Base64.encodeToString(response,2));
                        SerializedData serializedData1 = new SerializedData(response);
                        int constract = serializedData1.readInt32(true);
                        if(constract == -994444869)
                        {
                            TLRPC.TL_error deserialize = new TLRPC.TL_error();
                            deserialize.readParams(serializedData1,false);
                            toast(deserialize.code + "\n" + deserialize.text);
                            return;
                        }
                        Log.i("myapp", constract + "");
                        TLObject result =  tl_messages_eitaaCheckChatInvite.deserializeResponse(serializedData1,constract,true);
                        if (result instanceof TLRPC.TL_chatInvite)
                        {
                            TLRPC.TL_chatInvite result2 = (TLRPC.TL_chatInvite)result;
                            Toast.makeText(MainActivity.this, result2.participants_count + "", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, result2.title, Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, result2.chat.id + "", Toast.LENGTH_SHORT).show();

                        }else if (result instanceof TLRPC.TL_chatInvitePeek)
                        {
                            TLRPC.TL_chatInvitePeek result2 = (TLRPC.TL_chatInvitePeek)result;
                            Toast.makeText(MainActivity.this, result2.participants_count + "", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, result2.title, Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, result2.chat.id + "", Toast.LENGTH_SHORT).show();
                        }else if (result instanceof TLRPC.TL_chatInviteAlready)
                        {
                            TLRPC.TL_chatInviteAlready result2 = (TLRPC.TL_chatInviteAlready)result;
                            Toast.makeText(MainActivity.this, result2.participants_count + "", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, result2.title, Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, result2.chat.id + "", Toast.LENGTH_SHORT).show();

                            if (result2.chat.photo != null && result2.chat.photo.photo_small != null)
                                getChannelAndGPProfilePhoto(result2.chat.id,result2.chat.access_hash,result2.chat.photo.photo_id,result2.chat.photo.photo_small.volume_id,result2.chat.photo.photo_small.local_id,token);

                        }else if (result instanceof TLRPC.TL_chatInvitePreview)
                        {
                            TLRPC.TL_chatInvitePreview result2 = (TLRPC.TL_chatInvitePreview)result;
                            Toast.makeText(MainActivity.this, result2.participants_count + "", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, result2.title, Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, result2.chat.id + "", Toast.LENGTH_SHORT).show();
                        }else if (result instanceof TLRPC.TL_chatInvite_layer84)
                        {
                            TLRPC.TL_chatInvite_layer84 result2 = (TLRPC.TL_chatInvite_layer84)result;
                            Log.i("pooya",result2.title);
                            Log.i("pooya",result2.participants_count + "");
                            Toast.makeText(MainActivity.this, result2.title, Toast.LENGTH_SHORT).show();

                            if (result2.chatPhoto != null && result2.chatPhoto.photo_small != null)
                                getChannelAndGPProfilePhoto(0,0,result2.chatPhoto.photo_id,result2.chatPhoto.photo_small.volume_id,result2.chatPhoto.photo_small.local_id,token);

                        }else
                        {
                            Log.i("myapp",  "checkGp not ok");
                        }
                    }
                });
            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public void joinGp(String url,String token)
    {
        //http://eitaa.com/joinchat/2210070560Cae996bb68e
        String hash = "2210070560Cae996bb68e";
        TLRPC.TL_messages_importChatInvite tl_messages_importChatInvitel = new TLRPC.TL_messages_importChatInvite();
        tl_messages_importChatInvitel.hash = hash;
        byte[] mainData = mainDatas(tl_messages_importChatInvitel,token);

        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("myapp", Base64.encodeToString(response,2));
                        SerializedData serializedData1 = new SerializedData(response);
                        int constract = serializedData1.readInt32(true);
                        if(constract == -994444869)
                        {
                            TLRPC.TL_error deserialize = new TLRPC.TL_error();
                            deserialize.readParams(serializedData1,false);
                            toast(deserialize.code + "\n" + deserialize.text);
                            return;
                        }
                        Log.i("myapp", constract + "");
                        TLRPC.Updates result =  (TLRPC.Updates)tl_messages_importChatInvitel.deserializeResponse(serializedData1,constract,true);
                        //Log.i("myapp", result. + "");
                    }
                });
            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public void getMessagesViews(String type,long id,long access_hash,ArrayList<Integer> ids,String token)
    {
        TLRPC.InputPeer inputPeer = null;
        if (type.equals("chat"))
        {
            TLRPC.TL_inputPeerChat tl_inputPeerChat = new TLRPC.TL_inputPeerChat();
            tl_inputPeerChat.chat_id = id;
            inputPeer = tl_inputPeerChat;
        }else if (type.equals("user")){
            TLRPC.TL_inputPeerUser tl_inputPeerUser = new TLRPC.TL_inputPeerUser();
            tl_inputPeerUser.user_id = id;
            inputPeer = tl_inputPeerUser;
        }else if (type.equals("channel")){
            TLRPC.TL_inputPeerChannel inputPeerChannel = new TLRPC.TL_inputPeerChannel();
            inputPeerChannel.channel_id = id;
            inputPeerChannel.access_hash = access_hash;
            inputPeer = inputPeerChannel;
        }

        TLRPC.TL_messages_getMessagesViews tl_messages_getMessagesViews = new TLRPC.TL_messages_getMessagesViews();
        tl_messages_getMessagesViews.peer = inputPeer;
        tl_messages_getMessagesViews.id = ids;
        byte[] mainData = mainDatas(tl_messages_getMessagesViews,token);

        HelperHttp connection = new HelperHttp("bagher.eitaa.ir", 443, "/eitaa/index.php");
        connection.send(mainData, new HelperHttp.Result() {
            @Override
            public void onResponse(byte[] response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("myapp", Base64.encodeToString(response,2));
                        SerializedData serializedData1 = new SerializedData(response);
                        int constract = serializedData1.readInt32(true);
                        if(constract == -994444869)
                        {
                            TLRPC.TL_error deserialize = new TLRPC.TL_error();
                            deserialize.readParams(serializedData1,false);
                            toast(deserialize.code + "\n" + deserialize.text);
                            return;
                        }
                        Log.i("myapp", constract + "");
                        TLRPC.TL_messages_messageViews result =  (TLRPC.TL_messages_messageViews)tl_messages_getMessagesViews.deserializeResponse(serializedData1,constract,true);
                        Log.i("myapp", result.views.get(0).views + "");
                    }
                });
            }

            @Override
            public void onFailure(String str) {
                Log.i("myapp", str);
            }
        });
    }
    public byte[] mainDatas(TLObject tlObject,String token)
    {
        SerializedData serializedData = new SerializedData(false);
        tlObject.serializeToStream(serializedData);

        EitaaTLRPC.TL_clientRequestAPK tL_clientRequestAPK = new EitaaTLRPC.TL_clientRequestAPK();
        tL_clientRequestAPK.isWifi = true;
        tL_clientRequestAPK.isData = false;
        tL_clientRequestAPK.appPause = false;
        tL_clientRequestAPK.buildVersion = Application.BUILD_VERSION;
        tL_clientRequestAPK.layer = 133;
        tL_clientRequestAPK.foregreoundConnection = false;
        tL_clientRequestAPK.imei = "7b205cce-f2bf-40f7-9954-69cc3db93bc3";
        tL_clientRequestAPK.token = token;
        tL_clientRequestAPK.lang = "en";
        tL_clientRequestAPK.packed_data = serializedData.toByteArray();
        SerializedData serializedData2 = new SerializedData();
        tL_clientRequestAPK.serializeToStream(serializedData2);
        byte[] byteArray = serializedData2.toByteArray();
        return  byteArray;
    }



}