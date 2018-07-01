package com.example.just.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.just.Avtivity.TulingActivity;
import com.example.just.Bean.Chat;
import com.example.just.R;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;

import java.util.List;


/**
 * Created by yg on 18-6-30.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{
    //    上下文
    private Context context;

    //    对话列表
    private List<Chat> mlist;

    public ChatAdapter() {

    }

    public ChatAdapter(Context context, List<Chat> list) {
        this.context = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat chat = mlist.get(position);
        if (chat.getType() == Chat.TYPE_RECEIVED) {
//           如果收的的数据是左边，就显示左边的消息布局，将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftChat.setText(chat.getText());
//
        } else if (chat.getType() == chat.TYPE_SENT) {
//           如果发出的消息是右边，就显示右边的消息布局，将左边的消息布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightChat.setText(chat.getText());
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    /**
     * 声明控件
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftChat;
        TextView rightChat;
        ImageView bofang_left;
        ImageView bofang_right;

        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout = itemView.findViewById(R.id.left_layout);
            rightLayout = itemView.findViewById(R.id.right_layout);
            leftChat = itemView.findViewById(R.id.tv_left_text);
            rightChat = itemView.findViewById(R.id.tv_right_text);
            bofang_left = itemView.findViewById(R.id.bofang_left);
            bofang_right = itemView.findViewById(R.id.bofang_right);
            bofang_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    speekText(leftChat.getText().toString());
                }
            });
            bofang_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    speekText(rightChat.getText().toString());
                }
            });
        }
        //createSynthesizer方法的context，要从itemView中获取，不能用Activity.this
        public void speekText(String text) {
            SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer( itemView.getContext(), null);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "vixyun"); // 设置发音人
            mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
            mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围 0~100
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
            mTts.setParameter(SpeechConstant. TTS_AUDIO_PATH, "./sdcard/iflytek.pcm" );
            mTts.startSpeaking( text, new TulingActivity.MySynthesizerListener()) ;
        }
    }

}
