package com.example.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val RESULT1 = "Result #1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // on click lister to a button
        // when pressed the coroutine will be executed
        button.setOnClickListener{

            // launch coroutine
            // 3 possible coroutine scopes
            // IO, Main, Default(heavy computational work)
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }

    }

    // this function will be called on the main thread
    // in order to update a text view since a UI
    // component cannot be update from within a coroutine
    private fun setNewText(input: String){
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String){
        // withContext allows us to specifiy which
        // thread the code in the brackets will be
        // executed on.
        // the setting of the text string to the text view
        // must be done on the main thread
        withContext(Main){
            setNewText(input)
        }
    }

    private suspend fun fakeApiRequest(){
        val result1 = getResult1FromApi()
        println("debus: $result1")
        setTextOnMainThread(result1)
        // doing
        // text.text = result1
        // will cause app to crash
        // results from a coroutine cannot directly
        // update UI components
    }

    // marked as suspend because it will be used
    // within a coroutine
    private suspend fun getResult1FromApi(): String{
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT1
    }

    // will print the Thread on which the coroutine
    // is being executed
    private fun logThread(methodName: String){
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }

}

