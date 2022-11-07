package com.example.subway_alarm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.BtnLineNumberBinding
import com.example.subway_alarm.model.Station


class LineNumAdapter(val lineNumbers: Array<Int>): RecyclerView.Adapter<LineNumAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineNumAdapter.Holder {
        val binding = BtnLineNumberBinding.inflate(LayoutInflater.from(parent.context))
        val holder: Holder = Holder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(lineNumbers[position])
    }


    override fun getItemCount(): Int {
        return lineNumbers.size
    }

    class Holder(private val binding: BtnLineNumberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lineNum: Int) {
            when(lineNum) {
                1 -> binding.btnLineNum.setImageResource(R.drawable.line1)
                2 -> binding.btnLineNum.setImageResource(R.drawable.line2)
                3 -> binding.btnLineNum.setImageResource(R.drawable.line3)
                4 -> binding.btnLineNum.setImageResource(R.drawable.line4)
                5 -> binding.btnLineNum.setImageResource(R.drawable.line5)
                6 -> binding.btnLineNum.setImageResource(R.drawable.line6)
                7 -> binding.btnLineNum.setImageResource(R.drawable.line7)
                8 -> binding.btnLineNum.setImageResource(R.drawable.line8)
                9 -> binding.btnLineNum.setImageResource(R.drawable.line9)
                10 -> binding.btnLineNum.setImageResource(R.drawable.line10)
                11 -> binding.btnLineNum.setImageResource(R.drawable.line11)
                12 -> binding.btnLineNum.setImageResource(R.drawable.line12)
                13 -> binding.btnLineNum.setImageResource(R.drawable.line13)
                14 -> binding.btnLineNum.setImageResource(R.drawable.line14)
                15 -> binding.btnLineNum.setImageResource(R.drawable.line15)
                16 -> binding.btnLineNum.setImageResource(R.drawable.line16)

            }
        }

    }


}