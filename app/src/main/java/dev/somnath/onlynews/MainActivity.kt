package dev.somnath.onlynews

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.PendingIntent.getActivity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.somnath.onlynews.databinding.ActivityMainBinding
import dev.somnath.onlynews.ui.viewmodels.ArticlesViewModel
import dev.somnath.onlynews.utils.NetworkObserver

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding


    private val viewModel: ArticlesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.bottomNavView.setupWithNavController(findNavController(R.id.fragment))


        observeUserNetworkConnection()

    }




    fun toEndTransition(){
        binding.motionLayout.transitionToEnd()
    }

    fun toStartTransition(){
        binding.motionLayout.transitionToStart()
    }




    private fun observeUserNetworkConnection() {

        NetworkObserver.getNetworkLiveData(applicationContext)
            .observe(this, androidx.lifecycle.Observer { isConnected ->



                if (!isConnected) {

                    Log.i(TAG, "observeUserNetworkConnection: !isConnected")
                    
                    binding.textViewNetworkStatus.text = "No internet connection"
                    binding.networkStatusLayout.apply {
                        binding.networkStatusLayout.visibility = View.VISIBLE
                        setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                android.R.color.holo_red_light
                            )
                        )
                    }
                } else {



                    binding.textViewNetworkStatus.text = "Back Online"

                    viewModel.getFeedArticles()

                    Log.i(TAG, "observeUserNetworkConnection: isConnected")
                    binding.networkStatusLayout.apply {
                        animate()
                            .alpha(1f)
                            .setStartDelay(1000)
                            .setDuration(1000)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    binding.networkStatusLayout.visibility = View.GONE
                                }
                            }).start()
                        setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.green
                            )
                        )
                    }
                }
            })
    }







}