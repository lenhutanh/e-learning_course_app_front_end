package com.example.e_learningcourse.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_learningcourse.R;
import com.example.e_learningcourse.databinding.ItemCourseBinding;
import com.example.e_learningcourse.databinding.ItemCourseShimmerBinding;
import com.example.e_learningcourse.model.response.CourseDetailResponse;
import com.example.e_learningcourse.ui.course.CourseDetailsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_SHIMMER = 1;

    private List<CourseDetailResponse> courses;
    private OnBookmarkClickListener bookmarkClickListener;
    private Context context;
    private boolean showShimmer = false;

    public CourseAdapter(Context context) {
        this.context = context;
        this.courses = new ArrayList<>();
    }

    public interface OnBookmarkClickListener {
        void onBookmarkClick(CourseDetailResponse course, int position);
    }

    public CourseAdapter(List<CourseDetailResponse> courses) {
        this.courses = courses != null ? courses : new ArrayList<>();
    }

    public void setOnBookmarkClickListener(OnBookmarkClickListener listener) {
        this.bookmarkClickListener = listener;
    }

    public void addCourses(List<CourseDetailResponse> newCourses) {
        int oldSize = courses.size();
        courses.addAll(newCourses);
        notifyItemRangeInserted(oldSize, newCourses.size());
    }

    public void setCourses(List<CourseDetailResponse> courses) {
        this.courses = courses != null ? courses : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void showShimmer(boolean show) {
        this.showShimmer = show;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return showShimmer ? VIEW_TYPE_SHIMMER : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SHIMMER) {
            ItemCourseShimmerBinding binding = ItemCourseShimmerBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new ShimmerViewHolder(binding);
        } else {
            ItemCourseBinding binding = ItemCourseBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new CourseViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CourseViewHolder) {
            CourseDetailResponse course = courses.get(position);
            ((CourseViewHolder) holder).bind(course, position);
        } else if (holder instanceof ShimmerViewHolder) {
            ((ShimmerViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return showShimmer ? 10 : courses.size();
    }

    public void updateCourse(int position) {
        if (position >= 0 && position < courses.size()) {
            notifyItemChanged(position);
        }
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final ItemCourseBinding binding;

        public CourseViewHolder(ItemCourseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CourseDetailResponse course, int position) {
            binding.tvTitle.setText(course.getCourseName());
            binding.tvInstructor.setText("huy");
            binding.tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", course.getCoursePrice()));
            Glide.with(context)
                .load(course.getCourseImg())
                //.placeholder(R.drawable.ic_placeholder)
                .into(binding.ivThumbnail);
            if (course.isBestSeller()) {
                binding.tvBestSeller.setVisibility(View.VISIBLE);
            } else {
                binding.tvBestSeller.setVisibility(View.GONE);
            }
            binding.ivInstructorAvatar.setImageResource(R.drawable.avatar);

            binding.btnBookmark.setImageResource(
                course.isBookmarked() ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline
            );

            binding.btnBookmark.setOnClickListener(v -> {
                if (bookmarkClickListener != null) {
                    bookmarkClickListener.onBookmarkClick(course, position);
                }
            });

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), CourseDetailsActivity.class);
                v.getContext().startActivity(intent);
            });
        }
    }

    class ShimmerViewHolder extends RecyclerView.ViewHolder {
        private final ItemCourseShimmerBinding binding;

        public ShimmerViewHolder(ItemCourseShimmerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            binding.shimmerThumbnail.startShimmer();
            binding.shimmerTitle.startShimmer();
            binding.shimmerAvatar.startShimmer();
            binding.shimmerInstructor.startShimmer();
            binding.shimmerPrice.startShimmer();
        }
    }
} 