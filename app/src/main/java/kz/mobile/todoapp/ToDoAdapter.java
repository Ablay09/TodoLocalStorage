package kz.mobile.todoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToDoAdapter
        extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private List<ToDo> toDoList = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    public ToDoAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_to_do, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        holder.bind(toDoList.get(position));
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public void addAll(List<ToDo> list) {
        toDoList.clear();
        toDoList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(ToDo item) {
        toDoList.add(0, item);
        notifyItemInserted(0);
    }

    public void updateItem(int position, ToDo item) {
        toDoList.set(position, item);
        notifyItemChanged(position);
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewDescription;
        private TextView textViewDate;

        ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }

        public void bind(final ToDo item) {
            textViewName.setText(item.getName());
            textViewDescription.setText(item.getDescription());
            textViewDate.setText(item.getDate().toString());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition(), item);
                    }
                 }
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position, ToDo item);
    }
}
