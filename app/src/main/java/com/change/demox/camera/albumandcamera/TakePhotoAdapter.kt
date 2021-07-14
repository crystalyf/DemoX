package com.change.demox.camera.albumandcamera

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.change.demox.R
import com.change.demox.camera.part.Media
import com.change.demox.camera.part.PhotoStatue
import com.change.demox.databinding.ItemCameraPhotoShowBinding
import com.change.demox.utils.GlideUtils


/**
 * Camera2 照相显示的Adapter
 *
 */
class TakePhotoAdapter(
        private var context: Context,
        private var viewModel: CameraAndShowViewModel?,
        private var list: MutableList<Media>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageLayoutHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_camera_photo_show,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageLayoutHolder) {
            holder.bind(viewModel, position, list!!, context)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun setDataList() {
        this.list = viewModel?.photos?.value
        notifyDataSetChanged()
    }

    class ImageLayoutHolder(private val binding: ItemCameraPhotoShowBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(
                viewModel: CameraAndShowViewModel?,
                position: Int,
                list: MutableList<Media>, context: Context
        ) {
            val data = list[position]
            binding.position = position
            when (data.photoStatue) {
                PhotoStatue.TAKE_FINISH.ordinal -> {
                    binding.iv.visibility = View.GONE
                    binding.loadImage.visibility = View.VISIBLE
                    GlideUtils.loadWithCenterCropTransformAutoIndicator(binding.loadImage, data.uri)
                }
                PhotoStatue.TAKE_CAMERA.ordinal -> {
                    binding.iv.visibility = View.VISIBLE
                    binding.loadImage.visibility = View.GONE
                    binding.itemLayout.setBackgroundColor(
                            ContextCompat.getColor(
                                    context,
                                    R.color.white
                            )
                    )
                    binding.iv.setImageResource(R.drawable.ic_baseline_photo_camera)
                }

                PhotoStatue.TAKE_EMPTY.ordinal -> {
                    binding.iv.setImageDrawable(null)
                    binding.itemLayout.setBackgroundColor(
                            ContextCompat.getColor(
                                    context,
                                    R.color.gray_c4
                            )
                    )
                    binding.iv.visibility = View.VISIBLE
                    binding.loadImage.visibility = View.GONE
                }
            }
            binding.executePendingBindings()
        }
    }
}
