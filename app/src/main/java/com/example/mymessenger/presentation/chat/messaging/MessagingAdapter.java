package com.example.mymessenger.presentation.chat.messaging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.SelectItemsListener;
import com.example.mymessenger.models.Message;
import com.example.mymessenger.presentation.AdapterSelect;
import com.example.mymessenger.presentation.OnItemListClickListener;
import com.example.mymessenger.presentation.OnMessageClick;
import com.example.mymessenger.presentation.chat.ChatsRecycleViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessagingAdapter extends AdapterSelect {

    List<Message> messages;

    OnMessageClick listener;

    SelectItemsListener selectItemsListener;

    boolean isPrivateChannel;

    public MessagingAdapter(OnMessageClick listener,
                            SelectItemsListener selectItemsListener,
                            boolean isPrivateChannel) {
        this.isPrivateChannel = isPrivateChannel;
        this.listener = listener;
        this.selectItemsListener = selectItemsListener;
    }

    @NonNull
    @Override
    public MessagingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new MessagingViewHolder(view, listener, selectItemsListener, isPrivateChannel);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MessagingViewHolder) holder).bind(messages.get(position));
        ((MessagingViewHolder) holder).selectedOverlay.setVisibility(isSelected(position)
                ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        if (messages == null)
            return 0;
        return messages.size();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        messages.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            messages.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    public String getMessageId(int index) {
        return messages.get(index).getId();
    }

    public String getMessageFileName(int index) {
        return messages.get(index).getContentFile();
    }

    public List<String> getIds(List<Integer> selectedItems) {
        ArrayList<String> result = new ArrayList<>();
        for(Integer pos : selectedItems) {
            result.add(getMessageId(pos));
        }
        return result;
    }
}
