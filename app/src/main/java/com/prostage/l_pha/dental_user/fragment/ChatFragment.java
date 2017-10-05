package com.prostage.l_pha.dental_user.fragment;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prostage.l_pha.dental_user.BuildConfig;
import com.prostage.l_pha.dental_user.R;
import com.prostage.l_pha.dental_user.activity.MainActivity;
import com.prostage.l_pha.dental_user.adapter.ChatAdapter;
import com.prostage.l_pha.dental_user.common.Constants;
import com.prostage.l_pha.dental_user.firebaseUI.EndlessRecyclerViewScrollListener;
import com.prostage.l_pha.dental_user.firebaseUI.Logger;
import com.prostage.l_pha.dental_user.model.chat_model.ChatModel;
import com.prostage.l_pha.dental_user.model.chat_model.FileModel;
import com.prostage.l_pha.dental_user.model.server_model.user_model.UserModel;
import com.prostage.l_pha.dental_user.notification.FcmNotificationBuilder;
import com.prostage.l_pha.dental_user.utils.ResizeImageView;
import com.prostage.l_pha.dental_user.utils.SharedHelper;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.app.Activity.RESULT_OK;
import static com.prostage.l_pha.dental_user.common.Constants.CHAT_REF;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment implements ValueEventListener {

    private EmojiconEditText edtEmojIconMessage;
    private ImageView imgAdd, imgEmoji, imgMic, imgSend;
    private File filePathImageCamera;
    private EmojIconActions emojIcon;

    private RecyclerView rvListMessage;
    private ChatAdapter chatAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;

    private ResizeImageView resizeImage;

    private SharedHelper sharedHelper;
    private UserModel userModel;

    public static final int IMAGE_GALLERY_REQUEST = 1001;
    public static final int IMAGE_CAMERA_REQUEST = 2002;

    private static String mFileName = null;
//    private MediaRecorder mRecorder = null;
//    private Boolean clickRecord = true;

//    private int currentPage = 0;
//    private static final int TOTAL_ITEM_EACH_LOAD = 10;
    private ProgressBar mProgressBar;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewChat = inflater.inflate(R.layout.fragment_chat, container, false);

        //addControls
        View contentChat = viewChat.findViewById(R.id.fragmentChat);
        imgAdd = (ImageView) viewChat.findViewById(R.id.imageViewAdd);
        edtEmojIconMessage = (EmojiconEditText) viewChat.findViewById(R.id.emojiconEditTextMessage);
        imgEmoji = (ImageView) viewChat.findViewById(R.id.imageViewEmoji);
//        imgMic = (ImageView) viewChat.findViewById(R.id.imageViewMic);
        imgSend = (ImageView) viewChat.findViewById(R.id.imageViewSend);
        rvListMessage = (RecyclerView) viewChat.findViewById(R.id.recyclerViewMessage);
        mProgressBar = (ProgressBar) viewChat.findViewById(R.id.progressbar);

        //chuyen doi qua lai giua keyboard va emoji
        emojIcon = new EmojIconActions(getActivity(), contentChat, edtEmojIconMessage, imgEmoji);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_keyboard, R.drawable.ic_emoticon);

        return viewChat;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedHelper = new SharedHelper(getActivity());
        userModel = ((MainActivity) getActivity()).getUserModel();

        resizeImage = new ResizeImageView();
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //addEvents receive message
        receiveMsgFirebase();

        //event send text by KEYCODE_ENTER
        edtEmojIconMessage.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    sendMessageFirebase();
                    return true;
                }
                return false;
            }
        });

        //addEvents send message
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageFirebase();
            }
        });

        //addEvents add void
        /*imgMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, MainActivity.REQUEST_MICRO_PERMISSIONS);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog_send_audio, null);
                builder.setView(dialogView);

                final TextView txtRecord = (TextView) dialogView.findViewById(R.id.textViewRecord);
                final ImageView imgRecord = (ImageView) dialogView.findViewById(R.id.imageViewRecord);
                TextView cancel_dialog = (TextView) dialogView.findViewById(R.id.textViewCancel);

                final AlertDialog dialog = builder.create();

                //Handle click events
                imgRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickRecord) {
                            imgRecord.setImageDrawable(getResources().getDrawable(R.mipmap.ic_stop));
                            startRecording();
                            txtRecord.setText(getString(R.string.dialog_dialog_start));
                            clickRecord = false;
                        } else {
                            imgRecord.setImageDrawable(getResources().getDrawable(R.mipmap.ic_voice));
                            stopRecording();
                            txtRecord.setText(getString(R.string.dialog_dialog_stop));
                            clickRecord = true;
                            dialog.dismiss();
                        }
                    }
                });

                cancel_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });*/

        //addEvents add image
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.REQUEST_STORAGE_PERMISSIONS);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog_send_images, null);
                builder.setView(dialogView);

                LinearLayout linear_gallery = (LinearLayout) dialogView.findViewById(R.id.linear_open_gallery);
                LinearLayout linear_camera = (LinearLayout) dialogView.findViewById(R.id.linear_open_camera);
                TextView cancel_dialog = (TextView) dialogView.findViewById(R.id.textViewCancel);

                final AlertDialog dialog = builder.create();

                //Handle click events
                linear_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openGallery();
                        dialog.dismiss();
                    }
                });

                linear_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCamera();
                        dialog.dismiss();
                    }
                });

                cancel_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    //recording start void
   /* private void startRecording() {
        //dinh dang thoi gian luu audio vao bo nho thiet bi
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
        String nameAudio = dateFormat.format(new Date());
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + sharedHelper.getString(Constants.USERNAME) + "_" + nameAudio + ".3gp";

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
    }

    //recording stop void
    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        sendFileAudioFirebase();
    }*/

    //open gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_GALLERY_REQUEST);
    }

    //open camera
    private void openCamera() {
        //dinh dang thoi gian luu anh vao bo nho thiet bi
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
        String nomeFoto = dateFormat.format(new Date());
        filePathImageCamera = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), sharedHelper.getString(Constants.USERNAME) + "_" + nomeFoto + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Uri fileUri = Uri.fromFile(filePathImageCamera);

        Uri fileUri = FileProvider.getUriForFile(
                getContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                filePathImageCamera);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, IMAGE_CAMERA_REQUEST);
    }

    //send message on firebase
    private void sendMessageFirebase() {
        if (!edtEmojIconMessage.getText().toString().equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateModified = dateFormat.format(new Date());

            ChatModel model = new ChatModel();
            model.setSenderId(sharedHelper.getString(Constants.USERNAME));
            model.setDate(dateModified);
            model.setText(edtEmojIconMessage.getText().toString());
            model.setRead(false);
            model.setFile(null);
            mDatabaseRef.child(CHAT_REF).child(userModel.getAdminId() + "_" + sharedHelper.getString(Constants.USERNAME)).push().setValue(model);

            sendPushNotificationToReceiver("message", model.getText(), model.getSenderId(), String.valueOf(userModel.getAdminId()), model.getDate());

            edtEmojIconMessage.setText("");
        } else {
            Toast.makeText(
                    getActivity(),
                    getResources().getString(R.string.msg_error_chat),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    //send audio on firebase
/*    private void sendFileAudioFirebase() {
        //dinh dang thoi gian luu audio len firebase
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
        String nameAudio = dateFormat.format(new Date());
        String nameStorage = sharedHelper.getString(Constants.USERNAME) + "_" + nameAudio + ".3gp";
        StorageReference filepath = mStorageRef.child(Constants.CHAT_AUDIO_REF).child(userModel.getAdminId() + "_" + sharedHelper.getString(Constants.USERNAME)).child(nameStorage);
        Uri uri = Uri.fromFile(new File(mFileName));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //dinh dang thoi gian gui len firebase
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateModified = dateFormat.format(new Date());

                //dinh dang ten file gui len firebase
                DateFormat dateFormatFile = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
                String nameAudio = dateFormatFile.format(new Date());

                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                FileModel fileModel = new FileModel(downloadUrl.toString(), sharedHelper.getString(Constants.USERNAME) + "_" + nameAudio + ".3gp");

                ChatModel model = new ChatModel();
                model.setSenderId(sharedHelper.getString(Constants.USERNAME));
                model.setDate(dateModified);
                model.setText("audio");
                model.setRead(false);
                model.setFile(fileModel);
                mDatabaseRef.child(CHAT_REF).child(userModel.getAdminId() + "_" + sharedHelper.getString(Constants.USERNAME)).push().setValue(model);
            }
        });
    }*/

    //send image on firebase (gallary or camera)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mStorageRef = mStorage.getReferenceFromUrl(Constants.STORAGE_URL).child(Constants.CHAT_IMAGE_REF).child(userModel.getAdminId() + "_" + sharedHelper.getString(Constants.USERNAME));
        if (requestCode == IMAGE_GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                //compress image galerry
                Uri selectedImageUri = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, filePath, null, null, null);
                cursor.moveToFirst();
                String newImage = cursor.getString(cursor.getColumnIndex(filePath[0]));
                String imagePath = resizeImage.compressImage(newImage);
                Uri upImage = Uri.fromFile(new File(imagePath));
                if (upImage != null) {
                    sendFileGalleryFirebase(mStorageRef, upImage);
                } else {
                    //URI IS NULL
                }
            }
        } else if (requestCode == IMAGE_CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (filePathImageCamera != null && filePathImageCamera.exists()) {

                    //compress image camera
                    String filePath = filePathImageCamera.getPath();
                    String imagePath = resizeImage.compressImage(filePath);
                    File upImage = new File(imagePath);

                    sendFileCameraFirebase(mStorageRef, upImage);
                } else {
                    //IS NULL
                }
            }
        }
    }

    //send file from gallery on firebase
    private void sendFileGalleryFirebase(StorageReference storageReference, final Uri file) {
        if (storageReference != null) {
            //dinh dang time len firebase storage
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
            Date date = new Date();
            String name = dateFormat.format(date);
            StorageReference imageGalleryRef = storageReference.child(sharedHelper.getString(Constants.USERNAME) + "_" + name + ".jpg");
            UploadTask uploadTask = imageGalleryRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //dinh dang thoi gian gui len firebase
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String dateModified = dateFormat.format(new Date());

                    //dinh dang ten file gui len firebase
                    DateFormat dateFormatFile = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
                    String nameFile = dateFormatFile.format(new Date());

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    FileModel fileModel = new FileModel(downloadUrl.toString(), sharedHelper.getString(Constants.USERNAME) + "_" + nameFile + ".jpg");
                    ChatModel model = new ChatModel();
                    model.setSenderId(sharedHelper.getString(Constants.USERNAME));
                    model.setDate(dateModified);
                    model.setText("image");
                    model.setRead(false);
                    model.setFile(fileModel);
                    mDatabaseRef.child(CHAT_REF).child(userModel.getAdminId() + "_" + sharedHelper.getString(Constants.USERNAME)).push().setValue(model);
                    sendPushNotificationToReceiver("image", model.getText(), model.getSenderId(),
                            String.valueOf(userModel.getAdminId()), model.getDate());
                }
            });
        } else {
            //IS NULL
        }
    }

    //send file from camera on firebase
    private void sendFileCameraFirebase(StorageReference storageReference, final File file) {
        if (storageReference != null) {
            //dinh dang time len firebase storage
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
            Date date = new Date();
            String name = dateFormat.format(date);

            StorageReference imageGalleryRef = storageReference.child(sharedHelper.getString(Constants.USERNAME) + "_" + name + ".jpg");

            UploadTask uploadTask = imageGalleryRef.putFile(Uri.fromFile(file));
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //dinh dang thoi gian gui len firebase
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String dateModified = dateFormat.format(new Date());

                    //dinh dang ten file gui len firebase
                    DateFormat dateFormatFile = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
                    String nameFile = dateFormatFile.format(new Date());

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    FileModel fileModel = new FileModel(downloadUrl.toString(), sharedHelper.getString(Constants.USERNAME) + "_" + nameFile + ".jpg");

                    ChatModel model = new ChatModel();
                    model.setSenderId(sharedHelper.getString(Constants.USERNAME));
                    model.setDate(dateModified);
                    model.setText("image");
                    model.setRead(false);
                    model.setFile(fileModel);
                    mDatabaseRef.child(CHAT_REF).child(userModel.getAdminId() + "_" + sharedHelper.getString(Constants.USERNAME)).push().setValue(model);
                    sendPushNotificationToReceiver("image", model.getText(), model.getSenderId(), String.valueOf(userModel.getAdminId()), model.getDate());
                }
            });
        } else {
            //IS NULL
        }
    }

    //receive message from firebase
    private void receiveMsgFirebase() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
