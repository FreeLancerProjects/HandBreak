package com.creativeshare.hand_break.notifications;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.chat_activity.ChatActivity;
import com.creativeshare.hand_break.models.ChatUserModel;
import com.creativeshare.hand_break.models.MessageModel;
import com.creativeshare.hand_break.models.TypingModel;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Random;

public class FireBaseMessaging extends FirebaseMessagingService {
    Preferences preferences = Preferences.getInstance();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        final Map<String, String> map = remoteMessage.getData();

        for (String key : map.keySet())
        {
            Log.e("key : ",key+" value :"+ map.get(key));
        }
        if (getSession().equals(Tags.session_login)) {

            String current_user_id = getUserData().getUser_id();
            String to_user_id = map.get("to_user");
            if (current_user_id.equals(to_user_id)) {
                ManageNotification(map);
            }

        }


    }

    private void ManageNotification(Map<String, String> map) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNewVersionNotification(map);

        } else {
            createOldVersionNotification(map);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNewVersionNotification(final Map<String, String> map) {

        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String notification_type = map.get("notification_type");


        if (notification_type!=null&&notification_type.equals(Tags.FIREBASE_NOT_SEND_MESSAGE)) {

            String room_id_fk = map.get("room_id_fk");
            String message_id = map.get("id_message");
            String date = map.get("date");
            String message = map.get("message");

            String from_user_id = map.get("from_user");
            String to_user_id = map.get("to_user");
            String from_name = map.get("from_user_full_name");
            final String from_user_image = map.get("from_user_image");
            String to_user_full_name = map.get("to_user_full_name");
            String to_user_image = map.get("to_user_image");
            String message_type = map.get("chat_message_type");

            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String class_name = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            if (class_name.equals("com.creativeshare.hand_break.activities_fragments.chat_activity.ChatActivity")) {
                if (room_id_fk!=null&&room_id_fk.equals(getChatUserModel().getRoom_id())) {

                    MessageModel messageModel = new MessageModel(message_id,room_id_fk,date,message,message_type,"0",from_user_id,from_name,from_user_image,"","",to_user_id,to_user_full_name,to_user_image,"","");
                    EventBus.getDefault().post(messageModel);
                } else {
                    String CHANNEL_ID = "my_channel_02";
                    CharSequence CHANNEL_NAME = "my_channel_name";
                    int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

                    final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                    final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
                    channel.setShowBadge(true);
                    channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                            .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                            .build()
                    );
                    builder.setChannelId(CHANNEL_ID);
                    builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
                    builder.setSmallIcon(R.drawable.ic_notification);
                    builder.setContentTitle(map.get("from_name"));

                    Intent intent = new Intent(this, ChatActivity.class);
                    ChatUserModel chatUserModel = new ChatUserModel(from_name, from_user_image, from_user_id, room_id_fk,"");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("data", chatUserModel);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);

                    builder.setContentText(message);

                    final Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            if (manager != null) {
                                builder.setLargeIcon(bitmap);
                                manager.createNotificationChannel(channel);
                                manager.notify(new Random().nextInt(200), builder.build());
                            }

                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };


                    new Handler(Looper.getMainLooper())
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (from_user_image.equals("0"))
                                    {
                                        Picasso.with(FireBaseMessaging.this).load(R.drawable.logoa).into(target);

                                    }else
                                    {
                                        Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + from_user_image)).resize(250,250).into(target);

                                    }

                                }
                            }, 1);



                }
            } else {
                String CHANNEL_ID = "my_channel_02";
                CharSequence CHANNEL_NAME = "my_channel_name";
                int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

                final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
                channel.setShowBadge(true);
                channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                        .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                        .build()
                );
                builder.setChannelId(CHANNEL_ID);
                builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
                builder.setSmallIcon(R.drawable.ic_notification);
                builder.setContentTitle(map.get("from_name"));

                Intent intent = new Intent(this, ChatActivity.class);
                ChatUserModel chatUserModel = new ChatUserModel(from_name, from_user_image, from_user_id, room_id_fk,"");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("data", chatUserModel);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                builder.setContentText(message);

                final Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        if (manager != null) {
                            builder.setLargeIcon(bitmap);
                            manager.createNotificationChannel(channel);
                            manager.notify(new Random().nextInt(200), builder.build());
                        }

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                new Handler(Looper.getMainLooper())
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (from_user_image.equals("0"))
                                {
                                    Picasso.with(FireBaseMessaging.this).load(R.drawable.logoa).into(target);

                                }else
                                {
                                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + from_user_image)).resize(250,250).into(target);

                                }

                            }
                        }, 1);

            }


        } else {

            String CHANNEL_ID = "my_channel_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            channel.setShowBadge(true);
            channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );

            builder.setChannelId(CHANNEL_ID);
            builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
            builder.setSmallIcon(R.drawable.ic_notification);

            if (notification_type!=null&&notification_type.equals(Tags.FIREBASE_NOT_TYPING)) {
                builder.setContentTitle(map.get("from_name"));
                String from_user_id = map.get("from_user");
                String to_user_id = map.get("to_user");
                String room_id = map.get("room_id");
                String typing_value = map.get("typing_value");
                String from_name = map.get("from_name");

                TypingModel typingModel = new TypingModel(from_user_id, to_user_id, room_id, typing_value, from_name);
                EventBus.getDefault().post(typingModel);

            }
            /*
            if (notification_type.equals(Tags.FIREBASE_NOT_ORDER_STATUS)) {


                builder.setContentTitle(map.get("from_name"));

                Intent intent = new Intent(this, ClientHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                String order_status = map.get("order_status");
                Log.e("order_status",order_status+"_");
                final NotStateModel notStateModel = new NotStateModel(order_status);

                if (order_status.equals(String.valueOf(Tags.STATE_ORDER_NEW))) {
                    builder.setContentText(getString(R.string.new_order_sent));


                } else if (order_status.equals(String.valueOf(Tags.STATE_DELEGATE_SEND_OFFER))) {
                    builder.setContentText(getString(R.string.delegate_accept_order));

                } else if (order_status.equals(String.valueOf(Tags.STATE_DELEGATE_REFUSE_ORDER))) {
                    builder.setContentText(getString(R.string.order_refused));


                } else if (order_status.equals(String.valueOf(Tags.STATE_CLIENT_ACCEPT_OFFER))) {
                    builder.setContentText(getString(R.string.offer_accepted));

                } else if (order_status.equals(String.valueOf(Tags.STATE_CLIENT_REFUSE_OFFER))) {
                    builder.setContentText(getString(R.string.offer_refused));

                }else if (order_status.equals(String.valueOf(Tags.STATE_DELEGATE_COLLECTING_ORDER))) {
                    builder.setContentText(getString(R.string.collecting_order));

                }else if (order_status.equals(String.valueOf(Tags.STATE_DELEGATE_COLLECTED_ORDER))) {
                    builder.setContentText(getString(R.string.order_collected));

                }else if (order_status.equals(String.valueOf(Tags.STATE_DELEGATE_DELIVERING_ORDER))) {
                    builder.setContentText(getString(R.string.delivering_order));

                }else if (order_status.equals(String.valueOf(Tags.STATE_DELEGATE_DELIVERED_ORDER))) {
                    builder.setContentText(getString(R.string.order_delivered_successfully));

                }

                intent.putExtra("status", order_status);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                final Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        if (manager != null) {
                            builder.setLargeIcon(bitmap);
                            EventBus.getDefault().post(notStateModel);
                            manager.createNotificationChannel(channel);
                            manager.notify(new Random().nextInt(200), builder.build());
                        }

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };


                new Handler(Looper.getMainLooper())
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                String from_image = map.get("from_image");
                                if (from_image.equals("0"))
                                {

                                    Picasso.with(FireBaseMessaging.this).load(R.drawable.logo_only).into(target);

                                }else
                                {
                                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + from_image)).resize(250,250).into(target);

                                }




                            }
                        }, 1);



            }
            else if (notification_type.equals(Tags.FIREBASE_NOT_RATE)) {

                NotificationTypeModel notificationTypeModel = new NotificationTypeModel(Tags.FIREBASE_NOT_RATE);
                EventBus.getDefault().post(notificationTypeModel);

            }

*/
        }


    }

    private void createOldVersionNotification(final Map<String, String> map) {

        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String notification_type = map.get("notification_type");


        if (notification_type!=null&&notification_type.equals(Tags.FIREBASE_NOT_SEND_MESSAGE)) {

            String room_id_fk = map.get("room_id_fk");
            String message_id = map.get("id_message");
            String date = map.get("date");
            String message = map.get("message");

            String from_user_id = map.get("from_user");
            String to_user_id = map.get("to_user");
            String from_name = map.get("from_user_full_name");
            final String from_user_image = map.get("from_user_image");
            String to_user_full_name = map.get("to_user_full_name");
            String to_user_image = map.get("to_user_image");
            String message_type = map.get("chat_message_type");
            String msg_image = map.get("file");

            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String class_name = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            if (class_name.equals("com.creativeshare.hand_break.activities_fragments.chat_activity.ChatActivity")) {
                if (room_id_fk!=null&&room_id_fk.equals(getChatUserModel().getRoom_id())) {

                    MessageModel messageModel = new MessageModel(message_id,room_id_fk,date,message,message_type,msg_image,from_user_id,from_name,from_user_image,"","",to_user_id,to_user_full_name,to_user_image,"","");
                    EventBus.getDefault().post(messageModel);
                } else {

                    final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                    builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
                    builder.setSmallIcon(R.drawable.ic_notification);
                    builder.setContentTitle(map.get("from_name"));

                    Intent intent = new Intent(this, ChatActivity.class);
                    ChatUserModel chatUserModel = new ChatUserModel(from_name, from_user_image, from_user_id, room_id_fk,"");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("data", chatUserModel);

                    builder.setContentText(message);

                    final Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            if (manager != null) {
                                builder.setLargeIcon(bitmap);
                                manager.notify(new Random().nextInt(200), builder.build());
                            }

                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };


                    new Handler(Looper.getMainLooper())
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (from_user_image.equals("0"))
                                    {
                                        Picasso.with(FireBaseMessaging.this).load(R.drawable.logoa).into(target);

                                    }else
                                    {
                                        Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + from_user_image)).resize(250,250).into(target);

                                    }

                                }
                            }, 1);



                }
            } else {

                final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
                builder.setSmallIcon(R.drawable.ic_notification);
                builder.setContentTitle(map.get("from_name"));

                Intent intent = new Intent(this, ChatActivity.class);
                ChatUserModel chatUserModel = new ChatUserModel(from_name, from_user_image, from_user_id, room_id_fk,"");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("data", chatUserModel);

                builder.setContentText(message);

                final Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        if (manager != null) {
                            builder.setLargeIcon(bitmap);
                            manager.notify(new Random().nextInt(200), builder.build());
                        }

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                new Handler(Looper.getMainLooper())
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (from_user_image.equals("0"))
                                {
                                    Picasso.with(FireBaseMessaging.this).load(R.drawable.logoa).into(target);

                                }else
                                {
                                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + from_user_image)).resize(250,250).into(target);

                                }

                            }
                        }, 1);

            }


        } else {

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
            builder.setSmallIcon(R.drawable.ic_notification);

            if (notification_type!=null&&notification_type.equals(Tags.FIREBASE_NOT_TYPING)) {
                builder.setContentTitle(map.get("from_name"));
                String from_user_id = map.get("from_user");
                String to_user_id = map.get("to_user");
                String room_id = map.get("room_id");
                String typing_value = map.get("typing_value");
                String from_name = map.get("from_name");

                TypingModel typingModel = new TypingModel(from_user_id, to_user_id, room_id, typing_value, from_name);
                EventBus.getDefault().post(typingModel);

            }

/*

            if (notification_type.equals(Tags.FIREBASE_NOT_ORDER_STATUS)) {
                builder.setContentTitle(map.get("from_name"));



                Intent intent = new Intent(this, ClientHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                String order_status = map.get("order_status");
                final NotStateModel notStateModel = new NotStateModel(order_status);

                if (order_status.equals(String.valueOf(Tags.STATE_ORDER_NEW))) {
                    builder.setContentText(getString(R.string.new_order_sent));


                } else if (order_status.equals(String.valueOf(Tags.STATE_DELEGATE_SEND_OFFER))) {
                    builder.setContentText(getString(R.string.delegate_accept_order));

                } else if (order_status.equals(String.valueOf(Tags.STATE_DELEGATE_REFUSE_ORDER))) {
                    builder.setContentText(getString(R.string.order_refused));


                } else if (order_status.equals(String.valueOf(Tags.STATE_CLIENT_ACCEPT_OFFER))) {
                    builder.setContentText(getString(R.string.offer_accepted));

                } else if (order_status.equals(String.valueOf(Tags.STATE_CLIENT_REFUSE_OFFER))) {
                    builder.setContentText(getString(R.string.offer_refused));

                }else if (order_status.equals(String.valueOf(Tags.STATE_DELEGATE_COLLECTING_ORDER))) {
                    builder.setContentText(getString(R.string.collecting_order));

                }else if (order_status.equals(String.valueOf(Tags.STATE_DELEGATE_COLLECTED_ORDER))) {
                    builder.setContentText(getString(R.string.order_collected));

                }else if (order_status.equals(String.valueOf(Tags.STATE_DELEGATE_DELIVERING_ORDER))) {
                    builder.setContentText(getString(R.string.delivering_order));

                }else if (order_status.equals(String.valueOf(Tags.STATE_DELEGATE_DELIVERED_ORDER))) {
                    builder.setContentText(getString(R.string.order_delivered_successfully));

                }

                intent.putExtra("status", order_status);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                final Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        if (manager != null) {
                            builder.setLargeIcon(bitmap);
                            EventBus.getDefault().post(notStateModel);
                            manager.notify(new Random().nextInt(200), builder.build());
                        }

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };


                new Handler(Looper.getMainLooper())
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                String from_image = map.get("from_image");
                                if (from_image.equals("0"))
                                {

                                    Picasso.with(FireBaseMessaging.this).load(R.drawable.logo_only).into(target);

                                }else
                                {
                                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + from_image)).resize(250,250).into(target);

                                }




                            }
                        }, 1);



            }
            else if (notification_type.equals(Tags.FIREBASE_NOT_RATE)) {

                NotificationTypeModel notificationTypeModel = new NotificationTypeModel(Tags.FIREBASE_NOT_RATE);
                EventBus.getDefault().post(notificationTypeModel);

            }



           */

        }

    }

    private UserModel getUserData() {
        return preferences.getUserData(this);
    }

    private String getSession() {
        return preferences.getSession(this);
    }

    private ChatUserModel getChatUserModel() {
        return preferences.getChatUserData(this);
    }
}
