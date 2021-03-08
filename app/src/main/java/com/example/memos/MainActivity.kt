package com.example.memos

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.update_popup.*

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity(), OnDeleteListner, OnDeleteCheckListner, OnUpdateCheckPopup {

    //나중에 초기화
    lateinit var db : MemoDatabase
    var memoList : List<MemoEntity> = listOf<MemoEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = MemoDatabase.getInstance(this)!!

        addButton.setOnClickListener {
            //addButton 클릭이벤트 발생시
            val message : String? = editText.text.toString()
//            Toast.makeText(applicationContext, editText.text.toString().trim(), Toast.LENGTH_SHORT).show()

            if (message != null) {
                if(message.trim() == ""){//trim() 공백 삭제
                    //값이 없을경우
                    Toast.makeText(applicationContext, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                    editText.setText("")
                }else{
                    //널이 아닌 경우
                    val memo = MemoEntity(null, editText.text.toString())
                    //입력 후 ""으로 초기화
                    editText.setText("")
                    InsertMemo(memo)
                    Toast.makeText(applicationContext, "입력이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        getAllMemos()//시작 시 모든 항목 세팅
    }

    //1. Insert Data
    fun InsertMemo(memo : MemoEntity){
        //1. MainThread vs WorkerThread(Background Thread)
        //모든 행동은 메인, 데이터통신은 워커
        //Coroutine을 이용해 쓰레드 관리
        //이번에는 AsyncTask를 사용
        val insertTask = object : AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                //워커쓰레드에서 할 일
                db.memoDAO().insert(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                //Insert 후 모든 메모를 불러옴.
                getAllMemos()
            }
        }
        //실행
        insertTask.execute()
    }
    //2. Get Data
    fun getAllMemos(){
        val getTask = object : AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                memoList = db.memoDAO().getAll()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(memoList)
            }
        }

        getTask.execute()
    }
    //3. Delete Data
    fun deleteMemo(memo : MemoEntity){
        val deleteTask = object : AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                db.memoDAO().delete(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }
        deleteTask.execute()
    }

    //+, UPDATE
    fun updateMemo(memo : MemoEntity){
        val updateTask = object : AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                db.memoDAO().update(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }
        updateTask.execute()
    }

    //4. Set RecyclerView
    fun setRecyclerView(memoList : List<MemoEntity>){
        recyclerView.adapter = MyAdapter(this, memoList, this, this)
    }

    override fun onDeleteListner(memo: MemoEntity) {
        deleteMemo(memo)
    }

    //삭제 확인 Popup
    @SuppressLint("ServiceCast")
    override fun deleteCheckPopup(memo: MemoEntity) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflater.inflate(R.layout.activity_alter__popup, null)
//        val view2 = inflater.inflate(R.layout.update_popup, null)
        val textView : TextView = view.findViewById(R.id.deleteCheckMessage)
        textView.text = "delete?"

        val alertDialog = AlertDialog.Builder(this).setTitle("Delete Check").create()
        val alertDialog2 = AlertDialog.Builder(this).setTitle("Update Check").create()

        val deleteButton = view.findViewById<Button>(R.id.deleteCheckButton)
        //delete버튼 클릭시
        deleteButton.setOnClickListener{
            Toast.makeText(applicationContext, "delete ok", Toast.LENGTH_SHORT).show()
            deleteMemo(memo)
            alertDialog.dismiss()
        }
        
        //update버튼 클릭시
        val updateButton = view.findViewById<Button>(R.id.updateButton)
        updateButton.setOnClickListener(){
            Toast.makeText(applicationContext, "업데이트누름", Toast.LENGTH_SHORT).show()
//            Toast.makeText(applicationContext, "${memo.id}", Toast.LENGTH_SHORT).show()
            view = inflater.inflate(R.layout.update_popup, null)
            alertDialog2.setView(view)
            alertDialog2.show()
            alertDialog.dismiss()
            updateCheckPopup(memo, memo.id)
            alertDialog2.dismiss()
        }
//
//        //updateSubmit버튼 클릭시
//        val updateSubmit = view.findViewById<Button>(R.id.updateEdit)
//        updateSubmit.setOnClickListener(){
//            Toast.makeText(applicationContext, "업뎃완료", Toast.LENGTH_SHORT).show()
//
//        }

        val deleteCancel = view.findViewById<Button>(R.id.deleteCancelButton)
        //cancel버튼 클릭시
        deleteCancel.setOnClickListener(){
            Toast.makeText(applicationContext, "delete canceled", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

//        val updateCancel = view.findViewById<Button>(R.id.updateCancelButton)
//        //cancel버튼 클릭시
//        updateCancel.setOnClickListener(){
//            Toast.makeText(applicationContext, "update canceled", Toast.LENGTH_SHORT).show()
//            alertDialog.dismiss()
//        }
        alertDialog.setView(view)
        alertDialog.show()
    }

//    fun getMemoById(memo : MemoEntity){
//        val getTask = object : AsyncTask<Unit,Unit,Unit>(){
//            override fun doInBackground(vararg params: Unit?) {
//                newMemo = db.memoDAO().getData(memo)
//            }
//
//            override fun onPostExecute(result: Unit?) {
//                super.onPostExecute(result)
//            }
//        }
//
//        getTask.execute()
//    }

    override fun updateCheckPopup(memo : MemoEntity, id : Int?) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = inflater.inflate(R.layout.update_popup, null)

        val alertDialog = AlertDialog.Builder(this).setTitle("Update Check").create()
        val updateSubmitButton = view.findViewById<Button>(R.id.updateSubmitButton)
        //update버튼 클릭시
        updateSubmitButton.setOnClickListener{
            Toast.makeText(applicationContext, "update ok", Toast.LENGTH_SHORT).show()
//            Toast.makeText(applicationContext, updateEdit.text.toString(), Toast.LENGTH_SHORT).show()
//            val message = updateEdit.text.toString() //alertDialog(팝업창이므로)에서 바로 찾을 수 없음
            val message = view.findViewById<EditText>(R.id.updateEdit)//뷰를통해 EditView찾아서
            val memo = MemoEntity(id, message.text.toString())//사용.
            updateMemo(memo)
            alertDialog.dismiss()
        }
        val updateCancelButton = view.findViewById<Button>(R.id.updateCancelButton)
        //cancel버튼 클릭시
        updateCancelButton.setOnClickListener{
            Toast.makeText(applicationContext, "update canceled", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }
}