//        mLinearLayoutManager.setReverseLayout(true);

        Query query = mDatabaseRef.child(Constants.CHAT_REF).child(userModel.getAdminId() + "_" + sharedHelper.getString(Constants.USERNAME));

        chatAdapter = new ChatAdapter(getActivity(), sharedHelper.getString(Constants.USERNAME), query);
        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMsgCount = chatAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMsgCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    rvListMessage.scrollToPosition(positionStart);
                }
            }
        });

        rvListMessage.setLayoutManager(mLinearLayoutManager);
        rvListMessage.setAdapter(chatAdapter);
/*        rvListMessage.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Logger.enter();
                Toast.makeText(getActivity(), "Load More", Toast.LENGTH_SHORT).show();
                chatAdapter.more();
                Logger.exit();
            }
        });*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatAdapter != null) {
            chatAdapter.cleanup();
        }
    }

    //push thong bao cho mannager app
    private void sendPushNotificationToReceiver(String title, String message, String senderId, String receiverId, String time) {
        FcmNotificationBuilder.initialize()
                .title(title)
                .message(message)
                .sender(senderId)
                .topic(receiverId)
                .time(time)
                .send();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Logger.enter();
        mProgressBar.setVisibility(View.GONE);
        rvListMessage.setVisibility(View.VISIBLE);
        Logger.exit();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Logger.enter();
        Logger.exit();
    }
}