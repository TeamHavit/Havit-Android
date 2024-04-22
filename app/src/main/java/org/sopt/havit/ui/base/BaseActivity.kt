package org.sopt.havit.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import org.sopt.havit.ui.system_maintenance.SystemMaintenanceActivity
import org.sopt.havit.util.DialogOkUtil


abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
) : AppCompatActivity() {
    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this
    }

    val systemMaintenanceObserver = Observer<Boolean> { isSystemMaintenance ->
        if (isSystemMaintenance) startSystemMaintenanceActivity()
    }

    private fun startSystemMaintenanceActivity() {
        startActivity(Intent(this, SystemMaintenanceActivity::class.java))
        finish()
    }

    private fun showForcedUpdateNeededDialog() {
        val dialog = DialogOkUtil(DialogOkUtil.FORCED_UPDATE, ::moveToPlayStore, false)
        dialog.show(this.supportFragmentManager, this.javaClass.name)
    }

    fun showForcedUpdateDialogIfNeeded(isForcedUpdateNeeded: Boolean) {
        if (isForcedUpdateNeeded) {
            showForcedUpdateNeededDialog()
        }
    }

    private fun moveToPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = android.net.Uri.parse("market://details?id=org.sopt.havit")
        startActivity(intent)
    }
}

