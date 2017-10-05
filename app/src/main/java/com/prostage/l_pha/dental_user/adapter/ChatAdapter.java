package com.prostage.l_pha.dental_user.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.prostage.l_pha.dental_user.R;
import com.prostage.l_pha.dental_user.activity.FullScreenImageActivity;
import com.prostage.l_pha.dental_user.common.Constants;
import com.prostage.l_pha.dental_user.firebaseUI.InfiniteFirebaseRecyclerAdapter;
import com.prostage.l_pha.dental_user.model.chat_model.ChatModel;
import com.prostage.l_pha.dental_user.utils.UtilsHelper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by USER on 31-Mar-17.
 */

public class ChatAdapter extends FirebaseRecyclerAdapter<ChatModel, ChatAdapter.mViewHolderChat> {

    public static final int RIGHT_MSG = 0;
    public static final int LEFT_MSG = 1;
    public static final int RIGHT_MSG_IMG = 2;
    public static final int LEFT_MSG_IMG = 3;
//    public static final int RIGHT_MSG_AUD = 4;
//    public static final int LEFT_MSG_AUD = 5;

//    private MediaPlayer mPlayer = null;
//    private Boolean clickPlay = true;

    private String mCurrentUserId;
    private Context mContext;
    private int mPageLimit;

    public ChatAdapter(Context context, String mCurrentUserId, Query query) {
        super(ChatModel.class, R.layout.item_message_left, ChatAdapter.mViewHolderChat.class, query);

        this.mContext = context;
        this.mCurrentUserId = mCurrentUserId;
    }

    @Override
    public mViewHolderChat onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case RIGHT_MSG:
                View right_msg = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
                mViewHolderChat right_msgView = new mViewHolderChat(right_msg);
                return right_msgView;

            case LEFT_MSG:
                View left_msg = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
                mViewHolderChat left_msgView = new mViewHolderChat(left_msg);
                return left_msgView;

            case RIGHT_MSG_IMG:
                View right_msg_img = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_img_right, parent, false);
                mViewHolderChat right_msg_imgView = new mViewHolderChat(right_msg_img);
                return right_msg_imgView;

            case LEFT_MSG_IMG:
                View left_msg_img = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_img_left, parent, false);
                mViewHolderChat left_msg_imgView = new mViewHolderChat(left_msg_img);
                return left_msg_imgView;

