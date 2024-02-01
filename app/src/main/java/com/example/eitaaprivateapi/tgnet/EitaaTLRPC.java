package com.example.eitaaprivateapi.tgnet;

import java.util.ArrayList;

public class EitaaTLRPC {

    public static class TL_messages_EitaaCheckChatInvite extends TLRPC.TL_messages_checkChatInvite {
        public static int constructor = 507247221;
        public int flags;
        public String packageName;

        @Override
        public TLObject deserializeResponse(AbstractSerializedData stream, int constructor2, boolean exception) {
            return TLRPC.ChatInvite.TLdeserialize(stream, constructor2, exception);
        }

        @Override
        public void serializeToStream(AbstractSerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.flags);
            if ((this.flags & 2) != 0) {
                stream.writeString(this.packageName);
            }
            stream.writeString(this.hash);
        }
    }
    public static class TL_AppInfo extends TLObject {
        public static int constructor = 1635347945;
        public String app_version;
        public int build_version;
        public String device_model;
        public String lang_code;
        public String sign = "";
        public String system_version;

        @Override // ir.eitaa.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.build_version);
            stream.writeString(this.device_model);
            stream.writeString(this.system_version);
            stream.writeString(this.app_version);
            stream.writeString(this.lang_code);
            stream.writeString(this.sign);
        }
    }
    public static class TL_help_getConfig extends TLObject {
        public static int constructor = -990308245;
        public TL_AppInfo appInfo;
        public int user_id;

        @Override // ir.eitaa.tgnet.TLObject
        public TLObject deserializeResponse(AbstractSerializedData stream, int constructor2, boolean exception) {
            return TL_config.TLdeserialize(stream, constructor2, exception);
        }

        @Override // ir.eitaa.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData stream) {
            stream.writeInt32(constructor);
            this.appInfo.serializeToStream(stream);
            stream.writeInt32(this.user_id);
        }
    }
    public static class TL_liveStreamConfig extends TLObject {
        public static int constructor = 26504181;
        public int flags;
        public int maxVideoWidth = 720;
        public boolean send_in_groups;
        public boolean send_in_privateChannel;
        public boolean send_in_privateChat;
        public boolean send_in_publicChannel;
        public boolean send_in_supergroups;

        public TL_liveStreamConfig() {
        }

        public TL_liveStreamConfig(byte[] bytes) {
            SerializedData serializedData = new SerializedData(bytes);
            if (constructor == serializedData.readInt32(false)) {
                readParams(serializedData, false);
            }
        }

        public static TL_liveStreamConfig TLdeserialize(AbstractSerializedData stream, int constructor2, boolean exception) {
            if (constructor != constructor2) {
                if (exception) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_liveBannedRights", Integer.valueOf(constructor2)));
                }
                return null;
            }
            TL_liveStreamConfig tLRPC$TL_liveStreamConfig = new TL_liveStreamConfig();
            tLRPC$TL_liveStreamConfig.readParams(stream, exception);
            return tLRPC$TL_liveStreamConfig;
        }

        @Override // ir.eitaa.tgnet.TLObject
        public void readParams(AbstractSerializedData stream, boolean exception) {
            int readInt32 = stream.readInt32(exception);
            this.flags = readInt32;
            this.send_in_privateChat = (readInt32 & 1) != 0;
            this.send_in_publicChannel = (readInt32 & 2) != 0;
            this.send_in_privateChannel = (readInt32 & 4) != 0;
            this.send_in_supergroups = (readInt32 & 8) != 0;
            this.send_in_groups = (readInt32 & 16) != 0;
            this.maxVideoWidth = stream.readInt32(exception);
        }

        @Override // ir.eitaa.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData stream) {
            stream.writeInt32(constructor);
            int i = this.send_in_privateChat ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            int i2 = this.send_in_publicChannel ? i | 2 : i & (-3);
            this.flags = i2;
            int i3 = this.send_in_privateChannel ? i2 | 4 : i2 & (-5);
            this.flags = i3;
            int i4 = this.send_in_supergroups ? i3 | 8 : i3 & (-9);
            this.flags = i4;
            int i5 = this.send_in_groups ? i4 | 16 : i4 & (-17);
            this.flags = i5;
            stream.writeInt32(i5);
            stream.writeInt32(this.maxVideoWidth);
        }
    }
    public static class TL_config extends TLObject {
        public static int constructor = 856375399;
        public int base_lang_pack_version;
        public boolean blocked_mode;
        public int call_connect_timeout_ms;
        public int call_packet_timeout_ms;
        public int call_receive_timeout_ms;
        public int call_ring_timeout_ms;
        public int channels_read_media_period;
        public int chat_size_max;
        public int date;
        public int downloadChunkSize_KB;
        public int edit_time_limit;
        public int expires;
        public int flags;
        public int forwarded_count_max;
        public String gif_search_username;
        public String img_search_username;
        public int lang_pack_version;
        public String me_url_prefix;
        public int megagroup_size_max;
        public int notify_cloud_delay_ms;
        public int notify_default_delay_ms;
        public int offline_blur_timeout_ms;
        public int offline_idle_timeout_ms;
        public int online_cloud_timeout_ms;
        public int online_update_period_ms;
        public boolean pfs_enabled;
        public boolean phonecalls_enabled;
        public int pinned_dialogs_count_max;
        public int pinned_infolder_count_max;
        public boolean preload_featured_stickers;
        public int push_chat_limit;
        public int push_chat_period_ms;
        public int rating_e_decay;
        public int saved_gifs_limit;
        public int schedule_period_background_delay_ms;
        public int schedule_period_background_ms;
        public int schedule_period_forground_ms;
        public String static_maps_provider;
        public int stickers_recent_limit;
        public String suggested_lang_code;
        public boolean test_mode;
        public int this_dc;
        public int tmp_sessions;
        public int uploadChunkSize_KB;
        public String venue_search_username;
        public boolean revoke_pm_inbox = true;
        public ArrayList<TLRPC.TL_dcOption> dc_options = new ArrayList<>();
        public String dc_txt_domain_name = "tapv2.stel.com";
        public int revoke_time_limit = 31536000;
        public int revoke_pm_time_limit = 31536000;
        public int stickers_faved_limit = 5;
        public int caption_length_max = 1404;
        public int message_length_max = 4096;
        public int webfile_dc_id = 4;
        public String appURL = "";
        public String socketURL = "";
        public boolean update_available = false;
        public String payHost = "";
        public boolean payEnabled = false;
        public boolean exploreEnabled = false;
        public boolean mxbEnabled = false;
        public boolean callOutEnabled = false;
        public boolean reportVersion = false;
        public TL_liveStreamConfig liveStreamConfig = new TL_liveStreamConfig();

        public static TL_config TLdeserialize(AbstractSerializedData stream, int constructor2, boolean exception) {
            if (constructor != constructor2) {
                if (exception) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_config", Integer.valueOf(constructor2)));
                }
                return null;
            }
            TL_config tLRPC$TL_config = new TL_config();
            tLRPC$TL_config.readParams(stream, exception);
            return tLRPC$TL_config;
        }

        @Override // ir.eitaa.tgnet.TLObject
        public void readParams(AbstractSerializedData stream, boolean exception) {
            int readInt32 = stream.readInt32(exception);
            this.flags = readInt32;
            this.phonecalls_enabled = (readInt32 & 2) != 0;
            this.pfs_enabled = (readInt32 & 8192) != 0;
            this.date = stream.readInt32(exception);
            this.expires = stream.readInt32(exception);
            this.test_mode = stream.readBool(exception);
            this.this_dc = stream.readInt32(exception);
            int readInt322 = stream.readInt32(exception);
            if (readInt322 != 481674261) {
                if (exception) {
                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                }
                return;
            }
            int readInt323 = stream.readInt32(exception);
            for (int i = 0; i < readInt323; i++) {
                TLRPC.TL_dcOption TLdeserialize = TLRPC.TL_dcOption.TLdeserialize(stream, stream.readInt32(exception), exception);
                if (TLdeserialize == null) {
                    return;
                }
                this.dc_options.add(TLdeserialize);
            }
            this.dc_txt_domain_name = stream.readString(exception);
            this.chat_size_max = stream.readInt32(exception);
            this.megagroup_size_max = stream.readInt32(exception);
            this.forwarded_count_max = stream.readInt32(exception);
            this.online_update_period_ms = stream.readInt32(exception);
            this.offline_blur_timeout_ms = stream.readInt32(exception);
            this.offline_idle_timeout_ms = stream.readInt32(exception);
            this.online_cloud_timeout_ms = stream.readInt32(exception);
            this.notify_cloud_delay_ms = stream.readInt32(exception);
            this.notify_default_delay_ms = stream.readInt32(exception);
            this.push_chat_period_ms = stream.readInt32(exception);
            this.push_chat_limit = stream.readInt32(exception);
            this.saved_gifs_limit = stream.readInt32(exception);
            this.edit_time_limit = stream.readInt32(exception);
            this.revoke_time_limit = stream.readInt32(exception);
            this.revoke_pm_time_limit = stream.readInt32(exception);
            this.rating_e_decay = stream.readInt32(exception);
            this.stickers_recent_limit = stream.readInt32(exception);
            this.stickers_faved_limit = stream.readInt32(exception);
            this.channels_read_media_period = stream.readInt32(exception);
            if ((this.flags & 1) != 0) {
                this.tmp_sessions = stream.readInt32(exception);
            }
            this.pinned_dialogs_count_max = stream.readInt32(exception);
            this.pinned_infolder_count_max = stream.readInt32(exception);
            this.call_receive_timeout_ms = stream.readInt32(exception);
            this.call_ring_timeout_ms = stream.readInt32(exception);
            this.call_connect_timeout_ms = stream.readInt32(exception);
            this.call_packet_timeout_ms = stream.readInt32(exception);
            this.me_url_prefix = stream.readString(exception);
            if ((this.flags & 512) != 0) {
                this.gif_search_username = stream.readString(exception);
            }
            if ((this.flags & 1024) != 0) {
                this.venue_search_username = stream.readString(exception);
            }
            if ((this.flags & 2048) != 0) {
                this.img_search_username = stream.readString(exception);
            }
            if ((this.flags & 4096) != 0) {
                this.static_maps_provider = stream.readString(exception);
            }
            this.caption_length_max = stream.readInt32(exception);
            this.message_length_max = stream.readInt32(exception);
            this.webfile_dc_id = stream.readInt32(exception);
            if ((this.flags & 4) != 0) {
                this.suggested_lang_code = stream.readString(exception);
                this.lang_pack_version = stream.readInt32(exception);
                this.base_lang_pack_version = stream.readInt32(exception);
            }
            this.schedule_period_forground_ms = stream.readInt32(exception);
            this.schedule_period_background_ms = stream.readInt32(exception);
            this.schedule_period_background_delay_ms = stream.readInt32(exception);
            this.uploadChunkSize_KB = stream.readInt32(exception);
            this.downloadChunkSize_KB = stream.readInt32(exception);
            boolean z = (this.flags & 64) != 0;
            this.update_available = z;
            if (z) {
                this.appURL = stream.readString(exception);
            }
            int i2 = this.flags;
            boolean z2 = (i2 & 128) != 0;
            this.payEnabled = z2;
            this.callOutEnabled = (32768 & i2) != 0;
            this.exploreEnabled = (65536 & i2) != 0;
            this.mxbEnabled = (131072 & i2) != 0;
            this.reportVersion = (i2 & 262144) != 0;
            if (z2) {
                this.payHost = stream.readString(exception);
            }
            if ((this.flags & 256) != 0) {
                this.liveStreamConfig = TL_liveStreamConfig.TLdeserialize(stream, stream.readInt32(exception), exception);
            }
            if ((this.flags & 16384) != 0) {
                this.socketURL = stream.readString(exception);
            }
        }

        @Override // ir.eitaa.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData stream) {
            TL_liveStreamConfig tLRPC$TL_liveStreamConfig;
            stream.writeInt32(constructor);
            int i = this.phonecalls_enabled ? this.flags | 2 : this.flags & (-3);
            this.flags = i;
            int i2 = this.update_available ? i | 64 : i & (-65);
            this.flags = i2;
            int i3 = this.payEnabled ? i2 | 128 : i2 & (-129);
            this.flags = i3;
            stream.writeInt32(i3);
            stream.writeInt32(this.date);
            stream.writeInt32(this.expires);
            stream.writeBool(this.test_mode);
            stream.writeInt32(this.this_dc);
            stream.writeInt32(481674261);
            int size = this.dc_options.size();
            stream.writeInt32(size);
            for (int i4 = 0; i4 < size; i4++) {
                this.dc_options.get(i4).serializeToStream(stream);
            }
            stream.writeString(this.dc_txt_domain_name);
            stream.writeInt32(this.chat_size_max);
            stream.writeInt32(this.megagroup_size_max);
            stream.writeInt32(this.forwarded_count_max);
            stream.writeInt32(this.online_update_period_ms);
            stream.writeInt32(this.offline_blur_timeout_ms);
            stream.writeInt32(this.offline_idle_timeout_ms);
            stream.writeInt32(this.online_cloud_timeout_ms);
            stream.writeInt32(this.notify_cloud_delay_ms);
            stream.writeInt32(this.notify_default_delay_ms);
            stream.writeInt32(this.push_chat_period_ms);
            stream.writeInt32(this.push_chat_limit);
            stream.writeInt32(this.saved_gifs_limit);
            stream.writeInt32(this.edit_time_limit);
            stream.writeInt32(this.revoke_time_limit);
            stream.writeInt32(this.revoke_pm_time_limit);
            stream.writeInt32(this.rating_e_decay);
            stream.writeInt32(this.stickers_recent_limit);
            stream.writeInt32(this.stickers_faved_limit);
            stream.writeInt32(this.channels_read_media_period);
            if ((this.flags & 1) != 0) {
                stream.writeInt32(this.tmp_sessions);
            }
            stream.writeInt32(this.pinned_dialogs_count_max);
            stream.writeInt32(this.pinned_infolder_count_max);
            stream.writeInt32(this.call_receive_timeout_ms);
            stream.writeInt32(this.call_ring_timeout_ms);
            stream.writeInt32(this.call_connect_timeout_ms);
            stream.writeInt32(this.call_packet_timeout_ms);
            stream.writeString(this.me_url_prefix);
            if ((this.flags & 512) != 0) {
                stream.writeString(this.gif_search_username);
            }
            if ((this.flags & 1024) != 0) {
                stream.writeString(this.venue_search_username);
            }
            if ((this.flags & 2048) != 0) {
                stream.writeString(this.img_search_username);
            }
            if ((this.flags & 4096) != 0) {
                stream.writeString(this.static_maps_provider);
            }
            stream.writeInt32(this.caption_length_max);
            stream.writeInt32(this.message_length_max);
            stream.writeInt32(this.webfile_dc_id);
            if ((this.flags & 4) != 0) {
                stream.writeString(this.suggested_lang_code);
                stream.writeInt32(this.base_lang_pack_version);
                stream.writeInt32(this.lang_pack_version);
            }
            stream.writeInt32(this.schedule_period_forground_ms);
            stream.writeInt32(this.schedule_period_background_ms);
            stream.writeInt32(this.schedule_period_background_delay_ms);
            stream.writeInt32(this.uploadChunkSize_KB);
            stream.writeInt32(this.downloadChunkSize_KB);
            if (this.update_available) {
                stream.writeString(this.appURL);
            }
            if (this.payEnabled) {
                stream.writeString(this.payHost);
            }
            if ((this.flags & 256) != 0 && (tLRPC$TL_liveStreamConfig = this.liveStreamConfig) != null) {
                tLRPC$TL_liveStreamConfig.serializeToStream(stream);
            }
            if ((this.flags & 16384) != 0) {
                stream.writeString(this.socketURL);
            }
        }
    }
    public static class TL_auth_sentCode extends TLObject {
        public static int constructor = 1577067778;
        public int flags;
        public String message;
        public TLRPC.auth_CodeType next_type;
        public String phone_code_hash;
        public int timeout;
        public TLRPC.auth_SentCodeType type;

        public static TL_auth_sentCode TLdeserialize(AbstractSerializedData stream, int constructor2, boolean exception) {
            if (constructor != constructor2) {
                if (exception) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_auth_sentCode", Integer.valueOf(constructor2)));
                }
                return null;
            }
            TL_auth_sentCode tLRPC$TL_auth_sentCode = new TL_auth_sentCode();
            tLRPC$TL_auth_sentCode.readParams(stream, exception);
            return tLRPC$TL_auth_sentCode;
        }

        @Override // ir.eitaa.tgnet.TLObject
        public void readParams(AbstractSerializedData stream, boolean exception) {
            this.flags = stream.readInt32(exception);
            this.type = TLRPC.auth_SentCodeType.TLdeserialize(stream, stream.readInt32(exception), exception);
            this.phone_code_hash = stream.readString(exception);
            if ((this.flags & 2) != 0) {
                this.next_type = TLRPC.auth_CodeType.TLdeserialize(stream, stream.readInt32(exception), exception);
            }
            if ((this.flags & 4) != 0) {
                this.timeout = stream.readInt32(exception);
            }
            if ((this.flags & 256) != 0) {
                this.message = stream.readString(exception);
            }
        }

        @Override // ir.eitaa.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.flags);
            this.type.serializeToStream(stream);
            stream.writeString(this.phone_code_hash);
            if ((this.flags & 2) != 0) {
                this.next_type.serializeToStream(stream);
            }
            if ((this.flags & 4) != 0) {
                stream.writeInt32(this.timeout);
            }
            if ((this.flags & 256) != 0) {
                stream.writeString(this.message);
            }
        }
    }
    public static abstract class auth_Authorization extends TLObject {

        public static auth_Authorization TLdeserialize(AbstractSerializedData stream, int constructor, boolean exception) {
            auth_Authorization result = null;
            switch (constructor) {
                case 0x44747e9a:
                    result = new TL_auth_authorizationSignUpRequired();
                    break;
                case 0x33fb7bb8:
                    result = new TL_auth_authorization();
                    break;
            }
            if (result == null && exception) {
                throw new RuntimeException(String.format("can't parse magic %x in auth_Authorization", constructor));
            }
            if (result != null) {
                result.readParams(stream, exception);
            }
            return result;
        }
    }

    public static class TL_auth_authorizationSignUpRequired extends auth_Authorization {
        public static int constructor = 1148485274;
        public int flags;
        public TLRPC.TL_help_termsOfService terms_of_service;

        @Override // ir.eitaa.tgnet.TLObject
        public void readParams(AbstractSerializedData stream, boolean exception) {
            int readInt32 = stream.readInt32(exception);
            this.flags = readInt32;
            if ((readInt32 & 1) != 0) {
                this.terms_of_service = TLRPC.TL_help_termsOfService.TLdeserialize(stream, stream.readInt32(exception), exception);
            }
        }

        @Override // ir.eitaa.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.flags);
            if ((this.flags & 1) != 0) {
                this.terms_of_service.serializeToStream(stream);
            }
        }

    }
    public static class TL_auth_authorization extends auth_Authorization {
        public static int constructor = -855308010;
        public int flags;
        public int tmp_sessions;
        public String token;
        public TLRPC.User user;

        @Override // ir.eitaa.tgnet.TLObject
        public void readParams(AbstractSerializedData stream, boolean exception) {
            this.flags = stream.readInt32(exception);
            this.token = stream.readString(exception);
            if ((this.flags & 1) != 0) {
                this.tmp_sessions = stream.readInt32(exception);
            }
            this.user = TLRPC.User.TLdeserialize(stream, stream.readInt32(exception), exception);
        }

        @Override // ir.eitaa.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt32(this.flags);
            stream.writeString(this.token);
            if ((this.flags & 1) != 0) {
                stream.writeInt32(this.tmp_sessions);
            }
            this.user.serializeToStream(stream);
        }
    }
    /* loaded from: classes.dex */
    public static class TL_clientRequest extends TLObject {
        public boolean appPause;
        public int buildVersion;
        public int flags;
        public boolean foregreoundConnection;
        public String imei;
        public boolean isData;
        public boolean isWifi;
        public String lang;
        public int layer;
        public byte[] packed_data;
        public String token;

        @Override // ir.eitaa.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData stream) {
            throw null;
        }
    }
    public static class TL_clientRequestAPK extends TL_clientRequest {
        public static int constructor = 2059302894;

        @Override // ir.eitaa.tgnet.TLObject
        public void readParams(AbstractSerializedData stream, boolean exception) {
            this.token = stream.readString(exception);
            this.imei = stream.readString(exception);
            int readInt32 = stream.readInt32(exception);
            this.flags = readInt32;
            this.isWifi = (readInt32 & 2) != 0;
            this.isData = (readInt32 & 4) != 0;
            this.appPause = (readInt32 & 8) != 0;
            this.foregreoundConnection = (readInt32 & 16) != 0;
            this.packed_data = stream.readByteArray(exception);
            this.layer = stream.readInt32(exception);
            this.buildVersion = stream.readInt32(exception);
            this.lang = stream.readString(exception);
        }

        @Override // ir.eitaa.tgnet.TLRPC$TL_clientRequest, ir.eitaa.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData stream) {
            stream.writeInt32(constructor);
            int i = this.isWifi ? this.flags | 2 : this.flags & (-3);
            this.flags = i;
            int i2 = this.isData ? i | 4 : i & (-5);
            this.flags = i2;
            int i3 = this.appPause ? i2 | 8 : i2 & (-9);
            this.flags = i3;
            this.flags = this.foregreoundConnection ? i3 | 16 : i3 & (-17);
            this.flags |= 128;
            stream.writeString(this.token);
            stream.writeString(this.imei);
            stream.writeInt32(this.flags);
            stream.writeByteArray(this.packed_data);
            stream.writeInt32(this.layer);
            stream.writeInt32(this.buildVersion);
            stream.writeString(this.lang);
        }
    }
    public static class TL_gzip_packed extends TLObject {
        public static int constructor = 812830625;
        public byte[] packed_data;

        @Override // ir.eitaa.tgnet.TLObject
        public void readParams(AbstractSerializedData stream, boolean exception) {
            this.packed_data = stream.readByteArray(exception);
        }

        @Override // ir.eitaa.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeByteArray(this.packed_data);
        }
    }
    public static class TL_upload_saveFilePart extends TLObject {
        public static int constructor = -1291540959;
        public byte[] bytes;
        public long file_id;
        public int file_part;
        public int flags;
        public TLRPC.Peer peer;
        public long totalFileSize;

        @Override // ir.eitaa.tgnet.TLObject
        public TLObject deserializeResponse(AbstractSerializedData stream, int constructor2, boolean exception) {
            return TLRPC.Bool.TLdeserialize(stream, constructor2, exception);
        }

        @Override // ir.eitaa.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData stream) {
            stream.writeInt32(constructor);
            stream.writeInt64(this.file_id);
            stream.writeInt32(this.file_part);
            stream.writeByteArray(this.bytes);
            int i = this.flags | 2;
            this.flags = i;
            if (this.peer != null) {
                int i2 = i | 1;
                this.flags = i2;
                stream.writeInt32(i2);
                this.peer.serializeToStream(stream);
            } else {
                int i3 = i & (-2);
                this.flags = i3;
                stream.writeInt32(i3);
            }
            stream.writeInt64(this.totalFileSize);
        }

        @Override // ir.eitaa.tgnet.TLObject
        public void freeResources() {
            if (this.disableFree) {
            }
        }
    }
}
