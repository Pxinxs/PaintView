package com.tutorial.xyz

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.tutorial.xyz.utils.ImageFile
import com.tutorial.xyz.utils.Permissions
import com.tutorial.xyz.utils.toast
import com.tutorial.xyz.views.PaintView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val REQUEST_WRITE_CODE = 555
    }

    private var exit: Boolean = false

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnColor.setOnClickListener(this)
        llBottomSheetTop.setOnClickListener(this)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
//        bottomSheetBehavior.peekHeight = 300
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    toast("Expand sheet")
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                    toast("Close sheet")
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        sbBrushSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progressValue: Int, fromUser: Boolean) {
                val progress = progressValue + 1
                tvBrushSize.text = progress.toString()

                paintView.setStrokeWidth(progress.toFloat() * 5)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    override fun onClick(view: View) {
        when (view) {
            btnColor -> {
                showColorPicker()
            }
            llBottomSheetTop -> {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//                    toast("Expand sheet")
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                    toast("Close sheet")
                }
            }
        }
    }

    private fun showColorPicker() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
//                .initialColor(R.color.colorPrimaryDark)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener { selectedColor -> }
                .setPositiveButton("ok") { _, selectedColor, _ -> paintView.setBrushColor(selectedColor) }
                .setNegativeButton("cancel") { _, _ -> }
                .build()
                .show()
    }

    private fun getBitmap(container: PaintView): Bitmap {
        container.isDrawingCacheEnabled = true
        container.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(container.drawingCache)
        container.isDrawingCacheEnabled = false
        return bitmap
    }

    private fun takeImage() {
        ImageFile().saveImage(getBitmap(paintView), paintView.height.toFloat(), paintView.width.toFloat())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_save -> {
                if (Permissions().isCameraAndStoragePermissions(this, REQUEST_WRITE_CODE)) {
                    takeImage()
                }
                true
            }
            R.id.nav_delete -> {
                paintView.clear()
                true
            }
            R.id.nav_undo -> {
                paintView.undo()
                true
            }
            R.id.nav_redo -> {
                paintView.redo()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_WRITE_CODE) {
                takeImage()
            }
        }
    }

    override fun onBackPressed() {
        if (exit) moveTaskToBack(true)
        else {
            exit = true
            toast(getString(R.string.press_back_again_to_exit))
            Handler().postDelayed({ exit = false }, (3000).toLong())
        }
    }
}
