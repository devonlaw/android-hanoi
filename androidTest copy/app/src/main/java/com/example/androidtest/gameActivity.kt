package com.example.androidtest

import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game.*
import java.nio.file.Path

class gameActivity : AppCompatActivity() {

    private var stack1 = mutableListOf<View>()
    private var stack2 = mutableListOf<View>()
    private var stack3 = mutableListOf<View>()
    private var isSelected = false
    private lateinit var frame1: View
    private lateinit var frame2: View
    private lateinit var frame3: View
    private lateinit var selectedBlock: View
    private var blockX: Float = 0f
    private lateinit var previous: View
    private var moveCount = 0
    private var moves = "0"
    private lateinit var textMoves: TextView
    private var height = 0f
    private var setup = true
    private var blockY = 0f
    private var prevStack = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val block1 = findViewById<View>(R.id.block1)
        val block2 = findViewById<View>(R.id.block2)
        val block3 = findViewById<View>(R.id.block3)
        val block4 = findViewById<View>(R.id.block4)
        val block5 = findViewById<View>(R.id.block5)
        textMoves = findViewById(R.id.moveCount)
        frame1 = findViewById(R.id.frame1)
        frame2 = findViewById(R.id.frame2)
        frame3 = findViewById(R.id.frame3)
        stack1.add(block1)
        stack1.add(block2)
        stack1.add(block3)
        stack1.add(block4)
        stack1.add(block5)
        height = stack1.first().height.toFloat()
    }

    fun main(view: View) {
        if (!gameEnd()) {
            if (setup) {
                height = block1.height.toFloat()
                setup = false
            }
            if (isSelected) {
                //TODO: if the top block in a stack has been selected
                findBlockX(view)
                findBlockY(selectedBlock, view)
                if (validateMove(view)) {
                    moveBlock(selectedBlock)
                    addToStack(view)
                    incrementMove()
                    textMoves.text = moves
                } else {
                    addToStack(previous)
                }
                deselectBlock()
                isSelected = false

            } else {
                //TODO: if no block is selected
                findTopOfStack(view)
                previous = view
                removeTopOfStack(view)
            }
        }
        if (stack3.count() == 5) {
            textMoves.text = "Completed in " + moves + "!"
            block1.setBackgroundColor(resources.getColor(R.color.purple))
            block2.setBackgroundColor(resources.getColor(R.color.red))
            block3.setBackgroundColor(resources.getColor(R.color.blue))
            block4.setBackgroundColor(resources.getColor(R.color.green))
            block5.setBackgroundColor(resources.getColor(R.color.orange))
        }
    }

    private fun findTopOfStack(view: View) {
        var last = 0
        if (view == frame1 && !stack1.isEmpty()) {
            last = stack1.lastIndex
            prevStack = stack1.count()
            selectedBlock = stack1.elementAt(last)
            isSelected = true
            selectedBlock.setBackgroundColor(resources.getColor(R.color.selected))
        } else if (view == frame2 && !stack2.isEmpty()) {
            last = stack2.lastIndex
            prevStack = stack2.count()
            selectedBlock = stack2.elementAt(last)
            selectedBlock = stack2.last()
            isSelected = true
            selectedBlock.setBackgroundColor(resources.getColor(R.color.selected))
        } else if (view == frame3 && !stack3.isEmpty()) {
            last = stack3.lastIndex
            prevStack = stack3.count()
            selectedBlock = stack3.elementAt(last)
            isSelected = true
            selectedBlock.setBackgroundColor(resources.getColor(R.color.selected))
        }

    }

    private fun deselectBlock() {
        selectedBlock.setBackgroundColor(resources.getColor(R.color.grey))
    }

    private fun moveBlock(view: View) {
        ObjectAnimator.ofFloat(view, "translationX", blockX).apply {
            duration = 200
            start()
        }

        ObjectAnimator.ofFloat(view, "translationY", blockY).apply {
            duration = 200
            start()
        }

    }

    private fun findBlockX(view: View) {
        blockX = view.x
    }

    private fun findBlockY(view: View, frame: View) {
        when (view) {

            block5 -> {
                when (returnStack(frame).count()) {
                    0 -> blockY = height * 4
                    1 -> blockY = height * 3
                    2 -> blockY = height * 2
                    3 -> blockY = height
                    4 -> blockY = 0f
                }
            }
            block4 -> {
                when(returnStack(frame).count()){
                    0-> blockY = height * 3
                    1 -> blockY = height * 2
                    2 -> blockY = height * 1
                    3 -> blockY = 0f
                }
            }
            block3 -> {
                when(returnStack(frame).count()){
                    0-> blockY = height * 2
                    1 -> blockY = height
                    2 -> blockY = 0f
                }
            }
            block2 -> {
                when(returnStack(frame).count()){
                    0-> blockY = height
                    1 -> blockY = 0f
                }
            }
            block1 -> {
                blockY = 0f
            }
        }
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
        if (view == previous) {
            return false
        } else if (view == frame1 && !stack1.isEmpty()) {
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

    private fun incrementMove() {
        moveCount++
        moves = moveCount.toString()
    }

    private fun gameEnd(): Boolean {
        if (stack3.count() == 5) {
            return true
        }

        return false
    }

    private fun returnStack(view: View): List<View> {
        when (view) {
            frame1 -> return stack1
            frame2 -> return stack2
            frame3 -> return stack3
        }
        System.out.println("didn't return a stack")
        return stack1
    }
}
