package com.my.emogi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.my.emogi.model.Assets
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val TAG="MainActivity"

    var tagMap : HashMap<String, ArrayList<Assets>> ? = null

    val result: ArrayList<Result> = ArrayList()

    private var handler :Handler =object : Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if(msg?.what == Constants.MSG_PARSE_JSON){
                Log.i(TAG,"json parse finish")
                tagMap=msg.data.getSerializable(Constants.MSG_PARSE_JSON_DATA) as HashMap<String, ArrayList<Assets>> ?
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(TAG,"OnCreate")
        ParseJsonThread(this,handler).start()

        recycler.layoutManager = GridLayoutManager(this, 2)

        recycler.adapter = RecyclerAdapter(result, this)

        edit_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                s?.let{checkWord(s.toString())}
            }
        })
    }

    private fun checkWord(s: String?)
    {
        var str=s.toString()
        var lines=str.split("\n")
        Log.i(TAG,"search start")
        result.clear()
        for (line in lines) {
            var words=line.split(" ")
            for (word in words)
                searchInMap(word.toLowerCase())

        }
        recycler.adapter.notifyDataSetChanged()

        Log.i(TAG,"search finish")
    }
    private fun searchInMap(word: String)
    {
        val map  = tagMap
        map?.let{
            var asset=map.get(word)
            var new_word=word
            while (asset==null && new_word.length>0){
                if (!Character.isLetter(new_word.get(new_word.lastIndex)))
                    new_word=new_word.substring(0,new_word.lastIndex)
                else if (!Character.isLetter(new_word.get(0)))
                    new_word=new_word.substring(1,new_word.lastIndex)
                else
                    return
                asset=map.get(new_word)
            }
            Log.i(TAG, new_word+" "+asset?.get(0)?.url)
            result.add(Result(new_word,asset?.get(0)?.url))
        }
    }



}