/*            case RIGHT_MSG_AUD:
                View right_msg_aud = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_aud_right, parent, false);
                mViewHolderChat right_msg_audView = new mViewHolderChat(right_msg_aud);
                return right_msg_audView;

            case LEFT_MSG_AUD:
                View left_msg_aud = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_aud_left, parent, false);
                mViewHolderChat left_msg_audView = new mViewHolderChat(left_msg_aud);
                return left_msg_audView;*/
        }
        return null;
    }

    @Override
    protected void populateViewHolder(mViewHolderChat viewHolder, ChatModel model, int position) {
        if (model == null) {
            return;
        } else {
            //show text
            viewHolder.setTxtMessage(model.getText());

            //show time
            viewHolder.setTxtTimeStamp(model.getDate());

            //show group date
            viewHolder.setTxtDateStamp("------------- " + UtilsHelper.subDate_ddMMyyy(model.getDate()) + " -------------");

            //show image
            if (model.getFile() != null && model.getText().equals("image"))
                viewHolder.setImgPhoto(model.getFile().getUrl());

            //show voice
//            if (model.getFile() != null && model.getText().equals("audio"))
//                viewHolder.setImgVoid(model.getFile().getUrl());
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chatModel = getItem(position);
        if (chatModel.getFile() != null && chatModel.getText().equals("image")) {
            if (chatModel.getSenderId().equals(mCurrentUserId))
                return RIGHT_MSG_IMG;
            return LEFT_MSG_IMG;

        } /*else if (chatModel.getFile() != null && chatModel.getText().equals("audio")) {
            if (chatModel.getSenderId().equals(mCurrentUserId))
                return RIGHT_MSG_AUD;
            return LEFT_MSG_AUD;

        }*/ else if (chatModel.getFile() == null && chatModel.getSenderId().equals(mCurrentUserId))
            return RIGHT_MSG;
        return LEFT_MSG;
    }

    //my ViewHolder
    public class mViewHolderChat extends RecyclerView.ViewHolder {
        private TextView txtTimeStamp, txtDateStamp;
        private EmojiconTextView txtEmojiconMessage;
        private ImageView imgPhoto, imgVoid, imgAvatar;
        private ProgressBar progressLoad;
//        private ProgressBar progressBarLoadVoid;
//        private Handler handler;

        public mViewHolderChat(View itemView) {
            super(itemView);
            txtTimeStamp = (TextView) itemView.findViewById(R.id.textViewTimeStamp);
            txtDateStamp = (TextView) itemView.findViewById(R.id.textViewDate);
            txtEmojiconMessage = (EmojiconTextView) itemView.findViewById(R.id.emojiconTextViewMessage);
            imgPhoto = (ImageView) itemView.findViewById(R.id.imageViewChat);
//            imgVoid = (ImageView) itemView.findViewById(R.id.imageViewVoid);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imageViewUserChat);
            progressLoad = (ProgressBar) itemView.findViewById(R.id.progressBarLoad);
//            progressBarLoadVoid = (ProgressBar) itemView.findViewById(R.id.progressBarLoadVoice);
        }

        public void setImgAvatar() {
            if (imgAvatar == null)
                return;
            Glide.with(imgAvatar.getContext()).load(R.mipmap.ic_avatar_right).centerCrop().into(imgAvatar);
        }

        //set text message
        public void setTxtMessage(String message) {
            if (txtEmojiconMessage == null)
                return;
            txtEmojiconMessage.setText(message);
        }

        //set time message
        public void setTxtTimeStamp(String timestamp) {
            if (txtTimeStamp == null) {
                return;
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                try {
                    Date value = simpleDateFormat.parse(timestamp);
                    timestamp = android.text.format.DateFormat.getTimeFormat(mContext).format(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                txtTimeStamp.setText(timestamp);
            }
        }

        //set date message
        public void setTxtDateStamp(String datestamp) {
            if (txtDateStamp == null) {
                return;
            } else {
                int position = getAdapterPosition();
                String dateGroup = showCurrentDate(position);

                if (TextUtils.isEmpty(dateGroup)) {
                    txtDateStamp.setVisibility(View.GONE);
                } else {
                    txtDateStamp.setVisibility(View.VISIBLE);
                    txtDateStamp.setText(datestamp);
                }
            }
        }

        private String showCurrentDate(int position) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                ChatModel model = getItem(position);
                Date value = null;

                if (!TextUtils.isEmpty(model.getDate()))
                    value = simpleDateFormat.parse(model.getDate());

                if (value != null) {
                    if (position > 0) {
                        ChatModel modelPrev = getItem(position - 1);
                        Date valuePrev = null;

                        if (!TextUtils.isEmpty(modelPrev.getDate())) {
                            valuePrev = simpleDateFormat.parse(modelPrev.getDate());

                            //so sanh ngay hien tai va ngay truoc do
                            if (value.compareTo(valuePrev) != 0) {
                                DateFormat outFormat = android.text.format.DateFormat.getDateFormat(mContext);
                                return outFormat.format(value);

                            } else
                                return null;
                        }

                    } else {
                        DateFormat outFormat = android.text.format.DateFormat.getDateFormat(mContext);
                        return outFormat.format(value);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        //set image avatar
        public void setImgPhoto(String url) {
            if (imgPhoto == null)
                return;
            Glide.with(imgPhoto.getContext())
                    .load(url)
                    .asBitmap()
                    .override(768, 768)
                    .fitCenter()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            progressLoad.setVisibility(View.VISIBLE);
                            imgPhoto.setImageDrawable(itemView.getResources().getDrawable(R.drawable.placeholder));
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            imgPhoto.setImageBitmap(resource);
                            progressLoad.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            progressLoad.setVisibility(View.GONE);
                        }
                    });

            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ChatModel chatModel = getItem(position);
                    if (chatModel.getFile() != null && chatModel.getText().equals("image")) {
                        Intent intent = new Intent(mContext, FullScreenImageActivity.class);
                        intent.putExtra("idUser", chatModel.getSenderId());
                        intent.putExtra("urlPhotoUser", chatModel.getFile().getName());
                        intent.putExtra("urlPhotoClick", chatModel.getFile().getUrl());
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        //set audio
/*        public void setImgVoid(final String url) {
            if (imgVoid == null)
                return;
            imgVoid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ChatModel chatModel = getItem(position);
                    if (chatModel.getFile() != null && chatModel.getText().equals("audio")) {
                        if (clickPlay) {
                            progressBarLoadVoid.setVisibility(View.VISIBLE);
                            imgVoid.setImageDrawable(itemView.getResources().getDrawable(R.mipmap.ic_pause));
                            //start playing void
                            try {
                                if (mPlayer == null) {
                                    handler = new Handler();
                                    mPlayer = new MediaPlayer();
                                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            stopPlayer();
                                        }
                                    });
                                    mPlayer.setDataSource(url);
                                    mPlayer.prepare();
                                    progressBarLoadVoid.setMax(mPlayer.getDuration());//max cua progressbar
                                    progressBarLoadVoid.setProgress(mPlayer.getCurrentPosition());//vi tri hien tai
                                }
                                progressBarUpdation();
                                mPlayer.start();
                            } catch (IOException e) {
                                Log.e(Constants.LOG_TAG, "prepare() failed");
                            }

                            clickPlay = false;
                        } else {
                            pausePlayer();
                        }
                    }
                }

                private void pausePlayer() {
                    imgVoid.setImageDrawable(itemView.getResources().getDrawable(R.mipmap.ic_play));

                    //pause playing void
                    mPlayer.pause();
                    clickPlay = true;
                }

                private void stopPlayer() {
                    imgVoid.setImageDrawable(itemView.getResources().getDrawable(R.mipmap.ic_play));
                    progressBarLoadVoid.setVisibility(View.GONE);

                    //stop playing void
                    mPlayer.release();
                    mPlayer = null;
                    clickPlay = true;
                }

                //cap nhat progressbar dung de quy
                private void progressBarUpdation() {
                    if (mPlayer == null) {
                        return;
                    }
                    progressBarLoadVoid.setProgress(mPlayer.getCurrentPosition());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBarUpdation();
                        }
                    }, 0);
                }
            });
        }*/
    }
}
