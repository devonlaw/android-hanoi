package com.example.androidtest

import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View

class gameActivity : AppCompatActivity() {

    var stack1 = mutableListOf<View>()
    var stack2 = mutableListOf<View>()
    var stack3 = mutableListOf<View>()
    var isSelected = false
    private lateinit var frame1: View
    private lateinit var frame2: View
    private lateinit var frame3: View
    private lateinit var selectedBlock: View
    private var blockX: Float = 0f
    private lateinit var previous: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val block1 = findViewById<View>(R.id.block1)
        val block2 = findViewById<View>(R.id.block2)
        val block3 = findViewById<View>(R.id.block3)
        val block4 = findViewById<View>(R.id.block4)
        val block5 = findViewById<View>(R.id.block5)
        frame1 = findViewById(R.id.frame1)
        frame2 = findViewById(R.id.frame2)
        frame3 = findViewById(R.id.frame3)
        stack1.add(block1)
        stack1.add(block2)
        stack1.add(block3)
        stack1.add(block4)
        stack1.add(block5)
        selectedBlock = stack1.first()
    }

    fun main(view: View) {
        if (isSelected) {
            //TODO: if the top block in a stack has been selected
            findBlockX(view)
            previous = originalFrame(selectedBlock)
            if (validateMove(view)){
                moveBlock(selectedBlock)
                addToStack(view)
            }
            deselectBlock()
            isSelected = false

        } else {
            //TODO: if no block is selected
            findTopOfStack(view)
        }
    }

    private fun findTopOfStack(view: View) {
        if (view == frame1 && !stack1.isEmpty()) {
            selectedBlock = stack1.last()
            isSelected = true
            selectedBlock.setBackgroundColor(resources.getColor(R.color.selected))
        } else if (view == frame2 && !stack2.isEmpty()) {
            selectedBlock = stack2.last()
            isSelected = true
            selectedBlock.setBackgroundColor(resources.getColor(R.color.selected))
        } else if (view == frame3 && !stack3.isEmpty()) {
            selectedBlock = stack3.last()
            isSelected = true
            selectedBlock.setBackgroundColor(resources.getColor(R.color.selected))
        }

    }

    private fun deselectBlock() {
        selectedBlock.setBackgroundColor(resources.getColor(R.color.grey))
    }

    private fun moveBlock(view: View) {
        removeTopOfStack(previous)
        ObjectAnimator.ofFloat(view, "translationX", blockX).apply {
            duration = 200
            start()
        }
    }

    private fun findBlockX(view: View) {
        blockX = view.x
    }

    private fun removeTopOfStack(view: View) {
        if (view == frame1 && !stack1.isEmpty()) {
            stack1.remove(selectedBlock)
        } else if (view == frame2 && !stack2.isEmpty()) {
            stack2.remove(selectedBlock)
        } else if (view == frame3 && !stack3.isEmpty()) {
            stack3.remove(selectedBlock)
        }
    }

    private fun addToStack(view: View) {
        if (view == frame1) {
            stack1.add(selectedBlock)
        } else if (view == frame2) {
            stack2.add(selectedBlock)
        } else if (view == frame3) {
            stack3.add(selectedBlock)
        }
    }

    private fun validateMove(view: View): Boolean {
        if (view == frame1 && !stack1.isEmpty()) {
            if (selectedBlock.width > stack1.last().width) {
                return false
            }
        } else if (view == frame2 && !stack2.isEmpty()) {
            if (selectedBlock.width > stack2.last().width) {
                return false
            }
        } else if (view == frame3 && !stack3.isEmpty()) {
            if (selectedBlock.width > stack3.last().width) {
                return false
            }
        }
        return true
    }

    private fun originalFrame(view: View): View {
        if (view.x == frame1.x) {
            return frame1
        } else if (view.x == frame2.x) {
            return frame2
        } else {
            return frame3
        }
    }

}
