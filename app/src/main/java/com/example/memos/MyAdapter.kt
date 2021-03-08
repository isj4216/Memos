package com.example.memos

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_memo.view.*

class MyAdapter(val context: Context,
                var list : List<MemoEntity>,
                var onDeleteListner: OnDeleteListner,
                var onDeleteCheckListner: OnDeleteCheckListner) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        //아이템개수 리턴
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        //아이템을 넣는 틀
        val itenView = LayoutInflater.from(context).inflate(R.layout.item_memo, parent, false)

        return MyViewHolder(itenView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //틀과 넘어온 내용을 합쳐줌(View에 내용을 입력)
        //list = 1, 2, 3...
        val memo = list[position]

        holder.memo.text = memo.memo

        //리싸이클뷰 클릭이벤트.
        holder.itemView.setOnClickListener{
//            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
//            holder.itemView.set
//            holder.itemView.setBackgroundColor(Color.RED)
//            holder.itemView.setBackgroundColor(Color.WHITE)
        }

        //꾹 눌렀을 때 delete
        holder.root.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                onDeleteCheckListner.deleteCheckPopup(memo)
                return true
            }
        })

//        /////////////RecyclerView에 이벤트처리.
//        //(1) 리스트 내 항목 클릭 시 onClick() 호출
//        holder.itemView.setOnClickListener{
//            itemClickListener.onClick(it, position)
//        }
    }
//
        inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        //레이아웃 내 view연결
        val memo = itemView.textViewMemo
        val root = itemView.root
    }
//
//    //(2) 리스너 인터페이스
//    interface OnItemClickListner {
//        fun onClick(v : View, position : Int)
//    }
//
//    //(3) 외부에서 클릭 시 이벤트 설정
//    fun setItemClickListner(onItemClickListner : OnItemClickListner) {
//        this.itemClickListener = onItemClickListner
//    }
//
//    //(4) setItemClickListner로 설정한 함수 실행.
//    private lateinit var itemClickListener : OnItemClickListner
}